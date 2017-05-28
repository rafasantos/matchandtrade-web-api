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

	/*
	 * Check if name is mandatory and must be between 3 and 150 characters in length.
	 */
	private void checkNameLength(String name) {
		if (name == null || name.length() < 3 || name.length() > 150) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Trade.name is mandatory and its length must be between 3 and 150.");
		}
	}

	/**
	 * Validates if name is mandatory and must be between 3 and 150 characters in length.
	 * Validates if name is unique.
	 * @param userId
	 * @param json
	 */
	public void validatePost(TradeJson json) {
		checkNameLength(json.getName());
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination());
		searchCriteria.addCriterion(TradeCriteriaBuilder.Criterion.name, json.getName());
		SearchResult<TradeEntity> searchResult = tradeRepository.search(searchCriteria);
		if (!searchResult.getResultList().isEmpty()) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Trade.name must be unique.");
		}
	}


	/**
	 * Validates if name is mandatory and must be between 3 and 150 characters in length.
	 * Validates if the {@code Trade.tradeId} exists otherwise return status NOT_FOUND.
	 * Validates if authenticated {@code user} is the owner of the trade
	 * Validates if name is unique but not the same which is being updated
	 * @param json
	 * @param user
	 */
	@Transactional
	public void validatePut(TradeJson json, UserEntity user) {
		checkNameLength(json.getName());

		// Validates if the Trade.tradeId exists otherwise return status NOT_FOUND
		TradeEntity t = tradeRepository.get(json.getTradeId());
		if (t == null) {
			throw new RestException(HttpStatus.NOT_FOUND);
		}
		
		// Validates if authenticated user is the owner of the trade
		SearchCriteria searchTradeOwner = new SearchCriteria(new Pagination(1,1));
		searchTradeOwner.addCriterion(TradeMembershipCriteriaBuilder.Criterion.tradeId, json.getTradeId());
		searchTradeOwner.addCriterion(TradeMembershipCriteriaBuilder.Criterion.userId, user.getUserId());
		searchTradeOwner.addCriterion(TradeMembershipCriteriaBuilder.Criterion.type, TradeMembershipEntity.Type.OWNER);
		SearchResult<TradeMembershipEntity> searchResultTradeOwner = tradeMembershipRepository.search(searchTradeOwner);
		if (searchResultTradeOwner.getResultList().isEmpty()) {
			throw new RestException(HttpStatus.FORBIDDEN, "Authenticated user is not the owner of Trade.tradeId: " + json.getTradeId());
		}

		// Validates if name is unique but not the same which is being updated
		SearchCriteria searchUniqueName = new SearchCriteria(new Pagination(1,2));
		searchUniqueName.addCriterion(TradeCriteriaBuilder.Criterion.name, json.getName());
		SearchResult<TradeEntity> searchResultUniqueName = tradeRepository.search(searchUniqueName);
		// If results and it is not the same we the updating trade, then name already exists. Otherwise the result belongs to the same trade it is trying to update.
		if (!searchResultUniqueName.getResultList().isEmpty() && !json.getTradeId().equals(searchResultUniqueName.getResultList().get(0).getTradeId())) {
				throw new RestException(HttpStatus.BAD_REQUEST, "Trade.name must be unique.");
		}
	}
	
	@Transactional
	public void validateDelete(Integer tradeId) {
		TradeEntity tradeEntity = tradeRepository.get(tradeId);
		if (tradeEntity == null) {
			throw new RestException(HttpStatus.NOT_FOUND);
		}
	}

}
