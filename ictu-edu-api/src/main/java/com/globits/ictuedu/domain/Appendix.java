package com.globits.ictuedu.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.globits.core.domain.BaseObject;
import com.globits.core.domain.Person;

@Entity
@Table(name = "tbl_appendix")
public class Appendix extends BaseObject {
	private static final long serialVersionUID = 1L;

	@Column(name = "code")
	private String code; // Mã

	@Column(name = "full_name")
	private String fullName;

	@Column(name = "birthday")
	private String birthday;

	@Column(name = "gender")
	private String gender; // gioiTInh

	@Column(name = "place_of_birth")
	private String placeOfBirth; // Nơi sinh

	@Column(name = "ethnicity")
	private String ethnicity; // dan toc

	@Column(name = "nationality")
	private String nationality; // So hieu bang

	@Column(name = "all_credit")
	private String allCredit; // tong so tin chi

	@Column(name = "score_ten")
	private String scoreTen; // thang diem 10

	@Column(name = "score_four")
	private String scoreFour;// thang diem 4

	@Column(name = "rank_graduating ")
	private String rankGraduating;// xep loai tot nghiep
	
	@Column(name = "student_class")
	private String studentClass; // Lop hoc
	
	@Column(name = "training_system")
	private String trainingSystem; // He dao tao

	@Column(name = "majors")
	private String majors; // Nganh

	@Column(name = "specialized")
	private String specialized; // Chuyen nganh
 

	@Column(name = "level_training")
	private String levelTraining; // thoi gian dao tao
	
	
	@Column(name = "type_training")
	private String typeTraining; // So hieu bang

	@Column(name = "date_join")
	private String dateJoin; // ngay nhap hoc
	
	@Column(name = "date_out")
	private String dateOut; // ngay tot nghiep
	
	@Column(name = "number_diploma")
	private String numberDiploma; // So hieu bang

	@Column(name = "training_language")
	private String trainingLanguage; // So hieu bang

	@Column(name = "note")
	private String note; // ghi chu

	
	@Column(name = "from_uploaded_file")
	private String fromUploadedFile; // file upload sv


	public String getDateOut() {
		return dateOut;
	}


	public void setDateOut(String dateOut) {
		this.dateOut = dateOut;
	}


	public String getTrainingLanguage() {
		return trainingLanguage;
	}


	public void setTrainingLanguage(String trainingLanguage) {
		this.trainingLanguage = trainingLanguage;
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


	public static long getSerialversionuid() {
		return serialVersionUID;
	}





	
	

}
