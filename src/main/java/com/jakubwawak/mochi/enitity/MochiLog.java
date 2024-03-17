/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package com.jakubwawak.mochi.enitity;

import org.bson.Document;

import java.util.Date;

/**
 * Object for storing application log
 */
public class MochiLog {

    public String log_code;
    public String log_desc;
    public Date log_timestamp;

    public String category;

    /**
     * Constructor
     */
    public MochiLog(){
        log_code = "";
        log_desc = "";
        log_timestamp = new Date();
        category = "NRL";
    }

    /**
     * Construction with params
     * @param code
     * @param desc
     */
    public MochiLog(String code, String desc){
        log_code = code;
        log_desc = desc;
        log_timestamp = new Date();
        if (code.contains("FAILED") || code.contains("ERROR")){
            category = "ERR";
        }
        else{
            category = "NRL";
        }
    }

    /**
     * Constructor with database support
     */
    public MochiLog(Document log_document){
        log_code = log_document.getString("log_code");
        log_desc = log_document.getString("log_desc");
        log_timestamp = log_document.get("log_timestamp",Date.class);
        category = log_document.getString(category);
    }

    /**
     * Function for preparing document containing application log data
     * @return Document
     */
    public Document prepareDocument(){
        Document document = new Document();
        document.append("log_code",log_code);
        document.append("log_desc",log_desc);
        document.append("log_timestamp",log_timestamp);
        document.append("category",category);
        return document;
    }
}