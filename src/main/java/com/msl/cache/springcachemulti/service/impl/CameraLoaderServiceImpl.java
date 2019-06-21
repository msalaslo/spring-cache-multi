package com.msl.cache.springcachemulti.service.impl;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.msl.cache.springcachemulti.api.dto.CameraDTO;
import com.msl.cache.springcachemulti.api.dto.PageDTO;
import com.msl.cache.springcachemulti.service.CameraLoaderService;
import com.msl.cache.springcachemulti.service.CameraService;
import com.msl.cache.springcachemulti.service.CameraServiceAsync;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
/* TODO This a simple loader  for the PoC, it should be optimized in order to minimize 
	the load time in cache
*/ 
class CameraLoaderServiceImpl implements CameraLoaderService {

	@Autowired
	CameraService cameraService;
	
	@Autowired
	CameraServiceAsync cameraServiceAsync;

	private int pageSize = 100;

	@Async("customAsyncExecutor")
	public void loadRespoitoryToCache() {
		long numCameras = cameraService.count();
		int numPages = (new Long(numCameras).intValue() / pageSize) + 1;
		for (int page = 0; page < numPages; page++) {
			findAllCamerasPaginatedAndLoad(page, pageSize);
			LOGGER.info("loading page {} with pageSize {} and total num of cameras {} from camera repository", page, pageSize, numCameras);
		}
	}
	
	public void findAllCamerasPaginatedAndLoad(int page, int pageSize) {
		LOGGER.info("loading page {} from camera repository", page);
		PageDTO<CameraDTO> cameraPage = cameraService.findAll(page, pageSize);
		List<CameraDTO> cameras = cameraPage.getContent();
		loadToCachesStream(cameras);
	}
	
	public void loadToCachesStream(List<CameraDTO> cameras) {
		cameras.stream().forEach((c)-> {
			//Esto pone en cache las que camaras que van de una en una (serial y country+installation+zone)
			cameraServiceAsync.put(c);
			//Esto pone en cache las que camaras que van agrupadas (voss y country+installation)
			findGroupedCamerasAndPut(c);
		});
	}
	
	public void findGroupedCamerasAndPut(CameraDTO cameraDTO) {
		cameraServiceAsync.findByCountryAndInstallation(cameraDTO.getCountryCode(), cameraDTO.getInstallationId());
		//Para la PoC: Comentado porque de momento no hay voss, evitamos hacer consultas a la BBDD que no se cachean porque siempre devuelve null
//		cameraServiceAsync.findVossDevicesByCountryAndInstallation(cameraDTO.getCountryCode(), cameraDTO.getInstallationId());
	}
	
}
