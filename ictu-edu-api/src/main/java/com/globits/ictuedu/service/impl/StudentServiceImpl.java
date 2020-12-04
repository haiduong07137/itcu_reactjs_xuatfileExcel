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
import com.globits.ictuedu.domain.Student;
import com.globits.ictuedu.domain.Transcript;
import com.globits.ictuedu.dto.ClassMajorsDto;
import com.globits.ictuedu.dto.StudentDto;
import com.globits.ictuedu.dto.TranscriptDto;
import com.globits.ictuedu.dto.searchdto.SearchDto;
import com.globits.ictuedu.dto.searchdto.StudentSearchDto;
import com.globits.ictuedu.repository.StudentRepository;
import com.globits.ictuedu.repository.TranscriptRepository;
import com.globits.ictuedu.service.StudentService;

@Service
public class StudentServiceImpl extends GenericServiceImpl<Student, UUID> implements StudentService {

	@Autowired
	StudentRepository studentRepository;

	@Autowired
	TranscriptRepository transcriptRepository;

	@Override
	public Page<StudentDto> searchByPage(StudentSearchDto dto) {
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

		String sqlCount = "select count(entity.id) from Student as entity where (1=1)";
		String sql = "select new com.globits.ictuedu.dto.StudentDto(entity) from Student as entity where (1=1)";

		if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
			whereClause += " AND ( entity.displayName LIKE :text OR entity.code LIKE :text )";
		}
		
		if (dto.getStudentClass() != null && StringUtils.hasText(dto.getStudentClass())) {
			whereClause += " AND ( entity.studentClass LIKE :class  )";
		}
		
		if (dto.getMajors() != null && StringUtils.hasText(dto.getMajors())) {
			whereClause += " AND ( entity.majors LIKE :major  )";
		}

		sql += whereClause + orderBy;
		sqlCount += whereClause;

		Query q = manager.createQuery(sql, StudentDto.class);
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
		List<StudentDto> entities = q.getResultList();
		long count = (long) qCount.getSingleResult();

		Pageable pageable = PageRequest.of(pageIndex, pageSize);
		Page<StudentDto> result = new PageImpl<StudentDto>(entities, pageable, count);
		return result;
	}

	@Override
	public StudentDto getOne(UUID id) {
		Student entity = studentRepository.getOne(id);

		if (entity != null) {
			return new StudentDto(entity);
		}

		return null;
	}

	@Override
	public StudentDto saveOneOrUpdateExcel(StudentDto dto, UUID id) {
		if (dto != null) {
			Student entity = null;
			if (id != null) {
				if (dto.getId() != null && !dto.getId().equals(id)) {
					return null;
				}
				entity = studentRepository.getOne(id);
//				transcriptRepository.deleteAllTranscripts(id);
			}
			if (entity == null) {
				entity = new Student();
			}

			/* Set all the values */
			entity.setDisplayName(dto.getDisplayName());
			entity.setCode(dto.getCode());
			entity.setPlaceOfBirth(dto.getPlaceOfBirth());
			entity.setBirthPlace(dto.getBirthPlace());
			entity.setBirthDate(dto.getBirthDate());
			entity.setSpecialized(dto.getSpecialized());
			entity.setStudentClass(dto.getStudentClass());
			entity.setCourse(dto.getCourse());
			entity.setRankConduct(dto.getRankConduct());
			entity.setMajors(dto.getMajors());
			entity.setTrainingSystem(dto.getTrainingSystem());
			entity.setFromUploadedFile(dto.getFromUploadedFile());
			entity.setScoreFour(dto.getScoreFour());
			entity.setScoreTen(dto.getScoreTen());
			entity.setRankGraduating(dto.getRankGraduating());
			entity = studentRepository.save(entity);
			return new StudentDto(entity);
		}
		return null;
	}

	@Override
	public void deleteById(UUID id) {
		studentRepository.deleteById(id);
	}

	@Override
	public UUID getStudentIdFromCode(String studentCode) {
		Student dto = studentRepository.getStudentFromCode(studentCode);
		if (dto != null && dto.getId() != null) {
			return dto.getId();
		} else {
			return null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public ClassMajorsDto getMajorsAndClass() {
		ClassMajorsDto dto = new ClassMajorsDto(); 
		String sqlMajors = "SELECT DISTINCT entity.majors from Student as entity where (1=1)";
		Query qMajor = manager.createQuery(sqlMajors, String.class);
		List<String> listMajors = qMajor.getResultList(); 
		String sqlClass = "SELECT DISTINCT entity.studentClass from Student as entity where (1=1)";
		Query qClass = manager.createQuery(sqlClass, String.class);
		List<String> listClass = qClass.getResultList();  
		dto.setListClass(listClass);
		dto.setListMajors(listMajors);
		return dto;
	}

	@Override
	public Page<StudentDto> searchByPageDuplicateStudentCode(StudentSearchDto dto) {
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

		String orderBy = "   ORDER BY entity.code DESC";

		String sqlCount = "select count(entity.id)   FROM Student entity where entity.code in ( SELECT entity.code  FROM   Student entity  GROUP  BY entity.code HAVING COUNT(entity.code) > 1 )  ";
		String sql = "select new com.globits.ictuedu.dto.StudentDto(entity)  FROM Student entity where entity.code in ( SELECT entity.code  FROM   Student entity  GROUP  BY entity.code HAVING COUNT(entity.code) > 1 ) ";

		if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
			whereClause += " AND ( entity.displayName LIKE :text OR entity.code LIKE :text )";
		}
		
		if (dto.getStudentClass() != null && StringUtils.hasText(dto.getStudentClass())) {
			whereClause += " AND ( entity.studentClass LIKE :class  )";
		}
		
		if (dto.getMajors() != null && StringUtils.hasText(dto.getMajors())) {
			whereClause += " AND ( entity.majors LIKE :major  )";
		}

		sql += whereClause + orderBy;
		sqlCount += whereClause;

		Query q = manager.createQuery(sql, StudentDto.class);
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
		List<StudentDto> entities = q.getResultList();
		long count = (long) qCount.getSingleResult();

		Pageable pageable = PageRequest.of(pageIndex, pageSize);
		Page<StudentDto> result = new PageImpl<StudentDto>(entities, pageable, count);
		return result;
	}

	@Override
	public List<StudentDto> checkDuplicateStudentCode(List<String> codes) { 
		String sql = "select new com.globits.ictuedu.dto.StudentDto(entity)  FROM Student entity where entity.code in (:code) ";
		Query q = manager.createQuery(sql, StudentDto.class); 
		q.setParameter("code",codes); 
		List<StudentDto> entities = q.getResultList();
		return entities;
	}

	public void deleteMultiple(List<UUID> idList) {
		for (UUID id : idList) {
			studentRepository.deleteById(id);
		}
	}

	public void deleteAll() {
		studentRepository.deleteAll();
	}

}
