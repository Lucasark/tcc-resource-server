package tcc.uff.resource.server.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;
import tcc.uff.resource.server.model.document.UserDocument;
import tcc.uff.resource.server.repository.CustomizedUserRepository;

@Slf4j
@Component
public class CustomizedUserRepositoryImpl extends CustomizedAbstractMongoOperationsImpl<UserDocument> implements CustomizedUserRepository {

    CustomizedUserRepositoryImpl(MongoOperations operations) {
        super(operations);
    }

    @Override
    public Class<UserDocument> getTypedClass() {
        return UserDocument.class;
    }

    @Override
    public String getId() {
        return "_id";
    }
}
