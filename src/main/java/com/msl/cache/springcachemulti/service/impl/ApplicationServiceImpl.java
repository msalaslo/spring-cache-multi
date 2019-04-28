package com.msl.cache.springcachemulti.service.impl;

import com.msl.cache.springcachemulti.domain.entity.ApplicationItem;
import com.msl.cache.springcachemulti.domain.repository.ApplicationRepository;
import com.msl.cache.springcachemulti.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Sample service implementation.
 *
 * @since 1.0.0
 * @author FaaS [faas@securitasdirect.es]
 */
@Service
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Override
    public void createApplicationItem(ApplicationItem applicationItem) {
        applicationRepository.save(applicationItem);
    }

    @Override
    public List<ApplicationItem> getApplicationItems() {
      return applicationRepository.findAll();
    }


}
