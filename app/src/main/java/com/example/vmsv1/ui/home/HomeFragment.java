package com.example.vmsv1.ui.home;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;

import com.example.vmsv1.R;
import com.example.vmsv1.ui.SharedViewModel;
import com.example.vmsv1.ui.UserMaster;
import com.example.vmsv1.ui.AreaMaster;
import com.example.vmsv1.ui.BlackList;
import com.example.vmsv1.ui.CompanyMaster;
import com.example.vmsv1.ui.DailyVisitor;
import com.example.vmsv1.ui.GateMaster;
import com.example.vmsv1.ui.IDProofTypeMaster;
import com.example.vmsv1.ui.LocationMaster;
import com.example.vmsv1.ui.ManageVisitor;
import com.example.vmsv1.ui.SBUMaster;
import com.example.vmsv1.ui.VisitorEntry;
import com.example.vmsv1.ui.VisitorTypeMaster;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    private GridLayout adminSubLayout;
    private GridLayout userSubLayout;
    private Button cm,lm,sbum,gm,am,iptm,vtm,um,mv,dv,ve,bl;
    private SharedViewModel sharedViewModel;
    String userId="5";
    String sbuId="10";
    String defaultGateId="11";

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ImageButton admin= view.findViewById(R.id.admin_image_btn);
        ImageButton user = view.findViewById(R.id.user_image_btn);
        adminSubLayout = view.findViewById(R.id.admin_sub_layout);
        userSubLayout = view.findViewById(R.id.user_sub_layout);
        cm =view.findViewById(R.id.companymaster);
        lm =view.findViewById(R.id.locationmaster);
        sbum =view.findViewById(R.id.sbumaster);
        gm =view.findViewById(R.id.gatemaster);
        am = view.findViewById(R.id.areamaster);
        iptm= view.findViewById(R.id.idproofmaster);
        vtm = view.findViewById(R.id.visitortypemaster);
        um = view.findViewById(R.id.usermaster);
        ve =view.findViewById(R.id.visitorentry);
        dv =view.findViewById(R.id.dailyvistor);
        mv =view.findViewById(R.id.managevisitor);
        bl =view.findViewById(R.id.blacklistvisitor);

        adminSubLayout.setVisibility(View.GONE);
        userSubLayout.setVisibility(View.GONE);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        userId= String.valueOf(sharedViewModel.getUserId().getValue());
        Log.d("act","HomeFragment userId="+userId);
        sbuId= String.valueOf(sharedViewModel.getSbuId().getValue());
        Log.d("act","HomeFragment sbuId="+sbuId);
        defaultGateId= String.valueOf(sharedViewModel.getDefaultGateId().getValue());
        Log.d("act","HomeFragment defaultgateId="+defaultGateId);

        sharedViewModel.getUserId().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String userIdb) {
                userId = userIdb;
            }
        });
        sharedViewModel.getSbuId().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String sbuIdb) {
                sbuId = sbuIdb;
            }
        });
        sharedViewModel.getDefaultGateId().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String defaultGateIdb) {
                defaultGateId = defaultGateIdb;
            }
        });


        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle admin sub buttons visibility
                if (adminSubLayout.getVisibility() == View.VISIBLE) {
                    adminSubLayout.setVisibility(View.GONE);

                } else {
                    adminSubLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle user sub buttons visibility
                if (userSubLayout.getVisibility() == View.VISIBLE) {
                    userSubLayout.setVisibility(View.GONE);
                } else {
                    userSubLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        cm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), CompanyMaster.class);
                i.putExtra("userId",userId);
                i.putExtra("defaultGateId",defaultGateId);
                i.putExtra("sbuId",sbuId);
                startActivity(i);
            }
        });

        lm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), LocationMaster.class);
                i.putExtra("userId",userId);
                i.putExtra("defaultGateId",defaultGateId);
                i.putExtra("sbuId",sbuId);
                startActivity(i);
            }
        });

        sbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), SBUMaster.class);
                i.putExtra("userId",userId);
                i.putExtra("defaultGateId",defaultGateId);
                i.putExtra("sbuId",sbuId);
                startActivity(i);
            }
        });

        gm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), GateMaster.class);
                i.putExtra("userId",userId);
                i.putExtra("defaultGateId",defaultGateId);
                i.putExtra("sbuId",sbuId);
                startActivity(i);
            }
        });

        am.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AreaMaster.class);
                i.putExtra("userId",userId);
                i.putExtra("defaultGateId",defaultGateId);
                i.putExtra("sbuId",sbuId);
                startActivity(i);
            }
        });

        iptm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), IDProofTypeMaster.class);
                i.putExtra("userId",userId);
                i.putExtra("defaultGateId",defaultGateId);
                i.putExtra("sbuId",sbuId);
                startActivity(i);
            }
        });

        vtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), VisitorTypeMaster.class);
                i.putExtra("userId",userId);
                i.putExtra("defaultGateId",defaultGateId);
                i.putExtra("sbuId",sbuId);
                startActivity(i);
            }
        });

        um.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), UserMaster.class);
                i.putExtra("userId",userId);
                i.putExtra("defaultGateId",defaultGateId);
                i.putExtra("sbuId",sbuId);
                startActivity(i);
            }
        });

        dv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), DailyVisitor.class);
                i.putExtra("userId",userId);
                i.putExtra("defaultGateId",defaultGateId);
                i.putExtra("sbuId",sbuId);
                startActivity(i);
            }
        });

        mv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ManageVisitor.class);
                i.putExtra("userId",userId);
                i.putExtra("defaultGateId",defaultGateId);
                i.putExtra("sbuId",sbuId);
                startActivity(i);
            }
        });

        bl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), BlackList.class);
                i.putExtra("userId",userId);
                i.putExtra("defaultGateId",defaultGateId);
                i.putExtra("sbuId",sbuId);
                startActivity(i);
            }
        });

        ve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), VisitorEntry.class);
                i.putExtra("userId",userId);
                i.putExtra("defaultGateId",defaultGateId);
                i.putExtra("sbuId",sbuId);
                startActivity(i);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }

}
