package com.example.diplom_cloud_service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "file", indexes = @Index(columnList = "fileName"))
public class File {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	@ManyToOne(fetch =  FetchType.LAZY)
	@JoinColumn(name = "user_id")
	@JsonBackReference
	private User user;

	@Formula("(select u.login from users u where u.id = user_id)")
	private String login;
	@JsonProperty("fileName")
	private String fileName;
	@JsonProperty("file")
	private byte[] file;
	@JsonProperty("size")
	private Long size;

	public File(User user, String fileName, byte[] file, Long size) {
		this.user = user;
		this.fileName = fileName;
		this.file = file;
		this.size = size;
	}
}