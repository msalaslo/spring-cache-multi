package com.msl.cache.springcachemulti.service;

import java.util.List;

import com.msl.cache.springcachemulti.api.dto.CameraDTO;

public interface CameraDTOLoaderService {
	
	public void putToCaches(CameraDTO cameraDTO);
	public void loadToCaches(List<CameraDTO> cameras);

}
