package tcc.uff.resource.server.service.mongooperations;

public interface MongoOperationsService {

    <A> void addInSet(String keyId, String valueId, String key, A value, Class<?> clazz);
}
