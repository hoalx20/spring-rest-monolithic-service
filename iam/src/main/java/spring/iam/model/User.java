package spring.iam.model;

import java.time.LocalDateTime;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.NumericBooleanConverter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import spring.iam.introspector.UserIntrospect;

@Data
@EqualsAndHashCode(exclude = { "status" })
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@EntityListeners(value = UserIntrospect.class)
@Table
@SoftDelete
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@NotBlank(message = "username can not be blank")
	@Column(name = "username", unique = true, nullable = false)
	String username;

	@NotBlank(message = "password can not be blank")
	@Column(name = "password", nullable = false)
	String password;

	@CreationTimestamp
	LocalDateTime created;
	@UpdateTimestamp
	LocalDateTime updated;

	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
	Status status;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	Set<Device> devices;

	@ManyToMany(fetch = FetchType.EAGER)
	@SoftDelete(strategy = SoftDeleteType.ACTIVE, converter = NumericBooleanConverter.class)
	Set<Role> roles;
}
