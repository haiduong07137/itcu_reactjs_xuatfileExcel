package com.globits.ictuedu.dto;

import com.globits.core.dto.BaseObjectDto;
import com.globits.ictuedu.domain.Student;
import com.globits.ictuedu.domain.Transcript;

public class TranscriptDto extends BaseObjectDto {
	private StudentDto student;	//sinh vien
	
	private String module; // Học phần
	
	private String credit; // tín chỉ
	
	private String score; // Diem so
	
	private String mark; // diem chu
	
	private Double moduleOrder; // sap xep mon    
	
 

	public Double getModuleOrder() {
		return moduleOrder;
	}

	public void setModuleOrder(Double moduleOrder) {
		this.moduleOrder = moduleOrder;
	}

	public StudentDto getStudent() {
		return student;
	}

	public void setStudent(StudentDto student) {
		this.student = student;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public TranscriptDto() {
		
	}
	
	public TranscriptDto(Transcript entity) {
		if (entity != null) {
			this.id = entity.getId();
			this.moduleOrder=entity.getModuleOrder();
			this.student = new StudentDto(entity.getStudent(), true);
			this.module = entity.getModule();
			this.credit = entity.getCredit();
			this.score = entity.getScore();
			this.mark = entity.getMark();
			
		}
	}
}
