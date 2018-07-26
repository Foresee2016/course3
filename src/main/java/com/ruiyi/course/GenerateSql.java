package com.ruiyi.course;

import java.io.IOException;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 读取Excel表格，生成SQL语句，一个一个输入太麻烦了
 */
public class GenerateSql {
	static String filepathPrefix = "D:/VsSpace/选排课资料/双流排课需求/分开/";

	public static void main(String[] args) {
//		readOneCol();
//		readOneShiftCol();
		generateUuid();
	}
	
	/**
	 * 使用时修改要读取的列标号，读取不同的列，生成SQL语句，第一行一般是标题列不读取
	 */
	public static void readOneCol() {
		String path = filepathPrefix + "行政班.xlsx";
		try (Workbook book = new XSSFWorkbook(path)) {
			Sheet sheet = book.getSheetAt(0);
			int colIdx = 8, rowStart = 1, classNameCol = 0, courseNameRow = 0;
			int runLesson = 0, runCount = 0, priority = 1, merge = 4;
			float weekLessons = 1;
			float semesterLessons = 16 * weekLessons; // 按16周来算
			String courseName = sheet.getRow(courseNameRow).getCell(colIdx).getStringCellValue();
			int totalRow = sheet.getLastRowNum();
			for (int rownum = rowStart; rownum <= totalRow; rownum++) {
				Row row = sheet.getRow(rownum);
				if (row == null) {
					System.out.println("无数据");
					continue;
				}
				String className = row.getCell(classNameCol).getStringCellValue();
				String teacherName = row.getCell(colIdx).getStringCellValue();
				System.out.println("INSERT INTO `RI_TeachPlan` VALUES (NULL, "
						+ "(SELECT RI_UserId FROM `RI_Teacher` WHERE RI_TeacherName='" + teacherName + "'), "
						+ "'a30ceb8c-8f05-11e8-8d9b-6045cb8b0d65', 'bc65a33b-8f05-11e8-8d9b-6045cb8b0d34',	'c45508e0-8f05-11e8-8d9b-6045cb8b0d12', "
						+ "(SELECT RI_CourseId FROM `RI_Course` WHERE RI_CourseName='" + courseName + "'), 1, "
						+ "(SELECT RI_ClassId FROM `ri_classes` WHERE RI_ClassName='" + className + "'), 0, " + semesterLessons
						+ ", " + weekLessons + ", " + runLesson + ", " + runCount + ", 50, 20, 1, null, " + priority + ", '', "
						+ merge + ", 1);");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void readOneShiftCol() {
		String path2 = filepathPrefix + "走班.xlsx";
		try (Workbook book = new XSSFWorkbook(path2)) {
			Sheet sheet = book.getSheetAt(0);
			int rowStart = 1, courseNameCol = 0, classNameCol = 1, teacherNameCol = 2;
			int runLesson = 0, runCount = 0, merge = 1;
			int totalRow = sheet.getLastRowNum();
			for (int rownum = rowStart; rownum <= totalRow; rownum++) {
				Row row = sheet.getRow(rownum);
				if (row == null) {
					System.out.println("无数据");
					continue;
				}
				String courseName = row.getCell(courseNameCol).getStringCellValue();
				String className = row.getCell(classNameCol).getStringCellValue();
				String teacherName = row.getCell(teacherNameCol).getStringCellValue();
				float weekLessons = className.contains("高考") ? 5 : 1;
				int priority = className.contains("高考") ? 7 : 4;
				float semesterLessons = 16 * weekLessons; // 按16周来算
				System.out.println("INSERT INTO `RI_TeachPlan` VALUES (NULL, "
						+ "(SELECT RI_UserId FROM `RI_Teacher` WHERE RI_TeacherName='" + teacherName + "'), "
						+ "'a30ceb8c-8f05-11e8-8d9b-6045cb8b0d65', 'bc65a33b-8f05-11e8-8d9b-6045cb8b0d34',	'c45508e0-8f05-11e8-8d9b-6045cb8b0d12', "
						+ "(SELECT RI_CourseId FROM `RI_Course` WHERE RI_CourseName='" + courseName + "'), 1, "
						+ "(SELECT RI_ClassId FROM `ri_classes` WHERE RI_ClassName='" + (courseName + className) + "'), 0, "
						+ semesterLessons + ", " + weekLessons + ", " + runLesson + ", " + runCount + ", 50, 20, 1, null, "
						+ priority + ", '', " + merge + ", 1);");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void generateUuid() {
		int count=10;
		for (int i = 0; i < count; i++) {
			UUID uuid=UUID.randomUUID();
			System.out.println(uuid.toString());
		}
	}
}
