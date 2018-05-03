package com.cloudgroupx007.flashchatnewfirebase;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainChatActivity extends AppCompatActivity {

    // TODO: Add member variables here:
    private String mDisplayName;
    private ListView mChatListView;
    private EditText mInputText;
    private ImageButton mSendButton;
    private DatabaseReference mDatabaseReference;  /*this is a *database object*/
    private ChatListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);



        // TODO: Set up the display name and get the Firebase reference

        setupDisplayName();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        /* this mDatabaseReference needs to get a Firebase database object,
        then we use 'getReference()' on the FirebaseDatabase to obtain a database reference,
        Review documentation: of firebase.google.com/docs/reference/android/com/google/firebase/database/FirebaseDatabase  */

        // Link the Views in the layout to the Java code
        mInputText = (EditText) findViewById(R.id.messageInput);
        mSendButton = (ImageButton) findViewById(R.id.sendButton);
        mChatListView = (ListView) findViewById(R.id.chat_list_view);

        // TODO: Send the message when the "enter" button is pressed

        mInputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                sendMessage();
                return true; /* 'return true' to indicate that the event has been handled  */
            }
        });

        // TODO: Add an OnClickListener to the sendButton to send a message

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();

               /*the above lines of code make it the send button send messages on your soft keyboard*/
            }
        });

    }

    // TODO: Retrieve the display name from the Shared Preferences////////////////////

    private void setupDisplayName(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDisplayName = user.getDisplayName();

   /* below is replaced by above */

   // SharedPreferences prefs = getSharedPreferences(RegisterActivity.CHAT_PREFS, MODE_PRIVATE); /* you can have multiple shared preferences inside the same app*/

   // mDisplayName = prefs.getString(RegisterActivity.DISPLAY_NAME_KEY, null); /* the 'getString' method required a key*/

    // if (mDisplayName == null) mDisplayName = "Anonymous";

    }



    private void sendMessage() {

        Log.d("FlashChat", "I sent something");
        // TODO: Grab the text the user typed in and push the message to Firebase
        String input = mInputText.getText().toString(); /* this line grabs what the user typed in and stored in the 'mInputText', via the .toString method();*/
        if (!input.equals("")) {  /* This is saying if not empty then  */
                InstantMessage chat = new InstantMessage(input, mDisplayName);
                mDatabaseReference.child("messages").push().setValue(chat);
                mInputText.setText("");  /* It would be silly to leave the same text after sending the message right?
                                            so lets make sure it's empty with this code to ensure the user can
                                            have a clean slate to text again, minor but important */

                /* With this line of code we are saving an individual chat message to the database
                   -we are specifying here that we are storing all our messages in a place we call/establish as
                   "messages" ...ironically. Then we use a push method to reference this child location.
                   -Then finally we use 'setValue(chat)'. The heavy-lifting is done by the mDatabaseReference object
                    and the 'child' is where the data is saved, and the 'setValue' is the instruction that commits
                    the data to the database.*/

        }

    }

    // TODO: Override the onStart() lifecycle method. Setup the adapter here. Remembers, onStart comes right after onCreate in the MVC lifecycle
        @Override
        public void onStart() {
        super.onStart();
        mAdapter = new ChatListAdapter(this, mDatabaseReference,mDisplayName);
        mChatListView.setAdapter(mAdapter); /* The Listview now knows which adaptor it should talk */

        }

    @Override
    public void onStop() {
        super.onStop();

        // TODO: Remove the Firebase event listener on the adapter.
        mAdapter.cleanup();

    }

}
