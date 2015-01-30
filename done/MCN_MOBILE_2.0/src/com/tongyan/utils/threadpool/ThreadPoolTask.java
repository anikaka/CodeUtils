package com.tongyan.utils.threadpool;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.conn.ConnectTimeoutException;

import com.tongyan.common.data.Str2Json;
import com.tongyan.common.db.DBSectionService;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.WebServiceUtils;

import android.content.Context;
import android.os.Process;
import android.view.View;

/**
 * 任务单元
 * @className ThreadPoolTask
 * @author wanghb
 * @date 2014-7-21 PM 07:37:54
 * @Desc 工程标段更新
 */
public class ThreadPoolTask implements Runnable {

	
	public static final int REQUEST_COUNT = 0;
	public static final int REQUEST_TASK = 1;
	
	public Context mContext;
	private String mUsername;
	private String mPassword;
	private int mPageNum;
	public String mSectionId;
	private View mItemView;
	private TaskCallBack mCallBack;
	private int mRequestType;
	private boolean mIsLastTimes;
	
	
	public ThreadPoolTask(Context mContext,String mUsername,String mPassword, int mPageNum, String mSectionId, View mItemView, TaskCallBack mCallBack,int mRequestType, boolean mIsLastTimes) {
		this.mContext = mContext;
		this.mUsername = mUsername;
		this.mPassword = mPassword;
		this.mPageNum = mPageNum;
		this.mSectionId = mSectionId;
		this.mItemView = mItemView;
		this.mCallBack = mCallBack;
		this.mRequestType = mRequestType;
		this.mIsLastTimes = mIsLastTimes;
	}
	
	@Override
	public void run() {
		Process.setThreadPriority(Process.THREAD_PRIORITY_LOWEST);
		int back = 0;
		if(REQUEST_COUNT == mRequestType) {
			back = getTaskCount();
		} else {
			back = getTaskList();
		}
		
		if(mCallBack != null) {
			mCallBack.onTaskCallBack(mRequestType, mPageNum, mSectionId, mItemView, mCallBack, back, mIsLastTimes);
		}
	}
	
	
	/**
	 * 获取任务总数
	 * @return
	 */
	public int getTaskCount() {
		int back = 0;
		try {
			String jsonStr = WebServiceUtils.getRequestStr(mUsername, mPassword, null, null, null, "{sectionId:'"+mSectionId+"'}",Constansts.METHOD_OF_GETSECTIONSUBITEMCOUNT, mContext);
			Map<String,String> map = new Str2Json().getSectionsCount(jsonStr);
			if(map != null && "ok".equals(map.get("s"))) {
				back = Integer.parseInt(map.get("v"));
			} 
		} catch (Exception e) {
			e.printStackTrace();
			back = -2;
		} 
		return back;
	}
	
	/**
	 * 获取单个任务
	 * @return
	 */
	public int getTaskList() {
		int back = 0;
		try {
			String jsonStr = WebServiceUtils.getRequestStr(mUsername, mPassword, String.valueOf(Constansts.SECTION_PAGE_SIZE), String.valueOf(mPageNum), 
					          Constansts.METHOD_OF_GETSECTIONSUBITEMLISTBYPAGE, "{sectionId:'"+mSectionId+"'}", Constansts.METHOD_OF_GETLIST, mContext);
			List<HashMap<String, String>> list = new Str2Json().getSectionList(jsonStr);
			if(list != null) {
				int size = list.size();
				if(size > 0) {
					new DBSectionService().insertSectionBaseInfo(list, mSectionId);
				}
			}
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
			back = -2;
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			back = -2;
		}catch (Exception e) {
			e.printStackTrace();
			back = -1;
		} 
		return back;
	}
	
	
	public interface TaskCallBack {
		public void onTaskCallBack(int mRequestType, int mPageNum, String mSectionId,View view, TaskCallBack mCallback, int back , boolean mIsLastTimes);
	}
	
}
