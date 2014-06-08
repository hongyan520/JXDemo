package com.demo.jxdemo.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.demo.base.util.StringUtil;

/**
 *<p>Title:数据操作封装类</p>
 *<p>Description: </p>
 *<p>Copyright:Copyright (c) 2012</p>
 *<p>Company:湖南科创</p>
 *@author xianlu.lu
 *@version 1.0
 *@date 2012-5-2
 */
public class DBHelper {

	private static final String TAG = "DBHelper";

	/**
	 * 查询单个字段
	 * 
	 * @param context
	 * @param Sql
	 * @return
	 */
	public static String getOneValue(Context context, String Sql) throws Exception {
		String _retString = "";
		DatabaseHelper dbHelper = null;
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try{
			dbHelper = new DatabaseHelper(context);
			db = dbHelper.getWritableDatabase();
			cursor = db.rawQuery(Sql, null);
			cursor.moveToFirst();
			if (cursor.getCount() > 0)
				_retString = cursor.getString(0);
		}catch(Exception e){
			Log.e(TAG, StringUtil.deNull(e==null?"":e.getMessage()));
			throw e;
		}finally{
			if(db!=null){
				db.close();
			}
			if(dbHelper!=null){
				dbHelper.close();	
			}
			if(cursor !=null && !cursor.isClosed()){
				cursor.close();
			}
		}
		return _retString;
	}

	/**
	 * 查询返回单行[查询的第一条]记录
	 * 
	 * @param context
	 * @param Sql
	 * @return
	 */
	public static Map<String, Object> getOneRecord(Context context, String Sql) throws Exception{
		Map<String, Object> data = new HashMap<String, Object>();
		DatabaseHelper dbHelper = null;
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try{
			dbHelper = new DatabaseHelper(context);
			db = dbHelper.getWritableDatabase();
			cursor = db.rawQuery(Sql, null);
			cursor.moveToFirst();
			if (cursor.getCount() > 0)
				for (int i = 0; i < cursor.getColumnCount(); i++) {
					data.put(cursor.getColumnName(i), cursor.getString(i));
				}
		}catch(Exception e){
			Log.e(TAG, StringUtil.deNull(e==null?"":e.getMessage()));
			throw e;
		}finally{
			if(db!=null){
				db.close();
			}
			if(dbHelper!=null){
				dbHelper.close();	
			}
			if(cursor !=null && !cursor.isClosed()){
				cursor.close();
			}
		}
		return data;
	}

	/**
	 * 返回查询多条[List集合Map<String, Object>]的记录
	 * 
	 * @param context
	 * @param Sql
	 * @return
	 */
	public static ArrayList<Map<String, Object>> getRecordList(Context context,
			String Sql) throws Exception{
		ArrayList<Map<String, Object>> tablelist = new ArrayList<Map<String, Object>>();
		DatabaseHelper dbHelper = null;
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try{
			dbHelper = new DatabaseHelper(context);
			db = dbHelper.getWritableDatabase();
			cursor = db.rawQuery(Sql, null);
			cursor.moveToFirst();

			for (int row = 0; row < cursor.getCount(); row++) {
				Map<String, Object> data = new HashMap<String, Object>();
				for (int i = 0; i < cursor.getColumnCount(); i++) {
					data.put(cursor.getColumnName(i), cursor.getString(i));
				}
				tablelist.add(data);
				cursor.moveToNext();
			}
		}catch(Exception e){
			Log.e(TAG, StringUtil.deNull(e==null?"":e.getMessage()));
			throw e;
		}finally{
			if(db!=null){
				db.close();
			}
			if(dbHelper!=null){
				dbHelper.close();	
			}
			if(cursor !=null && !cursor.isClosed()){
				cursor.close();
			}
		}
		return tablelist;
	}

	/**
	 * 返回查询多条[List集合<Map<String, String>>]的记录
	 * 
	 * @param context
	 * @param Sql
	 * @return
	 */
	public static ArrayList<Map<String, String>> getRecordListString(
			Context context, String Sql) throws Exception{
		ArrayList<Map<String, String>> tablelist = new ArrayList<Map<String, String>>();
		DatabaseHelper dbHelper = null;
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try{
			dbHelper = new DatabaseHelper(context);
			db = dbHelper.getWritableDatabase();
			cursor = db.rawQuery(Sql, null);
			cursor.moveToFirst();

			for (int row = 0; row < cursor.getCount(); row++) {
				Map<String, String> data = new HashMap<String, String>();
				for (int i = 0; i < cursor.getColumnCount(); i++) {
					data.put(cursor.getColumnName(i), cursor.getString(i));
				}
				tablelist.add(data);
				cursor.moveToNext();
			}

		}catch(Exception e){
			Log.e(TAG, StringUtil.deNull(e==null?"":e.getMessage()));
			throw e;
		}finally{
			if(db!=null){
				db.close();
			}
			if(dbHelper!=null){
				dbHelper.close();	
			}
			if(cursor !=null && !cursor.isClosed()){
				cursor.close();
			}
		}
		return tablelist;
	}
}
