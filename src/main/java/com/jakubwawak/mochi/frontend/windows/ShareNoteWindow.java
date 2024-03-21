/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.mochi.frontend.windows;

import com.jakubwawak.mochi.MochiApplication;
import com.jakubwawak.mochi.backend.database.Database_Note;
import com.jakubwawak.mochi.enitity.Mochi;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.jakubwawak.mochi.enitity.Note;

/**
 * Window for logging user to the app
 */
public class ShareNoteWindow {

    // variables for setting x and y of window
    public String width = "40%";
    public String height = "40%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    Text headerText;
    Button shareaction_button;

    Button visitlink_button;

    Note note;

    /**
     * Constructor
     */
    public ShareNoteWindow(Note note){
        this.note = note;
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();
        main_dialog.addClassName("mochi-dialog-window");

        Database_Note dn = new Database_Note(MochiApplication.database);
        try{
            this.note = dn.getNote(note.note_id);
            if ( this.note != null ){
                prepare_dialog();
            }
            else{
                prepare_dialog_error();
            }
        }catch (Exception ex){
            MochiApplication.database.log("SHARE-NOTE-WINDOW-FAILED","Failed ("+ex.toString()+")");
            prepare_dialog_error();
        }

    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        headerText = new Text("");
        shareaction_button = new Button("", VaadinIcon.SHARE.create(),this::setShareaction_button);
        shareaction_button.addClassName("mochi-editor-button");

        visitlink_button = new Button("Visit shared note page!",VaadinIcon.NOTEBOOK.create());
        visitlink_button.addClassName("mochi-button-transparent");

        if ( this.note.note_url.isEmpty() ){
            headerText.setText("Note is not shared!");
            visitlink_button.setVisible(false);
            shareaction_button.setText("Share Note!");
        }
        else{
            visitlink_button.setVisible(true);
            shareaction_button.setText("Stop sharing");
        }
    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        main_layout.add(headerText,visitlink_button,shareaction_button);
        // set layout
        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        main_layout.getStyle().set("text-align", "center");

        main_dialog.add(main_layout);
        main_dialog.setWidth(width);main_dialog.setHeight(height);
    }

    /**
     * Function for creating layout with error
     */
    void prepare_dialog_error(){
        main_layout.add(new Text("Failed, note ("+note.note_id.toString()+") is not in the database!"));
        // set layout
        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        main_layout.getStyle().set("text-align", "center");

        main_dialog.add(main_layout);
        main_dialog.setWidth(width);main_dialog.setHeight(height);
    }

    /**
     * shareaction_button action
     * @param ex
     */
    private void setShareaction_button(ClickEvent ex){
        if ( shareaction_button.getText().contains("Note") ){
            note.share();
            Database_Note dn = new Database_Note(MochiApplication.database);
            if (dn.updateNote(note) != null ){
                MochiApplication.notificationService("Note ("+note.note_id+") is shared!",1);
                main_dialog.close();
            }
            else{
                MochiApplication.notificationService("Failed to share note, check log!",4);
            }
        }
        else{
            note.removeshare();
            Database_Note dn = new Database_Note(MochiApplication.database);
            if (dn.updateNote(note) != null ){
                MochiApplication.notificationService("Note ("+note.note_id+") removed sharing!",3);
                main_dialog.close();
            }
            else{
                MochiApplication.notificationService("Failed to share note, check log!",4);
            }
        }
    }
}