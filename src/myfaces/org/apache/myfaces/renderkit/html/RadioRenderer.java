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
package net.sourceforge.myfaces.renderkit.html;

import net.sourceforge.myfaces.component.ext.UISelectOneRadio;
import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.callback.CallbackRenderer;
import net.sourceforge.myfaces.renderkit.callback.CallbackSupport;
import net.sourceforge.myfaces.renderkit.html.util.HTMLEncoder;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;
import net.sourceforge.myfaces.renderkit.html.util.SelectItemUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.Iterator;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class RadioRenderer
    extends HtmlRenderer
    implements CallbackRenderer
{
    private static final Log log = LogFactory.getLog(RadioRenderer.class);

    public static final String TYPE = "Radio";
    private static final String ATTR_COUNT = RadioRenderer.class.getName() + ".COUNT";

    public String getRendererType()
    {
        return TYPE;
    }

    public void beforeEncodeBegin(FacesContext facesContext,
                                  Renderer renderer,
                                  UIComponent uiComponent) throws IOException
    {
        if (uiComponent instanceof UISelectItem)
        {
            SelectItem item = SelectItemUtil.getSelectItem(facesContext, (UISelectItem)uiComponent);
            UISelectOne parent = getParent(facesContext, uiComponent);
            Integer i = (Integer)parent.getAttributes().get(ATTR_COUNT);
            if (i == null)
            {
                i = new Integer(1);
            }
            else
            {
                i = new Integer(i.intValue() + 1);
            }
            parent.getAttributes().put(ATTR_COUNT, i);
            renderItem(facesContext, parent, item, isLayoutPageDirection(parent), false, i.intValue());
        }
    }

    public void afterEncodeEnd(FacesContext facesContext,
                               Renderer renderer,
                               UIComponent uiComponent) throws IOException
    {
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        if (uiComponent instanceof UISelectOneRadio)
        {
            CallbackSupport.addCallbackRenderer(facesContext, uiComponent, this);
        }
        else if (uiComponent instanceof UISelectOne)
        {
            Iterator it = SelectItemUtil.getSelectItems(facesContext, uiComponent);
            if (it.hasNext())
            {
                for (int i = 1; it.hasNext(); i++)
                {
                    //FIXME
                    /*
                    renderItem(facesContext,
                               (UISelectOne)uiComponent,
                               (SelectItem)it.next(),
                               isLayoutPageDirection((UISelectOne)uiComponent),
                               true,
                               i);
                               */
                }
            }
        }
        else
        {
            log.error("Expected UISelectOne or UISelectOneRadio when rendering input radio. "
                      + "component: " + uiComponent.getClientId(facesContext));
            return;
        }
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        super.encodeEnd(facesContext, uiComponent);
        if (uiComponent instanceof UISelectOneRadio)
        {
            CallbackSupport.removeCallbackRenderer(facesContext, uiComponent, this);
        }
    }

    //
    // Util
    //
    protected UISelectOne getParent(FacesContext facesContext, UIComponent uiComponent)
    {
        UIComponent parent = uiComponent.getParent();
        if (!(parent instanceof UISelectOne))
        {
            throw new FacesException("expected component of type UISelectOne");
        }
        return (UISelectOne)parent;
    }

    //
    // Rendering
    //
    protected void renderItem(FacesContext facesContext,
                              UISelectOne uiSelectOne,
                              SelectItem selectItem,
                              boolean isLayoutPageDirection,
                              boolean useLayout,
                              int itemCount)
        throws IOException
    {

        ResponseWriter writer = facesContext.getResponseWriter();

        String coumpoundId = uiSelectOne.getClientId(facesContext);

        //FIXME
        //Object currentValue = uiSelectOne.currentValue(facesContext);
        Object currentValue = null;
        String currentStrValue = ((currentValue != null) ? currentValue.toString() : null);

        beforeRenderItem(facesContext, uiSelectOne, selectItem, isLayoutPageDirection, useLayout, itemCount);
        writer.write("<input type=\"radio\"");

        writer.write(" name=\"");
        writer.write(coumpoundId);
        writer.write("\" id=\"");
        writer.write(coumpoundId);
        writer.write('"');
        Object itemValue = selectItem.getValue();
        if (itemValue != null)
        {
            writer.write(" value=\"");
            writer.write(itemValue.toString());
            writer.write('"');
        }

        if (currentStrValue != null && itemValue != null &&
            currentStrValue.equals(itemValue))
        {
            writer.write(" checked=\"true\"");
        }

        HTMLUtil.renderStyleClass(writer, uiSelectOne);
        HTMLUtil.renderHTMLAttributes(writer, uiSelectOne, HTML.INPUT_PASSTHROUGH_ATTRIBUTES);
        HTMLUtil.renderDisabledOnUserRole(facesContext, uiSelectOne);

        writer.write('>');
        renderLabel(facesContext, uiSelectOne, selectItem, useLayout);
        afterRenderItem(facesContext, uiSelectOne, selectItem, isLayoutPageDirection, useLayout);
    }

    protected void beforeRenderLabel(FacesContext facesContext,
                                     UISelectOne selectOne,
                                     SelectItem selectItem,
                                     boolean useLayout)
        throws IOException
    {
        if (useLayout)
        {
            ResponseWriter writer = facesContext.getResponseWriter();
            writer.write("&nbsp;");
        }
    }

    protected void renderLabel(FacesContext facesContext,
                               UISelectOne selectOne,
                               SelectItem selectItem,
                               boolean useLayout)
        throws IOException
    {
        String label = selectItem.getLabel();
        if (label != null && label.length() > 0)
        {
            ResponseWriter writer = facesContext.getResponseWriter();
            boolean span = selectOne.getAttributes().get(JSFAttr.SELECT_ONE_CLASS_ATTR) != null;
            beforeRenderLabel(facesContext, selectOne, selectItem, useLayout);
            if (span)
            {
                writer.write("<span ");
                HTMLUtil.renderStyleClass(writer, selectOne);
                writer.write(">");
            }
            label = selectItem.getLabel();
            writer.write(HTMLEncoder.encode(
                label,
                true,
                true));
            if (span)
            {
                writer.write("</span>");
            }
            afterRenderLabel(facesContext, selectOne, selectItem, useLayout);
        }
    }

    protected void afterRenderLabel(FacesContext facesContext,
                                    UISelectOne selectOne,
                                    SelectItem item,
                                    boolean useLayout)
        throws IOException
    {
    }

    protected void beforeRenderItem(FacesContext facesContext,
                                    UISelectOne uiSelectOne,
                                    SelectItem selectItem,
                                    boolean layoutPageDirection,
                                    boolean useLayout,
                                    int itemCount)
        throws IOException
    {
        if (useLayout && itemCount > 1)
        {
            if (layoutPageDirection)
            {
                facesContext.getResponseWriter().write("<br/>\n");
            }
            else
            {
                facesContext.getResponseWriter().write("&nbsp;&nbsp;&nbsp;");
            }
        }
    }

    protected void afterRenderItem(FacesContext facesContext,
                                   UISelectOne uiSelectOne,
                                   SelectItem selectItem,
                                   boolean layoutPageDirection,
                                   boolean useLayout)
        throws IOException
    {
    }

    private static final String PAGE_DIRECTION = "PAGE_DIRECTION";

    protected boolean isLayoutPageDirection(UISelectOne uiComponent)
    {
        String layout = (String)uiComponent.getAttributes().get(JSFAttr.LAYOUT_ATTR);
        return layout != null && layout.toUpperCase().equals(PAGE_DIRECTION) ? true : false;
    }
}