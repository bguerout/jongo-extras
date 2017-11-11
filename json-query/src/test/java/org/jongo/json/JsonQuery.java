/*
 * Copyright (C) 2011 Benoit GUEROUT <bguerout at gmail dot com> and Yves AMSELLEM <amsellem dot yves at gmail dot com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jongo.json;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.bson.conversions.Bson;
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
    public Bson toBson() {
        return null;
    }

    @Override
    public String toString() {
        return dbo.toString();
    }
}
