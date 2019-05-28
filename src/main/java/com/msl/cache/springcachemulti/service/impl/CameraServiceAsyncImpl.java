package com.msl.cache.springcachemulti.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.msl.cache.springcachemulti.api.dto.CameraDTO;
import com.msl.cache.springcachemulti.service.CameraService;
import com.msl.cache.springcachemulti.service.CameraServiceAsync;

@Service
public class CameraServiceAsyncImpl implements CameraServiceAsync {

	@Autowired
	CameraService service;

	@Async("customAsyncExecutor")
	public void findAll(int page, int pageSize) {
		service.findAll(page, pageSize);
	}

	@Async("customAsyncExecutor")
	public void findByCountryAndInstallationAndZone(String country, String installation, String zone) {
		service.findByCountryAndInstallationAndZone(country, installation, zone);
	}

	@Async("customAsyncExecutor")
	public void findByCountryAndInstallation(String country, String installation) {
		service.findByCountryAndInstallation(country, installation);
	}

	@Async("customAsyncExecutor")
	public void findById(String id) {
		service.findById(id);
	}
	
	@Async("customAsyncExecutor")
	public void findVossDevicesByCountryAndInstallation(String country, String installation) {
		service.findVossDevicesByCountryAndInstallation(country, installation);
	}
	
	@Async("customAsyncExecutor")
	public void put(CameraDTO cameraDTO) {
		service.put(cameraDTO);
	}
	
	public long count() {
		return service.count();
	}

}
