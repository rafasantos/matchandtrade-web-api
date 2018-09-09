package com.matchandtrade.persistence.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.matchandtrade.persistence.entity.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer>{
	
	UserEntity findByEmail(String email);

	@Query("SELECT u "
			+ " FROM MembershipEntity tm"
			+ " INNER JOIN tm.user AS u"
			+ " INNER JOIN tm.trade AS t"
			+ " INNER JOIN tm.articles AS i"
			+ " WHERE"
			+ " i.articleId = :articleId")
	UserEntity findByArticleId(@Param("articleId") Integer articleId);

	
	@Query("SELECT u "
			+ " FROM MembershipEntity tm"
			+ " INNER JOIN tm.user AS u"
			+ " INNER JOIN tm.offers AS o"
			+ " WHERE"
			+ " o.offerId = :offerId")
	UserEntity findByOfferId(@Param("offerId")Integer offerId);

	UserEntity findByArticles_ArticleId(Integer articleId);

}
