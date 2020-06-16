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
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class MongoDB {

    private MongoConnection mongoConnection;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> mongoCollection;

    public MongoDB(MongoConnection mongoConnection, MongoDatabase mongoDatabase, MongoCollection mongoCollection) {
        this.mongoConnection = mongoConnection;
        this.mongoDatabase = mongoDatabase;
        this.mongoCollection = mongoCollection;
    }

    public Future<Document> findDocumentAsync(String key, String value) {
        return Main.executorService.submit(new FindDocumentCallable(this.mongoCollection, key, value));
    }

    public Future<List<Document>> getDocumentsFromCollectionAsync() {
        return Main.executorService.submit(new GetDocumentsCallable(this.mongoCollection));
    }

    public Future insertDocumentAsync(Document document) {
        return Main.executorService.submit(new InsertDocumentCallable(this.mongoCollection, document));
    }

    public Future updateDocumentAsync(String key, String value, Document toUpdate) {
        return Main.executorService.submit(new UpdateDocumentCallable(this.mongoCollection, key, value, toUpdate));
    }

    public Document findDocumentSync(String key, String value) {
        return this.mongoCollection.find(Filters.eq(key, value)).first();
    }

    public List<Document> getDocumentsFromCollectionSync() {
        List<Document> documents = new ArrayList<>();

        for (Document document : this.mongoCollection.find()) {
            documents.add(document);
        }

        return documents;
    }

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
