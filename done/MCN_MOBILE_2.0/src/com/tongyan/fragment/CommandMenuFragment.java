package com.tongyan.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParserException;

import com.tongyan.activity.MyApplication;
import com.tongyan.activity.R;
import com.tongyan.activity.measure.command.CommandApproveAct;
import com.tongyan.common.data.Str2Json;
import com.tongyan.common.entities._User;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.WebServiceUtils;
import com.tongyan.widget.pullrefresh.PullToRefreshListView;
import com.tongyan.widget.pullrefresh.PullToRefreshBase.OnRefreshListener;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 监理指令 
 * @author ChenLang
 *
 */
public class CommandMenuFragment  extends AbstructFragment  implements OnItemClickListener{
	
	private Context mContext;
	private String mSectionId="";//标段Id
	private String mType;  //值为 0未申报,1待审批2已申报,3已审批,4全部
	private _User mUser;
	private MyApplication mApplication;
	private  PullToRefreshListView mPullTorefresh;
	private  ListView mListView;
	public static  ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>> baseArrayListCommand=new ArrayList<HashMap<String,ArrayList<HashMap<String,String>>>>();
	private int mCurrentPageCount ;
	private MyAsyncTask mMyAsyncTask;
	private MyApdater mAdapter;
	private Dialog mDialog;
	
