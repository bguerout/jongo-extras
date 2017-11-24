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

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import org.jongo.Mapper;
import org.jongo.ObjectIdUpdater;
import org.jongo.marshall.jackson.JacksonMapper;
import org.jongo.marshall.jackson.JacksonObjectIdUpdater;
import org.jongo.marshall.jackson.JongoAnnotationIntrospector;
import org.jongo.marshall.jackson.configuration.MapperModifier;
import org.jongo.util.compatibility.CompatibilitySuite;
import org.jongo.util.compatibility.TestContext;
import org.junit.runner.RunWith;

import static org.junit.runners.Parameterized.Parameters;

@RunWith(CompatibilitySuite.class)
public class PersistenceCompatibilitySuiteTest {

    @Parameters
    public static TestContext context() {

        ObjectMapper objectMapper = JacksonMapper.Builder.defaultObjectMapper();
        ObjectIdUpdater objectIdUpdater = new JacksonObjectIdUpdater(objectMapper, new PersistenceBeanPropertySelector());
        MapperModifier addPersistenceAnnotationModifier = new AddPersistenceAnnotationModifier();

        Mapper mapper = new JacksonMapper.Builder(objectMapper)
                .addModifier(addPersistenceAnnotationModifier)
                .withObjectIdUpdater(objectIdUpdater)
                .build();

        return new TestContext("PersistenceMapper", mapper);
    }

    private static class AddPersistenceAnnotationModifier implements MapperModifier {
        @Override
        public void modify(ObjectMapper mapper) {
            AnnotationIntrospector introspector = new JongoAnnotationIntrospector(new PersistenceAnnotatedIdSelector());
            AnnotationIntrospector jacksonIntrospector = mapper.getSerializationConfig().getAnnotationIntrospector();

            mapper.setAnnotationIntrospector(new AnnotationIntrospectorPair(introspector, jacksonIntrospector));
        }
    }
}
