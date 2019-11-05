package com.sargije.rest.hidmet.app.model;

import io.aexp.nodes.graphql.annotations.GraphQLIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the description database table.
 * 
 */
@Entity
public class Description implements Serializable {
	@GraphQLIgnore
	private static final long serialVersionUID = 1L;

	private String description;

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	@GraphQLIgnore
	private Long id;

	@Column(name="image_location")
	private String imageLocation;
/*

	//bi-directional many-to-one association to CurrentForecast
	@GraphQLIgnore
	@OneToMany(mappedBy="description")
	private Set<CurrentForecast> currentForecasts;

	//bi-directional many-to-one association to FivedayForecast
	@GraphQLIgnore
	@OneToMany(mappedBy="description")
	private Set<FivedayForecast> fivedayForecasts;

	//bi-directional many-to-one association to ShortTermForecast
	@GraphQLIgnore
	@OneToMany(mappedBy="description")
	private Set<ShortTermForecast> shortTermForecasts;
*/


	public Description() {
	}
	// public BigInteger getId() { return this.Id; }

	public void setId(Long id) { this.id = id; }

	public String getDescription() { return this.description;}

	public void setDescription(String description) { this.description = description; }

	public String getImageLocation() {
		return this.imageLocation;
	}

	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
	}
/*
	public Set<CurrentForecast> getCurrentForecasts() { return this.currentForecasts; }

	public void setCurrentForecasts(Set<CurrentForecast> currentForecasts) {
		this.currentForecasts = currentForecasts;
	}


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

	public void setFivedayForecasts(Set<FivedayForecast> fivedayForecasts) {
		this.fivedayForecasts = fivedayForecasts;
	}


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
	}
*/

}