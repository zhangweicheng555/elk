package com.kuandeng.kelk.config;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {


	@Bean
	public TransportClient transportClient() throws UnknownHostException {
		Settings settings = Settings.builder().put("cluster.name", "elasticsearch").put("client.transport.sniff", true)
				.build();
		TransportClient transportClient = new PreBuiltTransportClient(settings);
		TransportAddress transportAddress = new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300);
		transportClient.addTransportAddress(transportAddress);
		return transportClient;
	}

	/*
	 * @Bean public RestClientBuilder restClientBuilder() { HttpHost[] hosts = new
	 * HttpHost[] {new HttpHost("127.0.0.1", 9200, HTTP_SCHEME)}; return
	 * RestClient.builder(hosts); }
	 * 
	 * @Bean public RestHighLevelClient restHighLevelClient(@Autowired
	 * RestClientBuilder restClientBuilder) {
	 * restClientBuilder.setMaxRetryTimeoutMillis(60000); return new
	 * RestHighLevelClient(restClientBuilder.build()); }
	 */
}
