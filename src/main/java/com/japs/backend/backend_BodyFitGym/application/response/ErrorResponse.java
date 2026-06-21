package com.japs.backend.backend_BodyFitGym.application.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ErrorResponse {

	private String message;
	private List<Errors> errors;

}
