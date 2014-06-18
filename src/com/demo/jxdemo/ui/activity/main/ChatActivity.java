package com.demo.jxdemo.ui.activity.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.demo.jxdemo.R;
import com.demo.jxdemo.ui.activity.BaseActivity;
import com.demo.jxdemo.ui.adapter.ChatListAdapter;

public class ChatActivity extends BaseActivity
{
	private Button sendButton;

	private EditText contentEditText = null;

	private ListView chatListView = null;

	private List<Map<String, Object>> chatList = null;

	private ChatListAdapter chatAdapter = null;

	boolean jianghua;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chat);

		contentEditText = (EditText) this.findViewById(R.id.edit_chat);
		chatListView = (ListView) this.findViewById(R.id.listview_chat);
		sendButton = (Button) this.findViewById(R.id.btn_send);

		chatList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		for (int i = 0; i < 2; i++)
		{
			map = new HashMap<String, Object>();
			if (i % 2 == 0)
			{
				map.put("flag", "from");
			}
			else
			{
				map.put("flag", "to");
			}
			chatList.add(map);
		}

		chatAdapter = new ChatListAdapter(this, chatList);
		chatListView.setAdapter(chatAdapter);

		sendButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (!contentEditText.getText().toString().equals(""))
				{
					// 发送消息
					send();
				}
				else
				{
					Toast.makeText(ChatActivity.this, "Content is empty", Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	private void send()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		chatList.add(map);
		chatAdapter.notifyDataSetChanged();
		chatListView.setSelection(chatList.size() - 1);
		contentEditText.setText("");
	}
}
