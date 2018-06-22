/**
 * 算法包，负责利用模型类生成可用的课表，即让所有的Block中Room和Time不再是null，找到位置放进去。
 * 整体思路：
 *     先分班，ClassArranger类负责，根据学生选择了哪些课，放入还有空名额的班级中，某个班级满时计入下一班级，如物理1班满员了，到物理2班，都满员了throw new Clazz.ClazzInsufficientException。
 *     然后生成Block以及每个关联的Block，LinkGenerator类负责，统一教师所属Block被关联，同一学生所属的Block被关联，关联Block不能分在同一时间。
 *     然后为Block分配Time和Room，TimeRoomArranger类负责，考虑被关联Block不冲突的情况下，将所有Block的Time和Room都填上，（填不上的之后再考虑）
 */
package com.ruiyi.course.algo;