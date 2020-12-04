package com.globits.ictuedu.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;

import com.globits.core.dto.PersonDto;
import com.globits.ictuedu.domain.Student;
import com.globits.ictuedu.domain.Transcript;

public class StudentDto extends PersonDto {
	private String code; // Mã

	private String placeOfBirth; // Nơi sinh

	private String majors; // Nganh

	private String specialized; // Chuyen nganh

	private String studentClass; // Lop hoc

	private String course; // khóa học

	private String trainingSystem; // He dao tao

	private Set<TranscriptDto> transcripts; // Diem

	private String fromUploadedFile; // Tên file upload

	private String scoreTen; // Thang diem 10

	private String scoreFour; // thang diem 4

	private String rankGraduating; // xep loai tot nghiep

	private String fromSheetName; // nhap tu file nao

	private String rankConduct;
	
	
	 

	public String getRankConduct() {
		return rankConduct;
	}

	public void setRankConduct(String rankConduct) {
		this.rankConduct = rankConduct;
	}

	public String getFromSheetName() {
		return fromSheetName;
	}

	public void setFromSheetName(String fromSheetName) {
		this.fromSheetName = fromSheetName;
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

	public Set<TranscriptDto> getTranscripts() {
		return transcripts;
	}

	public void setTranscripts(Set<TranscriptDto> transcripts) {
		this.transcripts = transcripts;
	}

	public String getFromUploadedFile() {
		return fromUploadedFile;
	}

	public void setFromUploadedFile(String fromUploadedFile) {
		this.fromUploadedFile = fromUploadedFile;
	}

	public StudentDto() { 
	}

	public StudentDto(Student entity) {
		if (entity != null) {
			this.id = entity.getId();
			this.code = entity.getCode();
			this.placeOfBirth = entity.getPlaceOfBirth();
			this.majors = entity.getMajors();
			this.specialized = entity.getSpecialized();
			this.scoreFour = entity.getScoreFour();
			this.scoreTen = entity.getScoreTen();
			this.rankGraduating = entity.getRankGraduating();
			this.studentClass = entity.getStudentClass();
			this.course = entity.getCourse();
			this.trainingSystem = entity.getTrainingSystem();
			this.displayName = entity.getDisplayName();
			this.fromUploadedFile = entity.getFromUploadedFile(); 
			this.rankConduct=entity.getRankConduct();
			if (entity.getTranscripts() != null && entity.getTranscripts().size() > 0) {
				this.transcripts = new HashSet<TranscriptDto>();

				for (Transcript transcript : entity.getTranscripts()) {
					TranscriptDto dto = new TranscriptDto(transcript);
					this.transcripts.add(dto);
				}
			}
		}
	}

	public StudentDto(Student entity, boolean simple) {
		if (entity != null) {
			this.id = entity.getId();
			this.code = entity.getCode();
			this.placeOfBirth = entity.getPlaceOfBirth();
			this.majors = entity.getMajors();
			this.specialized = entity.getSpecialized();
			this.scoreFour = entity.getScoreFour();
			this.scoreTen = entity.getScoreTen();
			this.rankGraduating = entity.getRankGraduating();
			this.studentClass = entity.getStudentClass(); 
			this.course = entity.getCourse();
			this.trainingSystem = entity.getTrainingSystem();
			this.displayName = entity.getDisplayName();
			this.fromUploadedFile = entity.getFromUploadedFile(); 
			this.rankConduct=entity.getRankConduct();
		}
	}
}
