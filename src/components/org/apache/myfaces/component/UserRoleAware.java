/*
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
package net.sourceforge.myfaces.component;

/**
 * Behavioral interface.
 * Components that support user role checking should implement this interface
 * to optimize property access.
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.2  2004/05/18 14:31:36  manolito
 * user role support completely moved to components source tree
 *
 * Revision 1.1  2004/03/31 07:19:20  manolito
 * changed name from UserRoleSupport
 *
 */
public interface UserRoleAware
{
    public static final String ENABLED_ON_USER_ROLE_ATTR = "enabledOnUserRole";
    public static final String VISIBLE_ON_USER_ROLE_ATTR = "visibleOnUserRole";

    public String getEnabledOnUserRole();
    public void setEnabledOnUserRole(String userRole);

    public String getVisibleOnUserRole();
    public void setVisibleOnUserRole(String userRole);
}
