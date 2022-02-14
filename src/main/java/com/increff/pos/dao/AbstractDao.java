package com.increff.pos.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

public abstract class AbstractDao {

    @PersistenceContext
    private EntityManager em;

    protected <T> T getSingle(TypedQuery<T> query) {
        return query.getResultList().stream().findFirst().orElse(null);
    }

    protected <T> TypedQuery<T> getQuery(String jpql, Class<T> clazz) {
        return em.createQuery(jpql, clazz);
    }

    protected EntityManager em() {
        return em;
    }

    @Transactional
    public <T> void insert(T pojo) {
        em.persist(pojo);
    }

    public <T> void update(T pojo) {
    }
}
