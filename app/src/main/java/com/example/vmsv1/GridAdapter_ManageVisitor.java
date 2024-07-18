package com.example.vmsv1;

import static android.graphics.Color.rgb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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

        Button camera_status_button=convertView.findViewById(R.id.view_nda_button);
        Button nda_status_button=convertView.findViewById(R.id.nda_status_button);
        Button print_status_button=convertView.findViewById(R.id.print_status_button);
        Button exit_status_button=convertView.findViewById(R.id.exit_status_button);

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

        /*if(currentItem.getCamera_status()==false){
            camera_status_button.setText("N");
            camera_status_button.setBackgroundColor(rgb(255, 87, 51));
        }
        else {
            camera_status_button.setText("Y");
            camera_status_button.setBackgroundColor(rgb(34, 139, 34));
        }*/

        if(currentItem.getNdaStatus()=="N") {
            nda_status_button.setText("N");
            nda_status_button.setBackgroundColor(rgb(255, 87, 51));
        }
        else {
            nda_status_button.setText("Y");
            nda_status_button.setBackgroundColor(rgb(34, 139, 34));
        }

        /*if(currentItem.getPrintStatus()==false)
        {
            print_status_button.setText("N");
            print_status_button.setBackgroundColor(rgb(255, 87, 51));
        }
        else {
            print_status_button.setText("Y");
            print_status_button.setBackgroundColor(rgb(34, 139, 34));
        }

        if(currentItem.getExit_status()==false) {
            exit_status_button.setText("N");
            exit_status_button.setBackgroundColor(rgb(255, 87, 51));
        }
        else
        {
            exit_status_button.setText("Y");
            exit_status_button.setBackgroundColor(rgb(34, 139, 34));
        }*/

        return convertView;
    }
}
