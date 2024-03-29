/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
*/
package com.jakubwawak.mochi.frontend.components.mochiheader;

import com.jakubwawak.mochi.MochiApplication;
import com.jakubwawak.mochi.enitity.Vault;
import com.jakubwawak.mochi.frontend.components.VaultNotesMenu;
import com.jakubwawak.mochi.frontend.views.OpenVaultView;
import com.jakubwawak.mochi.frontend.windows.MochiOptionsWindow;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.StreamResource;

import java.util.Date;

/**
 * Component for creating bottom app header
 */
public class MochiHeader extends HorizontalLayout {

    FlexLayout left_layout,right_layout;

    TextField terminal_field;

    Button mochi_button;



    /**
     * Constructor
     */
    public MochiHeader(){
        addClassName("mochi-header");
        prepare_header();
    }

    void prepare_header(){

        mochi_button = new Button("mochi",this::setMochi_button);
        mochi_button.addClassName("mochi-header-button");

        terminal_field = new TextField();
        terminal_field.setPlaceholder("command...");terminal_field.setPrefixComponent(VaadinIcon.TERMINAL.create());
        terminal_field.setWidth("80%");
        terminal_field.addClassName("mochi-terminal-field");

        MochiApplication.vaultNotesMenu = new VaultNotesMenu();

        FlexLayout left_layout = new FlexLayout();
        left_layout.setJustifyContentMode(JustifyContentMode.CENTER);
        left_layout.setAlignItems(Alignment.CENTER);
        left_layout.setWidth("90%");

        FlexLayout center_layout = new FlexLayout();
        center_layout.setSizeFull();
        center_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        center_layout.setAlignItems(FlexComponent.Alignment.CENTER);
        center_layout.add(MochiApplication.vaultNotesMenu);

        FlexLayout right_layout = new FlexLayout();
        right_layout.setJustifyContentMode(JustifyContentMode.END);
        right_layout.setAlignItems(FlexComponent.Alignment.END);
        right_layout.setWidth("80%");

        StreamResource res = new StreamResource("aim_logo.png", () -> {
            return OpenVaultView.class.getClassLoader().getResourceAsStream("images/mochi_icon.png");
        });
        Image logo = new Image(res,"aim logo");
        logo.setHeight("4rem");
        logo.setWidth("4rem");

        left_layout.add(logo,terminal_field);
        right_layout.add(mochi_button);

        add(left_layout,center_layout,right_layout);
        setWidth("90%");
        setMargin(true);
        setAlignItems(Alignment.CENTER);
        setVerticalComponentAlignment(Alignment.CENTER);
    }

    /**
     * mochi_button action
     * @param ex
     */
    private void setMochi_button(ClickEvent ex){
        MochiOptionsWindow mow = new MochiOptionsWindow();
        add(mow.main_dialog);
        mow.main_dialog.open();
    }

}
