package com.tongyan.activity.adapter;

import java.util.List;

import com.tongyan.activity.R;
import com.tongyan.common.db.DBService;
import com.tongyan.common.entities._HolefaceSettingInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 
 * @ClassName P35_RisksListAdapter 
 * @author wanghb
 * @date 2013-9-3 am 10:44:13
 * @desc TODO
 */
public class RiskSettingAdapter extends BaseAdapter {
	
	private Context context;
	private LayoutInflater layoutInflater;
	
	private List<String> riskList;
	
	private String $holefaceId;
	private String $riskId;
	
	public RiskSettingAdapter(Context context, List<String> riskList, String $holefaceId, String $riskId) {
		layoutInflater = LayoutInflater.from(context);
		this.riskList = riskList;
		this.context = context;
		this.$holefaceId = $holefaceId;
		this.$riskId = $riskId;
	}
	
	
	@Override
	public int getCount() {
		return riskList == null ? 0 : riskList.size();
	}

	@Override
	public Object getItem(int position) {
		if (riskList != null) {
			return riskList.get(position);
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
			convertView = layoutInflater.inflate(R.layout.risk_camera_list_item, null);
			holder = new ViewHolder();
			holder.mSettingNameTextView = (TextView)convertView.findViewById(R.id.p36_risk_setting_type_text);
			holder.mIsSaveImageView = (TextView)convertView.findViewById(R.id.p36_risk_bad_building_setting_is_sure);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		String riskType = riskList.get(position);
		if(riskType != null) {
			holder.mSettingNameTextView.setText(riskType);
			List<_HolefaceSettingInfo> mHolefaceInfoList = new DBService(context).getHolefaceSettingInfoByRiskType($holefaceId,riskType);
			if(mHolefaceInfoList != null && mHolefaceInfoList.size() > 0) {
				boolean isCheck = true;
				for(int i = 0; i< mHolefaceInfoList.size(); i ++) {
					_HolefaceSettingInfo mInfo = mHolefaceInfoList.get(i);
					if(mInfo != null) {
						if(!"1".equals(mInfo.getIsCheck())) {
							isCheck = false;
						}
					}
				}
				if(isCheck) {
					//holder.mIsSaveImageView.setBackgroundResource(R.drawable.p36_check_out_yes);
					holder.mIsSaveImageView.setText(context.getResources().getString(R.string.is_start));
					holder.mIsSaveImageView.setTextColor(context.getResources().getColor(R.color.common_color));
				} else {
					holder.mIsSaveImageView.setText(context.getResources().getString(R.string.no_start));
					holder.mIsSaveImageView.setTextColor(context.getResources().getColor(R.color.gray));
					//holder.mIsSaveImageView.setBackgroundResource(R.drawable.p36_check_out_no);
				}
			}
			
			holder.mSettingName = riskType;
			holder.$riskId = $riskId;
			holder.$holefaceId = $holefaceId;
		}
		return convertView;
	}
	
	public class ViewHolder {
		public TextView mSettingNameTextView;
		public TextView mIsSaveImageView;
		public String mSettingName;
		public String $riskId;
		public String $holefaceId;
	}
}
