package com.msl.cache.springcachemulti.config;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
 
@Configuration
@Profile("hazelcast-cache")
public class HazelcastCacheConfig {

	@Bean
    CacheManager cacheManager() {
        return new HazelcastCacheManager(hazelcastInstance()); // (3)
    }
	
    @Bean
    @Profile("client")
    HazelcastInstance hazelcastInstance() {
        // for client HazelcastInstance LocalMapStatistics will not available
//        return HazelcastClient.newHazelcastClient();
        return Hazelcast.newHazelcastInstance();
    }
    
    @Bean
    public Config hazelCastConfig() {
 
        Config config = new Config();
        config.setInstanceName("hazelcast-cache");
 
        MapConfig camerasByCountryAndInstallation = new MapConfig();
        camerasByCountryAndInstallation.setTimeToLiveSeconds(-1);
        camerasByCountryAndInstallation.setEvictionPolicy(EvictionPolicy.LFU);
        config.getMapConfigs().put("cameras/ByCountryAndInstallation",camerasByCountryAndInstallation);
 
        MapConfig camerasByCountryAndInstallationAndZone = new MapConfig();
        camerasByCountryAndInstallationAndZone.setTimeToLiveSeconds(-1);
        camerasByCountryAndInstallationAndZone.setEvictionPolicy(EvictionPolicy.LFU);
        config.getMapConfigs().put("cameras/ByCountryAndInstallationAndZone",camerasByCountryAndInstallationAndZone);
        
        MapConfig camerasBySerial = new MapConfig();
        camerasBySerial.setTimeToLiveSeconds(-1);
        camerasBySerial.setEvictionPolicy(EvictionPolicy.LFU);
        config.getMapConfigs().put("cameras/BySerial",camerasBySerial);
        
        MapConfig camerasAll = new MapConfig();
        camerasAll.setTimeToLiveSeconds(-1);
        camerasAll.setEvictionPolicy(EvictionPolicy.LFU);
        config.getMapConfigs().put("cameras/all",camerasAll);
        
        MapConfig vossByCountryAndInstallation = new MapConfig();
        vossByCountryAndInstallation.setTimeToLiveSeconds(-1);
        vossByCountryAndInstallation.setEvictionPolicy(EvictionPolicy.LFU);
        config.getMapConfigs().put("voss/ByCountryAndInstallation",vossByCountryAndInstallation);
 
        return config;
    }
 
}
