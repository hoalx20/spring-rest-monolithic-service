package spring.iam.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import spring.iam.auth.IAuthProvider;
import spring.iam.constant.Failed;
import spring.iam.constant.Global;
import spring.iam.exception.ServiceExc;
import spring.iam.model.dto.AuthenticationDetail;
import spring.iam.model.dto.UserRes;
import spring.iam.repository.BadCredentialRepo;
import spring.iam.response.Response;
import spring.iam.service.IUserSrv;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DaoAuthenticationFlt extends OncePerRequestFilter {
  @NonFinal
  @Value("${jwt.access-token.secret}")
  String accessTokenSecret;

  IUserSrv userSrv;
  IAuthProvider<SignedJWT, UserRes> jwtAuthProvider;
  BadCredentialRepo badCredentialRepo;
  ObjectMapper objectMapper;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
      if (StringUtils.isEmpty(authorization)) {
        filterChain.doFilter(request, response);
        return;
      }
      String accessToken = authorization.replace("Bearer ", "");
      SignedJWT verify = jwtAuthProvider.verify(accessToken, accessTokenSecret);
      String username = verify.getJWTClaimsSet().getSubject();
      String accessTokenId = verify.getJWTClaimsSet().getJWTID();
      boolean isTokenBlocked = badCredentialRepo.existsByAccessTokenId(accessTokenId);
      if (StringUtils.isEmpty(username)) throw new ServiceExc(Failed.ILL_LEGAL_JWT_TOKEN);
      if (isTokenBlocked) {
        throw new ServiceExc(Failed.TOKEN_BLOCKED);
      }
      if (Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
        UserDetails userDetails = userSrv.userDetailsService().loadUserByUsername(username);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        var details = buildDetails(verify);
        authentication.setDetails(details);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
      }
      filterChain.doFilter(request, response);
    } catch (ServiceExc e) {
      Failed causeBy = e.getCauseBy();
      immediatelyTerminate(
          response, causeBy.getHttpStatus().value(), causeBy.getCode(), causeBy.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      Global causeBy = Global.UNCATEGORIZED_EXCEPTION;
      immediatelyTerminate(
          response, causeBy.getHttpStatus().value(), causeBy.getCode(), causeBy.getMessage());
    }
  }

  private void immediatelyTerminate(
      HttpServletResponse response, int httpStatus, int code, String message) throws IOException {
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(httpStatus);
    var resp = Response.builder().code(code).message(message).build();
    response.getWriter().write(objectMapper.writeValueAsString(resp));
    response.flushBuffer();
  }

  private HashMap<String, Object> buildDetails(SignedJWT signedJWT) throws Exception {
    HashMap<String, Object> details = new HashMap<>();
    String username = signedJWT.getJWTClaimsSet().getSubject();
    long userId = signedJWT.getJWTClaimsSet().getLongClaim("userId");
    String sessionId = signedJWT.getJWTClaimsSet().getJWTID();
    Date sessionExpiredAt = signedJWT.getJWTClaimsSet().getExpirationTime();
    String scope = signedJWT.getJWTClaimsSet().getStringClaim("scope");
    String referId = signedJWT.getJWTClaimsSet().getStringClaim("referId");
    details.put(
        "user",
        new AuthenticationDetail(username, userId, sessionId, sessionExpiredAt, scope, referId));
    return details;
  }
}
