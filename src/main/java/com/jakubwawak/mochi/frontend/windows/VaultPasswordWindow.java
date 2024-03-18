/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.mochi.frontend.windows;

import com.jakubwawak.mochi.MochiApplication;
import com.jakubwawak.mochi.backend.database.Database_Vault;
import com.jakubwawak.mochi.enitity.Mochi;
import com.jakubwawak.mochi.enitity.Vault;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;

/**
 * Window for logging user to the app
 */
public class VaultPasswordWindow {

    // variables for setting x and y of window
    public String width = "45%";
    public String height = "55%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    PasswordField password_field;
    Button openVault_button;

    Vault openedVault;

    /**
     * Constructor
     */
    public VaultPasswordWindow(String vault_hash){
        main_dialog = new Dialog();
        main_dialog.addClassName("mochi-dialog-window");
        main_layout = new VerticalLayout();

        Database_Vault dv = new Database_Vault(MochiApplication.database);
        openedVault = dv.getVault(vault_hash);
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components

        password_field = new PasswordField("Vault Password");password_field.setPlaceholder("pss secret");
        password_field.setWidthFull();

        openVault_button = new Button("Unlock Vault",this::setOpenVault_button);
        openVault_button.addClassName("mochi-button-bright"); openVault_button.setWidthFull();
    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        // set layout

        if ( openedVault != null ){
            main_layout.add(VaadinIcon.SAFE_LOCK.create());
            main_layout.add(new H6(openedVault.vault_name));
            main_layout.add(password_field);
            main_layout.add(openVault_button);
        }
        else{
            main_layout.add(new Text("Cannot find vault with provided hash"));
        }

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
     * openVault_button action
     * @param ex
     */
    private void setOpenVault_button(ClickEvent ex){
        if ( !password_field.getValue().isEmpty() ){
            if ( openedVault.vault_code.equals(password_field.getValue()) ){
                MochiApplication.notificationService("Vault "+openedVault.vault_name+" opened!",1);
                main_dialog.close();
                //TODO
            }
            else{
                MochiApplication.notificationService("Wrong password!",4);
            }
        }
        else{
            MochiApplication.notificationService("Password field is empty!",3);
        }
    }
}