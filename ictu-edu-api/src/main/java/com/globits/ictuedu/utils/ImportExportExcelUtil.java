package com.globits.ictuedu.utils;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.globits.ictuedu.domain.Student;
import com.globits.ictuedu.domain.Transcript;
import com.globits.ictuedu.dto.StudentDto;
import com.globits.ictuedu.dto.TranscriptDto;
import org.apache.poi.ss.usermodel.DateUtil;

@Component
public class ImportExportExcelUtil {
	static SimpleDateFormat sdf = new SimpleDateFormat();

	public static void handleRow(Row currentRow, StudentDto studentDto, Set<TranscriptDto> transcripts) {
		// Một row sẽ có 2 transcript
		TranscriptDto transcriptDto1 = new TranscriptDto();
		TranscriptDto transcriptDto2 = new TranscriptDto();
		int cellIndex = 0;
		int lastCellIndex = 20;
		Cell currentCell = null;
		Cell previousCell = null;
		Cell nextCell = null;
		if (currentRow != null) {
			while (cellIndex <= lastCellIndex) {
				if (cellIndex != 0) {
					previousCell = currentRow.getCell(cellIndex - 1);
				}
				currentCell = currentRow.getCell(cellIndex);
				nextCell = currentRow.getCell(cellIndex + 1);
				if (cellIndex < 6) {
					// Lấy transcript1 ở nửa bảng đầu
					ImportExportExcelUtil.handleCell(currentCell, previousCell, nextCell, studentDto, transcriptDto1);
					// Chỉ khi các trường đã được điền đầy đủ mới add vào Set
					if (transcriptDto1.getCredit() != null && transcriptDto1.getMark() != null
							&& transcriptDto1.getModule() != null && transcriptDto1.getScore() != null) {
						transcripts.add(transcriptDto1);
					}
				} else {
					// Lấy transcript2 ở nửa bảng sau
					ImportExportExcelUtil.handleCell(currentCell, previousCell, nextCell, studentDto, transcriptDto2);
					// Chỉ khi các trường đã được điền đầy đủ mới add vào Set
					if (transcriptDto2.getCredit() != null && transcriptDto2.getMark() != null
							&& transcriptDto2.getModule() != null && transcriptDto2.getScore() != null) {
						transcripts.add(transcriptDto2);
					}
				}
				cellIndex++;
			}
		}
	}

	public static void handleCell(Cell currentCell, Cell previousCell, Cell nextCell, StudentDto studentDto,
			TranscriptDto transcriptDto) {
		String valueOfCurrentCell;
		String valueOfPreviousCell;
		String valueOfNextCell;

		// Lay gia tri cac cell
		valueOfCurrentCell = ImportExportExcelUtil.getCellValue(currentCell);
		valueOfPreviousCell = ImportExportExcelUtil.getCellValue(previousCell);
		valueOfNextCell = ImportExportExcelUtil.getCellValue(nextCell);

		// Kiểm tra giá trị cell đằng trước để xem cell hiện tại thuộc trường nào, và
		// đặt trường đó cho sinh viên
		ImportExportExcelUtil.cellCheck(valueOfCurrentCell, valueOfPreviousCell, valueOfNextCell, studentDto,
				transcriptDto);
	}

	// Kiểm tra giá trị cell đằng trước để xem cell hiện tại thuộc trường nào, và
	// đặt trường đó cho sinh viên
	public static void cellCheck(String valueOfCurrentCell, String valueOfPreviousCell, String valueOfNextCell,
			StudentDto studentDto, TranscriptDto transcriptDto) {
		if (valueOfPreviousCell != null && valueOfPreviousCell.equals("Họ và tên:")) {
			studentDto.setDisplayName(valueOfCurrentCell);
		} else if (valueOfPreviousCell != null && valueOfPreviousCell.equals("Ngành:")) {
			studentDto.setMajors(valueOfCurrentCell);
		} else if (valueOfPreviousCell != null && valueOfPreviousCell.equals("Nơi sinh:")) {
			studentDto.setPlaceOfBirth(valueOfCurrentCell);
		} else if (valueOfPreviousCell != null && valueOfPreviousCell.equals("Chuyên ngành:")) {
			studentDto.setSpecialized(valueOfCurrentCell);
		} else if (valueOfPreviousCell != null && valueOfPreviousCell.equals("Mã SV:")) {
			studentDto.setCode(valueOfCurrentCell);
		} else if (valueOfPreviousCell != null && valueOfPreviousCell.equals("Lớp:")) {
			studentDto.setStudentClass(valueOfNextCell);
		} else if (valueOfPreviousCell != null && valueOfPreviousCell.equals("Khóa:")) {
			studentDto.setCourse(valueOfCurrentCell);
		} else if (valueOfPreviousCell != null && valueOfPreviousCell.equals("Hệ đào tạo:")) {
			studentDto.setTrainingSystem(valueOfCurrentCell);
		} else if (valueOfPreviousCell != null && valueOfPreviousCell.equals("Họ và tên:")) {
			studentDto.setDisplayName(valueOfCurrentCell);
		} else {
			Integer intValueOfPreviousCell = tryParseInt(valueOfPreviousCell);
			Integer intValueOfCurrentCell = tryParseInt(valueOfCurrentCell);
			Integer intValueOfNextCell = tryParseInt(valueOfNextCell);

			Double doubleValueOfPreviousCell = tryParseDouble(valueOfPreviousCell);
			Double doubleValueOfCurrentCell = tryParseDouble(valueOfCurrentCell);
			Double doubleValueOfNextCell = tryParseDouble(valueOfNextCell);

			// Nếu trường nào là string, bị kẹp giữa 2 trường integer thì trường đó là tên
			// học phần
			if (intValueOfPreviousCell != null && intValueOfNextCell != null && intValueOfCurrentCell == null) {
				transcriptDto.setModule(valueOfCurrentCell);
			}

			// Nếu trường nào là int, bị kẹp giữa 1 trường string và 1 trường double thì
			// trường đó là tín chỉ
			else if (intValueOfPreviousCell == null && doubleValueOfPreviousCell == null
					&& doubleValueOfNextCell != null && intValueOfCurrentCell != null) {
				transcriptDto.setCredit(valueOfCurrentCell);
			}

			// Nếu trường nào là double, bị kẹp giữa 1 trường int và 1 trường string thì
			// trường đó là điểm bằng số
			else if (intValueOfPreviousCell != null && intValueOfNextCell == null && doubleValueOfNextCell == null
					&& doubleValueOfCurrentCell != null) {
				transcriptDto.setScore(valueOfCurrentCell);
			}

			// Nếu trường nào là string, bị kẹp giữa 1 trường double và 1 trường rỗng thì
			// trường đó là điểm bằng chữ
			else if (doubleValueOfPreviousCell != null && valueOfNextCell == null && intValueOfCurrentCell == null) {
				transcriptDto.setMark(valueOfCurrentCell);
			}
		}
	}

	public static String getCellValue(Cell cell) { 
		String value = "";
		  if (cell != null && cell.getCellTypeEnum() == CellType.STRING && cell.getStringCellValue() != null) { 
			value = cell.getStringCellValue().trim();
		} else if (cell != null && cell.getCellTypeEnum() == CellType.NUMERIC && cell.getNumericCellValue() > 0) {
			value = String.valueOf(cell.getNumericCellValue());
		} else {
			value = "";
		} 
		return value;
	}

	public static Integer tryParseInt(String text) {
		try {
			return Integer.parseInt(text);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static Double tryParseDouble(String text) {
		try {
			return Double.parseDouble(text);
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
