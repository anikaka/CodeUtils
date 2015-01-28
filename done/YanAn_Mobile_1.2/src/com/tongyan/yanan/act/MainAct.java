package com.tongyan.yanan.act;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tongyan.yanan.common.adapter.ViewFragementAdapter;
import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.fragment.OaFragement;
import com.tongyan.yanan.fragment.PictrueFragement;
import com.tongyan.yanan.fragment.ProgressFragement;
import com.tongyan.yanan.fragment.QualityFragement;
import com.tongyan.yanan.fragment.SubsideFragement;

/**
 * @author ChenLang
 * @category 主界面
 * @date
 * @version YanAn 1.0
 */
public class MainAct extends FragmentActivity implements OnClickListener{
	ArrayList<Fragment>  mFragementList;
	//文件布局
	private   FragmentManager mFM;
	private   FragmentTransaction mFT; 
	//At the bottom of the main interface menu Layout
	 RelativeLayout mRl_oa, mRl_v,mRl_progress, mRl_quality,  mRl_prcture;
	//At the bottom of the main interface menu TextView
	TextView mTxt_oa,mTxt_v,mTxt_progress, mTxt_quality, mTxt_pic ,MTitle_common_content;
	//At the bottom of the main interface menu ImageView
	ImageView mImg_oa, mImg_v,mImg_progress, mImg_quality, mImg_pic;
	View   mView_oa,mView_v,mView_progress,mView_quality,mView_pic;
	Fragment  mLayoutOa,mLayoutV,mLayoutProgress,mLayoutQuality,mLayoutPicture;
	private ViewPager  mViewpager;
	// 当前的位置索引值
	ListView  mListViewSub;
	public void init(){
		TextView  mTitleName = (TextView)findViewById(R.id.title_common_content);
		mTitleName.setText(getResources().getString(R.string.txt_main_title));
		MTitle_common_content=(TextView)findViewById(R.id.title_common_content);
		mTxt_oa=(TextView)findViewById(R.id.txt_oa);
		mTxt_v=(TextView)findViewById(R.id.txt_v);
		mTxt_progress=(TextView)findViewById(R.id.txt_progress);
		mTxt_quality=(TextView)findViewById(R.id.txt_quality);
		mTxt_pic=(TextView)findViewById(R.id.txt_pic);

		mImg_oa=(ImageView)findViewById(R.id.img_oa);
		mImg_v=(ImageView)findViewById(R.id.img_v);
		mImg_progress=(ImageView)findViewById(R.id.img_progress);
		mImg_quality=(ImageView)findViewById(R.id.img_quality);
		mImg_pic=(ImageView)findViewById(R.id.img_pic);
		
		mView_oa=(View)findViewById(R.id.view_oa);
		mView_v=(View)findViewById(R.id.view_v);
		mView_progress=(View)findViewById(R.id.view_progress);
		mView_quality=(View)findViewById(R.id.view_quality);
		mView_pic=(View)findViewById(R.id.view_pic);
		
		mRl_oa=(RelativeLayout)findViewById(R.id.rl_oa);
		mRl_v=(RelativeLayout)findViewById(R.id.rl_v);
		mRl_progress=(RelativeLayout)findViewById(R.id.rl_progress);
		mRl_quality=(RelativeLayout)findViewById(R.id.rl_quality);
		mRl_prcture=(RelativeLayout)findViewById(R.id.rl_pic);
		
		// 实例化ViewPager
		mViewpager=(ViewPager)findViewById(R.id.viewpager);
		
		//设置点击事件
		mRl_oa.setOnClickListener(this);
		mRl_v.setOnClickListener(this);
		mRl_progress.setOnClickListener(this);
		mRl_quality.setOnClickListener(this);
		mRl_prcture.setOnClickListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//View v=LayoutInflater.from(this).inflate(R.layout.main, null);

		init();//The component initialization
		//第一次默认显示OA界面
     	mTxt_oa.setTextColor(getApplicationContext().getResources().getColor(R.color.color_pressed));
     	MTitle_common_content.setText(getApplication().getResources().getString(R.string.txt_oa));
		mImg_oa.setImageResource(R.drawable.icon_oa_focused);
		mView_oa.setVisibility(View.VISIBLE);
		initFragment();
		mFM=getSupportFragmentManager();
		mFT=mFM.beginTransaction();
		// 设置监听
		if(mViewpager!=null){			
			mViewpager.setOnPageChangeListener(new MyOnPageChangeListener());
		}
		// 设置适配器数据
		if(mViewpager!=null){
			mViewpager.setAdapter(new ViewFragementAdapter(getSupportFragmentManager()));
		}	
	}


	/** Framgent 初始化*/
	private void initFragment() {
			// 实例化Fragement
		OaFragement mFOa = new OaFragement();
		SubsideFragement mVF = new SubsideFragement();
		ProgressFragement mPF = new ProgressFragement();
		QualityFragement mQF = new QualityFragement();
		PictrueFragement mPicF = new PictrueFragement();
			// 实例化ArrayList对象
			mFragementList = new ArrayList<Fragment>();
			// 将要分页显示的Fragement装入数组中
			if (mFragementList != null) {
				mFragementList.add(mFOa);
				mFragementList.add(mVF);
				mFragementList.add(mPF);
				mFragementList.add(mQF);
				mFragementList.add(mPicF);
			}
	}

