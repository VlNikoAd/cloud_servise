package com.example.diplom_cloud_service.repository;


import com.example.diplom_cloud_service.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface FileRepository extends JpaRepository<File,Long> {

	List<File> findAllFilesByLogin(String login);
	@Query("SELECT f FROM File f WHERE f.fileName = :fileName AND f.user.login = :login")
	Optional<File> findFileByFileNameAndUserLogin(@Param("fileName") String fileName,
	                                              @Param("login") String login);
}
