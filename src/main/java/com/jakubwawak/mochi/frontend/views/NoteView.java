/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package com.jakubwawak.mochi.frontend.views;

import com.jakubwawak.mochi.MochiApplication;
import com.jakubwawak.mochi.backend.database.Database_Note;
import com.jakubwawak.mochi.enitity.Mochi;
import com.jakubwawak.mochi.enitity.Note;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
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
public class NoteView extends VerticalLayout implements HasUrlParameter<String>{

    Button vaultreturn_button;

    /**
     * Constructor
     */
    public NoteView(){
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
        add(new Text(note.getNote_name()));
        add(vaultreturn_button);
        Div editorPreview = new Div();
        MutableDataSet options = new MutableDataSet();
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        Node document = parser.parse(note.note_raw);
        String value = "<body><div>"+renderer.render(document)+"</div></body>";
        editorPreview.getStyle().set("color","white");
        Html htmlPreview = new Html(value);
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
