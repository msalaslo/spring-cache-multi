package com.msl.cache.springcachemulti.service.impl;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.msl.cache.springcachemulti.api.dto.CameraDTO;
import com.msl.cache.springcachemulti.service.CameraDTOLoaderService;
import com.msl.cache.springcachemulti.service.CameraServiceAsync;

@Service
class CameraDTOLoaderServiceImpl implements CameraDTOLoaderService{

	@Autowired
	CameraServiceAsync cameraService;

	@Async("customAsyncExecutor")
	public void loadToCaches(List<CameraDTO> cameras) {
		for (Iterator<CameraDTO> iterator = cameras.iterator(); iterator.hasNext();) {
			CameraDTO cameraDTO = (CameraDTO) iterator.next();
			//Esto pone en cache las que camaras que van de una en una (serial y country+installation+zone)
			cameraService.put(cameraDTO);
			//Esto pone en cache las que camaras que van agrupadas (voss y country+installation)
			findAndPut(cameraDTO);
		}
	}
	
	public void findAndPut(CameraDTO cameraDTO) {
		String country = cameraDTO.getCountryCode();
		String installation = cameraDTO.getInstallationId();
		String zone = cameraDTO.getZone();
		String serial = cameraDTO.getSerial();
		cameraService.findByCountryAndInstallation(country, installation);
		cameraService.findByCountryAndInstallationAndZone(country, installation, zone);
		cameraService.findById(serial);
		//Comentado porque de momento no hay voss, evitamos hacer consultas a la BBDD que no se cachean porque siempre devuelve null
//		cameraService.findVossDevicesByCountryAndInstallation(country, installation);
	}
}
