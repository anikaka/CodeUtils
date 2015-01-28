package com.tongyan.yanan.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Environment;
import android.util.Log;

public class FileTools {

	public final static String PAHT_SD=Environment.getExternalStorageDirectory()+"/";
	
	/** 在SD卡中创建目录*/
	public static  File createDir(String dirName){
		File  file=new File(PAHT_SD+dirName);
		Log.i("test", PAHT_SD+dirName);
		file.mkdirs();
		 return file;
	}
	
	/**在SD卡中创建文件
	 * @throws IOException */
	public static File createSDFile(String fileName) throws IOException{
		File file =new File(PAHT_SD+fileName);
		file.createNewFile();
		return file;
	}
	
    /** 
     * 判断SD卡上的文件夹是否存在 
     */  
    public static boolean isFileExist(String fileName){  
        File file = new File(PAHT_SD +fileName);  
        return file.exists();  
    }  
    /** 
     * 将一个InputStream里面的数据写入到SD卡中 
     */  
    public static  File write2SDFromInput(String path, String fileName, InputStream input){  
        File file = null;  
        OutputStream output = null;  
        try{  
        	createSDFile(path);  
            file = createSDFile(path + fileName);  
            output = new FileOutputStream(file);  
            byte buffer[] = new byte[ 4*1024];  
            while((input.read(buffer))!=-1){  
                output.write(buffer);  
            }  
            output.flush();  
        }  
        catch(Exception e){  
            e.printStackTrace();  
        }  
        finally{  
            try{  
                output.close();  
            }catch(Exception e){  
                e.printStackTrace();    
            }  
        }  
          
        return file;  
    }  

}
