package com.ruiyi.course.data;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ruiyi.course.model.Clazz;
import com.ruiyi.course.model.Course;
import com.ruiyi.course.model.Room;
import com.ruiyi.course.model.Teacher;
import com.ruiyi.course.model.Time;

interface ModelReader {
	/**
	 * 有课程禁排要求的，还需要设置banTimes 
	 */
	Map<String, Course> getCourseMap();
	/**
	 * 读数据库，除了读教师名和教的课，还要把教师禁排时间写入Teacher对象里banTime数组。没有禁排要求的保持初始值null 
	 */
	Map<String, Teacher> getTeacherMap();
	Map<Course, List<Clazz>> getClassMap();
	Time[][] getTimeTable();
	/**
	 * 教室有禁排要求的，直接在设置TimeTable时，将该Time对象的Block设置为Block.ROOM_BAN_BLOCK占位Block 
	 */
	Map<String, Room> getRoomMap();
	Set<Time> getGlobalBanTime();
}
