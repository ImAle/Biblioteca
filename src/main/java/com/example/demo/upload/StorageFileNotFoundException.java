package com.example.demo.upload;

public class StorageFileNotFoundException extends StorageException {

	public StorageFileNotFoundException(String mensaje) {
		super(mensaje);
	}

	public StorageFileNotFoundException(String mensaje, Throwable cause) {
		super(mensaje, cause);
	}

}
