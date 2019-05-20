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

	@Async
	public void findAll(int page, int pageSize) {
		service.findAll(page, pageSize);
	}

	@Async
	public void findBy(String country, String installation, String zone) {
		service.findBy(country, installation, zone);
	}

	@Async
	public void findBy(String country, String installation) {
		service.findBy(country, installation);
	}

	@Async
	public void findById(String id) {
		service.findById(id);
	}
	
	@Async
	public void findVossDevices(String country, String installation) {
		service.findVossDevices(country, installation);
	}
	
	@Async
	public void put(CameraDTO cameraDTO) {
		service.put(cameraDTO);
	}
	
	public long count() {
		return service.count();
	}

}
