package tcc.uff.resource.server.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tcc.uff.resource.server.model.document.CourseDocument;

import java.util.List;

@Repository
public interface CourseRepository extends MongoRepository<CourseDocument, String> {

    List<CourseDocument> findByTeacherEmail(String user);

    List<CourseDocument> findByMembersEmail(String user);
}