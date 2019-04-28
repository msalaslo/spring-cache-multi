package com.msl.cache.springcachemulti.domain.repository;

import com.msl.cache.springcachemulti.domain.entity.Camera;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Sample repository used for demonstration purposes only.  Ideally Spring Data SHOULD be used for building repositories.
 * <b>Please remove for actual project implementation.</b>
 *
 * @since 1.0.0
 * @author FaaS [faas@securitasdirect.es]
 */
@Slf4j
@Repository
public class CameraRepository {

    public List<Camera> findAll() {

        LOGGER.debug("Retrieving the whole list of application items!");

        return Arrays.asList(getDummyCamera());
    }

    public void save(Camera camera) {

        LOGGER.debug("Application item with ID {} added", camera.getId());
    }
    
    public Camera findById(String id) {
        LOGGER.info("Camera item with ID {} added", id);

        return getDummyCamera();
    }
    
    public String deleteById(String id) {
        return id;
    }
    
    private Camera getDummyCamera() {
        String installation = UUID.randomUUID().toString();
        String serial = UUID.randomUUID().toString();
        Camera camera = Camera.builder()
                .id("ESP" + installation + "01")
                .country("ESP")
                .installation(installation)
                .zone("01")
                .serial(serial)
                .build();
        return camera;
    }

}
