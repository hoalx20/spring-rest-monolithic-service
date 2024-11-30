package spring.iam.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum Global {
  UNCATEGORIZED_EXCEPTION(
      0, "uncategorized exception, service can not response.", HttpStatus.INTERNAL_SERVER_ERROR),
  METHOD_NOT_ALLOWED(2, "method not allowed on this endpoint.", HttpStatus.METHOD_NOT_ALLOWED),
  NOT_FOUND(3, "apis resource not found.", HttpStatus.NOT_FOUND),
  ILL_LEGAL_PAYLOAD(4, "missing or request payload is not readable.", HttpStatus.BAD_REQUEST),
  PAYLOAD_NOT_READABLE(5, "missing or request body is not readable.", HttpStatus.BAD_REQUEST),
  PARAMETER_NOT_READABLE(
      6, "missing or request parameter is not readable.", HttpStatus.BAD_REQUEST),
  HEADER_NOT_READABLE(7, "missing or request header is not readable.", HttpStatus.BAD_REQUEST),
  UNAUTHORIZED(
      8,
      "ill legal token: token has been edited, expired or not publish by us.",
      HttpStatus.UNAUTHORIZED),
  MISSING_AUTHORIZATION(
      9,
      "unauthorized: missing authorization or x-refresh-token header in header list.",
      HttpStatus.UNAUTHORIZED),
  FORBIDDEN(
      10, "forbidden: do not has right authority, do not f*ck with cat.", HttpStatus.FORBIDDEN);

  int code;
  String message;
  HttpStatus httpStatus;
}
