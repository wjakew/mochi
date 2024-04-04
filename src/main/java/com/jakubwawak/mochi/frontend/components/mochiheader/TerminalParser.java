/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
*/
package com.jakubwawak.mochi.frontend.components.mochiheader;

import com.jakubwawak.mochi.MochiApplication;
import com.jakubwawak.mochi.frontend.components.markdownEditor.EditorActions;

import java.util.ArrayList;

/**
 * Object for parsing terminal data
 */
public class TerminalParser {

    public ArrayList<String> inputHistory;

    EditorActions editorActions;

    /**
     * Constructor
     */
    public TerminalParser(){
        inputHistory = new ArrayList<>();
        editorActions = new EditorActions(MochiApplication.markdownEditor);
    }

    /**
     * Function for creating action based on input
     * @param userInput
     */
    public void createAction(String userInput){
        if ( userInput.contains("editor") ){
            // editor
        }
        else if ( userInput.contains("mochi") ){
            // mochi
        }
        else{
            MochiApplication.notificationService("Wrong command!",3);
        }
    }
}
