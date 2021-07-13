package com.example.imageapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.imageapp.R;
import com.example.imageapp.model.GenerPrecenter;
import com.example.imageapp.view.adapter.ContactAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatActivity extends AppCompatActivity {
    private static NetworkInterface mNetworkInterface;
    private RecyclerView mRecyclerViewChat;
    private Button mButtonLogin;
    private Button mButtonChat;
    private EditText mEditTextName;
    private List<String> mListMessages;
    private ContactAdapter mChatAdapter;
    private final String URL_SERVER = GenerPrecenter.URL;
    private Socket mSocket;

    // >=== start addition
    private List<String> mContacts;
    private RecyclerView mRecyclerViewContact;
    private ContactAdapter mContactAdapter;
    // <=== end addition
    {
        try {
            mSocket = IO.socket(GenerPrecenter.URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View v = findViewById(R.id.btn_chat);
        String x = Integer.toString(v.getWidth());
        String y = Integer.toString(v.getHeight());

        Toast.makeText(getBaseContext(), x+", "+y, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // >=== start addition
        mContacts = new ArrayList<>();
        mRecyclerViewContact = findViewById(R.id.rc_contact);
        mRecyclerViewContact.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        // <=== end addition


        mButtonLogin = findViewById(R.id.btn_login);
        mButtonChat = findViewById(R.id.btn_chat);
        mEditTextName = findViewById(R.id.edt_name);
        mListMessages = new ArrayList<>();
        mRecyclerViewChat = findViewById(R.id.rc_chat);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerViewChat.setLayoutManager(layoutManager);
        mChatAdapter = new ContactAdapter(mListMessages);
        mRecyclerViewChat.setAdapter(mChatAdapter);

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSocket.emit("user_login", mEditTextName.getText().toString());
            }
        });

        mButtonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSocket.emit("send_message", mEditTextName.getText().toString());
            }
        });


    }
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String message;
                    message = data.optString("data");
                    mChatAdapter.addMessage(message);
                }
            });
        }
    };

    // >=== start addition
    private Emitter.Listener onReceiptStatusUserOnline = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject obj = (JSONObject) args[0];
                    String user = obj.optString("data");
                    mContactAdapter.addUserOnline(user);
                }
            });
        }
    };

    private Emitter.Listener onReceiptStatusUserOffline = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject obj = (JSONObject) args[0];
                    String user = obj.optString("data");
                    mContactAdapter.removeUserOffline(user);
                }
            });
        }
    };

    private Emitter.Listener onGetListUser = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mContacts.clear();
                    JSONObject obj = (JSONObject) args[0];
                    JSONArray data = obj.optJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        mContacts.add(data.optString(i));
                    }
                    mContactAdapter = new ContactAdapter(mContacts);
                    mRecyclerViewContact.setAdapter(mContactAdapter);
                }
            });
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSocket.connect();
        mSocket.on("receiver_message", onNewMessage);
        mSocket.on("get_list_user", onGetListUser);
        mSocket.on("new_user_online", onReceiptStatusUserOnline);
        mSocket.on("user_disconnect", onReceiptStatusUserOffline);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mSocket.disconnect();
    }

    // <=== end addition
}