package com.demo.jxdemo.ui.activity.login;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ui.listener.OnClickAvoidForceListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.base.services.http.HttpPostAsync;
import com.demo.base.support.BaseConstants;
import com.demo.base.util.JsonUtil;
import com.demo.base.util.StringUtil;
import com.demo.jxdemo.R;
import com.demo.jxdemo.application.SharedPreferencesConfig;
import com.demo.jxdemo.constant.CommandConstants;
import com.demo.jxdemo.constant.Constant;
import com.demo.jxdemo.ui.activity.BaseActivity;
import com.demo.jxdemo.ui.activity.main.MainActivity;
import com.demo.jxdemo.ui.activity.menu.AboutActivity;
import com.demo.jxdemo.ui.activity.menu.UserInfoActivity;
import com.demo.jxdemo.ui.customviews.CustomDialog;
import com.demo.jxdemo.ui.customviews.CustomProgressDialog;
import com.demo.jxdemo.utils.ToastManager;

public class LoginActivity extends BaseActivity
{
	private EditText numEditText;

	private EditText checkEditText;

	private Button checkButton;

	private Map<String, String> configMap;

	/** 验证码 */
	private String checkCode = "";

	private TimeCount time;

	private CustomDialog cusDialog;

	private CustomProgressDialog customProgressDialog;

	private Map<String, OnClickListener> map = new HashMap<String, View.OnClickListener>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

