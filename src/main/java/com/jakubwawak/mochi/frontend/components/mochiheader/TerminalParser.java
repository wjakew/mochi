/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
*/
package com.jakubwawak.mochi.frontend.components.mochiheader;

import com.jakubwawak.mochi.MochiApplication;
import com.jakubwawak.mochi.enitity.Mochi;
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
            String[] words = userInput.split(" ");
            if ( words.length > 1 ){
                // editor commands
                switch(words[1]){
                    case "split":
                    {
                        // resize editor
                        if ( words.length == 3 ){
                            try{
                                int size = Integer.parseInt(words[2]);
                                editorActions.setEditorSplit(size);
                                MochiApplication.notificationService("Editor split changed!",1);
                            }catch(Exception e){
                                MochiApplication.notificationService("Wrong size amount!",4);
                            }
                        }
                        else{
                            MochiApplication.notificationService("Wrong editor command usage!",3);
                        }
                        break;
                    }
                    case "clear":
                    {
                        if ( words.length == 2 ){
                            editorActions.clearEditor();
                            MochiApplication.notificationService("Editor cleared!",1);
                        }
                        else{
                            MochiApplication.notificationService("Wrong editor command usage!",3);
                        }
                    }
                }
            }
            else{
                // show error
            }
        }
        else if ( userInput.contains("mochi") ){
            // mochi
        }
        else{
            MochiApplication.notificationService("Wrong command!",3);
        }
    }
}
