package tcc.uff.resource.server.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tcc.uff.resource.server.model.document.AttendenceDocument;

@Repository
public interface AttendenceRepository extends MongoRepository<AttendenceDocument, String> {
}
