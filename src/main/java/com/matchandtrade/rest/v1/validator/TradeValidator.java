package com.matchandtrade.rest.v1.validator;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.matchandtrade.common.Pagination;
import com.matchandtrade.common.SearchCriteria;
import com.matchandtrade.common.SearchResult;
import com.matchandtrade.persistence.criteria.TradeCriteriaBuilder;
import com.matchandtrade.persistence.criteria.TradeMembershipCriteriaBuilder;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.repository.TradeMembershipRepository;
import com.matchandtrade.repository.TradeRepository;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.TradeJson;

@Component
public class TradeValidator {

	@Autowired
	private TradeRepository tradeRepository;
	@Autowired
	private TradeMembershipRepository tradeMembershipRepository;
	
	/**
	 * {@code TradeJson.name} is mandatory, unique and must contain at least 3 characters.
	 * @param userId
	 * @param json
	 */
	public void validatePost(TradeJson json) {
		if (json.getName() == null || json.getName().length() < 3 || json.getName().length() > 150) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Trade.name is mandatory and must be between 3 and 150 characters in length.");
		}
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination());
		searchCriteria.addCriterion(TradeCriteriaBuilder.Criterion.name, json.getName());
		SearchResult<TradeEntity> searchResult = tradeRepository.search(searchCriteria);
		if (!searchResult.getResultList().isEmpty()) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Trade.name must be unique.");
		}
	}

	/**
	 * Validates if the {@code Trade.tradeId} exists otherwise return status NOT_FOUND
	 * Validates if authenticated {@code user} is the owner of the trade
	 * @param json
	 * @param user
	 */
	@Transactional
	public void validatePut(TradeJson json, UserEntity user) {
		// Validates if the Trade.tradeId exists otherwise return status NOT_FOUND
		TradeEntity t = tradeRepository.get(json.getTradeId());
		if (t == null) {
			throw new RestException(HttpStatus.NOT_FOUND);
		}
		
		// Validates if authenticated user is the owner of the trade
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination(1,1));
		searchCriteria.addCriterion(TradeMembershipCriteriaBuilder.Criterion.tradeId, json.getTradeId());
		searchCriteria.addCriterion(TradeMembershipCriteriaBuilder.Criterion.userId, user.getUserId());
		searchCriteria.addCriterion(TradeMembershipCriteriaBuilder.Criterion.type, TradeMembershipEntity.Type.OWNER);
		SearchResult<TradeMembershipEntity> searchResult = tradeMembershipRepository.search(searchCriteria);
		if (searchResult.getResultList().isEmpty()) {
			throw new RestException(HttpStatus.FORBIDDEN, "Authenticated user is not the owner of Trade.tradeId: " + json.getTradeId());
		}
		validatePost(json);
	}
	
	@Transactional
	public void validateDelete(Integer tradeId) {
		TradeEntity tradeEntity = tradeRepository.get(tradeId);
		if (tradeEntity == null) {
			throw new RestException(HttpStatus.NOT_FOUND);
		}
	}

}
