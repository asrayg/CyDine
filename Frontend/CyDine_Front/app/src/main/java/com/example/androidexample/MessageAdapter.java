package com.example.androidexample;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<String> messageList;

    public MessageAdapter(List<String> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }
    public void MessageViewHolder(@NonNull View itemView) {
        View textViewMessage = itemView.findViewById(R.id.textViewMessage);
        View reportText = itemView.findViewById(R.id.reportText);
        View likeButton = itemView.findViewById(R.id.likeButton);
        View dislikeButton = itemView.findViewById(R.id.dislikeButton);

        Log.d("MessageAdapter", "textViewMessage: " + textViewMessage);
        Log.d("MessageAdapter", "reportText: " + reportText);
        Log.d("MessageAdapter", "likeButton: " + likeButton);
        Log.d("MessageAdapter", "dislikeButton: " + dislikeButton);
    }


    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        String message = messageList.get(position);
        holder.textViewMessage.setText(message);

        holder.reportText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle report action
            }
        });

        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle like action
            }
        });

        holder.dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle dislike action
            }
        });
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMessage;
        TextView reportText;
        TextView likeButton;
        TextView dislikeButton;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
            reportText = itemView.findViewById(R.id.reportText);
            likeButton = itemView.findViewById(R.id.likeButton);
            dislikeButton = itemView.findViewById(R.id.dislikeButton);
        }
    }
}
