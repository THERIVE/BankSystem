package me.therive.bank.database.callables;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.concurrent.Callable;

public class InsertDocumentCallable implements Callable<Document> {

    private MongoCollection<Document> mongoCollection;

    private Document document;

    public InsertDocumentCallable(MongoCollection<Document> mongoCollection, Document document) {
        this.mongoCollection = mongoCollection;

        this.document = document;
    }

    @Override
    public Document call() {
        this.mongoCollection.insertOne(this.document);
        return this.document;
    }
}
