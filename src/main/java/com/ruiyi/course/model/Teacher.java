package com.ruiyi.course.model;

import java.util.Map;

import com.ruiyi.course.data.ModelFactory;

public class Teacher {
	public final String teacherId;
	public final Course course;
	public Time[] banTimes;

	public Teacher(String teacherId, Course course) {
		super();
		this.teacherId = teacherId;
		this.course = course;
	}

	public static final Map<String, Teacher> idMap;
	static {
		idMap = ModelFactory.getTeacherMap();
	}

	@Override
	public String toString() {
		return "Teacher:{" + teacherId + ", " + course.courseId + "}";
	}
}
