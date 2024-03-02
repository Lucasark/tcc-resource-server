package tcc.uff.resource.server.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import tcc.uff.resource.server.repository.MongoHelperRepository;

import java.io.Serializable;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Slf4j
@SuppressWarnings("java:S119")
public class MongoHelperRepositoryImpl<T, ID extends Serializable> extends SimpleMongoRepository<T, ID> implements MongoHelperRepository<T, ID> {

    private final MongoOperations mongoOperations;
    private final MongoEntityInformation<T, ID> entityInformation;

    public MongoHelperRepositoryImpl(MongoEntityInformation<T, ID> metadata, MongoOperations mongoOperations) {
        super(metadata, mongoOperations);
        this.entityInformation = metadata;
        this.mongoOperations = mongoOperations;
    }

    @Override
    public <S> void addInSet(ID id, String key, S value) {
        Query query = new Query(where(this.entityInformation.getIdAttribute()).is(id));
        mongoOperations.updateFirst(query, new Update().addToSet(key, value), entityInformation.getJavaType());
    }

    @Override
    public <S> void pull(ID id, String key, S value) {
        Query query = new Query(where(this.entityInformation.getIdAttribute()).is(id));
        mongoOperations.updateFirst(query, new Update().pull(key, value), entityInformation.getJavaType());
    }

}
