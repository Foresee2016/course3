package com.ruiyi.course.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ruiyi.course.OutputUtils;
import com.ruiyi.course.model.Block;
import com.ruiyi.course.model.Clazz;
import com.ruiyi.course.model.Course;
import com.ruiyi.course.model.Room;
import com.ruiyi.course.model.Teacher;
import com.ruiyi.course.model.Time;

/**
 * 从Excel中读大量模拟数据，数据库暂时没完整数据
 */
@SuppressWarnings("unused")
class ExcelData implements ModelReader {
	public static final String filepathPrefix = "D:/VsSpace/双流排课需求/分开/";

	@Override
	public Map<String, Course> getCourseMap() {
		Map<String, Course> courseMap = new HashMap<>();
		String path = filepathPrefix + "课程上几节.xlsx";
		try (Workbook book = new XSSFWorkbook(path)) {
			Sheet sheet = book.getSheetAt(0);
			int rownum = 1;
			Row row = sheet.getRow(rownum);
			while (row != null) {
				String courseId = row.getCell(0).getStringCellValue();
				Course course = new Course(courseId);
				String banTimeStr = row.getCell(1).getStringCellValue();
				try {
					Time[] banTimes = stringToBanTimes(banTimeStr);
					course.banTimes = banTimes;
				} catch (StringToBanTimesException e) { // 无法解析，保留禁排为空
					e.printStackTrace();
				}
				courseMap.put(courseId, course);
				rownum++;
				row = sheet.getRow(rownum);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new ReadExcelDataException(e.getMessage());
		}
		return courseMap;
	}

	protected static Time[] stringToBanTimes(String banTimeStr) throws StringToBanTimesException {
		String weekdayStr = banTimeStr.substring(0, 2);
		String lessonStr = banTimeStr.substring(2);
		int weekday = 0;
		for (; weekday < Time.weekdays.length; weekday++) {
			if (weekdayStr.equals(Time.weekdays[weekday])) {
				break;
			}
		}
		if (weekday == Time.weekdays.length) {
			throw new StringToBanTimesException("无法解析时间段：" + banTimeStr);
		}
		Time[] banTimes;
		switch (lessonStr) {
		case "上午":
			banTimes = new Time[5];
			for (int i = 0; i < banTimes.length; i++) {
				banTimes[i] = Time.getTime(Time.times, weekday, i);
			}
			break;
		case "下午":
			banTimes = new Time[4];
			for (int i = 0; i < banTimes.length; i++) {
				banTimes[i] = Time.getTime(Time.times, weekday, i + 5);
			}
			break;
		default:
			throw new StringToBanTimesException("错误课程范围：" + lessonStr);
		}
		return banTimes;
	}

	public static class StringToBanTimesException extends Exception {
		private static final long serialVersionUID = -4658606611136887317L;

		public StringToBanTimesException(String message) {
			super(message);
		}
	}

	@Override
	public Map<String, Teacher> getTeacherMap() { // 当前导入方法不能有一个教师教两个科目的，会只导入一个
		Map<String, Teacher> teacherMap = new HashMap<>();
		String path = filepathPrefix + "行政班.xlsx";
		try (Workbook book = new XSSFWorkbook(path)) {
			Sheet sheet = book.getSheetAt(0);
			int rownum = 0;
			Row row = sheet.getRow(rownum);
			if (row == null) {
				throw new ReadExcelDataException("无数据：" + path);
			}
			String[] courseIds = new String[3];
			for (int i = 0; i < courseIds.length; i++) {
				courseIds[i] = row.getCell(i + 1).getStringCellValue();
			}
			rownum++;
			row = sheet.getRow(rownum);
			while (row != null) {
				String yuTeacherId = row.getCell(1).getStringCellValue();
				String shuTeacherId = row.getCell(2).getStringCellValue();
				String yingTeacherId = row.getCell(3).getStringCellValue();
				putIfNotExist(teacherMap, yuTeacherId, courseIds[0]);
				putIfNotExist(teacherMap, shuTeacherId, courseIds[1]);
				putIfNotExist(teacherMap, yingTeacherId, courseIds[2]);
				rownum++;
				row = sheet.getRow(rownum);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new ReadExcelDataException(e.getMessage());
		}
		String path2 = filepathPrefix + "走班.xlsx";
		try (Workbook book = new XSSFWorkbook(path2)) {
			Sheet sheet = book.getSheetAt(0);
			int rownum = 0;
			Row row = sheet.getRow(rownum);
			if (row == null) {
				throw new ReadExcelDataException("无数据：" + path2);
			}
			rownum++;
			row = sheet.getRow(rownum);
			while (row != null) {
				String courseId = row.getCell(0).getStringCellValue();
				String teacherId = row.getCell(2).getStringCellValue();
				putIfNotExist(teacherMap, teacherId, courseId);
				rownum++;
				row = sheet.getRow(rownum);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new ReadExcelDataException(e.getMessage());
		}
		String banTimePath = filepathPrefix + "教师禁排.xlsx";
		try (Workbook book = new XSSFWorkbook(banTimePath)) {
			Sheet sheet = book.getSheetAt(0);
			int rownum = 0;
			Row row = sheet.getRow(rownum);
			if (row == null) {
				throw new ReadExcelDataException("无数据：" + banTimePath);
			}
			rownum++;
			row = sheet.getRow(rownum);
			while (row != null) {
				String teacherId = row.getCell(0).getStringCellValue();
				if (!teacherMap.containsKey(teacherId)) {
					System.err.println("禁排中包含未注册的教师" + teacherId);
				}
				int weekday = (int) row.getCell(1).getNumericCellValue();
				String lessonStr = row.getCell(2).getCellTypeEnum() == CellType.STRING ? row.getCell(2).getStringCellValue()
						: "" + (int) row.getCell(2).getNumericCellValue();
				List<Integer> lessons = parseLesson(lessonStr);
				Time[] banTimes = new Time[lessons.size()];
				for (int i = 0; i < lessons.size(); i++) {
					int lesson = lessons.get(i);
					Time time = Time.getTime(Time.times, weekday - 1, lesson - 1);
					banTimes[i] = time;
				}
				Teacher teacher = teacherMap.get(teacherId);
				if (teacher.banTimes == null) {
					teacher.banTimes = banTimes;
				} else {
					Time[] merge = new Time[banTimes.length + teacher.banTimes.length];
					System.arraycopy(teacher.banTimes, 0, merge, 0, teacher.banTimes.length);
					System.arraycopy(banTimes, 0, merge, teacher.banTimes.length, banTimes.length);
					teacher.banTimes = merge;
				}
				rownum++;
				row = sheet.getRow(rownum);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new ReadExcelDataException(e.getMessage());
		}
		return teacherMap;
	}

	protected static void putIfNotExist(Map<String, Teacher> teacherMap, String teacherId, String courseId) {
		if (teacherId == null || teacherId == "" || courseId == null || courseId == "" || teacherMap.containsKey(teacherId)) {
			return;
		}
		teacherMap.put(teacherId, new Teacher(teacherId, Course.nameMap.get(courseId)));
	}

	protected static List<Integer> parseLesson(String lessonStr) {
		List<Integer> res = new ArrayList<Integer>();
		String[] arr = lessonStr.split("，");
		for (String str : arr) {
			String[] subArr = str.split(",");
			for (String subStr : subArr) {
				if (subStr.contains("-")) {
					String[] endPoint = subStr.split("-");
					int start = Integer.parseInt(endPoint[0]);
					int end = Integer.parseInt(endPoint[1]);
					for (int i = start; i <= end; i++) {
						res.add(i);
					}
				} else {
					int value = Integer.parseInt(subStr);
					res.add(value);
				}
			}
		}
		return res;
	}

	@Deprecated //这里有错误，如果一个教师，带高考班和学考班，会有两个Course，如物理高考和物理学考，后读进来的会覆盖先读的
	@Override
	public Map<Course, List<Clazz>> getClassMap() {
		Map<Course, List<Clazz>> classMap = new HashMap<>();
		String path = filepathPrefix + "行政班.xlsx";
		try (Workbook book = new XSSFWorkbook(path)) {
			Sheet sheet = book.getSheetAt(0);
			int rownum = 0;
			Row row = sheet.getRow(rownum);
			if (row == null) {
				throw new ReadExcelDataException("无数据：" + path);
			}
			String[] courseIds = new String[3];
			for (int i = 0; i < courseIds.length; i++) {
				courseIds[i] = row.getCell(i + 1).getStringCellValue();
			}
			rownum++;
			row = sheet.getRow(rownum);
			while (row != null) {
				String classId = row.getCell(0).getStringCellValue();
				String yuTeacherId = row.getCell(1).getStringCellValue();
				String shuTeacherId = row.getCell(2).getStringCellValue();
				String yingTeacherId = row.getCell(3).getStringCellValue();
				String classIdYu = "语文" + classId;
				String classIdShu = "数学" + classId;
				String classIdYing = "英语" + classId;
				Clazz classYu = new Clazz(classIdYu, Teacher.idMap.get(yuTeacherId), 6, 9);
				Clazz classShu = new Clazz(classIdShu, Teacher.idMap.get(shuTeacherId), 6, 9);
				Clazz classYing = new Clazz(classIdYing, Teacher.idMap.get(yingTeacherId), 6, 9);
				setAllMutex(Arrays.asList(classYu, classShu, classYing));
				putClassInMap(classMap, "语文", classYu);
				putClassInMap(classMap, "数学", classShu);
				putClassInMap(classMap, "英语", classYing);
				rownum++;
				row = sheet.getRow(rownum);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new ReadExcelDataException(e.getMessage());
		}
		String path2 = filepathPrefix + "走班.xlsx";
		try (Workbook book = new XSSFWorkbook(path2)) {
			Sheet sheet = book.getSheetAt(0);
			int rownum = 0;
			Row row = sheet.getRow(rownum);
			if (row == null) {
				throw new ReadExcelDataException("无数据：" + path2);
			}
			rownum++;
			row = sheet.getRow(rownum);
			while (row != null) {
				String courseId = row.getCell(0).getStringCellValue();
				String classId = courseId + row.getCell(1).getStringCellValue();
				String teacherId = row.getCell(2).getStringCellValue();
				Clazz clazz=null;
				if(classId.contains("高考")){
					clazz=new Clazz(classId, Teacher.idMap.get(teacherId), 5, 6);
				}else{
					clazz=new Clazz(classId, Teacher.idMap.get(teacherId), 1, 4);
				}
				putClassInMap(classMap, courseId, clazz);
				rownum++;
				row = sheet.getRow(rownum);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new ReadExcelDataException(e.getMessage());
		}
		return classMap;
	}
	protected static void setAllMutex(List<Clazz> clazzs) { // 为列表中班级两两添加进互斥
		for (int i = 0; i < clazzs.size(); i++) {
			for (int j = 0; j < clazzs.size(); j++) {
				if (i == j) {
					continue;
				}
				clazzs.get(i).mutexClazz.add(clazzs.get(j));
			}
		}
	}
	protected static void putClassInMap(Map<Course, List<Clazz>> classMap, String courseId, Clazz clazz) {
		Course course = Course.nameMap.get(courseId);
		if (classMap.containsKey(course)) {
			classMap.get(course).add(clazz);
		} else {
			List<Clazz> clazzs=new ArrayList<>();
			clazzs.add(clazz);
			classMap.put(course, clazzs);
		}
	}

	@Override
	public Time[][] getTimeTable() {
		int LESSONS_PER_DAY = 9;
		String[] weekdays = Time.weekdays;
		Time[][] times = new Time[LESSONS_PER_DAY][weekdays.length];
		for (int i = 0; i < LESSONS_PER_DAY; i++) {
			for (int j = 0; j < weekdays.length; j++) {
				times[i][j] = new Time(weekdays[j] + i); // 暂时从0计，容易想。
			}
		}
		return times;
	}

	@Override
	public Map<String, Room> getRoomMap() {
		Map<String, Room> roomMap=new HashMap<>();
		String path = filepathPrefix + "教室.xlsx";
		try (Workbook book = new XSSFWorkbook(path)) {
			Sheet sheet = book.getSheetAt(0);
			int rownum = 0;
			Row row = sheet.getRow(rownum);
			if (row == null) {
				throw new ReadExcelDataException("无数据：" + path);
			}
			rownum++;
			row = sheet.getRow(rownum);
			while (row != null) {
				String roomId = row.getCell(0).getCellTypeEnum() == CellType.STRING ? row.getCell(0).getStringCellValue() : ""+(int)row.getCell(0).getNumericCellValue();
				Room room;
				if(!roomMap.containsKey(roomId)){
					Time[][] timeTable = Time.createCopy();
					room = new Room(roomId, timeTable);
					for (int i = 0; i < timeTable.length; i++) {
						Time[] week = timeTable[i];
						for (int j = 0; j < week.length; j++) {
							week[j].room = room;
						}
					}
					roomMap.put(roomId, room);
				}else{
					room=roomMap.get(roomId);
				}
				// 教室禁排先判断填充了，再生成。生成时可以有多行对应一个教室的
				if(row.getCell(1).getCellTypeEnum() != CellType.BLANK && row.getCell(2).getCellTypeEnum() != CellType.BLANK){
					int weekday=(int)row.getCell(1).getNumericCellValue();
					String lessonStr=row.getCell(2).getCellTypeEnum() == CellType.STRING ? row.getCell(2).getStringCellValue() : ""+(int)row.getCell(2).getNumericCellValue();
					List<Integer> lessons=parseLesson(lessonStr);
					for (int lesson : lessons) {
						setTimeTableOccupied(room.timeTable, weekday, lesson);
					}
				}
				rownum++;
				row = sheet.getRow(rownum);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new ReadExcelDataException(e.getMessage());
		}
		return roomMap;
	}
	protected static void setTimeTableOccupied(Time[][] timeTable, int weekday, int lesson) {
		try {
			Time time = timeTable[lesson][weekday];
			time.block = Block.ROOM_BAN_BLOCK;
		} catch (NullPointerException e) {
			return;
		}
	}
	@Override
	public Set<Time> getGlobalBanTime() {
		Set<Time> globalBanTimes=new HashSet<>();
		String path = filepathPrefix + "全局禁排.xlsx";
		try (Workbook book = new XSSFWorkbook(path)) {
			Sheet sheet = book.getSheetAt(0);
			int rownum = 0;
			Row row = sheet.getRow(rownum);
			if (row == null) {
				throw new ReadExcelDataException("无数据：" + path);
			}
			rownum++;
			row = sheet.getRow(rownum);
			while (row != null) {
				int weekday=(int) row.getCell(0).getNumericCellValue();
				String lessonStr=row.getCell(1).getCellTypeEnum() == CellType.STRING ? row.getCell(1).getStringCellValue() : ""+(int)row.getCell(1).getNumericCellValue();
				List<Integer> lessons=parseLesson(lessonStr);
				for (int lesson : lessons) {
					globalBanTimes.add(Time.getTime(Time.times, weekday-1, lesson-1));
				}
				rownum++;
				row = sheet.getRow(rownum);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new ReadExcelDataException(e.getMessage());
		}
		return globalBanTimes;
	}

	public static class ReadExcelDataException extends RuntimeException {
		private static final long serialVersionUID = -6923176372121922137L;

		public ReadExcelDataException(String message) {
			super(message);
		}
	}
}
