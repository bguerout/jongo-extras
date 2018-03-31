package org.jongo.persistence;

import com.fasterxml.jackson.databind.introspect.Annotated;
import org.jongo.marshall.jackson.JongoAnnotationIntrospector;

public class PersistenceAnnotationIdSelector extends JongoAnnotationIntrospector.AnnotatedIdSelector {

    @Override
    public boolean isId(Annotated a) {
        return a.hasAnnotation(javax.persistence.Id.class) || super.isId(a);
    }

    @Override
    public boolean isObjectId(Annotated a) {
        return a.hasAnnotation(javax.persistence.Id.class) || super.isObjectId(a);
    }
}
