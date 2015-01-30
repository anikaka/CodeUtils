package com.tongyan.utils;

/**
 * 计量流程操作
 * @author ChenLang
 *
 */
public enum ProcessOperation {
	
		ALL_APPROVE,  //审批全部
	    APPROVE, //审批
		DELIVER,   //打回
		TASK_TRACKING,   //  流程跟踪
		TASK_LOOK,// 流程查看
		STEP_BACk,    //打回上一步
		STEP_FIRST,  //打回到第一步
		UPDATE,		//数据刷新
		SUCCESS;      //操作成功
}
