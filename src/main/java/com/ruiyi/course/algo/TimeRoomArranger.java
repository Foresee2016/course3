package com.ruiyi.course.algo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ruiyi.course.model.Block;
import com.ruiyi.course.model.Block.CannotFindTimeForBlockException;
import com.ruiyi.course.model.Room;
import com.ruiyi.course.model.Time;
import com.ruiyi.course.model.Time.WeekdayLesson;

public class TimeRoomArranger {
	/**
	 * 目标：为已生成好的blocks，逐个添加RoomTime，表示它已排好课的时间和地点。 暂时的算法是：
	 * 1.对每一个block，遍历互斥block，标记这些RoomTime位置。然后寻找可行的RoomTime。
	 * 2.对找到的时间点，看每个Room持有着自己的Time表格，如果有某个教室在该时间点空着，分配该Room，此Block的RoomTime成功分配。
	 * 但如果所有教室都被占用着，就把这个时间点推入互斥时间，继续找下个时间点。
	 * （！！！这个需要优化，因为有很多已经找过了，只因为又推入了一个而又找了一遍，但不好标志，因为标志了一样得遍历所有标志，之后再想怎么解决）
	 * @return 未能安排完的Block列表
	 */
	public static List<Block> arrangeRoomTime(List<Block> blocks) {
		Set<Time> mutexTimes = getMutexTimeSet();
		List<Block> failedBlocks=new ArrayList<>();
		for (Block block : blocks) {
			for (Block mutexblock : block.mutexBlocks) {
				if (!mutexblock.timeRoomMap.isEmpty()) {
					for (Time time : mutexblock.timeRoomMap.keySet()) {
						WeekdayLesson weekdayLesson = Time.timeNameToWeekdayLesson(time.name);
						mutexTimes.add(Time.getTime(Time.times, weekdayLesson.weekday, weekdayLesson.lesson));
					}
				}
			}
			int lesson = 0, weekday = 0;
			Set<Integer> usedWeekday = new HashSet<>();
			try {
				for (int i = 0; i < block.clazz.weekLessons; i++) {
					while (true) {
						outer: for (; lesson < Time.times.length; lesson++) {
							for (weekday = 0; weekday < Time.times[0].length; weekday++) {
								Time time = Time.times[lesson][weekday];
								if (mutexTimes.contains(time)) {
									continue;
								}
								if (usedWeekday.size() < Time.weekdays.length && usedWeekday.contains(weekday)) {
									continue;
								}
								break outer;
							}
						}
						if (lesson == Time.times.length) {
							throw new Block.CannotFindTimeForBlockException(block);
						}
						Collection<Room> rooms = Room.rooms.values();
						int searchedRooms = 0;
						for (Room room : rooms) {
							if (room.timeTable[lesson][weekday].block == null) {
								block.timeRoomMap.put(room.timeTable[lesson][weekday], room);
								room.timeTable[lesson][weekday].block = block;
								mutexTimes.add(Time.getTime(Time.times, weekday, lesson));
								usedWeekday.add(weekday);
								break;
							}
							searchedRooms++;
						}
						if (searchedRooms == rooms.size()) { // 未找到能在该Time放下的Room，添加到禁止的里面，继续找Time
							mutexTimes.add(Time.getTime(Time.times, weekday, lesson));
						} else {
							break;
						}
					}
				}
			} catch (CannotFindTimeForBlockException e) {
				failedBlocks.add(e.block);
			}
			resetMutexTimeSet(mutexTimes);
		}
		return failedBlocks;
	}

	protected static Set<Time> getMutexTimeSet() {
		return new HashSet<>(Time.GLOBAL_BAN_TIME); // 获取全局禁排时间的副本返回
	}

	protected static void resetMutexTimeSet(Set<Time> mutexTimes) {
		mutexTimes.clear();
		mutexTimes.addAll(Time.GLOBAL_BAN_TIME);
	}
}
