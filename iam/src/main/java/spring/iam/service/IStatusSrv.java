package spring.iam.service;

import spring.iam.model.Status;
import spring.iam.model.User;
import spring.iam.model.dto.StatusCre;
import spring.iam.model.dto.StatusRes;
import spring.iam.model.dto.StatusUpd;
import spring.iam.response.Multi;

public interface IStatusSrv {
	Status ensureExistedById(Long id);

	User ensureOwningExistedByIds(Long id);

	void ensureOwningAvailableById(Long id);

	StatusRes save(StatusCre creation);

	StatusRes findById(Long id);

	Multi<StatusRes> findAll(int page, int size);

	Multi<StatusRes> findAllArchived(int page, int size);

	StatusRes updateById(Long id, StatusUpd update);

	StatusRes deleteById(Long id);
}
