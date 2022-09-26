package com.cdx.bas.application.repository;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;

public abstract class JPARepository<T, PK extends Serializable> implements GenericRepository<T, PK>{
    
    private Class<T> persistentClass;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @SuppressWarnings("unchecked")
    public JPARepository() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];
    }
    
    public Class<T> getPersistentClass() {
        return persistentClass;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public T add(T entity) {
        entityManager.persist(entity);
        return entity;
    }
    
    @Override
    public Optional<T> findById(PK id){
        if(id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(entityManager.find(getPersistentClass(), id));
    }

    @Override
    public T update(T entity) {
        return entityManager.merge(entity);
    }

    @Override
    public void remove(T entity) {
        entityManager.remove(entity);
    }

    @Override
    public Session getSession() {
        return entityManager.unwrap(Session.class);
    }   
}