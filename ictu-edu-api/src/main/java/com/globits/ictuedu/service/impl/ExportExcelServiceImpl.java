package com.globits.ictuedu.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.io.*;
//POI libraries to read Excel File
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.*;
import java.util.Iterator;
import java.io.*;

import org.apache.poi.hssf.model.InternalWorkbook;
import org.apache.poi.hssf.record.ExtendedFormatRecord;
//itext libraries to write PDF file 
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFOptimiser;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xwpf.usermodel.Borders;
import org.jboss.jandex.Main;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.WorkbookDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.globits.ictuedu.Constants;
import com.globits.ictuedu.domain.Appendix;
import com.globits.ictuedu.domain.Student;
import com.globits.ictuedu.domain.Transcript;
import com.globits.ictuedu.dto.AppendixDto;
import com.globits.ictuedu.dto.DownloadStatusDto;
import com.globits.ictuedu.dto.SignAndDateDto;
import com.globits.ictuedu.dto.SignatureDto;
import com.globits.ictuedu.dto.StudentDto;
import com.globits.ictuedu.dto.TranscriptDto;
import com.globits.ictuedu.dto.searchdto.AppendixSearchDto;
import com.globits.ictuedu.dto.searchdto.StudentSearchDto;
import com.globits.ictuedu.repository.AppendixRepository;
import com.globits.ictuedu.repository.StudentRepository;
import com.globits.ictuedu.repository.TranscriptRepository;
import com.globits.ictuedu.service.ExportExcelService;
import com.globits.ictuedu.service.SignatureService;

@Service
public class ExportExcelServiceImpl implements ExportExcelService {

	@Value("${attachment.path}")
	private String attachmentPath;

	@Value("${localhost.path}")
	private String hostPath;

	@Value("${server.servlet.context-path}")
	private String contextPath;

	@Value("${attachment.context.path}")
	private String attachmentContextPath;

	@Autowired
	SignatureService signatureService;

	@Autowired
	StudentRepository studentRepository;

	@Autowired
	AppendixRepository appendixRepository;

	@Autowired
	StudentServiceImpl studentServiceImpl;

	@Autowired
	TranscriptServiceImpl transcriptsServiceImpl;

	@Autowired
	TranscriptRepository transcriptRepository;

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	@Override
	public ByteArrayResource studentToExcel(StudentSearchDto dto) throws IOException {
		DownloadStatusDto statusDto = new DownloadStatusDto();
		List<Student> listStudent = new ArrayList<>();
		Student sv = studentRepository.getOne(dto.getId());
		if (sv != null) {
			listStudent.add(sv);
		}
		ByteArrayOutputStream excelFile = new ByteArrayOutputStream();
		statusDto = exportBangDiemMoi(listStudent, dto, true);
		statusDto.getHssfWb().write(excelFile);
		return new ByteArrayResource(excelFile.toByteArray());

	}

	public DownloadStatusDto checkStatus(StudentSearchDto dto) throws IOException {
		DownloadStatusDto statusDto = new DownloadStatusDto();
		List<Student> listStudent = new ArrayList<>();
		Student sv = studentRepository.getOne(dto.getId());
		if (sv != null) {
			listStudent.add(sv);
		}
		statusDto = exportBangDiemMoi(listStudent, dto, false);
		return statusDto;
	}

	@Override
	public ByteArrayResource allStudentToExcel(StudentSearchDto dto) throws IOException {
		DownloadStatusDto statusDto = new DownloadStatusDto();
		List<Student> listStudent = new ArrayList<>();
		if (dto.getStudentClass() != null && StringUtils.hasText(dto.getStudentClass())) {
			listStudent = studentRepository.getAllStudentByClass(dto.getStudentClass());
		} else if (dto.getMajors() != null && StringUtils.hasText(dto.getMajors())) {
			listStudent = studentRepository.getAllStudentByMajors(dto.getMajors());
		} else {
			listStudent = studentRepository.findAll();
		}
		ByteArrayOutputStream excelFile = new ByteArrayOutputStream();
		statusDto = exportBangDiemMoi(listStudent, dto, true);
		statusDto.getHssfWb().write(excelFile);
		return new ByteArrayResource(excelFile.toByteArray());
	}

	public DownloadStatusDto checkStatusAll(StudentSearchDto dto) throws IOException {
		DownloadStatusDto statusDto = new DownloadStatusDto();
		List<Student> listStudent = new ArrayList<>();
		if (dto.getStudentClass() != null && StringUtils.hasText(dto.getStudentClass())) {
			listStudent = studentRepository.getAllStudentByClass(dto.getStudentClass());
		} else if (dto.getMajors() != null && StringUtils.hasText(dto.getMajors())) {
			listStudent = studentRepository.getAllStudentByMajors(dto.getMajors());
		} else {
			listStudent = studentRepository.findAll();
		}
		statusDto = exportBangDiemMoi(listStudent, dto, false);
		return statusDto;
	}

	public DownloadStatusDto exportBangDiemMoi(List<Student> listStd, StudentSearchDto dto, Boolean check)
			throws IOException {
		DownloadStatusDto statusDto = new DownloadStatusDto();
		String status = "";
		FileInputStream fis = new FileInputStream(new File(attachmentPath + Constants.BANG_DIEM_MOI));
		HSSFWorkbook workbook1 = new HSSFWorkbook(fis);

		SignAndDateDto dtoSAD = new SignAndDateDto();
		dtoSAD.setDateExport(dto.getDateExport());
		dtoSAD.setLineOne(dto.getLineOne());
		dtoSAD.setLineTwo(dto.getLineTwo());
		dtoSAD.setLineThree(dto.getLineThree());
		dtoSAD.setSignName(dto.getSignName());
		dtoSAD.setIsFormal(dto.getIsFormal());
		status = createSheetBangDiem(listStd, workbook1, dtoSAD);
		if (check) {
			workbook1.removeSheetAt(0);
			statusDto.setHssfWb(workbook1);
		}
		statusDto.setLogDetail(status);
		return statusDto;

	}

	public ByteArrayResource phuLucToExcel(AppendixSearchDto dto) throws IOException {
		DownloadStatusDto statusDto = new DownloadStatusDto();
		List<Appendix> listAppendix = new ArrayList<>();
		if (dto.getId() != null) {
			Appendix appendix = appendixRepository.getOne(dto.getId());
			listAppendix.add(appendix);
		} else if (dto.getStudentClass() != null && StringUtils.hasText(dto.getStudentClass())) {
			listAppendix = appendixRepository.getAllStudentByClass(dto.getStudentClass());
		} else if (dto.getMajors() != null && StringUtils.hasText(dto.getMajors())) {
			listAppendix = appendixRepository.getAllStudentByMajors(dto.getMajors());
		} else {
			listAppendix = appendixRepository.findAll();
		}
		ByteArrayOutputStream excelFile = new ByteArrayOutputStream();
		statusDto = exportPhuLucVanBang(listAppendix, dto, true);
		statusDto.getHssfWb().write(excelFile);
		return new ByteArrayResource(excelFile.toByteArray());
	}

	public DownloadStatusDto checkStatusPhuLuc(AppendixSearchDto dto) throws IOException {

		DownloadStatusDto statusDto = new DownloadStatusDto();
		List<Appendix> listAppendix = new ArrayList<>();
		if (dto.getId() != null) {
			Appendix appendix = appendixRepository.getOne(dto.getId());
			listAppendix.add(appendix);
		} else if (dto.getStudentClass() != null && StringUtils.hasText(dto.getStudentClass())) {
			listAppendix = appendixRepository.getAllStudentByClass(dto.getStudentClass());
		} else if (dto.getMajors() != null && StringUtils.hasText(dto.getMajors())) {
			listAppendix = appendixRepository.getAllStudentByMajors(dto.getMajors());
		} else {
			listAppendix = appendixRepository.findAll();
		}
		statusDto = exportPhuLucVanBang(listAppendix, dto, false);
		return statusDto;
	}

