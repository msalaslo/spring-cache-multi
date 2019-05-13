package com.msl.cache.springcachemulti.api.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Sample DTO object. <b>Please remove for actual project implementation.</b>
 *
 * @since 1.0.0
 * @author FaaS [faas@securitasdirect.es]
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageDTO<T> extends BaseDTO {

	@ApiModelProperty(notes = "Content of the page", required = true)
	private List<T> content;

	@ApiModelProperty(notes = "Number of elements in the page", required = true)
	private long number;

	@ApiModelProperty(notes = "Size of elements in the page", required = true)
	private long size;

	@ApiModelProperty(notes = "Total elements in the repository", required = true)
	private long totalElements;

	@ApiModelProperty(notes = "Total pages in the repository", required = true)
	private long totalPages;

}
