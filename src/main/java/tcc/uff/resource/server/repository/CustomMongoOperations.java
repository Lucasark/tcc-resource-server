package tcc.uff.resource.server.repository;

public interface CustomMongoOperations<T> {

    //REMOVER DO SET

    <A> void addInSet(String valueId, String key, A value);

    Class<T> getTypedClass();

    String getId();
}
