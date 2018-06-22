package com.ruiyi.course.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ruiyi.course.model.Clazz;
import com.ruiyi.course.model.Course;
import com.ruiyi.course.model.Room;
import com.ruiyi.course.model.Teacher;
import com.ruiyi.course.model.Time;

@SuppressWarnings("unused")
class MockData /*implements ModelReader*/ {

//	@Override
//	public Map<String, Course> getCourseMap() {
//		Map<String, Course> nameMap = new HashMap<>();
//		nameMap.put("物理", new Course("物理"));
//		nameMap.put("化学", new Course("化学"));
//		nameMap.put("历史", new Course("历史"));
//		return nameMap;
//	}
//
//
//	@Override
//	public Map<String, Teacher> getTeacherMap() {
//		Map<String, Teacher> idMap = new HashMap<>();
//		idMap.put("阿大", new Teacher("阿大", Course.nameMap.get("物理")));
//		idMap.put("阿二", new Teacher("阿二", Course.nameMap.get("化学")));
//		idMap.put("阿三", new Teacher("阿三", Course.nameMap.get("历史")));
//		return idMap;
//	}
//	
//	@Override
//	public Map<Course, List<Clazz>> getClassMap() {
//		Map<Course, List<Clazz>> classMap = new HashMap<>();
//
//		Clazz clazz1 = new Clazz("物理1班", Teacher.idMap.get("阿大"));
//		Clazz clazz4 = new Clazz("物理2班", Teacher.idMap.get("阿大"));
//		Clazz clazz2 = new Clazz("化学1班", Teacher.idMap.get("阿二"));
//		Clazz clazz3 = new Clazz("历史1班", Teacher.idMap.get("阿三"));
//		classMap.put(Course.nameMap.get("物理"), Arrays.asList(clazz1, clazz4));
//		classMap.put(Course.nameMap.get("化学"), Arrays.asList(clazz2));
//		classMap.put(Course.nameMap.get("历史"), Arrays.asList(clazz3));
//
//		return classMap;
//	}
//
//	@Override
//	public Time[][] getTimeTable() {
//		int LESSONS_PER_DAY = Time.LESSONS_PER_DAY;
//		String[] weekdays = Time.weekdays;
//		Time[][] times = new Time[LESSONS_PER_DAY][weekdays.length];
//		for (int i = 0; i < LESSONS_PER_DAY; i++) {
//			for (int j = 0; j < weekdays.length; j++) {
//				times[i][j] = new Time(weekdays[j] + i); // 暂时从0计，容易想。
//			}
//		}
//		return times;
//	}
//
//	@Override
//	public Map<String, Room> getRoomMap() {
//		String[] roomsIds = new String[] { "五教210", "三教120", "二教308", };
//		Map<String, Room> rooms = new HashMap<>();
//		for (String roomId : roomsIds) {
//			Time[][] timeTable = Time.createCopy();
//			Room room = new Room(roomId, timeTable);
//			for (int i = 0; i < timeTable.length; i++) {
//				Time[] week = timeTable[i];
//				for (int j = 0; j < week.length; j++) {
//					week[j].room = room;
//				}
//			}
//			rooms.put(roomId, room);
//		}
//		return rooms;
//	}
//
//
//	@Override
//	public Set<Time> getGlobalBanTime() {
//		return null;
//	}

}
