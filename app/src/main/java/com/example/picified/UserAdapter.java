package com.example.picified;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.picified.Activities.InChatActivity;
import com.example.picified.Classes.User;
import com.example.picified.R;

import java.util.ArrayList;

public class UserAdapter extends BaseAdapter {

    Context context;
    private ArrayList<User>users;
    LayoutInflater mInflater;

    public UserAdapter(Context context,  ArrayList<User> users) { //Constructor for Users with the Context and our userslist.
        this.context = context;
        this.users = users;
    }

    private class ViewHolder {
        TextView userName;
        ImageView profilePicture;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final User USER = users.get(position);
        ViewHolder holder = null;
        mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        holder = new ViewHolder();

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_user_chat_list, parent, false);

            holder.userName = (TextView) convertView.findViewById(R.id.text_chat_user_list);
            holder.profilePicture = (ImageView) convertView.findViewById(R.id.image_chat_user_list);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InChatActivity.class);
                intent.putExtra("username", USER.getName());
                intent.putExtra("userpicture", USER.getProfile_picture());
                //Deliver all userdata here
                context.startActivity(intent);
            }
        });

        User user_pos = users.get(position);
        holder.userName.setText(user_pos.getName());
        Glide.with(context).load(USER.getProfile_picture()).into(holder.profilePicture);

        return convertView;
    }
}
