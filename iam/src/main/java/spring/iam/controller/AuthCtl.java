package spring.iam.controller;

import java.util.HashMap;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

@RestController
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthCtl {
	IAuthSrv jwtAuthSrv;

	@PostMapping("/sign-up")
	public ResponseEntity<Response<CredentialRes>> signUp(@RequestBody @Valid RegisterReq request) {
		Success created = Success.SIGN_UP;
		CredentialRes credential = jwtAuthSrv.signUp(request);
		var response = Response.<CredentialRes> builder().code(created.getCode()).message(created.getMessage()).payload(credential).build();
		return ResponseEntity.status(created.getHttpStatus()).body(response);
	}

	@PostMapping("/sign-in")
	public ResponseEntity<Response<CredentialRes>> signIn(@RequestBody @Valid CredentialReq request) {
		Success ok = Success.SIGN_IN;
		CredentialRes credential = jwtAuthSrv.signIn(request);
		var response = Response.<CredentialRes> builder().code(ok.getCode()).message(ok.getMessage()).payload(credential).build();
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/me")
	public ResponseEntity<Response<RegisterRes>> me(@AuthenticationPrincipal UserDetails userDetails,
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = true) String accessToken) {
		Success ok = Success.RETRIEVE_PROFILE;
		RegisterRes user = jwtAuthSrv.me(userDetails.getUsername());
		var response = Response.<RegisterRes> builder().code(ok.getCode()).message(ok.getMessage()).payload(user).build();
		return ResponseEntity.ok().body(response);
	}

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

	@PostMapping("/refresh")
	public ResponseEntity<Response<CredentialRes>> refresh(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = true) String accessToken,
			@RequestHeader(value = "X-REFRESH-TOKEN", required = true) String refreshToken, Authentication authentication) {
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
