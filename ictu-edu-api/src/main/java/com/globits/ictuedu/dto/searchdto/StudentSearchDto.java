package com.globits.ictuedu.dto.searchdto;

import java.util.UUID;

import com.globits.ictuedu.dto.StudentDto;

public class StudentSearchDto extends StudentDto {
	private UUID id;
	private int pageIndex;
	private int pageSize;
	private UUID signatureId;
	private String name;
	private String code;
	private String keyword;
	private Integer dateSearch;
	private String dateExport;
	

	private String lineOne;

	private String lineTwo;

	private String lineThree;

	private String signName;
	
private Boolean isFormal;
	
	

	public Boolean getIsFormal() {
		return isFormal;
	}

	public void setIsFormal(Boolean isFormal) {
		this.isFormal = isFormal;
	}

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

	public UUID getSignatureId() {
		return signatureId;
	}

	public void setSignatureId(UUID signatureId) {
		this.signatureId = signatureId;
	}

	public String getDateExport() {
		return dateExport;
	}

	public void setDateExport(String dateExport) {
		this.dateExport = dateExport;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getDateSearch() {
		return dateSearch;
	}

	public void setDateSearch(Integer dateSearch) {
		this.dateSearch = dateSearch;
	}
}
