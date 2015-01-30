package com.tygeo.highwaytunnel.common;

import java.util.LinkedList;
import java.util.List;

import com.tygeo.highwaytunnel.entity.LineSearch;
import com.tygeo.highwaytunnel.entity.Task;
import com.tygeo.highwaytunnel.entity.TunnelInfoE;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;

public class InfoApplication extends Application {
	private String task_name;// 项目名称
	private String task_check_name; // 项目检查名称
	private Task task; // 任务实体类
	private Task new_task;// 判断是否为新增任务
	private String pro_check_type; // 检查类型
	private int c;
	private String tabhost_tag;
	private LineSearch linesearch;
	private TunnelInfoE te;
	private Bitmap bt;
	private static InfoApplication a;
	private Object o;
	private List<Activity> activityList = new LinkedList<Activity>();
	private boolean isDownload;
	
	public boolean isDownload() {
		return isDownload;
	}

	public void setDownload(boolean isDownload) {
		this.isDownload = isDownload;
	}

	public static InfoApplication getInstance() {
		return a;
	}

	public void onCreate() {
		a = this;
		super.onCreate();
		isDownload = false;
	}

	public void setUserdata(Object paramObject) {
		this.o = paramObject;
	}

	public Object getUserdata() {
		return this.o;
	}

	public Bitmap getBt() {
		return bt;
	}

	public void setBt(Bitmap bt) {
		this.bt = bt;
	}

	public TunnelInfoE getTe() {
		return te;
	}

	public void setTe(TunnelInfoE te) {
		this.te = te;
	}

	public LineSearch getLinesearch() {
		return linesearch;
	}

	public void setLinesearch(LineSearch linesearch) {
		this.linesearch = linesearch;
	}

	public String getTask_name() {
		return task_name;
	}

	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}

	public String getTask_check_name() {
		return task_check_name;
	}

	public void setTask_check_name(String task_check_name) {
		this.task_check_name = task_check_name;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Task getNew_task() {
		return new_task;
	}

	public void setNew_task(Task new_task) {
		this.new_task = new_task;
	}

	public String getPro_check_type() {
		return pro_check_type;
	}

	public void setPro_check_type(String pro_check_type) {
		this.pro_check_type = pro_check_type;
	}

	public int getC() {
		return c;
	}

	public void setC(int c) {
		this.c = c;
	}

	public String getTabhost_tag() {
		return tabhost_tag;
	}

	public void setTabhost_tag(String tabhost_tag) {
		this.tabhost_tag = tabhost_tag;
	}
	
	public static InfoApplication getinstance() {
		if (null == a) {

			a = new InfoApplication();
		}

		return a;
	}

	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	public void exit() {
		for (Activity activity : activityList) {

			activity.finish();
		}
		System.exit(0);
	}

}
