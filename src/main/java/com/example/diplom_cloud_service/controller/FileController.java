package com.example.diplom_cloud_service.controller;


import com.example.diplom_cloud_service.dto.FileDto;
import com.example.diplom_cloud_service.service.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;
import java.util.Map;
@Log4j2
@AllArgsConstructor
@RestController
public class FileController {
	private final FileService fileService;

	@GetMapping("/list")
	public List<FileDto> getFileList(@RequestParam("limit") int limit) {
		return fileService.showSavedFile(limit);
	}



	@PostMapping("/file")
	public HttpStatus saveFile(String filename, MultipartFile file) throws IOException {
		fileService.uploadFile(filename, file);
		return HttpStatus.OK;
	}

	@DeleteMapping("/file")
	public HttpStatus deleteFile(@RequestParam("filename") String fileName) {
		return fileService.deleteFile(fileName);
	}

	@GetMapping("/file")
	public byte[] getFile(@RequestParam("filename") String fileName) {
		return fileService.getFileByName(fileName);
	}

	@PutMapping("/file")
	public HttpStatus editFile(@RequestParam("filename") String fileName,
	                           @RequestBody Map<String, String> newFileName) {
		return fileService.editFileByName(fileName, newFileName.get("filename"));
	}
}
