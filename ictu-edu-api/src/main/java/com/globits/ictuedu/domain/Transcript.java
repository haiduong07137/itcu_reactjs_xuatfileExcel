package com.globits.ictuedu.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.globits.core.domain.BaseObject;
import com.globits.core.domain.Person;

@Entity
@Table(name="tbl_transcript")
public class Transcript extends BaseObject{
	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	@JoinColumn(name="student_id")
	private Student student;	//sinh vien
	
	@Column(name="module")
	private String module; // Học phần
	
	@Column(name="credit")
	private String credit; // tín chỉ
	
	@Column(name="score")
	private String score; // Diem so
	
	@Column(name="mark")
	private String mark; // diem chu
	
	@Column(name="module_Order")
	private Double moduleOrder; // sap xep mon  

	public Double getModuleOrder() {
		return moduleOrder;
	}

	public void setModuleOrder(Double moduleOrder) {
		this.moduleOrder = moduleOrder;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
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
	
	
}
