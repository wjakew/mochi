/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.mochi.frontend.components.markdownEditor;

import com.jakubwawak.mochi.MochiApplication;
import com.jakubwawak.mochi.backend.database.Database_Note;
import com.jakubwawak.mochi.enitity.Mochi;
import com.jakubwawak.mochi.frontend.windows.PreviewWindow;
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
                        Database_Note dn = new Database_Note(MochiApplication.database);
                        Note addedNote =  dn.insertNote(note);
                        if (addedNote != null ) {
                            MochiApplication.currentVault.addNote(addedNote);
                            System.out.println(addedNote.note_name+"/"+addedNote.note_raw);
                            MochiApplication.vaultUpdateService();
                        }
                        else{
                            MochiApplication.notificationService("Database returned null in note ID!",1);
                        }
                    }
                    else{
                        MochiApplication.notificationService("Title or content is empty, cannot save!",1);
                    }
                    break;
                }
                case "share":
                {
                    if ( MochiApplication.currentEditor.currentNote != null )
                    {
                        if ( MochiApplication.currentEditor.currentNote.isShared() ){
                            MochiApplication.currentEditor.currentNote.share();
                            Database_Note dn = new Database_Note(MochiApplication.database);
                            Note note = dn.updateNote(MochiApplication.currentEditor.currentNote);
                            if ( note != null ){
                                MochiApplication.notificationService("Note is shared with URL ("+note.note_url+")",1);
                                MochiApplication.vaultUpdateService();
                                MochiApplication.currentEditor.currentNote= note;
                                MochiApplication.currentEditor.reload(MochiApplication.currentEditor.currentNote);
                            }
                            else{
                                MochiApplication.notificationService("Failed to share note, check log!",1);
                            }
                        }
                        else{
                            MochiApplication.currentEditor.currentNote.note_url = "";
                            Database_Note dn = new Database_Note(MochiApplication.database);
                            Note note = dn.updateNote(MochiApplication.currentEditor.currentNote);
                            if ( note != null ){
                                MochiApplication.notificationService("Note sharing broken, URL removed",1);
                                MochiApplication.vaultUpdateService();
                                MochiApplication.currentEditor.currentNote= note;
                                MochiApplication.currentEditor.reload(MochiApplication.currentEditor.currentNote);
                            }
                            else{
                                MochiApplication.notificationService("Failed to stop sharing note, check log!",1);
                            }
                        }
                    }
                    break;
                }
                case "preview":
                {
                    MochiApplication.currentEditor.currentNote.note_raw = MochiApplication.currentEditor.note_area.getValue();
                    PreviewWindow pw = new PreviewWindow(MochiApplication.currentEditor.currentNote);
                    MochiApplication.currentEditor.add(pw.main_dialog);
                    pw.main_dialog.open();
                    break;
                }
                case "close":
                {
                    MochiApplication.vaultUpdateService();
                    MochiApplication.notificationService("Closed ("+MochiApplication.currentVault.vault_id.toString()+")",1);
                    MochiApplication.currentVault = null;
                    this.getUI().ifPresent(ui ->
                            ui.navigate("/sezame"));
                    break;
                }
                case "clear":
                {
                    MochiApplication.currentEditor.note_area.setValue("");
                    MochiApplication.currentEditor.title_field.setValue("");
                    MochiApplication.currentEditor.currentNote = null;
                    MochiApplication.notificationService("Editor cleared!",1);

                    String header = MochiApplication.currentVault.vault_id.toString() + "/" + MochiApplication.currentVault.vault_name;
                    if ( MochiApplication.currentEditor.currentNote != null ){
                        header = header + "/" + MochiApplication.currentEditor.currentNote.note_url;
                    }
                    MochiApplication.currentEditor.headerString.setText(header);
                    break;
                }
                case "update":
                {
                    MochiApplication.currentEditor.currentNote.note_name = MochiApplication.currentEditor.title_field.getValue();
                    MochiApplication.currentEditor.currentNote.note_raw = MochiApplication.currentEditor.note_area.getValue();
                    Database_Note dn = new Database_Note(MochiApplication.database);
                    if ( dn.updateNote(MochiApplication.currentEditor.currentNote) != null ){
                        MochiApplication.vaultUpdateService();
                        MochiApplication.notificationService("Note ("+MochiApplication.currentEditor.currentNote.note_id.toString()+") updated!",1);
                    }
                    else{
                        MochiApplication.notificationService("Note update failed!",1);
                    }
                    break;
                }
                case "list":
                {
                    if ( MochiApplication.currentEditor.vnl.isVisible()){
                        MochiApplication.currentEditor.vnl.setVisible(false);
                    }
                    else{
                        MochiApplication.currentEditor.vnl.setVisible(true);
                    }
                    break;
                }
                default:
                {
                    MochiApplication.notificationService("Wrong terminal command ("+userInput+")",1);
                    break;
                }
            }
            setValue("");
        }
        else{
            MochiApplication.notificationService("Wrong command usage!",1);
        }
    }
}
