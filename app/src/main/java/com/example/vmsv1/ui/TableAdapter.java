package com.example.vmsv1.ui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vmsv1.DataModel;
import com.example.vmsv1.R;
import com.example.vmsv1.db.DatabaseHelperSQL;

import java.util.List;
import java.util.Map;

public class TableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DataModel> dataList;
    private final List<String> headers;
    private final boolean buttonPresence;
    private final String buttonName;
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;
    private final DatabaseHelperSQL db;
    private final String userId;

    public TableAdapter(List<DataModel> dataList, List<String> headers, boolean buttonPresence, String buttonName, DatabaseHelperSQL db, String userId) {
        this.dataList = dataList;
        this.headers = headers;
        this.buttonPresence = buttonPresence;
        this.buttonName = buttonName;
        this.db = db;
        this.userId = userId;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table_row, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_HEADER) {
            ((HeaderViewHolder) holder).bind(headers);
        } else {
            DataModel data = dataList.get(position - 1); // Offset for header
            ((ItemViewHolder) holder).bind(data, headers);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size() + 1; // +1 for header
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        LinearLayout headerLayout;

        HeaderViewHolder(View itemView) {
            super(itemView);
            headerLayout = itemView.findViewById(R.id.headerLayout);
        }

        void bind(List<String> headers) {
            headerLayout.removeAllViews();
            for (String header : headers) {
                TextView textView = new TextView(headerLayout.getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1f
                );
                textView.setLayoutParams(params);
                textView.setText(header);
                textView.setTextColor(headerLayout.getResources().getColor(android.R.color.black));
                textView.setBackgroundColor(headerLayout.getResources().getColor(android.R.color.white));
                textView.setPadding(8, 8, 8, 8); // Padding for better readability
                headerLayout.addView(textView);
            }
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout itemLayout;

        ItemViewHolder(View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.itemLayout);
        }

        void bind(DataModel data, List<String> headers) {
            itemLayout.removeAllViews();
            Map<String, Object> dataMap = data.getData();
            if (buttonPresence) {
                for (int i = 0; i < headers.size() - 1; i++) {
                    String header = headers.get(i);
                    TextView textView = new TextView(itemLayout.getContext());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            0,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            1f
                    );
                    textView.setLayoutParams(params);
                    textView.setText(String.valueOf(dataMap.get(header)));
                    textView.setPadding(8, 8, 8, 8);
                    itemLayout.addView(textView);
                }

                Button button = new Button(itemLayout.getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1f
                );
                button.setLayoutParams(params);
                button.setText(buttonName);
                setupButtonClickListener(button, data);
                itemLayout.addView(button);
            } else {
                for (String header : headers) {
                    TextView textView = new TextView(itemLayout.getContext());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            0,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            1f
                    );
                    textView.setLayoutParams(params);
                    textView.setText(String.valueOf(dataMap.get(header)));
                    textView.setPadding(8, 8, 8, 8);
                    itemLayout.addView(textView);
                }
            }
        }

        private void setupButtonClickListener(Button button, DataModel data) {
            if (buttonName.equals("Delete")) {
                button.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    String numberToBeRemoved = String.valueOf(dataList.get(position - 1).getData().get("Mobile Number"));
                    if (position != RecyclerView.NO_POSITION) {
                        dataList.remove(position - 1); // Offset for header
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, dataList.size() + 1);
                    }
                    int count = db.deleteVisitorFromBlackList(numberToBeRemoved, Integer.parseInt(userId));
                    Toast.makeText(itemView.getContext(), "Deleted rows=" + count, Toast.LENGTH_SHORT).show();
                });
            } else if (buttonName.equals("Reset Password")) {
                button.setOnClickListener(v -> {
                    Context context = itemView.getContext();
                    String userIdToBeChanged = String.valueOf(data.getData().get(headers.get(0)));
                    Intent intent = new Intent(context, ResetPassword.class);
                    intent.putExtra("userId_admin", userId);
                    intent.putExtra("userId_to_be_changed", userIdToBeChanged);
                    context.startActivity(intent);
                });
            }
        }
    }
}
