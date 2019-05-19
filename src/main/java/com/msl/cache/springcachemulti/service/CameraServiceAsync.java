package com.msl.cache.springcachemulti.service;

public interface CameraServiceAsync {
	public void findById(String id);
	public void findBy(String country, String installation);
	public void findBy(String country, String installation, String zone);	
	public void findAll(int page, int pageSize);
	public void findVossDevices(String country, String installation);
	public long count();
}
