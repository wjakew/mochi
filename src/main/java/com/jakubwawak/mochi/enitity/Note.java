/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.mochi.enitity;

import com.jakubwawak.mochi.maintanance.RandomString;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Note {

    public ObjectId note_id;
    public String note_name;
    public String note_tags;
    public String note_creationtime;

    public String note_code;   // for storing code for secured notes

    public String note_url;    // for storing url for sharing notes - if empty note is not shared
    public List<String> note_log;
    public String note_raw;

    /**
     * Constructor
     */
    public Note(){
        note_id = null;
        note_name = "";
        note_tags = "";
        note_creationtime = LocalDateTime.now().toString();
        note_log = new ArrayList<>();
        note_code = "";
        note_url = "";
        note_raw = "";
    }

    /**
     * Constructor with database support
     * @param document
     */
    public Note(Document document){
        note_id = document.getObjectId("_id");
        note_name = document.getString("note_name");
        note_tags = document.getString("note_tags");
        note_creationtime = document.getString("note_creationtime");
        note_log = document.getList("note_log",String.class);
        note_raw = document.getString("note_raw");
        note_url = document.getString("note_url");
        note_code = document.getString("note_code");
    }

    /**
     * Function for creating document to update/put on database
     * @return Document
     */
    public Document prepareDocument(){
        Document document = new Document();
        document.append("note_name",note_name);
        document.append("note_tags",note_tags);
        document.append("note_creationtime",note_creationtime);
        document.append("note_log",note_log);
        document.append("note_raw",note_raw);
        document.append("note_url",note_url);
        document.append("note_code",note_code);
        return document;
    }

    public String getNote_name(){
        return note_name;
    }

    public String getNoteCreationTime(){
        return note_creationtime;
    }

    /**
     * Function for adding log to note object
     * @param log
     */
    public void addLog(String log){
        note_log.add(LocalDateTime.now().toString()+" - "+log);
    }

    /**
     * Function for checking if note is shared
     * @return boolean
     */
    public boolean isShared(){
        return note_url.isEmpty();
    }

    /**
     * Function for creating share URL on objects
     * @return String
     */
    public String share(){
        RandomString rs = new RandomString(20);
        note_url = rs.nextString();
        addLog("Note is shared. Sharing started at URL: "+note_url);
        return note_url;
    }

    /**
     * Function for removing note sharing
     */
    public void removeshare(){
        note_url = "";
        addLog("Removed note sharing from URL.");
    }
}
