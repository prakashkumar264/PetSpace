package edu.neu.madcourse.petspace;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ChatMessageAdapter extends BaseAdapter {

    List<ChatMessage> messages = new ArrayList<ChatMessage>();
    Context context;

    public ChatMessageAdapter(Context context) {
        this.context = context;
    }


    public void add(ChatMessage message) {
        this.messages.add(message);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        MessageViewHolder holder = new MessageViewHolder();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        ChatMessage message = messages.get(i);

        if (message.isBelongsToCurrentUser()) { // this message was sent by us so let's create a basic chat bubble on the right
            convertView = messageInflater.inflate(R.layout.chat_my_message, null);
            holder.avatar = (View) convertView.findViewById(R.id.post_profile_img);
            holder.name = (TextView) convertView.findViewById(R.id.post_user_name);
            holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
            convertView.setTag(holder);
            holder.name.setText(message.getMemberData().getName());
            holder.messageBody.setText(message.getText());


        } else {
            convertView = messageInflater.inflate(R.layout.chat_their_message, null);
            holder.avatar = (View) convertView.findViewById(R.id.post_profile_img);
            holder.name = (TextView) convertView.findViewById(R.id.post_user_name);
            holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
            convertView.setTag(holder);
            holder.name.setText(message.getMemberData().getName());
            holder.messageBody.setText(message.getText());
        }

        return convertView;
    }

}

class MessageViewHolder {
    public View avatar;
    public TextView name;
    public TextView messageBody;
}