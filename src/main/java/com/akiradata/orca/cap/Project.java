package com.akiradata.orca.cap;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="project")
public abstract class Project {

	@XmlElement(name="name")
	String name;
	
	@XmlElement(name="uuid")
	String uuid;
	
	@XmlElement(name="children")
	List<Item> children = new LinkedList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public void addChildren(Item child){
		this.children.add(child);
	}
}
