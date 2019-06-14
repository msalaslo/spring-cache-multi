package com.msl.cache.springcachemulti.service;

import com.msl.cache.springcachemulti.api.dto.CameraDTO;

public interface CameraServicePubSub {
	public void deleteById(String id);
	public CameraDTO update(CameraDTO camera, String id);
}
