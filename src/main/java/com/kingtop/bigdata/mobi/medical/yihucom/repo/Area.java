package com.kingtop.bigdata.mobi.medical.yihucom.repo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;



@SuppressWarnings("serial")
public class Area implements Serializable{
	
	private int level;
	
	private String name;
	
	private String belong;
	
	private String url;

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBelong() {
		return belong;
	}

	public void setBelong(String belong) {
		this.belong = belong;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	
}
