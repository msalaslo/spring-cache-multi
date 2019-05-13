package com.msl.cache.springcachemulti.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.msl.cache.springcachemulti.domain.entity.Camera;


/**
 * Camera DAO/Repository
 * @since 1.0.0
 * @author FaaS [faas@securitasdirect.es]
 */
@Repository
public interface CameraRepository extends PagingAndSortingRepository<Camera, String>{
	
	public Optional<Camera> findByCountryCodeAndInstallationIdAndZone(String countrCode, String installationId, String zone);
	
	public Iterable<Camera> findByCountryCodeAndInstallationId(String countrCode, String installationId);
	
	public Iterable<Camera> findByZoneStartingWith(String zone);
	
	@Query("select c from Camera c where c.countryCode = ?1 and c.installationId = ?2 and c.zone like 'VS%' ")
	public Iterable<Camera> findVossDevicesBy(String country, String installation);

}
