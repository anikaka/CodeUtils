package com.tygeo.highwaytunnel.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.tygeo.highwaytunnel.R;
import com.tygeo.highwaytunnel.DBhelper.DBHelper;
import com.tygeo.highwaytunnel.DBhelper.DB_Provider;
import com.tygeo.highwaytunnel.activity.hidecheck.HideCheckMainActivity;
import com.tygeo.highwaytunnel.adpter.TaskInfoAdapter;
import com.tygeo.highwaytunnel.common.InfoApplication;
import com.tygeo.highwaytunnel.common.StaticContent;
import com.tygeo.highwaytunnel.common.WebServiceUtil;
import com.tygeo.highwaytunnel.entity.DiseaseInfoDto;
import com.tygeo.highwaytunnel.entity.EquipmentCheckContent;
import com.tygeo.highwaytunnel.entity.Task;
import com.tygeo.highwaytunnel.entity.UpdateInfo;

public class Task_info extends Activity {
	/*
	 * ������Ϣ
	 */
	Button delete, c_chek, t_chek, edit, update,
				mHideCheck; //�����Ų�
	AlertDialog.Builder builder;
	AlertDialog dialog;
	TextView info;
	String ci;
	private SQLiteDatabase database;
	private Button back_btn, add_btn, cancel;// exit_btn,next_btn,
	private ListView listview;
	TaskInfoAdapter adapter;
	String elec = "Ele_Check";
	Task task;
	DBHelper db;
	Context di;
	List<Task> listData;
	String str_taskName;
	String tname;
	int _id, sition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.task_info);
		InfoApplication.getinstance().addActivity(this);
		db = new DBHelper(StaticContent.DataBasePath);
		// ��ȡ��������
		str_taskName = ((InfoApplication) getApplication()).getTask_name();
