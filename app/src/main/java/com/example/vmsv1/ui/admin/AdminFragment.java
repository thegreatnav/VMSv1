package com.example.vmsv1.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.vmsv1.databinding.FragmentAdminBinding;
import com.example.vmsv1.ui.CompanyMaster;
import com.example.vmsv1.ui.GateMaster;
import com.example.vmsv1.ui.LocationMaster;
import com.example.vmsv1.ui.SBUMaster;
import com.example.vmsv1.ui.SharedViewModel;

public class AdminFragment extends Fragment {

    String userId="5";
    String sbuId="10";
    String defaultGateId="11";
    private FragmentAdminBinding binding;
    private SharedViewModel sharedViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AdminViewModel adminViewModel =
                new ViewModelProvider(this).get(AdminViewModel.class);

        binding = FragmentAdminBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        userId= String.valueOf(sharedViewModel.getUserId().getValue());
        //Log.d("act","userId="+userId);
        sbuId= String.valueOf(sharedViewModel.getSbuId().getValue());
        //Log.d("act","sbuId="+sbuId);
        defaultGateId= String.valueOf(sharedViewModel.getDefaultGateId().getValue());
        //Log.d("act","defaultgateId="+defaultGateId);

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
        //Toast.makeText(getActivity(), "Default Gate Id: "+defaultGateId+"\nSbu Id: "+sbuId, Toast.LENGTH_LONG).show();

        final ListView listView = binding.adminList;
        String[] adminitems ={"Company Master","Location Master","SBU Master","Gate Master","Area Master","ID Proof Type Master","Visitor Type Master","User Master"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,adminitems);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position){
                    case 0:
                        Intent i = new Intent(getContext(), CompanyMaster.class);
                        i.putExtra("userId",userId);
                        i.putExtra("defaultGateId",defaultGateId);
                        i.putExtra("sbuId",sbuId);
                        startActivity(i);
                        break;
                    case 1:
                        i = new Intent(view.getContext(), LocationMaster.class);
                        i.putExtra("userId",userId);
                        i.putExtra("defaultGateId",defaultGateId);
                        i.putExtra("sbuId",sbuId);
                        startActivity(i);
                        break;
                    case 2:
                        i = new Intent(view.getContext(), SBUMaster.class);
                        i.putExtra("userId",userId);
                        i.putExtra("defaultGateId",defaultGateId);
                        i.putExtra("sbuId",sbuId);
                        startActivity(i);
                        break;
                    case 3:
                        i = new Intent(view.getContext(), GateMaster.class);
                        i.putExtra("userId",userId);
                        i.putExtra("defaultGateId",defaultGateId);
                        i.putExtra("sbuId",sbuId);
                        startActivity(i);
                        break;

                }

            }
        });
        final TextView textView = binding.adminText;
        adminViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}