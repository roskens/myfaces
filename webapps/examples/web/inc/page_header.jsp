<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.sourceforge.net/tld/myfaces_ext_0_9.tld" prefix="x"%>

<h:panelGrid id="header_group1" columns="2" styleClass="pageHeader1"  >
    <h:graphicImage id="header_logo" url="images/logo_mini.jpg" alt="#{example_messages['alt_logo']}" />
    <f:verbatim>
        &nbsp;&nbsp;
        <font size="+1" color="#FFFFFF">MyFaces - The free JavaServer&#8482; Faces Implementation</font>
        <font size="-1" color="#FFFFFF">(Version 1.0.5 beta)</font>
    </f:verbatim>
</h:panelGrid>

<h:panelGrid id="header_group2" columns="1" styleClass="pageHeader2" columnClasses="pageHeader2col1"  >
    <x:jscookMenu layout="hbr" theme="ThemeOffice" >
        <%/* if you want to use jscookMenu in your application, you will have to:
               - add jscookmenu directory to your web directory
               - add scripts and stylesheets to html-header (see inc/header.inc for details)
             availaible themes: ThemeIE, ThemeMiniBlack, ThemeOffice, ThemePanel
             layout: hbr, hbl, hur, hul, vbr, vbl, vur, vul
             respect to Heng Yuan http://www.cs.ucla.edu/~heng/JSCookMenu
        */%>
        <x:navigationMenuItem id="nav_1" itemLabel="#{example_messages['nav_Home']}" action="go_home" />
        <x:navigationMenuItem id="nav_2" itemLabel="#{example_messages['nav_Examples']}" >
            <x:navigationMenuItem id="nav_2_1" itemLabel="#{example_messages['nav_Sample_1']}" action="go_sample1" icon="images/myfaces.gif" />
            <x:navigationMenuItem id="nav_2_2" itemLabel="#{example_messages['nav_Sample_2']}" action="go_sample2" icon="images/myfaces.gif" />
            <x:navigationMenuItem id="nav_2_3" itemLabel="#{example_messages['nav_Validate']}" action="go_validate" icon="images/myfaces.gif" />
            <x:navigationMenuItem id="nav_2_4" itemLabel="#{example_messages['nav_Components']}" icon="images/component.gif" split="true" >
                <x:navigationMenuItem id="nav_2_4_1" itemLabel="#{example_messages['nav_dataTable']}" action="go_dataTable" icon="images/myfaces.gif" />
                <x:navigationMenuItem id="nav_2_4_2" itemLabel="#{example_messages['nav_sortTable']}" action="go_sortTable" icon="images/myfaces.gif" />
                <x:navigationMenuItem id="nav_2_4_3" itemLabel="#{example_messages['nav_Selectbox']}" action="go_selectbox" icon="images/myfaces.gif" />
                <x:navigationMenuItem id="nav_2_4_4" itemLabel="#{example_messages['nav_FileUpload']}" action="go_fileupload" icon="images/myfaces.gif" />
                <x:navigationMenuItem id="nav_2_4_5" itemLabel="#{example_messages['nav_TabbedPane']}" action="go_tabbedPane" icon="images/myfaces.gif" />
                <x:navigationMenuItem id="nav_2_4_6" itemLabel="#{example_messages['nav_Calendar']}" action="go_calendar" icon="images/myfaces.gif" split="true" />
                <x:navigationMenuItem id="nav_2_4_7" itemLabel="#{example_messages['nav_dataList']}" action="go_dataList" icon="images/myfaces.gif" />
                <x:navigationMenuItem id="nav_2_4_8" itemLabel="#{example_messages['nav_tree']}" action="go_tree" icon="images/myfaces.gif" />
                <x:navigationMenuItem id="nav_2_4_9" itemLabel="#{example_messages['nav_rssTicker']}" action="go_rssticker" icon="images/myfaces.gif" />
                <x:navigationMenuItem id="nav_2_4_10" itemLabel="#{example_messages['nav_dataScroller']}" action="go_datascroller" icon="images/myfaces.gif" />
            </x:navigationMenuItem>
        </x:navigationMenuItem>
        <x:navigationMenuItem id="nav_3" itemLabel="#{example_messages['nav_Documentation']}" >
            <x:navigationMenuItem id="nav_3_1" itemLabel="#{example_messages['nav_Features']}" action="go_features" icon="images/myfaces.gif" />
        </x:navigationMenuItem>
        <x:navigationMenuItem id="nav_4" itemLabel="#{example_messages['nav_Options']}" action="go_options" />
        <x:navigationMenuItem id="nav_5" itemLabel="#{example_messages['nav_Info']}" split="true" >
            <x:navigationMenuItem id="nav_5_1" itemLabel="#{example_messages['nav_Contact']}" action="go_contact" icon="jscookmenu/ThemeOffice/help.gif" />
            <x:navigationMenuItem id="nav_5_2" itemLabel="#{example_messages['nav_Copyright']}" action="go_copyright" icon="jscookmenu/ThemeOffice/help.gif" />
        </x:navigationMenuItem>

    </x:jscookMenu>
</h:panelGrid>

