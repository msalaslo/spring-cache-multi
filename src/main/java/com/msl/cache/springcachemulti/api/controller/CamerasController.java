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

import com.msl.cache.springcachemulti.common.utils.CameraHandler;
import com.msl.cache.springcachemulti.domain.entity.Camera;
import com.msl.cache.springcachemulti.service.CameraService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CamerasController {

	@Autowired
	CameraService service;

	//TODO: Revisar RestFull API
//	@GetMapping(path = "/cameras", produces = "application/json")
//	public Iterable<Camera> all() {
//		return service.findAll();
//	}

	@GetMapping(path = "/cameras/{id}")
	public ResponseEntity<Camera> getById(@PathVariable String id) {
		LOGGER.info("Finding cameras by id:" + id);
		return service.findById(id).
				map(camera -> ResponseEntity.ok().body(camera)) // 200 OK
				.orElseGet(() -> ResponseEntity.notFound().build()); // 404 Not found
	}
	
	@GetMapping(path = "/cameras", produces = "application/json")
	public ResponseEntity<Iterable<Camera>> get(@RequestParam String country, @RequestParam String installation,
			@RequestParam(value = "zone", required=false) final String zone) {
		if(zone != null) {
			LOGGER.info("Finding cameras by country: {}, installation: {}, zone: {}", country , installation , zone);
			Optional<Camera> camera = service.findBy(country, installation, zone);
			if(camera.isPresent()) {
				List<Camera> cameras = new ArrayList<Camera>();
				cameras.add(camera.get());
				return new ResponseEntity<Iterable<Camera>>(cameras, HttpStatus.OK);
			} else {
				return ResponseEntity.notFound().build();
			}
//					map(camera -> ResponseEntity.ok().body(new Iterable(camera))) // 200 OK
//					.orElseGet(() -> ResponseEntity.notFound().build()); // 404 Not found
		}else {
			LOGGER.info("Finding cameras by country: {}, installation: {}", country , installation);
			return new ResponseEntity<Iterable<Camera>>(service.findBy(country, installation), HttpStatus.OK);
		}
	}
	
	@PostMapping(path = "/cameras", consumes = "application/json", produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public Camera create(@RequestBody Camera camera) {
		//TODO: Revisar el seteo del Id desde un controller
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