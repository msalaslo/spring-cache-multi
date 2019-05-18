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
		PageDTO<CameraDTO> camerasDtoPage = cameraConverter.toPageCameraDto(null, cameraPage);
		return camerasDtoPage;
	}

	@Cacheable(value = "cameras/ByCountryAndInstallationAndZone", cacheManager = "cacheManager", unless = "#result == null")
	public Optional<CameraDTO> findBy(String country, String installation, String zone) {
		LOGGER.debug("findBy country {}, installation {}, zone{}:", country, installation, zone);
		Optional<Camera> camera = repository.findByCountryCodeAndInstallationIdAndZone(country, installation, zone);
		return cameraConverter.toOptionalCameraDto(camera);
	}
	
	@Cacheable(value = "cameras/ByCountryAndInstallation", cacheManager = "cacheManager", unless = "#result == null or #result.size()==0")
	public Iterable<CameraDTO> findBy(String country, String installation) {
		LOGGER.debug("findBy country {}, installation {}", country, installation);
		Iterable<Camera> cameras = repository.findByCountryCodeAndInstallationId(country, installation);
		return cameraConverter.toIterableCameraDto(cameras);
	}

	@Cacheable(key = "#id", value = "cameras/BySerial", cacheManager = "cacheManager", unless = "#result == null")
	public Optional<CameraDTO>  findById(String id) {
		LOGGER.debug("findById:" + id);
		Optional<Camera> camera = repository.findById(id);
		return cameraConverter.toOptionalCameraDto(camera);
	}

	@Cacheable(key = "#camera.id", value = "voss/all", cacheManager = "cacheManager", unless = "#result == null")
	public Iterable<CameraDTO>  findVossDevices() {
		LOGGER.debug("findVossDevices, zone starts with VS");
		Iterable<Camera> cameras = repository.findByZoneStartingWith("VS");
		return cameraConverter.toIterableCameraDto(cameras);
	}
	
	@Cacheable(value = "voss/ByCountryAndInstallation", cacheManager = "cacheManager", unless = "#result == null or #result.size()==0")
	public Iterable<CameraDTO>  findVossDevices(String country, String installation) {
		LOGGER.debug("findVossDevices, zone starts with VS and country is {} and installation is {}", country, installation);
		Iterable<Camera> cameras = repository.findVossDevicesBy(country, installation);
		return cameraConverter.toIterableCameraDto(cameras);
	}
	
	@CachePut(key = "#camera.id", value = "cameras", cacheManager = "cacheManager")
	public CameraDTO create(CameraDTO camera) {
		LOGGER.info("This method does not create the object in the database, only has been cached:" + camera);
		return camera;
	}

	@CachePut(key = "#id", value = "cameras/BySerial", cacheManager = "cacheManager")
	public CameraDTO update(CameraDTO camera, String id) {
		LOGGER.debug("This method does not integrate with the database, update camera {} with id {}:", camera, id);
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
		LOGGER.debug("This method does not integrate with the database, deleteById:" + id);
	}
	
	@CachePut(key = "#camera.id", value = "cameras/BySerial", cacheManager = "cacheManager")
	public Camera createInRepository(Camera camera) {
		LOGGER.debug("create:" + camera);
		return repository.save(camera);
	}

	@CachePut(key = "#id", value = "cameras/BySerial", cacheManager = "cacheManager")
	public CameraDTO updateInRepository(CameraDTO camera, String id) {
		LOGGER.debug("update camera {} with id {}:", camera, id);
		Camera cameraEntity = cameraConverter.toCameraEntity(camera);
		return repository.findById(id).map(newCamera -> {
			camera.setSerial(camera.getSerial());
			Camera newCameraEntity = repository.save(cameraEntity);
			return cameraConverter.toCameraDto(newCameraEntity);
		}).orElseGet(() -> {
			camera.setId(id);
			Camera newCameraEntity = repository.save(cameraEntity);
			return cameraConverter.toCameraDto(newCameraEntity);
		});
	}

	@CacheEvict(key = "#id", value = "cameras/BySerial", cacheManager = "cacheManager")
	public void deleteByIdInRepository(String id) {
		LOGGER.debug("deleteById:" + id);
		repository.deleteById(id);
	}
	
	@Override
	public long count() {
		LOGGER.debug("count");
		return repository.count();
	}
}
