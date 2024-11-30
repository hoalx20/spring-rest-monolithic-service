package spring.iam.model.dto;

import java.util.Set;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRes {
	@Parameter(name = "userId", description = "userId", required = true, example = "1")
	@Schema(name = "userId", description = "userId", type = "long")
	Long id;

	@Parameter(name = "username", description = "username", required = true, example = "hoalx0")
	@Schema(name = "username", description = "username", type = "string")
	String username;

	@Parameter(name = "password", description = "password", required = true, example = "hoalx0")
	@Schema(name = "password", description = "password", type = "string")
	Set<RoleRes> roles;
}
