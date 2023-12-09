package tcc.uff.resource.server.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tcc.uff.resource.server.model.document.FrequencyDocument;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface FrequencyRepository extends MongoRepository<FrequencyDocument, String> {

    Optional<FrequencyDocument> findByDate(Instant date);

    Optional<FrequencyDocument> findByDateAndCourseId(Instant date, String courseId);
    List<FrequencyDocument> findByDateBetweenAndCourseId(Instant start, Instant end, String courseId);

}
