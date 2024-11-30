package spring.iam.service;

import spring.iam.model.Device;
import spring.iam.model.User;
import spring.iam.model.dto.DeviceCre;
import spring.iam.model.dto.DeviceRes;
import spring.iam.model.dto.DeviceUpd;
import spring.iam.response.Multi;

public interface IDeviceSrv {
  Device ensureExistedById(Long id);

  User ensureOwningExistedByIds(Long id);

  DeviceRes save(DeviceCre creation);

  DeviceRes findById(Long id);

  Multi<DeviceRes> findAll(int page, int size);

  Multi<DeviceRes> findAllArchived(int page, int size);

  DeviceRes updateById(Long id, DeviceUpd update);

  DeviceRes deleteById(Long id);
}
