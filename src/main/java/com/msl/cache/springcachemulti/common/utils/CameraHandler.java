package com.msl.cache.springcachemulti.common.utils;

import com.msl.cache.springcachemulti.domain.entity.Camera;

public class CameraHandler {
	
	public static void generateAndSetId(Camera camera) {
		if(camera != null) {
			if(camera.getId() == null) {
				camera.setId(generateId(camera.getCountry(), camera.getInstallation(), camera.getZone()));
			}
		}
	}
	
	public static Camera generateAndSetIdNreCamera(Camera camera) {
		Camera newCamera = new Camera();
		if(camera != null) {
			if(camera.getId() == null) {
				camera.setId(generateId(camera.getCountry(), camera.getInstallation(), camera.getZone()));
			}
		}
	}
	
	public static String generateId(String country, String installation, String zone) {
		return country + installation + zone;
	}

}
