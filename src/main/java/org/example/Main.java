package org.example;

import com.mongodb.client.*;
import org.bson.Document;

import static com.mongodb.client.model.Indexes.descending;

/**
 * This class connects to a MongoDB database and performs operations on the "Video" collection.
 * Operations include:
 * <ul>
 *     <li>Sorting videos by the number of likes</li>
 *     <li>Performing a full-text search based on video titles</li>
 * </ul>
 */
public class Main {

    /**
     * The main method connects to a MongoDB collection and performs data queries such as sorting and searching.
     *
     */
    public static void main(String[] args) {

        // -- MongoDB Connection -- Start

        // Connection string to MongoDB Atlas cluster
        String uri = "mongodb+srv://mariavathkruja06:1Schueler@mongo-db.pagqcdp.mongodb.net/Videoplattform?retryWrites=true&w=majority&appName=Mongo-DB";

        // Create a new MongoClient using try-with-resources (automatically closes connection)
        try (MongoClient mongoClient = MongoClients.create(uri)) {

            // Connect to the "Videoplattform" database
            MongoDatabase database = mongoClient.getDatabase("Videoplattform");

            // Access the "Video" collection within the database
            MongoCollection<Document> collection = database.getCollection("Video");

            // -- MongoDB Connection -- End

            // ------------------------- SORTING VIDEOS BY LIKES -----------------------------

            System.out.println("Videos sortiert nach Likes:");

            // Find all videos and sort them in descending order based on the "likes" field
            FindIterable<Document> sortedVideos = collection.find().sort(descending("likes"));

            // Print each video in JSON format
            for (Document video : sortedVideos) {
                System.out.println(video.toJson());
            }

            // ------------------------- FULL-TEXT SEARCH BY TITLE ----------------------------

            // Create a full-text search index on the "title" field
            collection.createIndex(new Document("title", "text"));

            // Define a keyword to search for in the video titles
            String searchKeyword = "Tirana";

            // Find videos where the title matches the search keyword using the text index
            FindIterable<Document> searchResults = collection.find(
                    new Document("$text", new Document("$search", searchKeyword))
            );

            System.out.println("\nVideos with the word '" + searchKeyword + "' in the title:");

            // Print each matching video in JSON format
            for (Document video : searchResults) {
                System.out.println(video.toJson());
            }

        } catch (Exception e) {
            // Print any errors that occur during execution
            System.err.println("Something went wrong: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
