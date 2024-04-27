/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.mochi.frontend.windows;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;

import java.util.ArrayList;

/**
 * Window for logging user to the app
 */
public class HelpWindow {

    // variables for setting x and y of window
    public String width = "50%";
    public String height = "50%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    String title;
    ArrayList<String> content;

    H1 titleHeader;
    TextArea messageContent_area;

    /**
     * Constructor
     */
    public HelpWindow(String title, ArrayList<String> content){
        this.title = title;
        this.content = content;
        main_dialog = new Dialog();
        main_dialog.addClassName("mochi-dialog-window");
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        titleHeader = new H1(title);
        messageContent_area = new TextArea();
        messageContent_area.setSizeFull();
        messageContent_area.addClassName("focus-editor-textarea");
        String windowContent = "";
        for(String line : content){
            windowContent  = windowContent + line + "\n";
        }
        messageContent_area.setValue(windowContent);
        messageContent_area.setEnabled(true);
    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        // set layout
        main_layout.add(titleHeader);
        main_layout.add(messageContent_area);
        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        main_dialog.add(main_layout);
        main_dialog.setWidth(width);main_dialog.setHeight(height);
    }
}