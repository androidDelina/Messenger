package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private static final String EXTRA_CURRENT_USER_ID = "current_user_id";
    private static final String EXTRA_OTHER_USER_ID = "other_user_id";

    private TextView textViewTitle;
    private View onlineStatus;
    private RecyclerView recyclerViewMessangers;
    private EditText editTextMessage;
    private ImageView imageViewSend;


    private String currentUserId;
    private String otherUserId;

    private MessageAdapter messageAdapter;

    private ChatViewModel chatViewModel;
    private ChatViewModelFactory chatViewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initViews();

        currentUserId = getIntent().getStringExtra(EXTRA_CURRENT_USER_ID);
        otherUserId = getIntent().getStringExtra(EXTRA_OTHER_USER_ID);

        chatViewModelFactory = new ChatViewModelFactory(currentUserId, otherUserId);
        chatViewModel = new ViewModelProvider(this, chatViewModelFactory).get(ChatViewModel.class);

        messageAdapter = new MessageAdapter(currentUserId);
        recyclerViewMessangers.setAdapter(messageAdapter);

        imageViewSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textMessage = editTextMessage.getText().toString().trim();
                Message message = new Message(textMessage, currentUserId, otherUserId);
                Log.e("ChatActivity", message.toString());
                chatViewModel.sendMessage(message);
            }
        });

        List<Message> listMessage = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Message message = new Message("Text " + i, currentUserId, otherUserId);
            listMessage.add(message);
        }
        messageAdapter.setMessages(listMessage);

        observeViewModel();

        chatViewModel.getMessages().observe(this, new Observer<List<Message>>() {

            @Override
            public void onChanged(List<Message> messages) {
                messageAdapter.setMessages(messages);
                Log.e("!!!!!!!!!!!!!", "getMessageSent() TRUE");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        chatViewModel.setUserOnline(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        chatViewModel.setUserOnline(false);

    }

    private void observeViewModel() {
        chatViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error != null) {
                    Toast.makeText(ChatActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });



        chatViewModel.getMessageSent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSent) {
                if (isSent) {
                    editTextMessage.setText("");
                }
            }
        });

        chatViewModel.getOtherUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User otherUser) {
                if (otherUser != null) {
                    String userInfo = String.format(
                            "%s %s",
                            otherUser.getName(),
                            otherUser.getLastName()
                    );

                    textViewTitle.setText(userInfo);
                }

                int colorBackCircle;

                if (otherUser.getOnline())
                    colorBackCircle = R.drawable.circle_green;
                else
                    colorBackCircle = R.drawable.circle_red;

                Drawable background = ContextCompat.getDrawable(ChatActivity.this, colorBackCircle);
                onlineStatus.setBackground(background);
            }
        });
    }

    private void initViews() {
        textViewTitle = findViewById(R.id.textViewTitle);
        onlineStatus = findViewById(R.id.onlineStatus);
        recyclerViewMessangers = findViewById(R.id.recyclerViewMessangers);
        editTextMessage = findViewById(R.id.editTextMessage);
        imageViewSend = findViewById(R.id.imageViewSend);
    }

    public static Intent newIntent(Context context, String currentUserId, String otherUserId) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EXTRA_CURRENT_USER_ID, currentUserId);
        intent.putExtra(EXTRA_OTHER_USER_ID, otherUserId);
        return intent;
    }
}