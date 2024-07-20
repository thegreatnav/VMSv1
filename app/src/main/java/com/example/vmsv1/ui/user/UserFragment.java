package com.example.vmsv1.ui.user;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.vmsv1.databinding.FragmentUserBinding;
import com.example.vmsv1.ui.BlackList;
import com.example.vmsv1.ui.DailyVisitor;
import com.example.vmsv1.ui.ManageVisitor;
import com.example.vmsv1.ui.SharedViewModel;
import com.example.vmsv1.ui.VisitorEntry;

public class UserFragment extends Fragment {

    String userId="5";
    String sbuId="10";
    String defaultGateId="11";

    private FragmentUserBinding binding;
    private SharedViewModel sharedViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        UserViewModel userViewModel =
                new ViewModelProvider(this).get(UserViewModel.class);

        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        userId= String.valueOf(sharedViewModel.getUserId().getValue());
        sbuId= String.valueOf(sharedViewModel.getSbuId().getValue());
        defaultGateId= String.valueOf(sharedViewModel.getDefaultGateId().getValue());

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

        final ListView listView = binding.userList;
        String[] useritems ={"Visitor Entry","Manage Visitor","Black List","Daily Visitor List"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,useritems);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position){
                    case 0:
                        Intent i = new Intent(getContext(), VisitorEntry.class);
                        i.putExtra("userId",userId);
                        i.putExtra("defaultGateId",defaultGateId);
                        i.putExtra("sbuId",sbuId);
                        startActivity(i);
                        break;
                    case 1:
                        i = new Intent(view.getContext(), ManageVisitor.class);
                        i.putExtra("userId",userId);
                        i.putExtra("defaultGateId",defaultGateId);
                        i.putExtra("sbuId",sbuId);
                        startActivity(i);
                        break;
                    case 2:
                        i = new Intent(view.getContext(), BlackList.class);
                        i.putExtra("userId",userId);
                        i.putExtra("defaultGateId",defaultGateId);
                        i.putExtra("sbuId",sbuId);
                        startActivity(i);
                        break;
                    case 3:
                        i = new Intent(view.getContext(), DailyVisitor.class);
                        i.putExtra("userId",userId);
                        i.putExtra("defaultGateId",defaultGateId);
                        i.putExtra("sbuId",sbuId);
                        startActivity(i);
                        break;
                }

            }
        });

        final TextView textView = binding.userText;
        userViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}