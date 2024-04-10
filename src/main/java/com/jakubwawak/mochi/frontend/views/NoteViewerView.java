/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package com.jakubwawak.mochi.frontend.views;

import com.jakubwawak.mochi.MochiApplication;
import com.jakubwawak.mochi.backend.database.Database_Note;
import com.jakubwawak.mochi.enitity.Note;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

/**
 * View for showing note content
 */
@Route("/note-viewer")
public class NoteViewerView extends VerticalLayout implements HasUrlParameter<String>{

    Button vaultreturn_button;

    /**
     * Constructor
     */
    public NoteViewerView(){
        addClassName("mochi-note-view");
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

    }

    void prepareComponents(){
        vaultreturn_button = new Button("Return to Main Screen", VaadinIcon.HOME.create(),this::setVaultreturn_button);
        vaultreturn_button.addClassName("mochi-button-transparent");
    }

    void prepareNoteView(Note note){
        prepareComponents();

        HorizontalLayout headerLayout = new HorizontalLayout();

        FlexLayout left_layout = new FlexLayout();
        left_layout.setJustifyContentMode(JustifyContentMode.START);
        left_layout.setAlignItems(Alignment.START);
        left_layout.setWidth("90%");

        FlexLayout right_layout = new FlexLayout();
        right_layout.setJustifyContentMode(JustifyContentMode.END);
        right_layout.setAlignItems(FlexComponent.Alignment.END);
        right_layout.setWidth("80%");

        headerLayout.add(left_layout,right_layout);

        headerLayout.setWidth("90%");
        headerLayout.setMargin(true);
        headerLayout.setAlignItems(Alignment.CENTER);
        headerLayout.setVerticalComponentAlignment(Alignment.CENTER);

        left_layout.add(vaultreturn_button);
        right_layout.add(new H6("Note Viewer"));

        add(headerLayout);
        add(new H1(note.getNote_name()));
        add(new H6("Note ID: "+note.note_id+" | "+"Date: "+note.note_creationtime));
        Div editorPreview = new Div();
        MutableDataSet options = new MutableDataSet();
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        Node document = parser.parse(note.note_raw);
        String value = "<body><div>"+renderer.render(document)+"</div></body>";
        editorPreview.getStyle().set("color","white");
        editorPreview.setSizeFull();
        Html htmlPreview = new Html(value);
        htmlPreview.getStyle().set("width","100%"); htmlPreview.getStyle().set("height","100%");
        htmlPreview.getStyle().set("text-algin","left");
        add(htmlPreview);
    }

    void prepareNoParameterView(){
        prepareComponents();
        add(new Text("No parameter - cannot show anything!"));
        add(vaultreturn_button);
    }

    void prepareErrorView(String parameter){
        prepareComponents();
        add(new Text("Error - no note with URL: "+parameter));
        add(vaultreturn_button);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
        if ( parameter == null ){
            // show no paremeter view
            prepareNoParameterView();
        }
        else{
            String note_url = parameter;
            // check if note_url exists - show the note
            Database_Note dn = new Database_Note(MochiApplication.database);
            Note note = dn.loadNote(note_url);
            if ( note != null ){
                // prepare note view
                prepareNoteView(note);
            }
            else{
                // show error screen
                prepareErrorView(parameter);
            }
        }
    }

    private void setVaultreturn_button(ClickEvent ex){
        vaultreturn_button.getUI().ifPresent(ui ->
                ui.navigate("/sezame"));
    }
}
