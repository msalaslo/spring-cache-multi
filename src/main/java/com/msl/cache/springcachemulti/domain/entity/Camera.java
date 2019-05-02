package com.msl.cache.springcachemulti.domain.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

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
	
	@Id
	public String country;
	@Id
	public String installation;
	@Id
	public String zone;
	
	public String serial;

}
