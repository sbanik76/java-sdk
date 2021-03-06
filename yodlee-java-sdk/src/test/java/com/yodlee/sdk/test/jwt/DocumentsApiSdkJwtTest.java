/**
 * Copyright (c) 2019 Yodlee, Inc. All Rights Reserved.
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */
package com.yodlee.sdk.test.jwt;

import org.databene.benerator.anno.Source;
import org.testng.annotations.Test;
import com.yodlee.sdk.api.DocumentsApi;
import com.yodlee.sdk.test.DocumentsApiSdkTest;

public class DocumentsApiSdkJwtTest extends AbstractSdkJWTUserTestSuite {

	private final DocumentsApi documentsApi = new DocumentsApi(null);

	public DocumentsApiSdkJwtTest() {
		documentsApi.setContext(AbstractSdkJWTUserTestSuite.setup());
	}

	@Test(enabled = true, dataProvider = "feeder")
	@Source("\\Documents\\getDocuments.csv")
	public void getDocuments(String testCaseId, String testCaseName, String keyword, String accountId, String docType,
			String fromDate, String toDate, String enabled) {
		DocumentsApiSdkTest.getDocumentsTest(documentsApi, testCaseId, testCaseName, keyword, accountId, docType,
				fromDate, toDate);
	}

	@Test(enabled = true, dataProvider = "feeder")
	@Source("\\Documents\\downloadDocuments.csv")
	public void downloadDocument(String testCaseId, String testCaseName, String documentId, String enabled) {
		DocumentsApiSdkTest.downloadDocumentTest(documentsApi, testCaseId, testCaseName, documentId);
	}

	@Test(enabled = true, dataProvider = "feeder")
	@Source("\\Documents\\deleteDocument.csv")
	public void deleteDocument(String testCaseId, String testCaseName, String documentId, String enabled) {
		DocumentsApiSdkTest.deleteDocumentTest(documentsApi, testCaseId, testCaseName, documentId);
	}
}
