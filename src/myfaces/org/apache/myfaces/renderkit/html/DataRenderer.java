/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003  The MyFaces Team (http://myfaces.sourceforge.net)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package net.sourceforge.myfaces.renderkit.html;

import net.sourceforge.myfaces.component.CommonComponentAttributes;
import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.renderkit.attr.CommonRendererAttributes;
import net.sourceforge.myfaces.renderkit.attr.DataRendererAttributes;
import net.sourceforge.myfaces.renderkit.callback.CallbackRenderer;
import net.sourceforge.myfaces.renderkit.callback.CallbackSupport;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.Iterator;
import java.util.Collection;
import java.util.Arrays;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class DataRenderer
    extends AbstractPanelRenderer
    implements CommonComponentAttributes,
               CommonRendererAttributes,
               DataRendererAttributes
{
    public static final String ITERATOR_ATTR = DataRenderer.class.getName() + ".ITERATOR";

    public static final String TYPE = "Data";
    public String getRendererType()
    {
        return TYPE;
    }

    public boolean supportsComponentType(String s)
    {
        return s.equals(UIPanel.TYPE);
    }

    public boolean supportsComponentType(UIComponent uicomponent)
    {
        return uicomponent instanceof UIPanel;
    }

    protected void initAttributeDescriptors()
    {
        addAttributeDescriptors(UIPanel.TYPE, TLD_HTML_URI, "panel_data", PANEL_DATA_ATTRIBUTES);
    }

    private static final Object DUMMY = new Object();
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException
    {
        // Init Iterator
        Iterator it = getIterator(facesContext, uiComponent);

        // Set ModelValue VAR_ATTR
        String varAttr = (String)uiComponent.getAttribute(VAR_ATTR);
        facesContext.setModelValue(varAttr, it != null && it.hasNext() ? it.next() : null);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException
    {
        //Remove iterator after last row
         uiComponent.setAttribute(ITERATOR_ATTR, null);
        //Reset ModelValue VAR_ATTR
         facesContext.setModelValue(VAR_ATTR, null);
    }

    public void encodeChildren(FacesContext context, UIComponent component)
        throws IOException
    {
    }

    protected Iterator getIterator(FacesContext facesContext, UIComponent uiComponent)
    {
        Iterator iterator = (Iterator)uiComponent.getAttribute(ITERATOR_ATTR);
        if (iterator == null)
        {
            Object v = uiComponent.currentValue(facesContext);
            if (v instanceof Iterator)
            {
                iterator = (Iterator)v;
            }
            else if (v instanceof Collection)
            {
                iterator = ((Collection)v).iterator();
            }
            else if (v instanceof Object[])
            {
                iterator = Arrays.asList((Object[])v).iterator();
            }
            else if (v instanceof Iterator)
            {
                iterator = (Iterator)v;
            }
            else
            {
                throw new IllegalArgumentException("Value of component " + UIComponentUtils.toString(uiComponent) + " is neither collection nor array.");
            }
            uiComponent.setAttribute(ITERATOR_ATTR, iterator);
        }
        return iterator;
    }

}
