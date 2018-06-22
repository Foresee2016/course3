package com.ruiyi.course.algo;

import java.util.ArrayList;
import java.util.List;

import com.ruiyi.course.model.Block;
import com.ruiyi.course.model.Clazz;
import com.ruiyi.course.model.Time;

public class LinkGenerator {
	
	/**
	 * 用班级生成Block列表，其中Block由于学生或教师而产生互斥联系。
	 * 
	 * ！！！时间复杂度为O(n*n*m*m)，n为班级数，m为每班人数。感觉这里需要优化，查重复时考虑排序后的二分查找，时间从m*m降到m*log(m)。
	 */
	public static List<Block> getBlocks() {
		// 生成blocks，此时内部只有对应的班级，没有互斥联系
		List<Block> blocks = new ArrayList<>();
		for (List<Clazz> clazzs : Clazz.classMap.values()) {
			for (Clazz clazz : clazzs) {
				Block block = new Block(clazz);
				blocks.add(block);
			}
		}
		// 生成互斥联系，理论上可以在生成时关联，少循环一次，但时间复杂度相同。
		for (int i = 0; i < blocks.size() - 1; i++) {
			Block blockI = blocks.get(i);
			for (int j = i + 1; j < blocks.size(); j++) {
				Block blockJ = blocks.get(j);
				if (blockI.clazz.teacher == blockJ.clazz.teacher) {
					blockI.mutexBlocks.add(blockJ);
					blockJ.mutexBlocks.add(blockI);
				}
			}
			setBanBlockLink(blockI);
		}
		setClassBanBlockLink(blocks);
		return blocks;
	}
	protected static void setBanBlockLink(Block block) {
		setTeacherBanBlockLink(block);
		setCourseBanBlockLink(block);
	}
	protected static void setTeacherBanBlockLink(Block block) {
		try {
			for (Time time : block.clazz.teacher.banTimes) {
				Time.WeekdayLesson weekdayLesson=Time.timeNameToWeekdayLesson(time.name);
				block.mutexBlocks.add(Block.getBanBlock(weekdayLesson.weekday, weekdayLesson.lesson));
			}
		} catch (NullPointerException e) {
			return;
		}
	}
	protected static void setCourseBanBlockLink(Block block) {
		try{			for (Time time : block.clazz.teacher.course.banTimes) {
				Time.WeekdayLesson weekdayLesson=Time.timeNameToWeekdayLesson(time.name);
				block.mutexBlocks.add(Block.getBanBlock(weekdayLesson.weekday, weekdayLesson.lesson));
			}
		}catch (NullPointerException e) {
			return;
		}
	}
	// 例：行政班的17级1班，不能同时上语文和英语。生成的时候会在Clazz对象的mutexClazz里，遍历生成连接
	protected static void setClassBanBlockLink(List<Block> blocks) {
		for (int i=0; i<blocks.size(); i++) {
			Block block = blocks.get(i);
			for (int j=i+1; j<blocks.size(); j++) {
				Block mutex = blocks.get(j);
				if(block.clazz.mutexClazz.contains(mutex.clazz)){
					block.mutexBlocks.add(mutex);
					mutex.mutexBlocks.add(block);
				}
			}
		}
	}
}
