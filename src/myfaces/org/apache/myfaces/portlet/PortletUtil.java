/*
 * Copyright 2005 The Apache Software Foundation.
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

package org.apache.myfaces.portlet;

import javax.faces.context.FacesContext;
import javax.portlet.RenderResponse;

/**
 * Static utility class for portlet-related operations.
 *
 * @author  Stan Silvert
 */
public final class PortletUtil 
{
    
    /** Don't allow a new instance of PortletUtil */
    private PortletUtil() 
    {
    }
    
    public static boolean isRenderResponse(FacesContext facesContext)
    {
        if (!isPortletRequest(facesContext)) return false;
        
        try{
            return facesContext.getExternalContext().getResponse() instanceof RenderResponse;
        }catch(NoClassDefFoundError e){
            // noop
        }
        return false;
    }
    
    public static boolean isPortletRequest(FacesContext facesContext)
    {
        try{
            return facesContext.getExternalContext()
                           .getSessionMap()
                           .get(MyFacesGenericPortlet.PORTLET_REQUEST_FLAG) != null;
        }catch(NoClassDefFoundError e){
            // noop
        }
        return false;
    }
}