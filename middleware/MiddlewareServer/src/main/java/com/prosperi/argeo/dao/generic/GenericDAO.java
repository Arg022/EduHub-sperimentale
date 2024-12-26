package com.prosperi.argeo.dao.generic;

import java.util.List;
import java.util.UUID;

public interface GenericDAO<T> {
    T findById(UUID id);
    List<T> findAll();
    void save(T entity);
    void update(T entity);
    void delete(UUID id);
}
