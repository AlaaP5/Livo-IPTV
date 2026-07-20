package com.rand.ishowApi.config;

import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.ClientTlsStrategyBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.opensearch.client.json.jackson.JacksonJsonpMapper;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.transport.httpclient5.ApacheHttpClient5TransportBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;


@Configuration
public class OpenSearchConfig {


    @Value("${opensearch.https:true}")
    private boolean https;

    @Value("${opensearch.host:localhost}")
    private String host;

    @Value("${opensearch.port:9200}")
    private int port;

    @Value("${opensearch.username:admin}")
    private String username;

    @Value("${opensearch.password:T7k9!vQ2@pL8}")
    private String password;
    @Bean
    public OpenSearchClient openSearchClient() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        // Use @Value-injected fields, no need for System.getenv()
        final var hosts = new HttpHost[] { new HttpHost(https ? "https" : "http", host, port) };

        final var sslContext = SSLContextBuilder.create()
                .loadTrustMaterial(null, (chains, authType) -> true)
                .build();

        final var transport = ApacheHttpClient5TransportBuilder.builder(hosts)
                .setMapper(new JacksonJsonpMapper())
                .setHttpClientConfigCallback(httpClientBuilder -> {

                    // Credentials
                    final var credentialsProvider = new BasicCredentialsProvider();
                    for (var h : hosts) {
                        credentialsProvider.setCredentials(
                                new AuthScope(h),
                                new UsernamePasswordCredentials(username, password.toCharArray())
                        );
                    }

                    // Disable SSL/TLS verification (dev only)
                    final var tlsStrategy = ClientTlsStrategyBuilder.create()
                            .setSslContext(sslContext)
                            .setHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                            .build();

                    final var connectionManager = PoolingAsyncClientConnectionManagerBuilder.create()
                            .setTlsStrategy(tlsStrategy)
                            .build();

                    return httpClientBuilder
                            .setDefaultCredentialsProvider(credentialsProvider)
                            .setConnectionManager(connectionManager);
                })
                .build();

        return new OpenSearchClient(transport);
    }
}
