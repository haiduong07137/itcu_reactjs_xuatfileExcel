package com.globits.ictuedu.service;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.ictuedu.domain.UploadActivityLog;
import com.globits.ictuedu.dto.UploadActivityLogDto;
import com.globits.ictuedu.dto.searchdto.UploadActivityLogSearchDto;

public interface UploadActivityLogService extends GenericService<UploadActivityLog, UUID> {
	Page<UploadActivityLogDto> searchByPage(UploadActivityLogSearchDto dto);
	
	UploadActivityLogDto getOne(UUID id);
	
	UploadActivityLogDto saveOneOrUpdate(UploadActivityLogDto dto, UUID id);
	
	public void deleteById(UUID id);
}
