/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.mochi.backend.database;

import com.jakubwawak.mochi.MochiApplication;
import com.jakubwawak.mochi.enitity.Note;
import com.jakubwawak.mochi.enitity.Vault;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;

/**
 * Object for maintaining vault data on database
 */
public class Database_Vault {

    Database_Connector database;

    /**
     * Constructor
     * @param database
     */
    public Database_Vault(Database_Connector database){
        this.database = database;
    }

    /**
     * Function for creating vault object on database
     * @param vault
     * @return Vault
     */
    public Vault insertVault(Vault vault){
        try{
            MongoCollection<Document> mochiCollection = database.get_data_collection("mochi_vault");
            InsertOneResult result = mochiCollection.insertOne(vault.prepareDocument());
            if ( result.wasAcknowledged() ){
                database.log("VAULT-INSERT","Created new vault ("+vault.vault_name+")");
                return getVault(vault.vault_hash,vault.vault_code);
            }
            database.log("VAULT-INSERT-FAILED","Failed to insert new vault, result was not acknowledged!");
            return null;
        }catch(Exception ex){
            database.log("VAULT-INSERT-FAILED","Failed to insert vault ("+ex.toString()+")");
            return null;
        }
    }

    /**
     * Function for getting vault object from database
     * @param vaultHash
     * @param code
     * @return Vault
     */
    public Vault getVault(String vaultHash,String code){
        Vault vault = null;
        try{
            MongoCollection<Document> mochiCollection = database.get_data_collection("mochi_vault");
            Document vault_document = mochiCollection.find(new Document("vault_hash",vaultHash)).first();
            if ( vault_document != null ){
                vault = new Vault(vault_document);
                if ( vault.vault_code.equals(code)){
                    MochiApplication.notificationService("Vault loaded ("+vault.vault_name+")",1);
                    return vault;
                }
                else{
                    MochiApplication.notificationService("Wrong vault code!",2);
                    return null;
                }
            }
            MochiApplication.notificationService("Cannot find vault with given hash!",3);
            return null;
        }
        catch(Exception ex){
            MochiApplication.database.log("GET-VAULT-FAILED","Failed to get vault ("+ex.toString()+")");
            return null;
        }
    }

    /**
     * Function for getting vault object from database
     * @param vault_id
     * @return Vault
     */
    public Vault getVault(ObjectId vault_id){
        Vault vault = null;
        try{
            MongoCollection<Document> mochiCollection = database.get_data_collection("mochi_vault");
            Document vault_document = mochiCollection.find(new Document("_id",vault_id)).first();
            if ( vault_document != null ){
                vault = new Vault(vault_document);
                return vault;
            }
            MochiApplication.notificationService("Cannot find vault with given ID!",3);
            return null;
        }
        catch(Exception ex){
            MochiApplication.database.log("GET-VAULT-FAILED","Failed to get vault ("+ex.toString()+")");
            return null;
        }
    }

    /**
     * Functionf for getting vault from database
     * @param vault_hash
     * @return Vault
     */
    public Vault getVault(String vault_hash){
        Vault vault = null;
        try{
            MongoCollection<Document> mochiCollection = database.get_data_collection("mochi_vault");
            Document vault_document = mochiCollection.find(new Document("vault_hash",vault_hash)).first();
            if ( vault_document != null ){
                vault = new Vault(vault_document);
                return vault;
            }
            MochiApplication.notificationService("Cannot find vault with given hash!",3);
            return null;
        }
        catch(Exception ex){
            MochiApplication.database.log("GET-VAULT-FAILED","Failed to get vault ("+ex.toString()+")");
            return null;
        }
    }

    /**
     * Function for updating vault object on database
     * @param vaultToUpdate
     * @return Vault
     */
    public Vault updateVault(Vault vaultToUpdate){
        try{
            MongoCollection<Document> mochiCollection = database.get_data_collection("mochi_vault");
            Document vault_document = mochiCollection.find(new Document("_id",vaultToUpdate.vault_id)).first();
            if ( vault_document != null ){
                vaultToUpdate.addLog("Database update!");
                Bson updates = Updates.combine(
                        Updates.set("vault_name",vaultToUpdate.vault_name),
                        Updates.set("vault_hash",vaultToUpdate.vault_hash),
                        Updates.set("vault_code",vaultToUpdate.vault_code),
                        Updates.set("vault_recovery_key",vaultToUpdate.vault_recovery_key),
                        Updates.set("vault_log",vaultToUpdate.vault_log),
                        Updates.set("vault_notes_list",vaultToUpdate.vault_notes_list)
                        );
                UpdateResult result = mochiCollection.updateOne(vault_document,updates);
                if ( result.getModifiedCount() >= 1 ){
                    MochiApplication.database.log("VAULT-UPDATE","Updated vault ("+vaultToUpdate.vault_id.toString()+")");
                    MochiApplication.notificationService("Updated vault "+vaultToUpdate.vault_id.toString(),2);
                    return getVault(vaultToUpdate.vault_id);
                }
                else{
                    MochiApplication.database.log("VAULT-UPDATE","Nothing to update on vault ("+vaultToUpdate.vault_id.toString()+")");
                    return vaultToUpdate;
                }
            }
            else{
                MochiApplication.database.log("VAULT-UPDATE","Cannot find vault on database with ID ("+vaultToUpdate.vault_id.toString()+")");
                return null;
            }
        }catch(Exception ex){
            MochiApplication.database.log("VAULT-UPDATE-FAILED","Failed to update vault ("+ex.toString()+")");
            return null;
        }
    }
}
