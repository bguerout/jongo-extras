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
        System.setProperty("org.ops4j.pax.url.mvn.repositories",
                "https://oss.sonatype.org/content/repositories/snapshots@snapshots@id=sonatype-nexus-snapshots,http://repo1.maven.org/maven2@id=central");

        return new Option[]{
                mavenBundle("org.jongo", "jongo", System.getProperty("jongo.version")),
                mavenBundle("com.fasterxml.jackson.core", "jackson-core", "2.3.0"),
                mavenBundle("com.fasterxml.jackson.core", "jackson-databind", "2.3.0"),
                mavenBundle("com.fasterxml.jackson.core", "jackson-annotations", "2.3.0"),
                mavenBundle("de.undercouch", "bson4jackson", "2.3.0"),
                mavenBundle("org.mongodb", "mongo-java-driver", "2.11.0"),
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
