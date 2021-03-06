/**
 * Copyright (c) 2019 Yodlee, Inc. All Rights Reserved.
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */
package com.yodlee.sdk.sampleflow;

import static com.yodlee.sdk.sampleflow.DemoAddAccountApplication.RESOURCES;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yodlee.api.model.AbstractModelComponent;
import com.yodlee.api.model.Field;
import com.yodlee.api.model.Name;
import com.yodlee.api.model.account.response.AccountResponse;
import com.yodlee.api.model.cobrand.CreateCobrandNotificationEvent;
import com.yodlee.api.model.cobrand.enums.CobrandNotificationEventType;
import com.yodlee.api.model.cobrand.request.CreateCobrandNotificationEventRequest;
import com.yodlee.api.model.provideraccounts.request.ProviderAccountRequest;
import com.yodlee.api.model.provideraccounts.response.AddedProviderAccountResponse;
import com.yodlee.api.model.providers.Providers;
import com.yodlee.api.model.providers.response.ProviderDetailResponse;
import com.yodlee.api.model.providers.response.ProviderResponse;
import com.yodlee.api.model.user.UserRegistration;
import com.yodlee.api.model.user.request.UserRequest;
import com.yodlee.api.model.user.response.UserResponse;
import com.yodlee.sdk.api.AccountsApi;
import com.yodlee.sdk.api.CobrandApi;
import com.yodlee.sdk.api.ProviderAccountsApi;
import com.yodlee.sdk.api.ProvidersApi;
import com.yodlee.sdk.api.UserApi;
import com.yodlee.sdk.api.exception.ApiException;
import com.yodlee.sdk.client.ApiListener;
import com.yodlee.sdk.client.ApiResponse;
import com.yodlee.sdk.context.JWTAppContext;
import com.yodlee.sdk.context.JWTUserContext;

/**
 * 
 * A class that consolidates the utility methods for Add account flow
 *
 */
public class AddAccountFlow {

	private AddAccountFlow() {}

	static ObjectMapper mapper = new ObjectMapper();

	public static void subscribeRefreshEvent(JWTAppContext jwtCobrandContext) throws ApiException {
		CobrandApi cobrandApi = new CobrandApi();
		cobrandApi.setContext(jwtCobrandContext);
		CreateCobrandNotificationEventRequest eventRequest = new CreateCobrandNotificationEventRequest();
		CreateCobrandNotificationEvent event = new CreateCobrandNotificationEvent();
		event.setCobrandNotificationEventName(CobrandNotificationEventType.REFRESH);
		String dns = DemoAddAccountApplication.RESOURCES.getDnsName();
		// Set callback URL to subscribe for REFRESH event
		event.setCallbackUrl("http://" + dns + ":" + getPort() + "/yourApp/callback");
		eventRequest.setCobrandNotificationEvent(event);
		ApiResponse<AbstractModelComponent> subribeEvent =
				cobrandApi.createSubscriptionEvent(CobrandNotificationEventType.REFRESH, eventRequest);
		System.out.println(String.format("subscribeEvent : %s ", subribeEvent.getStatusCode()));
	}

	public static UserResponse registerUser(JWTAppContext jwtCobrandContext, String userName) throws ApiException {
		UserApi userApi = new UserApi(jwtCobrandContext);
		UserResponse userResponse = null;
		UserRequest userRequest = new UserRequest();
		UserRegistration user = new UserRegistration();
		user.setLoginName(RESOURCES.getUserLoginName());
		Name name = new Name();
		// Set user values
		name.setFirst("john");
		name.setLast("doe");
		user.setName(name);
		user.setEmail("email@email.com");
		// Can set other values in user
		userRequest.setUser(user);
		ApiResponse<UserResponse> registeredUser = userApi.registerUser(userRequest);
		System.out.println(String.format("registerUser : %s ", registeredUser.getStatusCode()));
		userResponse = registeredUser.getData();
		System.out.println("Registered User Response : " + Utils.printResponse(userResponse));
		return userResponse;
	}