		configMap = SharedPreferencesConfig.config(this);
		time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
		findViews();
		initView();
		setViewClick();
	}

	private void findViews()
	{

		((TextView) findViewById(R.id.formTilte)).setText(getResources().getString(R.string.app_name));
		((TextView) findViewById(R.id.text_right)).setText(getResources().getString(R.string.about));
		((TextView) findViewById(R.id.text_right)).setVisibility(View.VISIBLE);

		numEditText = (EditText) findViewById(R.id.edit_login_phonenum);
		checkEditText = (EditText) findViewById(R.id.edit_login_pwd);
		checkButton = (Button) findViewById(R.id.btn_getcheck);
	}

	private void initView()
	{
		if (!StringUtil.isBlank(SharedPreferencesConfig.config(LoginActivity.this).get(Constant.USER_TEL)))
		{
			numEditText.setText(SharedPreferencesConfig.config(LoginActivity.this).get(Constant.USER_TEL));
		}

		map.put("第一个按钮", new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				ToastManager.getInstance(LoginActivity.this).showToast("第一个.....");
			}
		});
		map.put("第二个按钮", new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				ToastManager.getInstance(LoginActivity.this).showToast("第二个.....");
			}
		});
		cusDialog = new CustomDialog(LoginActivity.this, "描述1234566879756", map, "last", new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				ToastManager.getInstance(LoginActivity.this).showToast("最后一个........");
			}
		});
		customProgressDialog = new CustomProgressDialog(LoginActivity.this);
		customProgressDialog.createDialog(LoginActivity.this);
		customProgressDialog.gettView().setText("转转转");
	}

	private void setViewClick()
	{
		((RelativeLayout) findViewById(R.id.rlayout_into)).setOnClickListener(onClickAvoidForceListener);
		((LinearLayout) findViewById(R.id.layout_remark)).setOnClickListener(onClickAvoidForceListener);
		checkButton.setOnClickListener(onClickAvoidForceListener);
	}

	private OnClickAvoidForceListener onClickAvoidForceListener = new OnClickAvoidForceListener()
	{

		@Override
		public void onClickAvoidForce(View v)
		{
			switch (v.getId())
			{
				case R.id.rlayout_into:

					String test = "[{\"AcceptMaterial\":1,\"AcceptTraining\":1,\"ID\":1,\"Icon\":\"/uploads/dfceee24cb4f463f/IconSpoken.png\",\"Title\":\"英语口语\"},{\"AcceptMaterial\":1,\"AcceptTraining\":1,\"ID\":2,\"Icon\":\"/uploads/e0ed88dce6884ed8/IconWriting.png\",\"Title\":\"英语写作\"}]";
					SharedPreferencesConfig.saveConfig(LoginActivity.this, Constant.USER_COURSEARRAY, StringUtil.Object2String(test));

					StringBuffer testbuffer = new StringBuffer();
					testbuffer.append(" ");
					testbuffer.append("[{\"Abstract\":\"近日，网友评出“年度最伤人聊天词汇”，“呵呵”当选，没有之一。“笑声里含义深刻，不显山 ");
					testbuffer.append("不露水，让你琢磨不透。”网友形容这个词只有一个用处：以最大效果激怒对方，践踏对方全部热情。其实，英语中常说的“interesting”也有异曲同工之 ");
					testbuffer
							.append("妙。\",\"AttachementArray\":[],\"Banner\":\"\",\"CommentCount\":0,\"Content\":\"当外国人听你讲了一堆他觉得很扯的东西，一般都会说“interesting”。如果你真觉得他表示感兴趣 ");
					testbuffer.append("的话，那就错了，他们只是想表达“呵呵”而已。周末，分享一组超搞的创意，看外国怎么表达“呵呵”这个高贵冷艳的词。\r\n\r\n《国外怎么表达“呵呵”这个高贵冷艳的词 ");
					testbuffer.append("》\r\n\r\n　　近日，网友评出“年度最伤人聊天词汇”，“呵呵”当选，没有之一。“笑声里含义深刻，不显山不露水，让你琢磨不透。”网友形容这个词只有一个用处：以最 ");
					testbuffer.append("大效果激怒对方，践踏对方全部热情。其实，英语中常说的“interesting”也有异曲同工之妙。\r\n\r\n　　英语单词interesting，汉语通常译为“有趣的”。听到美国人 ");
					testbuffer
							.append("说“It’s interesting”（很有趣）或“That’s an interesting idea”（有趣的想法），通常，中国人都理解为赞同或夸奖。\r\n\r\n　　但是，实际上，interesting，并不 ");
					testbuffer.append("是一个褒义词。当然，也不是一个贬义词。“具体情况具体分析”。好比在某场合，有个中国人说了句什么，根据当时的情景、语境或上下文，闻者应该能听得出是真心赞同，还 ");
					testbuffer
							.append("是客套敷衍，甚或为讽刺嘲笑。\r\n\r\n　　interesting，根据权威的剑桥英语词典，意思是：引人注意（holding one’s attention）。比如，一个 同学说，I just read a  ");
					testbuffer
							.append("book. It’s very interesting. （我刚看了一本书，很有意思。）引起闻者的注意，接下来，会有补充评论，如：It’s really funny. （很搞笑的。）这是褒义的用法。\r\n ");
					testbuffer
							.append("\r\n　　也有贬义的用法。如： You screaming at me when it wasn't my fault was really interesting! （不是我的过错，你却对我吼叫，真有意思！）\r\n\r\n　　如何辨 ");
					testbuffer
							.append("别是否认同、赞同甚或夸奖？关键全在于根据语境上下文（The context is everything）。\r\n\r\n　　如果在说了“It’s interesting”或者“That’s an interesting idea ");
					testbuffer.append("”之后，显出“愿听详情”的样子，或者顺着话题分享同感，那表示闻者确实是真有兴趣，很是赞同。\r\n\r\n　　但如果下文是转换话题，迂回敲击，那便表明，闻者并不赞同 ");
					testbuffer.append("。“That’s an interesting idea”相当于汉语的“那可是个奇葩想法”。大家都知道，这里，奇葩的含义是怪异、离奇、不可思议。整个一句话的实际含义，并无表示赞同之意 ");
					testbuffer
							.append("。\r\n\r\n　　多数情况是，英美人对你所说的东西没有兴趣，但又不愿直白地让你扫兴，于是，便会说“It’s interesting.”或者“That’s an interesting idea.”显然， ");
					testbuffer.append("这并不表示赞同、欣赏或夸奖，只是礼貌性的回应而已。\r\n\r\n　　“呵呵”，是笑声的拟声词，原本表示笑声。在网络聊天时，若对对方表示不满或者不知道该说什么，有网 ");
					testbuffer.append("民会用“呵呵”应急敷衍。所以，在客套敷衍时，英美人用“interesting”词句，相当于中国网民用“呵呵”。\r\n\r\n　　当外国人听你讲了一堆他觉得很扯的东西，一般都会 ");
					testbuffer.append("说“interesting”。如果你真觉得他表示感兴趣的话，那就错了，他们只是想表达“呵呵”而已。周末，分享一组超搞的创意，看外国怎么表达“呵呵”这个高贵冷艳的 ");
					testbuffer
							.append("词。\",\"Fav\":0,\"ID\":2,\"Link\":\"\",\"New\":0,\"Title\":\"你会用英文表达''呵呵'' 吗？\",\"Type\":0},{\"Fav\":0,\"FeedbackAttachementArray\": ");
					testbuffer
							.append("[],\"FeedbackContent\":\"\",\"ID\":1,\"LastChatMap\":{},\"New\":0,\"RequirementAttachementArray\":[],\"RequirementDescription\":\"录制一个5到10分钟的语音，描述一下今年冬天 ");
					testbuffer.append("的第一场雪，可自行决定具体的场景。\",\"Title\":\"冬天的第一场雪\",\"Type\":1,\"UploadType\":0,\"UploadedContent\":\"\"}] ");
					SharedPreferencesConfig.saveConfig(LoginActivity.this, Constant.USER_TEST, testbuffer.toString());

					Intent intent = new Intent();
					intent.setClass(LoginActivity.this, MainActivity.class);
					startActivity(intent);
					LoginActivity.this.finish();
					// if (isPassCheck(1))
					// {
					// showProgress(2 * 60 * 1000);
					// login();
					// }
					break;
				case R.id.btn_getcheck:
					if (isPassCheck(0))
					{
						showProgress();
						getCheckCode();
						time.start();
					}
					break;
				case R.id.layout_remark:
					Intent inAbout = new Intent();
					inAbout.setClass(LoginActivity.this, AboutActivity.class);
					inAbout.putExtra("fromLogin", "fromLogin");
					startActivity(inAbout);
					// ToastManager.getInstance(LoginActivity.this).showToast("关于.......");

					// cusDialog.show();
					// customProgressDialog.show();

					break;
				default:
					break;
			}

		}
	};

	/**
	 * 验证条件是否符合
	 * 
	 * @param flag
	 *            0:验证码 1:登录
	 * @return
	 */
	private boolean isPassCheck(int flag)
	{
		String num = numEditText.getText().toString();
		String pwd = checkEditText.getText().toString();
		if (StringUtil.isBlank(num))
		{
			// numEditText.setError(getResources().getString(R.string.register_phonenum_hint));
			// numEditText.requestFocus();
			ToastManager.getInstance(LoginActivity.this).showToast(getResources().getString(R.string.register_phonenum_hint));
			return false;
		}
		else if (num.length() < 11 || !num.substring(0, 1).equals("1"))
		{
			// numEditText.setError(getResources().getString(R.string.register_phonenum_err));
			// numEditText.requestFocus();
			ToastManager.getInstance(LoginActivity.this).showToast(getResources().getString(R.string.register_phonenum_err));
			return false;
		}
		if (flag != 0)
		{
			if (StringUtil.isBlank(pwd) || pwd.length() < 4)
			{
				// checkEditText.setError("请输入" + getResources().getString(R.string.register_messcheck_hint));
				// checkEditText.requestFocus();
				ToastManager.getInstance(LoginActivity.this).showToast("请输入" + getResources().getString(R.string.register_messcheck_hint));
				return false;
			}
		}
		return true;
	}

	/**
	 * 获得验证码
	 */
	private void getCheckCode()
	{
		Map<String, Object> parasTemp = new HashMap<String, Object>();
		parasTemp.put("PhoneNumber", numEditText.getText().toString().trim());

		new HttpPostAsync(LoginActivity.this)
		{
			@Override
			public Object backResult(Object result)
			{// 请求回调
				System.out.println("SendAuthCode=" + result);
				if (result == null || "".equals(result.toString()))
				{
					dismissProgress();
					ToastManager.getInstance(LoginActivity.this).showToast("服务器异常，请联系管理员!");
				}
				else if (BaseConstants.HTTP_REQUEST_FAIL.equals(result.toString().trim()))
				{
					dismissProgress();
					ToastManager.getInstance(LoginActivity.this).showToast("连接不上服务器");
				}
				else
				{
					Map<String, Object> mapstr = JsonUtil.getMapString(result.toString());
					// Map<String, Object> headMap = JsonUtil.getMapString(mapstr.get("head").toString());
					// Map<String, Object> dataMap = JsonUtil.getMapString(mapstr.get("data").toString());
					boolean isSuccess = false;
					if (!mapstr.containsKey(CommandConstants.ERRCODE))
						isSuccess = true;
					String desc = (String) mapstr.get(CommandConstants.ERRCODE);
					if (!isSuccess && !StringUtil.isBlank(desc))
					{
						dismissProgress();
						ToastManager.getInstance(LoginActivity.this).showToast(desc);
					}
					else
					{
						checkCodeSuccessDeal(mapstr);// 成功后处理
					}
				}
				return "";
			}
		}.execute(BaseConstants.POST_KEYVALUE_DATA, CommandConstants.URL + CommandConstants.SENDAUTHCODE, parasTemp);

	}

	@SuppressWarnings("unchecked")
	private void checkCodeSuccessDeal(Map<String, Object> map)
	{
		new CheckCodeDealTask().execute(map);
	}

	class CheckCodeDealTask extends AsyncTask<Map<String, Object>, Integer, Void>
	{

		@Override
		protected void onPreExecute()
		{
			showProgress(1 * 60 * 1000);
		}

		@Override
		protected void onProgressUpdate(Integer... progress)
		{

		}

		@Override
		protected Void doInBackground(Map<String, Object>... map)
		{
			try
			{
				if (map != null)
				{
					// checkCode = "1234";// StringUtil.Object2String(map[0].get(""));
					ToastManager.getInstance(LoginActivity.this).showToast("请查看验证码!");
				}
				else
					ToastManager.getInstance(LoginActivity.this).showToast("获取验证码失败!");
			}
			catch (Exception e)
			{
				e.printStackTrace();
				ToastManager.getInstance(LoginActivity.this).showToast("获取验证码失败!");
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			dismissProgress();
		}

	}

	private void login()
	{
		Map<String, Object> parasTemp = new HashMap<String, Object>();
		parasTemp.put("PhoneNumber", numEditText.getText().toString().trim());
		parasTemp.put("AuthCode", checkEditText.getText().toString().trim());

		new HttpPostAsync(LoginActivity.this)
		{
			@Override
			public Object backResult(Object result)
			{// 请求回调
				System.out.println("SignIn=" + result);
				if (result == null || "".equals(result.toString()))
				{
					dismissProgress();
					ToastManager.getInstance(LoginActivity.this).showToast("服务器异常，请联系管理员!");
				}
				else if (BaseConstants.HTTP_REQUEST_FAIL.equals(result.toString().trim()))
				{
					dismissProgress();
					ToastManager.getInstance(LoginActivity.this).showToast("连接不上服务器");
				}
				else
				{
					Map<String, Object> mapstr = JsonUtil.getMapString(result.toString());
					// Map<String, Object> headMap = JsonUtil.getMapString(mapstr.get("head").toString());
					// Map<String, Object> dataMap = JsonUtil.getMapString(mapstr.get("data").toString());
					boolean isSuccess = false;
					if (!mapstr.containsKey(CommandConstants.ERRCODE))
						isSuccess = true;
					String desc = (String) mapstr.get(CommandConstants.ERRCODE);
					if (!isSuccess && !StringUtil.isBlank(desc))
					{
						dismissProgress();
						ToastManager.getInstance(LoginActivity.this).showToast(desc);
					}
					else
					{
						loginSuccessDeal(mapstr);// 成功后处理
					}
				}
				return "";
			}
		}.execute(BaseConstants.POST_KEYVALUE_DATA, CommandConstants.URL + CommandConstants.LOGIN, parasTemp);

	}

	@SuppressWarnings("unchecked")
	private void loginSuccessDeal(Map<String, Object> map)
	{
		new LoginDealTask().execute(map);
	}

	class LoginDealTask extends AsyncTask<Map<String, Object>, Integer, Void>
	{

		@Override
		protected void onPreExecute()
		{
			showProgress(2 * 60 * 1000);
		}

		@Override
		protected void onProgressUpdate(Integer... progress)
		{

		}

		@Override
		protected Void doInBackground(Map<String, Object>... map)
		{
			try
			{
				// 2，保存登录用户的基本信息到手机文件存储库中
				if (map != null)
				{
					SharedPreferencesConfig
							.saveConfig(LoginActivity.this, Constant.USER_ID, StringUtil.Object2String(map[0].get("UserID")));
					SharedPreferencesConfig.saveConfig(LoginActivity.this, Constant.USER_TOKEN,
							StringUtil.Object2String(map[0].get("UserToken")));
					SharedPreferencesConfig.saveConfig(LoginActivity.this, Constant.USER_TEL, numEditText.getText().toString());
					SharedPreferencesConfig.saveConfig(LoginActivity.this, Constant.USER_AVATAR,
							StringUtil.Object2String(map[0].get("Avatar")));
					SharedPreferencesConfig.saveConfig(LoginActivity.this, Constant.USER_INTRODUCTION,
							StringUtil.Object2String(map[0].get("Introduction")));
					SharedPreferencesConfig.saveConfig(LoginActivity.this, Constant.USER_JOB, StringUtil.Object2String(map[0].get("Job")));
					SharedPreferencesConfig.saveConfig(LoginActivity.this, Constant.USER_LOCATION,
							StringUtil.Object2String(map[0].get("Location")));
					SharedPreferencesConfig.saveConfig(LoginActivity.this, Constant.USER_GENDER,
							StringUtil.Object2String(map[0].get("Sex")));
					SharedPreferencesConfig.saveConfig(LoginActivity.this, Constant.USER_NAME,
							StringUtil.Object2String(map[0].get("Title")));
					SharedPreferencesConfig.saveConfig(LoginActivity.this, Constant.USER_TYPE,
							StringUtil.Object2String(map[0].get("UserType")));
					SharedPreferencesConfig.saveConfig(LoginActivity.this, Constant.USER_DIGEST,
							StringUtil.Object2String(map[0].get("Digest")));
					// TODO 课程待测
					SharedPreferencesConfig.saveConfig(LoginActivity.this, Constant.USER_COURSEARRAY,
							StringUtil.Object2String(map[0].get("CourseArray").toString()));

					dismissProgress();
					Intent intent = new Intent();
					if (StringUtil.isBlank(map[0].get("PhoneNumber").toString()))// PhoneNumber
					{// 如果没有电话号码返回,跳转到个人资料填写
						// TODO
						intent.setClass(LoginActivity.this, UserInfoActivity.class);
						intent.putExtra("", map[0].get("UserID").toString());
					}
					else
						intent.setClass(LoginActivity.this, MainActivity.class);

					startActivity(intent);
					LoginActivity.this.finish();
				}
				else
					ToastManager.getInstance(LoginActivity.this).showToast("登录失败!");

			}
			catch (Exception e)
			{
				e.printStackTrace();
				ToastManager.getInstance(LoginActivity.this).showToast("登录失败!");
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			dismissProgress();
		}

	}

	class TimeCount extends CountDownTimer
	{
		public TimeCount(long millisInFuture, long countDownInterval)
		{
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish()
		{// 计时完毕时触发
			checkButton.setText(getResources().getString(R.string.register_get_messcheck));
			checkButton.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished)
		{// 计时过程显示
			checkButton.setClickable(false);
			checkButton.setText(millisUntilFinished / 1000 + "秒");
		}

	}

	private void test()
	{
		StringBuffer testbuffer = new StringBuffer();
		testbuffer.append(" ");
		testbuffer.append("[{\"Abstract\":\"近日，网友评出“年度最伤人聊天词汇”，“呵呵”当选，没有之一。“笑声里含义深刻，不显山 ");
		testbuffer.append("不露水，让你琢磨不透。”网友形容这个词只有一个用处：以最大效果激怒对方，践踏对方全部热情。其实，英语中常说的“interesting”也有异曲同工之 ");
		testbuffer
				.append("妙。\",\"AttachementArray\":[],\"Banner\":\"\",\"CommentCount\":0,\"Content\":\"当外国人听你讲了一堆他觉得很扯的东西，一般都会说“interesting”。如果你真觉得他表示感兴趣 ");
		testbuffer.append("的话，那就错了，他们只是想表达“呵呵”而已。周末，分享一组超搞的创意，看外国怎么表达“呵呵”这个高贵冷艳的词。\r\n\r\n《国外怎么表达“呵呵”这个高贵冷艳的词 ");
		testbuffer.append("》\r\n\r\n　　近日，网友评出“年度最伤人聊天词汇”，“呵呵”当选，没有之一。“笑声里含义深刻，不显山不露水，让你琢磨不透。”网友形容这个词只有一个用处：以最 ");
		testbuffer.append("大效果激怒对方，践踏对方全部热情。其实，英语中常说的“interesting”也有异曲同工之妙。\r\n\r\n　　英语单词interesting，汉语通常译为“有趣的”。听到美国人 ");
		testbuffer.append("说“It’s interesting”（很有趣）或“That’s an interesting idea”（有趣的想法），通常，中国人都理解为赞同或夸奖。\r\n\r\n　　但是，实际上，interesting，并不 ");
		testbuffer.append("是一个褒义词。当然，也不是一个贬义词。“具体情况具体分析”。好比在某场合，有个中国人说了句什么，根据当时的情景、语境或上下文，闻者应该能听得出是真心赞同，还 ");
		testbuffer.append("是客套敷衍，甚或为讽刺嘲笑。\r\n\r\n　　interesting，根据权威的剑桥英语词典，意思是：引人注意（holding one’s attention）。比如，一个 同学说，I just read a  ");
		testbuffer.append("book. It’s very interesting. （我刚看了一本书，很有意思。）引起闻者的注意，接下来，会有补充评论，如：It’s really funny. （很搞笑的。）这是褒义的用法。\r\n ");
		testbuffer
				.append("\r\n　　也有贬义的用法。如： You screaming at me when it wasn't my fault was really interesting! （不是我的过错，你却对我吼叫，真有意思！）\r\n\r\n　　如何辨 ");
		testbuffer
				.append("别是否认同、赞同甚或夸奖？关键全在于根据语境上下文（The context is everything）。\r\n\r\n　　如果在说了“It’s interesting”或者“That’s an interesting idea ");
		testbuffer.append("”之后，显出“愿听详情”的样子，或者顺着话题分享同感，那表示闻者确实是真有兴趣，很是赞同。\r\n\r\n　　但如果下文是转换话题，迂回敲击，那便表明，闻者并不赞同 ");
		testbuffer.append("。“That’s an interesting idea”相当于汉语的“那可是个奇葩想法”。大家都知道，这里，奇葩的含义是怪异、离奇、不可思议。整个一句话的实际含义，并无表示赞同之意 ");
		testbuffer.append("。\r\n\r\n　　多数情况是，英美人对你所说的东西没有兴趣，但又不愿直白地让你扫兴，于是，便会说“It’s interesting.”或者“That’s an interesting idea.”显然， ");
		testbuffer.append("这并不表示赞同、欣赏或夸奖，只是礼貌性的回应而已。\r\n\r\n　　“呵呵”，是笑声的拟声词，原本表示笑声。在网络聊天时，若对对方表示不满或者不知道该说什么，有网 ");
		testbuffer.append("民会用“呵呵”应急敷衍。所以，在客套敷衍时，英美人用“interesting”词句，相当于中国网民用“呵呵”。\r\n\r\n　　当外国人听你讲了一堆他觉得很扯的东西，一般都会 ");
		testbuffer.append("说“interesting”。如果你真觉得他表示感兴趣的话，那就错了，他们只是想表达“呵呵”而已。周末，分享一组超搞的创意，看外国怎么表达“呵呵”这个高贵冷艳的 ");
		testbuffer
				.append("词。\",\"Fav\":0,\"ID\":2,\"Link\":\"\",\"New\":0,\"Title\":\"你会用英文表达''呵呵'' 吗？\",\"Type\":0},{\"Fav\":0,\"FeedbackAttachementArray\": ");
		testbuffer
				.append("[],\"FeedbackContent\":\"\",\"ID\":1,\"LastChatMap\":{},\"New\":0,\"RequirementAttachementArray\":[],\"RequirementDescription\":\"录制一个5到10分钟的语音，描述一下今年冬天 ");
		testbuffer.append("的第一场雪，可自行决定具体的场景。\",\"Title\":\"冬天的第一场雪\",\"Type\":1,\"UploadType\":0,\"UploadedContent\":\"\"}] ");
		String test = testbuffer.toString();
		List<Map<String, Object>> lMaps = JsonUtil.getList(test);
		int size = lMaps.size();
	}
}
