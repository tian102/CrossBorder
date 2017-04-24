package com.homemade.tianp.crossborder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Menu_FormFinalize extends Fragment {

    String BorderStation = "", Date = "", StartTime = "", FinishTime = "", WeatherConditions, EnumeratorInitial = "", CheckedBy = "", Comments = "";



    String form1_Header = "\nBorder Station;Date;Start;Finish;Weather Conditions;Comments;Enumerator Initial;Checked By:";


    EditText input_BorderStation, input_Date, input_StartTime, input_FinishTime,input_EnumeratorInitial,input_CheckedBy,input_Comments;

    Button button_EntryDate,button_StartTime,button_FinishTime, buttonDone, buttonDiscard;

    public Spinner spinner;

    public static String selectedForm = "";

    public static File incompleteFile;

    public Menu_FormFinalize() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_menu_form_finalize, container, false);

        return v;
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Finalize Form");

        IOHandler.directorySetup(getActivity());
        setupSetButtons(view);
        setupSpinner();
        setupEditTexts(view);
        setupBottomButtons(view);
        startupSettings();
    }

    private void setupSetButtons(View view){
        button_EntryDate = (Button) view.findViewById(R.id.Button_EntryDate);
        button_StartTime = (Button) view.findViewById(R.id.Button_StartTime);
        button_FinishTime = (Button) view.findViewById(R.id.Button_FinishTime);
    }

    private void setupSpinner() {
        View view = getView();
        spinner = (Spinner) view.findViewById(R.id.select_WeatherConditions);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.select_WeatherConditions, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    private void setupEditTexts(View view){
        input_BorderStation = ((EditText) view.findViewById(R.id.Input_BorderStation));
        input_Date  = ((EditText) view.findViewById(R.id.Input_Date));
        input_StartTime  = ((EditText) view.findViewById(R.id.Input_StartTime));
        input_FinishTime = ((EditText) view.findViewById(R.id.Input_FinishTime));
        input_EnumeratorInitial = ((EditText) view.findViewById(R.id.Input_EnumeratorInitial));
        input_CheckedBy = ((EditText) view.findViewById(R.id.Input_CheckedBy));
        input_Comments = ((EditText) view.findViewById(R.id.Input_Comments));
    }

    private void setupBottomButtons(View view){
        buttonDone = (Button) view.findViewById(R.id.Button_Done);
        buttonDiscard = (Button) view.findViewById(R.id.Button_Discard);

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entryDone(v);
            }
        });

        buttonDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entryDiscard();
            }
        });
    }

    private void startupSettings(){
        getActivity().setTitle("Finalize " + selectedForm);
    }

    private boolean checkFields() {
        if(BorderStation.equals("") || Date.equals("") || StartTime.equals("") || FinishTime.equals("") || EnumeratorInitial.equals("") || CheckedBy.equals("") || Comments.equals("")){
            return false;
        }else {
            return true;
        }
    }

    private void getDataFromFields(View view){
        BorderStation = checkNull(input_BorderStation.getText().toString());
        Date = checkNull(input_Date.getText().toString());
        StartTime = checkNull(input_StartTime.getText().toString());
        FinishTime = checkNull(input_FinishTime.getText().toString());
        WeatherConditions = checkNull(spinner.getSelectedItem().toString());
        EnumeratorInitial = checkNull(input_EnumeratorInitial.getText().toString());
        CheckedBy = checkNull(input_CheckedBy.getText().toString());
        Comments = checkNull(input_Comments.getText().toString());
    }

    private void entryDone(View v){
        getDataFromFields(v);
        if (checkFields()){
            String text = BorderStation + ";" + Date + ";" + StartTime + ";" + FinishTime + ";"
                    + WeatherConditions + ";#" + Comments + ";" + EnumeratorInitial + ";" + CheckedBy;
            try {
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("HH-mm-ss");
                String formattedTime = df.format(cal.getTime());
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int year = cal.get(Calendar.YEAR);

                String date = day + "-" + MainActivity.getMonth(month) + "-" + year;
                String CompleteFormFileName = selectedForm + "_" + date + "_" + formattedTime + ".csv";


                createCompleteForm(selectedForm,CompleteFormFileName,form1_Header + "\n" + text);
                MainActivity.fragmentcheckSelectedMenuItem(R.id.Menu_Home);
                navigateTo(R.id.Menu_Home, new Menu_Home());

            } catch (Exception e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity(), "Not all fields have been filled", Toast.LENGTH_SHORT).show();

        }
    }

    private void createCompleteForm(String formName, String fileName, String contents) throws FileNotFoundException {
        switch(formName){
            case "Form1A":
                IOHandler.copyFileFromDir(getActivity(),IOHandler.mainDirectory_Form1A_IncompleteForms,"IncompleteForm1A.csv",IOHandler.mainDirectory_Form1A_CompleteForms,fileName);
                IOHandler.appendToFile(getActivity(),IOHandler.mainDirectory_Form1A_CompleteForms,fileName,contents);
                IOHandler.deleteAllFilesFromDirectory(getActivity(), IOHandler.mainDirectory_Form1A_IncompleteEntries);
                IOHandler.deleteAllFilesFromDirectory(getActivity(), IOHandler.mainDirectory_Form1A_CompleteEntries);
                IOHandler.deleteAllFilesFromDirectory(getActivity(), IOHandler.mainDirectory_Form1A_IncompleteForms);
                Toast.makeText(getActivity(), fileName + " complete", Toast.LENGTH_SHORT).show();
                break;
            case "Form1B":
                IOHandler.copyFileFromDir(getActivity(),IOHandler.mainDirectory_Form1B_IncompleteForms,"IncompleteForm1B.csv",IOHandler.mainDirectory_Form1B_CompleteForms,fileName);
                IOHandler.appendToFile(getActivity(),IOHandler.mainDirectory_Form1B_CompleteForms,fileName,contents);
                IOHandler.deleteAllFilesFromDirectory(getActivity(), IOHandler.mainDirectory_Form1B_IncompleteEntries);
                IOHandler.deleteAllFilesFromDirectory(getActivity(), IOHandler.mainDirectory_Form1B_CompleteEntries);
                IOHandler.deleteAllFilesFromDirectory(getActivity(), IOHandler.mainDirectory_Form1B_IncompleteForms);
                Toast.makeText(getActivity(), fileName + " complete", Toast.LENGTH_SHORT).show();
                break;
            case "Form1C":
                IOHandler.copyFileFromDir(getActivity(),IOHandler.mainDirectory_Form1C_IncompleteForms,"IncompleteForm1C.csv",IOHandler.mainDirectory_Form1C_CompleteForms,fileName);
                IOHandler.appendToFile(getActivity(),IOHandler.mainDirectory_Form1C_CompleteForms,fileName,contents);
                IOHandler.deleteAllFilesFromDirectory(getActivity(), IOHandler.mainDirectory_Form1C_IncompleteEntries);
                IOHandler.deleteAllFilesFromDirectory(getActivity(), IOHandler.mainDirectory_Form1C_CompleteEntries);
                IOHandler.deleteAllFilesFromDirectory(getActivity(), IOHandler.mainDirectory_Form1C_IncompleteForms);
                Toast.makeText(getActivity(), fileName + " complete", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    private void entryDiscard(){
        new android.support.v7.app.AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("Cancel")
                .setMessage("Do you want to cancel the form finalisation?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.fragmentcheckSelectedMenuItem(R.id.Menu_Home);
                        navigateTo(R.id.Menu_Home, new Menu_Home());
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    private String checkNull(String string){
        if(string == null){
            string = "";
        }
        return string;
    }

    private void navigateTo(int selectedNavigationId, Fragment fragment){
        MenuItem menuItem = MainActivity.nav.getMenu().findItem(selectedNavigationId);
        menuItem.isChecked();
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        ft.replace(R.id.content_main,fragment);
        ft.commit();
    }

    public static void setSelectedForm(String formName){
        switch (formName){
            case "Form1A":
                selectedForm = formName;
                IOHandler.setCurrentDirectory(IOHandler.mainDirectory_Form1A);
                break;
            case "Form1B":
                selectedForm = formName;
                IOHandler.setCurrentDirectory(IOHandler.mainDirectory_Form1B);
                break;
            case "Form1C":
                selectedForm = formName;
                IOHandler.setCurrentDirectory(IOHandler.mainDirectory_Form1C);
                break;
        }
    }


}
