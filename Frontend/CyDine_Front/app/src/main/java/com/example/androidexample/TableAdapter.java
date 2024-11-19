package com.example.androidexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Adapter class for managing and displaying a list of {@link TableItem} objects in a RecyclerView.
 */
public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableViewHolder> {

    private final List<TableItem> tableItems;

    /**
     * Constructor for creating a new TableAdapter.
     *
     * @param tableItems The list of {@link TableItem} objects to be displayed.
     */
    public TableAdapter(List<TableItem> tableItems) {
        this.tableItems = tableItems;
    }

    /**
     * Creates a new ViewHolder when there are no existing ViewHolders available for reuse.
     *
     * @param parent   The parent ViewGroup into which the new View will be added.
     * @param viewType The view type of the new View.
     * @return A new instance of {@link TableViewHolder}.
     */
    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_item, parent, false);
        return new TableViewHolder(view);
    }

    /**
     * Binds data to the ViewHolder at the specified position.
     *
     * @param holder   The ViewHolder to bind data to.
     * @param position The position of the item within the data set.
     */
    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
        TableItem item = tableItems.get(position);
        holder.reviewCheckBox.setChecked(item.isReviewed());
        holder.nameOfPoster.setText(item.getNameOfPoster());
        holder.reportedBy.setText(item.getReportedBy());
    }

    /**
     * Returns the total number of items in the data set.
     *
     * @return The total number of items.
     */
    @Override
    public int getItemCount() {
        return tableItems.size();
    }

    /**
     * ViewHolder class for individual items in the RecyclerView.
     * Holds references to views for displaying item data.
     */
    static class TableViewHolder extends RecyclerView.ViewHolder {
        CheckBox reviewCheckBox;
        TextView nameOfPoster, reportedBy;

        /**
         * Constructor for TableViewHolder.
         *
         * @param itemView The root view of the individual list item.
         */
        public TableViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewCheckBox = itemView.findViewById(R.id.reviewCheckBox);
            nameOfPoster = itemView.findViewById(R.id.nameOfPoster);
            reportedBy = itemView.findViewById(R.id.reportedBy);
        }
    }
}
