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

import javax.faces.component.UIComponent;
import org.apache.myfaces.taglib.html.HtmlOutputTextTagBase;
/**
 * @author bdudney (latest modification by $Author$) 
 * @version $Revision$ $Date$ 
 * $Log$
 * Revision 1.1  2004/11/08 03:43:20  bdudney
 * Added a div element. x:div to use, inserts a div with class or style attributes
 *
 */
public class DivTag extends HtmlOutputTextTagBase {
  private String _style = null;
  private String _styleClass = null;

  public String getComponentType() {
    return Div.COMPONENT_TYPE;
  }

  public String getRendererType() {
    return DivRenderer.RENDERER_TYPE;
  }

  public void release() {
    super.release();
    this._style = null;
    this._styleClass = null;
  }

  /**
   * overrides setProperties() form UIComponentTag.
   */
  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    setStringProperty(component, "style", _style);
    setStringProperty(component, "styleClass", _styleClass);
  }

  //---------------------------------------------only the Setters
  public void setStyle(String style) {
    this._style = style;
  }

  public void setStyleClass(String styleClass) {
    this._styleClass = styleClass;
  }
}