/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003, 2004  The MyFaces Team (http://myfaces.sourceforge.net)
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
package net.sourceforge.myfaces.application.cbp;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKitFactory;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.myfaces.cbp.Page;
import net.sourceforge.myfaces.component.ext.Screen;
import net.sourceforge.myfaces.exception.InternalServerException;
import net.sourceforge.myfaces.exception.SmileException;
import net.sourceforge.myfaces.exception.SmileRuntimeException;
import net.sourceforge.myfaces.renderkit.html.HtmlResponseWriterImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The default view handler implementation
 * 
 * @author <a href="mailto:emol@users.sourceforge.net">Edwin Mol</a>
 */
public class ViewHandlerImpl extends ViewHandler {

	private static Log log = LogFactory.getLog(ViewHandlerImpl.class);
	
//	private StateManager stateManager;
	
	/**
	 * Default constructor
	 */
	public ViewHandlerImpl() {
//		stateManager = new StateManagerImpl();
	}
	
	/**
	 * @see javax.faces.application.ViewHandler#renderView(javax.faces.context.FacesContext, javax.faces.component.UIViewRoot)
	 */
	public void renderView(FacesContext ctx, UIViewRoot viewRoot) throws IOException, FacesException {
		// Apparently not all versions of tomcat have the same
		// default content-type.
		// So we'll set it explicitly.
		HttpServletResponse response = (HttpServletResponse) ctx.getExternalContext().getResponse();
		response.setContentType("text/html");
		
		// make sure to set the responsewriter
		initializeResponseWriter(ctx);		
		
		if(viewRoot == null) {
			throw new SmileRuntimeException("No component tree is available !");
		}
		String renderkitId = viewRoot.getRenderKitId();
		if (renderkitId == null) {
			renderkitId = calculateRenderKitId(ctx);
		}
		viewRoot.setRenderKitId(renderkitId);

		ResponseWriter out = ctx.getResponseWriter();
		try {

			out.startDocument();
			renderComponent(ctx.getViewRoot(),ctx);
			out.endDocument();
			ctx.getResponseWriter().flush();

		} catch (RuntimeException e) {
			throw new SmileRuntimeException(e.getMessage(),e);
		}
	}

	/**
	 * @see javax.faces.application.ViewHandler#restoreView(javax.faces.context.FacesContext, java.lang.String)
	 */
	public UIViewRoot restoreView(FacesContext ctx, String viewId) {
//		if (getStateManager().isSavingStateInClient(ctx)) {
//			//get the state from the client
			return getStateManager().restoreView(ctx,viewId, calculateRenderKitId(ctx));
//		} 
//		UIViewRoot currentViewRoot = (UIViewRoot) ctx.getExternalContext().getSessionMap().get(SESSION_KEY_CURRENT_VIEW);
//		if (currentViewRoot == null) {
//			return null;
//		} else if (currentViewRoot.getViewId().equals(viewId)) {
//			return currentViewRoot;
//		}
//		return null;
	}

	/**
	 * @see javax.faces.application.ViewHandler#createView(javax.faces.context.FacesContext, java.lang.String)
	 */
	public UIViewRoot createView(FacesContext ctx, String viewId) {

		UIViewRoot ret = new UIViewRoot();
		ret.setViewId(viewId);
		
		// TODO : Hack to allow unit tests to select empty views
		if(viewId.startsWith("unittesttree.")) {
			return ret;			
		}

		try {
			Class descriptorClazz = getDescriptorClassNameForViewId(viewId);
			if(descriptorClazz == null) { 
				// JSP page....
			} else {
				if(Page.class.isAssignableFrom(descriptorClazz)) {
					Page page = (Page) descriptorClazz.newInstance();
					page.init(ctx,ret);
				} else {
					throw new SmileException("Descriptor Class for '" + viewId + "' must implement net.sourceforge.smile.context.Page !");
				}
			}
		} catch(IllegalAccessException e) {
			throw new SmileException("Please make sure that the default constructor for descriptor class of <" + viewId + "> is public.",e);
		} catch(InstantiationException e) {
			throw new SmileException("An exception was generated by the default constructor of the descriptor class of <" + viewId + ">.",e);
		} catch(Throwable t) {
			throw new SmileException("Descriptor Class for '" + viewId + "' threw an exception during initialize() !",t);
		}

		//set the locale
		ret.setLocale(calculateLocale(ctx));

		//set the view on the session
		ctx.getExternalContext().getSessionMap().put(StateManagerImpl.SESSION_KEY_CURRENT_VIEW,ret);
		
		return ret;
	}

	/**
	 * @see javax.faces.application.ViewHandler#getStateManager()
	 */
	public StateManager getStateManager() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Application application = facesContext.getApplication();
		return application.getStateManager();
	}

	/**
	 * @see javax.faces.application.ViewHandler#writeState(javax.faces.context.FacesContext)
	 */
	public void writeState(FacesContext ctx) throws IOException {
		StateManager.SerializedView serializedView = null;
		if (null != (serializedView = getStateManager().saveSerializedView(ctx))) {
			getStateManager().writeState(ctx,serializedView);
		}
	}

	/**
	 * @see javax.faces.application.ViewHandler#getViewIdPath(javax.faces.context.FacesContext, java.lang.String)
	 */
	public String getViewIdPath(FacesContext ctx, String viewId) {
		// TODO implement conversion
		if (viewId != null && viewId.startsWith("/")) {
			return viewId.substring(1);
		} else {
			return viewId;			
		}
	}

	/**
	 * @see javax.faces.application.ViewHandler#calculateLocale(javax.faces.context.FacesContext)
	 */
	public Locale calculateLocale(FacesContext ctx) {
		Locale result = null;
		for(Enumeration enum = ((ServletRequest)ctx.getExternalContext().getRequest()).getLocales(); enum.hasMoreElements();)
		{
			Locale locale = (Locale)enum.nextElement();
			result = getMatch(ctx, locale);
			if(result != null)
				break;
		}

		if(result == null) {
			if(ctx.getApplication().getDefaultLocale() == null)
				result = Locale.getDefault();
			else
				result = ctx.getApplication().getDefaultLocale();			
		}
		return result;
		
	}
	
	

	/**
	 * make sure a ResponseWriter instance is set on the component
	 */
	private void initializeResponseWriter(FacesContext ctx) throws FacesException {
		//check if running in httpservlet environment
		boolean httpServletEnv = true;
		if (!(ctx.getExternalContext().getRequest() instanceof HttpServletRequest)) {
			throw new InternalServerException("Smile currently does not support environments other than Http Servlet Environment.");
		}
		HttpServletRequest request = (HttpServletRequest) ctx.getExternalContext().getRequest();
		HttpServletResponse response = (HttpServletResponse) ctx.getExternalContext().getResponse();
		String contextType = "text/html";
		String characterEncoding = request.getCharacterEncoding();
		try {
			
			ResponseWriter responseWriter = new HtmlResponseWriterImpl(response.getWriter(),contextType,characterEncoding);
//			ResponseWriter responseWriter = new ResponseWriterImplDecorated(response.getWriter(),contextType,characterEncoding);
			ctx.setResponseWriter(responseWriter);
		} catch (IOException e) {
			throw new FacesException(e.getMessage(),e);
		}		
	}
	
	/**
	 * Recursive operation to render a specific component in the view tree.
	 * 
	 * @param component
	 * @param context
	 */
	private void renderComponent(UIComponent component, FacesContext ctx) {
		try {
			component.encodeBegin(ctx);
			if(component.getRendersChildren()) {
				component.encodeChildren(ctx);
			} else {
				Iterator it;
				UIComponent currentChild;
				it = component.getChildren().iterator();
				while(it.hasNext()) {
					currentChild = (UIComponent) it.next();
					renderComponent(currentChild,ctx);
				}
			}		
			if (component instanceof Screen) {
				writeState(ctx); 
			}
			component.encodeEnd(ctx);
		} catch(IOException e) {
			log.error("Component <" + component.getId() + "> could not render ! Continuing rendering of view <" + ctx.getViewRoot().getViewId() + ">...");
		}
	}
	
	/**
	 * 
	 * @param ctx
	 * @param locale
	 * @return
	 */
	private Locale getMatch(FacesContext ctx, Locale locale)
	{
		Locale result = null;
		for(Iterator it = ctx.getApplication().getSupportedLocales(); it.hasNext();)
		{
			Locale supportedLocale = (Locale)it.next();
			if(locale.equals(supportedLocale))
			{
				result = supportedLocale;
				break;
			}
			if(locale.getLanguage().equals(supportedLocale.getLanguage()) && supportedLocale.getCountry().equals(""))
				result = supportedLocale;
		}

		return result;
	}

//	private boolean isPageJSP(UIViewRoot viewRoot) {
//		boolean ret = false;
//		
//		String viewId = viewRoot.getViewId();
//		if(getDescriptorClassNameForViewId(viewId) == null) {
//			ret = true;
//		}
//		
//		return ret;
//	}
	
	/**
	 * This function is responsible for finding the descripto class for a given
	 * viewId. The policy may change over time, like supporting multiple packages
	 * or mory flexible mapping of viewIds to descriptor classes,etc..
	 *  
	 * @param viewId
	 * @return the descriptor class for a given viewId, or null if no descriptor found.
	 */
	private Class getDescriptorClassNameForViewId(String viewId) {
		Class ret = null;
		String className;
		FacesContext ctx = FacesContext.getCurrentInstance();
				
		// TODO : We should implement a configurable scheme with more than one package.	
		if(viewId.endsWith(".jsf") || viewId.endsWith(".jsp")) {
			String shortClassName = viewId.substring(0,viewId.length()-4);
			if(shortClassName.startsWith("/")) {
				shortClassName = shortClassName.substring(1,shortClassName.length());
			}
			
			className = getDescriptorPackage(ctx) + shortClassName + getDescriptorPostfix(ctx);
			try {
				ret = Class.forName(className);
			} catch(ClassNotFoundException e) {
				ret = null;
			}
		}
		
		return ret;
	}

	/**
	 * Determines the package where the descriptor classes are located.
	 * @param context
	 * @return
	 */
	private String getDescriptorPackage(FacesContext context) {
		String ret = "";	// Default package.
		String temp;
		
		// Try to determine descriptor package...
		temp = context.getExternalContext().getInitParameter("net.sourceforge.smile.descriptor.package"); 
		if(temp != null) {
			ret = temp;
			if(!ret.endsWith(".")) {
				ret = ret + ".";
			}
		}
		
		return ret;
	}

	/**
	 * Determines the postfix for the descriptor class.
	 * @param context
	 * @return
	 */
	private String getDescriptorPostfix(FacesContext context) {
		String ret = "";	// Default.
		String temp;
		
		// Try to determine descriptor package...
		temp = context.getExternalContext().getInitParameter("net.sourceforge.smile.descriptor.postfix"); 
		if(temp != null) {
			ret = temp;
		}
		
		return ret;
	}

	/**
	 * @see javax.faces.application.ViewHandler#calculateRenderKitId(javax.faces.context.FacesContext)
	 */
	public String calculateRenderKitId(FacesContext ctx) {
		return RenderKitFactory.HTML_BASIC_RENDER_KIT;
	}

	/**
	 * @see javax.faces.application.ViewHandler#getActionURL(javax.faces.context.FacesContext, java.lang.String)
	 */
	public String getActionURL(FacesContext ctx, String viewId) {
		// TODO Look into this:
		//return ctx.getExternalContext().encodeActionURL(viewId);
		// TODO implement conversion
		if (viewId != null && viewId.startsWith("/")) {
			return viewId.substring(1);
		} else {
			return viewId;			
		}
	}

	/**
	 * @see javax.faces.application.ViewHandler#getResourceURL(javax.faces.context.FacesContext, java.lang.String)
	 */
	public String getResourceURL(FacesContext ctx, String path) {
		// TODO Auto-generated method stub
		return ctx.getExternalContext().encodeResourceURL(path);
	}
}