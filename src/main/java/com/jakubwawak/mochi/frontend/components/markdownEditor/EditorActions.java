/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
*/
package com.jakubwawak.mochi.frontend.components.markdownEditor;

import com.jakubwawak.mochi.MochiApplication;

/**
 * Object for creating actions based on markdown editor content
 */
public class EditorActions {

    MarkdownEditor markdownEditor;

    /**
     * Constructor
     */
    public EditorActions(MarkdownEditor markdownEditor){
        this.markdownEditor = markdownEditor;
    }

    /**
     * Function for clearing editor data
     */
    public void clearEditor(){
        markdownEditor.editorArea.setValue("");
        markdownEditor.refreshPreview();
        markdownEditor.saveupdate_button.setText("Save");
    }

    /**
     * Function for setting editor split size
     */
    public void setEditorSplit(int split){
        markdownEditor.splitLayout.setSplitterPosition(split);
    }
}
