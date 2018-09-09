package com.matchandtrade.persistence.criteria;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.common.SearchCriteria;

@Component
public class UserQueryBuilder implements QueryBuilder {

	public enum Field implements com.matchandtrade.persistence.common.Field {
		articleId("article.articleId");
		
		private String alias;

		private Field(String alias) {
			this.alias = alias;
		}
		
		@Override
		public String alias() {
			return alias;
		}
	}
	
	@Autowired
	private EntityManager entityManager;
	
	private static final String BASIC_HQL = "FROM MembershipEntity AS tm"
    		+ " INNER JOIN tm.user AS user"
    		+ " INNER JOIN tm.trade AS trade"
    		+ " INNER JOIN tm.articles AS article";

	@Override
	public Query buildCountQuery(SearchCriteria searchCriteria) {
		StringBuilder hql = new StringBuilder("SELECT COUNT(*) " + BASIC_HQL);
		return QueryBuilderUtil.buildQuery(searchCriteria, hql, entityManager, true);
	}

	@Override
	public Query buildSearchQuery(SearchCriteria searchCriteria) {
	StringBuilder hql = new StringBuilder("SELECT user " + BASIC_HQL);
		return QueryBuilderUtil.buildQuery(searchCriteria, hql, entityManager);
	}

}
