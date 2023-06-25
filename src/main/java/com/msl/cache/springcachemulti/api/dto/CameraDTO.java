package com.msl.cache.springcachemulti.api.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Sample DTO object. <b>Please remove for actual project implementation.</b>
 *
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CameraDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(notes = "serial, the identifier", required = true)
	public String serial;
    
    @ApiModelProperty(notes = "id autoincremental", required = true)
	public String id;
    
    @ApiModelProperty(notes = "Country code", required = true)
	public String countryCode;
    
    @ApiModelProperty(notes = "Installation id", required = true)
	public String installationId;
    
    @ApiModelProperty(notes = "Zone", required = true)
	public String zone;
    
    @ApiModelProperty(notes = "Alias", required = true)
    public String alias;
    
    @ApiModelProperty(notes = "Creation time", required = true)
	public Date creationTime;
    
    @ApiModelProperty(notes = "Last update time", required = true)
	public Date lastUpdateTime;
    
}
