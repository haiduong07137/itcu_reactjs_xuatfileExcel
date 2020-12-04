package com.globits.ictuedu.service.impl;

import java.util.List;
import java.util.UUID;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.ictuedu.domain.UploadActivityLog;
import com.globits.ictuedu.dto.UploadActivityLogDto;
import com.globits.ictuedu.dto.searchdto.UploadActivityLogSearchDto;
import com.globits.ictuedu.repository.UploadActivityLogRepository;
import com.globits.ictuedu.service.UploadActivityLogService;

@Service
public class UploadActivityLogServiceImpl extends GenericServiceImpl<UploadActivityLog, UUID> implements UploadActivityLogService {

	@Autowired
	UploadActivityLogRepository uploadActivityLogRepository;
	
	@Override
	public Page<UploadActivityLogDto> searchByPage(UploadActivityLogSearchDto dto) {
		if (dto == null) {
			return null;
		}

		int pageIndex = dto.getPageIndex();
		int pageSize = dto.getPageSize();

		if (pageIndex > 0) {
			pageIndex--;
		} else {
			pageIndex = 0;
		}

		String whereClause = "";

		String orderBy = " ORDER BY entity.createDate DESC";

		String sqlCount = "select count(entity.id) from UploadActivityLog as entity where (1=1)";
		String sql = "select new com.globits.ictuedu.dto.UploadActivityLogDto(entity) from UploadActivityLog as entity where (1=1)";

		if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
			whereClause += " AND ( entity.fileName LIKE :text )";
		}

		sql += whereClause + orderBy;
		sqlCount += whereClause;

		Query q = manager.createQuery(sql, UploadActivityLogDto.class);
		Query qCount = manager.createQuery(sqlCount);

		if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
			q.setParameter("text", '%' + dto.getKeyword() + '%');
			qCount.setParameter("text", '%' + dto.getKeyword() + '%');
		}

		int startPosition = pageIndex * pageSize;
		q.setFirstResult(startPosition);
		q.setMaxResults(pageSize);
		List<UploadActivityLogDto> entities = q.getResultList();
		long count = (long) qCount.getSingleResult(); 
		Pageable pageable = PageRequest.of(pageIndex, pageSize);
		Page<UploadActivityLogDto> result = new PageImpl<UploadActivityLogDto>(entities, pageable, count);
		return result;
	}

	@Override
	public UploadActivityLogDto getOne(UUID id) {
		UploadActivityLog entity = uploadActivityLogRepository.getOne(id);

		if (entity != null) {
			return new UploadActivityLogDto(entity);
		}

		return null;
	}

	@Override
	public UploadActivityLogDto saveOneOrUpdate(UploadActivityLogDto dto, UUID id) {
		if (dto != null) {
			UploadActivityLog entity = null;
			if (id != null) {
				if (dto.getId() != null && !dto.getId().equals(id)) {
					return null;
				}
				entity = uploadActivityLogRepository.getOne(id);
			}
			if (entity == null) {
				entity = new UploadActivityLog();
			}

			/* Set all the values */
			entity.setUploadTime(dto.getUploadTime());
			entity.setFileName(dto.getFileName());
			entity.setStatus(dto.getStatus());
			entity.setLogDetail(dto.getLogDetail());
			entity = uploadActivityLogRepository.save(entity);
			if (entity != null) {
				return new UploadActivityLogDto(entity);
			}
		}
		return null;
	}

	@Override
	public void deleteById(UUID id) {
		uploadActivityLogRepository.deleteById(id);
	}

}
