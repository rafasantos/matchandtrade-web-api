package com.matchandtrade.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.matchandtrade.persistence.entity.TradeEntity;

@Repository
public interface TradeRepository extends CrudRepository<TradeEntity, Integer>{

	@Query("SELECT trade FROM MembershipEntity AS membership"
			+ " INNER JOIN membership.trade AS trade"
			+ " INNER JOIN membership.articles AS article"
			+ " WHERE"
			+ " article.articleId IN (:articleIds)"
			+ " GROUP BY trade")
	Page<TradeEntity> findInArticleIdsGroupByTrade(@Param("articleIds") Integer[] articleIds, Pageable pageable);
	
}