	public static long getProviderId(JWTUserContext jwtUserContext, String name, String providerId)
			throws ApiException {
		ProvidersApi providersApi = new ProvidersApi(jwtUserContext);
		providersApi.addApiListener(sampleApiListener());
		ApiResponse<ProviderResponse> providerResponse =
				providersApi.getAllProviders(null, null, name, null, null, null);
		ProviderResponse providersList = providerResponse.getData();
		if (providersList != null) {
			List<Providers> providers = providersList.getProviders();
			for (Providers provider : providers) {
				// Here we are checking for providerId passed in is ther in response or not
				if (provider.getId().toString().equals(providerId)) {
					return provider.getId();
				}
			}
		}
		return -1L;
	}

	public static ProviderDetailResponse getProviderDetails(JWTUserContext jwtUserContext, Long providerId)
			throws ApiException {
		ProvidersApi providersApi = new ProvidersApi(jwtUserContext);
		ProviderDetailResponse provider = null;
		ApiResponse<ProviderDetailResponse> providerDetails = providersApi.getProvider(providerId);
		provider = providerDetails.getData();
		return provider;
	}

	public static AddedProviderAccountResponse linkAccount(JWTUserContext jwtUserContext,
			ProviderDetailResponse providerDetails, Long providerId, List<Field> fieldList) throws ApiException {
		List<Field> requestfields = new ArrayList<>();
		// List<Row> rows = providerDetails.getProviders().get(0).getLoginForms().get(0).getRows();
		// for (Row row : rows) {
		// List<Field> fields = row.getFields();
		// for (Field field : fields) {
		// Field newField = new Field();
		// newField.setId(field.getId());
		// newField.setName(field.getName());
		// newField.setValue("value"); //credentials
		// requestfields.add(newField);
		// }
		// }
		for (Field field : fieldList) {
			Field newField = new Field();
			newField.setId(field.getId());
			newField.setValue(field.getValue());
			requestfields.add(newField);
		}
		ProviderAccountRequest providerAccountRequest = new ProviderAccountRequest();
		providerAccountRequest.setField(requestfields);
		System.out.println("Add account request : " + Utils.printResponse(providerAccountRequest));
		ProviderAccountsApi providerAccountsApi = new ProviderAccountsApi(jwtUserContext);
		ApiResponse<AddedProviderAccountResponse> linkedProviderAccount = null;
		linkedProviderAccount = providerAccountsApi.linkCredentialProviderAccount(providerAccountRequest, providerId);
		System.out.println("Account added : " + Utils.printResponse(linkedProviderAccount.getData()));
		return linkedProviderAccount.getData();
	}

	public static AccountResponse getAccounts(JWTUserContext jwtUserContext, Long providerAccountIds)
			throws ApiException {
		AccountsApi accountsApi = new AccountsApi(jwtUserContext);
		Long[] providerAccountId = Utils.convertStringtoLongArray(providerAccountIds.toString());
		ApiResponse<AccountResponse> accounts = null;
		accounts = accountsApi.getAllAccounts(null, null, null, providerAccountId, null, null);
		return accounts.getData();
	}

	private static int getPort() {
		int port = 0;
		try (InputStream inStream = AddAccountFlow.class.getResourceAsStream("/application.properties");) {
			Properties properties = new Properties();
			properties.load(inStream);
			port = Integer.valueOf(properties.getProperty("server.port"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return port;
	}

	private static ApiListener sampleApiListener() {
		return new ApiListener() {

			@Override
			public void responseUpdate(long bytesRead, long contentLength, boolean done) {
				System.out.println(String.format("responseUpdate : bytesRead = %d, contentLength = %d, done = %s",
						bytesRead, contentLength, done));
			}

			@Override
			public void requestUpdate(long bytesWritten, long contentLength, boolean done) {
				System.out.println(String.format("requestUpdate : bytesWritten = %d, contentLength = %d, done = %s",
						bytesWritten, contentLength, done));
			}
		};
	}
}
