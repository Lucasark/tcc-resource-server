package tcc.uff.resource.server.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tcc.uff.resource.server.model.document.CourseDocument;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends MongoRepository<CourseDocument, String>, CustomizedCourseRepository {

    List<CourseDocument> findByTeacherEmail(String user);

    List<CourseDocument> findByMembersEmail(String user);

    Optional<CourseDocument> findByIdAndTeacherEmail(String id, String teacherEmail);
}
