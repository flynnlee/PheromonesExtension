package com.clouter.pheromones.exception;

public class NoSuchProtocolException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public NoSuchProtocolException(int id){
		super("protocol not exist. id:" + id);
	}
}
