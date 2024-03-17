/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com / j.wawak@usp.pl
 */
package com.jakubwawak.mochi.frontend.windows;

import com.jakubwawak.mochi.enitity.Vault;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Object for creating component for downloading file
 */
public class DownloadMkeyWindow {

    public Dialog dialog;
    VerticalLayout main_layout;

    Button close_button;

    File to_download;
    Vault vault;

    /**
     * Constructor
     */
    public DownloadMkeyWindow(File file_to_download, Vault vault){
        dialog = new Dialog();
        dialog.addClassName("mochi-dialog-window");
        this.to_download = file_to_download;
        this.vault = vault;
        main_layout = new VerticalLayout();
        close_button = new Button("Close Window",this::close_action);
        close_button.addClassName("mochi-button-transparent");
        prepare_dialog();
    }

    /**
     * Function for loading components
     */
    void prepare_dialog(){
        main_layout.add(VaadinIcon.KEY.create());
        main_layout.add(new H3("mKey download"));
        main_layout.add(new Text("To download the vault key, click on the link below:"));
        addLinkToFile(to_download);
        main_layout.add(new Text("Be careful! This is the only way for you to access your vault!"));
        main_layout.add(new H2("Your vault password: "+vault.vault_code));
        main_layout.add(close_button);

        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        main_layout.getStyle().set("text-align", "center");
        dialog.add(main_layout);
    }

    /**
     * Function for adding link to file
     * @param file
     */
    private void addLinkToFile(File file) {
        StreamResource streamResource = new StreamResource(file.getName(), () -> getStream(file));
        Anchor link = new Anchor(streamResource, String.format("%s (%d KB)", file.getName(),
                (int) file.length() / 1024));
        link.getElement().setAttribute("download", true);
        main_layout.add(link);
    }

    /**
     * Function for loading stream of file to object
     * @param file
     * @return InputStream
     */
    private InputStream getStream(File file) {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println("Failed to load stream file: "+e.toString());
        }
        return stream;
    }

    /**
     * Function for closing dialog window
     * @param e
     */
    private void close_action(ClickEvent e){
        dialog.close();
    }
}