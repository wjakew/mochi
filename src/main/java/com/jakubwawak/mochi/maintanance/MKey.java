/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.mochi.maintanance;

import com.jakubwawak.mochi.MochiApplication;
import com.jakubwawak.mochi.enitity.Vault;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * Object for parsing and storing MKey data
 */
public class MKey {

    public Vault vault;

    /**
     * Constructor
     * @param vault
     */
    public MKey(Vault vault){
        this.vault = vault;
    }

    /**
     * Function for creating mKey file
     * @return String
     */
    public String createFile(){
        try{
            String fileName = vault.vault_name.replaceAll(" ","").replaceAll("-","");
            fileName = fileName + ".mkey";
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(vault.vault_hash);
            writer.close();
            File file = new File(fileName);
            if ( file.exists() ){
                vault.addLog("Created mkey file for vault!");
                return file.getAbsolutePath();
            }
            return null;
        }catch(Exception ex){
            MochiApplication.database.log("MKEY-FILE-CREATE-FAILED","Failed to create mkey file ("+ex.toString()+")");
            return null;
        }
    }
}
