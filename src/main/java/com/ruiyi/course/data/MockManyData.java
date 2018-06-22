package com.ruiyi.course.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ruiyi.course.model.Block;
import com.ruiyi.course.model.Clazz;
import com.ruiyi.course.model.Course;
import com.ruiyi.course.model.Room;
import com.ruiyi.course.model.Teacher;
import com.ruiyi.course.model.Time;

class MockManyData implements ModelReader {

	@Override
	public Map<String, Course> getCourseMap() {
		Map<String, Course> nameMap = new HashMap<>();
		nameMap.put("语文", new Course("语文", 6, 9));
		nameMap.put("数学", new Course("数学", 6, 9));
		nameMap.put("英语", new Course("英语", 6, 7));
		nameMap.put("物理", new Course("物理", 5, 6));
		nameMap.put("化学", new Course("化学", 5, 5));
		nameMap.put("生物", new Course("生物", 5, 4));
		nameMap.put("历史", new Course("历史", 5, 6));
		nameMap.put("地理", new Course("地理", 5, 4));
		nameMap.put("政治", new Course("政治", 5, 4));
		setCourseBanTime(nameMap);
		return nameMap;
	}

	protected static void setCourseBanTime(Map<String, Course> nameMap) {
		nameMap.get("语文").banTimes = new Time[] { Time.getTime(Time.times, 3, 0), Time.getTime(Time.times, 3, 1),
				Time.getTime(Time.times, 3, 2), Time.getTime(Time.times, 3, 3), Time.getTime(Time.times, 3, 4), };
		nameMap.get("数学").banTimes = new Time[] { Time.getTime(Time.times, 2, 5), Time.getTime(Time.times, 2, 6),
				Time.getTime(Time.times, 2, 7), Time.getTime(Time.times, 2, 8),};
		nameMap.get("英语").banTimes = new Time[] { Time.getTime(Time.times, 0, 0), Time.getTime(Time.times, 0, 1),
				Time.getTime(Time.times, 0, 2), Time.getTime(Time.times, 0, 3), Time.getTime(Time.times, 0, 4), };
		nameMap.get("物理").banTimes = new Time[] { Time.getTime(Time.times, 3, 0), Time.getTime(Time.times, 3, 1),
				Time.getTime(Time.times, 3, 2), Time.getTime(Time.times, 3, 3), Time.getTime(Time.times, 3, 4), };
		nameMap.get("化学").banTimes = new Time[] { Time.getTime(Time.times, 1, 0), Time.getTime(Time.times, 1, 1),
				Time.getTime(Time.times, 1, 2), Time.getTime(Time.times, 1, 3), Time.getTime(Time.times, 1, 4), };
		nameMap.get("生物").banTimes = new Time[] { Time.getTime(Time.times, 1, 0), Time.getTime(Time.times, 1, 1),
				Time.getTime(Time.times, 1, 2), Time.getTime(Time.times, 1, 3), Time.getTime(Time.times, 1, 4), };
		nameMap.get("历史").banTimes = new Time[] { Time.getTime(Time.times, 4, 0), Time.getTime(Time.times, 4, 1),
				Time.getTime(Time.times, 4, 2), Time.getTime(Time.times, 4, 3), Time.getTime(Time.times, 4, 4), };
		nameMap.get("政治").banTimes = new Time[] { Time.getTime(Time.times, 4, 0), Time.getTime(Time.times, 4, 1),
				Time.getTime(Time.times, 4, 2), Time.getTime(Time.times, 4, 3), Time.getTime(Time.times, 4, 4), };
		nameMap.get("地理").banTimes = new Time[] { Time.getTime(Time.times, 3, 0), Time.getTime(Time.times, 3, 1),
				Time.getTime(Time.times, 3, 2), Time.getTime(Time.times, 3, 3), Time.getTime(Time.times, 3, 4), };
	}