	public DownloadStatusDto exportPhuLucVanBang(List<Appendix> listAppendix, AppendixSearchDto dto, boolean check)
			throws IOException {
		DownloadStatusDto dtoStatus = new DownloadStatusDto();
		String status = "";
		FileInputStream fis = new FileInputStream(new File(attachmentPath + Constants.PHU_LUC_VAN_BANG_VA_BANG_DIEM));
		HSSFWorkbook workbook1 = new HSSFWorkbook(fis);

////////////////////////////////////
		HSSFCellStyle styleCenterBacsic = workbook1.createCellStyle();
		HSSFFont fontCB = workbook1.createFont();
		fontCB.setFontHeightInPoints((short) 9);
		fontCB.setFontName("Times New Roman");
		styleCenterBacsic.setBorderBottom(BorderStyle.THIN);
		styleCenterBacsic.setBorderTop(BorderStyle.THIN);
		styleCenterBacsic.setBorderLeft(BorderStyle.THIN);
		styleCenterBacsic.setBorderRight(BorderStyle.THIN);
		styleCenterBacsic.setFont(fontCB);
		styleCenterBacsic.setAlignment(HorizontalAlignment.CENTER);
		styleCenterBacsic.setVerticalAlignment(VerticalAlignment.CENTER);
		styleCenterBacsic.setShrinkToFit(true);

///////////////////////////////
		HSSFCellStyle stylerBacsic = workbook1.createCellStyle();
		HSSFFont fontCBBS = workbook1.createFont();
		fontCBBS.setFontHeightInPoints((short) 9);
		fontCBBS.setFontName("Times New Roman");
		stylerBacsic.setBorderBottom(BorderStyle.THIN);
		stylerBacsic.setBorderTop(BorderStyle.THIN);
		stylerBacsic.setBorderLeft(BorderStyle.THIN);
		stylerBacsic.setBorderRight(BorderStyle.THIN);
		stylerBacsic.setFont(fontCBBS);
		stylerBacsic.setAlignment(HorizontalAlignment.LEFT);
		stylerBacsic.setVerticalAlignment(VerticalAlignment.CENTER);
		stylerBacsic.setShrinkToFit(true);
/////////////////////////

///////////////////////////////
		HSSFCellStyle stylerBacsicLine2 = workbook1.createCellStyle();
		HSSFFont fontCBBS2 = workbook1.createFont();
		fontCBBS2.setFontHeightInPoints((short) 9);
		fontCBBS2.setFontName("Times New Roman");
		stylerBacsicLine2.setBorderBottom(BorderStyle.THIN);
		stylerBacsicLine2.setBorderTop(BorderStyle.THIN);
		stylerBacsicLine2.setBorderLeft(BorderStyle.THIN);
		stylerBacsicLine2.setBorderRight(BorderStyle.THIN);
		stylerBacsicLine2.setFont(fontCBBS2);
		stylerBacsicLine2.setAlignment(HorizontalAlignment.LEFT);
		stylerBacsicLine2.setVerticalAlignment(VerticalAlignment.CENTER);
		stylerBacsicLine2.setWrapText(true);
/////////////////////////

///////////////////////////////
		HSSFCellStyle styleCenterBacsic12 = workbook1.createCellStyle();
		HSSFFont fontCB12 = workbook1.createFont();
		fontCB12.setFontHeightInPoints((short) 12);
		fontCB12.setFontName("Times New Roman");
		styleCenterBacsic12.setFont(fontCB12);
		styleCenterBacsic12.setAlignment(HorizontalAlignment.CENTER);
		styleCenterBacsic12.setVerticalAlignment(VerticalAlignment.CENTER);
		styleCenterBacsic12.setShrinkToFit(true);
/////////////////////////

///////////////////////////////
		HSSFCellStyle styleBoldCenter = workbook1.createCellStyle();
		HSSFFont font = workbook1.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short) 12);
		font.setFontName("Times New Roman");
		styleBoldCenter.setFont(font);
		styleBoldCenter.setAlignment(HorizontalAlignment.CENTER);
		styleBoldCenter.setVerticalAlignment(VerticalAlignment.CENTER);
		styleBoldCenter.setShrinkToFit(true);
/////////////////////////

///////////////////////////////
		HSSFCellStyle styleItalic = workbook1.createCellStyle();
		HSSFFont fontItalic = workbook1.createFont();
		fontItalic.setItalic(true);
		fontItalic.setFontHeightInPoints((short) 11);
		fontItalic.setFontName("Times New Roman");
		styleItalic.setFont(fontItalic);
		styleItalic.setAlignment(HorizontalAlignment.CENTER);
		styleItalic.setVerticalAlignment(VerticalAlignment.CENTER);
		styleItalic.setShrinkToFit(true);
/////////////////////////

///////////////////////////////
		HSSFCellStyle styleItalicUnderline = workbook1.createCellStyle();
		HSSFFont fontItalicU = workbook1.createFont();
		fontItalicU.setItalic(true);
		fontItalicU.setFontHeightInPoints((short) 12);
		fontItalicU.setFontName("Times New Roman");
		fontItalicU.setUnderline(Font.U_SINGLE);
		styleItalicUnderline.setFont(fontItalicU);
		styleItalicUnderline.setAlignment(HorizontalAlignment.RIGHT);
		styleItalicUnderline.setVerticalAlignment(VerticalAlignment.CENTER);
		styleItalicUnderline.setShrinkToFit(true);
/////////////////////////

///////////////////////////////
		HSSFCellStyle styleBoldCenter11 = workbook1.createCellStyle();
		HSSFFont fontNomarSize = workbook1.createFont();
		fontNomarSize.setBold(true);
		fontNomarSize.setFontHeightInPoints((short) 11);
		fontNomarSize.setFontName("Times New Roman");
		styleBoldCenter11.setFont(fontNomarSize);
		styleBoldCenter11.setAlignment(HorizontalAlignment.LEFT);
		styleBoldCenter11.setVerticalAlignment(VerticalAlignment.CENTER);
		styleBoldCenter11.setShrinkToFit(true);
/////////////////////////

///////////////////////////////
		HSSFCellStyle styleBoldCenterTopLeft11 = workbook1.createCellStyle();
		HSSFFont fontNomarSizeTopLeft = workbook1.createFont();
		fontNomarSizeTopLeft.setBold(true);
		fontNomarSizeTopLeft.setFontHeightInPoints((short) 11);
		fontNomarSizeTopLeft.setFontName("Times New Roman");
		styleBoldCenterTopLeft11.setFont(fontNomarSizeTopLeft);
		styleBoldCenterTopLeft11.setAlignment(HorizontalAlignment.LEFT);
		styleBoldCenterTopLeft11.setVerticalAlignment(VerticalAlignment.TOP);
/////////////////////////

///////////////////////////////
		HSSFCellStyle styleTopLeft10 = workbook1.createCellStyle();
		HSSFFont fontTopLeft = workbook1.createFont();
		fontTopLeft.setFontHeightInPoints((short) 10);
		fontTopLeft.setFontName("Times New Roman");
		styleTopLeft10.setFont(fontTopLeft);
		styleTopLeft10.setAlignment(HorizontalAlignment.LEFT);
		styleTopLeft10.setVerticalAlignment(VerticalAlignment.TOP);
/////////////////////////
		
///////////////////////////////
HSSFCellStyle styleItalicGhichu = workbook1.createCellStyle();
HSSFFont fontItalicGC = workbook1.createFont();
fontItalicGC.setItalic(true);
fontItalicGC.setFontHeightInPoints((short) 9);
fontItalicGC.setFontName("Times New Roman");
styleItalicGhichu.setFont(fontItalicGC);
styleItalicGhichu.setAlignment(HorizontalAlignment.LEFT);
styleItalicGhichu.setVerticalAlignment(VerticalAlignment.CENTER);
styleItalicGhichu.setShrinkToFit(true);
/////////////////////////

///////////////////////////////
		HSSFCellStyle styleBoldCenter11RIGHT = workbook1.createCellStyle();
		HSSFFont fontNomarSizeR = workbook1.createFont();
		fontNomarSizeR.setBold(true);
		fontNomarSizeR.setFontHeightInPoints((short) 11);
		fontNomarSizeR.setFontName("Times New Roman");
		styleBoldCenter11RIGHT.setFont(fontNomarSizeR);
		styleBoldCenter11RIGHT.setAlignment(HorizontalAlignment.LEFT);
		styleBoldCenter11RIGHT.setVerticalAlignment(VerticalAlignment.CENTER);
		styleBoldCenter11RIGHT.setShrinkToFit(true);
/////////////////////////

///////////////////////////////
		HSSFCellStyle styleBorderBoldCenter10 = workbook1.createCellStyle();
		HSSFFont fontNomarSize10 = workbook1.createFont();
		fontNomarSize10.setBold(true);
		fontNomarSize10.setFontHeightInPoints((short) 10);
		fontNomarSize10.setFontName("Times New Roman");
		styleBorderBoldCenter10.setFont(fontNomarSize10);
		styleBorderBoldCenter10.setAlignment(HorizontalAlignment.LEFT);
		styleBorderBoldCenter10.setVerticalAlignment(VerticalAlignment.CENTER);
		styleBorderBoldCenter10.setShrinkToFit(true);
/////////////////////////

///////////////////////////////
		HSSFCellStyle styleBorderBoldLeft10 = workbook1.createCellStyle();
		HSSFFont fontNomarSizeL10 = workbook1.createFont();
		fontNomarSizeL10.setBold(true);
		fontNomarSizeL10.setFontHeightInPoints((short) 10);
		fontNomarSizeL10.setFontName("Times New Roman");
		styleBorderBoldLeft10.setFont(fontNomarSizeL10);
		styleBorderBoldLeft10.setAlignment(HorizontalAlignment.LEFT);
		styleBorderBoldLeft10.setVerticalAlignment(VerticalAlignment.CENTER);
		styleBorderBoldLeft10.setShrinkToFit(true);
/////////////////////////
///////////////////////////////
		HSSFCellStyle styleBorderBoldCENTERCENTER10 = workbook1.createCellStyle();
		styleBorderBoldCENTERCENTER10.setFont(fontNomarSize10);
		styleBorderBoldCENTERCENTER10.setAlignment(HorizontalAlignment.CENTER);
		styleBorderBoldCENTERCENTER10.setVerticalAlignment(VerticalAlignment.CENTER);
		styleBorderBoldCENTERCENTER10.setShrinkToFit(true);
/////////////////////////

