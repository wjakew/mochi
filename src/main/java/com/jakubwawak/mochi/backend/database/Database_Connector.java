/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.mochi.backend.database;

import com.jakubwawak.mochi.MochiApplication;
import com.jakubwawak.mochi.enitity.Mochi;
import com.jakubwawak.mochi.enitity.MochiLog;
import com.jakubwawak.mochi.maintanance.ConsoleColors;
import com.mongodb.*;
import com.mongodb.client.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

/**
 * Object for maintaining database connection
 */
public class Database_Connector {

    public String database_url;

    public boolean connected;

    MongoClient mongoClient;
    MongoDatabase mongoDatabase;

    ArrayList<String> error_collection;

    /**
     * Constructor
     */
    public Database_Connector(){
        this.database_url = "";
        connected = false;
        error_collection = new ArrayList<>();
    }

    /**
     * Function for setting database url
     * @param database_url
     */
    public void setDatabase_url(String database_url){
        this.database_url = database_url;
    }

    /**
     * Function for connecting to database
     * @return boolean
     */
    public void connect(){
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(database_url))
                .serverApi(serverApi)
                .build();
        try{
            mongoClient = MongoClients.create(settings);
            connected = true;
            mongoDatabase = mongoClient.getDatabase("db_mochi");
            log("DB-CONNECTION","Connected succesffully with database - running application");
        }catch(MongoException ex){
            // catch error
            log("DB-CONNECTION-ERROR", "Failed to connect to database ("+ex.toString()+")");
            connected = false;
        }
    }

    /**
     * Function for loading collections
     * @param collection_name
     * @return MongoCollection<Document>
     */
    MongoCollection<Document> get_data_collection(String collection_name){
        return mongoDatabase.getCollection(collection_name);
    }

    /**
     * Function for inserting log data to database
     * @param applicationLog
     * @return Integer
     */
    public int insertLog(MochiLog applicationLog){
        try{
            MongoCollection<Document> log_collection = get_data_collection("aim_applog");
            log_collection.insertOne(applicationLog.prepareDocument());
            return 1;
        }catch(Exception ex){
            log("DB-INSERTLOG-FAILED","Failed to insert log ("+ex.toString()+")");
            return -1;
        }
    }

    /**
     * Function for loading configuration from database
     * @return Mochi
     */
    public Mochi getConfiguration(){
        try{
            MongoCollection<Document> mochiCollection = get_data_collection("mochi_configuration");
            FindIterable<Document> mochiDocuments = mochiCollection.find();
            for ( Document mochiDocument : mochiDocuments ){
                return new Mochi(mochiDocument);
            }
            Mochi configuration = new Mochi();
            mochiCollection.insertOne(configuration.prepareDocument());
            return configuration;
        }catch(Exception ex){
            log("DB-INSERTLOG-FAILED","Failed to insert log ("+ex.toString()+")");
            return null;
        }
    }

    /**
     * Function for story log data
     * @param log_category
     * @param log_text
     */
    public void log(String log_category, String log_text){
        error_collection.add(log_category+"("+ LocalDateTime.now(ZoneId.of("Europe/Warsaw")).toString()+") - "+log_text);
        if ( log_category.contains("FAILED") || log_category.contains("ERROR")){
            System.out.println(ConsoleColors.RED_BRIGHT+log_category+"["+ LocalDateTime.now(ZoneId.of("Europe/Warsaw")).toString()+") - "+log_text+"]"+ConsoleColors.RESET);
            try{
                Notification noti = Notification.show(log_text);
                noti.addThemeVariants(NotificationVariant.LUMO_ERROR);

            }catch(Exception ex){}
        }
        else{
            System.out.println(ConsoleColors.GREEN_BRIGHT+log_category+"["+ LocalDateTime.now(ZoneId.of("Europe/Warsaw")).toString()+") - "+log_text+"]"+ConsoleColors.RESET);
        }
        // inserting log to database
        if (MochiApplication.log_database_dump_flag == 1){
            if ( !log_category.equals("DB-INSERTLOG-FAILED")){
                insertLog(new MochiLog(log_category,log_text));
            }
        }
    }
}
