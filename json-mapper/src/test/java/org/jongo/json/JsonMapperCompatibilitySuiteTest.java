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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jongo.AnnotationsMisusedTest;
import org.jongo.BinaryTest;
import org.jongo.NonPojoTest;
import org.jongo.marshall.jackson.ParameterBindingWithJacksonTest;
import org.jongo.marshall.jackson.configuration.Mapping;
import org.jongo.marshall.jackson.configuration.PropertyModifier;
import org.jongo.marshall.jackson.configuration.VisibilityModifier;
import org.jongo.util.compatibility.CompatibilitySuite;
import org.jongo.util.compatibility.TestContext;
import org.junit.runner.RunWith;

import static org.junit.runners.Parameterized.Parameters;

@RunWith(CompatibilitySuite.class)
public class JsonMapperCompatibilitySuiteTest {

    @Parameters
    public static TestContext context() {

        Mapping config = new Mapping.Builder(new ObjectMapper())
                .registerModule(new JsonModule())
                .addModifier(new PropertyModifier())
                .addModifier(new VisibilityModifier())
                .build();

        TestContext context = new TestContext("JsonMapper", new JsonMapper(config));
        context.ignoreTestCase(BinaryTest.class);
        context.ignoreTestCase(NonPojoTest.class);
        context.ignoreTest(AnnotationsMisusedTest.class, "savingAPojoWithAnEmptyStringCustomId");

        //JsonEngine does not support primitive marshalled as Document
        context.ignoreTest(ParameterBindingWithJacksonTest.class, "canBindStringWithJsonValue");

        return context;
    }

}
