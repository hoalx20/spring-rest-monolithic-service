package spring.iam.model.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleCre {
	@NotBlank(message = "name can not be blank")
	String name;

	@NotBlank(message = "description can not be blank")
	String description;

	Set<Long> privilegeIds = new HashSet<>(List.of(1L));
}
