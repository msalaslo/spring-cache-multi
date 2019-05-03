package com.msl.cache.springcachemulti.domain.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.msl.cache.springcachemulti.domain.entity.Camera;


/**
 * Sample repository used for demonstration purposes only.  Ideally Spring Data SHOULD be used for building repositories.
 * <b>Please remove for actual project implementation.</b>
 *
 * @since 1.0.0
 * @author FaaS [faas@securitasdirect.es]
 */
@Repository
public interface CameraRepository extends PagingAndSortingRepository<Camera, String>{

}
