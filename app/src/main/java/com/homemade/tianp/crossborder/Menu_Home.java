package com.homemade.tianp.crossborder;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Menu_Home extends Fragment implements View.OnClickListener{
    Button new_Button, open_Button, share_Button, newForm1A_Entry_Button, newForm1B_Entry_Button,
            newForm1C_Entry_Button, completeForm1A_Entries_Button, completeForm1B_Entries_Button,
            completeForm1C_Entries_Button, incompleteForm1A_Entries_Button, incompleteForm1B_Entries_Button,
            incompleteForm1C_Entries_Button, form1A_Button, form1B_Button, form1C_Button;

    LinearLayout newLayout, openLayout;



    public Menu_Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("");

        IOHandler.directorySetup(getActivity());
        setupLayouts(view);
        setupRecentForms(view);
        setupButtons(view);
        setOnClickListeners();
        new_Button.performClick();
    }

    private void setupRecentForms(View view){
        TextView emptyMessage = (TextView)view.findViewById(R.id.TextView_EmptyMessage);
        ListView list = (ListView) getView().findViewById(R.id.ListView_IncompleteForms_Home);
        List<String> newList = new ArrayList<String>();
        newList.addAll(IOHandler.readDirectory(IOHandler.mainDirectory_Form1A_IncompleteForms));
        newList.addAll(IOHandler.readDirectory(IOHandler.mainDirectory_Form1B_IncompleteForms));
        newList.addAll(IOHandler.readDirectory(IOHandler.mainDirectory_Form1C_IncompleteForms));
        java.util.Collections.sort(newList);
        String[] stringArray = newList.toArray(new String[0]);

        list.setAdapter(new CustomAdapter_IncompleteForms_Home(getActivity(),stringArray));
        if (list.getCount() > 0){
            emptyMessage.setHeight(0);
        }else{
            emptyMessage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    private void setupButtons(View view){
        new_Button = (Button) view.findViewById(R.id.Button_New);
        open_Button = (Button) view.findViewById(R.id.Button_Open);
        share_Button = (Button) view.findViewById(R.id.Button_Share);

        newForm1A_Entry_Button = (Button) view.findViewById(R.id.Button_NewForm1A);
        newForm1B_Entry_Button = (Button) view.findViewById(R.id.Button_NewForm1B);
        newForm1C_Entry_Button = (Button) view.findViewById(R.id.Button_NewForm1C);

        completeForm1A_Entries_Button = (Button) view.findViewById(R.id.Button_CompleteForm1AEntries);
        completeForm1B_Entries_Button = (Button) view.findViewById(R.id.Button_CompleteForm1BEntries);
        completeForm1C_Entries_Button = (Button) view.findViewById(R.id.Button_CompleteForm1CEntries);

        incompleteForm1A_Entries_Button = (Button) view.findViewById(R.id.Button_IncompleteForm1AEntries);
        incompleteForm1B_Entries_Button = (Button) view.findViewById(R.id.Button_IncompleteForm1BEntries);
        incompleteForm1C_Entries_Button = (Button) view.findViewById(R.id.Button_IncompleteForm1CEntries);

        form1A_Button = (Button) view.findViewById(R.id.Button_IncompleteForm1A);
        form1B_Button = (Button) view.findViewById(R.id.Button_IncompleteForm1B);
        form1C_Button = (Button) view.findViewById(R.id.Button_IncompleteForm1C);


    }

    private void setupLayouts(View view) {
        newLayout = (LinearLayout) view.findViewById(R.id.SelectionView_New);
        openLayout = (LinearLayout) view.findViewById(R.id.SelectionView_Open);

    }

    private void setOnClickListeners(){
        new_Button.setOnClickListener(this);
        open_Button.setOnClickListener(this);
        share_Button.setOnClickListener(this);

        newForm1A_Entry_Button.setOnClickListener(this);
        newForm1B_Entry_Button.setOnClickListener(this);
        newForm1C_Entry_Button.setOnClickListener(this);

        completeForm1A_Entries_Button.setOnClickListener(this);
        completeForm1B_Entries_Button.setOnClickListener(this);
        completeForm1C_Entries_Button.setOnClickListener(this);

        incompleteForm1A_Entries_Button.setOnClickListener(this);
        incompleteForm1B_Entries_Button.setOnClickListener(this);
        incompleteForm1C_Entries_Button.setOnClickListener(this);

        form1A_Button.setOnClickListener(this);
        form1B_Button.setOnClickListener(this);
        form1C_Button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Button_New:
                new_Button.setPaintFlags(new_Button.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                newLayout.setVisibility(LinearLayout.VISIBLE);
                openLayout.setVisibility(LinearLayout.GONE);
                open_Button.setPaintFlags(0);
                break;
            case R.id.Button_Open:
                new_Button.setPaintFlags(0);
                newLayout.setVisibility(LinearLayout.GONE);
                openLayout.setVisibility(LinearLayout.VISIBLE);
                open_Button.setPaintFlags(new_Button.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                break;
            case R.id.Button_Share:
                navigateTo(R.id.Menu_Share, new Menu_CompleteForms());
                break;
            case R.id.Button_NewForm1A:
                navigateTo(R.id.Menu_New1A, new Menu_Form1A());
                break;
            case R.id.Button_NewForm1B:
                navigateTo(R.id.Menu_New1B, new Menu_Form1B());
                break;
            case R.id.Button_NewForm1C:
                navigateTo(R.id.Menu_New1C, new Menu_Form1C());
                break;
            case R.id.Button_CompleteForm1AEntries:
                Menu_CompleteEntries.setPath(IOHandler.mainDirectory_Form1A_CompleteEntries);
                IOHandler.setCurrentDirectory(IOHandler.mainDirectory_Form1A_CompleteEntries);
                navigateTo(R.id.Menu_Complete1A, new Menu_CompleteEntries());
                break;
            case R.id.Button_CompleteForm1BEntries:
                Menu_CompleteEntries.setPath(IOHandler.mainDirectory_Form1B_CompleteEntries);
                IOHandler.setCurrentDirectory(IOHandler.mainDirectory_Form1B_CompleteEntries);
                navigateTo(R.id.Menu_Complete1B, new Menu_CompleteEntries());
                break;
            case R.id.Button_CompleteForm1CEntries:
                Menu_CompleteEntries.setPath(IOHandler.mainDirectory_Form1C_CompleteEntries);
                IOHandler.setCurrentDirectory(IOHandler.mainDirectory_Form1C_CompleteEntries);
                navigateTo(R.id.Menu_Complete1C, new Menu_CompleteEntries());
                break;
            case R.id.Button_IncompleteForm1AEntries:
                Menu_IncompleteEntries.setPath(IOHandler.mainDirectory_Form1A_IncompleteEntries);
                IOHandler.setCurrentDirectory(IOHandler.mainDirectory_Form1A_IncompleteEntries);
                navigateTo(R.id.Menu_Incomplete1A, new Menu_IncompleteEntries());
                break;
            case R.id.Button_IncompleteForm1BEntries:
                Menu_IncompleteEntries.setPath(IOHandler.mainDirectory_Form1B_IncompleteEntries);
                IOHandler.setCurrentDirectory(IOHandler.mainDirectory_Form1B_IncompleteEntries);
                navigateTo(R.id.Menu_Incomplete1B, new Menu_IncompleteEntries());
                break;
            case R.id.Button_IncompleteForm1CEntries:
                Menu_IncompleteEntries.setPath(IOHandler.mainDirectory_Form1C_IncompleteEntries);
                IOHandler.setCurrentDirectory(IOHandler.mainDirectory_Form1C_IncompleteEntries);
                navigateTo(R.id.Menu_Incomplete1C, new Menu_IncompleteEntries());
                break;
            case R.id.Button_IncompleteForm1A:
                Menu_IncompleteForms.setPath(IOHandler.mainDirectory_Form1A_IncompleteForms);
                IOHandler.setCurrentDirectory(IOHandler.mainDirectory_Form1A_IncompleteForms);
                navigateTo(R.id.Menu_View1A, new Menu_IncompleteForms());
                break;
            case R.id.Button_IncompleteForm1B:
                Menu_IncompleteForms.setPath(IOHandler.mainDirectory_Form1B_IncompleteForms);
                IOHandler.setCurrentDirectory(IOHandler.mainDirectory_Form1B_IncompleteForms);
                navigateTo(R.id.Menu_View1B, new Menu_IncompleteForms());
                break;
            case R.id.Button_IncompleteForm1C:
                Menu_IncompleteForms.setPath(IOHandler.mainDirectory_Form1C_IncompleteForms);
                IOHandler.setCurrentDirectory(IOHandler.mainDirectory_Form1C_IncompleteForms);
                navigateTo(R.id.Menu_View1C, new Menu_IncompleteForms());
                break;
        }
    }


    private void navigateTo(int selectedNavigationId, Fragment fragment){
        MainActivity.fragmentcheckSelectedMenuItem(selectedNavigationId);
        MenuItem menuItem = MainActivity.nav.getMenu().findItem(selectedNavigationId);
        menuItem.isChecked();
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        ft.replace(R.id.content_main,fragment);
        ft.commit();
    }


}
