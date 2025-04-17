
package org.example;

import com.mongodb.client.*;
import org.bson.Document;
import static com.mongodb.client.model.Indexes.descending;




public class Main {
    public static void main(String[] args) {


        // -- MongoDB Connection -- Start

        // MongoDB connection string
        String uri = "mongodb+srv://mariavathkruja06:1Schueler@mongo-db.pagqcdp.mongodb.net/Videoplattform?retryWrites=true&w=majority&appName=Mongo-DB";

        // Create MongoClient
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            // Connect to the database
            MongoDatabase database = mongoClient.getDatabase("Videoplattform");

            // Connect to the collection
            MongoCollection<Document> collection = database.getCollection("Video");

            // -- MongoDB Connection -- End


            // ----------------------------- VIDEOS SORTED BY LIKES -----------------------------

            System.out.println("Videos sortiert nach Likes: ");
            // Sort videos by "likes" in descending order
            FindIterable<Document> sortedVideos = collection.find().sort(descending("likes"));

            // Iterate and print sorted videos
            for (Document video : sortedVideos) {
                System.out.println(video.toJson());
            }

            // ----------------------------- SEARCH VIDEOS BASED ON TITLE -----------------------------

            // Create a text index on the title field for full-text search
            collection.createIndex(new Document("title", "text"));

            // Here we have created a keyword which will be used to find our wanted word in our videos.
            String searchKeyword = "Tirana";
            FindIterable<Document> searchResults = collection.find(new Document("$text", new Document("$search", searchKeyword)));

            System.out.println("\nVideos with the word of Tirana '" + searchKeyword + "' in the title: ");

            // Iterating and printing search results
            for (Document video : searchResults) {
                System.out.println(video.toJson());
            }

        } catch (Exception e) {
            System.err.println("Something went wrong: " + e.getMessage());
            e.printStackTrace();
        }

    }
}

