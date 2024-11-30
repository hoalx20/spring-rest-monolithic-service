package spring.iam.exception;

import java.text.MessageFormat;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import lombok.var;
import spring.iam.constant.Failed;
import spring.iam.constant.Global;
import spring.iam.response.Response;

@ControllerAdvice
public class ServiceExcResolver {
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<Response<Object>> uncategorized(Exception exception) {
		Global uncategorized = Global.UNCATEGORIZED_EXCEPTION;
		var response = Response.builder().code(uncategorized.getCode()).message(uncategorized.getMessage()).build();
		return ResponseEntity.status(uncategorized.getHttpStatus()).body(response);
	}

	@ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<Response<Object>> methodNotAllowed(HttpRequestMethodNotSupportedException exception) {
		Global methodNotAllowed = Global.METHOD_NOT_ALLOWED;
		var response = Response.builder().code(methodNotAllowed.getCode()).message(methodNotAllowed.getMessage()).build();
		return ResponseEntity.status(methodNotAllowed.getHttpStatus()).body(response);
	}

	@ExceptionHandler(value = NoHandlerFoundException.class)
	public ResponseEntity<Response<Object>> notFound(NoHandlerFoundException exception) {
		Global notFound = Global.NOT_FOUND;
		var response = Response.builder().code(notFound.getCode()).message(notFound.getMessage()).build();
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<Response<Object>> illLegalPayload(MethodArgumentNotValidException exception) {
		Global badRequest = Global.ILL_LEGAL_PAYLOAD;
		String message = exception.getAllErrors().get(0).getDefaultMessage();
		var response = Response.builder().code(badRequest.getCode()).message(message).build();
		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(value = HandlerMethodValidationException.class)
	public ResponseEntity<Response<Object>> illLegalPayload(HandlerMethodValidationException exception) {
		Global badRequest = Global.ILL_LEGAL_PAYLOAD;
		String message = exception.getAllErrors().get(0).getDefaultMessage();
		var response = Response.builder().code(badRequest.getCode()).message(message).build();
		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(value = MissingServletRequestParameterException.class)
	public ResponseEntity<Response<Object>> paramNotReadable(MissingServletRequestParameterException exception) {
		Global badRequest = Global.PARAMETER_NOT_READABLE;
		String message = MessageFormat.format("{0} is missing in query string.", exception.getParameterName());
		var response = Response.builder().code(badRequest.getCode()).message(message).build();
		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Response<Object>> paramNotReadable(MethodArgumentTypeMismatchException exception) {
		Global badRequest = Global.PARAMETER_NOT_READABLE;
		String message = MessageFormat.format("{0} is missing or not readable.", exception.getPropertyName());
		var response = Response.builder().code(badRequest.getCode()).message(message).build();
		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(value = HttpMessageNotReadableException.class)
	public ResponseEntity<Response<Object>> payloadNotReadable(HttpMessageNotReadableException exception) {
		Global badRequest = Global.PAYLOAD_NOT_READABLE;
		var response = Response.builder().code(badRequest.getCode()).message(badRequest.getMessage()).build();
		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(value = MissingRequestHeaderException.class)
	public ResponseEntity<Response<Object>> headerNotReadable(MissingRequestHeaderException exception) {
		Global badRequest = Global.HEADER_NOT_READABLE;
		String message = MessageFormat.format("{0} is missing in request header.", exception.getHeaderName());
		var response = Response.builder().code(badRequest.getCode()).message(message).build();
		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(value = AuthorizationDeniedException.class)
	public ResponseEntity<Response<Object>> forbidden(AuthorizationDeniedException exception) {
		Global forbidden = Global.FORBIDDEN;
		var response = Response.builder().code(forbidden.getCode()).message(forbidden.getMessage()).build();
		return ResponseEntity.status(forbidden.getHttpStatus()).body(response);
	}

	@ExceptionHandler(value = ServiceExc.class)
	public ResponseEntity<Response<Object>> service(ServiceExc exception) {
		Failed causeBy = exception.getCauseBy();
		Response<Object> response = Response.builder().code(causeBy.getCode()).message(causeBy.getMessage()).build();
		return ResponseEntity.status(causeBy.getHttpStatus()).body(response);
	}
}
