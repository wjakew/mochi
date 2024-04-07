/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.mochi.frontend.components.markdownEditor;

import com.jakubwawak.mochi.MochiApplication;
import com.jakubwawak.mochi.enitity.Mochi;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.jakubwawak.mochi.enitity.Note;

/**
 * Component for creating editor terminal
 */
public class FocusEditorTerminal extends TextField {

    /**
     * Constructor
     */
    public FocusEditorTerminal(){
        super();
        addClassName("mochi-terminal-field");
        this.setPlaceholder("terminal..");
        this.setPrefixComponent(VaadinIcon.TERMINAL.create());
        this.setWidthFull();

        addKeyPressListener(e->{
            if ( e.getKey().equals(Key.ENTER)){
                String userInput = getValue();
                createAction(userInput);
            }
        });
    }

    /**
     * Function for creating action on terminal
     * @param userInput
     */
    void createAction(String userInput){
        String[] words = userInput.split(" ");
        if ( words.length == 1 ){
            switch(words[0]){
                case "save":
                {
                    Note note = new Note();
                    if (!MochiApplication.currentEditor.title_field.getValue().isEmpty() && !MochiApplication.currentEditor.note_area.getValue().isEmpty()){
                        note.note_name = MochiApplication.currentEditor.title_field.getValue();
                        note.note_raw = MochiApplication.currentEditor.note_area.getValue();
                        MochiApplication.currentVault.addNote(note);
                        MochiApplication.vaultUpdateService();
                    }
                    else{
                        MochiApplication.notificationService("Title or content is empty, cannot save!",1);
                    }
                    break;
                }
                case "close":
                {
                    MochiApplication.vaultUpdateService();
                    MochiApplication.notificationService("Closed ("+MochiApplication.currentVault.vault_id.toString()+")",1);
                    MochiApplication.currentVault = null;
                    this.getUI().ifPresent(ui ->
                            ui.navigate("/sezame"));
                }
            }
        }
        else{
            MochiApplication.notificationService("Wrong command usage!",1);
        }
    }
}
