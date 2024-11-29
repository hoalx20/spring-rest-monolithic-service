package spring.iam.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum Permission {
	ROLE_ADMIN("ROLE_ADMIN"), ROLE_USER("ROLE_USER"),

	CREATE("CREATE"), READ("READ"), UPDATE("UPDATE"), DELETE("DELETE");

	String value;
}