	@Override
	public Map<String, Teacher> getTeacherMap() {
		Map<String, Teacher> idMap = new HashMap<>();
		idMap.put("语文T1", new Teacher("语文T1", Course.nameMap.get("语文")));
		idMap.put("语文T2", new Teacher("语文T2", Course.nameMap.get("语文")));
		
		idMap.put("数学T1", new Teacher("数学T1", Course.nameMap.get("数学")));
		idMap.put("数学T2", new Teacher("数学T2", Course.nameMap.get("数学")));
		
		idMap.put("英语T1", new Teacher("英语T1", Course.nameMap.get("英语")));
		idMap.put("英语T2", new Teacher("英语T2", Course.nameMap.get("英语")));
		
		idMap.put("物理T1", new Teacher("物理T1", Course.nameMap.get("物理")));
		idMap.put("物理T2", new Teacher("物理T2", Course.nameMap.get("物理")));

		idMap.put("化学T1", new Teacher("化学T2", Course.nameMap.get("化学")));
		idMap.put("化学T2", new Teacher("化学T2", Course.nameMap.get("化学")));

		idMap.put("生物T1", new Teacher("生物T1", Course.nameMap.get("生物")));
		idMap.put("生物T2", new Teacher("生物T2", Course.nameMap.get("生物")));

		idMap.put("历史T1", new Teacher("历史T1", Course.nameMap.get("历史")));
		idMap.put("历史T2", new Teacher("历史T2", Course.nameMap.get("历史")));
		
		idMap.put("地理T1", new Teacher("地理T1", Course.nameMap.get("地理")));
		idMap.put("地理T2", new Teacher("地理T2", Course.nameMap.get("地理")));
		
		idMap.put("政治T1", new Teacher("政治T1", Course.nameMap.get("政治")));
		idMap.put("政治T2", new Teacher("政治T2", Course.nameMap.get("政治")));
		setTeacherBanTime(idMap);
		return idMap;
	}

	protected void setTeacherBanTime(Map<String, Teacher> idMap) {
		// 取Time对象的时候，从Time静态表里取，只拿到引用，省的分配空间。因为之后不会改变Time内值
		idMap.get("语文T1").banTimes = new Time[] { Time.getTime(Time.times, 3, 0), Time.getTime(Time.times, 3, 1),};
		idMap.get("物理T1").banTimes = new Time[] { Time.getTime(Time.times, 0, 0), Time.getTime(Time.times, 0, 1),
				Time.getTime(Time.times, 0, 2), Time.getTime(Time.times, 0, 3), Time.getTime(Time.times, 0, 4), };
		idMap.get("历史T2").banTimes = new Time[] { Time.getTime(Time.times, 2, 0), Time.getTime(Time.times, 2, 1),
				Time.getTime(Time.times, 2, 2), Time.getTime(Time.times, 2, 3), Time.getTime(Time.times, 2, 4), };
	}

