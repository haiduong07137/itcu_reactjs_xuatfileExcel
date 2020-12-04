package com.globits.ictuedu.utils;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

import com.globits.ictuedu.domain.Student;
import com.globits.ictuedu.dto.TranscriptDto;
import com.globits.ictuedu.dto.searchdto.StudentSearchDto;

public class CreateExcel {

	public void createSheetForStudent(HSSFWorkbook workbook, Student sv, List<TranscriptDto> list,
			StudentSearchDto dto) {

		HSSFSheet sheet = workbook.createSheet(sv.getCode());

		sheet.getPrintSetup().setLandscape(false);
		sheet.getPrintSetup().setPaperSize(PrintSetup.A4_PAPERSIZE);
		sheet.setMargin(Sheet.HeaderMargin, 0.59);
		sheet.setMargin(Sheet.BottomMargin, 0.51);
		sheet.setMargin(Sheet.LeftMargin, 0.11);
		sheet.setMargin(Sheet.RightMargin, 0.11);
		sheet.setMargin(Sheet.TopMargin, 0.39);
		sheet.setMargin(Sheet.FooterMargin, 0.51);
		sheet.setFitToPage(true);
		PrintSetup ps = sheet.getPrintSetup();
		ps.setFitWidth((short) 1);
		ps.setFitHeight((short) 0);
///////////////////////////////
		HSSFCellStyle styleBasic = workbook.createCellStyle();
		HSSFFont fontBasic = workbook.createFont();
		fontBasic.setFontName("Times New Roman");
		styleBasic.setFont(fontBasic);
/////////////////////////

///////////////////////////////
		HSSFCellStyle styleCenterBacsic12 = workbook.createCellStyle();
		HSSFFont fontCB12 = workbook.createFont();
		fontCB12.setFontHeightInPoints((short) 12);
		fontCB12.setFontName("Times New Roman");
		styleCenterBacsic12.setFont(fontCB12);
		styleCenterBacsic12.setAlignment(HorizontalAlignment.CENTER);
		styleCenterBacsic12.setVerticalAlignment(VerticalAlignment.CENTER);
/////////////////////////

///////////////////////////////
		HSSFCellStyle styleCenterBacsic = workbook.createCellStyle();
		HSSFFont fontCB = workbook.createFont();
		fontCB.setFontHeightInPoints((short) 9);
		fontCB.setFontName("Times New Roman");
		styleCenterBacsic.setBorderBottom(BorderStyle.THIN);
		styleCenterBacsic.setBorderTop(BorderStyle.THIN);
		styleCenterBacsic.setBorderLeft(BorderStyle.THIN);
		styleCenterBacsic.setBorderRight(BorderStyle.THIN);
		styleCenterBacsic.setFont(fontCB);
		styleCenterBacsic.setAlignment(HorizontalAlignment.CENTER);
		styleCenterBacsic.setVerticalAlignment(VerticalAlignment.CENTER);
/////////////////////////

///////////////////////////////
		HSSFCellStyle styleBoldCenter = workbook.createCellStyle();
		HSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short) 12);
		font.setFontName("Times New Roman");
		styleBoldCenter.setFont(font);
		styleBoldCenter.setAlignment(HorizontalAlignment.CENTER);
		styleBoldCenter.setVerticalAlignment(VerticalAlignment.CENTER);
/////////////////////////

///////////////////////////////
		HSSFCellStyle styleItalic = workbook.createCellStyle();
		HSSFFont fontItalic = workbook.createFont();
		fontItalic.setItalic(true);
		fontItalic.setFontHeightInPoints((short) 11);
		fontItalic.setFontName("Times New Roman");
		styleItalic.setFont(fontItalic);
		styleItalic.setAlignment(HorizontalAlignment.CENTER);
		styleItalic.setVerticalAlignment(VerticalAlignment.CENTER);
/////////////////////////

///////////////////////////////
		HSSFCellStyle styleItalicUnderline = workbook.createCellStyle();
		HSSFFont fontItalicU = workbook.createFont();
		fontItalicU.setItalic(true);
		fontItalicU.setFontHeightInPoints((short) 10);
		fontItalicU.setFontName("Times New Roman");
		fontItalicU.setUnderline(Font.U_SINGLE);
		styleItalicUnderline.setFont(fontItalicU);
		styleItalicUnderline.setAlignment(HorizontalAlignment.CENTER);
		styleItalicUnderline.setVerticalAlignment(VerticalAlignment.CENTER);
/////////////////////////

