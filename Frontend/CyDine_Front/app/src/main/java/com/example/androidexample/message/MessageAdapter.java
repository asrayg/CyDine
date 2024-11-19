package com.example.androidexample.message;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidexample.R;

import java.util.List;

/**
 * Adapter for displaying messages in a RecyclerView with actions for reporting, liking, and disliking.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<String> messageList;

    /**
     * Constructor for the MessageAdapter.
     *
     * @param messageList A list of strings representing the messages.
     */
    public MessageAdapter(List<String> messageList) {
        this.messageList = messageList;
    }

    /**
     * Called when a new ViewHolder is created.
     *
     * @param parent   The parent ViewGroup into which the new View will be added.
     * @param viewType The view type of the new View.
     * @return A new instance of {@link MessageViewHolder}.
     */
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

    /**
     * Binds data to the ViewHolder at the specified position.
     *
     * @param holder   The ViewHolder to bind data to.
     * @param position The position of the item within the data set.
     */
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

    /**
     * Returns the total number of items in the data set.
     *
     * @return The size of the message list.
     */
    @Override
    public int getItemCount() {
        return messageList.size();
    }

    /**
     * ViewHolder class for individual message items.
     * Holds references to the views for displaying and interacting with messages.
     */
    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMessage;
        TextView reportText;
        TextView likeButton;
        TextView dislikeButton;

        /**
         * Constructor for the MessageViewHolder.
         *
         * @param itemView The root view of the individual list item.
         */
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
            reportText = itemView.findViewById(R.id.reportText);
            likeButton = itemView.findViewById(R.id.likeButton);
            dislikeButton = itemView.findViewById(R.id.dislikeButton);
        }
    }
}
