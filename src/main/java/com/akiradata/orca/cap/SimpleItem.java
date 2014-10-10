package com.akiradata.orca.cap;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.imageio.IIOImage;

public class SimpleItem extends Item {

	IIOImage image;

	UUID uuid;
	
	List<Segment> segments = new LinkedList<Segment>();

	public IIOImage getImage() {
		return image;
	}

	public void setImage(IIOImage image) {
		this.image = image;
	}
	
	public void addSegment(Segment segment){
		this.segments.add(segment);
	}
	
	public UUID getUUID(){
		return this.uuid;
	}
	
	public void setUUID(UUID i){
		this.uuid = i;
	}
}
