package org.jongo.persistence;

import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import org.jongo.marshall.jackson.JacksonObjectIdSelector;

public class PersistenceObjectIdSelector extends JacksonObjectIdSelector {

    @Override
    public boolean isId(BeanPropertyDefinition property) {
        return property.getPrimaryMember().getAnnotation(javax.persistence.Id.class) != null || super.isId(property);
    }

    @Override
    public boolean isObjectId(BeanPropertyDefinition property) {
        return property.getPrimaryMember().getAnnotation(javax.persistence.Id.class) != null || super.isObjectId(property);
    }
}
