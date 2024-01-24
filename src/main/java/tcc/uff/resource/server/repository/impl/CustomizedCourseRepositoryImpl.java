package tcc.uff.resource.server.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;
import tcc.uff.resource.server.model.document.CourseDocument;
import tcc.uff.resource.server.repository.CustomizedCourseRepository;

@Slf4j
@Component
public class CustomizedCourseRepositoryImpl extends CustomizedAbstractMongoOperationsImpl<CourseDocument> implements CustomizedCourseRepository {

    CustomizedCourseRepositoryImpl(MongoOperations operations) {
        super(operations);
    }

    @Override
    public Class<CourseDocument> getTypedClass() {
        return CourseDocument.class;
    }
}
