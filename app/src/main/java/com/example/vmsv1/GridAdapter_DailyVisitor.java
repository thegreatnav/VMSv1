package com.example.vmsv1;

import static android.graphics.Color.rgb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.vmsv1.dataitems.VisitorSearchResult;

import org.w3c.dom.Text;

import java.util.List;

public class GridAdapter_DailyVisitor extends BaseAdapter {

    private Context context;
    private List<VisitorSearchResult> itemList;

    public GridAdapter_DailyVisitor(Context context, List<VisitorSearchResult> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.gridview_element_dailyvisitor, parent, false);
        }

        TextView visitor_name_text=convertView.findViewById(R.id.visitor_name_text);
        TextView visitor_name=convertView.findViewById(R.id.visitor_name_textview);
        TextView mobile_number_text=convertView.findViewById(R.id.mobile_number_text);
        TextView mob_no=convertView.findViewById(R.id.mob_no_textview);

        TextView visitor_id_text=convertView.findViewById(R.id.visitor_id_text);
        TextView visitor_id= convertView.findViewById(R.id.visitor_id_textview);
        TextView designation_text=convertView.findViewById(R.id.designation_text);
        TextView visitor_designation=convertView.findViewById(R.id.designation_textview);
        TextView place_text=convertView.findViewById(R.id.place_text);
        TextView visitor_place=convertView.findViewById(R.id.place_textview);
        TextView company_text=convertView.findViewById(R.id.company_text);
        TextView visitor_company=convertView.findViewById(R.id.company_textview);

        TextView visitor_type_text=convertView.findViewById(R.id.visitor_type_text);
        TextView visitor_type=convertView.findViewById(R.id.visitor_type_textview);
        TextView visiting_staff_text=convertView.findViewById(R.id.visiting_staff_text);
        TextView visiting_staff=convertView.findViewById(R.id.visiting_staff_textview);
        TextView approver_name_text=convertView.findViewById(R.id.approver_name_text);
        TextView approver_name=convertView.findViewById(R.id.approver_name_textview);
        TextView purpose_text=convertView.findViewById(R.id.purpose_text);
        TextView purpose=convertView.findViewById(R.id.purpose_textview);
        TextView visiting_area_text=convertView.findViewById(R.id.visiting_area_text);
        TextView visiting_area=convertView.findViewById(R.id.visiting_area_textview);

        TextView entry_date_text=convertView.findViewById(R.id.entry_date_text);
        TextView entry_time=convertView.findViewById(R.id.entry_date_textview);
        TextView exit_date_text=convertView.findViewById(R.id.exit_date_text);
        TextView exit_time=convertView.findViewById(R.id.exit_date_textview);

        Button view_nda_button=convertView.findViewById(R.id.view_nda_button);
        ImageButton downarrow_button=convertView.findViewById(R.id.downarrow_Button);

        // Assuming 'itemList' contains the list of visitor IDs, and each item has a getVisitorId() method
        VisitorSearchResult currentItem = itemList.get(position);
        visitor_name.setText(String.valueOf(currentItem.getVisitorName()));
        mob_no.setText(String.valueOf(currentItem.getMobileNo()));

        visitor_id.setText(String.valueOf(currentItem.getVisitorId()));
        visitor_designation.setText(String.valueOf(currentItem.getVisitorDesignation()));
        visitor_place.setText(String.valueOf(currentItem.getVisitorPlace()));
        visitor_company.setText(String.valueOf(currentItem.getVisitorCompany()));
        visitor_type.setText(String.valueOf(currentItem.getVisitorTypeId()));
        visiting_staff.setText(String.valueOf(currentItem.getVisitingFaculty()));
        approver_name.setText(String.valueOf(currentItem.getApproverName()));
        purpose.setText(String.valueOf(currentItem.getPurpose()));
        visiting_area.setText(String.valueOf(currentItem.getVisitingAreaName()));

        entry_time.setText(String.valueOf(currentItem.getEntryDatetime()));
        exit_time.setText(String.valueOf(currentItem.getExitDatetime()));

        visitor_id_text.setVisibility(View.GONE);
        visitor_id.setVisibility(View.GONE);
        designation_text.setVisibility(View.GONE);
        visitor_designation.setVisibility(View.GONE);
        place_text.setVisibility(View.GONE);
        visitor_place.setVisibility(View.GONE);
        company_text.setVisibility(View.GONE);
        visitor_company.setVisibility(View.GONE);
        visitor_type_text.setVisibility(View.GONE);
        visitor_type.setVisibility(View.GONE);
        visiting_staff_text.setVisibility(View.GONE);
        visiting_staff.setVisibility(View.GONE);
        approver_name_text.setVisibility(View.GONE);
        approver_name.setVisibility(View.GONE);
        purpose_text.setVisibility(View.GONE);
        purpose.setVisibility(View.GONE);
        visiting_area_text.setVisibility(View.GONE);
        visiting_area.setVisibility(View.GONE);
        entry_date_text.setVisibility(View.GONE);
        entry_time.setVisibility(View.GONE);
        exit_date_text.setVisibility(View.GONE);
        exit_time.setVisibility(View.GONE);
        view_nda_button.setVisibility(View.GONE);

        downarrow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int visibility = visitor_id.getVisibility() == View.GONE ? View.VISIBLE : View.GONE;

                visitor_id_text.setVisibility(visibility);
                visitor_id.setVisibility(visibility);
                designation_text.setVisibility(visibility);
                visitor_designation.setVisibility(visibility);
                place_text.setVisibility(visibility);
                visitor_place.setVisibility(visibility);
                company_text.setVisibility(visibility);
                visitor_company.setVisibility(visibility);
                visitor_type_text.setVisibility(visibility);
                visitor_type.setVisibility(visibility);
                visiting_staff_text.setVisibility(visibility);
                visiting_staff.setVisibility(visibility);
                approver_name_text.setVisibility(visibility);
                approver_name.setVisibility(visibility);
                purpose_text.setVisibility(visibility);
                purpose.setVisibility(visibility);
                visiting_area_text.setVisibility(visibility);
                visiting_area.setVisibility(visibility);
                entry_date_text.setVisibility(visibility);
                entry_time.setVisibility(visibility);
                exit_date_text.setVisibility(visibility);
                exit_time.setVisibility(visibility);
                view_nda_button.setVisibility(visibility);
            }
        });

        return convertView;
    }
}
