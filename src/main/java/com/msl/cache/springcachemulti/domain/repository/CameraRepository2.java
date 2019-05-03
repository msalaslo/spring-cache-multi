package com.msl.cache.springcachemulti.domain.repository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.msl.cache.springcachemulti.domain.entity.Camera;

import lombok.extern.slf4j.Slf4j;

/**
 * Sample repository used for demonstration purposes only.  Ideally Spring Data SHOULD be used for building repositories.
 * <b>Please remove for actual project implementation.</b>
 *
 * @since 1.0.0
 * @author FaaS [faas@securitasdirect.es]
 */
@Slf4j
@Repository
public class CameraRepository2 {

    public Camera save(Camera camera) {
        LOGGER.debug("Application item with ID {} added", camera.getCountry() + camera.getInstallation() + camera.getZone());
        return camera;
    }
    
    public List<Camera> findAll() {
        LOGGER.debug("Retrieving the whole list of application items!");
        return Arrays.asList(getDummyCamera("ESP", "123456", "23"));
    }
    
    public Camera findById(String id) {
        LOGGER.info("Camera item with ID {} added", id);
        return getDummyCamera(id);
    }
    
    public Camera findById(String country, String installation, String zone) {
        LOGGER.info("Camera item with ID {} added", country + installation + zone);
        return getDummyCamera(country, installation, zone);
    }
    
    public Boolean deleteById(String id) {
        return true;
    }
    
    private Camera getDummyCamera(String country, String installation, String zone) {
        String serial = UUID.randomUUID().toString();
        Camera camera = Camera.builder()
                .country(country)
                .installation(installation)
                .zone(zone)
                .serial(serial)
                .build();
        return camera;
    }
    
    private Camera getDummyCamera(String id) {
        String installation = UUID.randomUUID().toString();
        return getDummyCamera("FRA", installation, "01");
    }

}
