package com.github.ahunigel.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.dialect.Dialect;
import org.hibernate.internal.SessionFactoryImpl;

import javax.persistence.EntityManager;

/**
 * Created by Nigel.Zheng on 8/11/20 011.
 *
 * @author Nigel.Zheng
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DialectUtil {
  public static int getInExpressionCountLimit(EntityManager entityManager) {
    final Dialect dialect = getDialect(entityManager);
    return dialect.getInExpressionCountLimit();
  }

  public static Dialect getDialect(EntityManager entityManager) {
    final Session session = (Session) entityManager.getDelegate();
    final SessionFactoryImpl sessionFactory = (SessionFactoryImpl) session.getSessionFactory();
    return sessionFactory.getJdbcServices().getDialect();
  }
}
