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
		loadToCaches(cameras);
	}
	
	public void loadToCaches(List<CameraDTO> cameras) {
		for (Iterator<CameraDTO> iterator = cameras.iterator(); iterator.hasNext();) {
			CameraDTO cameraDTO = (CameraDTO) iterator.next();
			//Esto pone en cache las que camaras que van de una en una (serial y country+installation+zone)
			cameraServiceAsync.put(cameraDTO);
			//Esto pone en cache las que camaras que van agrupadas (voss y country+installation)
			findAndPut(cameraDTO);
		}
	}
	
	public void findAndPut(CameraDTO cameraDTO) {
		String country = cameraDTO.getCountryCode();
		String installation = cameraDTO.getInstallationId();
		String zone = cameraDTO.getZone();
		String serial = cameraDTO.getSerial();
		cameraServiceAsync.findByCountryAndInstallation(country, installation);
		cameraServiceAsync.findByCountryAndInstallationAndZone(country, installation, zone);
		cameraServiceAsync.findById(serial);
		//Para la PoC: Comentado porque de momento no hay voss, evitamos hacer consultas a la BBDD que no se cachean porque siempre devuelve null
//		cameraServiceAsync.findVossDevicesByCountryAndInstallation(country, installation);
	}
	
}
