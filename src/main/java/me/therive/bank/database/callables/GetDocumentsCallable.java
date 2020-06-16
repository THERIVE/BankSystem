package me.therive.bank.database.callables;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class GetDocumentsCallable implements Callable<List<Document>> {

    private MongoCollection<Document> mongoCollection;

    public GetDocumentsCallable(MongoCollection<Document> mongoCollection) {
        this.mongoCollection = mongoCollection;
    }

    @Override
    public List<Document> call() {
        List<Document> documents = new ArrayList<>();
        for (Document document : this.mongoCollection.find())
            documents.add(document);

        return documents;
    }
}
