package com.example.vmsv1.ui;


import android.content.res.Resources;
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
import com.example.vmsv1.ItemDomain;
import com.example.vmsv1.R;
import com.example.vmsv1.db.DatabaseHelperSQL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DataModel> dataList;
    private final List<String> headers;
    private final boolean button_presence;
    private final String button_name;

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;
    DatabaseHelperSQL db;
    String userId;

    public TableAdapter(List<DataModel> dataList, List<String> headers, boolean button_presence, String button_name,DatabaseHelperSQL db,String userId) {
        this.dataList = dataList;
        this.headers = headers;
        this.button_presence=button_presence;
        this.button_name=button_name;
        this.db=db;
        this.userId=userId;
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
                textView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                textView.setText(header);
                textView.setTextColor(headerLayout.getResources().getColor(android.R.color.black));
                textView.setBackgroundColor(headerLayout.getResources().getColor(android.R.color.white));
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
            if(button_presence)
            {
                for (int i=0;i<headers.size()-1;i++)
                {
                    String header=headers.get(i);
                    Log.d("Act1","header="+header);
                    TextView textView = new TextView(itemLayout.getContext());
                    textView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                    textView.setText(String.valueOf(dataMap.get(header)));
                    Log.d("Act1","value set="+String.valueOf(dataMap.get(header)));
                    itemLayout.addView(textView);
                }

                Button button = new Button(itemLayout.getContext());
                button.setText(button_name);
                if(button_name.equals("Delete"))
                {
                    button.setOnClickListener(v -> {

                        int position = getAdapterPosition();
                        String number_to_be_removed=String.valueOf(dataList.get(position-1).getData().get("Mobile Number"));
                        if (position != RecyclerView.NO_POSITION) {
                            dataList.remove(position - 1); // Offset for header
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, dataList.size() + 1);
                        }
                        int count=db.deleteVisitorFromBlackList(number_to_be_removed,Integer.parseInt(userId));
                        Toast.makeText(itemView.getContext(), "Deleted rows="+count,Toast.LENGTH_SHORT).show();
                    });
                }
                else if(button_name.equals("Reset Password"))
                {
                    button.setOnClickListener(v -> {

                    });
                }
                itemLayout.addView(button);
            }
            else
            {
                for (int i=0;i<headers.size();i++)
                {
                    String header=headers.get(i);
                    Log.d("Act1","header="+header);
                    TextView textView = new TextView(itemLayout.getContext());
                    textView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                    textView.setText(String.valueOf(dataMap.get(header)));
                    Log.d("Act1","value set="+String.valueOf(dataMap.get(header)));
                    itemLayout.addView(textView);
                }
            }
        }
    }
}
