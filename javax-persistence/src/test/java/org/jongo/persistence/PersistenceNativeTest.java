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

package org.jongo.persistence;

import com.mongodb.WriteConcern;
import com.mongodb.client.MongoCollection;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.Mapper;
import org.jongo.model.Friend;
import org.jongo.use_native.NativeTestBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class PersistenceNativeTest extends NativeTestBase {

    public PersistenceNativeTest() {
        super(PersistenceCompatibilitySuiteTest.context().getMapper());
    }

    private MongoCollection<PersistenceFriend> collection;

    @Before
    public void setUp() throws Exception {
        collection = jongo.getCollection("friends", PersistenceFriend.class);
    }

    @After
    public void tearDown() throws Exception {
        collection.drop();
    }

    @Test
    public void canInsertWithPersistenceId() throws Exception {

        MongoCollection<PersistenceFriend> friends = collection.withDocumentClass(PersistenceFriend.class);

        String id = ObjectId.get().toString();
        PersistenceFriend john = new PersistenceFriend(id, "Robert");
        friends.insertOne(john);

        PersistenceFriend result = friends.find().first();
        assertThat(result.getId()).isEqualTo(id);
    }
}
