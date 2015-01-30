package com.tygeo.highwaytunnel.adpter;

import java.util.ArrayList;
import java.util.HashMap;

import com.tygeo.highwaytunnel.R;
import com.tygeo.highwaytunnel.DBhelper.DB_Provider;
import com.tygeo.highwaytunnel.activity.hidecheck.HideCheckPictureActivity;
import com.tygeo.highwaytunnel.activity.hidecheck.HideCheckTabItemActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * 检查记录单适配器
 * @author ChenLang
 *
 */

public class HideCheckRecordAdapter extends  BaseAdapter {

	private Context mContext;
	private  ArrayList<HashMap<String, String>> mArrayList;
	private int mResoureId;
	private LayoutInflater mInflater;
	private ViewHolder mViewHolder;
	
	public HideCheckRecordAdapter(Context context,ArrayList<HashMap<String, String>> arrayList,int resoureId){
		this.mContext=context;
		this.mArrayList=arrayList;
		this.mResoureId=resoureId;
		mInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		
		return mArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		
		return mArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			mViewHolder=new ViewHolder();
			convertView=mInflater.inflate(mResoureId, null);
			mViewHolder.mTxtMileage=(TextView)convertView.findViewById(R.id.txtRecordMileage);
			mViewHolder.mTxtCheckContent=(TextView)convertView.findViewById(R.id.txtRecordCheckContent);
			mViewHolder.mTxtPicNumber=(TextView)convertView.findViewById(R.id.txtRecordPicNumber);
			mViewHolder.mTxtDescribe=(TextView)convertView.findViewById(R.id.txtRecordDescribe);
			mViewHolder.mBtnDel=(Button)convertView.findViewById(R.id.btnRecordDel);
			mViewHolder.mBtnPic=(Button)convertView.findViewById(R.id.btnRecordPic);
			convertView.setTag(mViewHolder);
		}else{
			mViewHolder=(ViewHolder)convertView.getTag();
		}
		final HashMap<String, String> map=mArrayList.get(position);
		if(map!=null){
			mViewHolder.mTxtMileage.setText(map.get("mileage"));
			mViewHolder.mTxtCheckContent.setText(map.get("checkContent"));
			mViewHolder.mTxtPicNumber.setText(map.get("picNumber"));
			mViewHolder.mTxtDescribe.setText(map.get("describe"));
			
			mViewHolder.mBtnPic.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					AlertDialog dialog = new AlertDialog.Builder(mContext).setTitle("是否进入照片管理??")
					.setPositiveButton("确认",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();	
									Intent intent = new Intent(mContext,HideCheckPictureActivity.class);
									intent.putExtra("id",map.get("id") );
									mContext.startActivity(intent);
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
						
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();

								}
							}).show();
				}
			});
			mViewHolder.mBtnDel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					AlertDialog dialog = new AlertDialog.Builder(mContext)
					.setTitle("提示")
					.setMessage("是否删除？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									DB_Provider.delCheckRecordHidecheckpic(map.get("id"));
									HideCheckTabItemActivity.refresh();
								}

							})
							
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).show();
				}
			});
		}
		return convertView;
	}

public class ViewHolder{
	 private TextView mTxtMileage;
	 private TextView mTxtCheckContent; //检查内容
	 private TextView mTxtPicNumber;  //照片编号
	 private TextView mTxtDescribe;     //病害描述
	 private Button    mBtnDel; 			//记录单删除
	 private Button    mBtnPic;         //拍照
//	 public HashMap<String, String > map;
}
}
