package com.example.androidexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableViewHolder> {

    private final List<TableItem> tableItems;

    public TableAdapter(List<TableItem> tableItems) {
        this.tableItems = tableItems;
    }

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_item, parent, false);
        return new TableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
        TableItem item = tableItems.get(position);
        holder.reviewCheckBox.setChecked(item.isReviewed());
        holder.nameOfPoster.setText(item.getNameOfPoster());
        holder.reportedBy.setText(item.getReportedBy());
    }

    @Override
    public int getItemCount() {
        return tableItems.size();
    }

    static class TableViewHolder extends RecyclerView.ViewHolder {
        CheckBox reviewCheckBox;
        TextView nameOfPoster, reportedBy;

        public TableViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewCheckBox = itemView.findViewById(R.id.reviewCheckBox);
            nameOfPoster = itemView.findViewById(R.id.nameOfPoster);
            reportedBy = itemView.findViewById(R.id.reportedBy);
        }
    }
}
