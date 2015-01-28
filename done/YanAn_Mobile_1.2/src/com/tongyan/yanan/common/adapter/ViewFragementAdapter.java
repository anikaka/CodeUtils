package com.tongyan.yanan.common.adapter;

import java.util.ArrayList;

import com.tongyan.yanan.common.utils.Constants;
import com.tongyan.yanan.fragment.OaFragement;
import com.tongyan.yanan.fragment.PictrueFragement;
import com.tongyan.yanan.fragment.ProgressFragement;
import com.tongyan.yanan.fragment.QualityFragement;
import com.tongyan.yanan.fragment.SubsideFragement;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * @author ChenLang
 * @category Fragement适配器
 *	@version YanAn 1.0
 */
public class ViewFragementAdapter extends FragmentPagerAdapter {
	
    public ViewFragementAdapter(FragmentManager fm) {
    	super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		
		case Constants.FLAGEMENT_OA:{
			return 	new OaFragement();
		}

		
		case Constants.FLAGEMENT_V:{			
			return new SubsideFragement();
		}
		
		case Constants.FLAGEMENT_PROGRESS:{			
			return new ProgressFragement();
		}
		
		case  Constants.FLAGEMENT_QUALITY:{			
			return new QualityFragement();
		}
		
		case Constants.FLAGEMENT_PICTURE:{
			return new PictrueFragement();
		}
		default:
			break;
		}
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Constants.FLAGEMENT_COUNT;
	}
    

  

}
