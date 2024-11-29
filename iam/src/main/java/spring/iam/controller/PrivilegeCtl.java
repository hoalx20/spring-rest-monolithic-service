package spring.iam.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import spring.iam.constant.Success;
import spring.iam.model.dto.PrivilegeCre;
import spring.iam.model.dto.PrivilegeRes;
import spring.iam.model.dto.PrivilegeUpd;
import spring.iam.response.Multi;
import spring.iam.response.Response;
import spring.iam.response.ResponsePaging;
import spring.iam.service.IPrivilegeSrv;

@RestController
@RequestMapping(path = "/api/v1/privileges")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PrivilegeCtl {
	IPrivilegeSrv privilegeSrv;

	@PostMapping
	public ResponseEntity<Response<Long>> save(@RequestBody @Valid PrivilegeCre creation) {
		Success created = Success.SAVE;
		PrivilegeRes saved = privilegeSrv.save(creation);
		var response = Response.<Long> builder().code(created.getCode()).message(created.getMessage()).payload(saved.getId()).build();
		return ResponseEntity.status(created.getHttpStatus()).body(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Response<PrivilegeRes>> findById(@PathVariable(name = "id", required = true) Long id) {
		Success ok = Success.FIND_BY_ID;
		PrivilegeRes queried = privilegeSrv.findById(id);
		var response = Response.<PrivilegeRes> builder().code(ok.getCode()).message(ok.getMessage()).payload(queried).build();
		return ResponseEntity.ok().body(response);
	}

	@GetMapping
	public ResponseEntity<ResponsePaging<List<PrivilegeRes>>> findAll(
			@RequestParam(name = "page", defaultValue = "0") @Min(value = 0, message = "page index must be start from 0.") int page,
			@RequestParam(name = "size", defaultValue = "50") @Min(value = 5, message = "page size must be greater than 5.") @Max(value = 50, message = "page size must be less than 50.") int size) {
		Success ok = Success.FIND_ALL;
		Multi<PrivilegeRes> queried = privilegeSrv.findAll(page, size);
		var response = ResponsePaging.<List<PrivilegeRes>> builder().code(ok.getCode()).message(ok.getMessage()).payload(queried.getValues())
				.paging(queried.getPaging()).build();
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/archived")
	public ResponseEntity<ResponsePaging<List<PrivilegeRes>>> findAllArchived(
			@RequestParam(name = "page", defaultValue = "0") @Min(value = 0, message = "page index must be start from 0.") int page,
			@RequestParam(name = "size", defaultValue = "50") @Min(value = 5, message = "page size must be greater than 5.") @Max(value = 50, message = "page size must be less than 50.") int size) {
		Success ok = Success.FIND_ALL;
		Multi<PrivilegeRes> queried = privilegeSrv.findAllArchived(page, size);
		var response = ResponsePaging.<List<PrivilegeRes>> builder().code(ok.getCode()).message(ok.getMessage()).payload(queried.getValues())
				.paging(queried.getPaging()).build();
		return ResponseEntity.ok().body(response);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Response<Long>> updateById(@PathVariable(name = "id", required = true) Long id, @RequestBody PrivilegeUpd update) {
		Success ok = Success.UPDATE;
		PrivilegeRes latest = privilegeSrv.updateById(id, update);
		var response = Response.<Long> builder().code(ok.getCode()).message(ok.getMessage()).payload(latest.getId()).build();
		return ResponseEntity.ok().body(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Response<Long>> deleteById(@PathVariable(name = "id", required = true) Long id) {
		Success ok = Success.DELETE;
		PrivilegeRes old = privilegeSrv.deleteById(id);
		var response = Response.<Long> builder().code(ok.getCode()).message(ok.getMessage()).payload(old.getId()).build();
		return ResponseEntity.ok().body(response);
	}
}
