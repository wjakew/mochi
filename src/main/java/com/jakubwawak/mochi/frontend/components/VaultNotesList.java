/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
*/
package com.jakubwawak.mochi.frontend.components;

import com.jakubwawak.mochi.MochiApplication;
import com.jakubwawak.mochi.enitity.Note;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;

import java.util.Set;

public class VaultNotesList extends VerticalLayout {

    /**
     * Consturctor
     */

    Grid<Note> noteGrid;

    public VaultNotesList(){
        noteGrid = new Grid<>(Note.class,false);
        noteGrid.addClassName("mochi-vault-list");
        prepareLayout();
    }

    /**
     * Function for preparing components
     */
    void prepareComponents(){
        noteGrid.addColumn(createCTaskRenderer()).setHeader(MochiApplication.currentVault.vault_name);
        noteGrid.setItems(MochiApplication.currentVault.vault_notes_objects);

        noteGrid.addItemClickListener(e->{
            Set<Note> selected = noteGrid.getSelectedItems();
            for(Note note : selected){
                MochiApplication.markdownEditor.reloadNote(note);
                break;
            }
        });
    }

    /**
     * Function for reloading grid
     */
    public void reloadGrid(){
        noteGrid.getDataProvider().refreshAll();
    }

    /**
     * Renderer for aim coding object
     * @return Renderer
     */
    private static Renderer<Note> createCTaskRenderer() {
        return LitRenderer.<Note> of(
                        "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                                + "  <vaadin-vertical-layout style=\"line-height: var(--lumo-line-height-m);\">"
                                + "    <span> ${item.fullName} </span>"
                                + "    <span style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">"
                                + "      ${item.email}" + "    </span>"
                                + "  </vaadin-vertical-layout>"
                                + "</vaadin-horizontal-layout>")
                .withProperty("fullName", Note::getNote_name)
                .withProperty("email", Note::getNoteCreationTime);
    }


    /**
     * Function for creating and loading layout
     */
    void prepareLayout(){
        prepareComponents();

        add(noteGrid);

        setSizeFull();
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        getStyle().set("text-align", "center");
    }
}
