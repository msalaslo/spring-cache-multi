package com.msl.cache.springcachemulti.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.msl.cache.springcachemulti.service.CameraLoaderService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CamerasLoaderController {

	@Autowired
	CameraLoaderService service;

	@GetMapping(path = "/loader-cameras/load")
	public void loadRepositoryToCache() {
		LOGGER.info("Loading cameras....");
		service.loadRespoitoryToCache();
		LOGGER.info("Repository loaded in cache....");
	}

}