package com.iispl.entity;

public class Source {
    String bankName;
    String sourceType;
    String sourcePath;
	public Source(String bankName, String sourceType, String sourcePath) {
		super();
		this.bankName = bankName;
		this.sourceType = sourceType;
		this.sourcePath = sourcePath;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public String getSourcePath() {
		return sourcePath;
	}
	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}
    
    
}
