package com.msl.cache.springcachemulti.service;

import java.util.Optional;

import com.msl.cache.springcachemulti.api.dto.CameraDTO;
import com.msl.cache.springcachemulti.api.dto.PageDTO;

public interface CameraService {
	public CameraDTO put(CameraDTO camera);
	public Optional<CameraDTO> findById(String id);
	public Iterable<CameraDTO> findBy(String country, String installation);
	public Optional<CameraDTO> findBy(String country, String installation, String zone);	
	public CameraDTO update(CameraDTO camera, String id);
	public void deleteById(String id);
	public PageDTO<CameraDTO> findAll(int page, int pageSize);
	public Iterable<CameraDTO> findVossDevices(String country, String installation);
	public long count();
}
