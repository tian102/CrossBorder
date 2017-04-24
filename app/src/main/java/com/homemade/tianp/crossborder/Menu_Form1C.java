package com.homemade.tianp.crossborder;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Menu_Form1C extends Fragment {

    String RegNo = "", VehicleType = "", GateOut = "";

    String saveDialog_Filename = "";

    String form1C_Header = "Reg. No;Vehicle Type;Gate Out (Departure)";

    EditText input_RegNo, input_GateOut;

    Button button_SetGateOut, buttonDone, buttonDiscard, buttonSave;

    Spinner spinner;

    public static boolean incompleteEntry = false;

    public static File incompleteFile;

    public static boolean completeEntry = false;

    public static File completeFile;

    public Menu_Form1C() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_menu_form1c, container, false);

        return v;
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("New Form 1C Entry");

        IOHandler.directorySetup(getActivity());
        setupSetButtons(view);
        setupSpinner();
        setupEditTexts(view);
        setupBottomButtons(view);
        startupSettings();
    }

    private void setupSetButtons(View view){
        button_SetGateOut = (Button)view.findViewById(R.id.Button_GateOut);
    }

    private void setupSpinner() {
        View view = getView();
        spinner = (Spinner) view.findViewById(R.id.select_VehicleType);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.select_VehicleType, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    private void setupEditTexts(View view){
        input_RegNo = ((EditText) view.findViewById(R.id.Input_RegNo));
        input_GateOut = ((EditText) view.findViewById(R.id.Input_GateOut));
    }

    private void setupBottomButtons(View view){
        buttonDone = (Button) view.findViewById(R.id.Button_Done);
        buttonSave = (Button) view.findViewById(R.id.Button_Save);
        buttonDiscard = (Button) view.findViewById(R.id.Button_Discard);

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entryDone(v);
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData(v);
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
        if(!incompleteEntry && !completeEntry){
            getActivity().setTitle("New Form 1C Entry");
        }else if(completeEntry){
            setCompleteEntries(completeFile);
            getActivity().setTitle("Form 1C - " + completeFile.getName());
            completeEntry = false;
        }else if(incompleteEntry){
            setIncompleteEntries(incompleteFile);
            getActivity().setTitle("Form 1C - " + incompleteFile.getName());
            incompleteEntry = false;
        }
    }


    private boolean checkFields() {
        if(RegNo.equals("") || GateOut.equals("")){
            return false;
        }else {
            return true;
        }
    }

    private void getDataFromFields(View view){

        RegNo = checkNull(input_RegNo.getText().toString());
        VehicleType = checkNull(spinner.getSelectedItem().toString());
        GateOut = checkNull(input_GateOut.getText().toString());
    }

    private void saveData(View v){
        getDataFromFields(v);
        saveDialog(getActivity());
        //Go home
    }

    private void saveDialog(final Context context){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Entry Name");

        // Set up the input
        final EditText input = new EditText(context);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        //builder.setMessage("Place instructional message here if needed");
        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString().equals("")) {
                    Toast.makeText(context, "Entry name missing", Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "Entry not saved", Toast.LENGTH_SHORT).show();
                }else {
                    saveDialog_Filename = input.getText().toString();
                    if(IOHandler.checkIfFileExist(getActivity(), IOHandler.mainDirectory_Form1C_IncompleteEntries + "", saveDialog_Filename + ".csv")) {
                        Toast.makeText(context, "Filename already exist", Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, "Entry not saved", Toast.LENGTH_SHORT).show();
                    }else {
                        String text = RegNo + ";" + VehicleType + ";" + GateOut;

                        try {
                            IOHandler.createFile(getActivity(),IOHandler.mainDirectory_Form1C_IncompleteEntries, saveDialog_Filename + ".csv",form1C_Header);
                            IOHandler.appendToFile(getActivity(),IOHandler.mainDirectory_Form1C_IncompleteEntries, saveDialog_Filename + ".csv",text);
                            Toast.makeText(context, saveDialog_Filename + " saved", Toast.LENGTH_SHORT).show();


                            navigateTo(R.id.Menu_Home,new Menu_Home());
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void entryDone(View v){
        getDataFromFields(v);
        if (checkFields()){
            String text = RegNo + ";" + VehicleType + ";" + GateOut;

            try {
                Calendar c = Calendar.getInstance();

                SimpleDateFormat df = new SimpleDateFormat("HH-mm-ss");
                String formattedDate = df.format(c.getTime());
                String entryName = formattedDate + "_" + RegNo;
                String incompleteFormFileName = "IncompleteForm1C.csv";
                IOHandler.createFile(getActivity(),IOHandler.mainDirectory_Form1C_CompleteEntries, entryName + ".csv",form1C_Header);
                IOHandler.appendToFile(getActivity(),IOHandler.mainDirectory_Form1C_CompleteEntries, entryName + ".csv",text);

                if(IOHandler.checkIfFileExist(getActivity(),IOHandler.mainDirectory_Form1C_IncompleteForms, incompleteFormFileName)){
                    IOHandler.appendToFile(getActivity(),IOHandler.mainDirectory_Form1C_IncompleteForms,incompleteFormFileName,text);
                }else {
                    IOHandler.createFile(getActivity(),IOHandler.mainDirectory_Form1C_IncompleteForms, incompleteFormFileName,form1C_Header);
                    IOHandler.appendToFile(getActivity(),IOHandler.mainDirectory_Form1C_IncompleteForms, incompleteFormFileName,text);
                }
                Toast.makeText(getActivity(), entryName + " saved", Toast.LENGTH_SHORT).show();

                MainActivity.fragmentcheckSelectedMenuItem(R.id.Menu_Home);
                navigateTo(R.id.Menu_Home, new Menu_Home());
            } catch (Exception e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity(), "Not all fields have been filled", Toast.LENGTH_SHORT).show();

        }
    }

    private void entryDiscard(){
        if (getActivity().getTitle() != "New Form 1C Entry"){
            new android.support.v7.app.AlertDialog.Builder(getActivity())
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setTitle("Discard")
                    .setMessage("Do you want to discard the current entry?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            IOHandler.deleteFile(getActivity(),incompleteFile);
                            navigateTo(R.id.Menu_Home, new Menu_Home());
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }else{
            new android.support.v7.app.AlertDialog.Builder(getActivity())
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setTitle("Discard")
                    .setMessage("Do you want to discard the current entry?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            navigateTo(R.id.Menu_Home, new Menu_Home());
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }
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

    private void setIncompleteEntries(File file){
        String[] entryContents = IOHandler.interpretEntry(getActivity(), file, "Form1C");
        input_RegNo.setText(entryContents[0]);
        spinner.setSelection(getIndex(spinner,entryContents[1]));
        input_GateOut.setText(entryContents[2]);
    }

    private void setCompleteEntries(File file){
        String[] entryContents = IOHandler.interpretEntry(getActivity(), file, "Form1C");
        input_RegNo.setText(entryContents[0]);
        spinner.setSelection(getIndex(spinner,entryContents[1]));
        input_GateOut.setText(entryContents[2]);

        input_RegNo.setEnabled(false);
        spinner.setEnabled(false);
        input_GateOut.setEnabled(false);

        button_SetGateOut.setEnabled(false);
        buttonDone.setEnabled(false);
        buttonDiscard.setEnabled(false);
        buttonSave.setEnabled(false);
    }

    private int getIndex(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }
}
