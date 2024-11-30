package spring.iam.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import spring.iam.model.dto.CredentialReq;
import spring.iam.model.dto.RegisterReq;
import spring.iam.model.dto.RegisterRes;
import spring.iam.model.dto.UserCre;
import spring.iam.model.dto.UserRes;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AuthMpr {
	UserCre asUserCreation(RegisterReq request);

	UserCre asUserCreation(CredentialReq request);

	RegisterRes asRegisterResponse(UserRes response);
}
