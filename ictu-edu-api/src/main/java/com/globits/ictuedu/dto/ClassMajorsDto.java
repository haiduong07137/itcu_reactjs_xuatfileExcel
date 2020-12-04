package com.globits.ictuedu.dto;
 
import java.util.List; 
 
public class ClassMajorsDto  {
	 private List<String> listMajors;
	 private List<String> listClass;
	 private List<String> listFromFileUpload;
	 private List<String> listTrainingSystem;
	 
	 
	 
	public List<String> getListTrainingSystem() {
		return listTrainingSystem;
	}
	public void setListTrainingSystem(List<String> listTrainingSystem) {
		this.listTrainingSystem = listTrainingSystem;
	}
	public List<String> getListFromFileUpload() {
		return listFromFileUpload;
	}
	public void setListFromFileUpload(List<String> listFromFileUpload) {
		this.listFromFileUpload = listFromFileUpload;
	}
	public List<String> getListMajors() {
		return listMajors;
	}
	public void setListMajors(List<String> listMajors) {
		this.listMajors = listMajors;
	}
	public List<String> getListClass() {
		return listClass;
	}
	public void setListClass(List<String> listClass) {
		this.listClass = listClass;
	}
	 
	 
}
