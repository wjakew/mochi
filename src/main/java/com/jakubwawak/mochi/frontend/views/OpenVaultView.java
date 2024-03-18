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
import com.vaadin.flow.component.Text;
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
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
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
    Button openVault_button, createVault_button;

    MemoryBuffer memoryBuffer;
    HorizontalLayout mainLayout;
    VerticalLayout leftLayout,rightLayout;

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
        logo.setHeight("30rem");
        logo.setWidth("30rem");

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
        keyUploadComponent.setWidth("100%");
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

        openVault_button = new Button("Open Vault",VaadinIcon.KEY.create());
        openVault_button.setWidthFull();
        openVault_button.addClassName("mochi-button-bright");

        createVault_button = new Button("Create...",VaadinIcon.LOCK.create(),this::setCreateVault_button);
        createVault_button.setWidth("50%");createVault_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);
        createVault_button.addClassName("mochi-button-dark");

        mainLayout = new HorizontalLayout();
        mainLayout.setSizeFull();
        mainLayout.setVerticalComponentAlignment(Alignment.CENTER);mainLayout.setAlignItems(Alignment.CENTER);
        mainLayout.addClassName("openvaultview-mainlayout");

        leftLayout = new VerticalLayout();
        leftLayout.addClassName("openvaultview-leftlayout");
        leftLayout.setWidthFull();leftLayout.setHeight("50%");
        leftLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        leftLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        leftLayout.getStyle().set("text-align", "center");

        rightLayout = new VerticalLayout();
        rightLayout.addClassName("openvaultview-rightlayout");
        rightLayout.setWidth("70%");rightLayout.setHeight("50%");
        rightLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        rightLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        rightLayout.getStyle().set("text-align", "center");

        rightLayout.add(VaadinIcon.LOCK.create());
        rightLayout.add(new H6("OPEN YOUT VAULT"));
        rightLayout.add(keyUploadComponent);
        rightLayout.add(openVault_button);
        rightLayout.add(createVault_button);

        leftLayout.add(logo);
        leftLayout.add(new H6(MochiApplication.version+"/"+MochiApplication.build));

        mainLayout.add(leftLayout,rightLayout);
    }

    /**
     * Function for preparing layout data
     */
    void prepareLayout(){
        prepareComponents();

        add(mainLayout);

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