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

	public Iterable<Camera> findAll() {
		LOGGER.info("findAll");
		return repository.findAll();
	}

	public Optional<Camera> findById(String country, String installation, String zone) {
		LOGGER.info("findById:" + generateId(country, installation, zone));
		return repository.findById(generateId(country, installation, zone));
	}

	@Cacheable(key = "'cache_cameras_id_' + #id", value = "cameras", cacheManager = "cacheManager")
	public Optional<Camera>  findById(String id) {
		return repository.findById(id);
	}

	@CachePut(key = "'cache_cameras_id_' + #id", value = "cameras", cacheManager = "cacheManager")
	private Camera createAndCache(Camera camera) {
		return repository.save(camera);
	}

	public Camera create(Camera camera) {
		camera.setId(generateId(camera.getCountry(), camera.getInstallation(), camera.getZone()));
		return createAndCache(camera);
	}

	@CachePut(key = "'cache_cameras_id_' + #id", value = "cameras", cacheManager = "cacheManager")
	public Camera update(Camera newCamera, String id) {
		return repository.findById(id).map(camera -> {
			camera.setCountry(newCamera.getCountry());
			camera.setInstallation(newCamera.getInstallation());
			camera.setSerial(newCamera.getSerial());
			camera.setId(newCamera.getId());
			return repository.save(camera);
		}).orElseGet(() -> {
			newCamera.setId(id);
			return repository.save(newCamera);
		});
	}

	@CacheEvict(key = "'cache_cameras_id_' + #id", value = "cameras", cacheManager = "cacheManager")
	public void deleteById(String id) {
		repository.deleteById(id);
	}

	public void delete(String country, String installation, String zone) {
		repository.deleteById(generateId(country, installation, zone));
	}

	private String generateId(String country, String installation, String zone) {
		return country + installation + zone;
	}

//	public void load(Collection<Camera> cameras) {
//		repository.load(cameras);
//	}

}
