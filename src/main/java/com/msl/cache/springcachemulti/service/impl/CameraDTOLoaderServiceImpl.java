package com.msl.cache.springcachemulti.service.impl;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.msl.cache.springcachemulti.api.dto.CameraDTO;
import com.msl.cache.springcachemulti.service.CameraDTOLoaderService;
import com.msl.cache.springcachemulti.service.CameraService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
class CameraDTOLoaderServiceImpl implements CameraDTOLoaderService{

	@Autowired
	CameraService cameraService;

	@Async
	public void loadToCaches(List<CameraDTO> cameras) {
		for (Iterator<CameraDTO> iterator = cameras.iterator(); iterator.hasNext();) {
			CameraDTO cameraDTO = (CameraDTO) iterator.next();
			loadToCaches(cameraDTO);
			LOGGER.info("loading List of cameras");
		}
	}
	
	@Async
	public void loadToCaches(CameraDTO cameraDTO) {
		String country = cameraDTO.getCountryCode();
		String installation = cameraDTO.getInstallationId();
		String zone = cameraDTO.getZone();
		String serial = cameraDTO.getSerial();
		cameraService.findBy(country, installation);
		cameraService.findBy(country, installation, zone);
		cameraService.findById(serial);
//		cameraService.findVossDevices(country, installation);
	}
}
