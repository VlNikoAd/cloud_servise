package com.example.diplom_cloud_service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
@Entity
@Table(name = "jwt_token", indexes = @Index(columnList = "token"))
public class Token {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "user_id")
	@JsonBackReference
	private User user;
	@Column(unique = true)
	private String token;


	@Override
	public String toString() {
		return "Token{" +
		       "id=" + id +
		       ", value='" + token + '\'' +
		       '}';
	}
}

