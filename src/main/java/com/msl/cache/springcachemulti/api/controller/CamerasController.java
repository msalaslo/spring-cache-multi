package com.msl.cache.springcachemulti.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.msl.cache.springcachemulti.api.dto.CameraDTO;
import com.msl.cache.springcachemulti.api.dto.PageDTO;
import com.msl.cache.springcachemulti.service.CameraService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/v1.0")
public class CamerasController {

	@Autowired
	CameraService service;

	@GetMapping(path = "/cameras/{id}")
	@ApiOperation(value = "Returns a Camera by serial number (ID)")
	public ResponseEntity<CameraDTO> getById(@ApiParam("Serial of the Camera to be obtained. Cannot be empty.") @PathVariable(value = "id", required = true) final String id) {
		LOGGER.info("Finding cameras by id (serial): {}", id);
		Optional<CameraDTO> camera = service.findById(id);
		if (camera.isPresent()) {
			return new ResponseEntity<CameraDTO>(camera.get(), HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping(path = "/cameras", produces = "application/json")
	public ResponseEntity<Iterable<CameraDTO>> get(@RequestParam(required = false) final String country,
			@RequestParam(required = false) final String zone,
			@RequestParam(required = false) final String installation) {
		if (zone != null) {
			LOGGER.info("Finding cameras by country: {}, installation: {}, zone: {}", country, installation, zone);
			Optional<CameraDTO> camera = service.findByCountryAndInstallationAndZone(country, installation, zone);
			if (camera.isPresent()) {
				List<CameraDTO> cameras = new ArrayList<CameraDTO>();
				cameras.add(camera.get());
				return new ResponseEntity<Iterable<CameraDTO>>(cameras, HttpStatus.OK);
			} else {
				return ResponseEntity.notFound().build();
			}
		} else if (country != null && installation != null) {
			LOGGER.info("Finding cameras by country: {}, installation: {}", country, installation);
			return new ResponseEntity<Iterable<CameraDTO>>(service.findByCountryAndInstallation(country, installation), HttpStatus.OK);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping(path = "/cameras/page", produces = "application/json")
	public ResponseEntity<PageDTO<CameraDTO>> findPaginated(@RequestParam(required = true) final Integer page,
			@RequestParam(required = true) final Integer size, @RequestParam(required = false) final String sort) {

		LOGGER.info("Finding all cameras page: {} and size {}", page, size);
		return new ResponseEntity<PageDTO<CameraDTO>>(service.findAll(page, size), HttpStatus.OK);
	}
	
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

	@PostMapping(path = "/cameras", consumes = "application/json", produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public CameraDTO put(@RequestBody CameraDTO camera) {
		service.put(camera);
		return camera;
	}

	@PutMapping(path = "/cameras/{id}", consumes = "application/json", produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public CameraDTO update(@RequestBody CameraDTO newCamera, @PathVariable String id) {
		LOGGER.info("Finding cameras by id..." + id);
		return service.update(newCamera, id);
	}

	@DeleteMapping(path = "/cameras/{id}", produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	void delete(@PathVariable String id) {
		service.deleteById(id);
	}
	
	@DeleteMapping(path = "/cameras", produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	void delete() {
		service.evictAllCacheValues();
	}
}