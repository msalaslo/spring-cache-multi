package com.msl.cache.springcachemulti.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.msl.cache.springcachemulti.common.utils.CameraHandler;
import com.msl.cache.springcachemulti.domain.entity.Camera;
import com.msl.cache.springcachemulti.domain.repository.CameraRepository;
import com.msl.cache.springcachemulti.service.CameraService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CameraServiceImpl implements CameraService {

	@Autowired
	CameraRepository repository;

	//TODO Revisar cachear todos los resultados
	public Iterable<Camera> findAll() {
		LOGGER.info("findAll");
		return repository.findAll();
	}

	//TODO En Spring Cache no funcionan las anotaciones de cache entre metodos internos 
	public Optional<Camera> findById(String country, String installation, String zone) {
		LOGGER.info("findBy country {}, installation {}, zone{}:", country, installation, zone);
		return repository.findById(CameraHandler.generateId(country, installation, zone));
	}

	@Cacheable(key = "#id", value = "cameras", cacheManager = "cacheManager", unless = "#result == null")
	public Optional<Camera>  findById(String id) {
		LOGGER.info("findById:" + id);
		return repository.findById(id);
	}

	@CachePut(key = "#camera.id", value = "cameras", cacheManager = "cacheManager")
	public Camera create(Camera camera) {
		LOGGER.info("create:" + camera);
		return repository.save(camera);
	}

	@CachePut(key = "#id", value = "cameras", cacheManager = "cacheManager")
	public Camera update(Camera camera, String id) {
		LOGGER.info("update camera {} with id {}:", camera, id);
		
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
		LOGGER.info("deleteById:" + id);
		repository.deleteById(id);
	}

	//TODO En Spring Cache no funcionan las anotaciones de cache entre metodos internos 
	public void delete(String country, String installation, String zone) {
		LOGGER.info("delete by country {}, installation {}, zone{}:", country, installation, zone);
		String id = CameraHandler.generateId(country, installation, zone);
		repository.deleteById(id);
	}

//	public void load(Collection<Camera> cameras) {
//		repository.load(cameras);
//	}

}
