/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.mochi.backend.database;

import com.jakubwawak.mochi.MochiApplication;
import com.jakubwawak.mochi.enitity.Note;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

/**
 * Object for maintaining note object on database
 */
public class Database_Note {

    Database_Connector database;

    /**
     * Constructor
     * @param database
     */
    public Database_Note(Database_Connector database){
        this.database = database;
    }


    /**
     * Function for getting note based on ObjectId
     * @param note_id
     * @return Note
     */
    public Note getNote(ObjectId note_id){
        try{
            MongoCollection<Document> mochiCollection = database.get_data_collection("mochi_notes");
            Document note_document = mochiCollection.find(new Document("_id",note_id)).first();
            if ( note_document != null ){
                return new Note(note_document);
            }
            return null;
        }catch(Exception ex){
            database.log("GET-NOTE-FAILED","Failed to load note from database ("+ex.toString()+")");
            return null;
        }
    }

    /**
     * Function for getting note from database based on the content
     * @param note_raw
     * @return Note
     */
    public Note getNote(String note_raw){
        try{
            MongoCollection<Document> mochiCollection = database.get_data_collection("mochi_notes");
            Document note_document = mochiCollection.find(new Document("note_raw",note_raw)).first();
            if ( note_document != null ){
                return new Note(note_document);
            }
            return null;
        }catch(Exception ex){
            database.log("GET-NOTE-FAILED","Failed to load note from database ("+ex.toString()+")");
            return null;
        }
    }


    /**
     * Function for getting note from database based on the content
     * @param note_name
     * @return Note
     */
    public Note getNotebyName(String note_name){
        try{
            MongoCollection<Document> mochiCollection = database.get_data_collection("mochi_notes");
            Document note_document = mochiCollection.find(new Document("note_name",note_name)).first();
            if ( note_document != null ){
                return new Note(note_document);
            }
            return null;
        }catch(Exception ex){
            database.log("GET-NOTE-FAILED","Failed to load note from database ("+ex.toString()+")");
            return null;
        }
    }

    /**
     * Function for loading notes to NoteView for sharing
     * @param note_url
     * @return Note
     */
    public Note loadNote(String note_url){
        try{
            MongoCollection<Document> mochiCollection = database.get_data_collection("mochi_notes");
            Document note_document = mochiCollection.find(new Document("note_url",note_url)).first();
            if ( note_document != null ){
                return new Note(note_document);
            }
            return null;
        }catch(Exception ex){
            database.log("GET-NOTE-FAILED","Failed to load note from database ("+ex.toString()+")");
            return null;
        }
    }
    /**
     * Function for inserting note
     * @param note
     * @return Note
     */
    public Note insertNote(Note note){
        try{
            MongoCollection<Document> mochiCollection = database.get_data_collection("mochi_notes");
            InsertOneResult result = mochiCollection.insertOne(note.prepareDocument());
            if ( result.wasAcknowledged() ){
                database.log("INSERT-NOTE","New note ("+note.note_name+") was created!");
                MochiApplication.notificationService("Created new note ("+note.note_name+")",1);
                return getNote(note.note_raw);
            }
            return null;
        }catch(Exception ex){
            database.log("INSERT-NOTE-FAILED","Failed to insert note ("+ex.toString()+")");
            return null;
        }
    }

    /**
     * Function for updating note on database
     * @param note
     * @return Note
     */
    public Note updateNote(Note note){
        try{
            MongoCollection<Document> mochiCollection = database.get_data_collection("mochi_notes");
            Document note_document = mochiCollection.find(new Document("_id",note.note_id)).first();
            if ( note_document != null ){
                note.addLog("Updated note!");
                Bson updates = Updates.combine(
                        Updates.set("note_name",note.note_name),
                        Updates.set("note_tags",note.note_tags),
                        Updates.set("note_code",note.note_code),
                        Updates.set("note_url",note.note_url),
                        Updates.set("note_log",note.note_log),
                        Updates.set("note_raw",note.note_raw)
                );
                UpdateResult result = mochiCollection.updateOne(note_document,updates);
                if ( result.getModifiedCount() >= 1 ){
                    database.log("NOTE-UPDATE","Updated note object ("+note.note_name+")");
                    MochiApplication.notificationService("Updated note ("+note.note_id.toString()+")",1);
                    return getNote(note.note_id);
                }
                return null;
            }
            else{
                database.log("NOTE-UPDATE","Cannot find note with given ID");
                return null;
            }
        }catch(Exception ex){
            MochiApplication.database.log("NOTE-UPDATE-FAILED","Failed to update note ("+ex.toString()+")");
            return null;
        }
    }
}
