package org.jongo.json.query;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.jongo.bson.Bson;
import org.jongo.marshall.Marshaller;
import org.jongo.marshall.MarshallingException;
import org.jongo.query.Query;
import org.jongo.query.QueryFactory;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonQueryFactory implements QueryFactory {

    private static final String DEFAULT_TOKEN = "#";
    private final String token;
    private final Marshaller marshaller;
    private final Pattern pattern;

    public JsonQueryFactory(Marshaller marshaller) {
        this(marshaller, DEFAULT_TOKEN);
    }

    public JsonQueryFactory(Marshaller marshaller, String token) {
        this.marshaller = marshaller;
        this.token = token;
        this.pattern = Pattern.compile(token);
    }

    public final Query createQuery(String query, Object... parameters) {
        if (parameters == null) {
            parameters = new Object[]{null};
        }
        if (parameters.length == 0) {
            return new JsonQuery(query);
        }
        return createQueryWithParameters(query, parameters);
    }

    private JsonQuery createQueryWithParameters(String template, Object[] parameters) {
        String query = template;
        assertThatParamsCanBeBound(query, parameters);
        int paramIndex = 0;
        int tokenIndex = 0;
        while (true) {
            tokenIndex = query.indexOf(token, tokenIndex);
            if (tokenIndex < 0) {
                break;
            }

            Object parameter = parameters[paramIndex++];

            String replacement;
            try {
                replacement = marshallParameter(parameter, true).toString();
            } catch (RuntimeException e) {
                String message = String.format("Unable to bind parameter: %s into query: %s", parameter, query);
                throw new IllegalArgumentException(message, e);
            }

            query = query.substring(0, tokenIndex) + replacement + query.substring(tokenIndex + token.length());
            tokenIndex += replacement.length();
        }

        return new JsonQuery(query);
    }

    private Object marshallParameter(Object parameter, boolean serializeBsonPrimitives) {
        try {
            if (parameter == null || Bson.isPrimitive(parameter)) {
                return serializeBsonPrimitives ? JSON.serialize(parameter) : parameter;
            }
            if (parameter instanceof Enum) {
                String name = ((Enum) parameter).name();
                return serializeBsonPrimitives ? JSON.serialize(name) : name;
            }
            if (parameter instanceof List) {
                return marshallArray(((List) parameter).toArray());
            }
            if (parameter instanceof Object[]) {
                return marshallArray((Object[]) parameter);
            }
            return marshallDocument(parameter);
        } catch (Exception e) {
            String message = String.format("Unable to marshall parameter: %s", parameter);
            throw new MarshallingException(message, e);
        }
    }

    private DBObject marshallArray(Object[] parameters) {
        BasicDBList list = new BasicDBList();
        for (int i = 0; i < parameters.length; i++) {
            list.put(i, marshallParameter(parameters[i], false));
        }
        return list;
    }

    private DBObject marshallDocument(Object parameter) {
        return marshaller.marshall(parameter).toDBObject();
    }

    private void assertThatParamsCanBeBound(String template, Object[] parameters) {
        int nbTokens = countTokens(template);
        if (nbTokens != parameters.length) {
            String message = String.format("Unable to bind parameters into query: %s. Tokens and parameters numbers mismatch " +
                    "[tokens: %s / parameters:%s]", template, nbTokens, parameters.length);
            throw new IllegalArgumentException(message);
        }
    }

    private int countTokens(String template) {
        int count = 0;
        Matcher matcher = pattern.matcher(template);
        while (matcher.find()) {
            count++;
        }
        return count;
    }

}
