package com.matchandtrade.rest.v1.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.matchandtrade.authorization.AuthorizationValidator;
import com.matchandtrade.rest.service.AuthenticationService;
import com.matchandtrade.rest.service.TradeResultService;
import com.matchandtrade.rest.v1.json.TradeResultJson;
import com.matchandtrade.rest.v1.validator.TradeResultValidator;

@RestController
@RequestMapping(path="/matchandtrade-api/v1/trades")
public class TradeResultController implements Controller {

	@Autowired
	AuthenticationService authenticationService;
	@Autowired
	TradeResultService tradeResultService;
	@Autowired
	TradeResultValidator tradeResultValidator;

	@RequestMapping(path="/{tradeId}/results",
			method = RequestMethod.GET,
			consumes = "text/csv",
			produces = "text/csv")
	public String getCsv(@PathVariable("tradeId") Integer tradeId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationService.findCurrentAuthentication());
		// Validate the request
		tradeResultValidator.validateGet(tradeId);
		// Delegate to Service layer
		return tradeResultService.findByTradeIdAsCsv(tradeId);
		// Transform the response - Nothing to transform
		// Assemble links - Nothing to assemble
	}

	@RequestMapping(path="/{tradeId}/results",
			method = RequestMethod.GET,
			produces = "application/json")
	public TradeResultJson getJson(@PathVariable("tradeId") Integer tradeId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationService.findCurrentAuthentication());
		// Validate the request
		tradeResultValidator.validateGet(tradeId);
		// Delegate to Service layer
		return tradeResultService.findByTradeIdAsJson(tradeId);
		// Transform the response - Nothing to transform
		// Assemble links - Nothing to assemble
	}

}
