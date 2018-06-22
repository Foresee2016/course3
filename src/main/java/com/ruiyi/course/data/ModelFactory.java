package com.ruiyi.course.data;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ruiyi.course.model.Block;
import com.ruiyi.course.model.Clazz;
import com.ruiyi.course.model.Course;
import com.ruiyi.course.model.Room;
import com.ruiyi.course.model.Teacher;
import com.ruiyi.course.model.Time;

public class ModelFactory {
	// static ModelReader modelReader = new MockData();
	//static ModelReader modelReader = new MockManyData();
	static ModelReader modelReader = new ExcelData();

	public static Map<String, Course> getCourseMap() {
		return modelReader.getCourseMap();
	}

	public static Map<String, Teacher> getTeacherMap() {
		return modelReader.getTeacherMap();
	}

	public static int getPerDayLessons() {
		return 9;
	}

	public static Time[][] getTimeTable() {
		return modelReader.getTimeTable();
	}

	public static Map<String, Room> getRoomMap() {
		return modelReader.getRoomMap();
	}

	public static Map<Course, List<Clazz>> getClassMap() {
		return modelReader.getClassMap();
	}

	public static Block[][] generateBanBlocks() {
		int lessonPerDay = Time.LESSONS_PER_DAY;
		Block[][] banBlocks = new Block[lessonPerDay][Time.weekdays.length];
		for (int i = 0; i < lessonPerDay; i++) {
			for (int j = 0; j < Time.weekdays.length; j++) {
				Block block = new Block(new Clazz("禁排" + Time.weekdays[j] + i, null, -1, -1));
				block.mutexBlocks=null;
				block.timeRoomMap.put(Time.getTime(Time.times, j, i), null);
				banBlocks[i][j] = block;
			}
		}
		return banBlocks;
	}
	public static Set<Time> getGlobalBanTime() {
		return modelReader.getGlobalBanTime();
	}
}
