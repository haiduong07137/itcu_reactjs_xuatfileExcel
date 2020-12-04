package com.globits.ictuedu.dto;

import javax.persistence.Column;

import com.globits.core.dto.BaseObjectDto;
import com.globits.ictuedu.domain.Appendix;
import com.globits.ictuedu.domain.Student;
import com.globits.ictuedu.domain.Transcript;

public class AppendixDto extends BaseObjectDto {
	private String code; // Mã

	private String fullName;

	private String birthday;

	private String gender; // gioiTInh

	private String placeOfBirth; // Nơi sinh

	private String ethnicity; // dan toc

	private String nationality; // So hieu bang

	private String allCredit; // tong so tin chi

	private String scoreTen; // thang diem 10

	private String scoreFour;// thang diem 4

	private String rankGraduating;// xep loai tot nghiep

	private String studentClass; // Lop hoc

	private String trainingSystem; // He dao tao

	private String majors; // Nganh

	private String specialized; // Chuyen nganh
 
	private String levelTraining; // trinh do dao tao

	private String typeTraining; // So hieu bang

	private String dateJoin; // ngay nhap hoc

	private String numberDiploma; // So hieu bang

	private String note; // ghi chu

	private String fromUploadedFile; // file upload sv
	 
	private String trainingLanguage; // ngon ngu dao tao
	 
	private String dateOut; // ngay tot nghiep

	
	public String getTrainingLanguage() {
		return trainingLanguage;
	}

	public void setTrainingLanguage(String trainingLanguage) {
		this.trainingLanguage = trainingLanguage;
	}

	public String getDateOut() {
		return dateOut;
	}

	public void setDateOut(String dateOut) {
		this.dateOut = dateOut;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPlaceOfBirth() {
		return placeOfBirth;
	}

	public void setPlaceOfBirth(String placeOfBirth) {
		this.placeOfBirth = placeOfBirth;
	}

	public String getEthnicity() {
		return ethnicity;
	}

	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getAllCredit() {
		return allCredit;
	}

	public void setAllCredit(String allCredit) {
		this.allCredit = allCredit;
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

	public String getStudentClass() {
		return studentClass;
	}

	public void setStudentClass(String studentClass) {
		this.studentClass = studentClass;
	}

	public String getTrainingSystem() {
		return trainingSystem;
	}

	public void setTrainingSystem(String trainingSystem) {
		this.trainingSystem = trainingSystem;
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

	 
 
	public String getLevelTraining() {
		return levelTraining;
	}

	public void setLevelTraining(String levelTraining) {
		this.levelTraining = levelTraining;
	}

	public String getTypeTraining() {
		return typeTraining;
	}

	public void setTypeTraining(String typeTraining) {
		this.typeTraining = typeTraining;
	}

	public String getDateJoin() {
		return dateJoin;
	}

	public void setDateJoin(String dateJoin) {
		this.dateJoin = dateJoin;
	}

	public String getNumberDiploma() {
		return numberDiploma;
	}

	public void setNumberDiploma(String numberDiploma) {
		this.numberDiploma = numberDiploma;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getFromUploadedFile() {
		return fromUploadedFile;
	}

	public void setFromUploadedFile(String fromUploadedFile) {
		this.fromUploadedFile = fromUploadedFile;
	}

	
	public AppendixDto() {
		// TODO Auto-generated constructor stub
	}

	public AppendixDto(Appendix entity) { 
		this.id=entity.getId();
		this.code = entity.getCode();
		this.fullName = entity.getFullName();
		this.birthday = entity.getBirthday();
		this.gender = entity.getGender();
		this.placeOfBirth = entity.getPlaceOfBirth();
		this.ethnicity = entity.getEthnicity();
		this.nationality = entity.getNationality();
		this.allCredit = entity.getAllCredit();
		this.scoreTen = entity.getScoreTen();
		this.scoreFour = entity.getScoreFour();
		this.rankGraduating = entity.getRankGraduating();
		this.studentClass = entity.getStudentClass();
		this.trainingSystem = entity.getTrainingSystem();
		this.majors = entity.getMajors();
		this.specialized = entity.getSpecialized(); 
		this.levelTraining = entity.getLevelTraining();
		this.typeTraining = entity.getTypeTraining();
		this.dateJoin = entity.getDateJoin();
		this.numberDiploma = entity.getNumberDiploma();
		this.note = entity.getNote();
		this.fromUploadedFile = entity.getFromUploadedFile();
		this.dateOut=entity.getDateOut();
		this.trainingLanguage=entity.getTrainingLanguage();
	}
	
	
}
