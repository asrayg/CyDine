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

public class UserAdapterActivity extends RecyclerView.Adapter<UserAdapterActivity.UserViewHolder> {
    private List<Map<String, Object>> userList;
    private OnUserSelectionChangedListener selectionChangedListener;

    public UserAdapterActivity(List<Map<String, Object>> userList, OnUserSelectionChangedListener selectionChangedListener) {
        this.userList = userList;
        this.selectionChangedListener = selectionChangedListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_row_item, parent, false);
        return new UserViewHolder(view);
    }

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

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        CheckBox userCheckBox;
        TextView nameTextView;
        TextView emailTextView;
        TextView passwordTextView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userCheckBox = itemView.findViewById(R.id.user_checkbox);
            nameTextView = itemView.findViewById(R.id.user_name);
            emailTextView = itemView.findViewById(R.id.user_email);
            passwordTextView = itemView.findViewById(R.id.user_password);
        }
    }

    // Interface for selection change callback
    interface OnUserSelectionChangedListener {
        void onUserSelectionChanged();
    }
}
