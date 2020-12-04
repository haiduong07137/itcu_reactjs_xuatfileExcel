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
import com.globits.ictuedu.domain.Signature;
import com.globits.ictuedu.domain.Student;
import com.globits.ictuedu.domain.Transcript;
import com.globits.ictuedu.dto.AppendixDto;
import com.globits.ictuedu.dto.ClassMajorsDto;
import com.globits.ictuedu.dto.SignatureDto;
import com.globits.ictuedu.dto.StudentDto;
import com.globits.ictuedu.dto.TranscriptDto;
import com.globits.ictuedu.dto.searchdto.AppendixSearchDto;
import com.globits.ictuedu.dto.searchdto.SearchDto;
import com.globits.ictuedu.dto.searchdto.SignatureSearchDto;
import com.globits.ictuedu.dto.searchdto.StudentSearchDto;
import com.globits.ictuedu.repository.AppendixRepository;
import com.globits.ictuedu.repository.SignatureRepository;
import com.globits.ictuedu.repository.StudentRepository;
import com.globits.ictuedu.repository.TranscriptRepository;
import com.globits.ictuedu.service.AppendixService;
import com.globits.ictuedu.service.SignatureService;
import com.globits.ictuedu.service.StudentService;

@Service
public class SignatureServiceImpl extends GenericServiceImpl<Signature, UUID> implements SignatureService {

	@Autowired
	StudentRepository studentRepository;

	@Autowired
	AppendixRepository appendixRepository;

	@Autowired
	TranscriptRepository transcriptRepository;

	@Autowired
	SignatureRepository signatureRepository;

	@Override
	public Page<SignatureDto> searchByPage(SignatureSearchDto dto) {
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

		String sqlCount = "select count(entity.id) from Signature as entity where (1=1)";
		String sql = "select new com.globits.ictuedu.dto.SignatureDto(entity) from Signature as entity where (1=1)";

		if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
			whereClause += " AND ( entity.signName LIKE :text OR entity.departName LIKE :text OR entity.signPrincipal LIKE :text  )";
		}

		sql += whereClause + orderBy;
		sqlCount += whereClause;

		Query q = manager.createQuery(sql, SignatureDto.class);
		Query qCount = manager.createQuery(sqlCount);

		if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
			q.setParameter("text", '%' + dto.getKeyword() + '%');
			qCount.setParameter("text", '%' + dto.getKeyword() + '%');
		}

		int startPosition = pageIndex * pageSize;
		q.setFirstResult(startPosition);
		q.setMaxResults(999999);
		List<SignatureDto> entities = q.getResultList();
		long count = (long) qCount.getSingleResult();

		Pageable pageable = PageRequest.of(pageIndex, pageSize);
		Page<SignatureDto> result = new PageImpl<SignatureDto>(entities, pageable, count);
		return result;
	}

	@Override
	public SignatureDto getOne(UUID id) {
		Signature entity = signatureRepository.getOne(id);

		if (entity != null) {
			return new SignatureDto(entity);
		}

		return null;
	}

	@Override
	public SignatureDto saveOneOrUpdateExcel(SignatureDto dto, UUID id) {
		if (dto != null) {
			Signature entity = null;
			if (id != null) {
				if (dto.getId() != null && !dto.getId().equals(id)) {
					return null;
				}
				entity = signatureRepository.getOne(id);

			}
			if (entity == null) {
				entity = new Signature();
			}

			/* Set all the values */
			entity.setLineOne(dto.getLineOne());
			entity.setLineTwo(dto.getLineTwo());
			entity.setLineThree(dto.getLineThree());
			entity.setSignName(dto.getSignName());
			signatureRepository.save(entity);
			return new SignatureDto(entity);
		}
		return null;
	}

	@Override
	public void deleteById(UUID id) {
		signatureRepository.deleteById(id);
	}

	public void deleteMultiple(List<UUID> idList) {
		for (UUID id : idList) {
			signatureRepository.deleteById(id);
		}
	}

	public void deleteAll() {
		signatureRepository.deleteAll();
	}

}