//		System.out.println(str_taskName);
		// ��ʼ�����
		listview = (ListView) findViewById(R.id.Task_Listview);
		info = (TextView) findViewById(R.id.Task_InfoText);
		info.setText(str_taskName);
		// exit_btn = (Button) findViewById(R.id.Task_ExitBtn);
		back_btn = (Button) findViewById(R.id.Task_BackBtn);
		add_btn = (Button) findViewById(R.id.Task_AddBtn);
		// ���ؼ���
		back_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// startActivity(new Intent(Task_info.this, Tunnel_info.class));
				finish();
			}
		});
		query();
		add_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Task_info.this, Task_add.class);
				Task task1 = new Task();
				task1.set_id(0);
				task1.setTask_name(str_taskName);
				task1.setCheck_date(" ");
				task1.setBegin_num(" ");
				task1.setEnd_num(" ");
				task1.setCivil_check(" ");
				task1.setTacilitiy_check(" ");
				task1.setCheck_head(" ");
				task1.setCheck_member(" ");
				task1.setUp_check_direciton("");
				task1.setDown_check_direciton("");
				task1.setMainte_org(" ");
				task1.setWeather(" ");
				task1.setTemperature(25);
				task1.setHumidity(70);
				task1.setPicture_beginnum(0);
				task1.setPatrol_car(" ");
				task1.setOperating_car(" ");
				task1.setCheck_type("");
				((InfoApplication) getApplication()).setNew_task(task1);
				startActivity(intent);
			}
		});
		
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,int position, long id) {
				sition = position;
				// ����application��������
				((InfoApplication) getApplication()).setTask(listData.get(position));
				String task_info = ((InfoApplication) getApplication()).getTask().getTask_name();
				String check_info = ((InfoApplication) getApplication()).getTask().getCheck_type();
				StaticContent.TaskStartMile=listData.get(position).getUp_num();
				StaticContent.TaskEndMile=listData.get(position).getDown_num();
				StaticContent.BeginMile=Integer.parseInt(listData.get(position).getUp_num().split("K")[1].split("\\+")[0])*1000+Integer.parseInt(listData.get(position).getUp_num().split("K")[1].split("\\+")[1]);
				StaticContent.EndMile=Integer.parseInt(listData.get(position).getDown_num().split("K")[1].split("\\+")[0])*1000+Integer.parseInt(listData.get(position).getDown_num().split("K")[1].split("\\+")[1]);
				if (((InfoApplication) getApplication()).getTask().getTacilitiy_check().equals("�����Լ���")) {
					elec = "Ele_Check_jc";
					// finish();
					StaticContent.ele_check_type = 2;
				} else {
					StaticContent.ele_check_type = 3;
				}
				// if
				// (listData.get(position).getCheck_direciton().equals("���ڡ�����"))
				// {
				// StaticContent.check_for = "����";
				//
				// }
				// if
				// (listData.get(position).getCheck_direciton().equals("���ڡ�����"))
				// {
				// StaticContent.check_for = "����";
				//
				// }
				String type = StaticContent.Up_Down;
				StaticContent.update_id = listData.get(position).getUpdate_id();
				String tt = type + "+";
				String t1 = listData.get(position).getUp_num();
//				String temp1 = t1.replace("K", "");
				try {
					String  z=t1.split("K")[1];
					int q=Integer.parseInt(z.split("+")[0]);
					int p=Integer.parseInt(z.split("+")[1]);
					StaticContent.BeginMile=q*1000+p;
				} catch (Exception e) {
					e.printStackTrace();
				}
				String t2 = listData.get(position).getDown_num();
//				String temp2 = t2.replace("YK", "");
//				String a2[] = temp2.split("\\+");
//				String e1 = a2[0];
//				String e2 = a2[1];
//				int k2 = Integer.parseInt(e1) * 1000;
//				int g2 = Integer.parseInt(e2);
//				StaticContent.ybegin_melete = k2 + g2;
//				 StaticContent.Yend_melete = e1 + "+" + g2;
				try {
				StaticContent.melete_count =Math.abs(StaticContent.EndMile-StaticContent.BeginMile);
//				StaticContent.Ymelete_count = StaticContent.Tyend_melete
//						- StaticContent.ybegin_melete;
				// ��������
				tname = listData.get(position).getTask_name();
				StaticContent.task_name = tname;
				} catch (Exception e) {
				}
				int task_id = listData.get(position).get_id();
				System.out.println("�����id:  " + task_id);
				StaticContent.task_id = task_id;
				System.out.println("list���������ƣ�" + tname);
				// ����id
				_id = listData.get(position).get_id();
				// ������
				// builder = new Builder(Task_info.this);
				LayoutInflater inflater = (LayoutInflater) Task_info.this.getSystemService(LAYOUT_INFLATER_SERVICE);
				final View DialogView = inflater.inflate(R.layout.alert_style,null);
				dialog = new AlertDialog.Builder(Task_info.this)
						.setNegativeButton("ȡ��",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,int which) {
										dialog.dismiss();
									}
								}).setView(DialogView).show();
				c_chek = (Button) DialogView.findViewById(R.id.alert_ccehck);
				t_chek = (Button) DialogView.findViewById(R.id.alert_tcheck);
				mHideCheck=(Button)DialogView.findViewById(R.id.hideCheck); // �����Ų鰴ť,�������
				edit = (Button) DialogView.findViewById(R.id.alert_edit);
				delete = (Button) DialogView.findViewById(R.id.alert_delete);
				update = (Button) DialogView.findViewById(R.id.alert_Update);
				Resources resource = (Resources) getBaseContext().getResources();
				ColorStateList csl = (ColorStateList) resource.getColorStateList(R.drawable.btnblue);
				ci = listData.get(position).getCheck_type().toString().trim();	
				if (ci.equals("�����ṹ")) {
					t_chek.setClickable(false);
					mHideCheck.setClickable(false);
//					mHideCheck.setBackgroundResource(R.drawable.hs);
					t_chek.setBackgroundResource(R.drawable.hs);
					c_chek.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							if (listData.get(sition).getUp_check_direciton().equals("0")) {
								StaticContent.check_for = "����";
								StaticContent.S_X = "S"; //ChenLang ����,
							} else {
								StaticContent.check_for = "����";
								StaticContent.S_X = "X";
							}
							startActivity(new Intent(Task_info.this,Civil_Check.class));
						}
					});
				}
				
				if (ci.equals("������ʩ")) {
					c_chek.setClickable(false);
					mHideCheck.setClickable(false);
					c_chek.setBackgroundResource(R.drawable.hs);
//					mHideCheck.setBackgroundResource(R.drawable.hs);
					t_chek.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							try {
								StaticContent.now_zh =StaticContent.BeginMile;
							} catch (Exception e) {
								StaticContent.now_zh=Integer.parseInt(StaticContent.TunnelBeginMile.split("k")[1]);
							}
							if (listData.get(sition).getUp_check_direciton().equals("0")) {
								StaticContent.check_for = "����";
								StaticContent.S_X = "S"; //ChenLang ����,
							} else {
								StaticContent.check_for = "����";
								StaticContent.S_X = "X"; //ChenLang ����,
							}
							if (((InfoApplication) getApplication()).getTask().getTacilitiy_check().equals("�����Լ���")) {
									elec = "Ele_Check_jc";
								// finish();
								StaticContent.ele_check_type = 2;
								startActivity(new Intent(Task_info.this,Ele_Check_jc.class));
							} else {
								StaticContent.ele_check_type = 3;
								startActivity(new Intent(Task_info.this,Ele_Check.class));
							}
						}
					});	
				}
