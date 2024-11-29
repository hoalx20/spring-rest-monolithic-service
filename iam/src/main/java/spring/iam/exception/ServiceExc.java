package spring.iam.exception;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import spring.iam.constant.Failed;

@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ServiceExc extends RuntimeException {
	Failed causeBy;
}
