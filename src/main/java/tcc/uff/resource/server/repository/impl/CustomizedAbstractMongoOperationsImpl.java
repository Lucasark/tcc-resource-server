package tcc.uff.resource.server.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import tcc.uff.resource.server.repository.CustomMongoOperations;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Slf4j
@RequiredArgsConstructor
public abstract class CustomizedAbstractMongoOperationsImpl<T> implements CustomMongoOperations<T> {

    private static final String ID = "id";

    private final MongoOperations operations;

    @Override
    public <A> void addInSet(String valueId, String key, A value) {
        Query query = new Query(where(getId()).is(valueId));
        operations.updateFirst(query, new Update().addToSet(key, value), getTypedClass());
    }

    @Override
    public String getId() {
        return ID;
    }

}
