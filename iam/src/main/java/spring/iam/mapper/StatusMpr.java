package spring.iam.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import spring.iam.model.Status;
import spring.iam.model.dto.StatusCre;
import spring.iam.model.dto.StatusRes;
import spring.iam.model.dto.StatusUpd;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StatusMpr {
	Status asModel(StatusCre creation);

	StatusRes asResponse(Status model);

	List<StatusRes> asListResponse(List<Status> models);

	void mergeModel(@MappingTarget Status model, StatusUpd update);
}
