package net.sourceforge.myfaces.examples.util;

import javax.faces.context.FacesContext;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.text.MessageFormat;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class GuiUtil
{
    private static String BUNDLE_NAME = "net.sourceforge.myfaces.examples.resource.example_messages";

    public static String getMessageResource(String key, Object[] arguments)
    {
        FacesContext context = FacesContext.getCurrentInstance();
        String resourceString;
        try
        {
            ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, context.getViewRoot().getLocale());
            resourceString = bundle.getString(key);
        }
        catch (MissingResourceException e)
        {
            return key;
        }

        if (arguments == null) return resourceString;

        MessageFormat format = new MessageFormat(resourceString, context.getViewRoot().getLocale());
        return format.format(arguments);
    }

}
