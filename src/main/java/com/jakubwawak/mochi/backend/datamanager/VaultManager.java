/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.mochi.backend.datamanager;

import com.jakubwawak.mochi.backend.database.Database_Connector;
import com.jakubwawak.mochi.backend.database.Database_Vault;
import com.jakubwawak.mochi.enitity.Vault;
import com.jakubwawak.mochi.maintanance.MKey;
import org.bson.types.ObjectId;

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
}
