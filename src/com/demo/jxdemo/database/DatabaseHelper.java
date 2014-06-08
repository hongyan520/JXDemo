package com.demo.jxdemo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.demo.base.util.StringUtil;
import com.demo.jxdemo.constant.Constant;

/**
 * <p>
 * Title:数据库建构及初始化类 更改数据库需注意步骤(采取不删除表更新)： 1，将数据库版本号加1 2，将数据库表结构变化除了在createTable创建表中更新外，还要在方法updateTableChange里进行更改 3，将更新后需要进行的逻辑处理写在upgradeConfig
 * 
 * @author yin.liu
 * @version 1.0
 * @date 2012-8-18
 */
public class DatabaseHelper extends SQLiteOpenHelper
{

	private static final String DATABASE_NAME = "BASEDB.db"; // 数据库名称

	public static final int DATABASE_VERSION = 14; // 数据库版本(改变版本号进行数据库更新需注意类上的注意步骤)

	public static final String DATABASE_VER = "DATABASEVERSION";

	private static final String TAG = "DatabaseHelper";

	private Context context;

	public DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	/**
	 * 数据库表创建
	 */
	public void onCreate(SQLiteDatabase db)
	{

		createTable(db);

		// 第一次安装系统根据脚本初始化数据
		firstInitDataFromAssets(db);

		// 初始化数据加载
		iniConfigData(db);

		// // 2,改变数据库版本号后，需要处理的逻辑执行（包括对数据的处理等）
		// /upgradeConfig(db);
	}

	/**
	 * 创建表
	 * 
	 * @param db
	 * @author ：yin.liu
	 * @date : 2012-8-18
	 */
	public void createTable(SQLiteDatabase db)
	{

		// 数据同步时间表
		exeSQL(db, "CREATE TABLE IF NOT EXISTS TD_PH_DATA_SYN_TIME (DATA_CODE text,LAST_SYN_TIME text)");
	}

	/**
	 * 第一次安装系统根据脚本初始化数据
	 * 
	 * @param db
	 * @author ：yin.liu
	 * @date : 2012-6-14
	 */
	private void firstInitDataFromAssets(SQLiteDatabase db)
	{
		UpgradeHelper upHelper = new UpgradeHelper(context, db);
		if (upHelper.isFirstInstall())
		{ // 第一次安装系统
			// upHelper.initDataFromAssets("TD_PH_MENU3D.sql");// 初始化3D菜单脚本
		}

	}

	/**
	 * 初初化系统需配置的数据库
	 * 
	 * @param db
	 */
	public void iniConfigData(SQLiteDatabase db)
	{
		db.execSQL("delete from TD_PH_DATA_SYN_TIME ");
		db.execSQL("insert into TD_PH_DATA_SYN_TIME(DATA_CODE,LAST_SYN_TIME)values(?,?)", new Object[] { "BASE_DATA", Constant.SYNTIME });
	}

	/**
	 * 执行SQL
	 * 
	 * @param db
	 * @param sql
	 */
	public void exeSQL(SQLiteDatabase db, String sql)
	{
		try
		{
			Log.i(TAG, sql);
			db.execSQL(sql);
		}
		catch (Exception e)
		{
			Log.e(TAG, StringUtil.deNull(e == null ? "" : e.getMessage()));
		}
	}

	/**
	 * 数据库表更新
	 */
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.d("", "DB update-------->" + oldVersion + "  new =" + newVersion);
		try
		{

			switch (newVersion)
			{
				default:
					break;

			}
		}
		catch (Exception e)
		{
			Log.e(TAG + ":onUpgrade", StringUtil.deNull(e == null ? "" : e.getMessage()));
		}

	}

}
