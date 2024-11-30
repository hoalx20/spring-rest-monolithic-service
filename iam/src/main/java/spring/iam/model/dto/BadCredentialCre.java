package spring.iam.model.dto;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BadCredentialCre {
	@NotBlank(message = "accessTokenId can not be blank")
	String accessTokenId;

	@NotBlank(message = "accessTokenExpiredAt can not be blank")
	Date accessTokenExpiredAt;

	@NotNull(message = "userId can not be null")
	Long userId;
}
