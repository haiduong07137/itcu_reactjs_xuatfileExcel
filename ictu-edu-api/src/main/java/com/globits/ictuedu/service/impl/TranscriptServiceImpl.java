package com.globits.ictuedu.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.ictuedu.domain.Student;
import com.globits.ictuedu.domain.Transcript;
import com.globits.ictuedu.dto.ClassMajorsDto;
import com.globits.ictuedu.dto.StudentDto;
import com.globits.ictuedu.dto.TranscriptDto;
import com.globits.ictuedu.dto.searchdto.SearchDto;
import com.globits.ictuedu.dto.searchdto.StudentSearchDto;
import com.globits.ictuedu.dto.searchdto.TranscriptSearchDto;
import com.globits.ictuedu.repository.StudentRepository;
import com.globits.ictuedu.repository.TranscriptRepository;
import com.globits.ictuedu.service.StudentService;
import com.globits.ictuedu.service.TranscriptService;

@Service

public class TranscriptServiceImpl extends GenericServiceImpl<Transcript, UUID> implements TranscriptService {

	@Autowired
	StudentRepository studentRepository;

	@Autowired
	TranscriptRepository transcriptRepository;

	@Override
	public Page<TranscriptDto> searchByPage(TranscriptSearchDto dto) {
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

		String orderBy = " ORDER BY entity.moduleOrder asc";

		String sqlCount = "select count(entity.id) from Transcript as entity where (1=1)";
		String sql = "select new com.globits.ictuedu.dto.TranscriptDto(entity) from Transcript as entity where (1=1)";

		if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
			whereClause += " AND ( entity.student.displayName LIKE :text OR entity.student.code LIKE :text )";
		}

		sql += whereClause + orderBy;
		sqlCount += whereClause;

		Query q = manager.createQuery(sql, TranscriptDto.class);
		Query qCount = manager.createQuery(sqlCount);

		if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
			q.setParameter("text", '%' + dto.getKeyword() + '%');
			qCount.setParameter("text", '%' + dto.getKeyword() + '%');
		} 
		int startPosition = pageIndex * pageSize;
		q.setFirstResult(startPosition);
		q.setMaxResults(pageSize);
		List<TranscriptDto> entities = q.getResultList();
		long count = (long) qCount.getSingleResult();
		Pageable pageable = PageRequest.of(pageIndex, pageSize);
		Page<TranscriptDto> result = new PageImpl<TranscriptDto>(entities, pageable, count);
		return result;
	}

	@Override
	public TranscriptDto getOne(UUID id) {
		Transcript entity = transcriptRepository.getOne(id);

		if (entity != null) {
			return new TranscriptDto(entity);
		}

		return null;
	}

	@Override
	public TranscriptDto saveOneOrUpdate(TranscriptDto dto, UUID id, UUID idStudent) {
		if (dto != null) {
			Transcript entity = null;
			if (id != null) {
				if (dto.getId() != null && !dto.getId().equals(id)) {
					return null;
				}
				entity = transcriptRepository.getOne(id);
				Student sv = studentRepository.getOne(idStudent);
				entity.setStudent(sv);
			}
			if (entity == null) {
				entity = new Transcript();
			}

			/* Set all the values */
			entity.setMark(dto.getMark());
			entity.setScore(dto.getScore());
			entity.setModule(dto.getModule());
			entity.setCredit(dto.getCredit());
			entity = transcriptRepository.save(entity);

		}
		return null;
	}

	@Override
	public void deleteById(UUID id) {
		transcriptRepository.deleteById(id);
	}

	@Override
	public Page<TranscriptDto> getScoreByStudentCode(StudentSearchDto dto) {
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

		String orderBy = " ORDER BY entity.moduleOrder ASC";

		String sqlCount = "select count(entity.id) from Transcript as entity where (1=1) AND (  entity.student.code = :code )";
		String sql = "select new com.globits.ictuedu.dto.TranscriptDto(entity) from Transcript as entity where (1=1) AND (  entity.student.code = :code )";

		if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
			whereClause += " AND ( entity.module LIKE :text )";
		}

		sql += whereClause + orderBy;
		sqlCount += whereClause;

		Query q = manager.createQuery(sql, TranscriptDto.class);
		Query qCount = manager.createQuery(sqlCount);
		q.setParameter("code", dto.getCode());
		qCount.setParameter("code", dto.getCode());
		if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
			q.setParameter("text", '%' + dto.getKeyword() + '%');
			qCount.setParameter("text", '%' + dto.getKeyword() + '%');
		}

		int startPosition = pageIndex * pageSize;
		q.setFirstResult(startPosition);
		q.setMaxResults(pageSize);
		List<TranscriptDto> entities = q.getResultList();
		long count = (long) qCount.getSingleResult();
		Pageable pageable = PageRequest.of(pageIndex, pageSize);
		Page<TranscriptDto> result = new PageImpl<TranscriptDto>(entities, pageable, count);
		return result;
	}

	public List<Transcript> getListTranscriptByStudent(String code) {

		String sql = " from Transcript as entity where (1=1) AND (  entity.student.code = :code ) ORDER BY entity.moduleOrder ASC";
		
		Query q = manager.createQuery(sql, Transcript.class);
		q.setParameter("code", code);
		List<Transcript> entities = q.getResultList();
		return entities;
	}

}
