package me.therive.bank.database.callables;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.concurrent.Callable;

public class FindDocumentCallable implements Callable<Document> {

    private MongoCollection<Document> mongoCollection;

    private String key;

    private String value;

    public FindDocumentCallable(MongoCollection<Document> mongoCollection, String key, String value) {
        this.mongoCollection = mongoCollection;

        this.key = key;
        this.value = value;
    }

    @Override
    public Document call() {
        return this.mongoCollection.find(Filters.eq(this.key, this.value)).first();
    }
}
