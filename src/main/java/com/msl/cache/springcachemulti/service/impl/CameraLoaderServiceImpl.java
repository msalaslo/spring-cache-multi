package com.msl.cache.springcachemulti.service.impl;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.msl.cache.springcachemulti.api.converter.CameraConverter;
import com.msl.cache.springcachemulti.api.dto.CameraDTO;
import com.msl.cache.springcachemulti.api.dto.PageDTO;
import com.msl.cache.springcachemulti.service.CameraLoaderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CameraLoaderServiceImpl implements CameraLoaderService {

	@Autowired
	CameraServiceImpl cameraService;

	@Autowired
	CameraConverter cameraConverter;

	private int pageSize = 100;

	public void loadRespoitoryToCache() {
		long numCameras = cameraService.count();
		int numPages = new Long(numCameras).intValue() / pageSize;
		for (int page = 0; page < numPages; page++) {
			PageDTO<CameraDTO> cameraPage = cameraService.findAll(page, pageSize);
			List<CameraDTO> cameras = cameraPage.getContent();
			for (Iterator<CameraDTO> iterator = cameras.iterator(); iterator.hasNext();) {
				CameraDTO cameraDTO = (CameraDTO) iterator.next();
				String country = cameraDTO.getCountryCode();
				String installation = cameraDTO.getInstallationId();
				String zone = cameraDTO.getZone();
				String serial = cameraDTO.getSerial();
				cameraService.findBy(country, installation);
				cameraService.findBy(country, installation, zone);
				cameraService.findById(serial);
				cameraService.findVossDevices(country, installation);
			}
		}

	}

}
