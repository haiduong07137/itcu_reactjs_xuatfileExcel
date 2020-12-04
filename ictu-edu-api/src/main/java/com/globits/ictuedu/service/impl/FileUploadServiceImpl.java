package com.globits.ictuedu.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.globits.ictuedu.Constants;
import com.globits.ictuedu.domain.Appendix;
import com.globits.ictuedu.domain.Student;
import com.globits.ictuedu.domain.Transcript;
import com.globits.ictuedu.dto.AppendixDto;
import com.globits.ictuedu.dto.StudentDto;
import com.globits.ictuedu.dto.UploadActivityLogDto;
import com.globits.ictuedu.repository.AppendixRepository;
import com.globits.ictuedu.repository.StudentRepository;
import com.globits.ictuedu.repository.TranscriptRepository;
import com.globits.ictuedu.service.FileUploadService;
import com.globits.ictuedu.service.StudentService;
import com.globits.ictuedu.service.UploadActivityLogService;
import com.globits.ictuedu.utils.ImportExportExcelUtil;
import com.globits.ictuedu.utils.NumberUtils;

@Service
public class FileUploadServiceImpl implements FileUploadService {
	@Autowired
	StudentRepository studentRepository;

	@Autowired
	AppendixRepository appendixRepository;
	@Autowired
	StudentService studentService;

	@Autowired
	AppendixServiceImpl appendixService;
	@Autowired
	TranscriptRepository transcriptRepository;
	@Autowired
	UploadActivityLogService uploadActivityLogService;
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	// Import diem, doc file excel từ file upload, doc tung sheet xong đọc tung dong
	public UploadActivityLogDto importStudentTranscriptFromInputStream(MultipartFile[] uploadfiles) {
		UploadActivityLogDto status = new UploadActivityLogDto();
		status.setStatus(Constants.SUCCESS);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		status.setUploadTime(timestamp);
		String warning = "";
		for (MultipartFile uploadfile : uploadfiles) {
			try {
				ByteArrayInputStream bis = new ByteArrayInputStream(uploadfile.getBytes());
				status.setFileName(uploadfile.getOriginalFilename());

				/* try { */
				@SuppressWarnings("resource")
				Workbook workbook;
				try {
					workbook = new HSSFWorkbook(bis);
					int numberOfSheets = workbook.getNumberOfSheets(); // lay so sheet
					// Thao tac voi tung sheet
					for (int i = 0; i < numberOfSheets; i++) {
						Sheet datatypeSheet = workbook.getSheetAt(i);// get sheet
						warning += handleSheet(datatypeSheet, uploadfile.getOriginalFilename(), false);
					}
					if (warning.length() == 0) {
						warning = validateStudentCodeUploadExcel(workbook);
						if (warning.length() > 0) {
							status.setStatus(Constants.FAILED);
							String warn = "Upload bảng điểm sinh viên lỗi. Xin mời kiểm tra lại file và upload lại: "
									+ uploadfile.getOriginalFilename() + ". Tại các Sheet sau: \n " + warning;
							status.setLogDetail(warn);
							uploadActivityLogService.saveOneOrUpdate(status, null);
							return status;
						}
						for (int i = 0; i < numberOfSheets; i++) {
							Sheet datatypeSheet = workbook.getSheetAt(i);// get sheet
							handleSheet(datatypeSheet, uploadfile.getOriginalFilename(), true);

						}
					} else if (warning.length() > 0) {
						status.setStatus(Constants.FAILED);
						String warn = "Upload bảng điểm sinh viên lỗi. Xin mời kiểm tra lại file và upload lại: "
								+ uploadfile.getOriginalFilename() + ". Tại các Sheet sau: \n " + warning;
						status.setLogDetail(warn);
						uploadActivityLogService.saveOneOrUpdate(status, null);
						return status;
					}

				} catch (IOException e) {
					status.setStatus(Constants.FAILED);
					String warn = "Upload bảng điểm sinh viên lỗi. Xin mời kiểm tra lại file và upload lại: "
							+ uploadfile.getOriginalFilename();
					status.setLogDetail(warn);
					uploadActivityLogService.saveOneOrUpdate(status, null);
				}
			} catch (Exception e) {
				status.setStatus(Constants.FAILED);
				String warn = "Upload bảng điểm sinh viên lỗi. Xin mời kiểm tra lại file và upload lại: "
						+ uploadfile.getOriginalFilename();
				status.setLogDetail(warn);
				uploadActivityLogService.saveOneOrUpdate(status, null);
			}
		}
		uploadActivityLogService.saveOneOrUpdate(status, null);
		return status;
	}

