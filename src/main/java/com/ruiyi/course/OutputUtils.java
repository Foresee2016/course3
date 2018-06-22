package com.ruiyi.course;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ruiyi.course.model.Block;
import com.ruiyi.course.model.Clazz;
import com.ruiyi.course.model.Course;
import com.ruiyi.course.model.Room;
import com.ruiyi.course.model.Teacher;
import com.ruiyi.course.model.Time;

public class OutputUtils {

	public static void outputBlocks(List<Block> blocks) {
		for (Block block : blocks) {
			System.out.print("Block班级：" + block.clazz.classId + ", 互斥Block：");
			for (Block b : block.mutexBlocks) {
				System.out.print(b.clazz.classId + ", ");
			}
			System.out.println();
		}
	}

	public static void outputBlockTimeRoom(List<Block> blocks) {
		for (Block block : blocks) {
			System.out.println(block);
		}
	}

	public static final int EXCEL_COL_WIDTH = 20000;
	public static final int EXCEL_ROW_HEIGHT = 3000;

	public static void outputExcel(List<Block> blocks, String filepath) throws IOException {
		try (Workbook wb = new XSSFWorkbook()) { // or new HSSFWorkbook();
			Sheet sheet = wb.createSheet("课表");
			CellStyle wrapTextStyle = wb.createCellStyle();
			wrapTextStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			wrapTextStyle.setWrapText(true);
			{
				Row row = sheet.createRow(0);
				for (int i = 0; i < Time.weekdays.length; i++) {
					Cell cell = row.createCell(i + 1);
					cell.setCellValue(Time.weekdays[i]);
					sheet.setColumnWidth(i + 1, EXCEL_COL_WIDTH);
				}
			}
			{
				for (int i = 0; i < Time.LESSONS_PER_DAY; i++) {
					Row row = sheet.createRow(i + 1);
					row.setHeight((short) EXCEL_ROW_HEIGHT);
					Cell cell = row.createCell(0);
					cell.setCellValue("第" + (i + 1) + "节");
				}
			}
			// Create a cell and put a value in it.
			for (Block block : blocks) {
				for (Time time : block.timeRoomMap.keySet()) {
					Room room = block.timeRoomMap.get(time);
					Time.WeekdayLesson weekdayLesson = Time.timeNameToWeekdayLesson(time.name);
					Row row = getOrCreateRow(sheet, weekdayLesson.lesson + 1);
					Cell cell = row.getCell(weekdayLesson.weekday + 1);
					if (cell == null) {
						cell = row.createCell(weekdayLesson.weekday + 1);
						cell.setCellValue(block.clazz.classId + "在" + room.roomId);
					} else {
						String old = cell.getStringCellValue();
						cell.setCellValue(old + ", " + block.clazz.classId + "在" + room.roomId);
					}
					cell.setCellStyle(wrapTextStyle);
				}
			}
			// Write the output to a file
			try (FileOutputStream fileOut = new FileOutputStream(filepath)) {
				wb.write(fileOut);
			}
		}
	}

	protected static Row getOrCreateRow(Sheet sheet, int rownum) {
		Row row = sheet.getRow(rownum);
		if (row == null) {
			row = sheet.createRow(rownum);
		}
		return row;
	}

	public static void outputAllReadInfo() {
		OutputUtils.outputCourseMap(Course.nameMap);
		OutputUtils.outputTeacherMap(Teacher.idMap);
		OutputUtils.outputClassMap(Clazz.classMap);
		OutputUtils.outputRoom();
		OutputUtils.outputGlobalBanTimes();
	}

	public static void outputRoom() {
		for (String roomId : Room.rooms.keySet()) {
			List<Time> banTimes = new ArrayList<>();
			Room room = Room.rooms.get(roomId);
			for (int i = 0; i < room.timeTable.length; i++) {
				Time[] times = room.timeTable[i];
				for (int j = 0; j < times.length; j++) {
					if (times[j].block != null) {
						banTimes.add(times[j]);
					}
				}
			}
			System.out.print("教室：" + roomId);
			if (banTimes.size() > 0) {
				System.out.print("，禁排时间：");
				for (Time time : banTimes) {
					System.out.print(time.name + ",");
				}
			}
			System.out.println();
		}
	}

	public static void outputRoomBanTimes() {
		Map<String, List<Time>> allRoomsBanTime = new HashMap<>();
		for (String roomId : Room.rooms.keySet()) {
			List<Time> banTimes = new ArrayList<>();
			Room room = Room.rooms.get(roomId);
			for (int i = 0; i < room.timeTable.length; i++) {
				Time[] times = room.timeTable[i];
				for (int j = 0; j < times.length; j++) {
					if (times[j].block != null) {
						banTimes.add(times[j]);
					}
				}
			}
			if (banTimes.size() > 0) {
				allRoomsBanTime.put(roomId, banTimes);
			}
		}
		for (String roomId : allRoomsBanTime.keySet()) {
			System.out.print("教室“" + roomId + "”禁排时间：");
			for (Time time : allRoomsBanTime.get(roomId)) {
				System.out.print(time.name + ", ");
			}
			System.out.println();
		}
	}

	public static void outputGlobalBanTimes() {
		System.out.print("公共活动全局禁排：");
		for (Time time : Time.GLOBAL_BAN_TIME) {
			System.out.print(time.name + ", ");
		}
		System.out.println();
	}

	public static void outputCourseMap(Map<String, Course> courseMap) {
		System.out.println("----- 所有课程 ----- 共 " + courseMap.size() + " 条记录");
		for (String courseId : courseMap.keySet()) {
			Course course = courseMap.get(courseId);
			System.out.print("课程：" + courseId + " 禁排时间：");
			if (course.banTimes != null) {
				for (int i = 0; i < course.banTimes.length; i++) {
					System.out.print(course.banTimes[i] + ",");
				}
			}
			System.out.println();
		}
	}

	public static void outputTeacherMap(Map<String, Teacher> teacherMap) {
		System.out.println("---教师列表--- 共" + teacherMap.size() + "条记录");
		for (String teacherId : teacherMap.keySet()) {
			Teacher teacher = teacherMap.get(teacherId);
			System.out.print("教师：" + teacherId + ", 课程：" + teacher.course.courseId + ", 禁排：");
			if (teacher.banTimes != null) {
				for (Time time : teacher.banTimes) {
					System.out.print(time.name + ",");
				}
			}
			System.out.println();
		}
	}

	public static void outputClassMap(Map<Course, List<Clazz>> classMap) {
		for (Course course : classMap.keySet()) {
			System.out.println("--课程--" + course.courseId);
			List<Clazz> clazzs = classMap.get(course);
			for (Clazz clazz : clazzs) {
				System.out.print("班级：" + clazz.classId + ", 教师：" + clazz.teacher.teacherId + ", 每周次数" + clazz.weekLessons
						+ ", 优先级" + clazz.priority + ", 互斥班级：");
				for (Clazz mutexClass : clazz.mutexClazz) {
					System.out.print(mutexClass.classId + ",");
				}
				System.out.println();
			}
		}
	}
}
