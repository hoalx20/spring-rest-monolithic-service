package spring.iam.service;

import spring.iam.model.dto.BadCredentialCre;
import spring.iam.model.dto.CredentialReq;
import spring.iam.model.dto.CredentialRes;
import spring.iam.model.dto.RegisterReq;
import spring.iam.model.dto.RegisterRes;

public interface IAuthSrv {
  CredentialRes signUp(RegisterReq request);

  CredentialRes signIn(CredentialReq request);

  RegisterRes me(String username);

  long signOut(BadCredentialCre creation);

  CredentialRes refresh(BadCredentialCre badCredential, String referId, String refreshToken);
}
