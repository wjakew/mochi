/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package com.jakubwawak.mochi.frontend.components.markdownEditor;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

/**
 * Main Editor for creating focus mode
 */
public class FocusEditor extends VerticalLayout {

    public TextField title_field;
    public TextArea note_area;

    /**
     * Constructor
     */
    public FocusEditor(){
        prepareComponents();
        addClassName("mochi-md-editor");
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
        prepareLayout();
    }

    /**
     * Function for preparing components
     */
    void prepareComponents(){
        title_field = new TextField();
        title_field.setPlaceholder("title...");
        title_field.setWidthFull();
        title_field.addClassName("focus-editor-textfield");

        note_area = new TextArea();
        note_area.setPlaceholder("note content...");
        note_area.setSizeFull();
        note_area.addClassName("focus-editor-textarea");
    }

    /**
     * Function for preparing layout
     */
    void prepareLayout(){
        add(title_field);
        add(note_area);
    }
}
