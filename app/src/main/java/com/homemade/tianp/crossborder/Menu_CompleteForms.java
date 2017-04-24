package com.homemade.tianp.crossborder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Menu_CompleteForms extends Fragment {


    public Menu_CompleteForms() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_completed_forms, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Complete Forms");
        IOHandler.directorySetup(getActivity());
        TextView emptyMessage = (TextView)view.findViewById(R.id.TextView_EmptyMessage);
        ListView list = (ListView) getView().findViewById(R.id.ListView_CompleteForms);
        List<String> newList = new ArrayList<String>();
        newList.addAll(IOHandler.readDirectory(IOHandler.mainDirectory_Form1A_CompleteForms));
        newList.addAll(IOHandler.readDirectory(IOHandler.mainDirectory_Form1B_CompleteForms));
        newList.addAll(IOHandler.readDirectory(IOHandler.mainDirectory_Form1C_CompleteForms));
        java.util.Collections.sort(newList);
        String[] stringArray = newList.toArray(new String[0]);



        list.setAdapter(new CustomAdapter_CompleteForms(getActivity(),stringArray));
        if (list.getCount() > 0){
            emptyMessage.setHeight(0);
        }else{
            emptyMessage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        }

    }


}