	public static CommandMenuFragment newInstance(Context context, String sectionId, String type, _User mUser) {
		CommandMenuFragment mFragment = new CommandMenuFragment();
		mFragment.mContext = context;
		mFragment.mSectionId = sectionId;
		mFragment.mType = type;
		mFragment.mUser = mUser;
		return mFragment;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {	
		   	 mApplication=(MyApplication)getActivity().getApplication();
		   	 mUser=mApplication.localUser;
			 View  view=inflater.inflate(R.layout.command_menu_content, container, false);
			 mPullTorefresh=(PullToRefreshListView)view.findViewById(R.id.listViewCommand);
			 mPullTorefresh.setOnRefreshListener(mFreshListener);
			 mListView=mPullTorefresh.getRefreshableView();
			 mAdapter=new MyApdater();
			 mListView.setAdapter(mAdapter);
			 mListView.setOnItemClickListener(this);
			 mMyAsyncTask=new MyAsyncTask();
			 mMyAsyncTask.execute();
			 showDialog();
		 return view;
	}

	OnRefreshListener mFreshListener = new OnRefreshListener(){
		@Override
		public void onRefresh() {
			if(mMyAsyncTask == null) {
				mMyAsyncTask = new MyAsyncTask(mPullTorefresh.getRefreshType());
			} else {
				if(!mMyAsyncTask.isCancelled())
					mMyAsyncTask.cancel(true);
				mMyAsyncTask = new MyAsyncTask(mPullTorefresh.getRefreshType());
			}
			mMyAsyncTask.execute();
		}
	};
	
	/** 显示对话框*/
	protected void showDialog(){
		mDialog=new Dialog(mContext, R.style.dialog);
		mDialog.setContentView(R.layout.common_loading_process_dialog);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.show();
	}
	
	/**关闭对话框 */
	protected void closeDialog(){
		if(mDialog!=null){
			mDialog.dismiss();
		}
	}
	
	public class MyAsyncTask extends AsyncTask<Void, Void, ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>>>{
		
		int pullType;
		public MyAsyncTask(){
			
		}
		public MyAsyncTask(int pullType){
		  this.pullType=pullType;	
		}
		
		@Override
		protected ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>> doInBackground(Void... params) {
			if(pullType == 0) {
				mCurrentPageCount = 1;
				if(baseArrayListCommand.size()>0){
					baseArrayListCommand.clear();
				}
			} 
			if(pullType == 1) {
				mCurrentPageCount = 1;
				if(baseArrayListCommand.size()>0){
					baseArrayListCommand.clear();
				}
			} 
			if(pullType == 2) {//向上
				mCurrentPageCount ++;
			} 

			StringBuffer  strBuffer=new StringBuffer();
			strBuffer.append("{");
			//tabIndex：0未申报1待审批2已申报3已审批4全部
			strBuffer.append("\"tabIndex\"");
			strBuffer.append(":");
			strBuffer.append("\""+(mType==null?"4":mType)+"\"");
			strBuffer.append(",");
			//section 标段ID
			strBuffer.append("\"section\"");
			strBuffer.append(":");
			strBuffer.append("\""+mSectionId+"\""); 
			strBuffer.append("}");
			HashMap<String, String> map=new HashMap<String, String>();
			map.put("publicKey", Constansts.PUBLIC_KEY);
			map.put("userName",mUser.getUsername());
			map.put("Password", mUser.getPassword());
			map.put("pageCount", String.valueOf(Constansts.PAGE_SIZE));
			map.put("currentPage", String.valueOf(mCurrentPageCount));
			map.put("type", "SuperviorOrder");
			map.put("parms", strBuffer.toString().trim());
			String stream=null;
			try {
				stream=WebServiceUtils.requestM(map, Constansts.METHOD_OF_GETLIST, mContext);
				if(stream!=null){
					return new Str2Json().getCommandData(stream);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>> result) {
			if(result != null && result.size() > 0) {
				baseArrayListCommand.addAll(result);
				if( pullType == 0) {
	        		mAdapter.notifyDataSetChanged();
	        	} else {
	        		mPullTorefresh.onRefreshComplete();
	        	}
			}else{
				mPullTorefresh.onRefreshComplete();
				mAdapter.notifyDataSetChanged();
				Toast.makeText(mContext, "数据已经全部更新", Toast.LENGTH_SHORT).show();
			}
			closeDialog();
			super.onPostExecute(result);
		}
	}
	
	
	public class MyApdater extends  BaseAdapter{

		private LayoutInflater mInflater;
		private ViewHolder mViewHolder;
		
		@Override
		public int getCount() {
			return baseArrayListCommand.size();
		}

		@Override
		public Object getItem(int position) {
			return baseArrayListCommand.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView==null){
				mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView=mInflater.inflate(R.layout.measure_command_list_item, null);
				mViewHolder=new ViewHolder();
				mViewHolder.mTextViewSectionName=(TextView)convertView.findViewById(R.id.textViewCommandSectionName);
				mViewHolder.mTextViewProjectName=(TextView)convertView.findViewById(R.id.textViewCommandProjectName);
				mViewHolder.mTextViewDraftDate=(TextView)convertView.findViewById(R.id.textViewCommandDraftDate);
				mViewHolder.mTextViewDraftPerson=(TextView)convertView.findViewById(R.id.textViewCommandDraftPerson);
				mViewHolder.mTextViewProcedureState=(TextView)convertView.findViewById(R.id.textViewCommandProcedureState);
				convertView.setTag(mViewHolder);
			}else{
			    mViewHolder=(ViewHolder)convertView.getTag();
			}
			       if(baseArrayListCommand.get(position).size()>0){
					if(baseArrayListCommand.get(position).get("commandAttribute").size()>0){
						HashMap<String, String>  map=baseArrayListCommand.get(position).get("commandAttribute").get(0);
						if(map!=null){
							mViewHolder.mTextViewSectionName.setText("标段: "+map.get("sectionName"));
							mViewHolder.mTextViewProjectName.setText("工程: "+map.get("projectName"));
							mViewHolder.mTextViewDraftDate.setText("拟稿日期: "+map.get("createTime"));
							mViewHolder.mTextViewDraftPerson.setText("拟稿人: "+map.get("empName"));
							if("0".equals(map.get("flowStatus"))){
								mViewHolder.mTextViewProcedureState.setText("流程状态: 未申报");
							}else if("1".equals(map.get("flowStatus"))){
								mViewHolder.mTextViewProcedureState.setText("流程状态: 待审批");
							}else if("2".equals(map.get("flowStatus"))){
								mViewHolder.mTextViewProcedureState.setText("流程状态: 已申报");
							}else if("3".equals(map.get("flowStatus"))){
								mViewHolder.mTextViewProcedureState.setText("流程状态: 已审批");
							}
							mViewHolder.mapAttribute=map;
						}
					}
				}
			
			return convertView;
			
		}
		private  class  ViewHolder{
			private TextView  mTextViewSectionName; //标段名称
			private TextView  mTextViewProjectName; // 工程名称
			private TextView  mTextViewDraftDate;     //拟稿日期
			private TextView  mTextViewDraftPerson; // 拟稿人
			private TextView  mTextViewProcedureState;//流程状态
			public HashMap<String, String> mapAttribute;
		}
		
	}

	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id){
			MyApdater.ViewHolder viewHolderItem=(MyApdater.ViewHolder)view.getTag();
			HashMap<String, String> map=viewHolderItem.mapAttribute;
			Intent intent=new Intent(mContext,CommandApproveAct.class);
//			intent.putExtra("type", mType);
			intent.putExtra("mapAttribute", map);
			startActivityForResult(intent, 0x09);
	}  
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==0x01){
			if(mMyAsyncTask==null){
				mMyAsyncTask = new MyAsyncTask(0);
			}else{
				if(!mMyAsyncTask.isCancelled()){
					 mMyAsyncTask.cancel(true);
						mMyAsyncTask = new MyAsyncTask(0);
				 }
			}
			 mMyAsyncTask.execute();
		}
//		super.onActivityResult(requestCode, resultCode, data);
	}

}
