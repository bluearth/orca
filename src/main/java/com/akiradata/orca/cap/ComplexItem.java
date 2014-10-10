package com.akiradata.orca.cap;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class ComplexItem extends Item {

	@XmlElement(name="children")
	List<Item> children = new LinkedList<>();
	
	public void addChildren(Item child){
		this.children.add(child);
	}
	
}
