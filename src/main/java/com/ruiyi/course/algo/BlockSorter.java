package com.ruiyi.course.algo;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.ruiyi.course.model.Block;

public class BlockSorter {
	public static void sortBlocks(List<Block> blocks) {
		Collections.sort(blocks, new Comparator<Block>() {
			@Override
			public int compare(Block block1, Block block2) {
				if(block1.clazz.priority==block2.clazz.priority){
					return block2.mutexBlocks.size()-block1.mutexBlocks.size();
				}
				return block2.clazz.priority - block1.clazz.priority;
			}
		});
	}
}
