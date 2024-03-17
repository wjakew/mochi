/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.mochi.frontend.windows;

import com.jakubwawak.mochi.MochiApplication;
import com.jakubwawak.mochi.backend.datamanager.VaultManager;
import com.jakubwawak.mochi.enitity.Vault;
import com.jakubwawak.mochi.maintanance.RandomString;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

/**
 * Window for logging user to the app
 */
public class CreateVaultWindow {

    // variables for setting x and y of window
    public String width = "70%";
    public String height = "60%";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    TextField vaultName_field;
    TextArea vaultString_field;
    TextField vaultCode_field;

    Button createVault_button;

    Vault vault;

    /**
     * Constructor
     */
    public CreateVaultWindow(){
        main_dialog = new Dialog();
        main_dialog.addClassName("mochi-dialog-window");
        main_layout = new VerticalLayout();
        prepare_dialog();
        vault = new Vault();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        vaultName_field = new TextField("Vault Name");
        vaultName_field.setWidth("100%");vaultName_field.setPlaceholder("precious mochi vault v1");
        vaultName_field.setClassName("mochi-input-field");

        vaultString_field = new TextArea("Vault Special String");
        vaultString_field.setWidth("100%");vaultString_field.setHeight("50%");
        vaultString_field.setClassName("mochi-input-field");
        RandomString rs = new RandomString(45);
        vaultString_field.setValue(rs.nextString());

        vaultCode_field = new TextField("Vault Code"); vaultCode_field.setWidth("50%"); vaultCode_field.setPlaceholder("secret password");
        vaultCode_field.setClassName("mochi-input-field");

        createVault_button = new Button("Create New Vault!",VaadinIcon.PLUS_CIRCLE_O.create(),this::setCreateVault_button);
        createVault_button.addClassName("mochi-button-bright"); createVault_button.setWidth("100%");
    }

    /**
     * Function for validating fields
     * @return boolean
     */
    boolean validateFields(){
        return !vaultName_field.getValue().isBlank() && !vaultString_field.getValue().isBlank() && !vaultCode_field.getValue().isBlank();
    }

    /**
     * createVault_button action
     * @param ex
     */
    private void setCreateVault_button(ClickEvent ex){
        if ( validateFields() ){
            VaultManager vm = new VaultManager(MochiApplication.database);
            Vault vault = vm.createVault(vaultString_field.getValue(),vaultName_field.getValue(),vaultCode_field.getValue());
            if ( vault != null ){
                MochiApplication.notificationService("Vault ("+vault.vault_name+") created!",1);
            }
        }
        else{
            MochiApplication.notificationService("Wrong input! Check fields.",3);
        }
    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        // set layout
        main_layout.add(VaadinIcon.PLUS.create());
        main_layout.add(new Text("Create New Vault"));

        main_layout.add(vaultName_field,vaultString_field,vaultCode_field,createVault_button);

        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        main_layout.getStyle().set("text-align", "center");

        main_dialog.add(main_layout);
        main_dialog.setWidth(width);main_dialog.setHeight(height);
    }
}