///////////////////////////////
		HSSFCellStyle styleBoldCenter11 = workbook.createCellStyle();
		HSSFFont fontNomarSize = workbook.createFont();
		fontNomarSize.setBold(true);
		fontNomarSize.setFontHeightInPoints((short) 11);
		fontNomarSize.setFontName("Times New Roman");
		styleBoldCenter11.setFont(fontNomarSize);
		styleBoldCenter11.setAlignment(HorizontalAlignment.LEFT);
		styleBoldCenter11.setVerticalAlignment(VerticalAlignment.CENTER);
/////////////////////////

///////////////////////////////
		HSSFCellStyle styleBoldCenterTopLeft11 = workbook.createCellStyle();
		HSSFFont fontNomarSizeTopLeft = workbook.createFont();
		fontNomarSizeTopLeft.setBold(true);
		fontNomarSizeTopLeft.setFontHeightInPoints((short) 11);
		fontNomarSizeTopLeft.setFontName("Times New Roman");
		styleBoldCenterTopLeft11.setFont(fontNomarSizeTopLeft);
		styleBoldCenterTopLeft11.setAlignment(HorizontalAlignment.LEFT);
		styleBoldCenterTopLeft11.setVerticalAlignment(VerticalAlignment.TOP);
/////////////////////////

///////////////////////////////
		HSSFCellStyle styleTopLeft10 = workbook.createCellStyle();
		HSSFFont fontTopLeft = workbook.createFont();
		fontTopLeft.setFontHeightInPoints((short) 10);
		fontTopLeft.setFontName("Times New Roman");
		styleTopLeft10.setFont(fontTopLeft);
		styleTopLeft10.setAlignment(HorizontalAlignment.LEFT);
		styleTopLeft10.setVerticalAlignment(VerticalAlignment.TOP);
/////////////////////////

///////////////////////////////
		HSSFCellStyle styleBoldCenter11RIGHT = workbook.createCellStyle();
		HSSFFont fontNomarSizeR = workbook.createFont();
		fontNomarSizeR.setBold(true);
		fontNomarSizeR.setFontHeightInPoints((short) 11);
		fontNomarSizeR.setFontName("Times New Roman");
		styleBoldCenter11RIGHT.setFont(fontNomarSizeR);
		styleBoldCenter11RIGHT.setAlignment(HorizontalAlignment.LEFT);
		styleBoldCenter11RIGHT.setVerticalAlignment(VerticalAlignment.CENTER);
/////////////////////////

///////////////////////////////
		HSSFCellStyle styleBorderBoldCenter10 = workbook.createCellStyle();
		HSSFFont fontNomarSize10 = workbook.createFont();
		fontNomarSize10.setBold(true);
		fontNomarSize10.setFontHeightInPoints((short) 10);
		fontNomarSize10.setFontName("Times New Roman");
		styleBorderBoldCenter10.setFont(fontNomarSize10);
		styleBorderBoldCenter10.setBorderBottom(BorderStyle.THIN);
		styleBorderBoldCenter10.setBorderTop(BorderStyle.THIN);
		styleBorderBoldCenter10.setBorderLeft(BorderStyle.THIN);
		styleBorderBoldCenter10.setBorderRight(BorderStyle.THIN);
		styleBorderBoldCenter10.setAlignment(HorizontalAlignment.CENTER);
		styleBorderBoldCenter10.setVerticalAlignment(VerticalAlignment.CENTER);
/////////////////////////

		HSSFCellStyle styleBoldCenterUnderline = workbook.createCellStyle();
		HSSFFont fontBoldCenterUnderLine = workbook.createFont();
		fontBoldCenterUnderLine.setUnderline(Font.U_SINGLE);
		fontBoldCenterUnderLine.setBold(true);
		fontBoldCenterUnderLine.setFontName("Times New Roman");
		fontBoldCenterUnderLine.setFontHeightInPoints((short) 13);
		styleBoldCenterUnderline.setFont(fontBoldCenterUnderLine);
		styleBoldCenterUnderline.setAlignment(HorizontalAlignment.CENTER);
		styleBoldCenterUnderline.setVerticalAlignment(VerticalAlignment.CENTER);
