package spring.iam.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Getter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponsePaging<T> {
	@Builder.Default
	long timestamp = System.currentTimeMillis();
	int code;
	String message;
	T payload;
	Paging paging;
}
