package com.msl.cache.springcachemulti.common.utils;

import com.msl.cache.springcachemulti.api.dto.CameraDTO;

public class CameraHandler {
	
	public static void generateAndSetId(CameraDTO camera) {
		if(camera != null) {
			if(camera.getId() == null) {
				camera.setId(generateId(camera.getCountryCode(), camera.getInstallationId(), camera.getZone()));
			}
		}
	}
	
	public static String generateId(String country, String installation, String zone) {
		return country + installation + zone;
	}

}
