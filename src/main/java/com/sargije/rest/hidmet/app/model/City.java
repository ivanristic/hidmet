package com.sargije.rest.hidmet.app.model;

import io.aexp.nodes.graphql.annotations.GraphQLIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;


/**
 * The persistent class for the city database table.
 * 
 */
@Entity
@ApiModel(description = "Class representing a city.")
public class City implements Serializable {

	@GraphQLIgnore
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	@ApiModelProperty(notes = "Unique id of the city", example = "1", required = true, position = 0)
	private Long id;

	@Size(min = 3, max = 24,message = "City must be between 3 and 24 characters")
	@Column(name="city_name")
	@ApiModelProperty(notes = "Unique name of the city", example = "Palić", required = true, position = 1)
	private String cityName;

	//bi-directional many-to-one association to CurrentForecast
	@GraphQLIgnore
	@OneToMany(mappedBy="city")
	private Set<CurrentForecast> currentForecasts;

	//bi-directional many-to-one association to FivedayForecast
	@GraphQLIgnore
	@OneToMany(mappedBy="city")
	private Set<FivedayForecast> fivedayForecasts;

	//bi-directional many-to-one association to ShortTermForecast
	@GraphQLIgnore
	@OneToMany(mappedBy="city")
	private Set<ShortTermForecast> shortTermForecasts;

	//bi-directional many-to-one association to ShortTermForecast
	@GraphQLIgnore
	@OneToMany(mappedBy="city")
	private Set<Station> station;


	public City() {
	}

	public Long getId() { return this.id; }

	public void setId(Long id) { this.id = id; }

	public String getCityName() {
		return this.cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
/*
	public Set<CurrentForecast> getCurrentForecasts() { return this.currentForecasts; }
*/
	public void setCurrentForecasts(Set<CurrentForecast> currentForecasts) {
		this.currentForecasts = currentForecasts;
	}

/*
	public CurrentForecast addCurrentForecast(CurrentForecast currentForecast) {
		getCurrentForecasts().add(currentForecast);
		currentForecast.setCity(this);

		return currentForecast;
	}

	public CurrentForecast removeCurrentForecast(CurrentForecast currentForecast) {
		getCurrentForecasts().remove(currentForecast);
		currentForecast.setCity(null);

		return currentForecast;
	}


	public Set<FivedayForecast> getFivedayForecasts() { return this.fivedayForecasts; }
*/
	public void setFivedayForecasts(Set<FivedayForecast> fivedayForecasts) {
		this.fivedayForecasts = fivedayForecasts;
	}

/*
	public FivedayForecast addFivedayForecast(FivedayForecast fivedayForecast) {
		getFivedayForecasts().add(fivedayForecast);
		fivedayForecast.setCity(this);

		return fivedayForecast;
	}

	public FivedayForecast removeFivedayForecast(FivedayForecast fivedayForecast) {
		getFivedayForecasts().remove(fivedayForecast);
		fivedayForecast.setCity(null);

		return fivedayForecast;
	}


	public Set<ShortTermForecast> getShortTermForecasts() { return this.shortTermForecasts; }
*/
	public void setShortTermForecasts(Set<ShortTermForecast> shortTermForecasts) {
		this.shortTermForecasts = shortTermForecasts;
	}
/*
	public ShortTermForecast addShortTermForecast(ShortTermForecast shortTermForecast) {
		getShortTermForecasts().add(shortTermForecast);
		shortTermForecast.setCity(this);

		return shortTermForecast;
	}

	public ShortTermForecast removeShortTermForecast(ShortTermForecast shortTermForecast) {
		getShortTermForecasts().remove(shortTermForecast);
		shortTermForecast.setCity(null);

		return shortTermForecast;
	}
*/

}