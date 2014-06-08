package com.demo.jxdemo.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.demo.base.util.StringUtil;
import com.demo.jxdemo.constant.Constant;

/**
 * @description：升级帮助类
 * @author: yin.liu
 * @date : 2012-6-14
 */
public class UpgradeHelper {
	
	private static final String TAG = "UpgradeHelper";
	
	private Context context;
	
	private SQLiteDatabase db;
	
	public UpgradeHelper(Context context,SQLiteDatabase db){
		this.context = context;
		this.db = db;
	}

	/**
	 * 将指定的脚本文件执行
	 * @param fileName
	 * @author ：yin.liu
	 * @date : 2012-6-14
	 */
	public void initDataFromAssets(String fileName) {
		InputStreamReader inputReader = null;
		BufferedReader bufReader = null;
		try {
			inputReader = new InputStreamReader(
					context.getResources().getAssets().open(fileName),"GBK");
			bufReader = new BufferedReader(inputReader);
			String line = "";
			StringBuffer sql = new StringBuffer();
			sql.append("");
			while ((line = bufReader.readLine()) != null){
				if(line.startsWith("insert")){
					if(!"".equals(sql.toString().trim())){//当前一次sql拼接有值时，执行
						db.execSQL(sql.toString());
					}
					sql.setLength(0);
					sql.append(line);
				}else if(!"".equals(line)){
					sql.append(line);
				}
			}
			
			if(!"".equals(sql.toString().trim())){//当最后一次sql拼接有值时，执行
				db.execSQL(sql.toString());
			}
		  Log.i(TAG, "初始化执行"+fileName+"脚本完成");
		} catch (Exception e) {
			Log.i(TAG, "初始化执行"+fileName+"脚本异常"+e.getMessage());
		}finally{
			try{
				if(bufReader!=null){
					bufReader.close();
				}
				if(inputReader!=null){
					inputReader.close();
				}
			}catch(IOException e){
				Log.e(TAG, "关闭连接异常"+e.getMessage());
			}
		}
		
	}
	
	/**
	 * 根据文件的绝对路径，执行脚本
	 * @param filePath 文件的绝对路径
	 * @author ：yin.liu
	 * @date : 2012-9-20
	 */
	public void executeSqlFileByFilePath(String filePath){
		FileReader fr = null;
		BufferedReader br = null;
		try{
			fr = new FileReader(filePath);//创建FileReader对象，用来读取字符流
			br = new BufferedReader(fr);    //缓冲指定文件的输入
			while (br.ready()) {
				String line = br.readLine();//读取一行
				if(!StringUtil.isBlank(line)){
					db.execSQL(line);
				}
			}
			Log.i(TAG, "执行"+filePath+"脚本完成");
		}catch(Exception e){
			Log.e(TAG, "executeSqlFileByFilePath异常"+e.getMessage());
		}finally{
			try {
				br.close();
				fr.close();
			} catch (IOException e) {
				Log.e(TAG, "executeSqlFileByFilePath关闭连接异常"+e.getMessage());
			}
		}
	}
	
	/**
	 * 是否是第一次安装系统
	 * @return
	 * @author ：yin.liu
	 * @date : 2012-6-14
	 */
	public boolean isFirstInstall(){
		Cursor cursor = db.rawQuery("select DATA_CODE from TD_PH_DATA_SYN_TIME where LAST_SYN_TIME = '"+Constant.SYNTIME+"' and DATA_CODE = 'BASE_DATA'  ", null);
		if(cursor.getCount()>0){
			return false;
		}
		cursor.close();
		return true;
	}
}

