package com.tygeo.highwaytunnel.adpter;

import java.util.ArrayList;
import java.util.HashMap;

import com.tygeo.highwaytunnel.R;
import com.tygeo.highwaytunnel.DBhelper.DB_Provider;
import com.tygeo.highwaytunnel.activity.hidecheck.HideCheckDeviceActivity;
import com.tygeo.highwaytunnel.activity.hidecheck.HideCheckDevicePictureActivity;
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
 * �豸�Ų����¼��
 * @author  ChenLang
 */

public class HideCheckDeviceRecordAdapter  extends BaseAdapter {

	private ArrayList<HashMap<String, String>> mArrayList;
 	private int mResoureId;
	private LayoutInflater mInflater;
	private  ViewHolder mViewHolder;
	private Context mContext;
	
	public HideCheckDeviceRecordAdapter(Context context,ArrayList<HashMap<String, String>> arrayList,int resoureId){
		this.mContext = context;
		this.mArrayList=arrayList;;
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
			mViewHolder =new ViewHolder();
			convertView=mInflater.inflate(mResoureId, null);
			mViewHolder.mTxtDeviceName=(TextView)convertView.findViewById(R.id.txtDeviceRecordItemDeviceName);
			mViewHolder.mTxtDeviceNumber=(TextView)convertView.findViewById(R.id.txtDeviceRecordItemDeviceNumber);
			mViewHolder.mTxtCheckContent=(TextView)convertView.findViewById(R.id.txtDeviceRecordItemDeviceCheckContent);
//			mViewHolder.mTxtLoaction=(TextView)convertView.findViewById(R.id.txtDeviceRecordItemDeviceLoaction);
//			mViewHolder.mTxtAcceptanceRecord=(TextView)convertView.findViewById(R.id.txtDeviceRecordItemAcceptanceRecord);
			mViewHolder.mTxtOperator=(TextView)convertView.findViewById(R.id.txtDeviceRecordItemOperator);
			mViewHolder.mTxtRemark=(TextView)convertView.findViewById(R.id.txtDeviceRecordRemark);
			mViewHolder.mBtnDelDeviceRecord=(Button)convertView.findViewById(R.id.btnDeviceRecordDel);
			mViewHolder.mBtnPic=(Button)convertView.findViewById(R.id.btnDeviceRecordPic);
			convertView.setTag(mViewHolder);
		}else{
			mViewHolder=(ViewHolder)convertView.getTag();
			
		}
		
		final HashMap<String, String> map=mArrayList.get(position);
		mViewHolder.mTxtDeviceName.setText(map.get("name"));
		mViewHolder.mTxtDeviceNumber.setText(map.get("number"));
		mViewHolder.mTxtCheckContent.setText(map.get("checkContent"));
		mViewHolder.mTxtOperator.setText(map.get("operator"));
//		mViewHolder.mTxtRemark.setText(map.get("remark"));
		mViewHolder.mBtnPic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog dialog = new AlertDialog.Builder(mContext).setTitle("�Ƿ�����?")
				.setPositiveButton("����",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();	
								Intent intent = new Intent(mContext,HideCheckDevicePictureActivity.class);
								intent.putExtra("id",map.get("id") );
								mContext.startActivity(intent);
							}
						})
				.setNegativeButton("ȡ��",
						new DialogInterface.OnClickListener() {
					
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();

							}
						}).show();
			}
		});
		mViewHolder.mBtnDelDeviceRecord.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog dialog = new AlertDialog.Builder(mContext)
				.setTitle("��ʾ")
				.setMessage("�Ƿ�ɾ����")
				.setPositiveButton("ȷ��",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								DB_Provider.delDeviceRecordHideCheckDevicePic(map.get("id"));
								HideCheckDeviceActivity.refresh();
							}

						})
				.setNegativeButton("ȡ��",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).show();
			}
		});
		return convertView;
	}
	
	public class ViewHolder{
		private  TextView mTxtDeviceName;        //�豸����
		private  TextView mTxtDeviceNumber;		// ����
		private  TextView mTxtCheckContent;     //�������
//		private  TextView mTxtLoaction;              //���̲�λ
//		private  TextView  mTxtAcceptanceRecord; //���ռ�¼
		private  TextView mTxtOperator; 					//������
//		private  TextView  mTxtPicNumber;             //��Ƭ��� ��ʱ��д
		private  TextView mTxtRemark;					//��ע
		private   Button   mBtnDelDeviceRecord;    //ɾ����¼
		private   Button    mBtnPic;						  //����
	}
	
}
