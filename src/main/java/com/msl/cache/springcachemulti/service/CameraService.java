package com.msl.cache.springcachemulti.service;

import com.msl.cache.springcachemulti.domain.entity.Camera;

public interface CameraService {
    public Camera findById(String country, String installation, String zone);
	public void create(Camera camera);
    public void deleteById(String country, String installation, String zone);
}
