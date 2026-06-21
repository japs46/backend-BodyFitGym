package com.japs.backend.backend_BodyFitGym.infrastructure.entry_points.rest.exceptionhandler;



import com.japs.backend.backend_BodyFitGym.application.response.ApiResponse;
import com.japs.backend.backend_BodyFitGym.application.response.ErrorResponse;
import com.japs.backend.backend_BodyFitGym.application.response.Errors;
import com.japs.backend.backend_BodyFitGym.application.utils.ResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST) // Devuelve un 400 Bad Request
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<ErrorResponse>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		List<Errors> errorsList = new ArrayList<>();
		
		ex.getBindingResult().getAllErrors().forEach(error ->{
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errorsList.add(Errors.builder().field(fieldName).errorMessage(errorMessage).build());
		});

		ErrorResponse errorResponse = ErrorResponse.builder()
				.message("Validacion Fallida")
				.errors(errorsList)
				.build();

		return ResponseEntity.badRequest().body(ApiResponse.<ErrorResponse>builder()
				.message("Bad Request")
				.status(false)
				.data(errorResponse).build());
    }
	
	@ResponseStatus(HttpStatus.BAD_REQUEST) // Devuelve un 400 Bad Request
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public String handleValidationExceptions2(HttpMessageNotReadableException ex) {
		return "No hay cuerpo de solicitud";
    }

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ApiResponse<Void>> handleBusinessException(RuntimeException e,WebRequest request) {
		String path = request.getDescription(false);
		ApiResponse<Void> apiResponse = ResponseBuilder.errorMessage(e.getMessage());
		log.error("Excepción de negocio en: {} -> {}", path, e.getMessage());
		log.error("response: {}",apiResponse.toString());
		log.error("Finalizo operacion");
		return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> handleUnexpectedException(Exception e,WebRequest request) {
		String path = request.getDescription(false);
		ApiResponse<Void> apiResponse = ResponseBuilder.errorMessage("No se pudo completar la operación. Por favor " +
				"intente más tarde o comuníquese con el administrador del sistema.");
		log.error("Excepción inesperada en: {} -> {}",path, e.getMessage());
		log.error("response: {}",apiResponse.toString());
		log.error("Finalizo operacion");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
	}

}