	/** 点击事件*/
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//OA界面
		case R.id.rl_oa:
			MTitle_common_content.setText(getResources().getString(R.string.txt_oa));
			setBottomTextColor();
			setImgPressed();
			setViewVisble();
	     	mTxt_oa.setTextColor(this.getResources().getColor(R.color.color_pressed));
			mImg_oa.setImageResource(R.drawable.icon_oa_focused);
			mView_oa.setVisibility(View.VISIBLE);
			mViewpager.setCurrentItem(Constants.FLAGEMENT_OA);
			break;
		//沉降界面
		case R.id.rl_v:
			MTitle_common_content.setText("沉降监测数据上传");
			setBottomTextColor();
			setImgPressed();
			setViewVisble();
			mTxt_v.setTextColor(this.getResources().getColor(R.color.color_pressed));
			mImg_v.setImageResource(R.drawable.icon_v_focused);
			mView_v.setVisibility(View.VISIBLE);
			mViewpager.setCurrentItem(Constants.FLAGEMENT_V);
			break;
		//进度界面
		case R.id.rl_progress:
			MTitle_common_content.setText("进度上报");
			setBottomTextColor();
			setImgPressed();
			setViewVisble();
			mTxt_progress.setTextColor(this.getResources().getColor(R.color.color_pressed));
			mImg_progress.setImageResource(R.drawable.icon_progress_focused);
			mView_progress.setVisibility(View.VISIBLE);
			mViewpager.setCurrentItem(Constants.FLAGEMENT_PROGRESS);
			break;
		//质量界面
		case R.id.rl_quality:
			setBottomTextColor();
			setImgPressed();
			setViewVisble();
			mTxt_quality.setTextColor(this.getResources().getColor(R.color.color_pressed));
			mImg_quality.setImageResource(R.drawable.icon_quality_focused);
			mView_quality.setVisibility(View.VISIBLE);
			mViewpager.setCurrentItem(Constants.FLAGEMENT_QUALITY);
			break;
		//照片界面
		case R.id.rl_pic:
			setBottomTextColor();
			setImgPressed();
			setViewVisble();
			mTxt_pic.setTextColor(this.getResources().getColor(R.color.color_pressed));
			mImg_pic.setImageResource(R.drawable.icon_prcture_focused);
			mView_pic.setVisibility(View.VISIBLE);
			mViewpager.setCurrentItem(Constants.FLAGEMENT_PICTURE);
			break;
		default:
			break;
		}
	}
	
	/** 设置底部菜单的图片默认为正常(没有点击) */
	public void setImgPressed() {
		mImg_oa.setImageResource(R.drawable.icon_oa_normal);
		mImg_v.setImageResource(R.drawable.icon_v_normal);
		mImg_progress.setImageResource(R.drawable.icon_progress_normal);
		mImg_quality.setImageResource(R.drawable.icon_quality_normal);
		mImg_pic.setImageResource(R.drawable.icon_prcture_normal);
	}
	
	/** 设置底部菜单的文字默认为正常(没有点击) */
	public void setBottomTextColor() {
		mTxt_oa.setTextColor(getResources().getColor(R.color.gray));
		mTxt_v.setTextColor(getResources().getColor(R.color.gray));
		mTxt_progress.setTextColor(getResources().getColor(R.color.gray));
		mTxt_quality.setTextColor(getResources().getColor(R.color.gray));
		mTxt_pic.setTextColor(getResources().getColor(R.color.gray));
	}
	
	/** 设置底部菜单的View默认为不可视 */
	public void setViewVisble() {
		mView_oa.setVisibility(View.INVISIBLE);
		mView_v.setVisibility(View.INVISIBLE);
		mView_progress.setVisibility(View.INVISIBLE);
		mView_quality.setVisibility(View.INVISIBLE);
		mView_pic.setVisibility(View.INVISIBLE);
	}
	
	/**选择 页面监听 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int position) {
			switch (position) {
			case Constants.FLAGEMENT_OA:
				setBottomTextColor();
				setImgPressed();
				setViewVisble();
		     	mTxt_oa.setTextColor(getResources().getColor(R.color.color_pressed));
				mImg_oa.setImageResource(R.drawable.icon_oa_focused);
				mView_oa.setVisibility(View.VISIBLE);
				MTitle_common_content.setText("OA");
				break;
			case Constants.FLAGEMENT_V:
				MTitle_common_content.setText("沉降监测数据上传");
				setBottomTextColor();
				setImgPressed();
				setViewVisble();
				mTxt_v.setTextColor(getResources().getColor(R.color.color_pressed));
				mImg_v.setImageResource(R.drawable.icon_v_focused);
				mView_v.setVisibility(View.VISIBLE);
				break;
			case Constants.FLAGEMENT_PROGRESS:
				MTitle_common_content.setText("进度上报");
				setBottomTextColor();
				setImgPressed();
				setViewVisble();
				mTxt_progress.setTextColor(getResources().getColor(R.color.color_pressed));
				mImg_progress.setImageResource(R.drawable.icon_progress_focused);
				mView_progress.setVisibility(View.VISIBLE);
				break;
			case Constants.FLAGEMENT_QUALITY:
				setBottomTextColor();
				setImgPressed();
				setViewVisble();
				mTxt_quality.setTextColor(getApplicationContext().getResources().getColor(R.color.color_pressed));
				mImg_quality.setImageResource(R.drawable.icon_quality_focused);
				mView_quality.setVisibility(View.VISIBLE);
				MTitle_common_content.setText("质量监测");
				break;
			case Constants.FLAGEMENT_PICTURE:
				setBottomTextColor();
				setImgPressed();
				setViewVisble();
				mTxt_pic.setTextColor(getResources().getColor(R.color.color_pressed));
				mImg_pic.setImageResource(R.drawable.icon_prcture_focused);
				mView_pic.setVisibility(View.VISIBLE);
				MTitle_common_content.setText("照片上传");
				break;
			default:
				break;
			}
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}

	}
	
 }
