package com.msl.cache.springcachemulti.api.converter;

import java.util.List;

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
public interface CameraConverter {
    List<CameraDTO> mapCameraListToDTO(List<Camera> cameras);
    //Void workaround: https://github.com/mapstruct/mapstruct/issues/661
    PageDTO<CameraDTO> mapCameraPageToDTO(Void workaround, Page<Camera> cameras);
}