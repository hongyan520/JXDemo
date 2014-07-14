package com.demo.jxdemo.ui.activity.login;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ui.listener.OnClickAvoidForceListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
		numEditText.addTextChangedListener(textWatcher);
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

//					String test = "[{\"AcceptMaterial\":1,\"AcceptTraining\":1,\"ID\":1,\"Icon\":\"/uploads/dfceee24cb4f463f/IconSpoken.png\",\"Title\":\"英语口语\"},{\"AcceptMaterial\":1,\"AcceptTraining\":1,\"ID\":2,\"Icon\":\"/uploads/e0ed88dce6884ed8/IconWriting.png\",\"Title\":\"英语写作\"}]";
//					SharedPreferencesConfig.saveConfig(LoginActivity.this, Constant.USER_COURSEARRAY, StringUtil.Object2String(test));
//					StringBuffer testbuffer = new StringBuffer();
//					testbuffer
//							.append("[{\"Abstract\":\"英文书写应符合书写规范，英文字母要写清楚、写整齐、写美观，字母的大小和字母之间的距离要匀称。书写应做到字形秀丽漂亮，通篇匀称和谐。\",\"AttachementArray\":[{\"Size\":3.246014e+06,\"URL\":\"/uploads/2014/6/23/79152daaa38a4e56/方正静蕾简体.rar\"},{\"Size\":1.357885e+06,\"URL\":\"/uploads/2014/6/23/efcd09d6e0ba4ca5/2273-11.jpg\"}],\"Banner\":\"/uploads/dd3ec5b3d9f143da/MaterialBanner1.jpg\",\"CommentCount\":0,\"Content\":\"       英文书写应符合书写规范，英文字母要写清楚、写整齐、写美观，字母的大小和字母之间的距离要匀称。书写应做到字形秀丽漂亮，通篇匀称和谐。\r\n       写英文字母要掌握正确笔顺。如小写字母i ，应该先写下面的部分，然后再打点。有的学生却按写汉字的习惯从上到下写，写快了，就会把点和下面的竖笔连在一起，显得十分别扭。字形t应为两笔。不少人却将两笔合成一笔，看上去不像t，倒像l或是e，难以辨认。另外，把r写成v，把q写成把g，把k写成h等等，都是中学生书写中常见的毛病。\",\"CourseID\":2,\"Fav\":0,\"ID\":1,\"Link\":\"http://xixinfei.iteye.com/blog/2002017\",\"New\":1,\"Time\":\"2014-06-25 15:06:33\",\"Title\":\"如何正确的书写英语文章\",\"Type\":0},{\"CourseID\":2,\"Fav\":0,\"FeedbackAttachementArray\":[],\"FeedbackContent\":\"\",\"ID\":2,\"Icon\":\"/uploads/2014/6/24/2e8c19dacae34c47/IconWriting.png\",\"LastChatMap\":{},\"New\":1,\"RequirementAttachementArray\":[{\"Size\":478,\"URL\":\"/uploads/2014/6/23/3feb85885e014e2d/VMWare Fusion 6 SN.txt\"},{\"Size\":3.246014e+06,\"URL\":\"/uploads/2014/6/23/79152daaa38a4e56/方正静蕾简体.rar\"}],\"RequirementDescription\":\"您下周五会去洛杉矶开会，请将您的整个行程告知对方知晓，并要求对方接机。\",\"Status\":0,\"Time\":\"2014-06-25 15:06:33\",\"Title\":\"给Peter写信告知您去洛杉矶的行程\",\"Type\":1,\"UploadType\":0,\"UploadedContentArray\":[]},{\"Abstract\":\"When life gets hard and you want to give up, remember that life is full of ups and downs, and without the downs, the ups would mean nothing. 当生活很艰难，你想要放弃的时候，请记住，生活充满了起起落落，如果没有低谷，那站在高处也失去了意义。\",\"AttachementArray\":[],\"Banner\":\"\",\"CommentCount\":0,\"Content\":\"\",\"CourseID\":1,\"Fav\":0,\"ID\":3,\"Link\":\"\",\"New\":1,\"Time\":\"2014-06-25 15:06:33\",\"Title\":\"\",\"Type\":0},{\"Abstract\":\"近日，网友评出“年度最伤人聊天词汇”，“呵呵”当选，没有之一。“笑声里含义深刻，不显山不露水，让你琢磨不透。”网友形容这个词只有一个用处：以最大效果激怒对方，践踏对方全部热情。其实，英语中常说的“interesting”也有异曲同工之妙。\",\"AttachementArray\":[{\"Size\":1.357885e+06,\"URL\":\"/uploads/2014/6/23/efcd09d6e0ba4ca5/2273-11.jpg\"},{\"Size\":3.246014e+06,\"URL\":\"/uploads/2014/6/23/79152daaa38a4e56/方正静蕾简体.rar\"},{\"Size\":478,\"URL\":\"/uploads/2014/6/23/3feb85885e014e2d/VMWare Fusion 6 SN.txt\"}],\"Banner\":\"/uploads/2014/6/23/ec8f707f267f487a/MaterialBanner1.jpg\",\"CommentCount\":0,\"Content\":\"当外国人听你讲了一堆他觉得很扯的东西，一般都会说“interesting”。如果你真觉得他表示感兴趣的话，那就错了，他们只是想表达“呵呵”而已。周末，分享一组超搞的创意，看外国怎么表达“呵呵”这个高贵冷艳的词。\r\n\r\n《国外怎么表达“呵呵”这个高贵冷艳的词》\r\n\r\n　　近日，网友评出“年度最伤人聊天词汇”，“呵呵”当选，没有之一。“笑声里含义深刻，不显山不露水，让你琢磨不透。”网友形容这个词只有一个用处：以最大效果激怒对方，践踏对方全部热情。其实，英语中常说的“interesting”也有异曲同工之妙。\r\n\r\n　　英语单词interesting，汉语通常译为“有趣的”。听到美国人说“It’s interesting”（很有趣）或“That’s an interesting idea”（有趣的想法），通常，中国人都理解为赞同或夸奖。\r\n\r\n　　但是，实际上，interesting，并不是一个褒义词。当然，也不是一个贬义词。“具体情况具体分析”。好比在某场合，有个中国人说了句什么，根据当时的情景、语境或上下文，闻者应该能听得出是真心赞同，还是客套敷衍，甚或为讽刺嘲笑。\r\n\r\n　　interesting，根据权威的剑桥英语词典，意思是：引人注意（holding one’s attention）。比如，一个 同学说，I just read a book. It’s very interesting. （我刚看了一本书，很有意思。）引起闻者的注意，接下来，会有补充评论，如：It’s really funny. （很搞笑的。）这是褒义的用法。\r\n\r\n　　也有贬义的用法。如： You screaming at me when it wasn't my fault was really interesting! （不是我的过错，你却对我吼叫，真有意思！）\r\n\r\n　　如何辨别是否认同、赞同甚或夸奖？关键全在于根据语境上下文（The context is everything）。\r\n\r\n　　如果在说了“It’s interesting”或者“That’s an interesting idea”之后，显出“愿听详情”的样子，或者顺着话题分享同感，那表示闻者确实是真有兴趣，很是赞同。\r\n\r\n　　但如果下文是转换话题，迂回敲击，那便表明，闻者并不赞同。“That’s an interesting idea”相当于汉语的“那可是个奇葩想法”。大家都知道，这里，奇葩的含义是怪异、离奇、不可思议。整个一句话的实际含义，并无表示赞同之意。\r\n\r\n　　多数情况是，英美人对你所说的东西没有兴趣，但又不愿直白地让你扫兴，于是，便会说“It’s interesting.”或者“That’s an interesting idea.”显然，这并不表示赞同、欣赏或夸奖，只是礼貌性的回应而已。\r\n\r\n　　“呵呵”，是笑声的拟声词，原本表示笑声。在网络聊天时，若对对方表示不满或者不知道该说什么，有网民会用“呵呵”应急敷衍。所以，在客套敷衍时，英美人用“interesting”词句，相当于中国网民用“呵呵”。\r\n\r\n　　当外国人听你讲了一堆他觉得很扯的东西，一般都会说“interesting”。如果你真觉得他表示感兴趣的话，那就错了，他们只是想表达“呵呵”而已。周末，分享一组超搞的创意，看外国怎么表达“呵呵”这个高贵冷艳的词。\",\"CourseID\":1,\"Fav\":0,\"ID\":2,\"Link\":\"http://xixinfei.iteye.com/blog/2002017\",\"New\":1,\"Time\":\"2014-06-25 15:06:33\",\"Title\":\"你会用英文表达''呵呵'' 吗？\",\"Type\":0},{\"CourseID\":1,\"Fav\":0,\"FeedbackAttachementArray\":[],\"FeedbackContent\":\"\",\"ID\":1,\"Icon\":\"/uploads/2014/6/24/928a4d457c8d4cb0/IconSpoken.png\",\"LastChatMap\":{},\"New\":1,\"RequirementAttachementArray\":[{\"Size\":3.246014e+06,\"URL\":\"/uploads/2014/6/23/79152daaa38a4e56/方正静蕾简体.rar\"},{\"Size\":478,\"URL\":\"/uploads/2014/6/23/3feb85885e014e2d/VMWare Fusion 6 SN.txt\"}],\"RequirementDescription\":\"录制一个5到10分钟的语音，描述一下今年冬天的第一场雪，可自行决定具体的场景。\",\"Status\":0,\"Time\":\"2014-06-25 15:06:33\",\"Title\":\"冬天的第一场雪\",\"Type\":1,\"UploadType\":0,\"UploadedContentArray\":[]}]");
//
//					SharedPreferencesConfig.saveConfig(LoginActivity.this, Constant.USER_TEST, testbuffer.toString());
//					Intent intent = new Intent();
//					intent.setClass(LoginActivity.this, MainActivity.class);
//					startActivity(intent);
//					LoginActivity.this.finish();
					 StringBuffer testbuffer = new StringBuffer();
					 testbuffer
					 .append("[{\"Abstract\":\"英文书写应符合书写规范，英文字母要写清楚、写整齐、写美观，字母的大小和字母之间的距离要匀称。书写应做到字形秀丽漂亮，通篇匀称和谐。\",\"AttachementArray\":[{\"Size\":3.246014e+06,\"URL\":\"/uploads/2014/6/23/79152daaa38a4e56/方正静蕾简体.rar\"},{\"Size\":1.357885e+06,\"URL\":\"/uploads/2014/6/23/efcd09d6e0ba4ca5/2273-11.jpg\"}],\"Banner\":\"/uploads/dd3ec5b3d9f143da/MaterialBanner1.jpg\",\"CommentCount\":0,\"Content\":\"       英文书写应符合书写规范，英文字母要写清楚、写整齐、写美观，字母的大小和字母之间的距离要匀称。书写应做到字形秀丽漂亮，通篇匀称和谐。\r\n       写英文字母要掌握正确笔顺。如小写字母i ，应该先写下面的部分，然后再打点。有的学生却按写汉字的习惯从上到下写，写快了，就会把点和下面的竖笔连在一起，显得十分别扭。字形t应为两笔。不少人却将两笔合成一笔，看上去不像t，倒像l或是e，难以辨认。另外，把r写成v，把q写成把g，把k写成h等等，都是中学生书写中常见的毛病。\",\"CourseID\":2,\"Fav\":0,\"ID\":1,\"Link\":\"http://xixinfei.iteye.com/blog/2002017\",\"New\":1,\"Time\":\"2014-06-25 15:06:33\",\"Title\":\"如何正确的书写英语文章\",\"Type\":0},{\"CourseID\":2,\"Fav\":0,\"FeedbackAttachementArray\":[],\"FeedbackContent\":\"\",\"ID\":2,\"Icon\":\"/uploads/2014/6/24/2e8c19dacae34c47/IconWriting.png\",\"LastChatMap\":{},\"New\":1,\"RequirementAttachementArray\":[{\"Size\":478,\"URL\":\"/uploads/2014/6/23/3feb85885e014e2d/VMWare Fusion 6 SN.txt\"},{\"Size\":3.246014e+06,\"URL\":\"/uploads/2014/6/23/79152daaa38a4e56/方正静蕾简体.rar\"}],\"RequirementDescription\":\"您下周五会去洛杉矶开会，请将您的整个行程告知对方知晓，并要求对方接机。\",\"Status\":0,\"Time\":\"2014-06-25 15:06:33\",\"Title\":\"给Peter写信告知您去洛杉矶的行程\",\"Type\":1,\"UploadType\":0,\"UploadedContentArray\":[]},{\"Abstract\":\"When life gets hard and you want to give up, remember that life is full of ups and downs, and without the downs, the ups would mean nothing. 当生活很艰难，你想要放弃的时候，请记住，生活充满了起起落落，如果没有低谷，那站在高处也失去了意义。\",\"AttachementArray\":[],\"Banner\":\"\",\"CommentCount\":0,\"Content\":\"\",\"CourseID\":1,\"Fav\":0,\"ID\":3,\"Link\":\"\",\"New\":1,\"Time\":\"2014-06-25 15:06:33\",\"Title\":\"\",\"Type\":0},{\"Abstract\":\"近日，网友评出“年度最伤人聊天词汇”，“呵呵”当选，没有之一。“笑声里含义深刻，不显山不露水，让你琢磨不透。”网友形容这个词只有一个用处：以最大效果激怒对方，践踏对方全部热情。其实，英语中常说的“interesting”也有异曲同工之妙。\",\"AttachementArray\":[{\"Size\":1.357885e+06,\"URL\":\"/uploads/2014/6/23/efcd09d6e0ba4ca5/2273-11.jpg\"},{\"Size\":3.246014e+06,\"URL\":\"/uploads/2014/6/23/79152daaa38a4e56/方正静蕾简体.rar\"},{\"Size\":478,\"URL\":\"/uploads/2014/6/23/3feb85885e014e2d/VMWare Fusion 6 SN.txt\"}],\"Banner\":\"/uploads/2014/6/23/ec8f707f267f487a/MaterialBanner1.jpg\",\"CommentCount\":0,\"Content\":\"当外国人听你讲了一堆他觉得很扯的东西，一般都会说“interesting”。如果你真觉得他表示感兴趣的话，那就错了，他们只是想表达“呵呵”而已。周末，分享一组超搞的创意，看外国怎么表达“呵呵”这个高贵冷艳的词。\r\n\r\n《国外怎么表达“呵呵”这个高贵冷艳的词》\r\n\r\n　　近日，网友评出“年度最伤人聊天词汇”，“呵呵”当选，没有之一。“笑声里含义深刻，不显山不露水，让你琢磨不透。”网友形容这个词只有一个用处：以最大效果激怒对方，践踏对方全部热情。其实，英语中常说的“interesting”也有异曲同工之妙。\r\n\r\n　　英语单词interesting，汉语通常译为“有趣的”。听到美国人说“It’s interesting”（很有趣）或“That’s an interesting idea”（有趣的想法），通常，中国人都理解为赞同或夸奖。\r\n\r\n　　但是，实际上，interesting，并不是一个褒义词。当然，也不是一个贬义词。“具体情况具体分析”。好比在某场合，有个中国人说了句什么，根据当时的情景、语境或上下文，闻者应该能听得出是真心赞同，还是客套敷衍，甚或为讽刺嘲笑。\r\n\r\n　　interesting，根据权威的剑桥英语词典，意思是：引人注意（holding one’s attention）。比如，一个 同学说，I just read a book. It’s very interesting. （我刚看了一本书，很有意思。）引起闻者的注意，接下来，会有补充评论，如：It’s really funny. （很搞笑的。）这是褒义的用法。\r\n\r\n　　也有贬义的用法。如： You screaming at me when it wasn't my fault was really interesting! （不是我的过错，你却对我吼叫，真有意思！）\r\n\r\n　　如何辨别是否认同、赞同甚或夸奖？关键全在于根据语境上下文（The context is everything）。\r\n\r\n　　如果在说了“It’s interesting”或者“That’s an interesting idea”之后，显出“愿听详情”的样子，或者顺着话题分享同感，那表示闻者确实是真有兴趣，很是赞同。\r\n\r\n　　但如果下文是转换话题，迂回敲击，那便表明，闻者并不赞同。“That’s an interesting idea”相当于汉语的“那可是个奇葩想法”。大家都知道，这里，奇葩的含义是怪异、离奇、不可思议。整个一句话的实际含义，并无表示赞同之意。\r\n\r\n　　多数情况是，英美人对你所说的东西没有兴趣，但又不愿直白地让你扫兴，于是，便会说“It’s interesting.”或者“That’s an interesting idea.”显然，这并不表示赞同、欣赏或夸奖，只是礼貌性的回应而已。\r\n\r\n　　“呵呵”，是笑声的拟声词，原本表示笑声。在网络聊天时，若对对方表示不满或者不知道该说什么，有网民会用“呵呵”应急敷衍。所以，在客套敷衍时，英美人用“interesting”词句，相当于中国网民用“呵呵”。\r\n\r\n　　当外国人听你讲了一堆他觉得很扯的东西，一般都会说“interesting”。如果你真觉得他表示感兴趣的话，那就错了，他们只是想表达“呵呵”而已。周末，分享一组超搞的创意，看外国怎么表达“呵呵”这个高贵冷艳的词。\",\"CourseID\":1,\"Fav\":0,\"ID\":2,\"Link\":\"http://xixinfei.iteye.com/blog/2002017\",\"New\":1,\"Time\":\"2014-06-25 15:06:33\",\"Title\":\"你会用英文表达''呵呵'' 吗？\",\"Type\":0},{\"CourseID\":1,\"Fav\":0,\"FeedbackAttachementArray\":[],\"FeedbackContent\":\"\",\"ID\":1,\"Icon\":\"/uploads/2014/6/24/928a4d457c8d4cb0/IconSpoken.png\",\"LastChatMap\":{},\"New\":1,\"RequirementAttachementArray\":[{\"Size\":3.246014e+06,\"URL\":\"/uploads/2014/6/23/79152daaa38a4e56/方正静蕾简体.rar\"},{\"Size\":478,\"URL\":\"/uploads/2014/6/23/3feb85885e014e2d/VMWare Fusion 6 SN.txt\"}],\"RequirementDescription\":\"录制一个5到10分钟的语音，描述一下今年冬天的第一场雪，可自行决定具体的场景。\",\"Status\":0,\"Time\":\"2014-06-25 15:06:33\",\"Title\":\"冬天的第一场雪\",\"Type\":1,\"UploadType\":0,\"UploadedContentArray\":[]}]");
					
					 SharedPreferencesConfig.saveConfig(LoginActivity.this, Constant.USER_TEST, testbuffer.toString());
					
					 if (isPassCheck(1))
					 {
					 showProgress();
					 login();
					 }
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
		String num = numEditText.getText().toString().replace(" ", "");
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
		parasTemp.put("PhoneNumber", numEditText.getText().toString().trim().replace(" ", ""));

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
						ToastManager.getInstance(LoginActivity.this).showToast("验证码短信已发送，请注意查收");
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
				// if (map != null)
				// {
				// // checkCode = "1234";// StringUtil.Object2String(map[0].get(""));
				// ToastManager.getInstance(LoginActivity.this).showToast("验证码短信已发送，请注意查收");
				// }
				// else
				// ToastManager.getInstance(LoginActivity.this).showToast("获取验证码失败!");
				// numEditText.setFocusable(false);
				// numEditText.setFocusableInTouchMode(false);
				// checkEditText.setFocusable(true);
				// checkEditText.setFocusableInTouchMode(true);
				// checkEditText.setSelectAllOnFocus(true);
				// checkEditText.setCursorVisible(true);
				// checkEditText.requestFocus();
				// checkEditText.setSelection(0);
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
			// numEditText.setFocusable(false);
			// numEditText.setFocusableInTouchMode(false);
			checkEditText.setFocusable(true);
			checkEditText.setFocusableInTouchMode(true);
			checkEditText.setSelectAllOnFocus(true);
			checkEditText.setCursorVisible(true);
			checkEditText.requestFocus();
			checkEditText.setSelection(0);
			dismissProgress();
		}

	}

	private void login()
	{
		Map<String, Object> parasTemp = new HashMap<String, Object>();
		parasTemp.put("PhoneNumber", numEditText.getText().toString().trim().replace(" ", ""));
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
					if (StringUtil.isBlank(map[0].get("PhoneNumber").toString()) || StringUtil.isBlank(map[0].get("Title").toString()))// PhoneNumber
					{// 如果没有电话号码返回,跳转到个人资料填写
						// TODO
						intent.setClass(LoginActivity.this, UserInfoActivity.class);
						intent.putExtra("", map[0].get("UserID").toString());
						intent.putExtra("fromLogin", "fromLogin");
						startActivity(intent);
					}
					else
					{
						intent.setClass(LoginActivity.this, MainActivity.class);
						startActivity(intent);
						LoginActivity.this.finish();
					}

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

	public TextWatcher textWatcher = new TextWatcher()
	{

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count)
		{
			String num = numEditText.getText().toString();
			Log.i("chan", "start:" + start + " before:" + before + " count:" + count);

			if (start == 2 && before == 0)
			{
				numEditText.setText(num + " ");
				num = numEditText.getText().toString();
				numEditText.setSelection(num.length());
			}
			if (start == 7 && before == 0)
			{
				numEditText.setText(num + " ");
				num = numEditText.getText().toString();
				numEditText.setSelection(num.length());
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after)
		{
		}

		@Override
		public void afterTextChanged(Editable s)
		{
			try
			{
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	};

	private void test()
	{
		StringBuffer testbuffer = new StringBuffer();
		testbuffer
				.append("[{\"Abstract\":\"近日，网友评出“年度最伤人聊天词汇”，“呵呵”当选，没有之一。“笑声里含义深刻，不显山不露水，让你琢磨不透。”网友形容这个词只有一个用处：以最大效果激怒对方，践踏对方全部热情。其实，英语中常说的“interesting”也有异曲同工之妙。\",\"AttachementArray\":[],\"Banner\":\"\",\"CommentCount\":0,\"Content\":\"当外国人听你讲了一堆他觉得很扯的东西，一般都会说“interesting”。如果你真觉得他表示感兴趣的话，那就错了，他们只是想表达“呵呵”而已。周末，分享一组超搞的创意，看外国怎么表达“呵呵”这个高贵冷艳的词。\r\n\r\n《国外怎么表达“呵呵”这个高贵冷艳的词》\r\n\r\n　　近日，网友评出“年度最伤人聊天词汇”，“呵呵”当选，没有之一。“笑声里含义深刻，不显山不露水，让你琢磨不透。”网友形容这个词只有一个用处：以最大效果激怒对方，践踏对方全部热情。其实，英语中常说的“interesting”也有异曲同工之妙。\r\n\r\n　　英语单词interesting，汉语通常译为“有趣的”。听到美国人说“It’s interesting”（很有趣）或“That’s an interesting idea”（有趣的想法），通常，中国人都理解为赞同或夸奖。\r\n\r\n　　但是，实际上，interesting，并不是一个褒义词。当然，也不是一个贬义词。“具体情况具体分析”。好比在某场合，有个中国人说了句什么，根据当时的情景、语境或上下文，闻者应该能听得出是真心赞同，还是客套敷衍，甚或为讽刺嘲笑。\r\n\r\n　　interesting，根据权威的剑桥英语词典，意思是：引人注意（holding one’s attention）。比如，一个 同学说，I just read a book. It’s very interesting. （我刚看了一本书，很有意思。）引起闻者的注意，接下来，会有补充评论，如：It’s really funny. （很搞笑的。）这是褒义的用法。\r\n\r\n　　也有贬义的用法。如： You screaming at me when it wasn't my fault was really interesting! （不是我的过错，你却对我吼叫，真有意思！）\r\n\r\n　　如何辨别是否认同、赞同甚或夸奖？关键全在于根据语境上下文（The context is everything）。\r\n\r\n　　如果在说了“It’s interesting”或者“That’s an interesting idea”之后，显出“愿听详情”的样子，或者顺着话题分享同感，那表示闻者确实是真有兴趣，很是赞同。\r\n\r\n　　但如果下文是转换话题，迂回敲击，那便表明，闻者并不赞同。“That’s an interesting idea”相当于汉语的“那可是个奇葩想法”。大家都知道，这里，奇葩的含义是怪异、离奇、不可思议。整个一句话的实际含义，并无表示赞同之意。\r\n\r\n　　多数情况是，英美人对你所说的东西没有兴趣，但又不愿直白地让你扫兴，于是，便会说“It’s interesting.”或者“That’s an interesting idea.”显然，这并不表示赞同、欣赏或夸奖，只是礼貌性的回应而已。\r\n\r\n　　“呵呵”，是笑声的拟声词，原本表示笑声。在网络聊天时，若对对方表示不满或者不知道该说什么，有网民会用“呵呵”应急敷衍。所以，在客套敷衍时，英美人用“interesting”词句，相当于中国网民用“呵呵”。\r\n\r\n　　当外国人听你讲了一堆他觉得很扯的东西，一般都会说“interesting”。如果你真觉得他表示感兴趣的话，那就错了，他们只是想表达“呵呵”而已。周末，分享一组超搞的创意，看外国怎么表达“呵呵”这个高贵冷艳的词。\",\"Fav\":0,\"ID\":2,\"Link\":\"\",\"New\":1,\"Time\":\"2014-06-15 17:08:08\",\"Title\":\"你会用英文表达''呵呵'' 吗？\",\"Type\":0}]");
		String test = testbuffer.toString();
		List<Map<String, Object>> lMaps = JsonUtil.getList(test);
		int size = lMaps.size();
	}
}
