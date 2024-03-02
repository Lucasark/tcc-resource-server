package tcc.uff.resource.server.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tcc.uff.resource.server.model.document.FrequencyDocument;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface FrequencyRepository extends MongoRepository<FrequencyDocument, String>, MongoHelperRepository<FrequencyDocument, String> {

    List<FrequencyDocument> findAllByCourseId(String courseId);

    Optional<FrequencyDocument> findByDateAndCourseId(Instant date, String courseId);

    List<FrequencyDocument> findByIdInAndFinished(Collection<String> id, Boolean finished);

    Optional<FrequencyDocument> findByCourseIdAndDate(String courseId, Instant date);

    List<FrequencyDocument> findFirst1ByIdInAndFinishedOrderByDate(Collection<String> id, Boolean finished);

}
