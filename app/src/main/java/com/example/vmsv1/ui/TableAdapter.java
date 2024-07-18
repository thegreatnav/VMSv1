package com.example.vmsv1.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vmsv1.DataModel;
import com.example.vmsv1.R;
import com.example.vmsv1.db.DatabaseHelperSQL;
import java.util.List;
import java.util.Map;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ItemViewHolder> {

    private static List<DataModel> dataList;
    private final List<String> headers;
    private static boolean button_presence = false;
    private static String button_name = "";
    private DatabaseHelperSQL db;
    private final DeleteVisitorCallback deleteVisitorCallback;
    private final AddVisitorCallback addVisitorCallback;

    public TableAdapter(List<DataModel> dataList, List<String> headers, boolean button_presence, String button_name, DatabaseHelperSQL db, DeleteVisitorCallback deleteVisitorCallback, AddVisitorCallback addVisitorCallback) {
        this.dataList = dataList;
        this.headers = headers;
        this.button_presence = button_presence;
        this.button_name = button_name;
        this.db = db;
        this.deleteVisitorCallback = deleteVisitorCallback;
        this.addVisitorCallback = addVisitorCallback;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table_row, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(dataList.get(position), headers);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
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
            for (String header : headers) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView textView = new TextView(itemView.getContext());
                textView.setLayoutParams(params);
                textView.setPadding(20, 20, 20, 20);
                textView.setText(String.valueOf(dataMap.get(header)));
                itemLayout.addView(textView);
            }

            if (button_presence) {
                Button button = new Button(itemView.getContext());
                button.setText(button_name);
                button.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    String mobileNoToDelete = String.valueOf(dataList.get(position).getData().get("Mobile Number"));
                    dataList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, dataList.size());

                    // Call onDeleteVisitor callback
                    if (deleteVisitorCallback != null) {
                        deleteVisitorCallback.onDeleteVisitor(mobileNoToDelete);
                    }
                });
                itemLayout.addView(button);
            }
        }
    }

    public interface DeleteVisitorCallback {
        void onDeleteVisitor(String mobileNo);
    }

    public interface AddVisitorCallback {
        void onAddVisitor(String mobileNo, String name, String reason, String dateAdded);
    }
}
