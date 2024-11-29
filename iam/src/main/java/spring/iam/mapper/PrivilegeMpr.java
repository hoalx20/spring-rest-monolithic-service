package spring.iam.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import spring.iam.model.Privilege;
import spring.iam.model.dto.PrivilegeCre;
import spring.iam.model.dto.PrivilegeRes;
import spring.iam.model.dto.PrivilegeUpd;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PrivilegeMpr {
	Privilege asModel(PrivilegeCre creation);

	PrivilegeRes asResponse(Privilege model);

	List<PrivilegeRes> asListResponse(List<Privilege> models);

	void mergeModel(@MappingTarget Privilege model, PrivilegeUpd update);
}
