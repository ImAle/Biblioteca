package com.example.demo.upload;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements StorageService{

	private final Path ubicacion;
	
	public FileSystemStorageService(StorageProperties propiedades) {
		
		if(propiedades.getLocation().trim().length() == 0)
			throw new StorageException("La ubicacion de subida de archivos no puede estar vacia");
		
		this.ubicacion = Paths.get(propiedades.getLocation());
	}
	
	
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
		
		try {
			// Comprueba si el archivo esta vacio
			if(file.isEmpty())
				throw new StorageException("El archivo esta vacio");
			// Determina la ubicacion del archivo
			Path destino = this.ubicacion.resolve(Paths.get(file.getOriginalFilename()))
					.normalize().toAbsolutePath();
			
			// Asegura que el archivo no se guarda fuera de la ruta 
			if (!destino.getParent().equals(this.ubicacion.toAbsolutePath())) {
			    throw new StorageException("No se puede guardar fuera del directorio");
			}
			
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, destino,
					StandardCopyOption.REPLACE_EXISTING);
			}
			
			String archivoOriginal = file.getOriginalFilename();
		        if (archivoOriginal == null || !archivoOriginal.contains(".")) {
		            throw new StorageException("No se pudo determinar la extensión del archivo");
		        }
			String extension = archivoOriginal.substring(archivoOriginal.lastIndexOf("."));
	        // Crear el nombre del archivo basado en el ID del profesor
	        String nuevoNombre = id + extension;
	        // Determinar la ubicación destino
	        destino = this.ubicacion.resolve(Paths.get(nuevoNombre)).normalize().toAbsolutePath();
	        
	        return nuevoNombre;
		} catch (Exception e) {
			throw new StorageException("Error al almacenar el archivo", e);
		}
		
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.ubicacion, 1)
				.filter(path -> !path.equals(this.ubicacion))
				.map(this.ubicacion::relativize);
		}
		catch (IOException e) {
			throw new StorageException("Error al leer los archivos: ", e);
		}

	}


	@Override
	public Path load(String filename) {
		return ubicacion.resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageFileNotFoundException(
						"No se pudo leer el archivo: " + filename);

			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("No se pudo leer el archivo: " + filename, e);
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(ubicacion.toFile());
		
	}

}
