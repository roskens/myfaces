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

import net.sourceforge.myfaces.renderkit.attr.ButtonRendererAttributes;
import net.sourceforge.myfaces.renderkit.html.util.HTMLEncoder;
import net.sourceforge.myfaces.renderkit.html.util.CommonAttributes;
import net.sourceforge.myfaces.util.logging.LogUtil;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ApplicationEvent;
import javax.faces.event.CommandEvent;
import javax.faces.event.FormEvent;
import java.io.IOException;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ButtonRenderer
        extends HTMLRenderer
        implements ButtonRendererAttributes
{
    public static final String TYPE = "Button";
    public String getRendererType()
    {
        return TYPE;
    }

    private String getHiddenValueParamName(FacesContext facesContext, UIComponent uiComponent)
    {
        return uiComponent.getClientId(facesContext) + ".VALUE";
    }

    public void decode(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        //super.decode must not be called, because value is handled here

        String paramName = uiComponent.getClientId(facesContext);
        String paramValue = facesContext.getServletRequest().getParameter(paramName);
        boolean submitted = false;
        if (paramValue == null)
        {
            if (facesContext.getServletRequest().getParameter(paramName + ".x") != null) //image button
            {
                submitted = true;
            }
        }
        else
        {
            if (paramValue.length() > 0)
            {
                submitted = true;
            }
        }

        if (submitted)
        {
            String commandName;
            String hiddenValue = facesContext.getServletRequest()
                                    .getParameter(getHiddenValueParamName(facesContext,
                                                                          uiComponent));
            if (hiddenValue != null)
            {
                commandName = hiddenValue;
            }
            else
            {
                if (paramValue != null)
                {
                    commandName = paramValue;
                }
                else
                {
                    commandName = getStringValue(facesContext, uiComponent);
                }
            }

            uiComponent.setValue(commandName);
            uiComponent.setValid(true);

            //Old event processing:

            ApplicationEvent appEvent;

            //Form suchen
            String formName = null;
            for (UIComponent parent = uiComponent.getParent(); parent != null; parent = parent.getParent())
            {
                if (parent instanceof UIForm)
                {
                    formName = ((UIForm)parent).getFormName();
                    break;
                }
            }

            if (formName == null)
            {
                appEvent = new CommandEvent(uiComponent, commandName);
            }
            else
            {
                appEvent = new FormEvent(uiComponent, formName, commandName);
            }

            facesContext.addApplicationEvent(appEvent);

            //New event processing:
            if (uiComponent instanceof UICommand)
            {
                ((UICommand)uiComponent).fireActionEvent(facesContext);
            }
            else
            {
                LogUtil.getLogger().warning("Component " + uiComponent.getClientId(facesContext) + "is no UICommand.");
            }
        }
        else
        {
            uiComponent.setValid(true);
        }
    }

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException
    {
        ResponseWriter writer = context.getResponseWriter();
        boolean hiddenParam = true;
        writer.write("<input type=");
        String imageSrc = (String)component.getAttribute(IMAGE_ATTR);
        if (imageSrc != null)
        {
            writer.write("\"image\" src=\"");
            writer.write(imageSrc);
            writer.write("\"");
            writer.write(" name=\"");
            writer.write(component.getClientId(context));
            writer.write("\"");
        }
        else
        {
            writer.write("\"submit\" name=\"");
            writer.write(component.getClientId(context));
            writer.write("\"");
            writer.write(" value=\"");
            String label = (String)component.getAttribute(LABEL_ATTR);
            //TODO: key/bundle instead of label
            if (label == null)
            {
                label = getStringValue(context, component);
                hiddenParam = false;
            }
            writer.write(HTMLEncoder.encode(label, false, false));
            writer.write("\"");
        }

        //css class:
        String cssClass = (String)component.getAttribute(COMMAND_CLASS_ATTR);
        if (cssClass != null)
        {
            writer.write(" class=\"");
            writer.write(cssClass);
            writer.write("\"");
        }

        CommonAttributes.renderHTMLEventHandlerAttributes(context, component);
        CommonAttributes.renderUniversalHTMLAttributes(context, component);
        CommonAttributes.renderAttributes(context, component, ButtonRendererAttributes.COMMON_BUTTON_ATTRIBUTES);

        writer.write(">");

        if (hiddenParam)
        {
            writer.write("<input type=\"hidden\" name=\"");
            writer.write(getHiddenValueParamName(context, component));
            writer.write("\" value=\"");
            String strVal = getStringValue(context, component);
            writer.write(HTMLEncoder.encode(strVal, false, false));
            writer.write("\">");
        }
    }

    public boolean supportsComponentType(String s)
    {
        return s.equals(UICommand.TYPE);
    }

    public boolean supportsComponentType(UIComponent uiComponent)
    {
        return uiComponent instanceof UICommand;
    }
}
