package com.globits.ictuedu.dto.searchdto;

import java.util.UUID;

import com.globits.ictuedu.dto.AppendixDto;
import com.globits.ictuedu.dto.SignatureDto;
import com.globits.ictuedu.dto.StudentDto;

public class SignatureSearchDto extends SignatureDto {
	private UUID id;
	private int pageIndex;
	private int pageSize;

	private String name;
	private String code;
	private String keyword;
	private Integer dateSearch; 
	 private  String  dateExport; 
	  
	 

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
