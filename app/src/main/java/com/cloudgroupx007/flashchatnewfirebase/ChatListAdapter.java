package com.cloudgroupx007.flashchatnewfirebase;

/* This class was created to have one job,
   have the adaptor provide the data from the cloud, and to display it on the Listview */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/* Because we are going to use 'extends' this class to the 'BaseAdapter' class from google (in the class
    declaration). We can now inherit all of the methods and fields from this parent superclass.
    In addition, as can use it as a template, as the 'BaseAdapter' will be regarded as the superclass
    and the 'ChatListAdaptor' is now a child/subclass of the superclass. */

/* Note that the BaseAdapter super class is underlined red and that is because it is 'abstract'
   if you hover your mouse over it. So it's asking you to provide the correct code/methods in your
    subclass to finish it properly. According to lecture, you can just hover over the declaration
    and click the red light bulb and implement all the necessary methods
    (selecting all 4 of the mentioned items/methods in the pop-up window, [that are considered unfinished])
     and clicking ok, as you did below. */

public class ChatListAdapter extends BaseAdapter {

    private Activity mActivity; /* Need a field for our activity */
    private DatabaseReference mDatabaseReference;
    private String mDisplayName;
    private ArrayList<DataSnapshot> mSnapshotList;
    /* ^^ DataSnapshot, is just a type (extended from BaseAdapter superclass from google Firebase,)
          used from Firebase to pass our data from the cloud database to your app. The angled brackets '<>'
           simply signify what kind of data will be stored, in the case above, a 'DataSnapshot' will be
           stored.*/

    /* Fyi, Arrays are fixed and ArrayList are flexible, you can add or subtract items from an ArrayList. */


    /* Below, all the @Override callbacks are automatically added when typing in the 'ChildEventListener' method
       which is a method from the Superclass(BaseAdapter)*/

    private ChildEventListener mListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            mSnapshotList.add(dataSnapshot);
            notifyDataSetChanged();

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    /*We've added member variables, now add the constructor below. The constructor in this case,
    creates and configures the ChatListAdapter object in this case */

    public ChatListAdapter(Activity activity, DatabaseReference ref, String name) {

        mActivity = activity;
        mDisplayName = name;
        mDatabaseReference = ref.child("messages"); /*this is referring to the 'messages' location in our database*/
        mDatabaseReference.addChildEventListener(mListener); /*code that retrieves data from Firebase*/

        mSnapshotList = new ArrayList<>();  /*we are creating mShapshotList from scratch by calling the constructor of the array list*/
    }
    /*We will populate the member variables using the INPUTS from the constructor*/


    /*Remember how we set an individual row in the chat will have it's own layout?
      We will now create a little helper class that acts as a nice little package for an individual row */

    static class ViewHolder { /* The ViewHolder class will hold all our chat messages in a single row, this class is referred to as an "inner class"*/
        TextView authorName;
        TextView body;
        LinearLayout.LayoutParams params;
    }



    @Override
    public int getCount() { /* asking how may items in the arrayList*/
        return mSnapshotList.size(); /* grabs the the count of chat messages/number of elements continuously  */
    }

    @Override
    public InstantMessage getItem(int position) {  /*this was changed from 'Object' to 'InstantMessage' type in order to satisfy final 'InstantMessage' below
                                                     also, we can safely do this as we are only storing/using the 'InstantMessage' type object.
                                                     Now that the types match, the red error should disappear at the bottom final 'InstantMessage...*/

        DataSnapshot snapshot = mSnapshotList.get(position);
        return snapshot.getValue(InstantMessage.class); /* extracting snapshot and converts the JSON into an object */
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {  /* this line will check if an existing row can be reused */

        if (convertView == null) {  /* this means if it equals null, we will create a new row from scratch, which is very resource costly, an right below is the code to configure this new said row*/
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chat_msg_row, parent, false);
            /* The inflate method is just android jargo for, "hey pass the XML file over here"
               the inflate method creates a new View for the user, and puts your 'message' from the app here */
            final ViewHolder holder = new ViewHolder();
            holder.authorName = (TextView) convertView.findViewById(R.id.author);
            holder.body = (TextView) convertView.findViewById(R.id.message);
            holder.params = (LinearLayout.LayoutParams) holder.authorName.getLayoutParams();
            convertView.setTag(holder);
              /* This 'setTag' method allows you to reuse the Viewholder without having to use R.id which is costly and laggy to the user*/
                    /* This inner class will hold on to all the things that make up an individual chat message row.
                       Now we are linking the fields of the ViewHolder to the view of the chat message row. */

        }

       final InstantMessage message = getItem(position);
       /*this is to actually display the author and message of the instant message,
         fyi, position means 0 will = first time in list, and 1 will = second item in list etc.
         so were saying here to get the instant message at the current position within the list.*/

       final ViewHolder holder = (ViewHolder) convertView.getTag();
       /*this is retrieving the view that was save in the convertView..but it will have old data in it
         so we have to add the following below*/

       boolean isMe = message.getAuthor().equals(mDisplayName);
       setChatRowAppearance(isMe, holder);

        String author = message.getAuthor();
        holder.authorName.setText(author);

        String msg = message.getMessage();
        holder.body.setText(msg);

        /*^^ This basically means when your user is scrolling up and down the list, no new row layouts have to be
            created unnecessarily. The code in your getView method now updates the contents in the list as the
            row scrolls into view */


        return convertView;   /* however if it is not equal to null, we will just return the convertview */
    }

    private void setChatRowAppearance(boolean isItMe, ViewHolder holder) {

        if (isItMe) {
            holder.params.gravity = Gravity.END;
            holder.authorName.setTextColor(Color.GREEN);
            holder.body.setBackgroundResource(R.drawable.bubble2);
            }

            else {
            holder.params.gravity = Gravity.START;
            holder.authorName.setTextColor(Color.BLUE);
            holder.body.setBackgroundResource(R.drawable.bubble1);
            }

        holder.authorName.setLayoutParams(holder.params);
        holder.body.setLayoutParams(holder.params);
    }


    public void cleanup() {

        mDatabaseReference.removeEventListener(mListener); /*stops using unnecessary resources*/
    }

}
