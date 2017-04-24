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

public class Menu_IncompleteForms extends Fragment {
    public static String[] filesFromDir = new String[]{};

    public Menu_IncompleteForms() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_incompleted_forms, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Incomplete Forms");
        IOHandler.directorySetup(getActivity());
        TextView emptyMessage = (TextView)view.findViewById(R.id.TextView_EmptyMessage);
        ListView list = (ListView) getView().findViewById(R.id.ListView_IncompleteForms);
        list.setAdapter(new CustomAdapter_IncompleteForms(getActivity(),filesFromDir));
        if (list.getCount() > 0){
            emptyMessage.setHeight(0);
        }else{
            emptyMessage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        }

    }
    public static void setPath(String path){
        filesFromDir = IOHandler.readDirectory(path).toArray(new String[0]);
    }


}