///////////////////////////////
		HSSFCellStyle styleBorderBoldRight10 = workbook1.createCellStyle();
		HSSFFont fontNomarSizeR10 = workbook1.createFont();
		fontNomarSizeR10.setBold(true);
		fontNomarSizeR10.setFontHeightInPoints((short) 10);
		fontNomarSizeR10.setFontName("Times New Roman");
		styleBorderBoldRight10.setFont(fontNomarSizeR10);
		styleBorderBoldRight10.setAlignment(HorizontalAlignment.RIGHT);
		styleBorderBoldRight10.setVerticalAlignment(VerticalAlignment.CENTER);
		styleBorderBoldRight10.setShrinkToFit(true);
/////////////////////////

		HSSFCellStyle styleBoldCenterUnderline = workbook1.createCellStyle();
		HSSFFont fontBoldCenterUnderLine = workbook1.createFont();
		fontBoldCenterUnderLine.setUnderline(Font.U_SINGLE);
		fontBoldCenterUnderLine.setBold(true);
		fontBoldCenterUnderLine.setFontName("Times New Roman");
		fontBoldCenterUnderLine.setFontHeightInPoints((short) 13);
		styleBoldCenterUnderline.setFont(fontBoldCenterUnderLine);
		styleBoldCenterUnderline.setAlignment(HorizontalAlignment.CENTER);
		styleBoldCenterUnderline.setVerticalAlignment(VerticalAlignment.CENTER);
		styleBoldCenterUnderline.setShrinkToFit(true);
///////////////////////////////
		HSSFCellStyle styleBoldCenterSizeLarger = workbook1.createCellStyle();
		HSSFFont fontBoldCenterSizeLarger = workbook1.createFont();
		fontBoldCenterSizeLarger.setBold(true);
		fontBoldCenterSizeLarger.setFontName("Times New Roman");
		fontBoldCenterSizeLarger.setFontHeightInPoints((short) 16);
		styleBoldCenterSizeLarger.setFont(fontBoldCenterSizeLarger);
		styleBoldCenterSizeLarger.setAlignment(HorizontalAlignment.CENTER);
		styleBoldCenterSizeLarger.setVerticalAlignment(VerticalAlignment.CENTER);
		styleBoldCenterSizeLarger.setShrinkToFit(true);
