/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.mochi.backend.datamanager;

import com.jakubwawak.mochi.MochiApplication;
import com.jakubwawak.mochi.backend.database.Database_Connector;
import com.jakubwawak.mochi.backend.database.Database_Note;
import com.jakubwawak.mochi.backend.database.Database_Vault;
import com.jakubwawak.mochi.enitity.Vault;
import com.jakubwawak.mochi.maintanance.MKey;
import org.bson.types.ObjectId;
import com.jakubwawak.mochi.enitity.Note;

/**
 * Object for maintaining all Vault related functions
 */
public class VaultManager {


    public Database_Vault dv;

    public VaultManager(Database_Connector database){
        dv = new Database_Vault(database);
    }


    /**
     * Function for creating vault object on database
     * @param vaultString
     * @param vaultName
     * @param code
     * @return Vault
     */
    public Vault createVault(String vaultString, String vaultName,String code){
        Vault vault = new Vault();
        vault.vault_name = vaultName;
        String preparedCode = vaultString+vault.vault_creation_time;
        String hash = Long.toString(preparedCode.hashCode()+1996+2019);
        vault.vault_hash = hash;
        vault.vault_code = code;
        return dv.insertVault(vault);
    }

    /**
     * Function for creating mKey file
     * @param vault_id
     * @return String (path to file)
     */
    public String createVaultmKey(ObjectId vault_id){
        Vault vault = dv.getVault(vault_id);
        MKey mkey = new MKey(vault);
        return mkey.createFile();
    }

    /**
     * Function for adding note to vault
     * @param note
     * @return
     */
    public void addNoteToVault(Note note){
        MochiApplication.currentVault.addNote(note);
        MochiApplication.currentVault.addLog("Added new note to vault: "+note.note_name);
        MochiApplication.vaultUpdateService();
    }

    /**
     * Function for loading note object to vault object
     */
    public void loadNotesObjectsToVault(){
        MochiApplication.currentVault.vault_notes_objects.clear();
        Database_Note dn = new Database_Note(MochiApplication.database);
        for(ObjectId note_id : MochiApplication.currentVault.vault_notes_list){
            Note note = dn.getNote(note_id);
            MochiApplication.currentVault.vault_notes_objects.add(note);
        }
        MochiApplication.database.log("VAULT-NOTES-UPDATE","Updated notes object list!");
        MochiApplication.notificationService("Notes object in vault updated!",2);
    }
}
