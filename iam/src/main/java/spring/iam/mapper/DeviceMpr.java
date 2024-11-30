package spring.iam.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import spring.iam.model.Device;
import spring.iam.model.dto.DeviceCre;
import spring.iam.model.dto.DeviceRes;
import spring.iam.model.dto.DeviceUpd;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DeviceMpr {
  Device asModel(DeviceCre creation);

  DeviceRes asResponse(Device model);

  List<DeviceRes> asListResponse(List<Device> models);

  void mergeModel(@MappingTarget Device model, DeviceUpd update);
}
