package tcc.uff.resource.server.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;
import tcc.uff.resource.server.model.document.FrequencyDocument;
import tcc.uff.resource.server.repository.CustomizedFrequencyRepository;

@Slf4j
@Component
public class CustomizedFrequencyRepositoryImpl extends CustomizedAbstractMongoOperationsImpl<FrequencyDocument> implements CustomizedFrequencyRepository {

    CustomizedFrequencyRepositoryImpl(MongoOperations operations) {
        super(operations);
    }

    @Override
    public Class<FrequencyDocument> getTypedClass() {
        return FrequencyDocument.class;
    }
}