	// Import diem, doc file excel từ file upload, doc tung sheet xong đọc tung dong
	public UploadActivityLogDto importThongTinPhuLucInputStream(MultipartFile[] uploadfiles) {
		UploadActivityLogDto status = new UploadActivityLogDto();

		status.setStatus(Constants.SUCCESS);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		status.setUploadTime(timestamp);
		String warning = "";
		for (MultipartFile uploadfile : uploadfiles) {
			try {
				ByteArrayInputStream bis = new ByteArrayInputStream(uploadfile.getBytes());
				status.setFileName(uploadfile.getOriginalFilename());
				/* try { */
				@SuppressWarnings("resource")
				Workbook workbook;
				try {
					workbook = new HSSFWorkbook(bis);
					int numberOfSheets = workbook.getNumberOfSheets(); // lay so sheet

					for (int i = 0; i < numberOfSheets; i++) {
						Sheet datatypeSheet = workbook.getSheetAt(i);// get sheet
						warning += handleSheetThongTinPhuLuc(datatypeSheet, uploadfile.getOriginalFilename(), false);
					}
					if (warning.length() == 0) {
						warning = validateStudentCodePhuLuc(workbook);
						if (warning.length() > 0) {
							status.setStatus(Constants.FAILED);
							String warn = "Upload bảng phụ lục lỗi. Xin mời kiểm tra lại file và upload lại: "
									+ uploadfile.getOriginalFilename() + ". Tại các Sheet sau: \n " + warning;
							status.setLogDetail(warn);
							uploadActivityLogService.saveOneOrUpdate(status, null);
							return status;
						}
						for (int i = 0; i < numberOfSheets; i++) {
							Sheet datatypeSheet = workbook.getSheetAt(i);// get sheet
							handleSheetThongTinPhuLuc(datatypeSheet, uploadfile.getOriginalFilename(), true);

						}
					} else if (warning.length() > 0) {
						status.setStatus(Constants.FAILED);
						String warn = "Upload bảng phụ lục lỗi. Xin mời kiểm tra lại file và upload lại: "
								+ uploadfile.getOriginalFilename() + ". Tại các Sheet sau: \n " + warning;
						status.setLogDetail(warn);
						uploadActivityLogService.saveOneOrUpdate(status, null);
						return status;
					}

				} catch (IOException e) {
					status.setStatus(Constants.FAILED);
					String warn = "Upload bảng phụ lục lỗi. Xin mời kiểm tra lại file và upload lại: "
							+ uploadfile.getOriginalFilename();
					status.setLogDetail(warn);
					uploadActivityLogService.saveOneOrUpdate(status, null);
				}
			} catch (Exception e) {
				status.setStatus(Constants.FAILED);
				String warn = "Upload bảng phụ lục lỗi. Xin mời kiểm tra lại file: " + uploadfile.getOriginalFilename();
				status.setLogDetail(warn);
				uploadActivityLogService.saveOneOrUpdate(status, null);

			}
		}
		uploadActivityLogService.saveOneOrUpdate(status, null);
		return status;
	}

