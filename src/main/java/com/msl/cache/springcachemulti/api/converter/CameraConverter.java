package com.msl.cache.springcachemulti.api.converter;

import java.util.List;
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
	
	public abstract List<CameraDTO> toListCameraDto(List<Camera> cameras);
	
    //Void workaround: https://github.com/mapstruct/mapstruct/issues/661
	public abstract PageDTO<CameraDTO> toPageCameraDto(Void workaround, Page<Camera> cameras);
    
	public abstract Camera toCameraEntity(CameraDTO camera);
}