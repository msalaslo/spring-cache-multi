package com.msl.cache.springcachemulti.domain.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Camera implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	public String id;
	public String country;
	public String installation;
	public String zone;
	public String serial;

}
