package meli.challenge.demo.model;

import java.io.Serializable;


public class StatisticsDTO implements Serializable {
    private static final long serialVersionUID = -4987150379494141985L;


    private Double average;
    private Double maxDistanceToBuenosAires;
    private Double minDistanceToBuenosAires;
    private Integer quantity;

    public StatisticsDTO() {
    }

    public StatisticsDTO(Double average, Double maxDistanceToBuenosAires, Double minDistanceToBuenosAires, Integer quantity) {
        this.average = average;
        this.maxDistanceToBuenosAires = maxDistanceToBuenosAires;
        this.minDistanceToBuenosAires = minDistanceToBuenosAires;
        this.quantity = quantity;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public Double getMaxDistanceToBuenosAires() {
        return maxDistanceToBuenosAires;
    }

    public void setMaxDistanceToBuenosAires(Double maxDistanceToBuenosAires) {
        this.maxDistanceToBuenosAires = maxDistanceToBuenosAires;
    }

    public Double getMinDistanceToBuenosAires() {
        return minDistanceToBuenosAires;
    }

    public void setMinDistanceToBuenosAires(Double minDistanceToBuenosAires) {
        this.minDistanceToBuenosAires = minDistanceToBuenosAires;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
