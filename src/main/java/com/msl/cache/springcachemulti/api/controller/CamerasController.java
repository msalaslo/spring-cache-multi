package com.msl.cache.springcachemulti.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import com.msl.cache.springcachemulti.service.CameraServiceAsync;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Validated
@Tag(name = "Cameras CRUD")
@RequestMapping("/v1.0")
public class CamerasController {

	@Autowired
	CameraService service;
	
	@Autowired
	CameraServiceAsync serviceAsync;
	
	@GetMapping(path = "/cameras/{serial}")
	@Operation(description = "Returns a Camera by serial number (ID)", responses = {
			@ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = CameraDTO.class))), responseCode = "200") })
	public ResponseEntity<CameraDTO> getById(@PathVariable(name="serial", required = true) final String serial) {
		LOGGER.info("Finding cameras by serial (id): {}", serial);
		Optional<CameraDTO> camera = service.findBySerial(serial);
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
	
	@GetMapping(path = "/cameras/page/nocache", produces = "application/json")
	public ResponseEntity<PageDTO<CameraDTO>> findAllNoCache(@RequestParam(required = true) final Integer page,
			@RequestParam(required = true) final Integer size, @RequestParam(required = false) final String sort) {
		LOGGER.info("Finding all cameras page: {} and size {}", page, size);
		PageDTO<CameraDTO> cameras = service.findAllNoCache(page, size);
		return new ResponseEntity<PageDTO<CameraDTO>>(cameras, HttpStatus.OK);
	}
	

	@GetMapping(path = "/cameras/page", produces = "application/json")
	public ResponseEntity<List<CameraDTO>> findAllCachedKeys(@RequestParam(required = true) final Integer page,
			@RequestParam(required = true) final Integer size, @RequestParam(required = false) final String sort) {
		LOGGER.info("Finding all cameras page: {} and size {}", page, size);
		List<String> keys = service.findAllKeys(page, size);
		List<CameraDTO> cameras = keys.stream().map(service::findBySerial).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
		return new ResponseEntity<List<CameraDTO>>(cameras, HttpStatus.OK);
	}

	@GetMapping(path = "/cameras/page/allcontent", produces = "application/json")
	public ResponseEntity<PageDTO<CameraDTO>> findAllCachedContent(@RequestParam(required = true) final Integer page,
			@RequestParam(required = true) final Integer size, @RequestParam(required = false) final String sort) {
		LOGGER.info("Finding all cameras page: {} and size {}", page, size);
		return new ResponseEntity<PageDTO<CameraDTO>>(service.findAll(page, size), HttpStatus.OK);
	}
	
	@PostMapping(path = "/cameras", consumes = "application/json", produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public CameraDTO put(@RequestBody CameraDTO camera) {
		service.put(camera);
		return camera;
	}

	@PutMapping(path = "/cameras/{serial}", consumes = "application/json", produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public CameraDTO update(@RequestBody CameraDTO newCamera, @PathVariable String serial) {
		LOGGER.info("Finding cameras by serial..." + serial);
		return service.update(newCamera, serial);
	}

	@DeleteMapping(path = "/cameras/{serial}", produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	void delete(@PathVariable String serial) {
		service.deleteById(serial);
	}
	
	@DeleteMapping(path = "/cameras/", produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	void delete() {
		service.evictAllCacheValues();
	}
}