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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.msl.cache.springcachemulti.api.dto.CameraDTO;
import com.msl.cache.springcachemulti.api.dto.PageDTO;
import com.msl.cache.springcachemulti.common.utils.CameraHandler;
import com.msl.cache.springcachemulti.domain.entity.Camera;
import com.msl.cache.springcachemulti.service.CameraService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CamerasController {

	@Autowired
	CameraService service;

	@GetMapping(path = "/cameras/{id}")
	public ResponseEntity<Iterable<Camera>> getById(@PathVariable(value = "id", required = true) final String id) {
		LOGGER.info("Finding cameras by id (serial): {}", id);
		Optional<Camera> camera = service.findById(id);
		if (camera.isPresent()) {
			List<Camera> cameras = new ArrayList<Camera>();
			cameras.add(camera.get());
			return new ResponseEntity<Iterable<Camera>>(cameras, HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping(path = "/cameras", produces = "application/json")
	public ResponseEntity<Iterable<Camera>> get(@RequestParam(required = false) final String country,
			@RequestParam(required = false) final String zone,
			@RequestParam(required = false) final String installation) {
		if (zone != null) {
			LOGGER.info("Finding cameras by country: {}, installation: {}, zone: {}", country, installation, zone);
			Optional<Camera> camera = service.findBy(country, installation, zone);
			if (camera.isPresent()) {
				List<Camera> cameras = new ArrayList<Camera>();
				cameras.add(camera.get());
				return new ResponseEntity<Iterable<Camera>>(cameras, HttpStatus.OK);
			} else {
				return ResponseEntity.notFound().build();
			}
		} else if (country != null && installation != null) {
			LOGGER.info("Finding cameras by country: {}, installation: {}", country, installation);
			return new ResponseEntity<Iterable<Camera>>(service.findBy(country, installation), HttpStatus.OK);
//		}
//		if (page != null && size != null) {
//			LOGGER.info("Finding all cameras page: {} and size {}", page, size);			
//			return new ResponseEntity<Iterable<Camera>>(service.findAll(page, size), HttpStatus.OK);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping(path = "/cameras/page", produces = "application/json")
	public ResponseEntity<PageDTO<CameraDTO>> findPaginated(
			@RequestParam(required = true) final Integer page,
			@RequestParam(required = true) final Integer size,
			@RequestParam(required = false) final String sort) {

			LOGGER.info("Finding all cameras page: {} and size {}", page, size);			
			return new ResponseEntity<PageDTO<CameraDTO>>(service.findAll(page, size), HttpStatus.OK);
	}

	@GetMapping(path = "/vosses", produces = "application/json")
	public ResponseEntity<Iterable<Camera>> getVosses(@RequestParam String country, @RequestParam String installation) {
		LOGGER.info("Finding vosses by country: {}, installation: {}", country, installation);
		return new ResponseEntity<Iterable<Camera>>(service.findVossDevices(country, installation), HttpStatus.OK);
	}

	@PostMapping(path = "/cameras", consumes = "application/json", produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public Camera create(@RequestBody Camera camera) {
		// TODO: Revisar el seteo del Id desde un controller
		CameraHandler.generateAndSetId(camera);
		service.create(camera);
		return camera;
	}

	@PutMapping(path = "/cameras/{id}", consumes = "application/json", produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public Camera update(@RequestBody Camera newCamera, @PathVariable String id) {
		LOGGER.info("Finding cameras by id..." + id);
		return service.update(newCamera, id);
	}

	@DeleteMapping(path = "/cameras/{id}", produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	void delete(@PathVariable String id) {
		service.deleteById(id);
	}
}