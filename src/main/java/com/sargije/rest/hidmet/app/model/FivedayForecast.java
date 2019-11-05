package com.sargije.rest.hidmet.app.model;

import io.aexp.nodes.graphql.annotations.GraphQLIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;


/**
 * The persistent class for the fiveday_forecast database table.
 * 
 */
@Entity
@Table(name="fiveday_forecast")
public class FivedayForecast implements Serializable {
	@GraphQLIgnore
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	@GraphQLIgnore
	private Long id;

	@NotNull(message = "Max Temperature can't be null")
	@Digits(fraction = 0, integer = 2, message = "Max Temperature must be Long with length 2 or less")
	@Column(name="max_temperature")
	private Integer maxTemperature;

	@Digits(fraction = 0, integer = 2, message = "Min Temperature must be Long with length 2 or less")
	@Column(name="min_temperature")
	private Integer minTemperature;

	@NotNull
	@GraphQLIgnore
	private BigInteger active;

	@Column(name="table_time")
	private LocalDateTime tableTime;

	//bi-directional many-to-one association to City
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="city_id", referencedColumnName="id")
	private City city;

	//bi-directional many-to-one association to Description
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="description_id", referencedColumnName="id")
	private Description description;

	//bi-directional many-to-one association to ForecastDate
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="date_id", referencedColumnName="id")
	private ForecastDate forecastDate;

	public FivedayForecast() {
	}

	//public BigInteger getActive() {return this.active;}

	public void setActive(BigInteger active) {
		this.active = active;
	}

	//public Long getId() { return this.id; }

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getMaxTemperature() {
		return this.maxTemperature;
	}

	public void setMaxTemperature(Integer maxTemperature) {
		this.maxTemperature = maxTemperature;
	}

	public Integer getMinTemperature() {
		return this.minTemperature;
	}

	public void setMinTemperature(Integer minTemperature) {
		this.minTemperature = minTemperature;
	}

	public LocalDateTime getTableTime() {
		return this.tableTime;
	}

	public void setTableTime(LocalDateTime tableTime) {
		this.tableTime = tableTime;
	}

	public City getCity() {
		return this.city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public Description getDescription() {
		return this.description;
	}

	public void setDescription(Description description) {
		this.description = description;
	}

	public ForecastDate getForecastDate() {
		return this.forecastDate;
	}

	public void setForecastDate(ForecastDate forecastDate) {
		this.forecastDate = forecastDate;
	}

}