package edu.agh.io.industryOptimizer.model;

import org.bson.Document;
import org.bson.types.ObjectId;

public class DefaultIdentifier implements Identifier {
    private final ObjectId id;

    public DefaultIdentifier(ObjectId id) {
        this.id = id;
    }

    @Override
    public ObjectId id() {
        return id;
    }

    @Override
    public Document toDocument() {
        return new Document(
                "batch_id",
                id.toString()
        );
    }

    public static Identifier fromDocument(Document document) {
        try {
            String id = document.getString("batch_id");

            if (id == null) {
                return null;
            }

            if (!ObjectId.isValid(id)) {
                return null;
            }

            return new DefaultIdentifier(
                    new ObjectId(id)
            );
        } catch (ClassCastException e) {
            return null;
        }
    }
}