/////////////////////////  

		for (Appendix dtoAppendix : listAppendix) {
			Student studentDto = studentRepository.getStudentFromCode(dtoAppendix.getCode());
			HSSFSheet sheet = workbook1.cloneSheet(1);
			workbook1.setSheetName(workbook1.getSheetIndex(sheet), dtoAppendix.getCode() + "_PHU_LUC");
			CellReference cr = new CellReference("D9");
			sheet.getRow(cr.getRow()).getCell(cr.getCol()).setCellValue(dtoAppendix.getFullName());
			cr = new CellReference("E10");
			sheet.getRow(cr.getRow()).getCell(cr.getCol()).setCellValue(dtoAppendix.getPlaceOfBirth());
			cr = new CellReference("R9");
			sheet.getRow(cr.getRow()).getCell(cr.getCol()).setCellValue(dtoAppendix.getBirthday());
			cr = new CellReference("R10");
			sheet.getRow(cr.getRow()).getCell(cr.getCol()).setCellValue(dtoAppendix.getCode());
			cr = new CellReference("E11");
			sheet.getRow(cr.getRow()).getCell(cr.getCol()).setCellValue(dtoAppendix.getMajors());
			if (studentDto == null || studentDto.getSpecialized().length() == 0) {
				status += "\n Sheet: " + dtoAppendix.getCode() + "_PHU_LUC"
						+ " \n - Không tìm thấy sinh viên hoặc chuyên ngành trong bảng điểm của sinh viên có mã: "
						+ dtoAppendix.getCode() + ".";
				cr = new CellReference("S11");
				sheet.getRow(cr.getRow()).getCell(cr.getCol()).setCellValue("");
			} else if (studentDto.getSpecialized().length() > 0) {
				cr = new CellReference("S11");
				sheet.getRow(cr.getRow()).getCell(cr.getCol()).setCellValue(studentDto.getSpecialized());
			} else {
				cr = new CellReference("S11");
				sheet.getRow(cr.getRow()).getCell(cr.getCol()).setCellValue("");
			}
			cr = new CellReference("E12");
			sheet.getRow(cr.getRow()).getCell(cr.getCol()).setCellValue(dtoAppendix.getStudentClass());
			cr = new CellReference("G13");
			sheet.getRow(cr.getRow()).getCell(cr.getCol()).setCellValue(dtoAppendix.getDateJoin());
			cr = new CellReference("T13");
			sheet.getRow(cr.getRow()).getCell(cr.getCol())
					.setCellValue(dtoAppendix.getDateJoin() + " đến " + dtoAppendix.getDateOut());
			cr = new CellReference("T14");
			sheet.getRow(cr.getRow()).getCell(cr.getCol()).setCellValue(dtoAppendix.getTypeTraining());
			cr = new CellReference("G14");
			sheet.getRow(cr.getRow()).getCell(cr.getCol()).setCellValue(dtoAppendix.getLevelTraining());
			cr = new CellReference("G15");
			sheet.getRow(cr.getRow()).getCell(cr.getCol()).setCellValue(dtoAppendix.getNumberDiploma());
			cr = new CellReference("T12");
			sheet.getRow(cr.getRow()).getCell(cr.getCol()).setCellValue(dtoAppendix.getTrainingLanguage());
			Date dateReal = new Date();
			try {
				dateReal = sdf.parse(dto.getDateExport());
			} catch (Exception e) {
				System.out.println(e);
			}
			LocalDate date = dateReal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			String dateEx = "Thái Nguyên, ngày " + date.getDayOfMonth() + " tháng " + date.getMonthValue() + " năm "
					+ date.getYear();
			cr = new CellReference("N17");
			sheet.getRow(cr.getRow()).getCell(cr.getCol()).setCellValue(dateEx);

			if (dto.getLineThree().length() > 0) {
				cr = new CellReference("N18");
				sheet.getRow(cr.getRow()).getCell(cr.getCol()).setCellValue(dto.getLineOne());
				cr = new CellReference("N19");
				sheet.getRow(cr.getRow()).getCell(cr.getCol()).setCellValue(dto.getLineTwo());
				cr = new CellReference("N20");
				sheet.getRow(cr.getRow()).getCell(cr.getCol()).setCellValue(dto.getLineThree());
				cr = new CellReference("N26");
				sheet.getRow(cr.getRow()).getCell(cr.getCol()).setCellValue(dto.getSignName());
			} else if (dto.getLineTwo().length() > 0) {
				cr = new CellReference("N18");
				sheet.getRow(cr.getRow()).getCell(cr.getCol()).setCellValue(dto.getLineOne());
				cr = new CellReference("N19");
				sheet.getRow(cr.getRow()).getCell(cr.getCol()).setCellValue(dto.getLineTwo());
				cr = new CellReference("N25");
				sheet.getRow(cr.getRow()).getCell(cr.getCol()).setCellValue(dto.getSignName());
			} else {
				cr = new CellReference("N18");
				sheet.getRow(cr.getRow()).getCell(cr.getCol()).setCellValue(dto.getLineOne());
				cr = new CellReference("N24");
				sheet.getRow(cr.getRow()).getCell(cr.getCol()).setCellValue(dto.getSignName());
			}

			if (studentDto == null || studentDto.getSpecialized().length() == 0) {
				status += "\n Sheet: " + dtoAppendix.getCode() + "_PHU_LUC" + " \n - Không tìm thấy sinh viên có mã: "
						+ dtoAppendix.getCode() + " trong bảng điểm. Chỉ có thể in phụ lục sinh viên này \n";
			} else {
				SignAndDateDto dtoSAD = new SignAndDateDto();
				dtoSAD.setDateExport(dto.getDateExport());
				dtoSAD.setLineOne(dto.getLineOne());
				dtoSAD.setLineTwo(dto.getLineTwo());
				dtoSAD.setLineThree(dto.getLineThree());
				dtoSAD.setSignName(dto.getSignName());
				HSSFSheet sheet1 = workbook1.cloneSheet(0);
				workbook1.setSheetName(workbook1.getSheetIndex(sheet1), studentDto.getCode() + "_BANG_DIEM");
				List<Transcript> list = transcriptsServiceImpl.getListTranscriptByStudent(studentDto.getCode());

				CellReference cr1 = new CellReference("F6");
				sheet1.getRow(cr1.getRow()).getCell(cr1.getCol()).setCellValue(studentDto.getDisplayName());// ho va ten
				cr1 = new CellReference("F7");
				sheet1.getRow(cr1.getRow()).getCell(cr1.getCol()).setCellValue(studentDto.getBirthDate());// ngay sinh
				cr1 = new CellReference("F8");
				sheet1.getRow(cr1.getRow()).getCell(cr1.getCol()).setCellValue(studentDto.getPlaceOfBirth());// noi sinh
				cr1 = new CellReference("R8");
				sheet1.getRow(cr1.getRow()).getCell(cr1.getCol()).setCellValue(studentDto.getStudentClass());// lop
				sheet1.getRow(cr1.getRow()).getCell(cr1.getCol()).setCellStyle(styleBoldCenterTopLeft11);
				cr1 = new CellReference("F9");
				sheet1.getRow(cr1.getRow()).getCell(cr1.getCol()).setCellValue(studentDto.getCode());// ma sinh vien

				if (studentDto.getMajors().length() > 24) {
					cr1 = new CellReference("S6");
					sheet1.getRow(cr1.getRow()).getCell(cr1.getCol()).setCellValue(studentDto.getMajors());// nganh
					sheet1.getRow(cr1.getRow()).setHeight((short) 520);
					cr1 = new CellReference("T7");
					sheet1.getRow(cr1.getRow()).getCell(cr1.getCol()).setCellValue(studentDto.getSpecialized());// chuyen
																												// nganh
					sheet1.getRow(cr1.getRow()).setHeight((short) 520);
				} else {
					cr1 = new CellReference("S6");
					sheet1.getRow(cr1.getRow()).getCell(cr1.getCol()).setCellValue(studentDto.getMajors());// nganh
					cr1 = new CellReference("T7");
					sheet1.getRow(cr1.getRow()).getCell(cr1.getCol()).setCellValue(studentDto.getSpecialized());// chuyen
																												// nganh
				}

				int rowNum = list.size();
				int demDong = 0;
				if (rowNum % 2 == 0) {
					rowNum = rowNum / 2;
					demDong = 11 + rowNum;
				} else {
					rowNum = (int) Math.floor(rowNum / 2);
					demDong = 12 + rowNum;
				}

				int stt = 1;
				int index = 0;
				for (int i = 11; i < demDong; i++) {
					sheet1.addMergedRegion(new CellRangeAddress(i, i, 1, 9));
					sheet1.getRow(i).getCell(0).setCellValue(stt);
					sheet1.getRow(i).getCell(0).setCellStyle(styleCenterBacsic);

					if (list.get(index).getModule().length() > 60) {
						sheet1.getRow(i).getCell(1).setCellValue(list.get(index).getModule());
						sheet1.getRow(i).getCell(1).setCellStyle(stylerBacsicLine2);
						sheet1.getRow(i).setHeight((short) 450);
					} else {
						sheet1.getRow(i).getCell(1).setCellValue(list.get(index).getModule());
						sheet1.getRow(i).getCell(1).setCellStyle(stylerBacsic);
					}
					sheet1.getRow(i).getCell(10).setCellValue(list.get(index).getCredit());
					sheet1.getRow(i).getCell(10).setCellStyle(styleCenterBacsic);
					sheet1.getRow(i).getCell(11).setCellValue(list.get(index).getScore());
					sheet1.getRow(i).getCell(11).setCellStyle(styleCenterBacsic);
					sheet1.getRow(i).getCell(12).setCellValue(list.get(index).getMark());
					sheet1.getRow(i).getCell(12).setCellStyle(styleCenterBacsic);
					setBorderMerger(i, i, 1, 9, sheet1);
					index++;
					stt++;
				}

				if (list.size() % 2 != 0) {
					demDong--;
				}
				for (int i = 11; i < demDong; i++) {
					sheet1.addMergedRegion(new CellRangeAddress(i, i, 15, 23));
					sheet1.getRow(i).getCell(14).setCellValue(stt);
					sheet1.getRow(i).getCell(14).setCellStyle(styleCenterBacsic);

					if (list.get(index).getModule().length() > 60) {
						sheet1.getRow(i).getCell(15).setCellValue(list.get(index).getModule());
						sheet1.getRow(i).getCell(15).setCellStyle(stylerBacsicLine2);
						sheet1.getRow(i).setHeight((short) 450);
					} else {
						sheet1.getRow(i).getCell(15).setCellValue(list.get(index).getModule());
						sheet1.getRow(i).getCell(15).setCellStyle(stylerBacsic);
					}
					sheet1.getRow(i).getCell(24).setCellValue(list.get(index).getCredit());
					sheet1.getRow(i).getCell(24).setCellStyle(styleCenterBacsic);
					sheet1.getRow(i).getCell(25).setCellValue(list.get(index).getScore());
					sheet1.getRow(i).getCell(25).setCellStyle(styleCenterBacsic);
					sheet1.getRow(i).getCell(26).setCellValue(list.get(index).getMark());
					sheet1.getRow(i).getCell(26).setCellStyle(styleCenterBacsic);
					setBorderMerger(i, i, 15, 23, sheet1);
					index++;
					stt++;
				}
				if (list.size() % 2 != 0) {
					demDong = demDong + 1;
				}
				AppendixDto appendixDto = null;
				appendixDto = appendixRepository.getStudentByCode(studentDto.getCode());
				sheet1.getRow(demDong).getCell(0).setCellValue("Tổng số tín chỉ tích lũy:");
				sheet1.getRow(demDong).getCell(0).setCellStyle(styleBorderBoldCenter10);
				sheet1.getRow(demDong).getCell(6).setCellStyle(styleBorderBoldLeft10); // 165
				if (appendixDto == null) {
					status += "\n sheet1: " + studentDto.getCode()
							+ " \n - Không tìm thấy số tín chỉ tích luỹ trong bảng phụ lục của sinh viên có mã: "
							+ studentDto.getCode() + ".";

				} else if (appendixDto.getAllCredit().length() > 0) {
					sheet1.getRow(demDong).getCell(6).setCellValue(appendixDto.getAllCredit());// TCTL
				} else if (appendixDto.getAllCredit().length() == 0) {
					appendixDto.setAllCredit("");
				}

				sheet1.getRow(demDong + 1).getCell(10).setCellValue(studentDto.getScoreTen());// diem 10
				sheet1.getRow(demDong + 2).getCell(10).setCellValue(studentDto.getScoreFour());// diem 4

				sheet1.getRow(demDong + 1).getCell(0).setCellValue("Điểm trung bình");
				sheet1.getRow(demDong + 1).getCell(0).setCellStyle(styleBorderBoldCenter10);
				sheet1.getRow(demDong + 1).getCell(6).setCellValue("Thang điểm 10:");
				sheet1.getRow(demDong + 1).getCell(6).setCellStyle(styleBorderBoldCenter10);
				sheet1.getRow(demDong + 1).getCell(10).setCellStyle(styleBorderBoldLeft10);// diem 10

				sheet1.getRow(demDong + 2).getCell(6).setCellValue("Thang điểm 4:");
				sheet1.getRow(demDong + 2).getCell(6).setCellStyle(styleBorderBoldCenter10);
				sheet1.getRow(demDong + 2).getCell(10).setCellStyle(styleBorderBoldLeft10);// diem 4
				if (!dto.getIsFormal()) {
					sheet1.createRow(demDong + 3).createCell(0).setCellValue("Xếp loại rèn luyện : ");
					sheet1.getRow(demDong + 3).getCell(0).setCellStyle(styleBorderBoldCenter10);
					sheet1.getRow(demDong + 3).createCell(6).setCellStyle(styleBorderBoldCenter10);// ren luyen
					sheet1.addMergedRegion(new CellRangeAddress(demDong + 3, demDong + 3, 0, 5));// Xep loai ren luyen
					setBorderMergerOutLeft(demDong + 3, demDong + 3, 0, 5, sheet1);
					sheet1.addMergedRegion(new CellRangeAddress(demDong + 3, demDong + 3, 6, 12));// Tot
					setBorderMergerOutRight(demDong + 3, demDong + 3, 6, 12, sheet1);
					if (studentDto.getRankConduct().length() == 0 || studentDto.getRankConduct() == null) {
						sheet1.getRow(demDong + 3).getCell(6).setCellValue("");
						status += "Sheet: " + studentDto.getCode() + "_BANG_DIEM"
								+ "\n - Không có xếp loại hạnh kiểm! \n";

					} else {
						sheet1.getRow(demDong + 3).getCell(6).setCellValue(studentDto.getRankConduct());
					}

					sheet1.getRow(demDong).getCell(14).setCellValue("Xếp loại tốt nghiệp:");
					sheet1.getRow(demDong).getCell(14).setCellStyle(styleBorderBoldCENTERCENTER10);
					sheet1.getRow(demDong).getCell(22).setCellStyle(styleBorderBoldCenter10);// xl tot nghiep
					sheet1.getRow(demDong).getCell(22).setCellValue(studentDto.getRankGraduating());// xl tot nghiep
					sheet1.addMergedRegion(new CellRangeAddress(demDong, demDong + 3, 14, 21));// Xep loai tot nghiep
					setBorderMergerOutLeft(demDong, demDong + 3, 14, 21, sheet1);
					sheet1.addMergedRegion(new CellRangeAddress(demDong, demDong + 3, 22, 26));// Gioi
					setBorderMergerOutRight(demDong, demDong + 3, 22, 26, sheet1);
				} else {
					sheet1.getRow(demDong).getCell(14).setCellValue("Xếp loại tốt nghiệp:");
					sheet1.getRow(demDong).getCell(14).setCellStyle(styleBorderBoldCENTERCENTER10);
					sheet1.addMergedRegion(new CellRangeAddress(demDong, demDong + 2, 14, 21));// Xep loai tot nghiep
					sheet1.getRow(demDong).getCell(22).setCellValue(studentDto.getRankGraduating());// xl tot nghiep
					sheet1.getRow(demDong).getCell(22).setCellStyle(styleBorderBoldCenter10);// xl tot nghiep
					setBorderMergerOutLeft(demDong, demDong + 2, 14, 21, sheet1);
					sheet1.addMergedRegion(new CellRangeAddress(demDong, demDong + 2, 22, 26));// Gioi
					setBorderMergerOutRight(demDong, demDong + 2, 22, 26, sheet1);
				}

				sheet1.getRow(demDong + 4).getCell(14).setCellValue(dateEx);
				sheet1.getRow(demDong + 4).getCell(14).setCellStyle(styleItalic);
				sheet1.getRow(demDong + 5).getCell(0).setCellValue("Ghi chú:");
				sheet1.getRow(demDong + 5).getCell(0).setCellStyle(styleItalicUnderline);
				sheet1.getRow(demDong + 5).getCell(2).setCellValue("Bảng điểm này chỉ cấp 1 lần khi ra trường");
				sheet1.getRow(demDong + 5).getCell(2).setCellStyle(styleItalicGhichu);
				short doChieuDai = 0;
				if (dto.getLineThree().length() > 0 && dto.getLineThree() != null) {
					sheet1.getRow(demDong + 5).getCell(14).setCellValue(dto.getLineOne());
					sheet1.getRow(demDong + 5).getCell(14).setCellStyle(styleBoldCenter);
					sheet1.getRow(demDong + 6).getCell(14).setCellValue(dto.getLineTwo());
					sheet1.getRow(demDong + 6).getCell(14).setCellStyle(styleBoldCenter);
					sheet1.getRow(demDong + 7).getCell(14).setCellValue(dto.getLineThree());
					sheet1.getRow(demDong + 7).getCell(14).setCellStyle(styleBoldCenter);
					sheet1.createRow(demDong + 14).createCell(14).setCellValue(dto.getSignName());
					sheet1.getRow(demDong + 14).getCell(14).setCellStyle(styleBoldCenter);
					sheet1.addMergedRegion(new CellRangeAddress(demDong + 14, demDong + 14, 14, 26));// PGS TS Nghia
					for (int i = 0; i < demDong + 14; i++) {
						doChieuDai += sheet1.getRow(i).getHeight();
					}
				} else if (dto.getLineTwo().length() > 0 && dto.getLineTwo() != null) {
					sheet1.getRow(demDong + 5).getCell(14).setCellValue(dto.getLineOne());
					sheet1.getRow(demDong + 5).getCell(14).setCellStyle(styleBoldCenter);
					sheet1.getRow(demDong + 6).getCell(14).setCellValue(dto.getLineTwo());
					sheet1.getRow(demDong + 6).getCell(14).setCellStyle(styleBoldCenter);
					sheet1.createRow(demDong + 13).createCell(14).setCellValue(dto.getSignName());
					sheet1.getRow(demDong + 13).getCell(14).setCellStyle(styleBoldCenter);
					sheet1.addMergedRegion(new CellRangeAddress(demDong + 13, demDong + 13, 14, 26));// PGS TS Nghia
					for (int i = 0; i < demDong + 13; i++) {
						doChieuDai += sheet1.getRow(i).getHeight();
					}
				} else {
					sheet1.getRow(demDong + 5).getCell(14).setCellValue(dto.getLineOne());
					sheet1.getRow(demDong + 5).getCell(14).setCellStyle(styleBoldCenter);
					sheet1.createRow(demDong + 12).createCell(14).setCellValue(dto.getSignName());
					sheet1.getRow(demDong + 12).getCell(14).setCellStyle(styleBoldCenter);
					sheet1.addMergedRegion(new CellRangeAddress(demDong + 12, demDong + 12, 14, 26));// PGS TS Nghia
					for (int i = 0; i < demDong + 12; i++) {
						doChieuDai += sheet1.getRow(i).getHeight();
					}
				}
				if (doChieuDai > Constants.GIOI_HAN_EXCEL) {
					for (int i = 11; i < demDong - 1; i++) {
						if (sheet1.getRow(i).getHeight() < (short) 400) {
							sheet1.getRow(i).setHeight((short) 230);
						}
					}
				}
				doChieuDai = 0;
				for (int i = 0; i < demDong + 12; i++) {
					doChieuDai += sheet1.getRow(i).getHeight();
				}
				if (doChieuDai > Constants.GIOI_HAN_EXCEL) {
					status += "Sheet: " + studentDto.getCode() + "_BANG_DIEM" + "\n - Đã vượt quá giới hạn trang! \n";
					workbook1.setSheetName(
							workbook1.getSheetIndex(workbook1.getSheet(studentDto.getCode() + "_BANG_DIEM")),
							studentDto.getCode() + "_BANG_DIEM-QGH");

				}
				sheet1.addMergedRegion(new CellRangeAddress(demDong, demDong, 0, 5));// Tin chi tich luy
				setBorderMergerOutLeft(demDong, demDong, 0, 5, sheet1);
				sheet1.addMergedRegion(new CellRangeAddress(demDong, demDong, 6, 12));// TCTL:165
				setBorderMergerOutRight(demDong, demDong, 6, 12, sheet1);
				sheet1.addMergedRegion(new CellRangeAddress(demDong + 1, demDong + 2, 0, 5));// Diem trung binh
				setBorderMerger(demDong + 1, demDong + 2, 0, 5, sheet1);
				sheet1.addMergedRegion(new CellRangeAddress(demDong + 1, demDong + 1, 6, 9));// Thang diem 10
				setBorderMergerOutLeft(demDong + 1, demDong + 1, 6, 9, sheet1);
				sheet1.addMergedRegion(new CellRangeAddress(demDong + 1, demDong + 1, 10, 12));// 10.0
				setBorderMergerOutRight(demDong + 1, demDong + 1, 10, 12, sheet1);

				sheet1.addMergedRegion(new CellRangeAddress(demDong + 2, demDong + 2, 6, 9));// Thang diem 4
				setBorderMergerOutLeft(demDong + 2, demDong + 2, 5, 8, sheet1);
				sheet1.addMergedRegion(new CellRangeAddress(demDong + 2, demDong + 2, 10, 12));// 4.0
				setBorderMergerOutRight(demDong + 2, demDong + 2, 9, 12, sheet1);

				sheet1.addMergedRegion(new CellRangeAddress(demDong + 4, demDong + 4, 14, 26));// Ngay thang nam
				sheet1.addMergedRegion(new CellRangeAddress(demDong + 5, demDong + 5, 14, 26));// Ngay thang nam
				sheet1.addMergedRegion(new CellRangeAddress(demDong + 5, demDong + 5, 0, 1));// Ghi chu
				sheet1.addMergedRegion(new CellRangeAddress(demDong + 5, demDong + 5, 2, 11));// cap 1 lan khi ra
																								// truong
				sheet1.addMergedRegion(new CellRangeAddress(demDong + 6, demDong + 6, 14, 26));// TL.hieu truong
				sheet1.addMergedRegion(new CellRangeAddress(demDong + 7, demDong + 7, 14, 26));// Truong phong dao
																								// tao
			}
		}
		workbook1.removeSheetAt(workbook1.getSheetIndex(workbook1.getSheet(Constants.TEN_MAU_SHEET_BANG_DIEM)));
		workbook1.removeSheetAt(workbook1.getSheetIndex(workbook1.getSheet(Constants.TEN_MAU_SHEET_PHU_LUC)));
		if (check) {
			dtoStatus.setHssfWb(workbook1);
		}
		dtoStatus.setLogDetail(status);
		return dtoStatus;
	}

	public void setBorderMerger(int fRow, int lRow, int fCol, int lCol, HSSFSheet sheet) {
		CellRangeAddress region = new CellRangeAddress(fRow, lRow, fCol, lCol);
		RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
	}

	public void setBorderMergerOutLeft(int fRow, int lRow, int fCol, int lCol, HSSFSheet sheet) {
		CellRangeAddress region = new CellRangeAddress(fRow, lRow, fCol, lCol);
		RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
	}

	public void setBorderMergerOutRight(int fRow, int lRow, int fCol, int lCol, HSSFSheet sheet) {
		CellRangeAddress region = new CellRangeAddress(fRow, lRow, fCol, lCol);
		RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
	}

	public String createSheetBangDiem(List<Student> listST, HSSFWorkbook workbook1, SignAndDateDto dto) {
		String status = "";
		////////////////////////////////////
		HSSFCellStyle styleCenterBacsic = workbook1.createCellStyle();
		HSSFFont fontCB = workbook1.createFont();
		fontCB.setFontHeightInPoints((short) 9);
		fontCB.setFontName("Times New Roman");
		styleCenterBacsic.setBorderBottom(BorderStyle.THIN);
		styleCenterBacsic.setBorderTop(BorderStyle.THIN);
		styleCenterBacsic.setBorderLeft(BorderStyle.THIN);
		styleCenterBacsic.setBorderRight(BorderStyle.THIN);
		styleCenterBacsic.setFont(fontCB);
		styleCenterBacsic.setAlignment(HorizontalAlignment.CENTER);
		styleCenterBacsic.setVerticalAlignment(VerticalAlignment.CENTER);
		styleCenterBacsic.setShrinkToFit(true);

		///////////////////////////////
		HSSFCellStyle stylerBacsic = workbook1.createCellStyle();
		HSSFFont fontCBBS = workbook1.createFont();
		fontCBBS.setFontHeightInPoints((short) 9);
		fontCBBS.setFontName("Times New Roman");
		stylerBacsic.setBorderBottom(BorderStyle.THIN);
		stylerBacsic.setBorderTop(BorderStyle.THIN);
		stylerBacsic.setBorderLeft(BorderStyle.THIN);
		stylerBacsic.setBorderRight(BorderStyle.THIN);
		stylerBacsic.setFont(fontCBBS);
		stylerBacsic.setAlignment(HorizontalAlignment.LEFT);
		stylerBacsic.setVerticalAlignment(VerticalAlignment.CENTER);
		stylerBacsic.setShrinkToFit(true);
		/////////////////////////

		///////////////////////////////
		HSSFCellStyle stylerBacsicLine2 = workbook1.createCellStyle();
		HSSFFont fontCBBS2 = workbook1.createFont();
		fontCBBS2.setFontHeightInPoints((short) 9);
		fontCBBS2.setFontName("Times New Roman");
		stylerBacsicLine2.setBorderBottom(BorderStyle.THIN);
		stylerBacsicLine2.setBorderTop(BorderStyle.THIN);
		stylerBacsicLine2.setBorderLeft(BorderStyle.THIN);
		stylerBacsicLine2.setBorderRight(BorderStyle.THIN);
		stylerBacsicLine2.setFont(fontCBBS2);
		stylerBacsicLine2.setAlignment(HorizontalAlignment.LEFT);
		stylerBacsicLine2.setVerticalAlignment(VerticalAlignment.CENTER);
		stylerBacsicLine2.setWrapText(true);
		/////////////////////////

		///////////////////////////////
		HSSFCellStyle styleCenterBacsic12 = workbook1.createCellStyle();
		HSSFFont fontCB12 = workbook1.createFont();
		fontCB12.setFontHeightInPoints((short) 12);
		fontCB12.setFontName("Times New Roman");
		styleCenterBacsic12.setFont(fontCB12);
		styleCenterBacsic12.setAlignment(HorizontalAlignment.CENTER);
		styleCenterBacsic12.setVerticalAlignment(VerticalAlignment.CENTER);
		styleCenterBacsic12.setShrinkToFit(true);
		/////////////////////////

		///////////////////////////////
		HSSFCellStyle styleBoldCenter = workbook1.createCellStyle();
		HSSFFont font = workbook1.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short) 12);
		font.setFontName("Times New Roman");
		styleBoldCenter.setFont(font);
		styleBoldCenter.setAlignment(HorizontalAlignment.CENTER);
		styleBoldCenter.setVerticalAlignment(VerticalAlignment.CENTER);
		styleBoldCenter.setShrinkToFit(true);
		/////////////////////////

		///////////////////////////////
		HSSFCellStyle styleItalic = workbook1.createCellStyle();
		HSSFFont fontItalic = workbook1.createFont();
		fontItalic.setItalic(true);
		fontItalic.setFontHeightInPoints((short) 11);
		fontItalic.setFontName("Times New Roman");
		styleItalic.setFont(fontItalic);
		styleItalic.setAlignment(HorizontalAlignment.CENTER);
		styleItalic.setVerticalAlignment(VerticalAlignment.CENTER);
		styleItalic.setShrinkToFit(true);
		/////////////////////////

