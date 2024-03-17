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

/**
 * Object for storing Vault data from object
 */
public class Vault {

    public ObjectId vault_id;
    public String vault_name;
    public String vault_creation_time;
    public String vault_recovery_key;        // randomly created string for recovering vault hash
    public String vault_hash;                // hash for connecting to vault in database
    public String vault_code;                // code for entering to vault using with vault_hash stored in .mkey file
    public List<String> vault_log;
    public List<String> vault_notes_list;

    /**
     * Constructor
     */
    public Vault() {
        this.vault_id = null;
        this.vault_name = "";
        this.vault_creation_time = LocalDateTime.now().toString();
        this.vault_hash = "";
        this.vault_code = "";
        RandomString rs = new RandomString(15);
        this.vault_recovery_key = rs.nextString();
        this.vault_log = new ArrayList<>();
        this.vault_notes_list = new ArrayList<>();

        addLog("Created vault");
    }

    /**
     * Constructor with database support
     * @param document
     */
    public Vault(Document document){
        this.vault_id = document.getObjectId("_id");
        this.vault_name = document.getString("vault_name");
        this.vault_creation_time = document.getString("vault_creation_time");
        this.vault_hash = document.getString("vault_hash");
        this.vault_code = document.getString("vault_code");
        this.vault_log = document.getList("vault_log",String.class);
        this.vault_notes_list = document.getList("vault_notes_list",String.class);
        this.vault_recovery_key = document.getString("vault_recovery_key");
    }

    /**
     * Function for preparing document object to put on database
     * @return
     */
    public Document prepareDocument() {
        Document document = new org.bson.Document();
        document.put("vault_name", this.vault_name);
        document.put("vault_creation_time", this.vault_creation_time);
        document.put("vault_hash", this.vault_hash);
        document.put("vault_code", this.vault_code);
        document.put("vault_log", this.vault_log);
        document.put("vault_notes_list", this.vault_notes_list);
        document.put("vault_recovery_key",this.vault_recovery_key);
        return document;
    }

    /**
     * Function for updating log data
     * @param log
     */
    public void addLog(String log){
        vault_log.add(LocalDateTime.now().toString()+" - "+log);
    }
}
