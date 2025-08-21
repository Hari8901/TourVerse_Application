package com.tourverse.backend.admin.entity;

import com.tourverse.backend.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "admins")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class Admin extends User {

	@Column(name = "employee_id", nullable = false, unique = true)
	@NotBlank(message = "Employee ID is required")
	private String employeeId;

	@Column(nullable = false)
	@NotBlank(message = "Department is required")
	private String department;
}