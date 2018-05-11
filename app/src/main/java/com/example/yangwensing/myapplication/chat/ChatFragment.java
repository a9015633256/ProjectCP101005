package com.example.yangwensing.myapplication.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yangwensing.myapplication.R;
import com.example.yangwensing.myapplication.info.StudentInfoEditFragment;
import com.example.yangwensing.myapplication.login.LoginFragment;
import com.example.yangwensing.myapplication.main.Common;
import com.example.yangwensing.myapplication.main.MyTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.security.PublicKey;
import java.util.List;

import static com.example.yangwensing.myapplication.info.StudentInfoEditFragment.TAG;
import static com.example.yangwensing.myapplication.main.Common.chatWebSocketClient;
import static com.example.yangwensing.myapplication.main.Common.getUserName;

/**
 * Created by nameless on 2018/5/11.
 */

public class ChatFragment extends Fragment {

    String receiver="";
    String senderte="";
    private LocalBroadcastManager broadcastManager;
    private LinearLayout layout;
    private ScrollView scrollView;
    private MyTask messageTask;

    private Button btsend;
    private EditText etmessage;



    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_chat,container,false);

        layout = view.findViewById(R.id.layout);
        scrollView = view.findViewById(R.id.scrollView);

        btsend = view.findViewById(R.id.btSend);
        etmessage = view.findViewById(R.id.etMessage);





        final Bundle bundle = getArguments();
        receiver = bundle.getString("receiver");
        senderte = bundle.getString("sender");


        getActivity().setTitle(receiver);


        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        registerChatReceiver();
        Common.connectServer(getActivity(), senderte);

        String url = Common.URL + "/LoginHelp";
        List<Message> messages = null;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getmessage");
        jsonObject.addProperty("sender",senderte);
        jsonObject.addProperty("receiver",receiver);
        String jsonOut = jsonObject.toString();
        messageTask = new MyTask(url, jsonOut);
        try {
            String jsonIn = messageTask.execute().get();
            Log.d(TAG, jsonIn);
            Type listType = new TypeToken<List<Message>>() {
            }.getType();
            messages = new Gson().fromJson(jsonIn, listType);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        if (messages == null || messages.isEmpty()) {
            Toast.makeText(getActivity(), "empty", Toast.LENGTH_SHORT).show();

        } else {
            String sender = senderte;
            View viewr;
            Toast.makeText(getActivity(), "have message", Toast.LENGTH_SHORT).show();
            for (int i=1; i<messages.size(); i++) {
                if (senderte.equals(messages.get(i).getSender())){
                    String text = sender + ":\n " + messages.get(i).getMessage() + "\n";


                    viewr = View.inflate(getActivity(), R.layout.chat_message_right, null);
                    TextView textView = viewr.findViewById(R.id.tvtype);
                    textView.setText(text);
                    layout.addView(viewr);
                }else{
                    String text =receiver + ":\n " + messages.get(i).getMessage() + "\n";

                    viewr = View.inflate(getActivity(), R.layout.chat_message_left, null);
                    TextView textView = viewr.findViewById(R.id.tvtype);
                    textView.setText(text);
                    layout.addView(viewr);
                }
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);//自動捲動到最下面
                    }
                });

            }

        }


        btsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = etmessage.getText().toString();
                if (message.trim().isEmpty()) {
                    Toast.makeText(getActivity(), "empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                String sender = senderte;
                // 將欲傳送訊息先顯示在TextView上

                showMessage(sender, message, false);

                // 將輸入的訊息清空
                etmessage.setText(null);
                // 捲動至最新訊息
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
                // 將欲傳送訊息轉成JSON後送出
                ChatMessage chatMessage = new ChatMessage("chat", sender, receiver, message);
                String chatMessageJson = new Gson().toJson(chatMessage);
                chatWebSocketClient.send(chatMessageJson);
                Log.d(TAG, "output: " + chatMessageJson);

            }
        });




        return view;
    }
    private void registerChatReceiver() {
        IntentFilter chatFilter = new IntentFilter("chat");
        ChatReceiver chatReceiver = new ChatReceiver();
        broadcastManager.registerReceiver(chatReceiver, chatFilter);
    }
    private class ChatReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            ChatMessage chatMessage = new Gson().fromJson(message, ChatMessage.class);
            String sender = chatMessage.getSender();
            // 接收到聊天訊息，若發送者與目前聊天對象相同，就將訊息顯示在TextView
            if (sender.equals(receiver)) {
                showMessage(sender, chatMessage.getMessage(), true);

            }
            Log.d(TAG, message);
        }
    }

    private void showMessage(String sender, String message, boolean left) {
        String text = sender + ":\n " + message;
        View viewr;
        // 準備左右2種layout給不同種類發訊者(他人/自己)使用
        if (left) {
            viewr = View.inflate(getActivity(), R.layout.chat_message_left, null);

        } else {
            viewr = View.inflate(getActivity(), R.layout.chat_message_right, null);
        }
        TextView textView = viewr.findViewById(R.id.tvtype);
        textView.setText(text);
        layout.addView(viewr);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);//自動捲動到最下面
            }
        });
    }
}
