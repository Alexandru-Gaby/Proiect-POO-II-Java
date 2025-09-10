package Dao;
import java.util.List;

import java.util.List;
import java.util.Optional;

public interface GenericDAO<T, ID> {
    T save(T entity);
    T update(T entity);
    boolean delete(ID id);
    Optional<T> findById(ID id);
    List<T> findAll();
}