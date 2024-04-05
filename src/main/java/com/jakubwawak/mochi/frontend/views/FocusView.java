/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
*/
package com.jakubwawak.mochi.frontend.views;

import com.jakubwawak.mochi.MochiApplication;
import com.jakubwawak.mochi.frontend.components.VaultNotesList;
import com.jakubwawak.mochi.frontend.components.markdownEditor.FocusEditor;
import com.jakubwawak.mochi.frontend.components.markdownEditor.MarkdownEditor;
import com.jakubwawak.mochi.frontend.components.mochiheader.MochiHeader;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.Lumo;

/**
 * Main application web view
 */
@PageTitle("vault by mochi")
@Route("/focus")
public class FocusView extends VerticalLayout {

    FocusEditor focusEditor;


    /**
     * Constructor
     */
    public FocusView(){
        this.getElement().setAttribute("theme", Lumo.DARK);
        addClassName("vaultview");
        prepareLayout();
    }

    /**
     * Function for preparing components
     */
    void prepareComponents(){
        focusEditor = new FocusEditor();
    }

    /**
     * Function for preparing layout data
     */
    void prepareLayout(){
        prepareComponents();
        if (MochiApplication.currentVault != null ){
            add(focusEditor);
        }
        else{
            add(new Text("No vault is opened right now."));
        }
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }
}