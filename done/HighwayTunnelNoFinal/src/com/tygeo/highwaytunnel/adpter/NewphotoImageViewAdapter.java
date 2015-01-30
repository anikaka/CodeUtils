package com.tygeo.highwaytunnel.adpter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tygeo.highwaytunnel.R;
import com.tygeo.highwaytunnel.activity.Chek_form;
import com.tygeo.highwaytunnel.activity.NewphotoShow;
import com.tygeo.highwaytunnel.common.ImageUtil;
import com.tygeo.highwaytunnel.common.StaticContent;
import com.tygeo.highwaytunnel.entity.LineSearch;
import com.tygeo.highwaytunnel.entity.newphotoentity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class NewphotoImageViewAdapter extends BaseAdapter {
	private List<newphotoentity> list;
	private LayoutInflater inflater;
	private int selectItem = -1;
	public static Map<Integer, Boolean> isSelected;
	private Context ctx;
	ArrayList<CheckBox> cb;
	ArrayList<newphotoentity> np;
	
	public NewphotoImageViewAdapter(List<newphotoentity> list, Context ctx) {
		this.list = list;
		
		this.ctx = ctx;
		this.inflater = LayoutInflater.from(ctx);
		cb=new ArrayList<CheckBox>();
		np=new ArrayList<newphotoentity>();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}
	
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	// TODO Auto-generated method stub
	public long getItemId(int position) {
		return position;
	}
	
	public int getposition(String name) {
		int i = list.indexOf(name);
		return i;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ResultComp comp = null;
		if (convertView == null) {
			convertView = inflater
					.inflate(R.layout.pohotoimagetype, null);
			comp = new ResultComp();
			comp.text1=(TextView)convertView.findViewById(R.id.photoimagetextview);
			comp.checkbox = (CheckBox) convertView
					.findViewById(R.id.photoimagecheckbox);
			comp.imageview = (ImageView) convertView
			.findViewById(R.id.phototypeimageview);
			convertView.setTag(comp);
			comp.imageview.setId(position);			
		} else {
			comp = (ResultComp) convertView.getTag();
		}
		// if (position == 0) {
		// convertView.setSelected(true);
		// convertView.setBackgroundResource(R.drawable.blue);
		// }
//		if (position == StaticContent.listselectindex) {
//			convertView.setBackgroundResource(R.drawable.blue);
//		} else {
//			convertView.setBackgroundResource(R.drawable.maybehs);
//		}
		if(parent.getChildCount() == position)  
		{  
			list.get(position).setIndex(position);
			comp.text1.setText(list.get(position).getPhotoname());
			ImageUtil util=new ImageUtil();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize=2;
			if (list.get(position).getPhoto_type().equals("内")) {
			
				
				
				Bitmap	bm = BitmapFactory.decodeFile(list.get(position).getPhotourl()+".jpg", options);
				byte[] b =util.Bitmap2Bytes(bm);
				System.out.println("压缩前 :"+b.length);
//				bitlist.add(bm);
				Bitmap	b2=ThumbnailUtils.extractThumbnail(bm, 300, 160);
				comp.imageview.setImageBitmap(b2);
			}
			else{
				String s=list.get(position).getPhotourl()+".jpg";
				File f=new File(s);
				if (!f.exists()) {
					comp.imageview.setBackgroundResource(R.drawable.outphoto);
				}else{
					Bitmap	bm = BitmapFactory.decodeFile(list.get(position).getPhotourl()+".jpg", options);
					byte[] b =util.Bitmap2Bytes(bm);
					System.out.println("压缩前 :"+b.length);
//					bitlist.add(bm);
					Bitmap	b2=ThumbnailUtils.extractThumbnail(bm, 600, 300);
					
					comp.imageview.setImageBitmap(b2);	
					
				}
				
			}
			comp.checkbox.setChecked(list.get(position).getFlag()==1);
			cb.add(comp.checkbox);
			comp.checkbox.setOnCheckedChangeListener( new listviewButtonListener(position));
		comp.imageview.setOnClickListener(new ImageViewButtonListener(position));}
		
	
		return convertView;
	}
	
	public final class ResultComp {
		public TextView text1;
		public CheckBox checkbox;
		public ImageView imageview;
	}
	public  ArrayList<CheckBox> DoBox(){
		return cb;
	}
	
	class listviewButtonListener implements OnCheckedChangeListener {
		private int position;
		
		public listviewButtonListener(int i) {
			position = i;
		}
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if (isChecked) {
				
				list.get(position).setFlag(1);
//				isSelected.put(position, true);
				NewphotoShow.checked.set(position, 1+"");
				System.out.println(position+" set true");
			}
			if(isChecked==false) {
				System.out.println(position+" set false");
//				if (!(np.indexOf(list.get(position))==-1)) {
				NewphotoShow.checked.set(position, 0+"");	
				list.get(position).setFlag(0);
//				isSelected.put(position, false);
//				}
			}
			
			
			
		}

	
			
		
		
	}
	
	
	class ImageViewButtonListener implements  View.OnClickListener {
		private int position;

		public ImageViewButtonListener(int i) {
			position = i;
		}


		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			LinearLayout ly = new LinearLayout(ctx);
			LayoutInflater inflater = LayoutInflater
					.from(ctx);
			
			 View view = inflater.inflate(
					R.layout.imageviewshow, null);
			ImageView imageview = (ImageView) view
					.findViewById(R.id.imageviewshowView);
			ly.addView(view);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize=2;
			ImageUtil util=new ImageUtil();
			if (list.get(position).getPhoto_type().equals("内")) {
				Bitmap	bm = BitmapFactory.decodeFile(list.get(position).getPhotourl()+".jpg", options);
				byte[] b =util.Bitmap2Bytes(bm);
				System.out.println("压缩前 :"+b.length);
//				bitlist.add(bm);
				Bitmap	b2=ThumbnailUtils.extractThumbnail(bm, 600, 300);
				
				imageview.setImageBitmap(b2);	
			}else{
				String s=list.get(position).getPhotourl()+".jpg";
				File f=new File(s);
				if (!f.exists()) {
					imageview.setBackgroundResource(R.drawable.outphoto);
				}else{
					Bitmap	bm = BitmapFactory.decodeFile(list.get(position).getPhotourl()+".jpg", options);
					byte[] b =util.Bitmap2Bytes(bm);
					System.out.println("压缩前 :"+b.length);
//					bitlist.add(bm);
					Bitmap	b2=ThumbnailUtils.extractThumbnail(bm, 600, 300);
					
					imageview.setImageBitmap(b2);	
					
				}
				
			}
			
			Dialog d=new Dialog(ctx);
			d.requestWindowFeature(Window.FEATURE_NO_TITLE);
			d.setContentView(ly);
			
			d.show();
			d.setCanceledOnTouchOutside(true);
			
		}
	}
	
	
	
	public  ArrayList<newphotoentity> GetCheckedBOx(){
		return np;
	}
	public  void RemoveCheck(newphotoentity i){
	   np.remove(i);
	   
	}
	
	
}
