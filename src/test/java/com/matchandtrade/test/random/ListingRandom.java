package com.matchandtrade.test.random;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import com.matchandtrade.persistence.facade.MembershipRepositoryFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ListingRandom {
	
	@Autowired
	private ArticleRepositoryFacade articleRepositoryFacade;
	@Autowired
	private MembershipRepositoryFacade membershipRepositoryFacade;

	@Transactional
	public void createPersisted(Integer articleId, Integer membershipId) {
		ArticleEntity article = articleRepositoryFacade.get(articleId);
		MembershipEntity membership = membershipRepositoryFacade.find(membershipId);
		membership.getArticles().add(article);
		membershipRepositoryFacade.save((membership));
	}

}