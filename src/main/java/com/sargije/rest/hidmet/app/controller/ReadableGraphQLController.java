package com.sargije.rest.hidmet.app.controller;

import com.sargije.rest.hidmet.app.graphql.service.requests.*;
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
				.request(FivedayForecastRequest.class)
				.build();
       System.out.println("Request Entity:" + requestEntity.toString());
		GraphQLResponseEntity<FivedayForecastRequest> responseEntity = graphQLTemplate.query(requestEntity, FivedayForecastRequest.class);
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
                .request(CurrentForecastRequest.class)
                .build();
        System.out.println("Request Entity:" + requestEntity.toString());
        GraphQLResponseEntity<CurrentForecastRequest> responseEntity = graphQLTemplate.query(requestEntity, CurrentForecastRequest.class);
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
                .request(ShortTermForecastRequest.class)
                .build();
//        System.out.println("Request Entity:" + requestEntity.toString());
        GraphQLResponseEntity<ShortTermForecastRequest> responseEntity = graphQLTemplate.query(requestEntity, ShortTermForecastRequest.class);
        model.addAttribute("listShortTermForecast", responseEntity.getResponse().getGetShortTermForecast());
        model.addAttribute("cityList", getGraphQLCities(ForecastType.SHORT_TERM));
        return "shortterm";
    }

    @RequestMapping("/airquality")
    public String getGraphQLAirQuality(Model model) throws MalformedURLException {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Basic aXZhbjoxMjM0NTY=");

        GraphQLRequestEntity airQualityRequestEntity = GraphQLRequestEntity.Builder()
                .url("http://localhost:8080/graphql")
                .headers(headers)
                .request(AirQualityRequest.class)
                .build();

        GraphQLRequestEntity stationRequestEntity = GraphQLRequestEntity.Builder()
                .url("http://localhost:8080/graphql")
                .headers(headers)
                .request(StationRequest.class)
                .build();
//        System.out.println("Request Entity:" + requestEntity.toString());
        GraphQLResponseEntity<AirQualityRequest> airQualityResponseEntity = graphQLTemplate.query(airQualityRequestEntity, AirQualityRequest.class);
        GraphQLResponseEntity<StationRequest> stationResponseEntity = graphQLTemplate.query(stationRequestEntity, StationRequest.class);
        model.addAttribute("listAirQuality", airQualityResponseEntity.getResponse().getGetAirQuality());
        model.addAttribute("stationList", stationResponseEntity.getResponse().getGetStations());
        return "airquality";
    }

    public List<City> getGraphQLCities(ForecastType ft) throws MalformedURLException {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Basic aXZhbjoxMjM0NTY=");
        GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
                .url("http://localhost:8080/graphql")
                .headers(headers)
                .request(CityRequest.class)
                .arguments(new Arguments("getAllCities", new Argument("forecastType", ft)))
                .build();
       // System.out.println("Request Entity:" + requestEntity.toString());
        GraphQLResponseEntity<CityRequest> responseEntity = graphQLTemplate.query(requestEntity, CityRequest.class);

        return responseEntity.getResponse().getGetAllCities();
    }

}
