package com.ruiyi.course.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ruiyi.course.data.ModelFactory;

public class Clazz {
	public final String classId;
	public final Teacher teacher;
	public final int weekLessons;
	public final int priority;
	public final List<Clazz> mutexClazz=new ArrayList<>();
	public Clazz(String classId, Teacher teacher, int weekLessons, int priority) {
		super();
		this.classId = classId;
		this.teacher = teacher;
		this.weekLessons = weekLessons;
		this.priority=priority;
	}
	@Deprecated
	public Clazz(String classId, Teacher teacher) {
		this(classId, teacher, -1, -1);
	}

	public static Map<Course, List<Clazz>> classMap;
	static {
		classMap = ModelFactory.getClassMap();
	}

	public static class ClazzInsufficientException extends RuntimeException {
		private static final long serialVersionUID = -1114868880407574057L;

		public ClazzInsufficientException(Course course) {
			super("科目“" + course.courseId + "”班级不足");
		}
	}

	@Override
	public String toString() {
		return "Clazz:{" + classId + ", " + teacher.teacherId + "}";
	}
}
