package spring.iam.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import spring.iam.model.Role;
import spring.iam.model.dto.RoleCre;
import spring.iam.model.dto.RoleRes;
import spring.iam.model.dto.RoleUpd;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoleMpr {
  Role asModel(RoleCre creation);

  RoleRes asResponse(Role model);

  List<RoleRes> asListResponse(List<Role> models);

  void mergeModel(@MappingTarget Role model, RoleUpd update);
}
