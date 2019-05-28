package com.msl.cache.springcachemulti.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.msl.cache.springcachemulti.api.dto.CameraDTO;
import com.msl.cache.springcachemulti.api.dto.PageDTO;
import com.msl.cache.springcachemulti.service.CameraDTOLoaderService;
import com.msl.cache.springcachemulti.service.CameraLoaderInnerService;
import com.msl.cache.springcachemulti.service.CameraService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
class CameraLoaderInnerServiceImpl implements CameraLoaderInnerService{

	@Autowired
	CameraService cameraService;

	@Autowired
	CameraDTOLoaderService cameraDTOservice;

	@Async("customAsyncExecutor")
	public void findAllCamerasPaginatedAndLoad(int page, int pageSize) {
		LOGGER.info("loading page {} from camera repository", page);
		PageDTO<CameraDTO> cameraPage = cameraService.findAll(page, pageSize);
		List<CameraDTO> cameras = cameraPage.getContent();
		cameraDTOservice.loadToCaches(cameras);
	}

}
