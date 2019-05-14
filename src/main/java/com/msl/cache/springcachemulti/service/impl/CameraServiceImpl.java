package com.msl.cache.springcachemulti.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.msl.cache.springcachemulti.api.converter.CameraConverter;
import com.msl.cache.springcachemulti.api.dto.CameraDTO;
import com.msl.cache.springcachemulti.api.dto.PageDTO;
import com.msl.cache.springcachemulti.domain.entity.Camera;
import com.msl.cache.springcachemulti.domain.repository.CameraRepository;
import com.msl.cache.springcachemulti.service.CameraService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CameraServiceImpl implements CameraService {

	@Autowired
	CameraRepository repository;
	
	@Autowired
	CameraConverter cameraConverter;

	@Cacheable(value = "cameras/all", cacheManager = "cacheManager", unless = "#result == null")
	public PageDTO<CameraDTO> findAll(int page, int pageSize) {
		LOGGER.info("findAll");
		Pageable pageable = PageRequest.of(page, pageSize);
		Page<Camera> cameraPage = repository.findAll(pageable);
		PageDTO<CameraDTO> camerasDtoPage = cameraConverter.mapCameraPageToDTO(null, cameraPage);
		return camerasDtoPage;
	}

	@Cacheable(value = "cameras/ByCountryAndInstallationAndZone", cacheManager = "cacheManager", unless = "#result == null")
	public Optional<Camera> findBy(String country, String installation, String zone) {
		LOGGER.debug("findBy country {}, installation {}, zone{}:", country, installation, zone);
		return repository.findByCountryCodeAndInstallationIdAndZone(country, installation, zone);
	}
	
	@Cacheable(value = "cameras/ByCountryAndInstallation", cacheManager = "cacheManager", unless = "#result == null or #result.size()==0")
	public Iterable<Camera> findBy(String country, String installation) {
		LOGGER.debug("findBy country {}, installation {}", country, installation);
		return repository.findByCountryCodeAndInstallationId(country, installation);
	}

	@Cacheable(key = "#id", value = "cameras/BySerial", cacheManager = "cacheManager", unless = "#result == null")
	public Optional<Camera>  findById(String id) {
		LOGGER.debug("findById:" + id);
		return repository.findById(id);
	}

	@Cacheable(key = "#camera.id", value = "voss/all", cacheManager = "cacheManager", unless = "#result == null")
	public Iterable<Camera>  findVossDevices() {
		LOGGER.debug("findVossDevices, zone starts with VS");
		return repository.findByZoneStartingWith("VS");
	}
	
	@Cacheable(value = "voss/ByCountryAndInstallation", cacheManager = "cacheManager", unless = "#result == null or #result.size()==0")
	public Iterable<Camera>  findVossDevices(String country, String installation) {
		LOGGER.debug("findVossDevices, zone starts with VS and country is {} and installation is {}", country, installation);
		return repository.findVossDevicesBy(country, installation);
	}
	
	@CachePut(key = "#camera.id", value = "cameras", cacheManager = "cacheManager")
	public Camera create(Camera camera) {
		LOGGER.debug("create:" + camera);
		return camera;
	}

	@CachePut(key = "#id", value = "cameras/BySerial", cacheManager = "cacheManager")
	public Camera update(Camera camera, String id) {
		LOGGER.debug("update camera {} with id {}:", camera, id);
		return repository.findById(id).map(newCamera -> {
			camera.setSerial(camera.getSerial());
			return camera;
		}).orElseGet(() -> {
			camera.setId(id);
			return camera;
		});
	}

	@CacheEvict(key = "#id", value = "cameras/BySerial", cacheManager = "cacheManager")
	public void deleteById(String id) {
		LOGGER.debug("deleteById:" + id);
	}
	
	@CachePut(key = "#camera.id", value = "cameras/BySerial", cacheManager = "cacheManager")
	public Camera createInRepository(Camera camera) {
		LOGGER.debug("create:" + camera);
		return repository.save(camera);
	}

	@CachePut(key = "#id", value = "cameras/BySerial", cacheManager = "cacheManager")
	public Camera updateInRepository(Camera camera, String id) {
		LOGGER.debug("update camera {} with id {}:", camera, id);
		
		return repository.findById(id).map(newCamera -> {
			camera.setSerial(camera.getSerial());
			return repository.save(camera);
		}).orElseGet(() -> {
			camera.setId(id);
			return repository.save(camera);
		});
	}

	@CacheEvict(key = "#id", value = "cameras/BySerial", cacheManager = "cacheManager")
	public void deleteByIdInRepository(String id) {
		LOGGER.debug("deleteById:" + id);
		repository.deleteById(id);
	}
}
