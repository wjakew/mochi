/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.mochi.enitity;

import java.time.LocalDateTime;
import org.bson.Document;

/**
 * Entity for storing table: Mochi
 */
public class Mochi {

    public String database_lastupdate;
    public String application_owner;
    public int api_enabled;
    public int vaultcreation_enabled;
    public int visual_mode;

    /**
     * Constructor
     */
    public Mochi(){
        database_lastupdate = LocalDateTime.now().toString();
        application_owner = "none";
        api_enabled = 1;
        vaultcreation_enabled = 1;
        visual_mode = 1;
    }

    /**
     * Constructor with database Document
     * @param document
     */
    public Mochi(Document document){
        database_lastupdate = document.getString("database_lastupdate");
        application_owner = document.getString("application_owner");
        api_enabled = document.getInteger("api_enabled");
        vaultcreation_enabled = document.getInteger("vaultcreation_enabled");
        visual_mode = document.getInteger("visual_mode");
    }

    /**
     * Function for preparing document
     * @return Document
     */
    public Document prepareDocument(){
        Document document = new Document();
        document.append("database_lastupdate",database_lastupdate);
        document.append("application_owner",application_owner);
        document.append("api_enabled",api_enabled);
        document.append("vaultcreation_enabled",vaultcreation_enabled);
        document.append("visual_mode",visual_mode);
        return document;
    }
}
