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

    @Column(name="pm10")
    private Float particleTenMicrometer;

    @Column(name="pm2_5")
    private Float particleTwoAndAHalfMicrometer;

    @Column(name="co")
    private Float carbonMonoxide;

    @Column(name="o3")
    private Float ozon;

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

    public Float getParticleTenMicrometer() {
        return particleTenMicrometer;
    }

    public void setParticleTenMicrometer(Float particleTenMicrometer) {
        this.particleTenMicrometer = particleTenMicrometer;
    }

    public Float getParticleTwoAndAHalfMicrometer() {
        return particleTwoAndAHalfMicrometer;
    }

    public void setParticleTwoAndAHalfMicrometer(Float particleTwoAndAHalfMicrometer) {
        this.particleTwoAndAHalfMicrometer = particleTwoAndAHalfMicrometer;
    }

    public Float getCarbonMonoxide() {
        return carbonMonoxide;
    }

    public void setCarbonMonoxide(Float carbonMonoxide) {
        this.carbonMonoxide = carbonMonoxide;
    }

    public Float getOzon() {
        return ozon;
    }

    public void setOzon(Float ozon) {
        this.ozon = ozon;
    }

    public LocalDateTime getTableTime() {
        return tableTime;
    }

    public void setTableTime(LocalDateTime tableTime) {
        this.tableTime = tableTime;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
