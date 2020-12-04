package com.globits.ictuedu.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.globits.core.domain.Person;

@Entity
@Table(name="tbl_student")
public class Student extends Person{
	private static final long serialVersionUID = 1L;
	@Column(name="code")
	private String code; // Mã
	
	@Column(name="place_of_birth")
	private String placeOfBirth; // Nơi sinh
	
	@Column(name="majors")
	private String majors; // Nganh
	
	@Column(name="specialized")
	private String specialized; // Chuyen nganh
	
	@Column(name="student_class")
	private String studentClass; // Lop hoc
	
	@Column(name="course")
	private String course; //khóa học
	
	@Column(name="training_system")
	private String trainingSystem; //He dao tao
	
	@Column(name="score_ten")
	private String scoreTen; //thang diem 10
	
	@Column(name="score_four")
	private String scoreFour;//thang diem 4
	
	@Column(name="rank_graduating ")
	private String rankGraduating;//xep loai tot nghiep
	
	@Column(name="rank_conduct  ")
	private String rankConduct;//xep loai hanh kiem
	
	@OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@NotFound(action = NotFoundAction.IGNORE)
	private Set<Transcript> transcripts;
	
	@Column(name="from_uploaded_file")
	private String fromUploadedFile;  // file upload sv
  
 
	 
	public String getRankConduct() {
		return rankConduct;
	}

	public void setRankConduct(String rankConduct) {
		this.rankConduct = rankConduct;
	}

	public String getScoreTen() {
		return scoreTen;
	}

	public void setScoreTen(String scoreTen) {
		this.scoreTen = scoreTen;
	}

	public String getScoreFour() {
		return scoreFour;
	}

	public void setScoreFour(String scoreFour) {
		this.scoreFour = scoreFour;
	}

	public String getRankGraduating() {
		return rankGraduating;
	}

	public void setRankGraduating(String rankGraduating) {
		this.rankGraduating = rankGraduating;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPlaceOfBirth() {
		return placeOfBirth;
	}

	public void setPlaceOfBirth(String placeOfBirth) {
		this.placeOfBirth = placeOfBirth;
	}

	public String getMajors() {
		return majors;
	}

	public void setMajors(String majors) {
		this.majors = majors;
	}

	public String getSpecialized() {
		return specialized;
	}

	public void setSpecialized(String specialized) {
		this.specialized = specialized;
	}

	public String getStudentClass() {
		return studentClass;
	}

	public void setStudentClass(String studentClass) {
		this.studentClass = studentClass;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public String getTrainingSystem() {
		return trainingSystem;
	}

	public void setTrainingSystem(String trainingSystem) {
		this.trainingSystem = trainingSystem;
	}

	public Set<Transcript> getTranscripts() {
		return transcripts;
	}

	public void setTranscripts(Set<Transcript> transcripts) {
		this.transcripts = transcripts;
	}

	public String getFromUploadedFile() {
		return fromUploadedFile;
	}

	public void setFromUploadedFile(String fromUploadedFile) {
		this.fromUploadedFile = fromUploadedFile;
	}
	
}