///////////////////////////////
		HSSFCellStyle styleBoldCenterSizeLarger = workbook.createCellStyle();
		HSSFFont fontBoldCenterSizeLarger = workbook.createFont();
		fontBoldCenterSizeLarger.setBold(true);
		fontBoldCenterSizeLarger.setFontName("Times New Roman");
		fontBoldCenterSizeLarger.setFontHeightInPoints((short) 16);
		styleBoldCenterSizeLarger.setFont(fontBoldCenterSizeLarger);
		styleBoldCenterSizeLarger.setAlignment(HorizontalAlignment.CENTER);
		styleBoldCenterSizeLarger.setVerticalAlignment(VerticalAlignment.CENTER);
/////////////////////////
		Cell cell;
		Row row;

		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 11));// dai hoc thai nguyen
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 12, 26));// cong hoa xa hoi
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 11));// truong dai hoc cong nghe
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 12, 26)); // doc lap tu do hanh phuc
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 11));// thong tin truyen tin
		for (int i = 0; i < 69; i++) {
			row = sheet.createRow(i);
			for (int j = 0; j < 69; j++) {
				row.createCell(j).setCellStyle(styleBasic);
				;
			}
		}

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		sheet.getRow(5).getCell(5).setCellValue(sv.getDisplayName()); // ho va ten
		sheet.getRow(5).getCell(18).setCellValue(sv.getMajors()); // nganh
		sheet.getRow(6).getCell(5).setCellValue(sdf.format(sv.getBirthDate())); // ngay sinh
		sheet.getRow(6).getCell(19).setCellValue(sv.getSpecialized()); // chuyen nganh
		sheet.getRow(7).getCell(5).setCellValue(sv.getPlaceOfBirth()); // noi sinh
		sheet.getRow(7).getCell(17).setCellValue(sv.getStudentClass()); // lop
		sheet.getRow(8).getCell(5).setCellValue(sv.getCode()); // ma sv

		sheet.getRow(3).setHeight((short) 160);
		sheet.getRow(4).setHeight((short) 448);// Do rong bang diem
		sheet.getRow(8).setHeight((short) 420);// do rong ma sinh vien
		sheet.addMergedRegion(new CellRangeAddress(4, 4, 0, 26));// bang diem
		sheet.addMergedRegion(new CellRangeAddress(5, 5, 1, 4));// ho va ten
		sheet.addMergedRegion(new CellRangeAddress(5, 5, 5, 14));// ten
		sheet.addMergedRegion(new CellRangeAddress(5, 5, 15, 17));// nganh
		sheet.addMergedRegion(new CellRangeAddress(5, 5, 18, 26));// ten nganh
		sheet.addMergedRegion(new CellRangeAddress(6, 6, 1, 4));// ngay sinh
		sheet.addMergedRegion(new CellRangeAddress(6, 6, 5, 14));// :15/6/9999
		sheet.addMergedRegion(new CellRangeAddress(6, 6, 15, 18));// chuyen nganh
		sheet.addMergedRegion(new CellRangeAddress(6, 6, 19, 26));// ten chuyen nganh
		sheet.addMergedRegion(new CellRangeAddress(7, 7, 1, 4));// noi sinh
		sheet.addMergedRegion(new CellRangeAddress(7, 7, 5, 14));// :LCai
		sheet.addMergedRegion(new CellRangeAddress(7, 7, 15, 16));// lop
		sheet.addMergedRegion(new CellRangeAddress(7, 7, 17, 26));// ten lop
		sheet.addMergedRegion(new CellRangeAddress(8, 8, 1, 4));// Ma sinh vien
		sheet.addMergedRegion(new CellRangeAddress(8, 8, 5, 14));// : ma sv

		sheet.getRow(0).getCell(0).setCellValue("ĐẠI HỌC THÁI NGUYÊN");
		sheet.getRow(0).getCell(0).setCellStyle(styleCenterBacsic12);
		sheet.getRow(0).getCell(12).setCellValue("CỘNG HOÀ XÃ HỘI CHỦ NGHĨA VIỆT NAM");
		sheet.getRow(0).getCell(12).setCellStyle(styleBoldCenter);
		sheet.getRow(1).getCell(0).setCellValue("TRƯỜNG ĐẠI HỌC CÔNG NGHỆ");
		sheet.getRow(1).getCell(0).setCellStyle(styleBoldCenter);
		sheet.getRow(1).getCell(12).setCellValue("Độc lập - Tự do - Hạnh Phúc");
		sheet.getRow(1).getCell(12).setCellStyle(styleBoldCenterUnderline);
		sheet.getRow(2).getCell(0).setCellValue("THÔNG TIN VÀ TRUYỀN THÔNG");
		sheet.getRow(2).getCell(0).setCellStyle(styleBoldCenter);
		sheet.getRow(4).getCell(0).setCellValue("BẢNG ĐIỂM");
		sheet.getRow(4).getCell(0).setCellStyle(styleBoldCenterSizeLarger);
		sheet.getRow(5).getCell(1).setCellValue("Họ và tên:");
		sheet.getRow(5).getCell(5).setCellStyle(styleBoldCenter11);
		sheet.getRow(5).getCell(15).setCellValue("Ngành:");
		sheet.getRow(5).getCell(18).setCellStyle(styleBoldCenter11RIGHT);
		sheet.getRow(6).getCell(1).setCellValue("Ngày sinh:");
		sheet.getRow(6).getCell(5).setCellStyle(styleBoldCenter11);
		sheet.getRow(6).getCell(15).setCellValue("Chuyên Ngành:");
		sheet.getRow(6).getCell(19).setCellStyle(styleBoldCenter11RIGHT);
		sheet.getRow(7).getCell(1).setCellValue("Nơi sinh:");
		sheet.getRow(7).getCell(5).setCellStyle(styleBoldCenter11);
		sheet.getRow(7).getCell(15).setCellValue("Lớp:");
		sheet.getRow(7).getCell(17).setCellStyle(styleBoldCenter11);
		sheet.getRow(8).getCell(1).setCellValue("Mã SV:");
		sheet.getRow(8).getCell(1).setCellStyle(styleTopLeft10);
		sheet.getRow(8).getCell(5).setCellStyle(styleBoldCenterTopLeft11);
		sheet.getRow(9).getCell(0).setCellValue("TT");
		sheet.getRow(9).getCell(0).setCellStyle(styleBorderBoldCenter10);
		sheet.getRow(9).getCell(1).setCellValue("Học phần");
		sheet.getRow(9).getCell(1).setCellStyle(styleBorderBoldCenter10);
		sheet.getRow(9).getCell(10).setCellValue("TC");
		sheet.getRow(9).getCell(10).setCellStyle(styleBorderBoldCenter10);
		sheet.getRow(9).getCell(11).setCellValue("Điểm");
		sheet.getRow(9).getCell(11).setCellStyle(styleBorderBoldCenter10);
		sheet.getRow(9).getCell(14).setCellValue("TT");
		sheet.getRow(9).getCell(14).setCellStyle(styleBorderBoldCenter10);
		sheet.getRow(9).getCell(15).setCellValue("Học phần");
		sheet.getRow(9).getCell(15).setCellStyle(styleBorderBoldCenter10);
		sheet.getRow(9).getCell(24).setCellValue("TC");
		sheet.getRow(9).getCell(24).setCellStyle(styleBorderBoldCenter10);
		sheet.getRow(9).getCell(25).setCellValue("Điểm");
		sheet.getRow(9).getCell(25).setCellStyle(styleBorderBoldCenter10);
		sheet.getRow(10).getCell(11).setCellValue("Số");
		sheet.getRow(10).getCell(12).setCellValue("Chữ");
		sheet.getRow(10).getCell(25).setCellValue("Số");
		sheet.getRow(10).getCell(26).setCellValue("Chữ");
		sheet.getRow(10).getCell(11).setCellStyle(styleBorderBoldCenter10);
		sheet.getRow(10).getCell(12).setCellStyle(styleBorderBoldCenter10);
		sheet.getRow(10).getCell(25).setCellStyle(styleBorderBoldCenter10);
		sheet.getRow(10).getCell(26).setCellStyle(styleBorderBoldCenter10);

		sheet.addMergedRegion(new CellRangeAddress(9, 10, 0, 0));// TT
		setBorderMerger(9, 10, 0, 0, sheet);
		sheet.addMergedRegion(new CellRangeAddress(9, 10, 1, 9));// : hoc phan
		setBorderMerger(9, 10, 1, 9, sheet);
		sheet.addMergedRegion(new CellRangeAddress(9, 10, 10, 10));// TC
		setBorderMerger(9, 10, 10, 10, sheet);
		sheet.addMergedRegion(new CellRangeAddress(9, 9, 11, 12));// Diem
		setBorderMerger(9, 9, 11, 12, sheet);
		sheet.addMergedRegion(new CellRangeAddress(9, 10, 14, 14));// TT
		setBorderMerger(9, 10, 14, 14, sheet);
		sheet.addMergedRegion(new CellRangeAddress(9, 10, 15, 23));// : hoc phan
		setBorderMerger(9, 10, 15, 26, sheet);
		sheet.addMergedRegion(new CellRangeAddress(9, 10, 24, 24));// TC
		setBorderMerger(9, 10, 24, 24, sheet);
		sheet.addMergedRegion(new CellRangeAddress(9, 9, 25, 26));// Diem
		setBorderMerger(9, 9, 25, 26, sheet);

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
			setBorderMerger(i, i, 1, 9, sheet);
			sheet.getRow(i).getCell(0).setCellValue(stt);
			sheet.getRow(i).getCell(0).setCellStyle(styleCenterBacsic);
			sheet.getRow(i).getCell(1).setCellValue(list.get(index).getModule());
			sheet.getRow(i).getCell(10).setCellValue(list.get(index).getCredit());
			sheet.getRow(i).getCell(10).setCellStyle(styleCenterBacsic);
			sheet.getRow(i).getCell(11).setCellValue(list.get(index).getScore());
			sheet.getRow(i).getCell(11).setCellStyle(styleCenterBacsic);
			sheet.getRow(i).getCell(12).setCellValue(list.get(index).getMark());
			sheet.getRow(i).getCell(12).setCellStyle(styleCenterBacsic);
			index++;
			stt++;
		}
		if (list.size() % 2 != 0) {
			demDong--;
		}
		for (int i = 11; i < demDong; i++) {
			sheet.addMergedRegion(new CellRangeAddress(i, i, 15, 23));
			setBorderMerger(i, i, 15, 23, sheet);
			sheet.getRow(i).getCell(14).setCellValue(stt);
			sheet.getRow(i).getCell(14).setCellStyle(styleCenterBacsic);
			sheet.getRow(i).getCell(15).setCellValue(list.get(index).getModule());
			sheet.getRow(i).getCell(24).setCellValue(list.get(index).getCredit());
			sheet.getRow(i).getCell(24).setCellStyle(styleCenterBacsic);
			sheet.getRow(i).getCell(25).setCellValue(list.get(index).getScore());
			sheet.getRow(i).getCell(25).setCellStyle(styleCenterBacsic);
			sheet.getRow(i).getCell(26).setCellValue(list.get(index).getMark());
			sheet.getRow(i).getCell(26).setCellStyle(styleCenterBacsic);
			index++;
			stt++;
		}
		if (list.size() % 2 != 0) {
			demDong = demDong + 1;
		}
		sheet.getRow(demDong).getCell(9).setCellValue(sv.getScoreTen());// diem 10
		sheet.getRow(demDong).getCell(22).setCellValue(sv.getRankGraduating());// xl tot nghiep
		sheet.getRow(demDong + 1).getCell(9).setCellValue(sv.getScoreFour());// diem 4
		sheet.getRow(demDong).getCell(0).setCellValue("Điểm trung bình");
		sheet.getRow(demDong).getCell(0).setCellStyle(styleBorderBoldCenter10);
		sheet.getRow(demDong).getCell(5).setCellValue("Thang điểm 10");
		sheet.getRow(demDong).getCell(5).setCellStyle(styleBorderBoldCenter10);
		sheet.getRow(demDong).getCell(9).setCellStyle(styleBorderBoldCenter10);// diem 10
		sheet.getRow(demDong).getCell(14).setCellValue("Xếp loại tốt nghiệp:");
		sheet.getRow(demDong).getCell(14).setCellStyle(styleBorderBoldCenter10);
		sheet.getRow(demDong).getCell(22).setCellStyle(styleBorderBoldCenter10);// xl tot nghiep
		sheet.getRow(demDong + 1).getCell(5).setCellValue("Thang điểm 4");
		sheet.getRow(demDong + 1).getCell(5).setCellStyle(styleBorderBoldCenter10);
		sheet.getRow(demDong + 1).getCell(9).setCellStyle(styleBorderBoldCenter10);// diem 4
		sheet.getRow(demDong + 2).getCell(0).setCellValue("Xếp loại rèn luyện : ");
		sheet.getRow(demDong + 2).getCell(0).setCellStyle(styleBorderBoldCenter10);
		sheet.getRow(demDong + 2).getCell(5).setCellStyle(styleBorderBoldCenter10);// ren luyen
 
		sheet.getRow(demDong + 3).getCell(14).setCellValue("");
		sheet.getRow(demDong + 3).getCell(14).setCellStyle(styleItalic);
		sheet.getRow(demDong + 4).getCell(0).setCellValue("Ghi chú:");
		sheet.getRow(demDong + 4).getCell(0).setCellStyle(styleItalicUnderline);
		sheet.getRow(demDong + 4).getCell(2).setCellValue("Bảng điểm này chỉ cấp 1 lần khi ra trường");
		sheet.getRow(demDong + 4).getCell(2).setCellStyle(styleItalic);
		sheet.getRow(demDong + 4).getCell(14).setCellValue("TL. HIỆU TRƯỞNG");
		sheet.getRow(demDong + 4).getCell(14).setCellStyle(styleBoldCenter);
		sheet.getRow(demDong + 5).getCell(14).setCellValue("TRƯỞNG PHÒNG ĐÀO TẠO");
		sheet.getRow(demDong + 5).getCell(14).setCellStyle(styleBoldCenter);
		sheet.getRow(demDong + 12).getCell(14).setCellValue("PGS. TS. Phùng Trung Nghĩa");
		sheet.getRow(demDong + 12).getCell(14).setCellStyle(styleBoldCenter);
		sheet.addMergedRegion(new CellRangeAddress(demDong, demDong + 1, 0, 4));// Diem trung binh
		setBorderMerger(demDong, demDong + 1, 0, 4, sheet);
		sheet.addMergedRegion(new CellRangeAddress(demDong, demDong, 5, 8));// Thang diem 10
		setBorderMerger(demDong, demDong, 5, 8, sheet);
		sheet.addMergedRegion(new CellRangeAddress(demDong, demDong, 9, 12));// 10.0
		setBorderMerger(demDong, demDong, 9, 12, sheet);
		sheet.addMergedRegion(new CellRangeAddress(demDong, demDong + 2, 14, 21));// Xep loai tot nghiep
		setBorderMerger(demDong, demDong + 2, 14, 21, sheet);
		sheet.addMergedRegion(new CellRangeAddress(demDong, demDong + 2, 22, 26));// Gioi
		setBorderMerger(demDong, demDong + 2, 22, 26, sheet);
		sheet.addMergedRegion(new CellRangeAddress(demDong + 1, demDong + 1, 5, 8));// Thang diem 4
		setBorderMerger(demDong + 1, demDong + 1, 5, 8, sheet);
		sheet.addMergedRegion(new CellRangeAddress(demDong + 1, demDong + 1, 9, 12));// 4.0
		setBorderMerger(demDong + 1, demDong + 1, 9, 12, sheet);
		sheet.addMergedRegion(new CellRangeAddress(demDong + 2, demDong + 2, 0, 4));// Xep loai ren luyen
		setBorderMerger(demDong + 2, demDong + 2, 0, 4, sheet);
		sheet.addMergedRegion(new CellRangeAddress(demDong + 2, demDong + 2, 5, 12));// Tot
		setBorderMerger(demDong + 2, demDong + 2, 5, 12, sheet);
		sheet.addMergedRegion(new CellRangeAddress(demDong + 3, demDong + 3, 14, 26));// Ngay thang nam
		sheet.addMergedRegion(new CellRangeAddress(demDong + 4, demDong + 4, 0, 1));// Ghi chu
		sheet.addMergedRegion(new CellRangeAddress(demDong + 4, demDong + 4, 2, 11));// cap 1 lan khi ra truong
		sheet.addMergedRegion(new CellRangeAddress(demDong + 4, demDong + 4, 14, 26));// TL.hieu truong
		sheet.addMergedRegion(new CellRangeAddress(demDong + 5, demDong + 5, 14, 26));// Truong phong dao tao
		sheet.addMergedRegion(new CellRangeAddress(demDong + 12, demDong + 12, 14, 26));// PGS TS Nghia

		sheet.setColumnWidth(0, 881);// tt
		for (int i = 1; i < 10; i++) {
			sheet.setColumnWidth(i, 953);// hoc phan
		}
		sheet.setColumnWidth(10, 1062);// tc
		sheet.setColumnWidth(11, 1356);// diem so
		sheet.setColumnWidth(12, 1099);// diem chu
		sheet.setColumnWidth(13, 125);// goc hep
		sheet.setColumnWidth(14, 881);// tt
		for (int i = 15; i < 24; i++) {
			sheet.setColumnWidth(i, 953);// hoc phan
		}
		sheet.setColumnWidth(24, 1062);// tc
		sheet.setColumnWidth(25, 1356);// diem so
		sheet.setColumnWidth(26, 1099);// diem chu
	}
	public void setBorderMerger(int fRow, int lRow, int fCol, int lCol, HSSFSheet sheet) {
		CellRangeAddress region = new CellRangeAddress(fRow, lRow, fCol, lCol);
		RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
	}
}
