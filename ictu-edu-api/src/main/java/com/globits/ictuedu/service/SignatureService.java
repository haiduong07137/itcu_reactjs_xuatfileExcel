package com.globits.ictuedu.service;
 
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService; 
import com.globits.ictuedu.domain.Signature;
import com.globits.ictuedu.dto.SignatureDto;
import com.globits.ictuedu.dto.searchdto.SignatureSearchDto; 

public interface SignatureService extends GenericService<Signature, UUID>{

	Page<SignatureDto> searchByPage(SignatureSearchDto dto);
	
	SignatureDto getOne(UUID id);
	
	SignatureDto saveOneOrUpdateExcel(SignatureDto dto, UUID id);
	
	public void deleteById(UUID id);
	
 
}