///////////////////////////////
		HSSFCellStyle styleItalicGhichu = workbook1.createCellStyle();
		HSSFFont fontItalicGC = workbook1.createFont();
		fontItalicGC.setItalic(true);
		fontItalicGC.setFontHeightInPoints((short) 9);
		fontItalicGC.setFontName("Times New Roman");
		styleItalicGhichu.setFont(fontItalicGC);
		styleItalicGhichu.setAlignment(HorizontalAlignment.LEFT);
		styleItalicGhichu.setVerticalAlignment(VerticalAlignment.CENTER);
		styleItalicGhichu.setShrinkToFit(true);
/////////////////////////

		///////////////////////////////
		HSSFCellStyle styleItalicUnderline = workbook1.createCellStyle();
		HSSFFont fontItalicU = workbook1.createFont();
		fontItalicU.setItalic(true);
		fontItalicU.setFontHeightInPoints((short) 12);
		fontItalicU.setFontName("Times New Roman");
		fontItalicU.setUnderline(Font.U_SINGLE);
		styleItalicUnderline.setFont(fontItalicU);
		styleItalicUnderline.setAlignment(HorizontalAlignment.RIGHT);
		styleItalicUnderline.setVerticalAlignment(VerticalAlignment.CENTER);
		styleItalicUnderline.setShrinkToFit(true);
		/////////////////////////

		///////////////////////////////
		HSSFCellStyle styleBoldCenter11 = workbook1.createCellStyle();
		HSSFFont fontNomarSize = workbook1.createFont();
		fontNomarSize.setBold(true);
		fontNomarSize.setFontHeightInPoints((short) 11);
		fontNomarSize.setFontName("Times New Roman");
		styleBoldCenter11.setFont(fontNomarSize);
		styleBoldCenter11.setAlignment(HorizontalAlignment.LEFT);
		styleBoldCenter11.setVerticalAlignment(VerticalAlignment.CENTER);
		styleBoldCenter11.setShrinkToFit(true);
		/////////////////////////

		///////////////////////////////
		HSSFCellStyle styleBoldCenterTopLeft11 = workbook1.createCellStyle();
		HSSFFont fontNomarSizeTopLeft = workbook1.createFont();
		fontNomarSizeTopLeft.setBold(true);
		fontNomarSizeTopLeft.setFontHeightInPoints((short) 11);
		fontNomarSizeTopLeft.setFontName("Times New Roman");
		styleBoldCenterTopLeft11.setFont(fontNomarSizeTopLeft);
		styleBoldCenterTopLeft11.setAlignment(HorizontalAlignment.LEFT);
		styleBoldCenterTopLeft11.setVerticalAlignment(VerticalAlignment.TOP);
		/////////////////////////

		///////////////////////////////
		HSSFCellStyle styleTopLeft10 = workbook1.createCellStyle();
		HSSFFont fontTopLeft = workbook1.createFont();
		fontTopLeft.setFontHeightInPoints((short) 10);
		fontTopLeft.setFontName("Times New Roman");
		styleTopLeft10.setFont(fontTopLeft);
		styleTopLeft10.setAlignment(HorizontalAlignment.LEFT);
		styleTopLeft10.setVerticalAlignment(VerticalAlignment.TOP);
		/////////////////////////

		///////////////////////////////
		HSSFCellStyle styleBoldCenter11RIGHT = workbook1.createCellStyle();
		HSSFFont fontNomarSizeR = workbook1.createFont();
		fontNomarSizeR.setBold(true);
		fontNomarSizeR.setFontHeightInPoints((short) 11);
		fontNomarSizeR.setFontName("Times New Roman");
		styleBoldCenter11RIGHT.setFont(fontNomarSizeR);
		styleBoldCenter11RIGHT.setAlignment(HorizontalAlignment.LEFT);
		styleBoldCenter11RIGHT.setVerticalAlignment(VerticalAlignment.CENTER);
		styleBoldCenter11RIGHT.setShrinkToFit(true);
		/////////////////////////

		///////////////////////////////
		HSSFCellStyle styleBorderBoldCenter10 = workbook1.createCellStyle();
		HSSFFont fontNomarSize10 = workbook1.createFont();
		fontNomarSize10.setBold(true);
		fontNomarSize10.setFontHeightInPoints((short) 10);
		fontNomarSize10.setFontName("Times New Roman");
		styleBorderBoldCenter10.setFont(fontNomarSize10);
		styleBorderBoldCenter10.setAlignment(HorizontalAlignment.LEFT);
		styleBorderBoldCenter10.setVerticalAlignment(VerticalAlignment.CENTER);
		styleBorderBoldCenter10.setShrinkToFit(true);
		/////////////////////////

		///////////////////////////////
		HSSFCellStyle styleBorderBoldCENTERCENTER10 = workbook1.createCellStyle();
		styleBorderBoldCENTERCENTER10.setFont(fontNomarSize10);
		styleBorderBoldCENTERCENTER10.setAlignment(HorizontalAlignment.CENTER);
		styleBorderBoldCENTERCENTER10.setVerticalAlignment(VerticalAlignment.CENTER);
		styleBorderBoldCENTERCENTER10.setShrinkToFit(true);
		/////////////////////////

		///////////////////////////////
		HSSFCellStyle styleBorderBoldRight10 = workbook1.createCellStyle();
		HSSFFont fontNomarSizeR10 = workbook1.createFont();
		fontNomarSizeR10.setBold(true);
		fontNomarSizeR10.setFontHeightInPoints((short) 10);
		fontNomarSizeR10.setFontName("Times New Roman");
		styleBorderBoldRight10.setFont(fontNomarSizeR10);
		styleBorderBoldRight10.setAlignment(HorizontalAlignment.RIGHT);
		styleBorderBoldRight10.setVerticalAlignment(VerticalAlignment.CENTER);
		styleBorderBoldRight10.setShrinkToFit(true);
		/////////////////////////

		HSSFCellStyle styleBoldCenterUnderline = workbook1.createCellStyle();
		HSSFFont fontBoldCenterUnderLine = workbook1.createFont();
		fontBoldCenterUnderLine.setUnderline(Font.U_SINGLE);
		fontBoldCenterUnderLine.setBold(true);
		fontBoldCenterUnderLine.setFontName("Times New Roman");
		fontBoldCenterUnderLine.setFontHeightInPoints((short) 13);
		styleBoldCenterUnderline.setFont(fontBoldCenterUnderLine);
		styleBoldCenterUnderline.setAlignment(HorizontalAlignment.CENTER);
		styleBoldCenterUnderline.setVerticalAlignment(VerticalAlignment.CENTER);
		styleBoldCenterUnderline.setShrinkToFit(true);
		///////////////////////////////
		HSSFCellStyle styleBoldCenterSizeLarger = workbook1.createCellStyle();
		HSSFFont fontBoldCenterSizeLarger = workbook1.createFont();
		fontBoldCenterSizeLarger.setBold(true);
		fontBoldCenterSizeLarger.setFontName("Times New Roman");
		fontBoldCenterSizeLarger.setFontHeightInPoints((short) 16);
		styleBoldCenterSizeLarger.setFont(fontBoldCenterSizeLarger);
		styleBoldCenterSizeLarger.setAlignment(HorizontalAlignment.CENTER);
		styleBoldCenterSizeLarger.setVerticalAlignment(VerticalAlignment.CENTER);
		styleBoldCenterSizeLarger.setShrinkToFit(true);
		/////////////////////////
		for (Student sv : listST) {
			HSSFSheet sheet = workbook1.cloneSheet(0);
			workbook1.setSheetName(workbook1.getSheetIndex(sheet), sv.getCode() + "_BANG_DIEM");
			List<Transcript> list = transcriptsServiceImpl.getListTranscriptByStudent(sv.getCode());

			CellReference cr = new CellReference("F6");
			sheet.getRow(cr.getRow()).getCell(cr.getCol()).setCellValue(sv.getDisplayName());// ho va ten
			cr = new CellReference("F7");
			sheet.getRow(cr.getRow()).getCell(cr.getCol()).setCellValue(sv.getBirthDate());// ngay sinh
			cr = new CellReference("F8");
			sheet.getRow(cr.getRow()).getCell(cr.getCol()).setCellValue(sv.getPlaceOfBirth());// noi sinh
			cr = new CellReference("R8");
			sheet.getRow(cr.getRow()).getCell(cr.getCol()).setCellValue(sv.getStudentClass());// lop
			sheet.getRow(cr.getRow()).getCell(cr.getCol()).setCellStyle(styleBoldCenterTopLeft11);
			cr = new CellReference("F9");
			sheet.getRow(cr.getRow()).getCell(cr.getCol()).setCellValue(sv.getCode());// ma sinh vien

			if (sv.getMajors().length() > 24) {
				cr = new CellReference("S6");
				sheet.getRow(cr.getRow()).getCell(cr.getCol()).setCellValue(sv.getMajors());// nganh
				sheet.getRow(cr.getRow()).setHeight((short) 520);
				cr = new CellReference("T7");
				sheet.getRow(cr.getRow()).getCell(cr.getCol()).setCellValue(sv.getSpecialized());// chuyen nganh
				sheet.getRow(cr.getRow()).setHeight((short) 520);
			} else {
				cr = new CellReference("S6");
				sheet.getRow(cr.getRow()).getCell(cr.getCol()).setCellValue(sv.getMajors());// nganh
				cr = new CellReference("T7");
				sheet.getRow(cr.getRow()).getCell(cr.getCol()).setCellValue(sv.getSpecialized());// chuyen nganh
			}

			int rowNum = list.size();
			int demDong = 0;
			if (rowNum % 2 == 0) {
				rowNum = rowNum / 2;
				demDong = 11 + rowNum;
			} else {
				rowNum = (int) Math.floor(rowNum / 2);
				demDong = 12 + rowNum;
			}

			int stt = 1;
			int index = 0;
			for (int i = 11; i < demDong; i++) {
				sheet.addMergedRegion(new CellRangeAddress(i, i, 1, 9));
				sheet.getRow(i).getCell(0).setCellValue(stt);
				sheet.getRow(i).getCell(0).setCellStyle(styleCenterBacsic);

				if (list.get(index).getModule().length() > 60) {
					sheet.getRow(i).getCell(1).setCellValue(list.get(index).getModule());
					sheet.getRow(i).getCell(1).setCellStyle(stylerBacsicLine2);
					sheet.getRow(i).setHeight((short) 450);
				} else {
					sheet.getRow(i).getCell(1).setCellValue(list.get(index).getModule());
					sheet.getRow(i).getCell(1).setCellStyle(stylerBacsic);
				}
				sheet.getRow(i).getCell(10).setCellValue(list.get(index).getCredit());
				sheet.getRow(i).getCell(10).setCellStyle(styleCenterBacsic);
				sheet.getRow(i).getCell(11).setCellValue(list.get(index).getScore());
				sheet.getRow(i).getCell(11).setCellStyle(styleCenterBacsic);
				sheet.getRow(i).getCell(12).setCellValue(list.get(index).getMark());
				sheet.getRow(i).getCell(12).setCellStyle(styleCenterBacsic);
				setBorderMerger(i, i, 1, 9, sheet);
				index++;
				stt++;
			}

			if (list.size() % 2 != 0) {
				demDong--;
			}
			for (int i = 11; i < demDong; i++) {
				sheet.addMergedRegion(new CellRangeAddress(i, i, 15, 23));
				sheet.getRow(i).getCell(14).setCellValue(stt);
				sheet.getRow(i).getCell(14).setCellStyle(styleCenterBacsic);

				if (list.get(index).getModule().length() > 60) {
					sheet.getRow(i).getCell(15).setCellValue(list.get(index).getModule());
					sheet.getRow(i).getCell(15).setCellStyle(stylerBacsicLine2);
					sheet.getRow(i).setHeight((short) 450);
				} else {
					sheet.getRow(i).getCell(15).setCellValue(list.get(index).getModule());
					sheet.getRow(i).getCell(15).setCellStyle(stylerBacsic);
				}
				sheet.getRow(i).getCell(24).setCellValue(list.get(index).getCredit());
				sheet.getRow(i).getCell(24).setCellStyle(styleCenterBacsic);
				sheet.getRow(i).getCell(25).setCellValue(list.get(index).getScore());
				sheet.getRow(i).getCell(25).setCellStyle(styleCenterBacsic);
				sheet.getRow(i).getCell(26).setCellValue(list.get(index).getMark());
				sheet.getRow(i).getCell(26).setCellStyle(styleCenterBacsic);
				setBorderMerger(i, i, 15, 23, sheet);
				index++;
				stt++;
			}
			if (list.size() % 2 != 0) {
				demDong = demDong + 1;
			}
			AppendixDto appendixDto = null;
			appendixDto = appendixRepository.getStudentByCode(sv.getCode());
			sheet.getRow(demDong).getCell(0).setCellValue("Tổng số tín chỉ tích lũy:");
			sheet.getRow(demDong).getCell(0).setCellStyle(styleBorderBoldCenter10);
			sheet.getRow(demDong).getCell(6).setCellStyle(styleBorderBoldCenter10); // 165
			if (appendixDto == null) {
				status += "\n Sheet: " + sv.getCode()
						+ " \n - Không tìm thấy số tín chỉ tích luỹ trong bảng phụ lục của sinh viên có mã: "
						+ sv.getCode() + ". \n";

			} else if (appendixDto.getAllCredit().length() > 0) {
				sheet.getRow(demDong).getCell(6).setCellValue(appendixDto.getAllCredit());// TCTL
			} else if (appendixDto.getAllCredit().length() == 0) {
				appendixDto.setAllCredit("");
			}

			sheet.getRow(demDong + 1).getCell(10).setCellValue(sv.getScoreTen());// diem 10

			sheet.getRow(demDong + 2).getCell(10).setCellValue(sv.getScoreFour());// diem 4

			sheet.getRow(demDong + 1).getCell(0).setCellValue("Điểm trung bình");
			sheet.getRow(demDong + 1).getCell(0).setCellStyle(styleBorderBoldCenter10);
			sheet.getRow(demDong + 1).getCell(6).setCellValue("Thang điểm 10:");
			sheet.getRow(demDong + 1).getCell(6).setCellStyle(styleBorderBoldCenter10);
			sheet.getRow(demDong + 1).getCell(10).setCellStyle(styleBorderBoldCenter10);// diem 10
			sheet.getRow(demDong + 2).getCell(6).setCellValue("Thang điểm 4:");
			sheet.getRow(demDong + 2).getCell(6).setCellStyle(styleBorderBoldCenter10);
			sheet.getRow(demDong + 2).getCell(10).setCellStyle(styleBorderBoldCenter10);// diem 4
			if (!dto.getIsFormal()) {
				sheet.getRow(demDong + 3).getCell(0).setCellValue("Xếp loại rèn luyện : ");
				sheet.getRow(demDong + 3).getCell(0).setCellStyle(styleBorderBoldCenter10);
				sheet.getRow(demDong + 3).getCell(6).setCellStyle(styleBorderBoldCenter10);// ren luyen
				sheet.addMergedRegion(new CellRangeAddress(demDong + 3, demDong + 3, 0, 5));// Xep loai ren luyen
				setBorderMergerOutLeft(demDong + 3, demDong + 3, 0, 5, sheet);
				sheet.addMergedRegion(new CellRangeAddress(demDong + 3, demDong + 3, 6, 12));// Tot
				setBorderMergerOutRight(demDong + 3, demDong + 3, 6, 12, sheet);
				if (sv.getRankConduct().length() == 0 || sv.getRankConduct() == null) {
					sheet.getRow(demDong + 3).getCell(6).setCellValue("");
					status += "Sheet: " + sv.getCode() + "_BANG_DIEM" + "\n - Không có xếp loại hạnh kiểm! \n";

				} else {
					sheet.getRow(demDong + 3).getCell(6).setCellValue(sv.getRankConduct());
				}

				sheet.getRow(demDong).getCell(14).setCellValue("Xếp loại tốt nghiệp:");
				sheet.getRow(demDong).getCell(14).setCellStyle(styleBorderBoldCENTERCENTER10);
				sheet.getRow(demDong).getCell(22).setCellStyle(styleBorderBoldCenter10);// xl tot nghiep
				sheet.getRow(demDong).getCell(22).setCellValue(sv.getRankGraduating());// xl tot nghiep
				sheet.addMergedRegion(new CellRangeAddress(demDong, demDong + 3, 14, 21));// Xep loai tot nghiep
				setBorderMergerOutLeft(demDong, demDong + 3, 14, 21, sheet);
				sheet.addMergedRegion(new CellRangeAddress(demDong, demDong + 3, 22, 26));// Gioi
				setBorderMergerOutRight(demDong, demDong + 3, 22, 26, sheet);
			} else {
				sheet.getRow(demDong).getCell(14).setCellValue("Xếp loại tốt nghiệp:");
				sheet.getRow(demDong).getCell(14).setCellStyle(styleBorderBoldCENTERCENTER10);
				sheet.getRow(demDong).getCell(22).setCellStyle(styleBorderBoldCenter10);// xl tot nghiep
				sheet.getRow(demDong).getCell(22).setCellValue(sv.getRankGraduating());// xl tot nghiep
				sheet.addMergedRegion(new CellRangeAddress(demDong, demDong + 2, 14, 21));// Xep loai tot nghiep
				setBorderMergerOutLeft(demDong, demDong + 2, 14, 21, sheet);
				sheet.addMergedRegion(new CellRangeAddress(demDong, demDong + 2, 22, 26));// Gioi
				setBorderMergerOutRight(demDong, demDong + 2, 22, 26, sheet);
			}

			Date dateReal = new Date();
			try {
				dateReal = sdf.parse(dto.getDateExport());
			} catch (Exception e) {
				System.out.println(e);
			}
			LocalDate date = dateReal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			String dateEx = "Ngày " + date.getDayOfMonth() + " tháng " + date.getMonthValue() + " năm "
					+ date.getYear();
			sheet.getRow(demDong + 4).getCell(14).setCellValue(dateEx);
			sheet.getRow(demDong + 4).getCell(14).setCellStyle(styleItalic);
			sheet.getRow(demDong + 5).getCell(0).setCellValue("Ghi chú:");
			sheet.getRow(demDong + 5).getCell(0).setCellStyle(styleItalicUnderline);
			sheet.getRow(demDong + 5).getCell(2).setCellValue("Bảng điểm này chỉ cấp 1 lần khi ra trường");
			sheet.getRow(demDong + 5).getCell(2).setCellStyle(styleItalicGhichu);
			short doChieuDai = 0;
			if (dto.getLineThree().length() > 0 && dto.getLineThree() != null) {
				sheet.getRow(demDong + 5).getCell(14).setCellValue(dto.getLineOne());
				sheet.getRow(demDong + 5).getCell(14).setCellStyle(styleBoldCenter);
				sheet.getRow(demDong + 6).getCell(14).setCellValue(dto.getLineTwo());
				sheet.getRow(demDong + 6).getCell(14).setCellStyle(styleBoldCenter);
				sheet.getRow(demDong + 7).getCell(14).setCellValue(dto.getLineThree());
				sheet.getRow(demDong + 7).getCell(14).setCellStyle(styleBoldCenter);
				sheet.createRow(demDong + 14).createCell(14).setCellValue(dto.getSignName());
				sheet.getRow(demDong + 14).getCell(14).setCellStyle(styleBoldCenter);
				sheet.addMergedRegion(new CellRangeAddress(demDong + 14, demDong + 14, 14, 26));// PGS TS Nghia
				for (int i = 0; i < demDong + 14; i++) {
					doChieuDai += sheet.getRow(i).getHeight();
				}
			} else if (dto.getLineTwo().length() > 0 && dto.getLineTwo() != null) {
				sheet.getRow(demDong + 5).getCell(14).setCellValue(dto.getLineOne());
				sheet.getRow(demDong + 5).getCell(14).setCellStyle(styleBoldCenter);
				sheet.getRow(demDong + 6).getCell(14).setCellValue(dto.getLineTwo());
				sheet.getRow(demDong + 6).getCell(14).setCellStyle(styleBoldCenter);
				sheet.createRow(demDong + 13).createCell(14).setCellValue(dto.getSignName());
				sheet.getRow(demDong + 13).getCell(14).setCellStyle(styleBoldCenter);
				sheet.addMergedRegion(new CellRangeAddress(demDong + 13, demDong + 13, 14, 26));// PGS TS Nghia
				for (int i = 0; i < demDong + 13; i++) {
					doChieuDai += sheet.getRow(i).getHeight();
				}
			} else {
				sheet.getRow(demDong + 5).getCell(14).setCellValue(dto.getLineOne());
				sheet.getRow(demDong + 5).getCell(14).setCellStyle(styleBoldCenter);
				sheet.createRow(demDong + 12).createCell(14).setCellValue(dto.getSignName());
				sheet.getRow(demDong + 12).getCell(14).setCellStyle(styleBoldCenter);
				sheet.addMergedRegion(new CellRangeAddress(demDong + 12, demDong + 12, 14, 26));// PGS TS Nghia
				for (int i = 0; i < demDong + 12; i++) {
					doChieuDai += sheet.getRow(i).getHeight();
				}
			}
			if (doChieuDai > Constants.GIOI_HAN_EXCEL) {
				for (int i = 11; i < demDong - 1; i++) {
					if (sheet.getRow(i).getHeight() < (short) 400) {
						sheet.getRow(i).setHeight((short) 230);
					}
				}
			}
			doChieuDai = 0;
			for (int i = 0; i < demDong + 12; i++) {
				doChieuDai += sheet.getRow(i).getHeight();
			}
			if (doChieuDai > Constants.GIOI_HAN_EXCEL) {
				status += "\n Sheet: " + sv.getCode() + "_BANG_DIEM" + "\n - Đã vượt quá giới hạn trang! \n";
				workbook1.setSheetName(workbook1.getSheetIndex(workbook1.getSheet(sv.getCode() + "_BANG_DIEM")),
						sv.getCode() + "_BANG_DIEM-QGH");
			}
			sheet.addMergedRegion(new CellRangeAddress(demDong, demDong, 0, 5));// Tin chi tich luy
			setBorderMergerOutLeft(demDong, demDong, 0, 5, sheet);
			sheet.addMergedRegion(new CellRangeAddress(demDong, demDong, 6, 12));// TCTL:165
			setBorderMergerOutRight(demDong, demDong, 6, 12, sheet);
			sheet.addMergedRegion(new CellRangeAddress(demDong + 1, demDong + 2, 0, 5));// Diem trung binh
			setBorderMerger(demDong + 1, demDong + 2, 0, 5, sheet);
			sheet.addMergedRegion(new CellRangeAddress(demDong + 1, demDong + 1, 6, 9));// Thang diem 10
			setBorderMergerOutLeft(demDong + 1, demDong + 1, 6, 9, sheet);
			sheet.addMergedRegion(new CellRangeAddress(demDong + 1, demDong + 1, 10, 12));// 10.0
			setBorderMergerOutRight(demDong + 1, demDong + 1, 10, 12, sheet);

			sheet.addMergedRegion(new CellRangeAddress(demDong + 2, demDong + 2, 6, 9));// Thang diem 4
			setBorderMergerOutLeft(demDong + 2, demDong + 2, 5, 8, sheet);
			sheet.addMergedRegion(new CellRangeAddress(demDong + 2, demDong + 2, 10, 12));// 4.0
			setBorderMergerOutRight(demDong + 2, demDong + 2, 9, 12, sheet);

			sheet.addMergedRegion(new CellRangeAddress(demDong + 4, demDong + 4, 14, 26));// Ngay thang nam
			sheet.addMergedRegion(new CellRangeAddress(demDong + 5, demDong + 5, 14, 26));// Ngay thang nam
			sheet.addMergedRegion(new CellRangeAddress(demDong + 5, demDong + 5, 0, 1));// Ghi chu
			sheet.addMergedRegion(new CellRangeAddress(demDong + 5, demDong + 5, 2, 11));// cap 1 lan khi ra truong
			sheet.addMergedRegion(new CellRangeAddress(demDong + 6, demDong + 6, 14, 26));// TL.hieu truong
			sheet.addMergedRegion(new CellRangeAddress(demDong + 7, demDong + 7, 14, 26));// Truong phong dao tao
		}
		return status;
	}

}
