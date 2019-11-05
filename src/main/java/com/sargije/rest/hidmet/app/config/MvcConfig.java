package com.sargije.rest.hidmet.app.config;

import com.coxautodev.graphql.tools.SchemaParserDictionary;
import com.sargije.rest.hidmet.app.model.Authorities;
import com.sargije.rest.hidmet.app.model.AuthoritiesId;
import io.aexp.nodes.graphql.GraphQLTemplate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Bean
	@CacheEvict(allEntries = true, cacheNames = {"currentActiveForecasts"})
	public void currentForecastCacheEvict() {
	}

	@Bean
	@CacheEvict(allEntries = true, cacheNames = {"description"})
	public void descriptionCacheEvict() {
	}

	@Bean
	@CacheEvict(allEntries = true, cacheNames = {"shortTermActiveForecasts"})
	public void shortTearmForecastCacheEvict() {
	}

	@Bean
	@CacheEvict(allEntries = true, cacheNames = {"fivedayActiveForecasts"})
	public void fivedayForecastCacheEvict() {
	}

	@Bean
	public GraphQLTemplate getGraphQLTemplate() {

		GraphQLTemplate graphQLTemplate = new GraphQLTemplate();

		return graphQLTemplate;
	}

	@Bean
	public RestTemplate getRestTemplate() {

		RestTemplate restTemplate = new RestTemplate();

		restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor("ivan", "123456"));
		return restTemplate;
	}

	// workaround for Nested Input Types not Working for Mutations #216 https://github.com/graphql-java-kickstart/graphql-java-tools/issues/216
	@Bean
	public SchemaParserDictionary schemaParserDictionary() {
		return new SchemaParserDictionary().add(Authorities.class).add(AuthoritiesId.class);
	}




}
