/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.mochi.frontend.windows;

import com.jakubwawak.mochi.MochiApplication;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.atmosphere.interceptor.AtmosphereResourceStateRecovery;

/**
 * Window for logging user to the app
 */
public class MochiOptionsWindow {

    // variables for setting x and y of window
    public String width = "50%";
    public String height = "30%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    Button closevault_button;
    Button createnote_button;

    /**
     * Constructor
     */
    public MochiOptionsWindow(){
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
        closevault_button = new Button("Close Vault", VaadinIcon.CLOSE.create(),this::setClosevault_button);
        closevault_button.addClassName("mochi-button-dark");
        closevault_button.setWidthFull();

        createnote_button = new Button("New Note",VaadinIcon.PENCIL.create(),this::setCreatenote_button);
        createnote_button.addClassName("mochi-button-dark");
        createnote_button.setWidthFull();
    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        // set layout

        main_layout.add(VaadinIcon.GLOBE.create());
        main_layout.add(new Text("Mochi Options"));
        main_layout.add(createnote_button);
        main_layout.add(closevault_button);

        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        main_layout.getStyle().set("text-align", "center");

        main_dialog.add(main_layout);
        main_dialog.setWidth(width);main_dialog.setHeight(height);
    }

    /**
     * Function for closing vault
     * @param ex
     */
    private void setClosevault_button(ClickEvent ex){
        MochiApplication.currentVault = null;
        closevault_button.getUI().ifPresent(ui ->
                ui.navigate("/sezame"));
        MochiApplication.notificationService("Vault closed!",3);
    }

    /**
     * Function for creating new vault
     * @param ex
     */
    private void setCreatenote_button(ClickEvent ex){
        EditorWindow ew = new EditorWindow(null);
        main_layout.add(ew.main_dialog);
        ew.main_dialog.open();
        main_dialog.close();
    }
}