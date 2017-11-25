package org.jongo.persistence;

import com.fasterxml.jackson.databind.introspect.Annotated;
import org.jongo.marshall.jackson.JongoAnnotationIdSelector;

public class PersistenceAnnotationIdSelector extends JongoAnnotationIdSelector {

    @Override
    public boolean isId(Annotated a) {
        return a.hasAnnotation(javax.persistence.Id.class) || super.isId(a);
    }

    @Override
    public boolean isObjectId(Annotated a) {
        return a.hasAnnotation(javax.persistence.Id.class) || super.isObjectId(a);
    }
}
