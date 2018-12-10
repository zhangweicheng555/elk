package com.kuandeng.kelk.config;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

	private static final String HTTP_SCHEME = "http";
	public static final int MAX_PAGE_SIZE = 10000;

	@Bean
	public RestClientBuilder restClientBuilder() {
		HttpHost[] hosts = new HttpHost[] {new HttpHost("192.168.5.34", 9200, HTTP_SCHEME)};
		return RestClient.builder(hosts);
	}

	@Bean
	public RestHighLevelClient restHighLevelClient(@Autowired RestClientBuilder restClientBuilder) {
		restClientBuilder.setMaxRetryTimeoutMillis(60000);
		return new RestHighLevelClient(restClientBuilder.build());
	}
}
