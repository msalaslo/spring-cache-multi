package com.msl.cache.springcachemulti.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.msl.cache.springcachemulti.api.converter.ItemConverter;
import com.msl.cache.springcachemulti.api.dto.ItemDTO;
import com.msl.cache.springcachemulti.service.ApplicationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * Sample controller used as template.
 * Used to invoke from the Spring Feign and Rest Template clients. 
 * <b>Please remove for actual project implementation.</b>
 *
 * @since 3.0.0
 * @author FaaS [faas@securitasdirect.es]
 */
@Slf4j
@RestController
@RequestMapping("/remote")
@Api(value = "Remote application demo")
public class RemoteController {

    @Autowired
    private ItemConverter itemConverter;

    @Autowired
    private ApplicationService applicationService;
    
    @GetMapping(produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "view the list of ALL application items", response = ItemDTO.class)
    public List<ItemDTO> getItems(@RequestHeader HttpHeaders headers) {
        LOGGER.debug("Remote application controller invoked:" + headers);
        return applicationService.getApplicationItems().stream()
                .map(itemConverter::toItemDto)
                .collect(Collectors.toList());
    }
}
