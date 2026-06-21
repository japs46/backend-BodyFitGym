package com.japs.backend.backend_BodyFitGym.application.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Errors {
	
	private String field;
	
	private String errorMessage;

}
