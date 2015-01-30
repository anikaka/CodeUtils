package com.tongyan.widget.view;

import com.tongyan.activity.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;
/**
 * 
 * @ClassName MSpinner.java
 * @Author wanghb
 * @Date 2013-9-25 pm 02:46:58
 * @Desc 自定义下拉菜单控件
 */
public class MSpinner extends Spinner{
	
	private Context mContext;
	//private Dialog mDialog = null;
	//private ArrayList<String> mList;
	private String mInitText;
	private int mTextColor;
	private int mBackgroundView;
	
	
	public MSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = getContext();//I have a question,if(getContext == context){System.out.println("it's right!");}
		  if (isInEditMode()) {
	            return;
	       }
		  //为MSpinner设置adapter，主要用于初始化显示spinner的text值
		  this.setAdapter(mAdapter);
	}
	
	public String getmInitText() {
		return mInitText;
	}
	public void setmInitText(String mInitText) {
		this.mInitText = mInitText;
	}
	
	private BaseAdapter mAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return 1;
        }
        @Override
        public Object getItem(int arg0) {
            return null;
        }
        @Override
        public long getItemId(int arg0) {
            return 0;
        }
        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
        	if(0 == mBackgroundView) {
        		mBackgroundView = R.layout.common_spinner;//now,just 3 class;one-P04_GpsSettingAct,two-P26_GpsItemInfoAct,three-P35_RiskTaskListAct
        	}
        	
        	TextView mTextView = (TextView)LayoutInflater.from(mContext).inflate(mBackgroundView, null);
        	if(0 != mTextColor) {
        		mTextView.setTextColor(mTextColor);
        	}
        	mTextView.setText(mInitText);
            return mTextView;
        }
        @Override
        public View getDropDownView(int position, View convertView,
        	ViewGroup parent) {
        	if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.gps_list_item, null);
			} 
			TextView textView = (TextView)convertView.findViewById(R.id.p04_gps_content_item_text);
			textView.setText(mInitText);
			return convertView;
        }
    };

	public BaseAdapter getmAdapter() {
		return mAdapter;
	}
	public void setmAdapter(BaseAdapter mAdapter) {
		this.mAdapter = mAdapter;
	}
	public void setmBackgroundView(int mBackgroundView) {
		this.mBackgroundView = mBackgroundView;
	}
	public int getmBackgroundView() {
		return mBackgroundView;
	}

	public void setmTextColor(int mTextColor) {
		this.mTextColor = mTextColor;
	}

	public int getmTextColor() {
		return mTextColor;
	}
}
