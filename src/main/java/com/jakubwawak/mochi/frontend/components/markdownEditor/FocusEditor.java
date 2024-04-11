/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package com.jakubwawak.mochi.frontend.components.markdownEditor;

import com.jakubwawak.mochi.MochiApplication;
import com.jakubwawak.mochi.enitity.Mochi;
import com.jakubwawak.mochi.enitity.Note;
import com.jakubwawak.mochi.frontend.components.VaultNotesList;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

/**
 * Main Editor for creating focus mode
 */
public class FocusEditor extends VerticalLayout {

    public TextField title_field;
    public TextArea note_area;

    public Note currentNote;

    public FocusEditorTerminal terminal;

    public VaultNotesList vnl;
    public Text headerString;

    /**
     * Constructor
     */
    public FocusEditor(Note currentNote){
        this.currentNote = currentNote;
        terminal = new FocusEditorTerminal();
        vnl = new VaultNotesList();
        prepareComponents();
        addClassName("mochi-md-editor");
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.START);
        setDefaultHorizontalComponentAlignment(Alignment.START);
        prepareLayout();
    }

    /**
     * Function for preparing components
     */
    void prepareComponents(){
        title_field = new TextField();
        title_field.setPlaceholder("title...");
        title_field.setWidthFull();
        title_field.addClassName("focus-editor-titlearea");

        note_area = new TextArea();
        note_area.setPlaceholder("note content...");
        note_area.setSizeFull();
        note_area.addClassName("focus-editor-textarea");
    }

    /**
     * Function for reloading note content
     */
    void reload(){
        title_field.setValue(currentNote.note_name);
        note_area.setValue(currentNote.note_raw);
    }

    /**
     * Function for reloading content to given note
     * @param note
     */
    public void reload(Note note){
        currentNote = note;
        title_field.setValue(currentNote.note_name);
        note_area.setValue(currentNote.note_raw);

        String header = MochiApplication.currentVault.vault_id.toString() + "/" + MochiApplication.currentVault.vault_name;
        if ( currentNote != null ){
            header = header + "/" + currentNote.note_url;
        }
        headerString.setText(header);
    }

    /**
     * Function for preparing layout
     */
    void prepareLayout(){
        add(title_field);

        HorizontalLayout centerLayout = new HorizontalLayout();
        centerLayout.setAlignItems(Alignment.START);
        centerLayout.setVerticalComponentAlignment(Alignment.START);
        centerLayout.setSizeFull();

        centerLayout.add(note_area,vnl);
        centerLayout.getStyle().set("overflow","hidden");

        vnl.setVisible(false);
        vnl.reloadGrid();

        add(centerLayout);

        FlexLayout left_layout = new FlexLayout();
        left_layout.setSizeFull();
        left_layout.setJustifyContentMode(JustifyContentMode.CENTER);
        left_layout.setAlignItems(Alignment.CENTER);
        left_layout.setWidthFull();

        String header = MochiApplication.currentVault.vault_id.toString() + "/" + MochiApplication.currentVault.vault_name;
        if ( currentNote != null ){
            header = header + "/" + currentNote.note_url;
        }
        headerString = new Text(header);
        left_layout.add(headerString);

        FlexLayout right_layout = new FlexLayout();
        right_layout.setSizeFull();
        right_layout.setJustifyContentMode(JustifyContentMode.START);
        right_layout.setAlignItems(FlexComponent.Alignment.END);
        right_layout.setWidthFull();
        right_layout.add(terminal);

        HorizontalLayout bottomHeader = new HorizontalLayout();
        bottomHeader.setMargin(true);
        bottomHeader.setAlignItems(Alignment.START);
        bottomHeader.setVerticalComponentAlignment(Alignment.START);
        bottomHeader.add(left_layout,right_layout);

        add(bottomHeader);
    }
}
