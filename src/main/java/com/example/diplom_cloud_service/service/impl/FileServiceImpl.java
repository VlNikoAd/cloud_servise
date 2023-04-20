package com.example.diplom_cloud_service.service.impl;



import com.example.diplom_cloud_service.dto.FileDto;
import com.example.diplom_cloud_service.entity.File;
import com.example.diplom_cloud_service.entity.User;
import com.example.diplom_cloud_service.exception.InvalidDataException;
import com.example.diplom_cloud_service.mapper.MapFile;
import com.example.diplom_cloud_service.repository.FileRepository;
import com.example.diplom_cloud_service.service.FileService;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class FileServiceImpl implements FileService {

	private final FileRepository fileRepository;
	private final SecurityAuthServiceImpl securityAuthService;
	private final MapFile map = new MapFile();

	public FileServiceImpl(FileRepository fileRepository,SecurityAuthServiceImpl securityAuthService) {
		this.fileRepository = fileRepository;
		this.securityAuthService = securityAuthService;
	}

	@Override
	public HttpStatus uploadFile(String filename,
	                             MultipartFile file) throws IOException {

		String login = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = securityAuthService.findUserByLogin(login);


		File uploadFile = new File();
		uploadFile.setFileName(filename);
		uploadFile.setFile(file.getBytes());
		uploadFile.setUser(user);
		uploadFile.setSize(file.getSize());

		fileRepository.saveAndFlush(uploadFile);
		return HttpStatus.OK;
	}

	@Override
	public HttpStatus editFileByName(String fileName, String newFileName) {
		String login = getAuthenticationLogin();
		File file = findFileByFileNameAndLogin(fileName, login);
		file.setFileName(newFileName);
		fileRepository.saveAndFlush(file);
		return HttpStatus.OK;
	}

	@Override
	public byte[] getFileByName(String fileName) {
		String login = getAuthenticationLogin();
		File file = findFileByFileNameAndLogin(fileName, login);
		return file.getFile();
	}

	@Override
	public HttpStatus deleteFile(String fileName) {

		String login = getAuthenticationLogin();
		File file = findFileByFileNameAndLogin(fileName, login);
		fileRepository.delete(file);
		log.info("File delete");
		return HttpStatus.OK;
	}

	@Override
	public List<FileDto> showSavedFile(int limit) {
		String login = getAuthenticationLogin();
		return fileRepository.findAllFilesByLogin(login)
		                     .stream().map(map::mapFileToFileDto)
		                     .collect(Collectors.toList());
	}

	@Transactional
	public File findFileByFileNameAndLogin(String fileName, String login) {
		return fileRepository.findFileByFileNameAndUserLogin(fileName, login)
		                     .orElseThrow(() -> new InvalidDataException("File Not Found"));
	}

	public String getAuthenticationLogin() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
}

