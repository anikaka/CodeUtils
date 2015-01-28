package com.tongyan.yanan.tfinal;

import java.lang.reflect.Field;

import com.tongyan.yanan.tfinal.annotation.view.EventListener;
import com.tongyan.yanan.tfinal.annotation.view.Select;
import com.tongyan.yanan.tfinal.annotation.view.ViewInject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
/**
 * 
 * @className FragmentFinalActivity
 * @author wanghb
 * @date 2014-7-7 PM 03:47:55
 * @Desc TODO
 */
public class FinalFragmentAct extends FragmentActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	

	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		initView();
	}


	public void setContentView(View view, LayoutParams params) {
		super.setContentView(view, params);
		initView();
	}

	public void setContentView(View view) {
		super.setContentView(view);
		initView();
	}

	private void initView(){
		Field[] fields = getClass().getDeclaredFields();
		if(fields!=null && fields.length>0){
			for(Field field : fields){
				ViewInject viewInject = field.getAnnotation(ViewInject.class);
				if(viewInject!=null){
					int viewId = viewInject.id();
					try {
						field.setAccessible(true);
						field.set(this,findViewById(viewId));
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					String clickMethod = viewInject.click();
					if(!TextUtils.isEmpty(clickMethod))
						setViewClickListener(field,clickMethod);
					
					String longClickMethod = viewInject.longClick();
					if(!TextUtils.isEmpty(longClickMethod))
						setViewLongClickListener(field,longClickMethod);
					
					String itemClickMethod = viewInject.itemClick();
					if(!TextUtils.isEmpty(itemClickMethod))
						setItemClickListener(field,itemClickMethod);
					
					String itemLongClickMethod = viewInject.itemLongClick();
					if(!TextUtils.isEmpty(itemLongClickMethod))
						setItemLongClickListener(field,itemLongClickMethod);
					
					Select select = viewInject.select();
					if(!TextUtils.isEmpty(select.selected()))
						setViewSelectListener(field,select.selected(),select.noSelected());
					
				}
			}
		}
	}
	
	
	private void setViewClickListener(Field field,String clickMethod){
		try {
			Object obj = field.get(this);
			if(obj instanceof View){
				((View)obj).setOnClickListener(new EventListener(this).click(clickMethod));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setViewLongClickListener(Field field,String clickMethod){
		try {
			Object obj = field.get(this);
			if(obj instanceof View){
				((View)obj).setOnLongClickListener(new EventListener(this).longClick(clickMethod));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setItemClickListener(Field field,String itemClickMethod){
		try {
			Object obj = field.get(this);
			if(obj instanceof AbsListView){
				((AbsListView)obj).setOnItemClickListener(new EventListener(this).itemClick(itemClickMethod));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setItemLongClickListener(Field field,String itemClickMethod){
		try {
			Object obj = field.get(this);
			if(obj instanceof AbsListView){
				((AbsListView)obj).setOnItemLongClickListener(new EventListener(this).itemLongClick(itemClickMethod));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setViewSelectListener(Field field,String select,String noSelect){
		try {
			Object obj = field.get(this);
			if(obj instanceof View){
				((AbsListView)obj).setOnItemSelectedListener(new EventListener(this).select(select).noSelect(noSelect));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private MHandler handler = new MHandler();

	protected void handleOtherMessage(int flag) {

	}

	public void sendFMessage(int flag) {
		handler.sendEmptyMessage(flag);
	}

	public void sendMessageDely(int flag, long delayMillis) {
		handler.sendEmptyMessageDelayed(flag, delayMillis);
	}


	private class MHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if (!Thread.currentThread().isInterrupted()) {
				switch (msg.what) {
				default:
					handleOtherMessage(msg.what);
				}
			}
		}

	}
	

}
