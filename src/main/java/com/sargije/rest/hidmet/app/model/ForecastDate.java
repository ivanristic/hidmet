package com.sargije.rest.hidmet.app.model;

import io.aexp.nodes.graphql.annotations.GraphQLIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;


/**
 * The persistent class for the forecast_date database table.
 * 
 */
@Entity
@Table(name="forecast_date")
public class ForecastDate implements Serializable {
	@GraphQLIgnore
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	@GraphQLIgnore
	private Long id;

	@Column(name="date")
	private LocalDate forecastDate;

	//bi-directional many-to-one association to FivedayForecast
	@GraphQLIgnore
	@OneToMany(mappedBy="forecastDate")
	private Set<FivedayForecast> fivedayForecasts;

	//bi-directional many-to-one association to ShortTermForecast
	@GraphQLIgnore
	@OneToMany(mappedBy="forecastDate")
	private Set<ShortTermForecast> shortTermForecasts;


	public ForecastDate() {
	}

	public Long getId() { return this.id; }

	public void setId(Long id) { this.id = id; }

	public LocalDate getForecastDate() { return this.forecastDate; }

	public void setForecastDate(LocalDate date) { this.forecastDate = date; }
	/*
        public Set<FivedayForecast> getFivedayForecasts() { return this.fivedayForecasts; }

	public void setFivedayForecasts(Set<FivedayForecast> fivedayForecasts) {
		this.fivedayForecasts = fivedayForecasts;
	}


        public FivedayForecast addFivedayForecast(FivedayForecast fivedayForecast) {
            getFivedayForecasts().add(fivedayForecast);
            fivedayForecast.setCity(this);

            return fivedayForecast;
        }

        public FivedayForecast removeFivedayForecast(FivedayForecast fivedayForecast) {
            getFivedayForecasts().remove(fivedayForeca);
            fivedayForecast.setCity(null);

            return fivedayForecast;
        }


        public Set<ShortTermForecast> getShortTermForecasts() { return this.shortTermForecasts; }

	public void setShortTermForecasts(Set<ShortTermForecast> shortTermForecasts) {
		this.shortTermForecasts = shortTermForecasts;
	}

	public ShortTermForecast addShortTermForecast(ShortTermForecast shortTermForecast) {
		getShortTermForecasts().add(shortTermForecast);
		shortTermForecast.setCity(this);

		return shortTermForecast;
	}

	public ShortTermForecast removeShortTermForecast(ShortTermForecast shortTermForecast) {
		getShortTermForecasts().remove(shortTermForecast);
		shortTermForecast.setCity(null);

		return shortTermForecast;
	}*/


}