package br.com.cresol.orderms.exception;

public class LocationConflictException extends RuntimeException{
	public LocationConflictException(String msg) {
		super(msg);
	}
}
