package meli.challenge.demo.model;

import meli.challenge.demo.utils.UUIDHelper;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "Statistics")
public class Statistics implements Serializable {
	private static final long serialVersionUID = -4987350379414141985L;

	@Id
	private String statisticsId;

	private String ip;
	private Double distanceToBuenosAires;

	public Statistics() {
	}

	public Statistics(String ip, Double distanceToBuenosAires) {
		this.statisticsId = UUIDHelper.getUuid();
		this.ip = ip;
		this.distanceToBuenosAires = distanceToBuenosAires;
	}

	public String getStatisticsId() {
		return statisticsId;
	}

	public void setStatisticsId(String statisticsId) {
		this.statisticsId = statisticsId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Double getDistanceToBuenosAires() {
		return distanceToBuenosAires;
	}

	public void setDistanceToBuenosAires(Double distanceToBuenosAires) {
		this.distanceToBuenosAires = distanceToBuenosAires;
	}
}
