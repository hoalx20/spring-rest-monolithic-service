package spring.iam.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import spring.iam.introspector.DeviceIntrospect;

@Data
@EqualsAndHashCode(exclude = { "user" })
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@EntityListeners(value = DeviceIntrospect.class)
@Table
@SoftDelete
public class Device implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@NotBlank(message = "ipAddress can not be blank")
	@Column(name = "ipAddress", nullable = false)
	String ipAddress;

	@NotBlank(message = "userAgent can not be blank")
	@Column(name = "userAgent", nullable = false)
	String userAgent;

	@CreationTimestamp
	LocalDateTime created;
	@UpdateTimestamp
	LocalDateTime updated;

	@ManyToOne
	@JoinColumn(name = "user_id")
	User user;
}