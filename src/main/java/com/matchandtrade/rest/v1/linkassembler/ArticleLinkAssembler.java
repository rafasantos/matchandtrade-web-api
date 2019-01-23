package com.matchandtrade.rest.v1.linkassembler;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.rest.v1.controller.ArticleController;
import com.matchandtrade.rest.v1.json.ArticleJson;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Component
public class ArticleLinkAssembler {
	public void assemble(SearchResult<ArticleJson> searchResult) {
		for (ArticleJson json : searchResult.getResultList()) {
			assemble(json);
		}
	}

	public void assemble(ArticleJson json) {
		Link self = linkTo(ArticleController.class).slash(json.getArticleId()).withSelfRel();
		json.add("self", self.getHref());
	}
}
