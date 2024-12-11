package com.example.demo.upload;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements StorageService{

	private final Path ubicacion = null;
	
	
	@Override
	public void init() {
		try {
			Files.createDirectories(ubicacion);
		} catch (Exception e) {
			throw new StorageException("Error:", e);
		}
		
	}

	@Override
	public String store(MultipartFile file, int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stream<Path> loadAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Path load(String filename) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resource loadAsResource(String filename) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

}
