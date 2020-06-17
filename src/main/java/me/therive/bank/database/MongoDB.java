package me.therive.bank.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import me.therive.bank.Main;
import me.therive.bank.database.callables.FindDocumentCallable;
import me.therive.bank.database.callables.GetDocumentsCallable;
import me.therive.bank.database.callables.InsertDocumentCallable;
import me.therive.bank.database.callables.UpdateDocumentCallable;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MongoDB {

    private MongoConnection mongoConnection;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> mongoCollection;

    public MongoDB(MongoConnection mongoConnection, MongoDatabase mongoDatabase, MongoCollection mongoCollection) {
        this.mongoConnection = mongoConnection;
        this.mongoDatabase = mongoDatabase;
        this.mongoCollection = mongoCollection;
    }

    /**
     * Find asynchronously documents from database
     *
     * @param key find by key
     * @param value find value with key
     * @return found document from the database
     */

    public CompletableFuture<Document> findDocumentAsync(String key, String value) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return Main.EXECUTOR_SERVICE.submit(new FindDocumentCallable(this.mongoCollection, key, value)).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    /**
     * Get all documents from collection asynchronously
     *
     * @return all documents from collection
     */

    public CompletableFuture<List<Document>> getDocumentsFromCollectionAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return Main.EXECUTOR_SERVICE.submit(new GetDocumentsCallable(this.mongoCollection)).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    /**
     * Insert single document to database asynchronously
     *
     * @param document document to insert
     * @return information for completed insert
     */

    public CompletableFuture insertDocumentAsync(Document document) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return Main.EXECUTOR_SERVICE.submit(new InsertDocumentCallable(this.mongoCollection, document)).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    /**
     * Update single documents from the database asynchronously
     *
     * @param key find by key
     * @param value find value with key
     * @param toUpdate document to update
     * @return information for completed update
     */

    public Future updateDocumentAsync(String key, String value, Document toUpdate) {
        return Main.EXECUTOR_SERVICE.submit(new UpdateDocumentCallable(this.mongoCollection, key, value, toUpdate));
    }

    /**
     * Find synchronously documents from database
     *
     * @param key find by key
     * @param value
     * @return found document from the database
     */

    public Document findDocumentSync(String key, String value) {
        return this.mongoCollection.find(Filters.eq(key, value)).first();
    }

    /**
     * Get all documents from collection synchronously
     *
     * @return all documents from collection
     */

    public List<Document> getDocumentsFromCollectionSync() {
        List<Document> documents = new ArrayList<>();

        for (Document document : this.mongoCollection.find()) {
            documents.add(document);
        }

        return documents;
    }

    /**
     * Update single documents from the database synchronously
     *
     * @param key find by key
     * @param value find value with key
     * @param toUpdate document to update
     */

    public void updateDocumentSync(String key, String value, Document toUpdate) {
        this.mongoCollection.replaceOne(Filters.eq(key, value), toUpdate);
    }

    public MongoConnection getMongoConnection() {
        return this.mongoConnection;
    }

    public MongoDatabase getDatabase() {
        return this.mongoDatabase;
    }

    public MongoCollection<Document> getCollection() {
        return this.mongoCollection;
    }
}
