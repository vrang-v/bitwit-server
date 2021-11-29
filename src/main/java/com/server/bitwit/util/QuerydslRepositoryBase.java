package com.server.bitwit.util;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

import static org.springframework.util.Assert.notNull;

@Getter
@Repository
public abstract class QuerydslRepositoryBase {
    
    private final Class<?> clazz;
    
    private EntityManager   entityManager;
    private JPAQueryFactory query;
    private Querydsl        querydsl;
    
    protected QuerydslRepositoryBase(Class<?> clazz) {
        notNull(clazz, "Domain class mush not be null!");
        this.clazz = clazz;
    }
    
    @PostConstruct
    public void validate( ) {
        notNull(entityManager, "EntityManager must not be null!");
        notNull(querydsl, "Querydsl must not be null!");
        notNull(query, "JPAQueryFactory must not be null!");
    }
    
    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        notNull(entityManager, "EntityManager must not be null");
        var entityInformation        = JpaEntityInformationSupport.getEntityInformation(clazz, entityManager);
        var simpleEntityPathResolver = SimpleEntityPathResolver.INSTANCE;
        var path                     = simpleEntityPathResolver.createPath(entityInformation.getJavaType( ));
        this.entityManager = entityManager;
        this.querydsl      = new Querydsl(entityManager, new PathBuilder<>(path.getType( ), path.getMetadata( )));
        this.query         = new JPAQueryFactory(entityManager);
    }
    
    protected <T> JPAQuery<T> select(Expression<T> expression) {
        return query.select(expression);
    }
    
    protected <T> JPAQuery<T> selectFrom(EntityPath<T> entityPath) {
        return query.selectFrom(entityPath);
    }
    
    protected <T> JPAQuery<T> selectDistinct(Expression<T> expression) {
        return query.selectDistinct(expression);
    }
    
    protected <T> Page<T> paginate(Pageable pageable, JPAQuery<T> query) {
        var content = querydsl.applyPagination(pageable, query).fetch( );
        return PageableExecutionUtils.getPage(content, pageable, query::fetchCount);
    }
    
    protected <T, C> Page<T> paginate(Pageable pageable, JPAQuery<T> contentQuery, JPAQuery<C> countQuery) {
        var content = querydsl.applyPagination(pageable, contentQuery).fetch( );
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }
}
