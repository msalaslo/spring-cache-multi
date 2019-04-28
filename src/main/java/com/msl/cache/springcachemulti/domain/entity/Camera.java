package com.msl.cache.springcachemulti.domain.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Camera implements Serializable{

	private static final long serialVersionUID = 1L;
	public String id;
	public String country;
	public String installation;
	public String zone;
	public String serial;

	public Camera(String country, String installation, String zone, String serial) {
		this.id = country + installation + zone;
		this.country = country;
		this.installation = installation;
		this.zone = zone;
		this.serial = serial;
	}
}
