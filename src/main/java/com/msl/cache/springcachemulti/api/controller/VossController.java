package com.msl.cache.springcachemulti.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.msl.cache.springcachemulti.api.dto.CameraDTO;
import com.msl.cache.springcachemulti.api.dto.PageDTO;
import com.msl.cache.springcachemulti.service.CameraService;
import com.msl.cache.springcachemulti.service.CameraServiceAsync;
import com.msl.cache.springcachemulti.service.CameraServicePubSub;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/v1.0")
public class VossController {

	@Autowired
	CameraService service;
	
	@Autowired
	CameraServiceAsync serviceAsync;
	
	@Autowired
	CameraServicePubSub servicePubSub;
	
	@GetMapping(path = "/vosses/page", produces = "application/json")
	public ResponseEntity<PageDTO<CameraDTO>> findVossesPaginated(@RequestParam(required = true) final Integer page,
			@RequestParam(required = true) final Integer size, @RequestParam(required = false) final String sort) {

		LOGGER.info("Finding all voss page: {} and size {}", page, size);
		return new ResponseEntity<PageDTO<CameraDTO>>(service.findAllVoss(page, size), HttpStatus.OK);
	}

	@GetMapping(path = "/vosses", produces = "application/json")
	public ResponseEntity<Iterable<CameraDTO>> getVosses(@RequestParam String country,
			@RequestParam String installation) {
		LOGGER.info("Finding vosses by country: {}, installation: {}", country, installation);
		return new ResponseEntity<Iterable<CameraDTO>>(service.findVossDevicesByCountryAndInstallation(country, installation), HttpStatus.OK);
	}

}