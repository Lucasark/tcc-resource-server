package tcc.uff.resource.server.service.mongooperations.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import tcc.uff.resource.server.service.mongooperations.MongoOperationsService;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Slf4j
@Service
@RequiredArgsConstructor
public class MongoOperationsServiceImpl implements MongoOperationsService {

    private final MongoOperations operations;

    @Override
    public <A> void addInSet(String keyId, String valueId, String key, A value, Class<?> clazz) {
        Query query = new Query(where(keyId).is(valueId));
        operations.updateFirst(query, new Update().addToSet(key, value), clazz);
    }
}
