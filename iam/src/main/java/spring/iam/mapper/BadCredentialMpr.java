package spring.iam.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import spring.iam.model.BadCredential;
import spring.iam.model.dto.BadCredentialCre;
import spring.iam.model.dto.BadCredentialRes;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BadCredentialMpr {
	BadCredential asModel(BadCredentialCre creation);

	BadCredentialRes asResponse(BadCredential model);
}
