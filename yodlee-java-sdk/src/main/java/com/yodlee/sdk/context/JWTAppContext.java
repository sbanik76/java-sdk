/**
 * Copyright (c) 2019 Yodlee, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yodlee, Inc. Use is subject to license terms.
 */
package com.yodlee.sdk.context;

import com.yodlee.sdk.configuration.cobrand.JWTAppConfiguration;

public class JWTAppContext extends AbstractJWTContext<JWTAppConfiguration> {

	public JWTAppContext(String jwtToken, JWTAppConfiguration jwtAppConfiguration) {
		super(jwtToken, jwtAppConfiguration);
	}

	@Override
	public ContextType getContextType() {
		return ContextType.COBRAND;
	}
}