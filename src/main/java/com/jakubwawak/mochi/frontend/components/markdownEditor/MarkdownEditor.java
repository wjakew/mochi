/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package com.jakubwawak.mochi.frontend.components.markdownEditor;

import com.jakubwawak.mochi.MochiApplication;
import com.jakubwawak.mochi.backend.database.Database_Note;
import com.jakubwawak.mochi.enitity.Note;
import com.jakubwawak.mochi.enitity.Vault;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import com.vladsch.flexmark.parser.Parser;
import org.yaml.snakeyaml.error.Mark;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Object for creating markdown editor
 */
public class MarkdownEditor extends VerticalLayout {

    public String currentValue;
    public String mode = "DARK"; // values - DARK / LIGHT
    public SplitLayout splitLayout;

    TextArea editorArea;

    Div editorPreview;
    Html htmlPreview;

    Button saveupdate_button;
    String saveupdateText;

    TextField name_field;

    HorizontalLayout header;
    Note note;
    /**
     * Costructor
     */
    public MarkdownEditor(Note note){
        addClassName("mochi-md-editor");
        if ( note == null ){
            this.note = new Note();
            saveupdateText = "Save";
        }
        else{
            this.note = note;
            saveupdateText = "Update";
        }
        this.currentValue = this.note.note_raw;
        prepareCompontents();
        // style layout
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
        // prepare layout
        prepareLayout();
    }

    /**
     * Function for update component from different
     * @param content
     */
    public void update(String content){
        editorArea.setValue(content);
        parseToHtml();
    }

    /**
     * Function for adding component to source
     * @param component
     */
    public void addComponent(String component){
        editorArea.setValue(editorArea.getValue()+"\n"+component);
        parseToHtml();
    }

    /**
     * Function for parsing string to md as HTML
     * @return String
     */
    String parseToHtml(){
        currentValue = editorArea.getValue();
        note.note_raw = currentValue;
        editorArea.setValue(currentValue);
        MutableDataSet options = new MutableDataSet();
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        Node document = parser.parse(currentValue);
        String value = "<body><div>"+renderer.render(document)+"</div></body>";
        editorPreview.getStyle().set("color","black");
        return value;
    }

    /**
     * Function for refreshing preview from other component
     */
    public void refreshPreview(){
        parseToHtml();
        showNotification("Preview refreshed!");
    }

    /**
     * Function for preparing components
     */
    void prepareCompontents() {
        saveupdate_button = new Button(saveupdateText,VaadinIcon.DISC.create(),this::setSaveupdate_button);
        saveupdate_button.addClassName("mochi-button-dark");

        name_field = new TextField();
        name_field.setPlaceholder("note title");
        name_field.addClassName("mochi-terminal-field");
        name_field.setWidth("70%");
        name_field.setValue(note.note_name);

        editorArea = new TextArea();
        editorArea.addClassName("mochi-terminal-field");
        editorArea.setSizeFull();
        editorArea.setValue(currentValue);

        editorPreview = new Div();
        editorPreview.setSizeFull();
        editorPreview.getStyle().set("font-family","Monospace");
        editorPreview.getStyle().set("text-align","left");

        htmlPreview= new Html(parseToHtml());
        htmlPreview.addClassName("mochi-md-editor-preview");
        editorPreview.add(htmlPreview);

        editorArea.addValueChangeListener(e -> {
            htmlPreview = new   Html(parseToHtml());
            editorPreview.removeAll();
            editorPreview.add(htmlPreview);
        });
        editorArea.addBlurListener(e -> {
            htmlPreview = new   Html(parseToHtml());
            editorPreview.removeAll();
            editorPreview.add(htmlPreview);
        });
    }

    /**
     * Function for preparing main layout
     */
    void prepareLayout(){

        // set color mode
        if ( mode.equals("DARK") ){
            this.getElement().setAttribute("theme", Lumo.DARK);
            //getStyle().set("background-image","linear-gradient(gray,black)");
            getStyle().set("color","black");
        }
        else{
            this.getElement().setAttribute("theme", Lumo.LIGHT);
            //getStyle().set("background-image","linear-gradient(gray,white)");
            getStyle().set("color","black");
            editorArea.getStyle().set("color","black");
        }

        prepareHeader();

        VerticalLayout leftEditorLayout = new VerticalLayout(new H6("editor"),editorArea);
        leftEditorLayout.setSizeFull();
        leftEditorLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        leftEditorLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        leftEditorLayout.getStyle().set("text-align", "center");

        VerticalLayout rightPreviewLayout = new VerticalLayout(new H6("preview"),editorPreview);
        rightPreviewLayout.setSizeFull();
        rightPreviewLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        rightPreviewLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        rightPreviewLayout.getStyle().set("text-align", "center");

        splitLayout = new SplitLayout(leftEditorLayout,rightPreviewLayout);
        setSizeFull();
        splitLayout.setSizeFull();
        splitLayout.setSplitterPosition(70);

        add(header);
        add(splitLayout);
    }

    /**
     * Function for preparing header data
     */
    void prepareHeader(){
        header = new HorizontalLayout();

        // prepare window layout and components
        FlexLayout center_layout = new FlexLayout();
        center_layout.setSizeFull();
        center_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        center_layout.setAlignItems(FlexComponent.Alignment.CENTER);

        FlexLayout left_layout = new FlexLayout();
        left_layout.setSizeFull();
        left_layout.setJustifyContentMode(JustifyContentMode.START);
        left_layout.setAlignItems(Alignment.CENTER);
        left_layout.add(saveupdate_button,name_field);

        FlexLayout right_layout = new FlexLayout();
        right_layout.setSizeFull();
        right_layout.setJustifyContentMode(JustifyContentMode.END);
        right_layout.setAlignItems(Alignment.CENTER);

        header.add(left_layout,center_layout,right_layout);
        header.setWidth("100%");
        header.setMargin(true);
        header.getStyle().set("color","black");
        header.getStyle().set("border-radius","15px");

        header.setMargin(true);
        header.setAlignItems(Alignment.CENTER);
        header.setVerticalComponentAlignment(Alignment.CENTER);
    }


    // interconnectors

    /**
     * Function for showing notification on the component
     * @param notificationText
     */
    public void showNotification(String notificationText){
        Notification notification = Notification.show(notificationText, 5000,
                Notification.Position.MIDDLE);
        notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
    }

    /**
     * Function for reloading note
     * @param note
     */
    public void reloadNote(Note note){
        if ( note != null ){
            this.note = note;
            name_field.setValue(this.note.note_name);
            saveupdateText = "Update";
            saveupdate_button.setText(saveupdateText);
            editorArea.setValue(this.note.note_raw);
            refreshPreview();
        }
    }

    /**
     * Function for saving note to vault
     * @param ex
     */
    public void setSaveupdate_button(ClickEvent ex){
        if ( saveupdateText.equals("Save")){
            note.note_raw = editorArea.getValue();
            note.note_name = name_field.getValue();
            Database_Note dn = new Database_Note(MochiApplication.database);
            Note savedNote = dn.insertNote(note);
            if ( savedNote != null ){
                MochiApplication.vaultManager.addNoteToVault(savedNote);
                note = savedNote;
                saveupdateText = "Update";
                saveupdate_button.setText(saveupdateText);
            }
        }
        else{
            // update note
            note.note_raw = editorArea.getValue();
            note.note_name = name_field.getValue();
            Database_Note dn = new Database_Note(MochiApplication.database);
            Note updatedNote = dn.updateNote(note);
            if ( updatedNote != null ){
                saveupdateText = "Update";
                saveupdate_button.setText(saveupdateText);
            }
        }

    }
}
