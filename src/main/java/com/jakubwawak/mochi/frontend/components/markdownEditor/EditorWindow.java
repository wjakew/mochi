/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.mochi.frontend.components.markdownEditor;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Window for showing editor on the screen
 */
public class EditorWindow {

    // variables for setting x and y of window
    public String width = "80%";
    public String height = "80%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    MarkdownEditor me;

    Button close_button;

    /**
     * Constructor
     */
    public EditorWindow(){
        main_dialog = new Dialog();
        main_dialog.setDraggable(true);
        main_dialog.setResizable(true);
        main_dialog.setModal(false);
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        me = new MarkdownEditor(null);
        close_button = new Button("Close Editor",this::setClose_button);
        close_button.getStyle().set("background-color","black");
        close_button.getStyle().set("color","white");
        close_button.getStyle().set("font-family","Monospace");
    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        // set layout
        main_layout.add(me);
        main_layout.add(close_button);
        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        main_layout.getStyle().set("text-align", "center");

        main_layout.getStyle().set("border-radius","25px");
        main_layout.getStyle().set("background-color",backgroundStyle);
        main_layout.getStyle().set("--lumo-font-family","Monospace");
        main_dialog.add(main_layout);
        main_dialog.setWidth(width);main_dialog.setHeight(height);
    }

    /**
     * close_button action
     * @param ex
     */
    private void setClose_button(ClickEvent ex){
        main_dialog.close();
    }
}