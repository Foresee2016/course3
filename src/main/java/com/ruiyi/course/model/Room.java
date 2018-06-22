package com.ruiyi.course.model;

import java.util.Map;

import com.ruiyi.course.data.ModelFactory;

public class Room {
	public final String roomId;
	public final Time[][] timeTable;

	public Room(String roomId, Time[][] timeTable) {
		super();
		this.roomId = roomId;
		this.timeTable = timeTable;
	}

	public static final Map<String, Room> rooms;
	/**
	 * 注：这里涉及到类加载顺序和static块执行顺序。这个静态块中，通过ModelFactory获取Map，而ModelFactory需要使用到Time[][]时间表，
	 * 所以会再取加载Time类并执行Time的初始化，Time静态块执行，初始化其中的static变量Time[][]
	 * times，Time初始化时并没有引用Room的对象，所以能正常加载完。
	 * 因此再回来执行这个Room类中的静态块，ModelFactory才可以得到Time[][]时间表的副本，这时它不是null了。
	 * 而对于时间表副本中每个Time的Room属性，会在Room创建对象之后赋值。因此Time类的Room不能是final，也不能在构造函数里初始化它（会造成交替循环初始化）
	 */
	static {
		rooms = ModelFactory.getRoomMap();
	}

	@Override
	public String toString() {
		return roomId;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Room) {
			return roomId == ((Room) obj).roomId;
		}
		return false;
	}
}
