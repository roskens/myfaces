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
package org.apache.myfaces.custom.div;

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * @author bdudney (latest modification by $Author$) 
 * @version $Revision$ $Date$ 
 * $Log$
 * Revision 1.1  2004/11/08 03:43:20  bdudney
 * Added a div element. x:div to use, inserts a div with class or style attributes
 *
 */
public class Div extends UIOutput {
  public static final String COMPONENT_TYPE = "org.apache.myfaces.Div";
  public static final String COMPONENT_FAMILY = "javax.faces.Output";
  private static final String DEFAULT_RENDERER_TYPE = DivRenderer.RENDERER_TYPE;
  private static final Log log = LogFactory.getLog(Div.class);
  private String _style = null;
  private String _styleClass = null;

  // ------------------------------------------------------------ Constructors
  public Div() {
    setRendererType(DEFAULT_RENDERER_TYPE);
  }

  public String getFamily() {
    return COMPONENT_FAMILY;
  }

  public String getStyle() {
    if (_style != null)
      return _style;
    ValueBinding vb = getValueBinding("style");
    return vb != null ? (String) vb.getValue(getFacesContext()) : null;
  }

  public void setStyle(String style) {
    this._style = style;
  }

  public String getStyleClass() {
    if (_styleClass != null)
      return _styleClass;
    ValueBinding vb = getValueBinding("styleClass");
    return vb != null ? (String) vb.getValue(getFacesContext()) : null;
  }

  public void setStyleClass(String styleClass) {
    this._styleClass = styleClass;
  }

  public void restoreState(FacesContext context, Object state) {
    Object values[] = (Object[]) state;
    super.restoreState(context, values[0]);
    _style = (String) values[1];
    _styleClass = (String) values[2];
  }

  public Object saveState(FacesContext context) {
    Object values[] = new Object[3];
    values[0] = super.saveState(context);
    values[1] = _style;
    values[2] = _styleClass;
    return values;
  }
}