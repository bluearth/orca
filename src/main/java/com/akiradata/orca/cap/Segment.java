package com.akiradata.orca.cap;

import javafx.scene.shape.Path;

public class Segment {
	
	String label;
	
	Path path;
	
	StringBuffer content = new StringBuffer();

	public void setPath(Path p){
		this.path = p;
	}
	
	public Path getPath(){
		return this.path;
	}
	
	public void setContent(String content){
		this.content = new StringBuffer(content);
	}
	
	public String getContent(){
		return content.toString();
	}

}
