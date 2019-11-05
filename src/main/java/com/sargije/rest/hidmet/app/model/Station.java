package com.sargije.rest.hidmet.app.model;

import io.aexp.nodes.graphql.annotations.GraphQLIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Station implements Serializable {

    @GraphQLIgnore
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    //bi-directional many-to-one association to City
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="city_id", referencedColumnName="id")
    private City city;

    @Column(name="station_name")
    private String stationName;

    private String network;

    @Column(name="eoi_code")
    private String eoiCode;

    private String classification;

    private String zone;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getEoiCode() {
        return eoiCode;
    }

    public void setEoiCode(String eoiCode) {
        this.eoiCode = eoiCode;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }
}