//				//TODO �������
//				if("�������".equals(listData.get(position).getCivil_check())){
//					c_chek.setClickable(false);
//					t_chek.setClickable(false);
//					mHideCheck.setClickable(true);
//					c_chek.setBackgroundResource(R.drawable.hs);
//					t_chek.setBackgroundResource(R.drawable.hs);
////					mHideCheck.setBackgroundResource(R.drawable.white);
//					mHideCheck.setOnClickListener(new View.OnClickListener() {
//						
//						@Override
//						public void onClick(View v) {
//						 Intent  intent=new Intent(Task_info.this, HideCheckMainActivity.class);
//						 startActivity(intent);
//						}
//					});
//				}else{
//					mHideCheck.setClickable(false);
//					mHideCheck.setBackgroundResource(R.drawable.hs);
//				}

				// �����ṹ���
				if (ci.equals("�����ṹ/������ʩ")) {
					c_chek.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
													StaticContent.S_X = "S";
													StaticContent.Up_Down = "ZK";
													StaticContent.now_zh =StaticContent.BeginMile;
													if (listData.get(sition).getDown_check_direciton().equals("0")) {
														StaticContent.check_for = "����";
														StaticContent.S_X = "X"; //ChenLang ����,
													} else {
														StaticContent.check_for = "����";
														StaticContent.S_X = "Y"; //ChenLang ����,
													}
													startActivity(new Intent(Task_info.this,Civil_Check.class));
												}
						}
					);
					t_chek.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							StaticContent.now_zh =StaticContent.BeginMile;
							if (listData.get(sition).getUp_check_direciton().equals("0")) {
								StaticContent.check_for = "����";
								StaticContent.S_X = "S"; //ChenLang ����,
							} else {
								StaticContent.check_for = "����";
								StaticContent.S_X = "X"; //ChenLang ����,
							}
							if (((InfoApplication) getApplication()).getTask().getTacilitiy_check().equals("�����Լ���")) {
								elec = "Ele_Check_jc";
								// finish();
								StaticContent.ele_check_type = 2;
								startActivity(new Intent(Task_info.this,Ele_Check_jc.class));
							} else {
								StaticContent.ele_check_type = 3;
								startActivity(new Intent(Task_info.this,Ele_Check.class));
							}
						}
					});
				}
				
				// ������ʩ���
				// �༭����
				edit.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						dialog.dismiss();
						Task task = listData.get(sition);
						((InfoApplication) getApplication()).setTask(task);
						startActivity(new Intent(Task_info.this, Task_add.class));
						// finish();
					}
				});
				
				//�����ϴ�
				update.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						dialog.dismiss();
						String text="";
						if (listData.get(sition).getIs_update()==1) {
							text="���ϴ�,�Ƿ���Ҫ�����ϴ�?";
						}else{
							text="�Ƿ��ϴ�?";
						}
						AlertDialog dilog = new AlertDialog.Builder(Task_info.this).setTitle(text)
								.setPositiveButton("ȷ��",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(DialogInterface dialog,int which) {
												// stub
												dialog.dismiss();
												// UpdateInfo info =
												// Web_date_provider
												// .GetCheckInfo(
												// StaticContent.update_id,
												// listData.get(sition));
												// JSONObject jb =
												// WebServiceUtil
												// .GetCheckInfo(info);
												// WebServiceUtil
												// .RequestBaseCheckInfo(jb);

												new Thread(new Runnable() {

													@Override
													public void run() {
														try {
															if (listData.get(sition).getCivil_check().equals("�ճ����")) {
																   //�������
																	String jb = UpdateInfo.getde(listData.get(sition).getUp_check_direciton());
																	//��������
																	JSONArray ja = DiseaseInfoDto.GetDiseaseInfo(StaticContent.update_id);
																	boolean f = WebServiceUtil.RequestTest(jb,	ja);
																	if (f == true) {
																		listData.get(sition).setIs_update(1);
																		handler.sendEmptyMessage(2);
																	} else {
																		handler.sendEmptyMessage(22);
																	}
																	Map<String, ArrayList<String>> map = DB_Provider.GetTaskPicPath(StaticContent.update_id);
																	if ((map.size()> 0)) {
																		boolean fz = WebServiceUtil.UpdatePic();
																		if (!fz) {
																			handler.sendEmptyMessage(4);
																		} else {
																			handler.sendEmptyMessage(5);
																		}
																}
															}
															if (listData	.get(sition).getCivil_check().equals("���ڼ��")) {
																	String jb = UpdateInfo.getdq(listData.get(sition).getUp_check_direciton());
																	String ja = DiseaseInfoDto.GetDiseaseInfoDQ(StaticContent.update_id);
//																	Log.i("test","��������="+ja);
																	boolean f = WebServiceUtil.RequestTestDQ(jb,ja);
																	if (f == true) {
																		listData.get(sition).setIs_update(1);	
																		handler.sendEmptyMessage(2);
																	} else {
																		handler.sendEmptyMessage(22);
																	}
																	Map<String, ArrayList<String>> map = DB_Provider.GetTaskPicPath(StaticContent.update_id);
																	if ((map.size()> 0)) {
																		boolean fz = WebServiceUtil.UpdatePic();
																		if (!fz) {
																			handler.sendEmptyMessage(4);
																		} else {
																			handler.sendEmptyMessage(5);
																		}
																}
															}
														} catch (Exception e) {
															e.printStackTrace();
														}
														try {
															if (listData	.get(sition).getTacilitiy_check().equals("�����Լ���")) {

																if (EquipmentCheckContent.ischeckone()) { // ����м���¼
																	EquipmentCheckContent ep = new EquipmentCheckContent();
																	String ja = ep.getEleCheckJsonJC(0);
																	String jb = ep.getEleJCDailycheckJC(0);
																	boolean f = WebServiceUtil.SavaEquipmentRegularlyCheck(ja,jb);
																	if (f == true) {
																		listData.get(sition).setIs_update(1);
																		handler.sendEmptyMessage(6);
																	} else {
																		handler.sendEmptyMessage(23);
																	}

																} else {
																	handler.sendEmptyMessage(3);
																}
															}
															if (listData.get(sition).getTacilitiy_check().equals("���ڼ���")) {
																if (EquipmentCheckContent.ischeckone()) { 					// ����м���¼
																	EquipmentCheckContent ep = new EquipmentCheckContent();
																	String ja = ep.getEleCheckJsonJC(0);
																	String jb = ep.getEleJCDailycheckJC(1);
																	boolean f = WebServiceUtil.SavaEquipmentPeriodicCheck(ja,jb);
																	if (f == true) {
																		listData.get(sition).setIs_update(1);
																		handler.sendEmptyMessage(6);
																	} else {
																		handler.sendEmptyMessage(23);
																	}
																} else {
																	handler.sendEmptyMessage(3);
																}
															}

														} catch (Exception e) {
															e.printStackTrace();
														}
													}
												}).start();

												// Toast t = new Toast(						
												// Task_info.this);
												// t.makeText(Task_info.this,
												// "�ϴ����",
												// Toast.LENGTH_LONG)
												// .show();
												query();
											}
										})
								.setNegativeButton("ȡ��",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
											}
										}).show();

					}
				});
				
				// ɾ������
				delete.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
						AlertDialog dia = new AlertDialog.Builder(
								Task_info.this)
								.setTitle("�Ƿ�ɾ��")
								.setPositiveButton("ȷ��",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												// stub
												if (listData.size() == 1) {
													String sql1 = "delete from"
															+ " TASK where _id='"
															+ _id + "'";
													DB_Provider.deleteBH(StaticContent.update_id);
													db.execSql(sql1);
													Intent intent = new Intent(Task_info.this,Task_info.class);
													intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
													//
													startActivity(intent);
												} else {
													dialog.dismiss();
													String sql = "delete from"
															+ " TASK where _id='"
															+ _id + "'";
													DB_Provider.deleteBH(StaticContent.update_id);
													db.execSql(sql);
													query();
												}
												// startActivity(new Intent(
												// Task_info.this,
												// Task_info.class));

												// adapter.notifyDataSetChanged();
											}

										})
								.setNegativeButton("ȡ��",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												// stub
												dialog.dismiss();

											}
										}).show();
					}
					// }
				});
				// builder.show();
				// dialog.show();
			}
		});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				Toast.makeText(Task_info.this, "�ϴ�ʧ��,���������ַ�������ϴ�", Toast.LENGTH_SHORT).show();
				break;
			case 1:
				toast();
				break;
			case 2:
				Toast.makeText(Task_info.this, "�����ṹ�����ϴ����", Toast.LENGTH_SHORT).show();
				break;
			case 11:
				Toast.makeText(Task_info.this, "�����������������к��ύ", Toast.LENGTH_SHORT).show();
				break;
			case 12:
				Toast.makeText(Task_info.this, "�����������������к��ύ",Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(Task_info.this, "���� ���������ݲ�����",Toast.LENGTH_SHORT).show();
				break;
			case 22:
				Toast.makeText(Task_info.this, "�����ṹ�����ϴ�ʧ��,���ӳ�ʱ,���������ַ�������ϴ�",Toast.LENGTH_SHORT).show();
				break;
			case 23:
				Toast.makeText(Task_info.this, "������ʩ�����ϴ�ʧ��,���ӳ�ʱ,���������ַ�������ϴ�",Toast.LENGTH_SHORT).show();
				break;
			case 4:
				Toast.makeText(Task_info.this, "ͼƬ�ϴ�ʧ��,���ӳ�ʱ,���������ַ�������ϴ�",Toast.LENGTH_SHORT).show();
				break;
			case 5:
				Toast.makeText(Task_info.this, "����ͼƬ�ϴ��ɹ�",Toast.LENGTH_SHORT).show();
				break;
			case 6:
				Toast.makeText(Task_info.this, "������ʩ�����ϴ����",Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		};
	};
	
	String message;

	public void toast() {
		Toast.makeText(Task_info.this, message, 1).show();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			// startActivity(new Intent(Task_info.this,Tunnel_info.class));
			finish();
			break;

		default:
			break;
		}
		return false;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}

	public void query() {

		/*
		 * task_name VARCHAR( 20 ), check_date VARCHAR( 40 ), up_num VARCHAR( 20
		 * ), down_num VARCHAR( 20 ), civil_check VARCHAR( 50 ), tacilitiy_check
		 * VARCHAR( 30 ), check_head VARCHAR( 20 ), check_member VARCHAR( 20 ),
		 * up_check_direciton INTEGER, mainte_org VARCHAR( 20 ), weather
		 * INTEGER, temperature INTEGER, humidity INTEGER, picture_beginnum
		 * INTEGER, patrol_car VARCHAR( 20 ), operating_car VARCHAR( 20 ),
		 * check_type VARCHAR( 20 ), update_id VARCHAR( 50 ), section_id
		 * VARCHAR( 20 ), line_id VARCHAR( 20 ), turnnel_id VARCHAR( 20 ),
		 * up_down VARCHAR( 20 ), current_lczh INTEGER, down_check_direciton
		 * 
		 * /
		 */
		String sql = "select _id,task_name,check_date,up_num,down_num,civil_check,tacilitiy_check,check_head,check_member,up_check_direciton,mainte_org,weather, temperature,"
				+ "humidity,picture_beginnum,patrol_car,operating_car,check_type,update_id,down_check_direciton  from TASK where task_name='"
				+ str_taskName + "'";
		System.out.println("��ѯ���� " + sql);
		Cursor c = db.query(sql);
		if (!(c.getCount() == 0)) {
			if (c.moveToFirst()) {
				listData = new ArrayList<Task>();
				do {
					task = new Task();
					task.set_id(c.getInt(0));
					task.setTask_name(c.getString(1));
					task.setCheck_date(c.getString(2));
					task.setUp_num(c.getString(3));
					task.setDown_num(c.getString(4));
					task.setCivil_check(c.getString(5));
					task.setTacilitiy_check(c.getString(6));
					task.setCheck_head(c.getString(7));
					task.setCheck_member(c.getString(8));
					task.setUp_check_direciton(c.getString(9));
					task.setMainte_org(c.getString(10));
					task.setWeather(c.getString(11));
					task.setTemperature((Integer) c.getInt(12));
					task.setHumidity((Integer) c.getInt(13));
					task.setPicture_beginnum(c.getInt(14));
					task.setPatrol_car(c.getString(15));
					task.setOperating_car(c.getString(16));
					task.setCheck_type(c.getString(17));
					task.setUpdate_id(c.getString(18));
					task.setDown_check_direciton(c.getString(19));
					if (c.getString(17).equals("�����ṹ")) {
						task.setTitile(c.getString(17) + "-" + c.getString(5));
					} else if (c.getString(17).equals("������ʩ")) {
						task.setTitile(c.getString(17) + "-" + c.getString(6));
					} else if (c.getString(17).equals("�����ṹ/������ʩ")) {
						task.setTitile(c.getString(17) + "-" + c.getString(5)
								+ "/" + c.getString(6));
					}
					System.out.println(c.getString(17));
					listData.add(task);

				} while (c.moveToNext());
				c.close();
			}

			adapter = new TaskInfoAdapter(listData, this, 1);
			listview.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			
			// }else {
			// List<Task> list2=new ArrayList<Task>();
			// Task t=new Task();
			// list2.add(t);
			// adapter=new TaskInfoAdapter(list2, Task_info.this, 1);
			// listview.setAdapter(adapter);
			// adapter.notifyDataSetChanged();
			//
			//
			// }
			//
		}

	}

}