package tcc.uff.resource.server.repository;

import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface MongoHelperRepository<T, ID extends Serializable> {

    <S> void addInSet(ID id, String key, S value);

    <S> void pull(ID id, String key, S value);
}
