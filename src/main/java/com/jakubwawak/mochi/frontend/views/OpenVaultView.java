/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
*/
package com.jakubwawak.mochi.frontend.views;

import com.jakubwawak.mochi.MochiApplication;
import com.jakubwawak.mochi.backend.database.Database_Vault;
import com.jakubwawak.mochi.enitity.Vault;
import com.jakubwawak.mochi.frontend.windows.CreateVaultWindow;
import com.jakubwawak.mochi.frontend.windows.VaultPasswordWindow;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.Lumo;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Main application web view
 */
@PageTitle("mochi by Jakub Wawak")
@Route("/sezame")
@RouteAlias("/")
public class OpenVaultView extends VerticalLayout {

    TextField mochi_field;
    TextField firstadd_field;
    TextField secondadd_field;
    TextField thirdadd_field;
    TextField fourthadd_field;

    Button openVault_button;

    int fieldFlag;

    Vault temporaryVault;


    /**
     * Constructor
     */
    public OpenVaultView(){
        this.getElement().setAttribute("theme", Lumo.DARK);
        fieldFlag = 0;
        addClassName("openvaultview");
        prepareLayout();
    }

    /**
     * Function for preparing components
     */
    void prepareComponents(){
        mochi_field = new TextField();
        mochi_field.setValue("mochi");
        mochi_field.setPlaceholder("?");
        mochi_field.addClassName("focus-editor-textfield");

        firstadd_field = new TextField();
        firstadd_field.setPlaceholder("firstadd field");
        firstadd_field.addClassName("focus-editor-textfield");

        secondadd_field = new TextField();
        secondadd_field.setPlaceholder("firstadd field");
        secondadd_field.addClassName("focus-editor-textfield");

        thirdadd_field = new TextField();
        thirdadd_field.setPlaceholder("firstadd field");
        thirdadd_field.addClassName("focus-editor-textfield");

        fourthadd_field = new TextField();
        fourthadd_field.setPlaceholder("firstadd field");
        fourthadd_field.addClassName("focus-editor-textfield");

        firstadd_field.setVisible(false);
        secondadd_field.setVisible(false);
        thirdadd_field.setVisible(false);
        fourthadd_field.setVisible(false);

        openVault_button = new Button("Open Vault",this::setOpenVault_button);
        openVault_button.addClassName("mochi-button-transparent");
        openVault_button.setVisible(false);

        mochi_field.addKeyPressListener(e->{
            String value = mochi_field.getValue();

            switch(value){
                case "create":
                {
                    CreateVaultWindow cvw = new CreateVaultWindow();
                    add(cvw.main_dialog);
                    cvw.main_dialog.open();
                    break;
                }
                default:
                {
                    Database_Vault dv = new Database_Vault(MochiApplication.database);
                    Vault vault = dv.getVault(value);
                    if ( dv.getVault(value) != null ){
                        temporaryVault = vault;
                        firstadd_field.setPlaceholder("vault pin...");
                        firstadd_field.setVisible(true);
                        fieldFlag = 0;
                    }
                    else{
                        if ( fieldFlag != 1 )
                            MochiApplication.notificationService("No vault with given hash!",1);
                    }
                }
            }
        });

        firstadd_field.addKeyPressListener(e->{
            if ( e.getKey().equals(Key.ENTER)){
                String value = firstadd_field.getValue();
                if ( temporaryVault.vault_code.equals(value)){
                    openVault_button.setVisible(true);
                }
                else{
                    MochiApplication.notificationService("Wrong PIN for vault ("+temporaryVault.vault_id.toString()+")",1);
                }
            }
        });
    }

    /**
     * Function for preparing layout data
     */
    void prepareLayout(){
        prepareComponents();

        add(mochi_field);
        add(firstadd_field);
        add(secondadd_field);
        add(thirdadd_field);
        add(fourthadd_field);
        add(openVault_button);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

    private void setOpenVault_button(ClickEvent ex){
        MochiApplication.currentVault = temporaryVault;
        openVault_button.getUI().ifPresent(ui ->
                ui.navigate("/focus"));
    }

}