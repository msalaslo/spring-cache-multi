package com.msl.cache.springcachemulti.api.converter;

import java.util.Optional;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import com.msl.cache.springcachemulti.api.dto.CameraDTO;
import com.msl.cache.springcachemulti.api.dto.PageDTO;
import com.msl.cache.springcachemulti.domain.entity.Camera;

/**
 * Camera converter
 *
 * @since 1.0.0
 * @author FaaS [faas@securitasdirect.es]
 */
@Mapper(componentModel = "spring")
public abstract class CameraConverter {
	public abstract CameraDTO toCameraDto(Camera camera);
	
	//Implementamos este mapeo ya que de momento mapstruct no soporta Optional (busca un contructor sin parametros de Optional)
	public Optional<CameraDTO> toOptionalCameraDto(Optional<Camera> camera){
		if(camera.isPresent()) {
			return Optional.of(toCameraDto(camera.get()));
		}else {
			return Optional.empty();
		}
	}
	
	public abstract Iterable<CameraDTO> toIterableCameraDto(Iterable<Camera> cameras);
	
    //Void workaround: https://github.com/mapstruct/mapstruct/issues/661
	public abstract PageDTO<CameraDTO> toPageCameraDto(Void workaround, Page<Camera> cameras);
    
	public abstract Camera toCameraEntity(CameraDTO camera);
}