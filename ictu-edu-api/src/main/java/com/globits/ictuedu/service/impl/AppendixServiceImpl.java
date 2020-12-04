package com.globits.ictuedu.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Query;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.ictuedu.domain.Appendix;
import com.globits.ictuedu.domain.Student;
import com.globits.ictuedu.domain.Transcript;
import com.globits.ictuedu.dto.AppendixDto;
import com.globits.ictuedu.dto.ClassMajorsDto;
import com.globits.ictuedu.dto.StudentDto;
import com.globits.ictuedu.dto.TranscriptDto;
import com.globits.ictuedu.dto.searchdto.AppendixSearchDto;
import com.globits.ictuedu.dto.searchdto.SearchDto;
import com.globits.ictuedu.dto.searchdto.StudentSearchDto;
import com.globits.ictuedu.repository.AppendixRepository;
import com.globits.ictuedu.repository.StudentRepository;
import com.globits.ictuedu.repository.TranscriptRepository;
import com.globits.ictuedu.service.AppendixService;
import com.globits.ictuedu.service.StudentService;

@Service
public class AppendixServiceImpl extends GenericServiceImpl<Appendix, UUID> implements AppendixService {

	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	AppendixRepository appendixRepository;

	@Autowired
	TranscriptRepository transcriptRepository;

	@Override
	public Page<AppendixDto> searchByPage(AppendixSearchDto dto) {
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

		String sqlCount = "select count(entity.id) from Appendix as entity where (1=1)";
		String sql = "select new com.globits.ictuedu.dto.AppendixDto(entity) from Appendix as entity where (1=1)";

		if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
			whereClause += " AND ( entity.fullName LIKE :text OR entity.code LIKE :text OR entity.studentClass LIKE :text  )";
		}
		 

		if (dto.getStudentClass() != null && StringUtils.hasText(dto.getStudentClass())) {
			whereClause += " AND ( entity.studentClass LIKE :class  )";
		}
		
		if (dto.getMajors() != null && StringUtils.hasText(dto.getMajors())) {
			whereClause += " AND ( entity.majors LIKE :major  )";
		}
		
		
		sql += whereClause + orderBy;
		sqlCount += whereClause;

		Query q = manager.createQuery(sql, AppendixDto.class);
		Query qCount = manager.createQuery(sqlCount);

		
		
		if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
			q.setParameter("text", '%' + dto.getKeyword() + '%');
			qCount.setParameter("text", '%' + dto.getKeyword() + '%');
		}

		if (dto.getStudentClass() != null && StringUtils.hasText(dto.getStudentClass())) {
			q.setParameter("class", '%' + dto.getStudentClass() + '%');
			qCount.setParameter("class", '%' + dto.getStudentClass() + '%');
		}
		
		if (dto.getMajors() != null && StringUtils.hasText(dto.getMajors())) {
			q.setParameter("major", '%' + dto.getMajors() + '%');
			qCount.setParameter("major", '%' + dto.getMajors() + '%');
		}
		
		int startPosition = pageIndex * pageSize;
		q.setFirstResult(startPosition);
		q.setMaxResults(pageSize);
		List<AppendixDto> entities = q.getResultList();
		long count = (long) qCount.getSingleResult();

		Pageable pageable = PageRequest.of(pageIndex, pageSize);
		Page<AppendixDto> result = new PageImpl<AppendixDto>(entities, pageable, count);
		return result;
	}

	@Override
	public AppendixDto getOne(UUID id) {
		Appendix entity = appendixRepository.getOne(id);

		if (entity != null) {
			return new AppendixDto(entity);
		}

		return null;
	}

	@Override
	public AppendixDto saveOneOrUpdateExcel(AppendixDto dto, UUID id) {
		if (dto != null) {
			Appendix entity = null;
			if (id != null) {
				if (dto.getId() != null && !dto.getId().equals(id)) {
					return null;
				}
				entity = appendixRepository.getOne(id);
				transcriptRepository.deleteAllTranscripts(id);
			}
			if (entity == null) {
				entity = new Appendix();
			}

			/* Set all the values */
			entity.setAllCredit(dto.getAllCredit());
			entity.setBirthday(dto.getBirthday());
			entity.setCode(dto.getCode());
			entity.setDateJoin(dto.getDateJoin());
			entity.setLevelTraining(dto.getLevelTraining());
			entity.setEthnicity(dto.getEthnicity());
			entity.setFullName(dto.getFullName());
			entity.setGender(dto.getGender());
			entity.setMajors(dto.getMajors());
			entity.setNationality(dto.getNationality());
			entity.setNote(dto.getNote());
			entity.setNumberDiploma(dto.getNumberDiploma());
			entity.setPlaceOfBirth(dto.getPlaceOfBirth());
			entity.setRankGraduating(dto.getRankGraduating());
			entity.setScoreFour(dto.getScoreFour());
			entity.setScoreTen(dto.getScoreTen());
			entity.setSpecialized(dto.getSpecialized());
			entity.setStudentClass(dto.getStudentClass());
			entity.setTrainingSystem(dto.getTrainingSystem());
			entity.setTypeTraining(dto.getTypeTraining()); 
			entity.setTrainingLanguage(entity.getTrainingLanguage());
			entity.setDateOut(dto.getDateOut());
			entity = appendixRepository.save(entity);
			return new AppendixDto(entity);
		}
		return null;
	}

	@Override
	public void deleteById(UUID id) {
		appendixRepository.deleteById(id);
	}
	@Override
	public List<StudentDto> checkDuplicateStudentCode(List<String> codes) { 
		String sql = "select new com.globits.ictuedu.dto.StudentDto(entity)  FROM Student entity where entity.code in (:code) ";
		Query q = manager.createQuery(sql, StudentDto.class); 
		q.setParameter("code",codes); 
		List<StudentDto> entities = q.getResultList();
		return entities;
	}
	
	@Override
	public List<AppendixDto> checkDuplicateStudentCodeAppendix(List<String> codes) { 
		String sql = "select new com.globits.ictuedu.dto.AppendixDto(entity)  FROM Appendix entity where entity.code in (:code) ";
		Query q = manager.createQuery(sql, AppendixDto.class); 
		q.setParameter("code",codes); 
		List<AppendixDto> entities = q.getResultList();
		return entities;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public ClassMajorsDto getMajorsAndClass() {
		ClassMajorsDto dto = new ClassMajorsDto(); 
		String sqlMajors = "SELECT DISTINCT entity.majors from Appendix as entity where (1=1)";
		Query qMajor = manager.createQuery(sqlMajors, String.class);
		List<String> listMajors = qMajor.getResultList(); 
		String sqlClass = "SELECT DISTINCT entity.studentClass from Appendix as entity where (1=1)";
		Query qClass = manager.createQuery(sqlClass, String.class);
		List<String> listClass = qClass.getResultList();  
		dto.setListClass(listClass);
		dto.setListMajors(listMajors);
		return dto;
	}
	public void deleteMultiple(List<UUID> idList) {
		for (UUID id : idList) {
			appendixRepository.deleteById(id);
		}
	}

	public void deleteAll() {
		appendixRepository.deleteAll();
	}

}
