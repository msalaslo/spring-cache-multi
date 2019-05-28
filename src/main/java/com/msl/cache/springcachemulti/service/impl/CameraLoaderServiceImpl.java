package com.msl.cache.springcachemulti.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.msl.cache.springcachemulti.service.CameraLoaderInnerService;
import com.msl.cache.springcachemulti.service.CameraLoaderService;
import com.msl.cache.springcachemulti.service.CameraService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
class CameraLoaderServiceImpl implements CameraLoaderService {

	@Autowired
	CameraService cameraService;
	
	@Autowired
	CameraLoaderInnerService innerService;

	private int pageSize = 100;

	@Async("customAsyncExecutor")
	public void loadRespoitoryToCache() {
		long numCameras = cameraService.count();
		int numPages = (new Long(numCameras).intValue() / pageSize) + 1;
		for (int page = 0; page < numPages; page++) {
			innerService.findAllCamerasPaginatedAndLoad(page, pageSize);
			LOGGER.info("loading page {} with pageSize {} and total num of cameras {} from camera repository", page, pageSize, numCameras);
		}
	}
	
}
