/*
 * Copyright 2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sourceforge.myfaces.application.jsp;

import net.sourceforge.myfaces.webapp.webxml.ServletMapping;
import net.sourceforge.myfaces.webapp.webxml.WebXml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.*;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.4  2004/08/26 15:34:06  manolito
 * trivial cosmetic changes
 *
 * Revision 1.3  2004/08/26 14:25:21  manolito
 * JspTilesViewHandlerInitializer no longer needed, JspTilesViewHandlerImpl is initialized automatically now
 *
 * Revision 1.2  2004/07/19 08:18:19  royalts
 * moved net.sourceforge.myfaces.webapp.webxml and net.sourceforge.util.xml to share src-tree (needed WebXml for JspTilesViewHandlerImpl)
 *
 * Revision 1.1  2004/07/16 17:46:46  royalts
 * moved net.sourceforge.myfaces.webapp.webxml and net.sourceforge.util.xml to share src-tree (needed WebXml for JspTilesViewHandlerImpl)
 *
 */
public class JspTilesViewHandlerImpl
    extends ViewHandler
{
    private ViewHandler _viewHandler;

    private static final Log log = LogFactory.getLog(JspTilesViewHandlerImpl.class);
    private static final String TILES_EXTENSION = ".tiles";
    private static final String TILES_DEF_ATTR = "tiles-definitions";

    private DefinitionsFactory _definitionsFactory;

    public JspTilesViewHandlerImpl(ViewHandler viewHandler)
    {
        _viewHandler = viewHandler;
    }

    public void setDefinitionsFactory(DefinitionsFactory definitionsFactory)
    {
        _definitionsFactory = definitionsFactory;
    }

    public DefinitionsFactory getDefinitionsFactory()
    {
        if (_definitionsFactory == null)
        {
            if (log.isDebugEnabled()) log.debug("JspTilesViewHandlerImpl init");
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            String tilesDefinitions = context.getInitParameter("tiles-definitions");
            if (tilesDefinitions == null)
            {
                log.fatal("No Tiles definition found. Specify Definition files by adding "
                          + TILES_DEF_ATTR + " param in your web.xml");
                return null;
            }

            DefinitionsFactoryConfig factoryConfig = new DefinitionsFactoryConfig();
            factoryConfig.setDefinitionConfigFiles(tilesDefinitions);
            try
            {
                if (log.isDebugEnabled()) log.debug("Reading tiles definitions");
                _definitionsFactory = TilesUtil.createDefinitionsFactory((ServletContext)context.getContext(),
                                                                        factoryConfig);
            }
            catch (DefinitionsFactoryException e)
            {
                log.fatal("Error reading tiles definitions", e);
                return null;
            }
        }
        return _definitionsFactory;
    }

    public void renderView(FacesContext facesContext, UIViewRoot viewToRender) throws IOException, FacesException
    {
        if (viewToRender == null)
        {
            log.fatal("viewToRender must not be null");
            throw new NullPointerException("viewToRender must not be null");
        }

        ExternalContext externalContext = facesContext.getExternalContext();

        String viewId = facesContext.getViewRoot().getViewId();
        ServletMapping servletMapping = getServletMapping(externalContext);
        if (servletMapping.isExtensionMapping())
        {
            String defaultSuffix = externalContext.getInitParameter(ViewHandler.DEFAULT_SUFFIX_PARAM_NAME);
            String suffix = defaultSuffix != null ? defaultSuffix : ViewHandler.DEFAULT_SUFFIX;
            if (!viewId.endsWith(suffix))
            {
                int dot = viewId.lastIndexOf('.');
                if (dot == -1)
                {
                    if (log.isTraceEnabled()) log.trace("Current viewId has no extension, appending default suffix " + suffix);
                    viewId = viewId + suffix;
                }
                else
                {
                    if (log.isTraceEnabled()) log.trace("Replacing extension of current viewId by suffix " + suffix);
                    viewId = viewId.substring(0, dot) + suffix;
                }
                facesContext.getViewRoot().setViewId(viewId);
            }
        }

        if (log.isTraceEnabled()) log.trace("Dispatching to " + viewId);

        String tilesId = viewId;
        int idx = tilesId.indexOf(".");
        if (idx > 0)
        {
            tilesId = tilesId.substring(0, tilesId.indexOf(".")) + TILES_EXTENSION;
        }
        else
        {
            tilesId = tilesId  + TILES_EXTENSION;
        }
        ServletRequest request = (ServletRequest)externalContext.getRequest();
        ServletContext servletContext = (ServletContext)externalContext.getContext();

        ComponentDefinition definition = null;
        try
        {
            definition = getDefinitionsFactory().getDefinition(tilesId, request, servletContext);
            if (definition == null)
            {
                log.error("could not find tiles-definition with name " + tilesId);
                throw new NullPointerException("could not find tiles-definition with name " + tilesId);
            }
        }
        catch (DefinitionsFactoryException e)
        {
            throw new FacesException(e);
        }

        ComponentContext tileContext = ComponentContext.getContext(request);
        if (tileContext == null)
        {
            tileContext = new ComponentContext(definition.getAttributes());
            ComponentContext.setContext(tileContext, request);
        }
        else
        {
            tileContext.addMissing(definition.getAttributes());
        }

        viewId = definition.getPage();
        externalContext.dispatch(viewId);
    }

    private static ServletMapping getServletMapping(ExternalContext externalContext)
    {
        String servletPath = externalContext.getRequestServletPath();
        String requestPathInfo = externalContext.getRequestPathInfo();

        WebXml webxml = WebXml.getWebXml(externalContext);
        List mappings = webxml.getFacesServletMappings();

        boolean isExtensionMapping = requestPathInfo == null;

        for (int i = 0, size = mappings.size(); i < size; i++)
        {
            ServletMapping servletMapping = (ServletMapping) mappings.get(i);
            if (servletMapping.isExtensionMapping() == isExtensionMapping)
            {
                String urlpattern = servletMapping.getUrlPattern();
                if (isExtensionMapping)
                {
                    String extension = urlpattern.substring(1, urlpattern.length());
                    if (servletPath.endsWith(extension))
                    {
                        return servletMapping;
                    }
                }
                else
                {
                    urlpattern = urlpattern.substring(0, urlpattern.length() - 2);
                    // servletPath starts with "/" except in the case where the
                    // request is matched with the "/*" pattern, in which case
                    // it is the empty string (see Servlet Sepc 2.3 SRV4.4)
                    if (servletPath.equals(urlpattern))
                    {
                        return servletMapping;
                    }
                }
            }
        }
        log.error("could not find pathMapping for servletPath = " + servletPath +
                  " requestPathInfo = " + requestPathInfo);
        throw new IllegalArgumentException("could not find pathMapping for servletPath = " + servletPath +
                  " requestPathInfo = " + requestPathInfo);
    }


    public Locale calculateLocale(FacesContext context)
    {
        return _viewHandler.calculateLocale(context);
    }

    public String calculateRenderKitId(FacesContext context)
    {
        return _viewHandler.calculateRenderKitId(context);
    }

    public UIViewRoot createView(FacesContext context, String viewId)
    {
        return _viewHandler.createView(context, viewId);
    }

    public String getActionURL(FacesContext context, String viewId)
    {
        return _viewHandler.getActionURL(context, viewId);
    }

    public String getResourceURL(FacesContext context, String path)
    {
        return _viewHandler.getResourceURL(context, path);
    }

    public UIViewRoot restoreView(FacesContext context, String viewId)
    {
        return _viewHandler.restoreView(context, viewId);
    }

    public void writeState(FacesContext context) throws IOException
    {
        _viewHandler.writeState(context);
    }

}
