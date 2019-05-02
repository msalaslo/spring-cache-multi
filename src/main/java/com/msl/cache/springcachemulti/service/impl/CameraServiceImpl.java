package com.msl.cache.springcachemulti.service.impl;

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
public class CameraServiceImpl implements CameraService{
	
	@Autowired
	CameraRepository repository;

	@Cacheable(cacheNames = "cameras")
    public Camera findById(String country, String installation, String zone){
		LOGGER.info("Dentro del servicio buscado por id:" + country + installation + zone);
    	return repository.findById(country, installation, zone);
    }
	
	public void create(Camera camera) {
		repository.save(camera);
		put(camera);
	}
	
	@CachePut(value="cameras")
	public void put(Camera camera) {
		repository.save(camera);
	}
	
	@CacheEvict(value = "cameras")
    public void deleteById(String country, String installation, String zone){
		repository.deleteById(country, installation, zone);
    }
	
//	public void load(Collection<Camera> cameras) {
//		repository.load(cameras);
//	}
	
}
