package org.json.osgi;

import org.jongo.Jongo;
import org.jongo.query.Query;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;


@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class OsgiTest {

    @Inject
    private BundleContext bundleContext;

    @Configuration
    public Option[] config() {
        System.setProperty("org.ops4j.pax.url.mvn.defaultLocalRepoAsRemote", "true");
        System.setProperty("org.ops4j.pax.url.mvn.repositories",
                "https://oss.sonatype.org/content/repositories/snapshots@snapshots@id=sonatype-snapshots," +
                        "https://repo1.maven.org/maven2@id=central");

        String jongoVersion = System.getProperty("jongo.version");
        String jacksonVersion = System.getProperty("jackson.version");
        String bson4JacksonVersion = System.getProperty("bson4jackson.version");
        String mongodbJavaDriverVersion = System.getProperty("mongo-java-driver.version");

        return new Option[]{
                mavenBundle("org.jongo", "jongo", jongoVersion),
                mavenBundle("org.mongodb", "mongo-java-driver", mongodbJavaDriverVersion),
                mavenBundle("com.fasterxml.jackson.core", "jackson-core", jacksonVersion),
                mavenBundle("com.fasterxml.jackson.core", "jackson-databind", jacksonVersion),
                mavenBundle("com.fasterxml.jackson.core", "jackson-annotations", jacksonVersion),
                mavenBundle("de.undercouch", "bson4jackson", bson4JacksonVersion),
                junitBundles()
        };
    }

    @Test
    public void canCreateAnJongoInstanceFromOsgiContext() {

        Jongo jongo = new Jongo(null);

        Query query = jongo.createQuery("{}");

        assertNotNull(query);
    }
}
