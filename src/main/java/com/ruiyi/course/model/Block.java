package com.ruiyi.course.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruiyi.course.data.ModelFactory;

/**
 * Block对象作为排课的基本单元，整体对应一个或多个RoomTime
 */
public class Block {
	public final Clazz clazz;
	// 类之间可以互相持有引用，但不要放在构造函数里。
	public final Map<Time, Room> timeRoomMap;
	public List<Block> mutexBlocks;

	public Block(Clazz clazz) {
		super();
		this.clazz = clazz;
		mutexBlocks = new ArrayList<>();
		timeRoomMap = new HashMap<>();
	}

	public static final Block[][] BAN_BLOCKS;
	public static final Block ROOM_BAN_BLOCK = new Block(new Clazz("教室禁排占位", null));
	static {
		BAN_BLOCKS = ModelFactory.generateBanBlocks();
	}

	public static Block getBanBlock(int weekday, int lesson) {
		return BAN_BLOCKS[lesson][weekday];
	}

	@Override
	public String toString() {
		String timeRooms = "";
		for (Time time : timeRoomMap.keySet()) {
			Room room = timeRoomMap.get(time);
			timeRooms += "[时间：" + (time == null ? null : time.name) + ",地点：" + (room == null ? null : room.roomId) + "]， ";
		}
		return "Block:{" + clazz.classId + ", " + timeRooms + "}";
	}

	public static class CannotFindTimeForBlockException extends RuntimeException {
		private static final long serialVersionUID = 7008943283515140971L;
		public Block block;

		public CannotFindTimeForBlockException(Block block) {
			super("无法为Block找到Time：" + block);
			this.block = block;
		}
	}
}
