package android.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;


public class Contact {

	private Context mContext;
	public Contact(Context context){
		this.mContext=context;
	}
	
	/**
	 * 获取联系人号码 */
	public  void getContacts(){
		Cursor  c=mContext.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		while(c.moveToNext()){
			//获取联系人ID
			String contactId=c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
			//获取联系人姓名
			String contactName=c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			// 获取手机通讯录信息  
	        ContentResolver resolver = mContext.getContentResolver();  
	        Cursor c1=resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID
	        		+"=?", new String[]{contactId}, null);
		 while(c1.moveToNext()){
//			 Log.i("test",c1.getString(c1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
		 }
		}
	}
}
