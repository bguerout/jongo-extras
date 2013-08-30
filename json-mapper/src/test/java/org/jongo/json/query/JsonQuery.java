package org.jongo.json.query;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.jongo.query.Query;

class JsonQuery implements Query {

    private final DBObject dbo;

    public JsonQuery(String query) {
        this.dbo = marshallQuery(query);
    }

    private DBObject marshallQuery(String query) {
        try {
            return (DBObject) JSON.parse(query);
        } catch (Exception e) {
            throw new IllegalArgumentException(query + " cannot be parsed", e);
        }
    }

    public DBObject toDBObject() {
        return dbo;
    }

    @Override
    public String toString() {
        return dbo.toString();
    }
}
