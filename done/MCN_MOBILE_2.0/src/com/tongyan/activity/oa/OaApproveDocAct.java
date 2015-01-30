package com.tongyan.activity.oa;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.xmlpull.v1.XmlPullParserException;

import com.tongyan.activity.AbstructCommonActivity;
import com.tongyan.activity.MyApplication;
import com.tongyan.activity.MainAct;
import com.tongyan.activity.R;
import com.tongyan.common.data.Str2Json;
import com.tongyan.common.entities._User;
import com.tongyan.utils.Constansts;
import com.tongyan.utils.MDialog;
import com.tongyan.utils.WebServiceUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * 
 * @ClassName P13_ApproveDocAct 
 * @author wanghb
 * @date 2013-07-16 pm 02:32:03
 * @date2 2014-09-19 pm 15:43
 * @desc 移动OA-发文/收文审批(收发文审批重要类)
 *  获取审批需要的参数
 *  I.发文
 *    1.Action:distribute（要求分发，此时data为可以分发的人员）
 *    2.Action:noAttachment（没有上传套红附件，请在系统上传文件）
 *    3.Action:suggestion（请审批意见）
 *  II.收文
 *    1.需要处理(是否结束-结束：显示结束按钮，不结束：则不显示)
 *    2.不需要处理-直接end
 *    3.选择下一步审批人
 */
public class OaApproveDocAct extends AbstructCommonActivity {
	
	private static final String TAG = "OaApproveDocAct";
	
	private static final String RECEIVE_DOC_COMMAND_END = "end";
	private static final String RECEIVE_DOC_COMMAND_NEXT = "next";
	
	private Context mContext = this;
	private Button homeBtn;
	private Button commitBtn;
	private EditText editText;
	
	private _User localUser;
	private Dialog mDialog;
	
	private String rowId;
	private String flowId;
	private String mIntentType;
	private String mType = null;
	
	private HashMap<String, Object> mReadyBack = null;
	
	private Button mOverBtn = null;
	
