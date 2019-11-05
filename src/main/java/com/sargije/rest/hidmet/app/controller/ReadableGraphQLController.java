package com.sargije.rest.hidmet.app.controller;

import com.sargije.rest.hidmet.app.graphql.service.Request;
import com.sargije.rest.hidmet.app.model.City;
import com.sargije.rest.hidmet.app.model.ForecastType;
import io.aexp.nodes.graphql.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/gql")
public class ReadableGraphQLController {

    @Autowired
    public GraphQLTemplate graphQLTemplate;


    @RequestMapping("/fiveday")
    public String getGraphQLFiveday(Model model) throws MalformedURLException {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Basic aXZhbjoxMjM0NTY=");

		GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
				.url("http://localhost:8080/graphql")
                .headers(headers)
				.request(Request.class)
				.build();
       System.out.println("Request Entity:" + requestEntity.toString());
		GraphQLResponseEntity<Request> responseEntity = graphQLTemplate.query(requestEntity, Request.class);
        model.addAttribute("listFiveDayForecast", responseEntity.getResponse().getGetFivedayForecast());
        model.addAttribute("cityList", getGraphQLCities(ForecastType.FIVEDAY));
        return "fiveday";
    }

    @RequestMapping("/current")
    public String getGraphQLCurrent(Model model) throws MalformedURLException {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Basic aXZhbjoxMjM0NTY=");

        GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
                .url("http://localhost:8080/graphql")
                .headers(headers)
                .request(Request.class)
                .build();
        System.out.println("Request Entity:" + requestEntity.toString());
        GraphQLResponseEntity<Request> responseEntity = graphQLTemplate.query(requestEntity, Request.class);
        model.addAttribute("listCurrentForecast", responseEntity.getResponse().getGetCurrentForecast());
  //      model.addAttribute("cityList", getGraphQLCities(ForecastType.CURRENT));
        return "current";
    }

    @RequestMapping("/shortterm")
    public String getGraphQLShortTerm(Model model) throws MalformedURLException {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Basic aXZhbjoxMjM0NTY=");

        GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
                .url("http://localhost:8080/graphql")
                .headers(headers)
                .request(Request.class)
                .build();
//        System.out.println("Request Entity:" + requestEntity.toString());
        GraphQLResponseEntity<Request> responseEntity = graphQLTemplate.query(requestEntity, Request.class);
        model.addAttribute("listShortTermForecast", responseEntity.getResponse().getGetShortTermForecast());
        model.addAttribute("cityList", getGraphQLCities(ForecastType.SHORT_TERM));
        return "shortterm";
    }


    public List<City> getGraphQLCities(ForecastType ft) throws MalformedURLException {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Basic aXZhbjoxMjM0NTY=");
        GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
                .url("http://localhost:8080/graphql")
                .headers(headers)
                .request(Request.class)
                .arguments(new Arguments("getAllCities", new Argument("forecastType", ft)))
                .build();
        System.out.println("Request Entity:" + requestEntity.toString());
        GraphQLResponseEntity<Request> responseEntity = graphQLTemplate.query(requestEntity, Request.class);

        return responseEntity.getResponse().getGetAllCities();
    }

}
