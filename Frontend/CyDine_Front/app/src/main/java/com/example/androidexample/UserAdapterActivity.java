package com.example.androidexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

/**
 * Adapter class for managing a list of users in a RecyclerView.
 * Displays user details such as name and email, along with a selection checkbox.
 */
public class UserAdapterActivity extends RecyclerView.Adapter<UserAdapterActivity.UserViewHolder> {
    private List<Map<String, Object>> userList;
    private OnUserSelectionChangedListener selectionChangedListener;

    /**
     * Constructor for the UserAdapterActivity.
     *
     * @param userList                The list of users to display in the RecyclerView.
     * @param selectionChangedListener Callback interface to handle user selection changes.
     */
    public UserAdapterActivity(List<Map<String, Object>> userList, OnUserSelectionChangedListener selectionChangedListener) {
        this.userList = userList;
        this.selectionChangedListener = selectionChangedListener;
    }

    /**
     * Creates a new ViewHolder when there are no existing ViewHolders available for reuse.
     *
     * @param parent   The parent ViewGroup into which the new View will be added.
     * @param viewType The view type of the new View.
     * @return A new UserViewHolder instance.
     */
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_row_item, parent, false);
        return new UserViewHolder(view);
    }

    /**
     * Binds data to the ViewHolder at the specified position.
     *
     * @param holder   The ViewHolder to bind data to.
     * @param position The position of the item within the data set.
     */
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Map<String, Object> user = userList.get(position);
        holder.nameTextView.setText((String) user.get("name"));
        holder.emailTextView.setText((String) user.get("email"));
        holder.passwordTextView.setText("••••••••");
        holder.userCheckBox.setChecked((Boolean) user.get("selected"));

        // Handle checkbox selection
        holder.userCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            user.put("selected", isChecked);
            selectionChangedListener.onUserSelectionChanged();
        });
    }

    /**
     * Returns the total number of items in the data set.
     *
     * @return The total number of items.
     */
    @Override
    public int getItemCount() {
        return userList.size();
    }

    /**
     * ViewHolder class for user items in the RecyclerView.
     * Holds references to the views displaying user data and the selection checkbox.
     */
    static class UserViewHolder extends RecyclerView.ViewHolder {
        CheckBox userCheckBox;
        TextView nameTextView;
        TextView emailTextView;
        TextView passwordTextView;

        /**
         * Constructor for UserViewHolder.
         *
         * @param itemView The root view of the individual list item.
         */
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userCheckBox = itemView.findViewById(R.id.user_checkbox);
            nameTextView = itemView.findViewById(R.id.user_name);
            emailTextView = itemView.findViewById(R.id.user_email);
            passwordTextView = itemView.findViewById(R.id.user_password);
        }
    }

    /**
     * Interface for handling changes in user selection.
     */
    interface OnUserSelectionChangedListener {
        /**
         * Callback method invoked when the selection state of a user changes.
         */
        void onUserSelectionChanged();
    }
}
