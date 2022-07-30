package com.mgg.environmentcheck;

public class SnappyError extends Error {
	private static final long serialVersionUID = 1L;
	
	private final SnappyErrorCode errorCode;
	
	public SnappyError(SnappyErrorCode code) {
		super();
		this.errorCode = code;
	}
	
	public SnappyError(SnappyErrorCode code, Error e) {
		super(e);
		this.errorCode = code;
	}
	
	public SnappyError(SnappyErrorCode code, String message) {
		super(message);
		this.errorCode = code;
	}
	
	@Override
	public String getMessage() {
		return String.format("[%s] %s", errorCode.name(), super.getMessage());
	}
}