package com.ruiyi.course.model;

import java.util.Set;

import com.ruiyi.course.data.ModelFactory;

public class Time {
	public final String name;
	public Room room;  //不要定义成final，也不要放在构造函数里。Time和Room类循环引用。
	public Block block; //类之间可以互相持有引用，但不要放在构造函数里。
	public Time(String name) {
		super();
		this.name = name;
	}
	public static String[] weekdays = { "周一", "周二", "周三", "周四", "周五" };
	public static final int LESSONS_PER_DAY;
	public static final Time[][] times; //注：时间表按照通常课表的布局，行时lesson，列时weekday
	public static final Set<Time> GLOBAL_BAN_TIME;
	static {
		LESSONS_PER_DAY=ModelFactory.getPerDayLessons();
		times=ModelFactory.getTimeTable();
		GLOBAL_BAN_TIME=ModelFactory.getGlobalBanTime();
	}
	/**
	 * 获取Time引用，weekday和lesson从0开始，容易想循环 
	 */
	public static Time getTime(Time[][] times, int weekday, int lesson) {
		if(times==null || weekday<0 || lesson<0 || lesson>=times.length ||  weekday>= times[0].length ){
			throw new NoThatTimeException("weekday="+weekday+", lesson="+lesson);
		}
		return times[lesson][weekday];
	}
	/**
	 * 生成原times的一份副本，副本中只有名称是相同的，Room和Block均为null
	 */
	public static Time[][] createCopy() {
		Time[][] copy=new Time[times.length][times[0].length];
		for (int i = 0; i < times.length; i++) {
			for(int j=0; j<times[0].length; j++){
				copy[i][j]=new Time(times[i][j].name);
			}
		}
		return copy;
	}
	/**
	 * 解析timeName，成int[2]，weekday+lesson，暂时timeName长度可以为3或4。
	 * 周一5，将解析为int[2]{0, 4};
	 */
	public static WeekdayLesson timeNameToWeekdayLesson(String timeName) throws IllegalTimeNameException {
		if(timeName==null || (timeName.length()!=3 && timeName.length()!=4)){
			throw new IllegalTimeNameException(timeName);
		}
		WeekdayLesson weekdayLesson=new WeekdayLesson();
		String weekday=timeName.substring(0, 2);
		String lesson=timeName.substring(2, 3);
		int i=0;
		for (; i < weekdays.length; i++) {
			if(weekday.equals(weekdays[i])){
				weekdayLesson.weekday=i;
				break;
			}
		}
		if(i==weekdays.length){
			throw new IllegalTimeNameException(timeName);
		}
		try {
			weekdayLesson.lesson=Integer.parseInt(lesson);
		} catch (NumberFormatException e) {
			throw new IllegalTimeNameException(timeName);
		}
		return weekdayLesson;
	}
	public static class WeekdayLesson {
		public int weekday;
		public int lesson;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Time){
			return name.equals(((Time)obj).name);
		}
		return false;
	}
	@Override
	public String toString() {
		return name;
	}
	public static class NoThatTimeException extends RuntimeException {
		private static final long serialVersionUID = -4806035098864104112L;
		public NoThatTimeException(String weekdayLesson) {
			super("当前课表没有排课时间："+weekdayLesson);
		}
	}
	public static class IllegalTimeNameException extends RuntimeException {
		private static final long serialVersionUID = -1767006434301918875L;
		public IllegalTimeNameException(String timeName) {
			super("解析时间点错误："+timeName);
		}
	}
}
