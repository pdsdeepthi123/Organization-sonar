package com.dtt.organization;


import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import org.apache.hc.core5.ssl.TrustStrategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import ug.daes.DAESService;
import ug.daes.Result;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.DefaultClientTlsStrategy;
import org.apache.hc.core5.ssl.SSLContextBuilder;

import org.apache.hc.client5.http.io.HttpClientConnectionManager;



@OpenAPIDefinition(info = @Info(title = "My API", version = "1.0", description = "API documentation"))
@SpringBootApplication
public class OrganizationApplication {

	Logger logger = LoggerFactory.getLogger(OrganizationApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(OrganizationApplication.class, args);

	}

	@Bean
	public RestTemplate restTemplate() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

		SSLContext sslContext = SSLContextBuilder.create()
				.loadTrustMaterial(null, acceptingTrustStrategy)
				.build();

		var tlsStrategy = new DefaultClientTlsStrategy(sslContext);

		HttpClientConnectionManager connectionManager =
				PoolingHttpClientConnectionManagerBuilder.create()
						.setTlsSocketStrategy(tlsStrategy)
						.build();

		CloseableHttpClient httpClient = HttpClients.custom()
				.setConnectionManager(connectionManager)
				.build();

		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

		requestFactory.setConnectionRequestTimeout(300_000);
		requestFactory.setConnectTimeout(300_000);
		requestFactory.setReadTimeout(300_000);

		return new RestTemplate(requestFactory);
	}



	@Bean
	public Result signatueServiceInitilize() {
		try {
			return DAESService.initPKINativeUtils();
		} catch (Exception e) {
			logger.error("PKI initialization failed", e);
			return new Result();
		}
	}



}
