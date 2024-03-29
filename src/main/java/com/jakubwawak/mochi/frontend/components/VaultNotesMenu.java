/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.mochi.frontend.components;

import com.jakubwawak.mochi.MochiApplication;
import com.jakubwawak.mochi.backend.database.Database_Note;
import com.jakubwawak.mochi.enitity.Note;
import com.jakubwawak.mochi.enitity.Vault;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.html.H6;

import java.util.ArrayList;


/**
 * Function for creating menu for storing notes
 */
public class VaultNotesMenu extends MenuBar {

    MenuItem menuItemHeader;
    SubMenu subItems;
    ComponentEventListener<ClickEvent<MenuItem>> listener;
    ArrayList<MenuItem> notesMenuItems;

    /**
     * Constructor
     */
    public VaultNotesMenu(){
        super();
        notesMenuItems = new ArrayList<>();
        addClassName("vault-notes-picker");
        menuItemHeader = this.addItem(MochiApplication.currentVault.vault_name);
        menuItemHeader.addClassName("mochi-header-button");

        listener = event -> {
            MenuItem selectedItem = event.getSource();
            String noteName = selectedItem.getText();
            Database_Note dn = new Database_Note(MochiApplication.database);
            MochiApplication.markdownEditor.reloadNote(dn.getNotebyName(noteName));
            System.out.println(selectedItem.getText());
        };

        reload();
    }

    /**
     * Function for reloading notes in object
     */
    public void reload(){
        // clearing old entries
        subItems = menuItemHeader.getSubMenu();
        subItems.removeAll();
        notesMenuItems.clear();
        // loading current notes
        for(Note note : MochiApplication.currentVault.vault_notes_objects){
            MenuItem menuItem = subItems.addItem(note.getNote_name());
            menuItem.setCheckable(false);
            menuItem.setChecked(false);
            notesMenuItems.add(menuItem);
            menuItem.addClickListener(listener);
        }
    }
}
