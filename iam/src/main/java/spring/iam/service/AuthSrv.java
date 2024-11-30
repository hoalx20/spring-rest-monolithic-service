package spring.iam.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import spring.iam.auth.IAuthProvider;
import spring.iam.constant.Failed;
import spring.iam.exception.ServiceExc;
import spring.iam.mapper.AuthMpr;
import spring.iam.mapper.BadCredentialMpr;
import spring.iam.model.BadCredential;
import spring.iam.model.dto.BadCredentialCre;
import spring.iam.model.dto.CredentialReq;
import spring.iam.model.dto.CredentialRes;
import spring.iam.model.dto.RegisterReq;
import spring.iam.model.dto.RegisterRes;
import spring.iam.model.dto.UserCre;
import spring.iam.model.dto.UserRes;
import spring.iam.repository.BadCredentialRepo;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthSrv implements IAuthSrv {
	@NonFinal
	@Value("${jwt.access-token.secret}")
	String accessTokenSecret;

	@NonFinal
	@Value("${jwt.access-token.time-to-live}")
	long accessTokenTimeToLive;

	@NonFinal
	@Value("${jwt.refresh-token.secret}")
	String refreshTokenSecret;

	@NonFinal
	@Value("${jwt.refresh-token.time-to-live}")
	long refreshTokenTimeToLive;

	IAuthProvider<SignedJWT, UserRes> jwtAuthProvider;
	IUserSrv userSrv;
	BadCredentialRepo badCredentialRepo;
	AuthMpr authMpr;
	BadCredentialMpr badCredentialMpr;
	PasswordEncoder passwordEncoder;

	public CredentialRes newCredentials(UserRes user) {
		String accessId = UUID.randomUUID().toString();
		String refreshId = UUID.randomUUID().toString();
		String accessToken = jwtAuthProvider.sign(user, accessTokenTimeToLive, accessTokenSecret, accessId, refreshId);
		long accessTokenIssuedAt = System.currentTimeMillis();
		String refreshToken = jwtAuthProvider.sign(user, refreshTokenTimeToLive, refreshTokenSecret, refreshId, accessId);
		long refreshTokenIssuedAt = System.currentTimeMillis();
		return new CredentialRes(accessToken, accessTokenIssuedAt, refreshToken, refreshTokenIssuedAt);
	}

	@Override
	public CredentialRes signUp(RegisterReq request) {
		try {
			UserCre creation = authMpr.asUserCreation(request);
			UserRes user = userSrv.save(creation);
			return newCredentials(user);
		} catch (ServiceExc e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceExc(Failed.SIGN_UP);
		}
	}

	@Override
	public CredentialRes signIn(CredentialReq request) {
		try {
			UserRes user = userSrv.findByUsername(request.getUsername());
			boolean isMatch = passwordEncoder.matches(request.getPassword(), user.getPassword());
			if (!isMatch)
				throw new ServiceExc(Failed.BAD_CREDENTIALS);
			return newCredentials(user);
		} catch (ServiceExc e) {
			if (Failed.NOT_EXISTS.equals(e.getCauseBy()))
				throw new ServiceExc(Failed.BAD_CREDENTIALS);
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceExc(Failed.SIGN_IN);
		}
	}

	@Override
	public RegisterRes me(String username) {
		try {
			UserRes user = userSrv.findByUsername(username);
			return authMpr.asRegisterResponse(user);
		} catch (ServiceExc e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceExc(Failed.RETRIEVE_PROFILE);
		}
	}

	@Override
	public long signOut(BadCredentialCre creation) {
		try {
			BadCredential badCredential = badCredentialMpr.asModel(creation);
			BadCredential saved = badCredentialRepo.save(badCredential);
			return saved.getId();
		} catch (ServiceExc e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceExc(Failed.SIGN_OUT);
		}
	}

	@Override
	public CredentialRes refresh(BadCredentialCre creation, String referId, String refreshToken) {
		try {
			var claims = jwtAuthProvider.verify(refreshToken, refreshTokenSecret);
			String refreshTokenId = claims.getJWTClaimsSet().getJWTID();
			if (!referId.equals(refreshTokenId)) {
				throw new ServiceExc(Failed.TOKEN_NOT_SUITABLE);
			}
			BadCredential badCredential = badCredentialMpr.asModel(creation);
			BadCredential saved = badCredentialRepo.save(badCredential);
			UserRes user = userSrv.findById(saved.getUserId());
			return newCredentials(user);
		} catch (ServiceExc e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceExc(Failed.REFRESH_JWT_TOKEN);
		}
	}
}
