package com.example.vmsv1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vmsv1.DataModel;
import com.example.vmsv1.R;
import com.example.vmsv1.db.DatabaseHelperSQL;
import com.example.vmsv1.ui.ResetPassword;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

    private Context context;
    private List<DataModel> dataList;
    private List<String> headers;
    private boolean button_presence;
    private String button_name;
    private DatabaseHelperSQL db;
    private String userId;

    public GridAdapter(Context context, List<DataModel> dataList, List<String> headers, boolean button_presence, String button_name, DatabaseHelperSQL db, String userId) {
        this.context = context;
        this.dataList = dataList;
        this.headers = headers;
        this.button_presence = button_presence;
        this.button_name = button_name;
        this.db = db;
        this.userId = userId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataModel currentItem = dataList.get(position);
        Map<String, Object> dataMap = currentItem.getData();

        // Set always visible fields
        holder.primaryField1.setText(String.valueOf(dataMap.get(headers.get(0))));
        holder.primaryField2.setText(String.valueOf(dataMap.get(headers.get(1))));

        // Set expandable fields
        for (int i = 2; i < headers.size(); i++) {
            TextView fieldText = holder.expandableFields.get(i - 2);
            fieldText.setText(headers.get(i) + ": " + String.valueOf(dataMap.get(headers.get(i))));
        }

        // Set button if present
        if (button_presence) {
            holder.actionButton.setText(button_name);
            holder.actionButton.setVisibility(View.VISIBLE);
            setupButtonClickListener(holder.actionButton, currentItem);
        } else {
            holder.actionButton.setVisibility(View.GONE);
        }

        // Set expand/collapse listener
        holder.expandButton.setOnClickListener(v -> {
            boolean isExpanded = holder.expandableLayout.getVisibility() == View.VISIBLE;
            holder.expandableLayout.setVisibility(isExpanded ? View.GONE : View.VISIBLE);
            holder.expandButton.setImageResource(isExpanded ? R.drawable.ic_expand_more : R.drawable.ic_expand_less);
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private void setupButtonClickListener(Button button, DataModel data) {
        if (button_name.equals("Delete")) {
            button.setOnClickListener(v -> {
                String number_to_be_removed = String.valueOf(data.getData().get("Mobile Number"));
                int position = dataList.indexOf(data);
                if (position != -1) {
                    dataList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, dataList.size());
                }
                int count = db.deleteVisitorFromBlackList(number_to_be_removed, Integer.parseInt(userId));
                Toast.makeText(context, "Deleted rows=" + count, Toast.LENGTH_SHORT).show();
            });
        } else if (button_name.equals("Reset Password")) {
            button.setOnClickListener(v -> {
                String userIdToBeChanged = String.valueOf(data.getData().get(headers.get(0)));
                Intent intent = new Intent(context, ResetPassword.class);
                intent.putExtra("userId_admin", userId);
                intent.putExtra("userId_to_be_changed", userIdToBeChanged);
                context.startActivity(intent);
            });
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView primaryField1;
        TextView primaryField2;
        ImageButton expandButton;
        LinearLayout expandableLayout;
        List<TextView> expandableFields;
        Button actionButton;

        ViewHolder(View view) {
            super(view);
            primaryField1 = view.findViewById(R.id.primaryField1);
            primaryField2 = view.findViewById(R.id.primaryField2);
            expandButton = view.findViewById(R.id.expandButton);
            expandableLayout = view.findViewById(R.id.expandableLayout);
            actionButton = view.findViewById(R.id.actionButton);

            expandableFields = new ArrayList<>();
            for (int i = 0; i < 10; i++) { // Adjust this number based on your maximum possible fields
                TextView field = new TextView(view.getContext());
                expandableLayout.addView(field);
                expandableFields.add(field);
            }
        }
    }

    // Method to update data
    public void updateData(List<DataModel> newData) {
        this.dataList = newData;
        notifyDataSetChanged();
    }
}