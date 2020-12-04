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

import com.globits.core.domain.BaseObject;
import com.globits.core.domain.Person;

@Entity
@Table(name="tbl_signature")
public class Signature extends BaseObject{
	private static final long serialVersionUID = 1L; 
	
	@Column(name="line_One")
	private String lineOne; 
	
	@Column(name="line_Two")
	private String lineTwo;  
	
	@Column(name="line_Three")
	private String lineThree;  

	@Column(name="sign_Name")
	private String signName;

	public String getLineOne() {
		return lineOne;
	}

	public void setLineOne(String lineOne) {
		this.lineOne = lineOne;
	}

	public String getLineTwo() {
		return lineTwo;
	}

	public void setLineTwo(String lineTwo) {
		this.lineTwo = lineTwo;
	}

	public String getLineThree() {
		return lineThree;
	}

	public void setLineThree(String lineThree) {
		this.lineThree = lineThree;
	}

	public String getSignName() {
		return signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}  
	
	
	
	
}
