/**
 * Copyright (c) 2019 Yodlee, Inc. All Rights Reserved.
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */
package com.yodlee.api.model.transaction.request;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yodlee.api.model.AbstractModelComponent;
import com.yodlee.api.model.Request;
import com.yodlee.api.model.transaction.TransactionCategorizationRuleInfo;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionCategorizationRuleRequest extends AbstractModelComponent implements Request {

	@NotNull(message = "{transactions.rule.required}")
	@Valid
	@JsonProperty("rule")
	private TransactionCategorizationRuleInfo rule;

	public TransactionCategorizationRuleInfo getRule() {
		return rule;
	}

	public void setRule(TransactionCategorizationRuleInfo rule) {
		this.rule = rule;
	}

	@Override
	public String toString() {
		return "TransactionCategorizationRuleRequest [rule=" + rule + "]";
	}
}