	public String handleSheetThongTinPhuLuc(Sheet datatypeSheet, String fromUploadedFileName, Boolean checkFile) {
		String statusReturn = "";
//		UUID studentId = studentService.getStudentIdFromCode(code);
		String validatePhuluc = "";
		int startMarkIndex = 1;
		while (true) {
			if (datatypeSheet.getRow(startMarkIndex) == null || NumberUtils.isNumeric(
					ImportExportExcelUtil.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(0))) == false) {
				break;
			}

			String code = "";
			String name = "";
			String dateOfBirth = "";
			String gender = "";
			String placeOfBirth = "";
			String typeTraining = "";
			String ethnicity = "";
			String studentClass = "";
			String nationality = "";
			String allCredit = "";
			String scoreFour = "";
			String scoreTen = "";
			String rankGraduating = "";
			String levelTraining = "";
			String trainingSystem = "";
			String dateJoin = "";
			String diploma = "";
			String majors = "";
			String note = "";
			String dateOut = "";
			String languageTraining = "";
			Appendix appendix = new Appendix();
			code = ImportExportExcelUtil.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(1));
			String firstName = ImportExportExcelUtil.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(2));
			String lastName = ImportExportExcelUtil.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(3));
			name = firstName + " " + lastName;

			dateOfBirth = ImportExportExcelUtil.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(4));
			gender = ImportExportExcelUtil.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(5));
			placeOfBirth = ImportExportExcelUtil.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(6));
			ethnicity = ImportExportExcelUtil.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(7));
			nationality = ImportExportExcelUtil.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(8));
			allCredit = ImportExportExcelUtil.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(9));
			scoreTen = ImportExportExcelUtil.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(10));
			scoreFour = ImportExportExcelUtil.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(11));
			rankGraduating = ImportExportExcelUtil.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(12));
			studentClass = ImportExportExcelUtil.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(13));
			trainingSystem = ImportExportExcelUtil.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(14));
			majors = ImportExportExcelUtil.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(15));
			typeTraining = ImportExportExcelUtil.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(16));
			dateJoin = ImportExportExcelUtil.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(18));
			dateOut = ImportExportExcelUtil.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(19));
			diploma = ImportExportExcelUtil.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(20));
			languageTraining = ImportExportExcelUtil.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(21));
			note = ImportExportExcelUtil.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(22));
			levelTraining = ImportExportExcelUtil.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(17));
			appendix.setFullName(name == null ? "" : name);
			appendix.setBirthday(dateOfBirth == null ? "" : dateOfBirth);
			appendix.setPlaceOfBirth(placeOfBirth == null ? "" : placeOfBirth);
			appendix.setTrainingSystem(trainingSystem == null ? "" : trainingSystem);
			appendix.setStudentClass(studentClass == null ? "" : studentClass);
			appendix.setMajors(majors == null ? "" : majors);
			appendix.setCode(code == null ? "" : code);
			if (allCredit == null) {
				allCredit = "";
			} else {
				if (allCredit.contains(".")) {
					allCredit = allCredit.substring(0, allCredit.lastIndexOf("."));
				}
			}
			appendix.setDateOut(dateOut);
			appendix.setTrainingLanguage(languageTraining);
			appendix.setAllCredit(allCredit);
			appendix.setFromUploadedFile(fromUploadedFileName == null ? "" : fromUploadedFileName);
			appendix.setScoreFour(scoreFour == null ? "" : scoreFour);
			appendix.setScoreTen(scoreTen == null ? "" : scoreTen);
			appendix.setGender(gender == null ? "" : gender);
			appendix.setEthnicity(ethnicity == null ? "" : ethnicity);
			appendix.setStudentClass(studentClass == null ? "" : studentClass);
			appendix.setRankGraduating(rankGraduating == null ? "" : rankGraduating);
			appendix.setNationality(nationality == null ? "" : nationality);
			appendix.setNumberDiploma(diploma == null ? "" : diploma);
			appendix.setDateJoin(dateJoin == null ? "" : dateJoin);
			appendix.setTypeTraining(typeTraining == null ? "" : typeTraining);
			appendix.setLevelTraining(levelTraining == null ? "" : levelTraining);
			appendix.setNote(note == null ? "" : note);

			validatePhuluc += validateAppendix(appendix, startMarkIndex);
			if (checkFile) {
				appendixRepository.save(appendix);
			}

			startMarkIndex++;
		}
		if (validatePhuluc.length() > 0) {
			statusReturn += " Sheet: " + datatypeSheet.getSheetName() + ": \n " + validatePhuluc;
		}
		return statusReturn;

	}

	public String validateStudentCodeUploadExcel(Workbook workbook) {
		List<String> list = new ArrayList<>();
		List<StudentDto> listCheck = new ArrayList<>();
		String status = "";
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			Sheet datatypeSheet = workbook.getSheetAt(i);//
			CellReference cr = new CellReference("F9");
			String code = datatypeSheet.getRow(cr.getRow()).getCell(cr.getCol()).getStringCellValue();
			list.add(code);
		}
		listCheck = appendixService.checkDuplicateStudentCode(list);
		if (listCheck.size() > 0) {
			status = "Các mã sinh viên sau đã tồn tại: \n";
			for (int i = 0; i < listCheck.size(); i++) {
				status += listCheck.get(i).getCode() + " ; ";
				if (i % 2 != 0) {
					status += "\n";
				}
			}
		}
		return status;
	}

	public String validateStudentCodePhuLuc(Workbook workbook) {
		List<String> list = new ArrayList<>();
		List<AppendixDto> listCheck = new ArrayList<>();
		String status = "";
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			Sheet datatypeSheet = workbook.getSheetAt(i);//
			int startMarkIndex = 1;
			while (true) {
				if (datatypeSheet.getRow(startMarkIndex) == null) {
					break;
				}
				String code = "";
				code = ImportExportExcelUtil.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(1));
				list.add(code);
				startMarkIndex++;
			}
		}
		listCheck = appendixService.checkDuplicateStudentCodeAppendix(list);
		if (listCheck.size() > 0) {
			status = "Các mã sinh viên sau đã tồn tại: \n";
			for (int i = 0; i < listCheck.size(); i++) {
				status += listCheck.get(i).getCode() + " ; ";
				if (i % 2 != 0) {
					status += "\n";
				}
			}
		}
		return status;

	}

	public String validateAppendix(Appendix trans, Integer index) {
		index++;
		String status = "";
		if (trans.getCode().length() == 0) {
			status += "- Thiếu mã sinh viên tại ô B" + index + ".\n";
		}
		if (trans.getFullName().length() == 0) {
			status += "- Thiếu họ tên tại ô D" + index + ".\n";
		}
		if (trans.getBirthday().length() == 0) {
			status += "- Thiếu ngày sinh tại ô E" + index + ".\n";
		}
		if (trans.getGender().length() == 0) {
			status += "- Thiếu giới tính tại ô F" + index + ".\n";
		}
		if (trans.getPlaceOfBirth().length() == 0) {
			status += "- Thiếu nơi sinh tại ô G" + index + ".\n";
		}
		if (trans.getEthnicity().length() == 0) {
			status += "- Thiếu dân tộc tại ô H" + index + ".\n";
		}
		if (trans.getNationality().length() == 0) {
			status += "- Thiếu quốc tịch tại ô I" + index + ".\n";
		}
		if (trans.getAllCredit().length() == 0) {
			status += "- Thiếu TCTL tại ô J" + index + ".\n";
		}
		if (trans.getScoreTen().length() == 0) {
			status += "- Thiếu điểm hệ 10 tại ô K" + index + ".\n";
		}
		if (trans.getScoreFour().length() == 0) {
			status += "- Thiếu điểm hệ 4 tại ô L" + index + ".\n";
		}
		if (trans.getRankGraduating().length() == 0) {
			status += "- Thiếu xếp loại tốt nghiệp tại ô M" + index + ".\n";
		}
		if (trans.getStudentClass().length() == 0) {
			status += "- Thiếu lớp tại ô N" + index + ".\n";
		}

		if (trans.getTrainingSystem().length() == 0) {
			status += "- Thiếu hệ đào tạo tại ô O" + index + ".\n";
		}
		if (trans.getMajors().length() == 0) {
			status += "- Thiếu ngành tại ô P" + index + ".\n";
		}
		if (trans.getLevelTraining().length() == 0) {
			status += "- Thiếu trình độ đào tạo tại ô R" + index + ".\n";
		}

		if (trans.getTypeTraining().length() == 0) {
			status += "- Thiếu hình thức đào tạo tại ô Q" + index + ".\n";
		}
		if (trans.getDateJoin().length() == 0) {
			status += "- Thiếu ngày nhập học tại ô S" + index + ".\n";
		}
		if (trans.getDateOut().length() == 0) {
			status += "- Thiếu ngày tốt nghiệp tại ô T" + index + ".\n";
		}
		if (trans.getNumberDiploma().length() == 0) {
			status += "- Thiếu số hiệu bằng tại ô U" + index + ".\n";
		}
		if (trans.getTrainingLanguage().length() == 0) {
			status += "- Thiếu ngôn ngữ đào tạo tại ô V" + index + ".\n";
		}

		return status;
	}

	public String validateStudent(Student dto) {
		String status = "";
		if (dto.getDisplayName().length() == 0) {
			status += "- Thiếu họ tên tại ô F6. \n";
		}
		if (dto.getBirthDate() == null) {
			status += "- Thiếu ngày sinh tại ô F7. \n";
		}
		if (dto.getPlaceOfBirth().length() == 0) {
			status += "- Thiếu nơi sinh tại ô F8. \n";
		}
		if (dto.getTrainingSystem().length() == 0) {
			status += "- Thiếu Hệ đào tạo tại ô S9. \n";
		}
		if (dto.getCourse().length() == 0) {
			status += "- Thiếu Khoá học tại ô X8. \n";
		}

		if (dto.getStudentClass().length() == 0) {
			status += "- Thiếu Lớp học sinh tại ô R8. \n";
		}

		if (dto.getSpecialized().length() == 0) {
			status += "- Thiếu Chuyên ngành tại ô T7. \n";
		}
		if (dto.getMajors().length() == 0) {
			status += "- Thiếu Ngành học tại ô S6. \n";
		}
		if (dto.getScoreFour().length() == 0) {
			status += "- Thiếu Thang điểm 4 \n";
		}
		if (dto.getScoreTen().length() == 0) {
			status += "- Thiếu Thang điểm 10 \n";
		}
		if (dto.getRankGraduating().length() == 0) {
			status += "- Thiếu Xếp loại tốt nghiệp \n";
		}
//	
//		if (dto.getRankConduct().length() == 0) {
//			status += "- Thiếu Xếp loại rèn luyện \n";
//		}
		return status;
	}

	public String validateTranscript(Transcript trans, Integer index) {

		String status = "";
		if (trans.getModule().length() == 0) {
			status += "- Thiếu tên môn học tại ô B" + index + ".\n";
		}
		if (trans.getCredit().length() == 0) {
			status += "- Thiếu tín chỉ tại ô K" + index + ".\n";
		}
		if (trans.getScore().length() == 0) {
			status += "- Thiếu điểm số tại ô L" + index + ".\n";
		}
		if (trans.getMark().length() == 0) {
			status += "- Thiếu điểm chữ tại ô M" + index + ".\n";
		}
		return status;
	}

	public String validateTranscriptCol2(Transcript trans, Integer index) {

		String status = "";
		if (trans.getModule().length() == 0) {
			status += "- Thiếu tên môn học tại ô P" + index + ".\n";
		}
		if (trans.getCredit().length() == 0) {
			status += "- Thiếu tín chỉ tại ô Y" + index + ".\n";
		}
		if (trans.getScore().length() == 0) {
			status += "- Thiếu điểm số tại ô Z" + index + ".\n";
		}
		if (trans.getMark().length() == 0) {
			status += "- Thiếu điểm chữ tại ô AA" + index + ".\n";
		}
		return status;
	}

	public String handleSheet(Sheet datatypeSheet, String fromUploadedFileName, Boolean checkFile) {

		String status = "";
		String validateTranscrip = "";
		Student student = new Student();
		Set<Transcript> transcripts = new HashSet<Transcript>();
		String name = "";
		String dateOfBirth = "";
		String placeOfBirth = "";
		String majors = "";
		String specialized = "";
		String studentClass = "";
		String trainingSystem = "";
		String code = "";
		String course = "";
		String rankConduct = "";
		CellReference cr = new CellReference("F6");

		try {
			name = datatypeSheet.getRow(cr.getRow()).getCell(cr.getCol()).getStringCellValue();
		} catch (Exception e) {
		}
		try {
			cr = new CellReference("F7");
			dateOfBirth = datatypeSheet.getRow(cr.getRow()).getCell(cr.getCol()).getStringCellValue();
		} catch (Exception e) {
		}

		try {
			cr = new CellReference("F8");
			placeOfBirth = datatypeSheet.getRow(cr.getRow()).getCell(cr.getCol()).getStringCellValue();

		} catch (Exception e) {
		}
		try {
			cr = new CellReference("F9");
			code = datatypeSheet.getRow(cr.getRow()).getCell(cr.getCol()).getStringCellValue();
		} catch (Exception e) {
		}
		try {

			cr = new CellReference("S6");
			majors = datatypeSheet.getRow(cr.getRow()).getCell(cr.getCol()).getStringCellValue();

		} catch (Exception e) {
		}
		try {
			cr = new CellReference("T7");
			specialized = datatypeSheet.getRow(cr.getRow()).getCell(cr.getCol()).getStringCellValue();
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			cr = new CellReference("R8");
			studentClass = datatypeSheet.getRow(cr.getRow()).getCell(cr.getCol()).getStringCellValue();
		} catch (Exception e) {
		}
		try {
			cr = new CellReference("S9");
			trainingSystem = datatypeSheet.getRow(cr.getRow()).getCell(cr.getCol()).getStringCellValue();
		} catch (Exception e) {
		}
		try {

			cr = new CellReference("X8");
			course = datatypeSheet.getRow(cr.getRow()).getCell(cr.getCol()).getStringCellValue();
		} catch (Exception e) {
		}

		student.setDisplayName(name);
		try {
			student.setBirthDate(sdf.parse(dateOfBirth));
		} catch (ParseException e) {
			try {
				student.setBirthDate(sdf.parse("01/01/1970"));
			} catch (Exception e2) {
			}
		}
		student.setPlaceOfBirth(placeOfBirth);
		student.setTrainingSystem(trainingSystem);
		student.setCourse(course);
		student.setStudentClass(studentClass);
		student.setSpecialized(specialized);
		student.setMajors(majors);
		student.setCode(code);
		student.setFromUploadedFile(fromUploadedFileName);
//		UUID studentId = studentService.getStudentIdFromCode(code);

		if (student != null) {
			int startMarkIndex = 11;
			while (NumberUtils
					.isNumeric(ImportExportExcelUtil.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(0)))) {
				String module = "";
				String credit = "";
				String score = "";
				String mark = "";
				Transcript transcript = new Transcript();
				String moduleOrder = ImportExportExcelUtil
						.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(0));
				module = ImportExportExcelUtil.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(1));
				credit = ImportExportExcelUtil.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(10));
				score = ImportExportExcelUtil.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(11));
				mark = ImportExportExcelUtil.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(12));
				transcript.setModule(module == null ? "" : module);
				transcript.setCredit(credit == null ? "" : credit);
				transcript.setScore(score == null ? "" : score);
				transcript.setMark(mark == null ? "" : mark);
				Double moduleOrderTran = 0.0;
				try {
					moduleOrderTran = Double.parseDouble(moduleOrder);
				} catch (Exception e) {
					// TODO: handle exception
				}
				transcript.setModuleOrder(moduleOrderTran);
				validateTranscrip += validateTranscript(transcript, startMarkIndex);
				transcripts.add(transcript);
				transcript.setStudent(student);
				startMarkIndex++;
			}
			String score10 = ImportExportExcelUtil.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(9));
			String score4 = ImportExportExcelUtil.getCellValue(datatypeSheet.getRow(startMarkIndex + 1).getCell(9));

			String rankGraduating = ImportExportExcelUtil
					.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(22));

			rankConduct = ImportExportExcelUtil.getCellValue(datatypeSheet.getRow(startMarkIndex + 2).getCell(5));
			if (score4 == null) {
				student.setScoreFour("");
			} else {
				if (score4.length() == 1) {
					student.setScoreFour(score4 + ".00");
				} else {
					student.setScoreFour(score4);
				}
			}

			if (score10 == null) {
				student.setScoreTen("");
			} else {
				if (score10.length() == 1) {
					student.setScoreTen(score10 + ".00");
				} else {
					student.setScoreTen(score10);
				}
			}
			student.setRankGraduating(rankGraduating == null ? "" : rankGraduating);
			student.setRankConduct(rankConduct == null ? "" : rankConduct);
			startMarkIndex = 11;
			while (NumberUtils
					.isNumeric(ImportExportExcelUtil.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(14)))) {
				Transcript transcript = new Transcript();
				String moduleOrder = ImportExportExcelUtil
						.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(14));
				String module = ImportExportExcelUtil.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(15));
				String credit = ImportExportExcelUtil.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(24));
				String score = ImportExportExcelUtil.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(25));
				String mark = ImportExportExcelUtil.getCellValue(datatypeSheet.getRow(startMarkIndex).getCell(26));
				transcript.setModule(module == null ? "" : module);
				transcript.setCredit(credit == null ? "" : credit);
				transcript.setScore(score == null ? "" : score);
				transcript.setMark(mark == null ? "" : mark);
				Double moduleOrderTran = 0.0;
				try {
					moduleOrderTran = Double.parseDouble(moduleOrder);
				} catch (Exception e) {
					// TODO: handle exception
				}
				transcript.setModuleOrder(moduleOrderTran);
				transcripts.add(transcript);
				validateTranscrip += validateTranscriptCol2(transcript, startMarkIndex);
				transcript.setStudent(student);
				startMarkIndex++;
			}
			String validateStudent = validateStudent(student);
			if (validateStudent.length() > 0 || validateTranscrip.length() > 0) {
				status += " Sheet: " + datatypeSheet.getSheetName() + ": \n " + validateStudent + validateTranscrip
						+ " \n ";
			}

			if (checkFile) {
				studentRepository.save(student);
				transcriptRepository.saveAll(transcripts);
			}
		}
		return status;
	}

}
