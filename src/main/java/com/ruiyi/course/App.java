package com.ruiyi.course;

import java.io.IOException;
import java.util.List;

import com.ruiyi.course.OutputUtils;
import com.ruiyi.course.algo.BlockSorter;
import com.ruiyi.course.algo.LinkGenerator;
import com.ruiyi.course.algo.TimeRoomArranger;
import com.ruiyi.course.model.Block;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	OutputUtils.outputAllReadInfo();
		List<Block> blocks = LinkGenerator.getBlocks();
		BlockSorter.sortBlocks(blocks);
		OutputUtils.outputBlocks(blocks);
		System.out.println("---------------------");
		OutputUtils.outputRoomBanTimes();
		System.out.println("---------------------");
		OutputUtils.outputGlobalBanTimes();
		System.out.println("---------------------");
		List<Block> failedBlocks=TimeRoomArranger.arrangeRoomTime(blocks);
		OutputUtils.outputBlockTimeRoom(blocks);
		System.out.println("-------未能安排的Block------");
		OutputUtils.outputBlockTimeRoom(failedBlocks);
		System.out.println("---------------------");
		try {
			String filepath = "D:/VsSpace/schedule" + System.currentTimeMillis() + ".xlsx";
			OutputUtils.outputExcel(blocks, filepath);
			System.out.println("输出Excel文件：" + filepath);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
