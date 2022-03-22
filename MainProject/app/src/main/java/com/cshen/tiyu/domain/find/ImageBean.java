package com.cshen.tiyu.domain.find;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class ImageBean implements  Serializable {
	private String  ImageId;
	private String imageUrl;
	private String imageName;
	private long attchmentAllLength;
	//private ArrayList<File> fileList;
	private File file;

	

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	/*public ArrayList<File> getFileList() {
		return fileList;
	}

	public void setFileList(ArrayList<File> fileList) {
		this.fileList = fileList;
	}*/

	public long getAttchmentAllLength() {
		return attchmentAllLength;
	}

	public void setAttchmentAllLength(long attchmentAllLength) {
		this.attchmentAllLength = attchmentAllLength;
	}

	public String getImageId() {
		return ImageId;
	}

	public void setImageId(String imageId) {
		ImageId = imageId;
	}

	

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

}