	@Override
	public Map<Course, List<Clazz>> getClassMap() {
		Map<Course, List<Clazz>> classMap = new HashMap<>();
		Clazz[] yu = new Clazz[4];
		yu[0] = new Clazz("语文0班", Teacher.idMap.get("语文T1"));
		yu[1] = new Clazz("语文1班", Teacher.idMap.get("语文T1"));
		yu[2] = new Clazz("语文2班", Teacher.idMap.get("语文T2"));
		yu[3] = new Clazz("语文3班", Teacher.idMap.get("语文T2"));
		Clazz[] shu = new Clazz[4];
		shu[0] = new Clazz("数学0班", Teacher.idMap.get("数学T1"));
		shu[1] = new Clazz("数学1班", Teacher.idMap.get("数学T1"));
		shu[2] = new Clazz("数学2班", Teacher.idMap.get("数学T2"));
		shu[3] = new Clazz("数学3班", Teacher.idMap.get("数学T2"));
		Clazz[] ying = new Clazz[4];
		ying[0] = new Clazz("英语0班", Teacher.idMap.get("英语T1"));
		ying[1] = new Clazz("英语1班", Teacher.idMap.get("英语T1"));
		ying[2] = new Clazz("英语2班", Teacher.idMap.get("英语T2"));
		ying[3] = new Clazz("英语3班", Teacher.idMap.get("英语T2"));
		Clazz[] wu = new Clazz[4];
		wu[0] = new Clazz("物理0班", Teacher.idMap.get("物理T1"));
		wu[1] = new Clazz("物理1班", Teacher.idMap.get("物理T1"));
		wu[2] = new Clazz("物理2班", Teacher.idMap.get("物理T2"));
		wu[3] = new Clazz("物理3班", Teacher.idMap.get("物理T2"));
		Clazz[] hua = new Clazz[4];
		hua[0] = new Clazz("化学0班", Teacher.idMap.get("化学T1"));
		hua[1] = new Clazz("化学1班", Teacher.idMap.get("化学T1"));
		hua[2] = new Clazz("化学2班", Teacher.idMap.get("化学T2"));
		hua[3] = new Clazz("化学3班", Teacher.idMap.get("化学T2"));
		Clazz[] sheng = new Clazz[4];
		sheng[0] = new Clazz("生物0班", Teacher.idMap.get("生物T1"));
		sheng[1] = new Clazz("生物1班", Teacher.idMap.get("生物T1"));
		sheng[2] = new Clazz("生物2班", Teacher.idMap.get("生物T2"));
		sheng[3] = new Clazz("生物3班", Teacher.idMap.get("生物T2"));
		Clazz[] li = new Clazz[4];
		li[0] = new Clazz("历史0班", Teacher.idMap.get("历史T1"));
		li[1] = new Clazz("历史1班", Teacher.idMap.get("历史T1"));
		li[2] = new Clazz("历史2班", Teacher.idMap.get("历史T2"));
		li[3] = new Clazz("历史3班", Teacher.idMap.get("历史T2"));
		Clazz[] di = new Clazz[4];
		di[0] = new Clazz("地理0班", Teacher.idMap.get("地理T1"));
		di[1] = new Clazz("地理1班", Teacher.idMap.get("地理T1"));
		di[2] = new Clazz("地理2班", Teacher.idMap.get("地理T2"));
		di[3] = new Clazz("地理3班", Teacher.idMap.get("地理T2"));
		Clazz[] zheng = new Clazz[4];
		zheng[0] = new Clazz("政治0班", Teacher.idMap.get("政治T1"));
		zheng[1] = new Clazz("政治1班", Teacher.idMap.get("政治T1"));
		zheng[2] = new Clazz("政治2班", Teacher.idMap.get("政治T2"));
		zheng[3] = new Clazz("政治3班", Teacher.idMap.get("政治T2"));

		classMap.put(Course.nameMap.get("语文"), Arrays.asList(yu));
		classMap.put(Course.nameMap.get("数学"), Arrays.asList(shu));
		classMap.put(Course.nameMap.get("英语"), Arrays.asList(ying));
		classMap.put(Course.nameMap.get("物理"), Arrays.asList(wu));
		classMap.put(Course.nameMap.get("化学"), Arrays.asList(hua));
		classMap.put(Course.nameMap.get("生物"), Arrays.asList(sheng));
		classMap.put(Course.nameMap.get("历史"), Arrays.asList(li));
		classMap.put(Course.nameMap.get("地理"), Arrays.asList(di));
		classMap.put(Course.nameMap.get("政治"), Arrays.asList(zheng));

		return classMap;
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
		String[] roomsIds = new String[] { "五教210", "四教402", "三教120", "二教308", "一教503"};
		Map<String, Time[]> allRoomBanTimesMap = getAllRoomBanTimeMap();
		Set<String> hasBanRooms = allRoomBanTimesMap.keySet();
		Map<String, Room> rooms = new HashMap<>();
		for (String roomId : roomsIds) {
			Time[][] timeTable = Time.createCopy();
			Room room = new Room(roomId, timeTable);
			for (int i = 0; i < timeTable.length; i++) {
				Time[] week = timeTable[i];
				for (int j = 0; j < week.length; j++) {
					week[j].room = room;
				}
			}
			rooms.put(roomId, room);
			if (hasBanRooms.contains(roomId)) {
				Time[] banTimes = allRoomBanTimesMap.get(roomId);
				for (Time time : banTimes) {
					Time.WeekdayLesson weekdayLesson = Time.timeNameToWeekdayLesson(time.name);
					setTimeTableOccupied(room.timeTable, weekdayLesson.weekday, weekdayLesson.lesson);
				}
			}
		}
		return rooms;
	}

	protected Map<String, Time[]> getAllRoomBanTimeMap() {
		Map<String, Time[]> roomBanTimesMap = new HashMap<>();
		// 模拟“五教210”每天第一节课禁排
		roomBanTimesMap.put("五教210", new Time[] { Time.getTime(Time.times, 0, 0), Time.getTime(Time.times, 1, 0),
				Time.getTime(Time.times, 2, 0), Time.getTime(Time.times, 3, 0), Time.getTime(Time.times, 4, 0), });
		return roomBanTimesMap;
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
		globalBanTimes.add(Time.getTime(Time.times, 0, 8));
		globalBanTimes.add(Time.getTime(Time.times, 4, 8));
		return globalBanTimes;
	}
}
