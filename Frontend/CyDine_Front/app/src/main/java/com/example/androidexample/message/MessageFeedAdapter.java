package com.example.androidexample.message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidexample.R;

import java.util.List;

/**
 * Adapter for displaying a list of messages in a RecyclerView.
 */
public class MessageFeedAdapter extends RecyclerView.Adapter<MessageFeedAdapter.MessageViewHolder> {

    private final List<String> messages;

    /**
     * Constructor for the MessageFeedAdapter.
     *
     * @param messages A list of strings representing the messages to be displayed.
     */
    public MessageFeedAdapter(List<String> messages) {
        this.messages = messages;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_feed_item, parent, false);
        return new MessageViewHolder(view);
    }

    /**
     * Binds data to the ViewHolder at the specified position.
     *
     * @param holder   The ViewHolder to bind data to.
     * @param position The position of the item within the data set.
     */
    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.messageText.setText(messages.get(position));
    }

    /**
     * Returns the total number of messages in the data set.
     *
     * @return The size of the message list.
     */
    @Override
    public int getItemCount() {
        return messages.size();
    }

    /**
     * ViewHolder class for individual message items in the RecyclerView.
     * Holds references to the views for displaying message data.
     */
    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        /**
         * Constructor for the MessageViewHolder.
         *
         * @param itemView The root view of the individual list item.
         */
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
        }
    }
}
