package com.msl.cache.springcachemulti.service;

import com.msl.cache.springcachemulti.domain.entity.Camera;

public interface CameraService {
    public Camera findById(String id);
	public void create(Camera camera);
    public void deleteById(String id);
}
