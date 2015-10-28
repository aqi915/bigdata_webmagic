package com.kingtop.bigdata.mobi.medical.yihucom.repo;

import lombok.Getter;
import lombok.Setter;

public class Doctor {

	
	private String hospital;
	
	private String hospital_url;
	
	private String office_type;
	
	private String office;
	
	private String office_url;
	
	private String url;
	
	private String name;
	
	//职称
	private String title;
	
	private String intro_url;
	
	//擅长
	private String skill;
	
	private String desc;

	private String source;
	
	public String getSource() {
		return source;
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

	public String getOffice_type() {
		return office_type;
	}

	public void setOffice_type(String office_type) {
		this.office_type = office_type;
	}

	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public String getOffice_url() {
		return office_url;
	}

	public void setOffice_url(String office_url) {
		this.office_url = office_url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIntro_url() {
		return intro_url;
	}

	public void setIntro_url(String intro_url) {
		this.intro_url = intro_url;
	}

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setSource(String source) {
		this.source = source;
		
	}




	
}
