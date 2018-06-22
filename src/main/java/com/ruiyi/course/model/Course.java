package com.ruiyi.course.model;

import java.util.Map;

import com.ruiyi.course.data.ModelFactory;

public class Course {
	public final String courseId;
	
	public Time[] banTimes;

	public Course(String courseId) {
		super();
		this.courseId = courseId;
	}
	@Deprecated
	public Course(String courseId, int weekLessons, int priority) {
		this(courseId);
	}
	public static final Map<String, Course> nameMap;
	static {
		nameMap = ModelFactory.getCourseMap();
	}

	@Override
	public String toString() {
		return courseId;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Course) {
			Course course = (Course) obj;
			return this == course;
		}
		return false;
	}

	public static class NoThatCourseException extends RuntimeException {
		private static final long serialVersionUID = -9124350307901305346L;

		public NoThatCourseException(String courseId) {
			super("已有课程中未包含要选择的课程：" + courseId);
		}
	}
}
