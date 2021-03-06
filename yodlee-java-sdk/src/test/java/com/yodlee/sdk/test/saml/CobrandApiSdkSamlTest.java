/**
 * Copyright (c) 2019 Yodlee, Inc. All Rights Reserved.
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */
package com.yodlee.sdk.test.saml;

import org.databene.benerator.anno.Source;
import org.testng.annotations.Test;
import com.yodlee.sdk.api.CobrandApi;
import com.yodlee.sdk.api.exception.ApiException;
import com.yodlee.sdk.test.CobrandApiSdkTest;

public class CobrandApiSdkSamlTest extends AbstractSdkSAMLCobrandTestSuite {

	private static final CobrandApi cobrandApi = new CobrandApi(null);

	public CobrandApiSdkSamlTest() throws ApiException {
		cobrandApi.setContext(AbstractSdkSAMLCobrandTestSuite.setup());
	}

	@Test(enabled = false, dataProvider = "feeder")
	@Source("\\Cobrand\\authenticateCoBrandLogin.csv")
	public void cobrandLoginUsingPOSTTest(String testCaseId, String testCaseName, String enabled) {
		CobrandApiSdkTest.testCobrandLogin(cobrandApi, testCaseId, testCaseName);
	}

	@Test(enabled = false, dataProvider = "feeder")
	@Source("\\Cobrand\\deleteSubscription.csv")
	public void cobrandLogoutUsingDELETETest(String testCaseId, String testCaseName, String enabled) {
		CobrandApiSdkTest.testCobrandLogout(cobrandApi);
	}

	@Test(enabled = false, dataProvider = "feeder")
	@Source("\\Cobrand\\publicKey.csv")
	public void publicKeyUsingGETTest(String testCaseId, String testCaseName, String enabled) {
		CobrandApiSdkTest.testGetPublicKey(cobrandApi, testCaseId, testCaseName);
	}

	@Test(enabled = true, dataProvider = "feeder")
	@Source("\\Cobrand\\subscribeEvent.csv")
	public void subscribeEventUsingPOSTTest(String testCaseId, String testCaseName, String fileName, String eventName,
			String enabled) {
		CobrandApiSdkTest.testSubsribeEvent(cobrandApi, testCaseId, testCaseName, fileName, eventName);
	}

	@Test(enabled = true, dataProvider = "feeder")
	@Source("\\Cobrand\\getSubscribedEvents.csv")
	public void getSubscribedEventsUsingGETTest(String testCaseId, String testCaseName, String fileName,
			String eventName, String enabled) {
		CobrandApiSdkTest.testGetSubsribedEvent(cobrandApi, testCaseId, testCaseName, fileName, eventName);
	}

	@Test(enabled = true, dataProvider = "feeder")
	@Source("\\Cobrand\\updateSubscription.csv")
	public void updateSubscriptionUsingPUTTest(String testCaseId, String testCaseName, String fileNametoSubcribe,
			String fileName, String eventName, String enabled) {
		CobrandApiSdkTest.testUpdateSubsribedEvent(cobrandApi, testCaseId, testCaseName, fileNametoSubcribe, fileName,
				eventName);
	}

	@Test(enabled = true, dataProvider = "feeder")
	@Source("\\Cobrand\\deleteSubscription.csv")
	public void deleteSubscriptionUsingDELETETest(String testCaseId, String testCaseName, String fileName,
			String eventName, String enabled) {
		CobrandApiSdkTest.testDeleteSubsribeEvent(cobrandApi, testCaseId, testCaseName, fileName, eventName);
	}
}
