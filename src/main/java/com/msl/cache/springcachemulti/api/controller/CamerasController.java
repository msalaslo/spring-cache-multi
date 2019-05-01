package com.msl.cache.springcachemulti.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.msl.cache.springcachemulti.domain.entity.Camera;
import com.msl.cache.springcachemulti.service.CameraService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/cameras")
@Slf4j
public class CamerasController {

	@Autowired
	CameraService service;

	@GetMapping(path = "/{id}")
	public Camera get(@PathVariable String id) {
		LOGGER.info("Finding cameras by id..." + id);
		return service.findById(id);
	}

    @PostMapping(consumes = "application/json", produces = "application/json")
	public ResponseEntity<Camera> create(@RequestBody Camera Camera) {
		this.service.create(Camera);
		return new ResponseEntity<>(Camera, HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	void delete(@PathVariable String id) {
		service.deleteById(id);
	}
}