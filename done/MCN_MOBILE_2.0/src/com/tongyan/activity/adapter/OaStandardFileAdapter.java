package com.tongyan.activity.adapter;

import java.io.File;
import java.util.List;

import com.tongyan.activity.R;
import com.tongyan.utils.FileUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 
 * @ClassName P22_StandardFileAdapter 
 * @author wanghb
 * @date 2013-7-29 am 10:44:13
 * @desc TODO
 */
public class OaStandardFileAdapter extends BaseAdapter {
	
	private LayoutInflater layoutInflater;
	private  List<File> fileList;
	
	public OaStandardFileAdapter(Context context, List<File> fileList) {
		layoutInflater = LayoutInflater.from(context);
		this.fileList = fileList;
	}
	
	@Override
	public int getCount() {
		return fileList == null ? 0 : fileList.size();
	}

	@Override
	public Object getItem(int position) {
		if (fileList != null) {
			return fileList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.oa_standard_file_list_item, null);
			holder = new ViewHolder();
			holder.mIconView = (ImageView)convertView.findViewById(R.id.standard_file_icon);
			holder.nameTextView = (TextView) convertView.findViewById(R.id.standard_file_name);
			holder.sizeTextView = (TextView) convertView.findViewById(R.id.standard_file_size);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		File file = fileList.get(position);
		String fileName = file.getName();
		if(file.isDirectory()) {
			holder.sizeTextView.setVisibility(View.GONE);
			holder.mIconView.setBackgroundResource(R.drawable.document_folder);
		} else if(fileName.endsWith(".pdf")) {
			holder.sizeTextView.setVisibility(View.VISIBLE);
			holder.sizeTextView.setText(FileUtils.getFileSize(file.length()));
			holder.mIconView.setBackgroundResource(R.drawable.documents_icon_pdf);
		} else if(fileName.endsWith(".doc") || fileName.endsWith(".docx")) {
			holder.sizeTextView.setVisibility(View.VISIBLE);
			holder.sizeTextView.setText(FileUtils.getFileSize(file.length()));
			holder.mIconView.setBackgroundResource(R.drawable.documents_icon_doc);
		} else if(fileName.endsWith(".xlsx")) {
			holder.sizeTextView.setVisibility(View.VISIBLE);
			holder.sizeTextView.setText(FileUtils.getFileSize(file.length()));
			holder.mIconView.setBackgroundResource(R.drawable.documents_icon_ppt);
		} else {
			holder.sizeTextView.setVisibility(View.VISIBLE);
			holder.sizeTextView.setText(FileUtils.getFileSize(file.length()));
			holder.mIconView.setBackgroundResource(R.drawable.documents_icon_other);
		}
		holder.nameTextView.setText(fileName);
		holder.mFile = file;
		return convertView;
	}
	
	public final class ViewHolder {
		public ImageView mIconView;
		public TextView nameTextView;
		public TextView sizeTextView;
		public File mFile;
	}
	
}
