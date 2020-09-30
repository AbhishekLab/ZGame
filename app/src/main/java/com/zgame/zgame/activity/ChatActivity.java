package com.zgame.zgame.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.zgame.zgame.R;
import com.zgame.zgame.fragment.ChatFragment;
import com.zgame.zgame.fragment.UserListInRoomFragment;

public class ChatActivity extends AppCompatActivity {
    private ChatFragment chatFragment;
    private UserListInRoomFragment userListInRoomFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        String toUid = getIntent().getStringExtra("toUid");
        final String roomID = getIntent().getStringExtra("roomID");
        String roomTitle = getIntent().getStringExtra("roomTitle");
        if (roomTitle!=null) {
            actionBar.setTitle(roomTitle);
        }

        // chatting area
        chatFragment = ChatFragment.getInstance(toUid, roomID);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragment, chatFragment )
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        chatFragment.backPressed();
        finish();;
    }

}
