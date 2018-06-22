/**
 * 这个包内是算法模型类，介绍时由底向上。先排课然后学生选课时查看是否冲突，所以当前没有Student的信息
 * 注：底层类多含有Map来查找对象的引用，这些对象都是不易变的，不需要每次创建对象，初始化在静态块中（实际应用中通过读数据库获取）。
 * Course：课程类，内层对象：
 *     String courseId：课程名或ID
 *     static Map<String, Course> nameMap：通过courseId获取对象引用
 * Teacher：教师类，内层对象：
 *     String teacherId：教师名或者ID，如阿大
 *     Course：教师教的课程对象
 *     Time[] banTimes：教师禁排时间数组，没有禁排的就保持初始null
 *     static final Map<String, Teacher> idMap：通过teacherId获取对象引用
 * Clazz：班级，内层对象：
 *     String classId：班级名或者ID，如五教210
 *     Teacher：这个班级对应的教师对象
 * Block：按块被安排的单元，内层对象：
 *     Clazz：它对应的班级的对象
 *     Room：占用的教室对象，例如：五教210
 *     Time：占用的时间点对象，例如：周一5
 *     List<Block> mutexBlocks：它互斥的其它单元，根据学生和教师产生互斥关系，具有互斥关系的不能安排在同一时间。
 *         例如：阿大教物理1班和2班，那么物理1班和物理2班对应的Block互斥。
 *         例如：学生S1被分在了物理1班和化学1班，那么物理1班和化学1班对应的Block互斥。
 *     ------ 2018-4-27增加 ------
 *     static Block[][] banBlock：代表禁排的静态Block，二维静态Block表，它们固定的Time对应课表的每一个位置，Room和Clazz和mutexBlock都是null，目的是当需要禁排时，让那些Block关联Block表中对应位置的。
 *               举例：物理1班教师阿大需要禁排周一1-5节，那就让物理1班所属Block的mutexBlock包含周一1-5的五个Block，生成时会看到这些互斥时间
 * Time：时间点类，内层对象：
 *     String time：时间点的名，例如：周一3表示周一的第4节课（暂时从0开始计数）
 *     Room：时间点属于的教室（模板Time[][]中这个属性为null，为教室生成副本时赋Room引用）
 *     Block：占用该时间点的块的引用。（这里引用构成了一个环形，允许这样）
 *     static Time[][]提供时间表模板，createCopy()通过它生成副本。
 *     注：时间表按照通常课表的布局，行时lesson，列时weekday
 * Room：教室类，内层对象：
 *     String roomId：教室名或者ID
 *     Time[][] timeTable：教室持有的时间表副本，这里构成了环形引用，为了方便取值。
 *     Map<String, Room> rooms：通过roomId获取对象引用
 *     
 * ------ 禁排模型说明 ------
 * 教师禁排和课程禁排从数据库读入后，设置在单独的Teacher和Course对象banTimes里，而教室因为持有独立的时间表，禁排时把对应时间点的Time的Block被静态占位Block.ROOM_BAN_BLOCK占用。
 * 安排教室时，如果发现Room的Time[][]该点已经有Block了，不会再分配。
 * ------ 全局禁排 ------
 * 如果有“校会”这种全局活动，直接在ArrangeClass中，生成mutexTimes集合时，用非空的已添加了公共活动的集合。
 */
package com.ruiyi.course.model;
