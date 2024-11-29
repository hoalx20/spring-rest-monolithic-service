package spring.iam.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import spring.iam.model.User;
import spring.iam.model.dto.UserCre;
import spring.iam.model.dto.UserRes;
import spring.iam.model.dto.UserUpd;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMpr {
	User asModel(UserCre creation);

	UserRes asResponse(User model);

	List<UserRes> asListResponse(List<User> models);

	void mergeModel(@MappingTarget User model, UserUpd update);
}
