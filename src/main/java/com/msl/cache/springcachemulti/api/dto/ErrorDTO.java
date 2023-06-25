package com.msl.cache.springcachemulti.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


/**
 * DTO representing the error according to vnd.error specification.
 *
 */
@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorDTO extends BaseDTO {

    private long timestamp;
    private int status;
    private String error;
    private String exception;
    private String message;
}
