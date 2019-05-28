package com.msl.cache.springcachemulti.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	public Page<Camera> findByZoneStartingWith(String zoneStarting, Pageable pageable);
	public Iterable<Camera> findByCountryCodeAndInstallationIdAndZoneStartingWith(String country, String installation, String zoneStarting);
}
