package spring.iam.exception;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import spring.iam.constant.Global;
import spring.iam.response.Response;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	ObjectMapper objectMapper;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		Global unauthorized;
		String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (StringUtils.isEmpty(authorization)) {
			unauthorized = Global.MISSING_AUTHORIZATION;
		} else {
			unauthorized = Global.UNAUTHORIZED;
		}
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		var resp = Response.builder().code(unauthorized.getCode()).message(unauthorized.getMessage()).build();
		response.getWriter().write(objectMapper.writeValueAsString(resp));
		response.flushBuffer();
	}
}
