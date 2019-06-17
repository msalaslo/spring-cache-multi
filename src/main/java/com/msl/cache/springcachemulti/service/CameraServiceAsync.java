package com.msl.cache.springcachemulti.service;

import com.msl.cache.springcachemulti.api.dto.CameraDTO;

public interface CameraServiceAsync {
	
	public void put(CameraDTO camera);
	public void findById(String id);
	public void findByCountryAndInstallation(String country, String installation);
	public void findByCountryAndInstallationAndZone(String country, String installation, String zone);	
	public void findAll(int page, int pageSize);
	public void findVossDevicesByCountryAndInstallation(String country, String installation);
	public long count();
}
