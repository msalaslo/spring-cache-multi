package com.msl.cache.springcachemulti.api.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.msl.cache.springcachemulti.api.dto.CameraDTO;
import com.msl.cache.springcachemulti.service.CameraService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("v2.0")
public class CamerasController2 {

	@Autowired
	CameraService service;

	
	@GetMapping(path = "/cameras/{id}")
	@ApiOperation(value = "Returns a Camera by serial number (ID)")
	public ResponseEntity<CameraDTO> getByIdV20(@ApiParam("Serial of the Camera to be obtained. Cannot be empty.") @PathVariable(value = "id", required = true) final String id) {
		LOGGER.info("V2 Finding cameras by id (serial): {}", id);
		Optional<CameraDTO> camera = service.findById(id);
		if (camera.isPresent()) {
			return new ResponseEntity<CameraDTO>(camera.get(), HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}


}