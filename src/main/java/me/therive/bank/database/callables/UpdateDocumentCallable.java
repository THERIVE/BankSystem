package me.therive.bank.database.callables;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.concurrent.Callable;

public class UpdateDocumentCallable implements Callable<Document> {

    private MongoCollection<Document> mongoCollection;

    private String key;

    private String value;

    private Document document;


    public UpdateDocumentCallable(MongoCollection<Document> mongoCollection, String key, String value, Document document) {
        this.mongoCollection = mongoCollection;

        this.key = key;
        this.value = value;
        this.document = document;
    }

    @Override
    public Document call() {
        Document find = this.mongoCollection.find(Filters.eq(this.key, this.value)).first();

        if (find == null) {
            this.mongoCollection.insertOne(this.document);
            return this.document;
        }

        this.mongoCollection.replaceOne(Filters.eq(this.key, this.document.getString(this.key)), this.document);
        return this.document;
    }
}
