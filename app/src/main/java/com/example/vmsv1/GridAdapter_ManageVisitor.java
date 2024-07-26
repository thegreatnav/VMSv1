package com.example.vmsv1;

import static android.graphics.Color.rgb;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.vmsv1.dataitems.VisitorSearchResult;

import java.util.List;

public class GridAdapter_ManageVisitor extends BaseAdapter {

    private Context context;
    private List<VisitorSearchResult> itemList;

    public GridAdapter_ManageVisitor(Context context, List<VisitorSearchResult> itemList) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.gridview_element_managevisitor, parent, false);
        }
        TextView visitor_id= convertView.findViewById(R.id.visitor_id_textview);
        TextView visitor_name=convertView.findViewById(R.id.visitor_name_textview);
        TextView mob_no=convertView.findViewById(R.id.mob_no_textview);

        TextView visitor_designation=convertView.findViewById(R.id.designation_textview);
        TextView visitor_place=convertView.findViewById(R.id.place_textview);
        TextView visitor_company=convertView.findViewById(R.id.company_textview);
        TextView visiting_staff=convertView.findViewById(R.id.visiting_staff_textview);
        TextView approver_name=convertView.findViewById(R.id.approver_name_textview);
        TextView purpose=convertView.findViewById(R.id.purpose_textview);
        TextView visiting_area=convertView.findViewById(R.id.visiting_area_textview);

        TextView entry_time=convertView.findViewById(R.id.entry_date_textview);
        TextView exit_time=convertView.findViewById(R.id.exit_date_textview);

        ImageButton camera_status_button=convertView.findViewById(R.id.camera_status_button);
        ImageButton nda_status_button=convertView.findViewById(R.id.nda_status_button);
        ImageButton print_label_button=convertView.findViewById(R.id.print_label_button);
        ImageButton exit_status_button=convertView.findViewById(R.id.exit_status_button);
        ImageButton downarrow_button = convertView.findViewById(R.id.downarrow_Button);

        TextView designation = convertView.findViewById(R.id.designation_text);
        TextView place = convertView.findViewById(R.id.place_text);
        TextView company = convertView.findViewById(R.id.company_text);
        TextView entry_date = convertView.findViewById(R.id.entry_date_text);
        TextView exit_date = convertView.findViewById(R.id.exit_date_text);
        TextView visiting_staff_text = convertView.findViewById(R.id.visiting_staff_text);
        TextView approver_name_text = convertView.findViewById(R.id.approver_name_text);
        TextView purpose_text = convertView.findViewById(R.id.purpose_text);
        TextView visiting_area_text = convertView.findViewById(R.id.visiting_area_text);
        TextView visitor_name_text = convertView.findViewById(R.id.visitor_name_text);

        // Assuming 'itemList' contains the list of visitor IDs, and each item has a getVisitorId() method
        VisitorSearchResult currentItem = itemList.get(position);
        visitor_id.setText(String.valueOf(currentItem.getVisitorId()));
        visitor_name.setText(String.valueOf(currentItem.getVisitorName()));
        mob_no.setText(String.valueOf(currentItem.getMobileNo()));

        visitor_designation.setText(String.valueOf(currentItem.getVisitorDesignation()));
        visitor_place.setText(String.valueOf(currentItem.getVisitorPlace()));
        visitor_company.setText(String.valueOf(currentItem.getVisitorCompany()));
        visiting_staff.setText(String.valueOf(currentItem.getVisitingFaculty()));
        approver_name.setText(String.valueOf(currentItem.getApproverName()));
        purpose.setText(String.valueOf(currentItem.getPurpose()));
        visiting_area.setText(String.valueOf(currentItem.getVisitingAreaName()));

        entry_time.setText(String.valueOf(currentItem.getEntryDatetime()));
        exit_time.setText(String.valueOf(currentItem.getExitDatetime()));

        if(currentItem.getPhotoFilePath()==null)
        {
            camera_status_button.setImageResource(R.drawable.camera_red);
        }
        else
        {
            camera_status_button.setImageResource(R.drawable.camera_green);
        }
        if(!String.valueOf(currentItem.getNdaStatus()).equals("Y"))
        {
            nda_status_button.setImageResource(R.drawable.lock_red);
        }
        else
        {
            nda_status_button.setImageResource(R.drawable.lock_green);
        }
        if(currentItem.getMobileNo()!=null && currentItem.getNdaStatus()!=null && currentItem.getPhotoFilePath()!=null)
        {
            print_label_button.setImageResource(R.drawable.print_green);
        }
        else {
            print_label_button.setImageResource(R.drawable.print_red);
        }

        if(currentItem.getExitDatetime()==null) {
            exit_status_button.setImageResource(R.drawable.exit_red);
        }
        else
        {
            exit_status_button.setImageResource(R.drawable.exit_green);
        }

        downarrow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int visibility = visitor_name.getVisibility() == View.GONE ? View.VISIBLE : View.GONE;

                camera_status_button.setVisibility(visibility);
                nda_status_button.setVisibility(visibility);
                print_label_button.setVisibility(visibility);
                exit_status_button.setVisibility(visibility);
                visitor_name_text.setVisibility(visibility);
                visitor_name.setVisibility(visibility);
                visitor_designation.setVisibility(visibility);
                designation.setVisibility(visibility);
                place.setVisibility(visibility);
                company.setVisibility(visibility);
                entry_date.setVisibility(visibility);
                exit_date.setVisibility(visibility);
                visiting_staff_text.setVisibility(visibility);
                approver_name_text.setVisibility(visibility);
                purpose_text.setVisibility(visibility);
                visiting_area_text.setVisibility(visibility);
                visitor_designation.setVisibility(visibility);
                visitor_place.setVisibility(visibility);
                visitor_company.setVisibility(visibility);
                entry_time.setVisibility(visibility);
                exit_time.setVisibility(visibility);
                visiting_staff.setVisibility(visibility);
                approver_name.setVisibility(visibility);
                purpose.setVisibility(visibility);
                visiting_area.setVisibility(visibility);

                // Assuming 'itemList' contains the list of visitor IDs, and each item has a getVisitorId() method
            }
        });
        return convertView;
    }
}