	private String mSuggest = null;
	private String mAction$Command = null;//收发文提交时的流程控制字段，详细请查阅接口文档
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPage();
		setClickListener();
		businessM();
	}
	
	private void initPage() {
		setContentView(R.layout.oa_document_approve);
		homeBtn = (Button)findViewById(R.id.p13_document_approve_home_btn);
		commitBtn = (Button)findViewById(R.id.p13_doc_approve_btn_id);
		editText = (EditText)findViewById(R.id.p13_document_approve_edittext);
		mOverBtn = (Button)findViewById(R.id.oa_document_approve_finish);
	}
	
	private void setClickListener() {
		homeBtn.setOnClickListener(homeBtnListener);
		commitBtn.setOnClickListener(commitBtnListener);
		mOverBtn.setOnClickListener(mOverBtnListener);
	}
	
	private void businessM(){
		MyApplication myApp = ((MyApplication)getApplication());
		myApp.addActivity(this);
		localUser = myApp.localUser;
		if(getIntent() != null && getIntent().getExtras() != null) {
			rowId = (String) getIntent().getExtras().get("rowId");
			flowId = (String) getIntent().getExtras().get("flowId");
			mIntentType = (String) getIntent().getExtras().get("IntentType");
			if(Constansts.TYPE_OF_DOCUMENTSEND.equals(mIntentType)) {//发文
				mType = getResources().getString(R.string.send_words);
			} else {//收文
				mType = getResources().getString(R.string.receive_words);
			}
			readyData();
		}
	}
	
	/**
	 * 打开加载动画
	 */
	public void loadingDialog() {
		mDialog = new AlertDialog.Builder(this).create();
		mDialog.show();
    	//注意此处要放在show之后 否则会报异常
		mDialog.setContentView(R.layout.common_loading_process_dialog);
		mDialog.setCanceledOnTouchOutside(false);
	}
	
	public void readyData() {
		
		loadingDialog();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				StringBuilder builder = new StringBuilder();
				builder.append("{rowId:'");
				builder.append(rowId);
				builder.append("',flowId:'");
				builder.append(flowId);
				builder.append("'}");
				try {
					/*
					ReadyForApproveResponse{ReadyForApproveResult={"s":"ok","v":{"action":"distribute","data":[{"EmployeeList":[
					{"Id":"1601c467-3a06-421a-84c9-f04ab35783e7","Name":"超级管理员"},{"Id":"df442f37-a64d-4a64-9035-e1116a0aa8e7","Name":"管理员"}],
					"Children":[{"EmployeeList":[{"Id":"51fc007f-782c-4e23-998a-4b305b0b232a","Name":"郭索敏"},
					{"Id":"0012f9e2-ba66-438f-8edb-1fa9ec639ccf","Name":"胡玉明"},{"Id":"564dd3fc-ca9a-4c19-8c83-a699083171fd","Name":"刘云川"},{"Id":"f77b9983-90a4-40df-a896-bc6969b1b643","Name":"孙宏"},{"Id":"c8349801-8a49-4bd2-bc77-492c085a135b","Name":"徐义标"},{"Id":"03174a39-0324-45a1-a12e-9890a89cd794","Name":"张龙生"},{"Id":"1013cd98-3c8d-4ea6-99a2-962a81658724","Name":"曾云谋"}],"Children":[],"Name":"项目办领导","Id":"187182ab-e96a-42de-b9d3-0bab976cd33a"},{"EmployeeList":[{"Id":"8c7f054c-8a90-4354-acc8-998d9e4e7806","Name":"昌宁项目办"},{"Id":"ed9d1949-59d5-4498-b25d-f16d9611d44c","Name":"戴程林"},{"Id":"75f48cf2-d878-4890-afe0-dd83b480f34c","Name":"旷福天"},{"Id":"788092da-2d8a-4f35-843e-21f83d192688","Name":"魏明艳"},{"Id":"f1200551-bec8-4f55-afea-5540e2abb5aa","Name":"夏天"},{"Id":"25fcfbe7-4e59-4be1-8165-c17d82ef4f5d","Name":"杨志峰"}],"Children":[],"Name":"合约处","Id":"83e6d983-654c-47b8-9939-e001cd0d9c70"},{"EmployeeList":[{"Id":"7d7d3b91-1fd9-4736-a9f6-1332f2803c7b","Name":"高振飞"},{"Id":"71fed21d-d8f2-4d9d-9017-cfde207d6008","Name":"金和平"},{"Id":"092c18b9-9613-4040-9894-c47dd8007f81","Name":"赖伟东"},{"Id":"ab366f5b-ad71-47d6-92aa-1af46e42b8c7","Name":"幸福"},{"Id":"94514e72-76d9-4921-865f-db97a4120f01","Name":"熊伟峰"},{"Id":"f2f66b10-e114-4515-a5ea-d6e1ff9e419e","Name":"张辉木"},{"Id":"cdeedb44-2cfb-4ca8-b236-2ee1f1ace758","Name":"张莉莉"},{"Id":"adaf0b20-11db-48c9-a487-b38e5ecc6134","Name":"张源斌"}],"Children":[],"Name":"工程处","Id":"e8247a5b-7759-48d3-b23c-1f7b8490b625"},{"EmployeeList":[{"Id":"7cf271da-9e8a-481a-a576-7cdde1ab4e78","Name":"梁华"}],"Children":[],"Name":"劳安处","Id":"38926a6e-d0c5-4689-8fe1-9913fe049cd4"},{"EmployeeList":[{"Id":"57437518-b157-420a-89fe-7f8be8b4c164","Name":"刘琴"},{"Id":"d695a258-d51c-4ae4-b692-37e69246555f","Name":"万秋妹"},{"Id":"b71f8f17-9125-4a35-beb6-6116f714bd2e","Name":"于春杨"},{"Id":"4700b334-d0ee-47f7-8a7c-e172b45a66bc","Name":"余剑锋"}],"Children":[],"Name":"财审处","Id":"caadc35d-b0fb-4aa8-b56a-5b977dc6db24"},{"EmployeeList":[{"Id":"28ea1e90-698c-4c2a-ac8b-59969738b375","Name":"蔡小东"},{"Id":"7db02dc1-9252-42e5-b825-66b990d42a42","Name":"段宏涛"},{"Id":"86eee9ad-260c-489e-a22a-2c425dda22bc","Name":"冯小毛"},{"Id":"44a9756c-688c-47f7-a11e-9bdc7df3e3c1","Name":"余淑华"}],"Children":[],"Name":"总工办","Id":"743e7d33-68a7-4049-ab41-78e6c0d3a888"},{"EmployeeList":[{"Id":"5b55e204-5e2b-40b4-92a2-56a037c762d0","Name":"刘毓闽"},{"Id":"b556cbf5-7328-4aa0-8dd0-647503f79075","Name":"陶琳"},{"Id":"5c52eb4e-b06f-4cdc-a085-882330df73c9","Name":"吴燕华"},{"Id":"0c70308b-0368-4c53-99b2-a4c9d5ca09bf","Name":"肖连铭"},{"Id":"f9868ceb-a4a0-463d-9d84-855ca99235b0","Name":"徐志勇"},{"Id":"26126ce8-6354-47ae-880e-76d3ed0455c2","Name":"周建斌"},{"Id":"ad68d16c-1585-49ec-8abb-d7cadec8fe75","Name":"张思捷"}],"Children":[],"Name":"综合处","Id":"9922309c-1a66-4913-bddb-c22b2f12045f"},{"EmployeeList":[{"Id":"f1703417-7d72-4391-afdd-253caae7dbc6","Name":"郭延新"},{"Id":"1aee27aa-0073-4a30-b18a-8126d3b9f766","Name":"邱志清"},{"Id":"e3cac2c6-06dc-4011-aa63-732f10881afc","Name":"沈小敏"},{"Id":"17d2eab5-65ff-4896-bfba-091c3965c6e8","Name":"王光龙"}],"Children":[],"Name":"政监处","Id":"de0d934c-293e-46d2-9d83-1c91eea6f4ee"},{"EmployeeList":[{"Id":"ce0a4b71-1ae3-4e33-aabd-02bf81b39316","Name":"陈志华"},{"Id":"276adb9c-02ae-4e85-8d67-04a8b1712fdb","Name":"龚雨琦"},{"Id":"0c996450-fef9-4183-bf38-88e61bf05b97","Name":"刘永平"},{"Id":"9c10a4a0-8325-48e6-9728-bef8889c31c1","Name":"裴佳雯"},{"Id":"ca5c7138-416f-48fc-8dbe-a6b350eb7b72","Name":"潘龙"},{"Id":"17700e87-61b9-405b-b508-ed4b79221f88","Name":"苏清波"},{"Id":"e4ada61a-2c71-4ed1-8e84-09a976bf8b8a","Name":"汪军"},{"Id":"71e47644-501d-4268-b9d7-595f64930b5f","Name":"周小勇"},{"Id":"c912b3d9-c48c-4b9c-8443-747c87dc5040","Name":"宗璐"}],"Children":[],"Name":"中心试验室","Id":"48b4d728-c4b6-44e7-839d-14ac1a4b9d44"},{"EmployeeList":[{"Id":"0e3aae35-22e1-43cc-8223-1c43af1f67f6","Name":"曹家声"},{"Id":"aaf4c77a-6007-429e-b736-e8c9a031da06","Name":"范军"},{"Id":"51153814-ef16-4a88-8a2c-64198f02966f","Name":"黄橙"},{"Id":"a0717892-0423-4777-81f6-e415b3e04e39","Name":"夏俊伟"},{"Id":"c2a9ad49-c4d2-4187-bb2e-9305be43ad10","Name":"周道根"},{"Id":"0f8a0e7f-65dd-4d35-8cee-5023ec127c89","Name":"张华萍"},{"Id":"b0183a7d-06ba-4d3d-ba6a-6cf9573bb395","Name":"张金杰"},{"Id":"e6c51b5f-5875-4d7d-a648-6f36bfca4cb1","Name":"栾鹏"},{"Id":"e9da0027-0bb0-4cb1-a64b-194daa4c3c5b","Name":"周书志"},{"Id":"9b1af53f-9fe3-4fed-b89c-d56e2d3339e8","Name":"周叶强"}],"Children":[{"EmployeeList":[{"Id":"19e58c57-b3c0-498d-9749-cc6f49f7bd30","Name":"方水平"},{"Id":"1d7e901f-0ce0-4aba-b9cf-d3d0d17e0d0d","Name":"秦文奎"},{"Id":"fd213322-c1e1-4431-b224-ecabfe859eba","Name":"阮轶"}],"Children":[],"Name":"江西省交通设计研究院有限责任公司","Id":"71aecd72-2864-49bb-a315-16c1c8d2ac17"},{"EmployeeList":[{"Id":"cdae3f85-4c39-4ff7-b30f-fd27786b7f73","Name":"陈连生"},{"Id":"f1e80fc4-67c2-4885-b826-161580886727","Name":"陈青"},{"Id":"8aa034fe-f874-47a1-b8d6-60441a754b5d","Name":"邓桢颖"},{"Id":"ab0cebd8-964b-43a3-b2e7-0ddca0f8dc1b","Name":"JA1"},{"Id":"8287fac0-5ee7-4225-b823-19f52051ef46","Name":"JA2"},{"Id":"07b53623-7f2b-4043-84fc-1711ecc27f3a","Name":"JA3"},{"Id":"e4072005-9a1b-41e3-97dc-3e149feb362b","Name":"JA4"},{"Id":"e7dc8c64-2eb5-4fb7-b165-214f580a18e3","Name":"刘伟"},{"Id":"da0c2f30-e339-4bda-9f6b-5f25def3dfd9","Name":"刘晓霞"},{"Id":"9375d78b-d979-4562-b161-42e0af4cccd4","Name":"盛由辉"},{"Id":"df78dce4-b2ee-4aa8-9fe9-ca50d241c7ec","Name":"万春明"},{"Id":"0a3e24d1-06d3-4826-831b-4abce82b61d2","Name":"吴平华"},{"Id":"226e5e07-d5af-4e90-af6a-77b7d0ea69ce","Name":"魏文"},{"Id":"76b6ae76-8fb2-4ddc-92c0-6bd1adf1bd80","Name":"王锡金"},{"Id":"c320885b-61dd-4fb1-a383-2981dfdd9ae8","Name":"王正强"},{"Id":"654f68cd-24e7-4b51-aaa2-e59bc667e338","Name":"许荣发"},{"Id":"dc307418-96d4-429e-b6c8-e1196f542029","Name":"杨林兴"},{"Id":"4fb888cf-b8d3-4657-bec5-fa77efdc8868","Name":"杨小军"},{"Id":"b80cc473-8d85-4a14-b45a-ec6417d4a711","Name":"曾海星"},{"Id":"fecfde9e-79dd-4060-8daf-0e523de00de8","Name":"邹小玉"},{"Id":"5ec96afe-8d9a-415a-b6bd-f5f57948e573","Name":"郤正彬"}],"Children":[{"EmployeeList":[{"Id":"c770cb2b-5d1c-4e0b-acfd-9ad6b36fc7a3","Name":"刘江华"},{"Id":"ba3ca223-8a13-49d0-9fce-4af07384f5d0","Name":"刘世亮"},{"Id":"e9313717-a18e-4a0a-9c26-94f45d5127e9","Name":"彭兰芳"},{"Id":"4230fdfe-84d7-49e1-ae3b-a961c6a39b90","Name":"吴显惠"},{"Id":"c8440752-1de4-4276-a9f2-4de1d6b4cfcd","Name":"袁坚"},{"Id":"09202428-05ef-42d7-8df7-48d04fea1e15","Name":"杨莉丽"},{"Id":"aaf1c4a7-3918-4b16-9dcf-0b612fe18ee3","Name":"章建波"}],"Children":[],"Name":"江西宜春公路建设集团有限公司","Id":"6b85245c-7bc4-40ee-9129-20069f1aafee"},{"EmployeeList":[{"Id":"f6d096b1-be1e-477a-92b4-024d19cd8ffe","Name":"敖长华"},{"Id":"ec795c6d-74b2-4e8a-a4d1-5637849496c3","Name":"程炳来"},{"Id":"676e4e59-a934-4c8f-b697-4e579dcde282","Name":"陈卫平"},{"Id":"f2da622d-2f6a-4e20-9fc9-c53be0ab1b7f","Name":"何红桔"},{"Id":"85903a43-4477-4eec-88a9-9a63f9e010a5","Name":"胡珊珊"},{"Id":"34e4eba4-f75b-44bb-ac7b-310f38817d71","Name":"李慧"},{"Id":"17a0ee1d-6e29-424b-bddc-df2f137f8082","Name":"袁志刚"}],"Children":[],"Name":"江西省公路桥梁工程局","Id":"0f408889-7f91-4039-a805-2588bab907a9"},{"EmployeeList":[{"Id":"a28196c6-17de-45cf-bf1d-ab9c57006a89","Name":"陈林"},{"Id":"0a03c115-cd8e-455c-a36f-118234dfa80f","Name":"杜君"},{"Id":"f6d54759-ff83-4f5c-9c0c-10ce91bfd728","Name":"刘晓伟"},{"Id":"97988727-2541-46a5-8cf8-7825c2435bd2","Name":"彭志川"},{"Id":"1995192d-84fd-4ebb-9584-f8a294a68ed8","Name":"田红"},{"Id":"b1d1ecbe-a677-45bd-8404-a5f599e032c1","Name":"王文东"},{"Id":"31bddf2d-212a-4455-8804-99cbc3d5f958","Name":"颜晓彤"}],"Children":[],"Name":"中铁十三局集团有限公司","Id":"dafd103a-2f05-47c7-ad50-f4357d73a617"},{"EmployeeList":[{"Id":"4a4ae7f4-5962-48d1-8ccd-9b2b95908f47","Name":"戴聪"},{"Id":"6a9dd9eb-fbbf-40e8-b72f-f37fab275580","Name":"罗秋香"},{"Id":"fba138aa-51e1-4e3e-b389-b405fa22853b","Name":"余胜"},{"Id":"1583e0dc-37ae-4f8f-9e2e-21baf45aff55","Name":"叶伟坤"},{"Id":"41c41dbd-f5c1-4e02-ad10-0399df0f7ee9","Name":"张美玲"},{"Id":"01b35ff1-91fd-4b18-bd0f-f95b1e43be06","Name":"张宁春"},{"Id":"ebfb013b-e9f5-417a-ab9a-cb8e25365620","Name":"张卫东"}],"Children":[],"Name":"中国路桥集团西安实业发展有限公司","Id":"8382c470-5be0-4e56-9cce-4c46141c6405"}],"Name":"江西省嘉和工程咨询监理有限公司","Id":"d5416f1f-4f7d-40ea-b762-edb41236ac55"},{"EmployeeList":[{"Id":"f5a678d8-b1f4-4e52-b536-d40104588320","Name":"陈贵州"},{"Id":"6bf94349-3685-4156-98bd-68f54f983767","Name":"陈小军"},{"Id":"a6b2a0ed-1c33-4680-9f40-f90e56d390a0","Name":"冯幸福"},{"Id":"cfc40c0e-d925-457b-af32-9c0a6a3b54b3","Name":"黄瑞敏"},{"Id":"c26920e1-c6cf-422d-8c4e-87edfb9feb96","Name":"JA5"},{"Id":"b5f9663f-316b-4a06-bd65-2bc3ef5a1eaf","Name":"JA6"},{"Id":"1bb6789e-9dd7-485e-9b9e-c7b5deda86cb","Name":"JA7"},{"Id":"7be4e5f0-2140-40fb-aa46-9d2ecb32d6f7","Name":"李国珍"},{"Id":"8cd27c8b-1c38-468d-866a-79b04858662b","Name":"李美美"},{"Id":"836c5979-14e8-4696-9526-2b58834f21a5","Name":"雷  义"},{"Id":"a15a4a30-2ee8-44e2-97cd-f6911749abc4","Name":"孟青"},{"Id":"3fe22531-867f-4ce4-a64a-9ff0bb1030fa","Name":"彭中秋"},{"Id":"9f283b85-5118-46be-94c5-9e029c0447e3","Name":"宋平"},{"Id":"3cf9528f-8a87-4e47-a3a3-45b219724c1a","Name":"吴勇"},{"Id":"3d37f8a1-233e-439a-bb20-af89a8d658a0","Name":"许大胜"},{"Id":"ec8c3468-5089-4bf5-bd73-2950febba648","Name":"琚彭庆"},{"Id":"81bac192-d996-4c6a-9f88-444174d77541","Name":"晏文兰"},{"Id":"4d6d8b38-35f9-441f-ace5-15fee0492ccd","Name":"朱永龙"},{"Id":"a0aa184c-65be-4387-aea1-6343a744d4ab","Name":"朱永明"}],"Children":[{"EmployeeList":[{"Id":"752c21bc-e997-4554-9831-82837ab6a92f","Name":"蔡凌阳"},{"Id":"c9cf20bc-f2da-4d17-8783-a3a8909f1ef0","Name":"傅业芳"},{"Id":"6958ccaf-0da3-496f-913b-a270415bab4e","Name":"高军"},{"Id":"bed69faa-e521-42fd-80a2-af3cc6901f6a","Name":"顾世忠"},{"Id":"e650de37-d58d-4bec-be77-01256f0ece63","Name":"林玮"},{"Id":"08f4e035-756a-4efb-9e64-0ac3db556c66","Name":"张志京"},{"Id":"f30fc8a7-9954-4e4b-92d5-68050c58571f","Name":"张祝勇"}],"Children":[],"Name":"中铁十四局集团有限公司","Id":"19fac8ba-5323-4672-ba16-f2f51de17119"},{"EmployeeList":[{"Id":"ac2bab9e-6968-412d-bc9f-0c5993b43ec3","Name...*/
					
					//ReadyForApproveResponse{ReadyForApproveResult={"s":"ok","v":{"action":"noAttachment","data":"没有上传套红附件，请在系统上传文件"}}; }
					
					
					//ReadyForApproveResponse{ReadyForApproveResult={"s":"ok","v":{"action":"suggestion","data":{"stepName":"核稿","template":"同意,同意申报,同意计量,同意支付,",
					//"emplist":[{"id":"caadc35d-b0fb-4aa8-b56a-5b977dc6db24","name":"财审处","emps":[{"id":"4700b334-d0ee-47f7-8a7c-e172b45a66bc","name":"余剑锋"}]},{"id":"e8247a5b-7759-48d3-b23c-1f7b8490b625","name":"工程处","emps":[{"id":"94514e72-76d9-4921-865f-db97a4120f01","name":"熊伟峰"}]},{"id":"83e6d983-654c-47b8-9939-e001cd0d9c70","name":"合约处","emps":[{"id":"25fcfbe7-4e59-4be1-8165-c17d82ef4f5d","name":"杨志峰"}]},{"id":"187182ab-e96a-42de-b9d3-0bab976cd33a","name":"项目办领导","emps":[{"id":"51fc007f-782c-4e23-998a-4b305b0b232a","name":"郭索敏"},{"id":"0012f9e2-ba66-438f-8edb-1fa9ec639ccf","name":"胡玉明"},{"id":"f77b9983-90a4-40df-a896-bc6969b1b643","name":"孙宏"},{"id":"03174a39-0324-45a1-a12e-9890a89cd794","name":"张龙生"}]},{"id":"de0d934c-293e-46d2-9d83-1c91eea6f4ee","name":"政监处","emps":[{"id":"e3cac2c6-06dc-4011-aa63-732f10881afc","name":"沈小敏"}]},{"id":"48b4d728-c4b6-44e7-839d-14ac1a4b9d44","name":"中心试验室","emps":[{"id":"71e47644-501d-4268-b9d7-595f64930b5f","name":"周小勇"}]},{"id":"9922309c-1a66-4913-bddb-c22b2f12045f","name":"综合处","emps":[{"id":"5b55e204-5e2b-40b4-92a2-56a037c762d0","name":"刘毓闽"}]},{"id":"743e7d33-68a7-4049-ab41-78e6c0d3a888","name":"总工办","emps":[{"id":"86eee9ad-260c-489e-a22a-2c425dda22bc","name":"冯小毛"}]}],"laststepdate":null}}}; }
					
					
					/*
					 ReadyForApproveResponse{ReadyForApproveResult={"s":"ok","v":{"needDeal":true,"canEnd":false,"emplist":[]}}; }
					 */
					String str = WebServiceUtils.getRequestStr(localUser.getUsername(), localUser.getPassword(), null, null, mType, builder.toString(), Constansts.METHOD_OF_READYFORAPPROVE, mContext);
					/*
					File file = new File(FileUtils.getSDCardPath() + File.separator +"dddd.txt");
					 if (!file.exists()) {  
						 file.createNewFile();
					 }
					 FileWriter writer;
				        try {
				            writer = new FileWriter(file.getPath());
				            writer.write(str);
				            writer.flush();
				            writer.close();
				        } catch (IOException e) {
				            e.printStackTrace();
				        }*/
					mReadyBack = new Str2Json().getSendDocumentReady(str, mIntentType);
					if(mReadyBack != null) {
						sendMessage(Constansts.MES_TYPE_1);
					} else {
						sendMessage(Constansts.ERRER);
					}
					} catch (IOException e) {
						e.printStackTrace();
					} catch (XmlPullParserException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	
	
	
	
	/**
	 * 收发文审批提交
	 */
	public void commit(final String mSelectEmpId) {
		
		loadingDialog();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				StringBuilder builder = new StringBuilder();
				if(Constansts.TYPE_OF_DOCUMENTSEND.equals(mIntentType)) {//发文
					builder.append("{flowId:'");
					builder.append(flowId);
					builder.append("',rowId:'");
					builder.append(rowId);
					builder.append("',action:'");//
					builder.append(mAction$Command);//next转交下一步/end直接结束流程
					builder.append("',suggest:'");
					builder.append(mSuggest);//审批意见（没有步骤就没有）
					builder.append("',selectEmpId:'");
					builder.append(mSelectEmpId);
					builder.append("',dealDate:'");
					builder.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					builder.append("'}");
				} else {//收文
					builder.append("{flowId:'");
					builder.append(flowId);
					builder.append("',rowId:'");
					builder.append(rowId);
					builder.append("',command:'");//
					builder.append(mAction$Command);//next转交下一步/end直接结束流程
					builder.append("',suggest:'");
					builder.append(mSuggest);//审批意见（没有步骤就没有）
					builder.append("',selectEmpId:'");
					builder.append(mSelectEmpId);
					builder.append("'}");
				}
				
				String str;
				try {
					str = WebServiceUtils.getRequestStr(localUser.getUsername(), localUser.getPassword(), null, null, mType, builder.toString(), Constansts.METHOD_OF_APPROVE, mContext);
					Map<String,String> mR = new Str2Json().updateDocApprove(str);
					if(mR != null && "ok".equalsIgnoreCase(mR.get("s"))) {
						sendMessage(Constansts.SUCCESS);
					} else {
						sendMessage(Constansts.MES_TYPE_2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					sendMessage(Constansts.MES_TYPE_2);
				} 
			}
		}).start();
	}
	
	
	/**
	 * 收文人员选择勾选
	 */
	Map<String,String> mCacheRowIdMap = null;
	OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {
		@Override 
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			HashMap<String, String> mTag = (HashMap<String, String>)buttonView.getTag();
			
			if(mCacheRowIdMap == null) {
				mCacheRowIdMap = new HashMap<String,String>();
			} 
			
			if(mTag != null) {
				if(isChecked) {
					mCacheRowIdMap.put(mTag.get("rowId"), "1");
				} else {
					mCacheRowIdMap.remove(mTag.get("rowId"));
					//mCacheRowIdMap.put(mTag.get("rowId"), "0");
				}
			}
		}
	};
	
	/**
	 * 收文人员选择Dialog
	 */
	MDialog dialogCategory = null;
	@SuppressWarnings("unchecked")
	private void recevieLayout() {
		dialogCategory = new MDialog(OaApproveDocAct.this, R.style.dialog);
		dialogCategory.createDialog(R.layout.oa_document_approve_pop, 0.9, 0.4, getWindowManager());
		dialogCategory.setCanceledOnTouchOutside(false);
		LinearLayout linearLayout = (LinearLayout)dialogCategory.findViewById(R.id.p13_approve_sub_container);
		Button sureBtn = (Button)dialogCategory.findViewById(R.id.p13_approve_sub_sure_btn);
		Button cancelBtn = (Button)dialogCategory.findViewById(R.id.p13_approve_sub_cancel_btn);
		sureBtn.setOnClickListener(sureBtnListener);
		cancelBtn.setOnClickListener(cancelBtnListener);
		//
		ArrayList<HashMap<String, Object>> mDptList = (ArrayList<HashMap<String, Object>>)mReadyBack.get("emplist");
		
		if(mDptList != null && mDptList.size() > 0) {
			for(HashMap<String, Object> m : mDptList) {
				if(m != null) {
					TextView textViewTitle = new TextView(this);
					textViewTitle.setText((String)m.get("DptName"));
					textViewTitle.setTextColor(Color.BLACK);
					linearLayout.addView(textViewTitle);
					//Employee
					ArrayList<HashMap<String, String>> mEmpList = (ArrayList<HashMap<String, String>>)m.get("Employee");
					if(mEmpList != null && mEmpList.size() > 0) {
						for(HashMap<String, String> emp : mEmpList) {
							LinearLayout layout = createLayout(LinearLayout.HORIZONTAL);
							//职位
							CheckBox checkBox = new CheckBox(this);
							checkBox.setButtonDrawable(R.drawable.p13_approve_pop_checkbox_selector);
							checkBox.setTag(emp);
							checkBox.setOnCheckedChangeListener(checkedChangeListener);
							
							layout.addView(checkBox);
							
							TextView textViewDpt = new TextView(this);
							textViewDpt.setText(emp.get("empName"));
							textViewDpt.setTextColor(Color.BLACK);
							layout.addView(textViewDpt);
							textViewDpt.setPadding(4, 0, 0, 0);
							layout.setPadding(20, 0, 0, 0);
							linearLayout.addView(layout);
						}
					}
				}
			}
		}
	}
	
	// 生成布局
	private LinearLayout createLayout(int iOrientation) {
		LinearLayout lay = new LinearLayout(this);
		lay.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		lay.setOrientation(iOrientation);
		return lay;
	}
	
	/**
	 * 收文-审批时的直接结束按钮监听器
	 */
	OnClickListener mOverBtnListener = new OnClickListener() {
		public void onClick(View v) {
			commit("");
		}
	};
	
	/**
	 * 监听器-收文审批时的选人审批按钮
	 */
	OnClickListener sureBtnListener = new OnClickListener() {
		public void onClick(View v) {
			if(mCacheRowIdMap == null || countMapSize(mCacheRowIdMap)==0) {
				Toast.makeText(OaApproveDocAct.this, "请勾选下一步审批人", Toast.LENGTH_SHORT).show();
				return;
			}
			commit(iterateSelecter(mCacheRowIdMap));
		}
	};
	
	/**
	 * 收文-计算审批人个数
	 * @param map
	 * @return
	 */
	private int countMapSize(Map<String, String> map) {
		if(map != null && map.size() > 0) {
			int i = 0;
			for(Map.Entry<String, String> m : map.entrySet()) {
					i++;
			}
			return i;
		} else {
			return 0;
		}
	}
	/**
	 * 收文-迭代审批人
	 * @param map
	 * @return
	 */
	private String iterateSelecter(Map<String, String> map) {
		StringBuilder builder = new StringBuilder();
		if(map != null && map.size() > 0) {
			int i = 0;
			int size = map.size();
			for(Map.Entry<String, String> m :map.entrySet()) {
				builder.append(m.getKey());
				i ++;
				if(i != size) {
					builder.append(",");
				} 
			}
		}
		return builder.toString();
	}
	
	OnClickListener cancelBtnListener = new OnClickListener() {
		public void onClick(View v) {
			if(dialogCategory != null) {
				dialogCategory.cancel();
			}
		}
	};
	
	/**
	 * 发文-人员选择弹框，用到递归
	 */
	@SuppressWarnings("unchecked")
	private void sendLayout() {
		dialogCategory = new MDialog(OaApproveDocAct.this, R.style.dialog);
		dialogCategory.createDialog(R.layout.oa_document_approve_pop, 0.9, 0.4, getWindowManager());
		dialogCategory.setCanceledOnTouchOutside(false);
		LinearLayout linearLayout = (LinearLayout)dialogCategory.findViewById(R.id.p13_approve_sub_container);
		Button sureBtn = (Button)dialogCategory.findViewById(R.id.p13_approve_sub_sure_btn);
		Button cancelBtn = (Button)dialogCategory.findViewById(R.id.p13_approve_sub_cancel_btn);
		sureBtn.setOnClickListener(sureBtnListener);
		cancelBtn.setOnClickListener(cancelBtnListener);
		//
		ArrayList<HashMap<String, Object>> mDptList = (ArrayList<HashMap<String, Object>>)mReadyBack.get("data");
		/*if(mDptList != null && mDptList.size() > 0) {
			for(HashMap<String, Object> m : mDptList) {*/
				recursionLayout(linearLayout, mDptList, 1);
		/*	}
		}*/
	}
	/**
	 * 发文审批-意见
	 */
	private void sendSuggestionLayout() {
		dialogCategory = new MDialog(OaApproveDocAct.this, R.style.dialog);
		dialogCategory.createDialog(R.layout.oa_document_approve_pop, 0.9, 0.4, getWindowManager());
		dialogCategory.setCanceledOnTouchOutside(false);
		LinearLayout linearLayout = (LinearLayout)dialogCategory.findViewById(R.id.p13_approve_sub_container);
		Button sureBtn = (Button)dialogCategory.findViewById(R.id.p13_approve_sub_sure_btn);
		Button cancelBtn = (Button)dialogCategory.findViewById(R.id.p13_approve_sub_cancel_btn);
		sureBtn.setOnClickListener(sureBtnListener);
		cancelBtn.setOnClickListener(cancelBtnListener);
		
		ArrayList<HashMap<String, Object>> mDptList = (ArrayList<HashMap<String, Object>>)mReadyBack.get("emplist");
		if(mDptList != null && mDptList.size() > 0) {
			for(HashMap<String, Object> mDptMap : mDptList) {
				if(mDptMap != null) {
					TextView textViewTitle = new TextView(this);
					textViewTitle.setText((String)mDptMap.get("name"));
					textViewTitle.setTextColor(Color.BLACK);
					linearLayout.addView(textViewTitle);
					//Employee
					ArrayList<HashMap<String, String>> mEmpList = (ArrayList<HashMap<String, String>>)mDptMap.get("EmpsList");
					if(mEmpList != null && mEmpList.size() > 0) {
						for(HashMap<String, String> emp : mEmpList) {
							LinearLayout layout = createLayout(LinearLayout.HORIZONTAL);
							//职位
							CheckBox checkBox = new CheckBox(this);
							checkBox.setButtonDrawable(R.drawable.p13_approve_pop_checkbox_selector);
							checkBox.setTag(emp);
							checkBox.setOnCheckedChangeListener(checkedChangeListener);
							
							layout.addView(checkBox);
							
							TextView textViewDpt = new TextView(this);
							textViewDpt.setText(emp.get("name"));
							textViewDpt.setTextColor(Color.BLACK);
							layout.addView(textViewDpt);
							textViewDpt.setPadding(4, 0, 0, 0);
							layout.setPadding(20, 0, 0, 0);
							linearLayout.addView(layout);
						}
					}
				}
			}
		}
		
	}
	
	
	
	
	/**
	 * 发文-人员选择弹框-迭代
	 */
	private void recursionLayout(final LinearLayout linearLayout, ArrayList<HashMap<String, Object>> mDptList, final int maginLef) {
			reloadRecursionLayout(linearLayout, mDptList, maginLef);
	}
	
	/**
	 * 缓存子集中父级的勾选
	 */
	HashMap<String, Boolean> mCacheSelectMap = new HashMap<String, Boolean>();
	
	/**
	 * 添加子集中父级的勾选
	 * @param mDptList
	 * @param isSelect
	 */
	public void addSubItemMap(ArrayList<HashMap<String, Object>> mDptList, boolean isSelect) {
		if(mDptList != null && mDptList.size() > 0) {
			for(HashMap<String, Object> m : mDptList) {
				if(m != null) {
					mCacheSelectMap.put((String)m.get("Id"), isSelect);
					addSubItemMap((ArrayList<HashMap<String, Object>>)m.get("Children"), isSelect);
				}
			}
		}
	}
	
	/**
	 * 递归 选择人员界面
	 * @param linearLayout
	 * @param mDptList
	 * @param maginLeft  距离左边的px的基数
	 */
	private void reloadRecursionLayout(final LinearLayout linearLayout, final ArrayList<HashMap<String, Object>> mDptList, final int maginLeft) {
		if(mDptList != null && mDptList.size() > 0) {
			for(final HashMap<String, Object> m : mDptList) {
				if(m != null) {
					boolean isSelected = mCacheSelectMap.get((String)m.get("Id")) == null ? false : mCacheSelectMap.get((String)m.get("Id")) ;
					final LinearLayout mParentListLayout = createLayout(LinearLayout.VERTICAL);
					final ArrayList<HashMap<String, String>> mEmployeeList = (ArrayList<HashMap<String, String>>)m.get("EmployeeList");
					final ArrayList<HashMap<String, Object>> mChildrenList = (ArrayList<HashMap<String, Object>>)m.get("Children");
					LinearLayout mParentLayout = createLayout(LinearLayout.HORIZONTAL);
					CheckBox checkBox = new CheckBox(this);
					checkBox.setButtonDrawable(R.drawable.p13_approve_pop_checkbox_selector);
					checkBox.setTag(m);
					checkBox.setChecked(isSelected);
					checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							if(isChecked) {
								linearLayout.removeAllViews();
								mCacheSelectMap.put((String)m.get("Id"), true);
								addSubItemMap(mChildrenList, true);
								reloadRecursionLayout(linearLayout, (ArrayList<HashMap<String, Object>>)mReadyBack.get("data"), 1);
							} else {
								linearLayout.removeAllViews();
								mCacheSelectMap.put((String)m.get("Id"), false);
								addSubItemMap(mChildrenList, false);
								reloadRecursionLayout(linearLayout, (ArrayList<HashMap<String, Object>>)mReadyBack.get("data"), 1);
							}
						}
					});
					
					mParentLayout.addView(checkBox);
					
					TextView textViewDpt = new TextView(this);
					textViewDpt.setText((String)m.get("Name"));
					textViewDpt.setTextColor(Color.BLACK);
					mParentLayout.addView(textViewDpt);
					textViewDpt.setPadding(10, 0, 0, 0);
					mParentLayout.setPadding(25 * maginLeft, 0, 0, 0);
					linearLayout.addView(mParentLayout);
					
					if(mEmployeeList != null && mEmployeeList.size() > 0) {
						for(HashMap<String, String> mm : mEmployeeList) {
							if(mm != null) {
								LinearLayout mEmpLayout = createLayout(LinearLayout.HORIZONTAL);
								CheckBox mmcheckBox = new CheckBox(this);
								mmcheckBox.setButtonDrawable(R.drawable.p13_approve_pop_checkbox_selector);
								mmcheckBox.setTag(mm);
								mmcheckBox.setOnCheckedChangeListener(mcheckedChangeListener);
								mmcheckBox.setChecked(isSelected);
								mEmpLayout.addView(mmcheckBox);
								
								TextView mmtextViewDpt = new TextView(this);
								mmtextViewDpt.setText((String)mm.get("Name"));
								mmtextViewDpt.setTextColor(Color.BLACK);
								mEmpLayout.addView(mmtextViewDpt);
								mmtextViewDpt.setPadding(10, 0, 0, 0);
								mEmpLayout.setPadding(25 * (maginLeft + 1), 0, 0, 0);
								mParentListLayout.addView(mEmpLayout);
							}
						}
					}
					linearLayout.addView(mParentListLayout);
					
					if(mChildrenList != null && mChildrenList.size() > 0) {
						recursionLayout(linearLayout, mChildrenList, maginLeft + 1);
					}
				}
			}
		}
	}
	
	
	/**
	 * 发文-选人
	 */
	OnCheckedChangeListener mcheckedChangeListener = new OnCheckedChangeListener() {
		@Override 
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			HashMap<String, String> mTag = (HashMap<String, String>)buttonView.getTag();
			
			if(mCacheRowIdMap == null) {
				mCacheRowIdMap = new HashMap<String,String>();
			} 
			
			if(mTag != null) {
				if(isChecked) {
					mCacheRowIdMap.put(mTag.get("rowId"), "1");
				} else {
					mCacheRowIdMap.remove(mTag.get("rowId"));
					//mCacheRowIdMap.put(mTag.get("rowId"), "0");
				}
			}
		}
	};
	
	
	/**
	 * 发文/收文-审批按钮监听
	 */
	OnClickListener commitBtnListener = new OnClickListener() {
		public void onClick(View v) {
			//dialog();
			mSuggest = editText.getText().toString();
			if("".equals(mSuggest.trim())) {
				Toast.makeText(OaApproveDocAct.this, "请填写审批意见", Toast.LENGTH_SHORT).show();
				return;
			}
			if(Constansts.TYPE_OF_DOCUMENTSEND.equals(mIntentType)) {//发文
				if("noAttachment".equals(mReadyBack.get("action"))) {
					Toast.makeText(mContext, "需要上传套红附件，请在系统上传文件", Toast.LENGTH_SHORT).show();
					return;
				} else if("distribute".equals(mReadyBack.get("action"))) {//需要分发
					sendLayout();
				} else {//审批意见
					sendSuggestionLayout();
				}
			} else {//收文
				if (null != mReadyBack.get("emplist")) {// 需要选人
					recevieLayout();
				} else {
					commit("");
				}
			}
		}
	};
	
	
	OnClickListener homeBtnListener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(OaApproveDocAct.this,MainAct.class);
			startActivity(intent);
		}
	};
	
	/**
	 * 处理收发文审批处理数据
	 * TODO
	 */
	public void handlerReady() {
		if(Constansts.TYPE_OF_DOCUMENTSEND.equals(mIntentType)) {//发文
			mAction$Command = (String)mReadyBack.get("action");
			if("noAttachment".equals(mAction$Command)) {
				Toast.makeText(this, "需要上传套红附件，请在系统上传文件", Toast.LENGTH_SHORT).show();
				finish();
				return;
			} else if("distribute".equals(mAction$Command)) {//需要分发
				//sendLayout();
			} else {//审批意见
				
			}
		} else {//收文
			if("true".equals(mReadyBack.get("needDeal"))) {//需要处理
				if("true".equals(mReadyBack.get("canEnd"))) {//能结束-则有结束按钮
					mOverBtn.setVisibility(View.VISIBLE);
				} 
				mAction$Command = RECEIVE_DOC_COMMAND_NEXT;
			} else {//不需要处理-则直接结束(不需要选人-直接结束)
				mAction$Command = RECEIVE_DOC_COMMAND_END;
			}
		}
	}
	
	
	@Override
	protected void onDestroy() {
		closeDialog();
		closeDialogCategory();
		super.onDestroy();
	}
	
	/**
	 * 关闭加载Dialog
	 */
	public void closeDialog() {
		if(mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
	}
	
	
	public void closeDialogCategory() {
		if(dialogCategory != null) {
			dialogCategory.cancel();
			dialogCategory = null;
		}
	}
	
	@Override
	protected void handleOtherMessage(int flag) {
		switch (flag) {
		case Constansts.SUCCESS:
			closeDialog();
			closeDialogCategory();
			Toast.makeText(this, "提交成功", Toast.LENGTH_SHORT).show();
			setResult(666);
			finish();
			break;
		case Constansts.ERRER:
			closeDialog();
			Toast.makeText(this, "操作失败，请重试", Toast.LENGTH_SHORT).show();
			finish();
			break;
		case Constansts.NET_ERROR:
			closeDialog();
			Toast.makeText(this, "网络异常", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.CONNECTION_TIMEOUT:
			closeDialog();
			Toast.makeText(this, "网络连接超时", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.MES_TYPE_1://获取准备数据成功
			closeDialog();
			handlerReady();
			break;
		case Constansts.MES_TYPE_2:
			closeDialog();
			closeDialogCategory();
			Toast.makeText(this, "提交失败", Toast.LENGTH_SHORT).show();
			break;
		case Constansts.MES_TYPE_3:
			closeDialog();
			Toast.makeText(this, "公文分发需要套红，请在电脑上操作", Toast.LENGTH_SHORT).show();
			break;
		default:
			closeDialog();
			break;
		}
	}
	
}
