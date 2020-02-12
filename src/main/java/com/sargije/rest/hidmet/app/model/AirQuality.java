package com.sargije.rest.hidmet.app.model;

import io.aexp.nodes.graphql.annotations.GraphQLIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class AirQuality implements Serializable {

    @GraphQLIgnore
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    @GraphQLIgnore
    private Long id;

    //bi-directional many-to-one association to City
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="station_id", referencedColumnName="id")
    private Station station;

    @Column(name="so2")
    private Float sulfurDioxide;

    @Column(name="no2")
    private Float nitrogenDioxide;

    @Column(name="nox")
    private Float monoNitrogenOxides;

    @Column(name="no")
    private Float nitrogenOxide;

    @Column(name="pm10_24h")
    private Float particleTenMicrometerPerDay;

    @Column(name="pm10_1h")
    private Float particleTenMicrometerPerHour;

    @Column(name="pm25_24h")
    private Float particleTwoAndAHalfMicrometerPerDay;

    @Column(name="co")
    private Float carbonOxide;

    @Column(name="o3")
    private Float ozon;

    @Column(name="benzen")
    private Float benzen;

    @Column(name="dd")
    private Float dd;

    @Column(name="v")
    private Float speed;

    @Column(name="t")
    private Float temperature;

    @Column(name="table_time")
    private LocalDateTime tableTime;

    @GraphQLIgnore
    private boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public Float getSulfurDioxide() {
        return sulfurDioxide;
    }

    public void setSulfurDioxide(Float sulfurDioxide) {
        this.sulfurDioxide = sulfurDioxide;
    }

    public Float getNitrogenDioxide() {
        return nitrogenDioxide;
    }

    public void setNitrogenDioxide(Float nitrogenDioxide) {
        this.nitrogenDioxide = nitrogenDioxide;
    }

    public Float getMonoNitrogenOxides() {
        return monoNitrogenOxides;
    }

    public void setMonoNitrogenOxides(Float monoNitrogenOxides) {
        this.monoNitrogenOxides = monoNitrogenOxides;
    }

    public Float getNitrogenOxide() {
        return nitrogenOxide;
    }

    public void setNitrogenOxide(Float nitrogenOxide) {
        this.nitrogenOxide = nitrogenOxide;
    }

    public Float getParticleTenMicrometerPerDay() {
        return particleTenMicrometerPerDay;
    }

    public void setParticleTenMicrometerPerDay(Float particleTenMicrometerPerDay) {
        this.particleTenMicrometerPerDay = particleTenMicrometerPerDay;
    }

    public Float getParticleTenMicrometerPerHour() {
        return particleTenMicrometerPerHour;
    }

    public void setParticleTenMicrometerPerHour(Float particleTenMicrometerPerHour) {
        this.particleTenMicrometerPerHour = particleTenMicrometerPerHour;
    }

    public Float getParticleTwoAndAHalfMicrometerPerDay() {
        return particleTwoAndAHalfMicrometerPerDay;
    }

    public void setParticleTwoAndAHalfMicrometerPerDay(Float particleTwoAndAHalfMicrometerPerDay) {
        this.particleTwoAndAHalfMicrometerPerDay = particleTwoAndAHalfMicrometerPerDay;
    }

    public Float getCarbonOxide() {
        return carbonOxide;
    }

    public void setCarbonOxide(Float carbonOxide) {
        this.carbonOxide = carbonOxide;
    }

    public Float getOzon() {
        return ozon;
    }

    public void setOzon(Float ozon) {
        this.ozon = ozon;
    }

    public Float getBenzen() {
        return benzen;
    }

    public void setBenzen(Float benzen) {
        this.benzen = benzen;
    }

    public Float getDd() {
        return dd;
    }

    public void setDd(Float dd) {
        this.dd = dd;
    }

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public LocalDateTime getTableTime() {
        return tableTime;
    }

    public void setTableTime(LocalDateTime tableTime) {
        this.tableTime = tableTime;
    }


    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
