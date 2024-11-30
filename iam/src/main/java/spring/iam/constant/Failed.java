package spring.iam.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum Failed {
  SAVE(200, "can not save {resource}: try again later.", HttpStatus.INTERNAL_SERVER_ERROR),
  FIND_BY_ID(
      201, "can not retrieve {resource} by id: try again later.", HttpStatus.INTERNAL_SERVER_ERROR),
  FIND_BY_ID_NO_CONTENT(202, "retrieve {resource} by id return no content.", HttpStatus.NO_CONTENT),
  FIND_BY(
      203,
      "can not retrieve {resource} by {criteria}: try again later.",
      HttpStatus.INTERNAL_SERVER_ERROR),
  FIND_BY_NO_CONTENT(
      204, "retrieve {resource} by {criteria} return no content.", HttpStatus.NO_CONTENT),
  FIND_ALL_BY_ID(
      205, "can not retrieve {resource} by id: try again later.", HttpStatus.INTERNAL_SERVER_ERROR),
  FIND_ALL_BY_ID_NO_CONTENT(
      206, "retrieve {resource}s by id return no content.", HttpStatus.NO_CONTENT),
  FIND_ALL(207, "can not retrieve {resource}: try again later.", HttpStatus.INTERNAL_SERVER_ERROR),
  FIND_ALL_NO_CONTENT(208, "retrieve {resource} return no content.", HttpStatus.NO_CONTENT),
  FIND_ALL_BY(
      209,
      "can not retrieve {resource} by {criteria}: try again later.",
      HttpStatus.INTERNAL_SERVER_ERROR),
  FIND_ALL_BY_NO_CONTENT(
      210, "retrieve {resource}s by {criteria} return no content.", HttpStatus.NO_CONTENT),
  FIND_ALL_ARCHIVED(
      211,
      "can not retrieve archived {resource}: try again later.",
      HttpStatus.INTERNAL_SERVER_ERROR),
  FIND_ALL_ARCHIVED_NO_CONTENT(
      212, "retrieve archived {resource} return no content.", HttpStatus.NO_CONTENT),
  UPDATE(213, "can not update {resource}: try again later.", HttpStatus.INTERNAL_SERVER_ERROR),
  DELETE(214, "can not delete {resource}: try again later.", HttpStatus.INTERNAL_SERVER_ERROR),

  ALREADY_EXISTED(
      215, "{resource} already existed, {identity} must be unique.", HttpStatus.BAD_REQUEST),
  NOT_EXISTS(216, "{resource} does not existed.", HttpStatus.NOT_MODIFIED),
  OWNING_SIDE_NOT_EXISTS(
      217, "can not save {resource}: {owning side} not exists.", HttpStatus.BAD_REQUEST),
  OWNING_SIDE_NOT_AVAILABLE(
      218, "can not save {resource}: {owning side} not available.", HttpStatus.BAD_REQUEST),

  SIGN_JWT_TOKEN(
      219,
      "can not sign token: ill legal claims or encrypt algorithm.",
      HttpStatus.INTERNAL_SERVER_ERROR),
  PARSE_JWT_TOKEN(
      220,
      "can not parse token: ill legal token or encrypt algorithm.",
      HttpStatus.INTERNAL_SERVER_ERROR),
  ILL_LEGAL_JWT_TOKEN(
      221, "ill legal token: token has been edited or not publish by us.", HttpStatus.UNAUTHORIZED),
  JWT_TOKEN_EXPIRED(
      222, "token has been expired: sign in again to get new one.", HttpStatus.UNAUTHORIZED),

  SIGN_UP(223, "can not sign up: try again later.", HttpStatus.INTERNAL_SERVER_ERROR),
  USER_EXISTED(224, "user already existed, username must be unique.", HttpStatus.BAD_REQUEST),
  SIGN_IN(225, "can not sign in: try again later.", HttpStatus.INTERNAL_SERVER_ERROR),
  BAD_CREDENTIALS(226, "bad credentials: username or password not match.", HttpStatus.BAD_REQUEST),
  RETRIEVE_PROFILE(
      227, "can not retrieve profile: try again later.", HttpStatus.INTERNAL_SERVER_ERROR),
  SIGN_OUT(228, "can not sign out: token not be recalled.", HttpStatus.INTERNAL_SERVER_ERROR),
  ENSURE_NOT_BAD_CREDENTIALS(229, "can not ensure token is not recall.", HttpStatus.UNAUTHORIZED),
  TOKEN_BLOCKED(230, "token has been recall: can not use this any more", HttpStatus.UNAUTHORIZED),
  TOKEN_NOT_SUITABLE(
      231, "access token and refresh token are not suitable.", HttpStatus.UNAUTHORIZED),
  RECALL_TOKEN(
      232,
      "refresh token may not complete: token not be recalled.",
      HttpStatus.INTERNAL_SERVER_ERROR),
  REFRESH_JWT_TOKEN(
      233, "can not refresh token: try again later.", HttpStatus.INTERNAL_SERVER_ERROR);

  int code;
  String message;
  HttpStatus httpStatus;
}
