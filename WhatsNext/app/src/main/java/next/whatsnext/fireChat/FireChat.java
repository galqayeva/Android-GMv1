package next.whatsnext.fireChat;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.FirebaseDatabase;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import next.whatsnext.R;
import next.whatsnext.UserRecycler.UserRV;
import next.whatsnext.mainRecycler.MainActivity;

public class FireChat extends AppCompatActivity {

    View rootView;
    ImageView emojiButton;
    EmojiconEditText emojiconEditText;
    EmojIconActions emojIcon;

    String from;
    String to;

    private FirebaseListAdapter<ChatMessage> adapter;
    private String fromto;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_chat);

        rootView = findViewById(R.id.root_Chat);
        emojiButton = (ImageView) findViewById(R.id.emoji_btn);
        emojiconEditText = (EmojiconEditText) findViewById(R.id.emojicon_edit_text);

        emojIcon = new EmojIconActions(this, rootView, emojiconEditText, emojiButton);
        emojIcon.ShowEmojIcon();
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {

            }

            @Override
            public void onKeyboardClose() {

            }
        });

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("WhatsNext",0);
        username = prefs.getString("username","");

        Bundle b = getIntent().getExtras();

        assert b != null;
        int classint = b.getInt("classint");

        switch (classint){
            case 0:
              from = b.getString("from");
              to = b.getString("to");
                break;
        }
        fromto = from+"-"+to;

        switch (classint){
            case 1:
                fromto = b.getString("databkey");
                from = username;
                break;
        }




        displayChatMessages();




        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String inputStr = emojiconEditText.getText().toString().trim();

                if (!inputStr.equals("")){
                FirebaseDatabase.getInstance()
                        .getReference().child("messages").child(fromto)
                        .push()
                        .setValue(new ChatMessage(inputStr,
                                from)
                        );


                String user123;
                    if (fromto.contains("-"+username)){
                        user123 = fromto.replace("-"+username,"");}
                    else{user123 = fromto.replace(username+"-","");}
                    FirebaseDatabase.getInstance()
                            .getReference().child("notification").child(user123)
                            .push()
                            .setValue(new ChatMessage(inputStr,
                                    from)
                            );
                }
                        else {
                    emojiconEditText.setError("Boş mesaj göndərmək qadağandır!");
                }


                emojiconEditText.setText("");
            }
        });

    }


    private void displayChatMessages() {
        ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);

        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.item_in_message, FirebaseDatabase.getInstance().getReference().child("messages").child(fromto)) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {

                EmojiconTextView messageText = (EmojiconTextView) v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("HH:mm",
                        model.getMessageTime()));
            }

            @Override
            public View getView(int position, View view, ViewGroup viewGroup) {
                ChatMessage chatMessage = getItem(position);
                if (chatMessage.getMessageUser()!=null)
                if (chatMessage.getMessageUser().equals(username))
                    view = getLayoutInflater().inflate(R.layout.item_out_message, viewGroup, false);
                else
                    view = getLayoutInflater().inflate(R.layout.item_in_message, viewGroup, false);

                //generating view
                populateView(view, chatMessage, position);

                return view;
            }
        };

        listOfMessages.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        FireChat.this.overridePendingTransition(R.anim.fromleft, R.anim.toright);
    }
}
