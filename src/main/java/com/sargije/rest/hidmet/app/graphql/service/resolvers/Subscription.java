package com.sargije.rest.hidmet.app.graphql.service.resolvers;

import com.sargije.rest.hidmet.app.graphql.service.publishers.AirQualityPublisher;
import com.sargije.rest.hidmet.app.graphql.service.publishers.CurrentForecastPublisher;
import com.sargije.rest.hidmet.app.graphql.service.publishers.FivedayForecastPublisher;
import com.sargije.rest.hidmet.app.graphql.service.publishers.ShortTermForecastPublisher;
import com.sargije.rest.hidmet.app.model.AirQuality;
import com.sargije.rest.hidmet.app.model.CurrentForecast;
import com.sargije.rest.hidmet.app.model.FivedayForecast;
import com.sargije.rest.hidmet.app.model.ShortTermForecast;
import graphql.kickstart.tools.GraphQLSubscriptionResolver;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Subscription implements GraphQLSubscriptionResolver {


    @Autowired
    private CurrentForecastPublisher currentForecastPublisher;

    @Autowired
    private FivedayForecastPublisher fivedayForecastPublisher;

    @Autowired
    private ShortTermForecastPublisher shortTermForecastPublisher;

    @Autowired
    private AirQualityPublisher airQualityPublisher;
    // name must be same as graphqls

    Publisher<List<CurrentForecast>> subscribeToCurrentForecast(){
        return currentForecastPublisher.getPublisher();
    }

    Publisher<List<FivedayForecast>> subscribeToFivedayForecast(){
        return fivedayForecastPublisher.getPublisher();
    }

    Publisher<List<ShortTermForecast>> subscribeToShortTermForecast(){ return shortTermForecastPublisher.getPublisher(); }

    Publisher<List<AirQuality>> subscribeToAirQuality(){ return airQualityPublisher.getPublisher(); }
}
