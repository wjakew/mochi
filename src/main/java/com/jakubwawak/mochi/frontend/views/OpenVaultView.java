/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
*/
package com.jakubwawak.mochi.frontend.views;

import com.jakubwawak.mochi.MochiApplication;
import com.jakubwawak.mochi.backend.database.Database_Note;
import com.jakubwawak.mochi.backend.database.Database_Vault;
import com.jakubwawak.mochi.enitity.Note;
import com.jakubwawak.mochi.enitity.Vault;
import com.jakubwawak.mochi.frontend.components.markdownEditor.FocusEditor;
import com.jakubwawak.mochi.frontend.windows.HelpWindow;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.Lumo;

import java.util.ArrayList;

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
        mochi_field.setMaxLength(100);
        mochi_field.addClassName("openvault-input-field");

        firstadd_field = new TextField();
        firstadd_field.setPlaceholder("firstadd field");
        firstadd_field.addClassName("openvault-input-field");
        firstadd_field.setMaxLength(100);

        secondadd_field = new TextField();
        secondadd_field.setPlaceholder("firstadd field");
        secondadd_field.addClassName("openvault-input-field");
        secondadd_field.setMaxLength(100);

        thirdadd_field = new TextField();
        thirdadd_field.setPlaceholder("firstadd field");
        thirdadd_field.addClassName("openvault-input-field");
        thirdadd_field.setMaxLength(100);

        fourthadd_field = new TextField();
        fourthadd_field.setPlaceholder("firstadd field");
        fourthadd_field.addClassName("openvault-input-field");
        fourthadd_field.setMaxLength(100);

        firstadd_field.setVisible(false);
        secondadd_field.setVisible(false);
        thirdadd_field.setVisible(false);
        fourthadd_field.setVisible(false);

        mochi_field.addKeyPressListener(e->{
            if ( e.getKey().equals(Key.ENTER)){
                String value = mochi_field.getValue();
                switch(value){
                    case "create":
                    {
                        firstadd_field.setPlaceholder("vault name");
                        secondadd_field.setPlaceholder("code");
                        thirdadd_field.setPlaceholder("pin");
                        firstadd_field.setVisible(true);secondadd_field.setVisible(true);thirdadd_field.setVisible(true);
                        break;
                    }
                    case "help":
                    {
                        ArrayList<String> helpContent = new ArrayList<>();
                        helpContent.add("create - creates new vault");
                        helpContent.add("other input - code for your personal vault");
                        HelpWindow hw = new HelpWindow("mochi help",helpContent);
                        add(hw.main_dialog);
                        hw.main_dialog.open();
                        mochi_field.setValue("");
                        break;
                    }
                    default:
                    {
                        Database_Vault dv = new Database_Vault(MochiApplication.database);
                        Vault vault = dv.getVault(value);
                        // check if given value is vault hash
                        if ( dv.getVault(value) != null ){
                            temporaryVault = vault;
                            firstadd_field.setPlaceholder("vault pin...");
                            firstadd_field.setVisible(true);
                            firstadd_field.focus();
                            fieldFlag = 0;
                        }
                        // check if given value is a share URL
                        else{
                            Database_Note dn = new Database_Note(MochiApplication.database);
                            Note sharedNote = dn.loadNote(value);
                            if(sharedNote != null){
                                mochi_field.getUI().ifPresent(ui->{ui.navigate("/note-viewer/"+value);});
                            }
                            else{
                                MochiApplication.notificationService("No object ("+value+")",1);
                            }
                        }
                    }
                }
            }
        });

        firstadd_field.addKeyPressListener(e->{
            if ( e.getKey().equals(Key.ENTER)){
                String value = firstadd_field.getValue();
                if ( temporaryVault.vault_code.equals(value)){
                    MochiApplication.currentVault = temporaryVault;
                    MochiApplication.vaultUpdateService();
                    MochiApplication.currentEditor = new FocusEditor(null);
                    firstadd_field.getUI().ifPresent(ui ->
                            ui.navigate("/focus"));
                }
                else{
                    MochiApplication.notificationService("Wrong PIN for vault ("+temporaryVault.vault_id.toString()+")",1);
                }
            }
        });

        thirdadd_field.addKeyPressListener(e->{
            if ( e.getKey().equals(Key.ENTER)){
                if ( !firstadd_field.getValue().isBlank() && !secondadd_field.getValue().isBlank() && !thirdadd_field.getValue().isBlank() ){
                    String vaultName = firstadd_field.getValue();
                    String vaultCode = secondadd_field.getValue();
                    String vaultPin = thirdadd_field.getValue();

                    Database_Vault dv = new Database_Vault(MochiApplication.database);
                    if ( dv.getVault(vaultCode) != null ){
                        MochiApplication.notificationService("Cannot use hash!",1);
                    }
                    else{
                        Vault vault = new Vault();
                        vault.vault_hash = vaultCode;
                        vault.vault_name = vaultName;
                        vault.vault_code = vaultPin;
                        Vault createdVault = dv.insertVault(vault);
                        MochiApplication.currentVault = createdVault;
                        thirdadd_field.getUI().ifPresent(ui ->
                                ui.navigate("/focus"));
                    }
                }
                else{
                    MochiApplication.notificationService("Wrong user input!",1);
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

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }
}