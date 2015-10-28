package com.kingtop.bigdata.mobi.medical.yihucom.repo;

import lombok.Getter;
import lombok.Setter;

public class Office {

	private String source;
	
	private String url;
	
	private String hospital;
	
	private String hospital_url;
	
	private String offic_type;
	
	private String name;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHospital() {
		return hospital;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	public String getHospital_url() {
		return hospital_url;
	}

	public void setHospital_url(String hospital_url) {
		this.hospital_url = hospital_url;
	}

	public String getOffic_type() {
		return offic_type;
	}

	public void setOffic_type(String offic_type) {
		this.offic_type = offic_type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	
}
