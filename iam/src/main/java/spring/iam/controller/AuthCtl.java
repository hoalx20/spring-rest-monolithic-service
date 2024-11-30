package spring.iam.controller;

import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import spring.iam.constant.Success;
import spring.iam.model.dto.AuthenticationDetail;
import spring.iam.model.dto.BadCredentialCre;
import spring.iam.model.dto.CredentialReq;
import spring.iam.model.dto.CredentialRes;
import spring.iam.model.dto.RegisterReq;
import spring.iam.model.dto.RegisterRes;
import spring.iam.response.Response;
import spring.iam.service.IAuthSrv;

@Tag(name = "Authentication Controller", description = "Authentication controller on API")
@RestController
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthCtl {
	IAuthSrv jwtAuthSrv;

	@Operation(summary = "Sign-up", description = "Sign-up endpoint for new user")
	@ApiResponse(responseCode = "200", description = "Ok: Success", content = {
			@Content(schema = @Schema(implementation = CredentialRes.class), mediaType = "application/json") })
	@ApiResponse(responseCode = "400", description = "Bad request: missing username, password, roleIds, username already exists, etc...", content = {
			@Content(schema = @Schema(), mediaType = "application/json") })
	@ApiResponse(responseCode = "500", description = "Internal server error: uncategorized error", content = {
			@Content(schema = @Schema(), mediaType = "application/json") })
	@PostMapping("/sign-up")
	public ResponseEntity<Response<CredentialRes>> signUp(@RequestBody @Valid RegisterReq request) {
		Success created = Success.SIGN_UP;
		CredentialRes credential = jwtAuthSrv.signUp(request);
		var response = Response.<CredentialRes> builder().code(created.getCode()).message(created.getMessage()).payload(credential).build();
		return ResponseEntity.status(created.getHttpStatus()).body(response);
	}

	@Operation(summary = "Sign-in", description = "Sign-in endpoint for existing user")
	@ApiResponse(responseCode = "200", description = "Ok: Success", content = {
			@Content(schema = @Schema(implementation = CredentialRes.class), mediaType = "application/json") })
	@ApiResponse(responseCode = "400", description = "Bad request: missing username, password, username not exists, etc...", content = {
			@Content(schema = @Schema(), mediaType = "application/json") })
	@ApiResponse(responseCode = "500", description = "Internal server error: uncategorized error", content = {
			@Content(schema = @Schema(), mediaType = "application/json") })
	@PostMapping("/sign-in")
	public ResponseEntity<Response<CredentialRes>> signIn(@RequestBody @Valid CredentialReq request) {
		Success ok = Success.SIGN_IN;
		CredentialRes credential = jwtAuthSrv.signIn(request);
		var response = Response.<CredentialRes> builder().code(ok.getCode()).message(ok.getMessage()).payload(credential).build();
		return ResponseEntity.ok().body(response);
	}

	@Operation(summary = "Retrieve profile", description = "Retrieve profile endpoint for existing user")
	@ApiResponse(responseCode = "200", description = "Ok: Success", content = {
			@Content(schema = @Schema(implementation = RegisterReq.class), mediaType = "application/json") })
	@ApiResponse(responseCode = "401", description = "Unauthorized: missing accessToken, token is ill legal, etc...", content = {
			@Content(schema = @Schema(), mediaType = "application/json") })
	@ApiResponse(responseCode = "500", description = "Internal server error: uncategorized error", content = {
			@Content(schema = @Schema(), mediaType = "application/json") })
	@SecurityRequirement(name = "Authorization")
	@GetMapping("/me")
	public ResponseEntity<Response<RegisterRes>> me(Authentication authentication) {
		Success ok = Success.RETRIEVE_PROFILE;
		RegisterRes user = jwtAuthSrv.me(authentication.getName());
		var response = Response.<RegisterRes> builder().code(ok.getCode()).message(ok.getMessage()).payload(user).build();
		return ResponseEntity.ok().body(response);
	}

	@Operation(summary = "Sign out", description = "Sign out endpoint for logon user")
	@ApiResponse(responseCode = "200", description = "Ok: Success", content = {
			@Content(schema = @Schema(implementation = Long.class), mediaType = "application/json") })
	@ApiResponse(responseCode = "401", description = "Unauthorized: missing accessToken, token is ill legal, etc...", content = {
			@Content(schema = @Schema(), mediaType = "application/json") })
	@ApiResponse(responseCode = "500", description = "Internal server error: uncategorized error", content = {
			@Content(schema = @Schema(), mediaType = "application/json") })
	@SecurityRequirement(name = "Authorization")
	@PostMapping("/sign-out")
	public ResponseEntity<Response<Long>> signOut(Authentication authentication) {
		Success created = Success.SIGN_OUT;
		@SuppressWarnings("unchecked")
		var user = ((AuthenticationDetail) ((HashMap<String, Object>) authentication.getDetails()).get("user"));
		var badCredential = new BadCredentialCre(user.getSessionId(), user.getSessionExpiredAt(), user.getUserId());
		long id = jwtAuthSrv.signOut(badCredential);
		Response<Long> response = Response.<Long> builder().code(created.getCode()).message(created.getMessage()).payload(id).build();
		return ResponseEntity.status(created.getHttpStatus()).body(response);
	}

	@Operation(summary = "Refresh token", description = "Refresh token endpoint for existing user")
	@ApiResponse(responseCode = "200", description = "Ok: Success", content = {
			@Content(schema = @Schema(implementation = RegisterRes.class), mediaType = "application/json") })
	@ApiResponse(responseCode = "401", description = "Unauthorized: missing accessToken, fresh token, token is ill legal, edited, etc...", content = {
			@Content(schema = @Schema(), mediaType = "application/json") })
	@ApiResponse(responseCode = "500", description = "Internal server error: uncategorized error", content = {
			@Content(schema = @Schema(), mediaType = "application/json") })
	@SecurityRequirement(name = "Authorization")
	@PostMapping("/refresh")
	public ResponseEntity<Response<CredentialRes>> refresh(@RequestHeader(value = "X-REFRESH-TOKEN", required = true) String refreshToken,
			Authentication authentication) {
		Success ok = Success.REFRESH_JWT_TOKEN;
		refreshToken = refreshToken.replace("Bearer ", "");
		@SuppressWarnings("unchecked")
		var user = ((AuthenticationDetail) ((HashMap<String, Object>) authentication.getDetails()).get("user"));
		var badCredential = new BadCredentialCre(user.getSessionId(), user.getSessionExpiredAt(), user.getUserId());
		CredentialRes credential = jwtAuthSrv.refresh(badCredential, user.getReferId(), refreshToken);
		Response<CredentialRes> response = Response.<CredentialRes> builder().code(ok.getCode()).message(ok.getMessage()).payload(credential).build();
		return ResponseEntity.ok().body(response);
	}
}
