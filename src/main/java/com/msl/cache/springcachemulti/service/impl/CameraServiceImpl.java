package com.msl.cache.springcachemulti.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.msl.cache.springcachemulti.domain.entity.Camera;
import com.msl.cache.springcachemulti.domain.repository.CameraRepository;
import com.msl.cache.springcachemulti.service.CameraService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CameraServiceImpl implements CameraService {

	@Autowired
	CameraRepository repository;

	@Cacheable(value = "cameras/all", cacheManager = "cacheManager", unless = "#result == null")
	public Iterable<Camera> findAll() {
		LOGGER.info("findAll");
		return repository.findAll();
	}

	@Cacheable(value = "cameras/ByIndex", cacheManager = "cacheManager", unless = "#result == null")
	public Optional<Camera> findBy(String country, String installation, String zone) {
		LOGGER.debug("findBy country {}, installation {}, zone{}:", country, installation, zone);
		return repository.findByCountryCodeAndInstallationIdAndZone(country, installation, zone);
	}

	@Cacheable(key = "#id", value = "cameras/ById", cacheManager = "cacheManager", unless = "#result == null")
	public Optional<Camera>  findById(String id) {
		LOGGER.debug("findById:" + id);
		return repository.findById(id);
	}

	@CachePut(key = "#camera.id", value = "cameras", cacheManager = "cacheManager")
	public Camera create(Camera camera) {
		LOGGER.debug("create:" + camera);
		return repository.save(camera);
	}

	@CachePut(key = "#id", value = "cameras", cacheManager = "cacheManager")
	public Camera update(Camera camera, String id) {
		LOGGER.debug("update camera {} with id {}:", camera, id);
		
		return repository.findById(id).map(newCamera -> {
			camera.setSerial(camera.getSerial());
			return repository.save(camera);
		}).orElseGet(() -> {
			camera.setId(id);
			return repository.save(camera);
		});
	}

	@CacheEvict(key = "#id", value = "cameras", cacheManager = "cacheManager")
	public void deleteById(String id) {
		LOGGER.debug("deleteById:" + id);
		repository.deleteById(id);
	}
}
