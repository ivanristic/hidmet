package com.sargije.rest.hidmet.app.model;

import io.aexp.nodes.graphql.annotations.GraphQLIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;


/**
 * The persistent class for the current_forecast database table.
 * 
 */
@Entity
@Table(name="current_forecast")
public class CurrentForecast implements Serializable {

	@GraphQLIgnore
	private static final long serialVersionUID = 1L;

	@NotNull
	@GraphQLIgnore
	private BigInteger active;

	@NotNull(message = "Feels like can't be null")
	@Digits(fraction = 0, integer = 2)
	@Column(name="feels_like")
	private Integer feelsLike;

	@NotNull(message = "Humidity can't be null")
	@Digits(fraction = 0, integer = 3, message = "Humidity must be Long with length 3 or less")
	private Integer humidity;

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	@GraphQLIgnore
	private Long id;

	@NotNull(message = "Pressure can't be null")
	@Digits(fraction = 2, integer = 4, message = "Pressure must be Decimal with length 4 or less with 2 decimal points")
	private Float  pressure;

	@Column(name="table_time")
	private LocalDateTime tableTime;

	@NotNull(message = "Temperature can't be null")
	@Digits(fraction = 0, integer = 2, message = "Temperature must be Long with length 2 or less")
	private Integer temperature;

	@Size(min = 1, max = 3, message = "Wind direction must be between 1 and 3 characters")
	@Column(name="wind_direction")
	private String windDirection;

	@Size(min = 1, max = 5, message = "Wind speed must be between 1 and 5 characters")
	@Column(name="wind_speed")
	private String windSpeed;

	//bi-directional many-to-one association to City
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="city_id", referencedColumnName="id")
	private City city;

	//bi-directional many-to-one association to Description
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="description_id", referencedColumnName="id")
	private Description description;

	public CurrentForecast() {
	}

	//public BigInteger getActive() {return this.active;}

	public void setActive(BigInteger active) {
		this.active = active;
	}

	public Integer getFeelsLike() {
		return this.feelsLike;
	}

	public void setFeelsLike(Integer feelsLike) {
		this.feelsLike = feelsLike;
	}

	public Integer getHumidity() {
		return this.humidity;
	}

	public void setHumidity(Integer humidity) {
		this.humidity = humidity;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {this.id = id; }

	public Float getPressure() {
		return this.pressure;
	}

	public void setPressure(Float pressure) {
		this.pressure = pressure;
	}

	public LocalDateTime getTableTime() {
		return this.tableTime;
	}

	public void setTableTime(LocalDateTime tableTime) {
		this.tableTime = tableTime;
	}

	public Integer getTemperature() {
		return this.temperature;
	}

	public void setTemperature(Integer temperature) {
		this.temperature = temperature;
	}

	public String getWindDirection() {
		return this.windDirection;
	}

	public void setWindDirection(String windDirection) {
		this.windDirection = windDirection;
	}

	public String getWindSpeed() {
		return this.windSpeed;
	}

	public void setWindSpeed(String windSpeed) {
		this.windSpeed = windSpeed;
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

}