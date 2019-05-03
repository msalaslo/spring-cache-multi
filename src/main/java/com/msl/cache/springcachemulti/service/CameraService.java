package com.msl.cache.springcachemulti.service;

import java.util.Optional;

import com.msl.cache.springcachemulti.domain.entity.Camera;

public interface CameraService {
	public Optional<Camera> findById(String id);
	public Optional<Camera> findById(String country, String installation, String zone);
	public Camera create(Camera camera);
	public Camera update(Camera camera, String id);
	public void delete(String country, String installation, String zone);
	public void deleteById(String id);
	public Iterable<Camera> findAll();
}
