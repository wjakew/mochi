/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
*/
package com.jakubwawak.mochi.frontend.views;

import com.jakubwawak.mochi.MochiApplication;
import com.jakubwawak.mochi.frontend.windows.CreateVaultWindow;
import com.jakubwawak.mochi.frontend.windows.VaultPasswordWindow;
import com.vaadin.flow.component.ClickEvent;
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

    Upload keyUploadComponent;
    Button upload_button;
    Button createVault_button;

    MemoryBuffer memoryBuffer;
    VerticalLayout centerLayout;

    /**
     * Constructor
     */
    public OpenVaultView(){
        this.getElement().setAttribute("theme", Lumo.DARK);
        addClassName("openvaultview");
        prepareLayout();
    }

    /**
     * Function for preparing components
     */
    void prepareComponents(){

        StreamResource res = new StreamResource("aim_logo.png", () -> {
            return OpenVaultView.class.getClassLoader().getResourceAsStream("images/mochi_icon.png");
        });
        Image logo = new Image(res,"aim logo");
        logo.setHeight("15rem");
        logo.setWidth("15rem");

        upload_button = new Button("Upload Key!");
        upload_button.addClassName("mochi-button-transparent");
        Span dropLabel = new Span("");
        Icon dropIcon = VaadinIcon.KEY.create();

        memoryBuffer = new MemoryBuffer();
        keyUploadComponent = new Upload(memoryBuffer);
        keyUploadComponent.setUploadButton(upload_button);
        keyUploadComponent.setDropLabel(dropLabel);
        keyUploadComponent.setDropLabelIcon(dropIcon);
        keyUploadComponent.setMaxFiles(1);
        int maxFileSizeInBytes =  1024 * 1024; // 1MB
        keyUploadComponent.setMaxFileSize(maxFileSizeInBytes);
        keyUploadComponent.setAcceptedFileTypes("application/mkey", ".mkey");
        keyUploadComponent.setWidth("50%");
        keyUploadComponent.addClassName("openvaultview-upload");

        keyUploadComponent.addSucceededListener(event -> {
            // Get information about the uploaded file
            try{
                InputStream fileData = memoryBuffer.getInputStream();
                String fileName = event.getFileName();
                long contentLength = event.getContentLength();
                String mimeType = event.getMIMEType();

                // Do something with the file data
                // processFile(fileData, fileName, contentLength, mimeType);
                Scanner s = new Scanner(fileData).useDelimiter("\\A");
                String vault_hash = s.hasNext() ? s.next() : "";

                VaultPasswordWindow vpw = new VaultPasswordWindow(vault_hash);
                add(vpw.main_dialog);
                vpw.main_dialog.open();
                keyUploadComponent.clearFileList();
            }catch(Exception ex){
                MochiApplication.notificationService("Failed: "+ex.toString(),4);
            }
        });

        keyUploadComponent.addFileRejectedListener(event -> {
            String errorMessage = event.getErrorMessage();
            Notification notification = Notification.show("File is not a vault key", 5000,
                    Notification.Position.MIDDLE);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        });

        createVault_button = new Button("Create new Vault",VaadinIcon.SAFE.create(),this::setCreateVault_button);
        createVault_button.setWidth("50%");createVault_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);
        createVault_button.addClassName("mochi-button-dark");


        centerLayout = new VerticalLayout();
        centerLayout.addClassName("centerlayout");
        centerLayout.setWidth("50%");
        centerLayout.setHeight("70%");
        centerLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        centerLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        centerLayout.getStyle().set("text-align", "center");

        centerLayout.add(logo);
        centerLayout.add(new H3("mochi"));
        centerLayout.add(keyUploadComponent);
        centerLayout.add(createVault_button);
    }

    /**
     * Function for preparing layout data
     */
    void prepareLayout(){
        prepareComponents();

        add(centerLayout);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

    /**
     * createvault_button action
     * @param ex
     */
    private void setCreateVault_button(ClickEvent ex){
        CreateVaultWindow cvw = new CreateVaultWindow();
        add(cvw.main_dialog);
        cvw.main_dialog.open();
    }

}