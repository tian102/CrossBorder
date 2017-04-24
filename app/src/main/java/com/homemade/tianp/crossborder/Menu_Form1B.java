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
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Menu_Form1B extends Fragment {

    String RegNo = "", EntryTime = "", SubmissionToCustoms = "", InspectIn = "", InspectOut = "", ReleaseOrder = "",
            GateOut = "";

    String saveDialog_Filename = "";

    String form1B_Header = "Reg. No.;Entry Time;Submission to Customs;Inspection in:;Inspection out:;Release Order;Gate Out (Departure);";

    EditText input_RegNo, input_EntryTime, input_SubmissionToCustoms, input_InspectIn, input_InspectOut,
            input_ReleaseOrder, input_GateOut;

    Button button_SetEntryTime, buttonDone, buttonSave, buttonDiscard, button_SetInspectIn, button_SetInspectOut, button_SetReleaseOrder, button_SetGateOut;

    public static boolean incompleteEntry = false;

    public static File incompleteFile;

    public static boolean completeEntry = false;

    public static File completeFile;

    public Menu_Form1B() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_menu_form1b, container, false);

        return v;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("New Form 1B Entry");

        IOHandler.directorySetup(getActivity());
        setupSetButtons(view);
        setupEditTexts(view);
        setupBottomButtons(view);
        startupSettings();
    }


    private void setupSetButtons(View view){
        button_SetEntryTime = (Button)view.findViewById(R.id.Button_EntryTime);
        button_SetInspectIn = (Button)view.findViewById(R.id.Button_InspectIn);
        button_SetInspectOut = (Button)view.findViewById(R.id.Button_InspectOut);
        button_SetReleaseOrder = (Button)view.findViewById(R.id.Button_ReleaseOrder);
        button_SetGateOut = (Button)view.findViewById(R.id.Button_GateOut);
    }

    private void setupEditTexts(View view){
        input_RegNo = ((EditText) view.findViewById(R.id.Input_RegNo));
        input_EntryTime = ((EditText) view.findViewById(R.id.Input_EntryTime));
        input_SubmissionToCustoms = ((EditText) view.findViewById(R.id.Input_SubmissionCustoms));
        input_InspectIn = ((EditText) view.findViewById(R.id.Input_InspectIn));
        input_InspectOut = ((EditText) view.findViewById(R.id.Input_InspectOut));
        input_ReleaseOrder = ((EditText) view.findViewById(R.id.Input_ReleaseOrder));
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
            getActivity().setTitle("New Form 1B Entry");
        }else if(completeEntry){
            setCompleteEntries(completeFile);
            getActivity().setTitle("Form 1B - " + completeFile.getName());
            completeEntry = false;
        }else if(incompleteEntry){
            setIncompleteEntries(incompleteFile);
            getActivity().setTitle("Form 1B - " + incompleteFile.getName());
            incompleteEntry = false;
        }
    }

    private boolean checkFields() {
        if(RegNo.equals("") || EntryTime.equals("") || SubmissionToCustoms.equals("") ||
                InspectIn.equals("") || InspectOut.equals("") || ReleaseOrder.equals("") ||
                GateOut.equals("")){
            return false;
        }else {
            return true;
        }
    }

    private void getDataFromFields(View view){

        RegNo = checkNull(input_RegNo.getText().toString());
        EntryTime = checkNull(input_EntryTime.getText().toString());
        SubmissionToCustoms = checkNull(input_SubmissionToCustoms.getText().toString());
        InspectIn = checkNull(input_InspectIn.getText().toString());
        InspectOut = checkNull(input_InspectOut.getText().toString());
        ReleaseOrder = checkNull(input_ReleaseOrder.getText().toString());
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
                    if(IOHandler.checkIfFileExist(getActivity(), IOHandler.mainDirectory_Form1B_IncompleteEntries + "", saveDialog_Filename + ".csv")) {
                        Toast.makeText(context, "Filename already exist", Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, "Entry not saved", Toast.LENGTH_SHORT).show();
                    }else {
                        String text = RegNo + ";" + EntryTime + ";" + SubmissionToCustoms + ";" + InspectIn + ";" + InspectOut + ";" + ReleaseOrder + ";" + GateOut;

                        try {
                            IOHandler.createFile(getActivity(),IOHandler.mainDirectory_Form1B_IncompleteEntries, saveDialog_Filename + ".csv",form1B_Header);
                            IOHandler.appendToFile(getActivity(),IOHandler.mainDirectory_Form1B_IncompleteEntries, saveDialog_Filename + ".csv",text);
                            Toast.makeText(context, saveDialog_Filename + " saved", Toast.LENGTH_SHORT).show();

                            MainActivity.fragmentcheckSelectedMenuItem(R.id.Menu_Home);
                            navigateTo(R.id.Menu_Home, new Menu_Home());

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
            String text = RegNo + ";" + EntryTime + ";" + SubmissionToCustoms + ";" + InspectIn + ";" + InspectOut + ";" + ReleaseOrder + ";" + GateOut;

            try {
                Calendar c = Calendar.getInstance();

                SimpleDateFormat df = new SimpleDateFormat("HH-mm-ss");
                String formattedDate = df.format(c.getTime());
                String entryName = formattedDate + "_" + RegNo;
                String incompleteFormFileName = "IncompleteForm1B.csv";
                IOHandler.createFile(getActivity(),IOHandler.mainDirectory_Form1B_CompleteEntries, entryName + ".csv",form1B_Header);
                IOHandler.appendToFile(getActivity(),IOHandler.mainDirectory_Form1B_CompleteEntries, entryName + ".csv",text);

                if(IOHandler.checkIfFileExist(getActivity(),IOHandler.mainDirectory_Form1B_IncompleteForms, incompleteFormFileName)){
                    IOHandler.appendToFile(getActivity(),IOHandler.mainDirectory_Form1B_IncompleteForms,incompleteFormFileName,text);
                }else {
                    IOHandler.createFile(getActivity(),IOHandler.mainDirectory_Form1B_IncompleteForms, incompleteFormFileName,form1B_Header);
                    IOHandler.appendToFile(getActivity(),IOHandler.mainDirectory_Form1B_IncompleteForms, incompleteFormFileName,text);
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

    private String checkNull(String string){
        if(string == null){
            string = "";
        }
        return string;
    }

    private void entryDiscard(){
        if (getActivity().getTitle() != "New Form 1B Entry"){
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

    private void navigateTo(int selectedNavigationId, Fragment fragment){
        MenuItem menuItem = MainActivity.nav.getMenu().findItem(selectedNavigationId);
        menuItem.isChecked();
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        ft.replace(R.id.content_main,fragment);
        ft.commit();
    }

    private void setIncompleteEntries(File file){
        String[] entryContents = IOHandler.interpretEntry(getActivity(), file, "Form1B");
        input_RegNo.setText(entryContents[0]);
        input_EntryTime.setText(entryContents[1]);
        input_SubmissionToCustoms.setText(entryContents[2]);
        input_InspectIn.setText(entryContents[3]);
        input_InspectOut.setText(entryContents[4]);
        input_ReleaseOrder.setText(entryContents[5]);
        input_GateOut.setText(entryContents[6]);
    }

    private void setCompleteEntries(File file){
        String[] entryContents = IOHandler.interpretEntry(getActivity(), file, "Form1B");
        input_RegNo.setText(entryContents[0]);
        input_EntryTime.setText(entryContents[1]);
        input_SubmissionToCustoms.setText(entryContents[2]);
        input_InspectIn.setText(entryContents[3]);
        input_InspectOut.setText(entryContents[4]);
        input_ReleaseOrder.setText(entryContents[5]);
        input_GateOut.setText(entryContents[6]);

        input_RegNo.setEnabled(false);
        input_EntryTime.setEnabled(false);
        input_SubmissionToCustoms.setEnabled(false);
        input_InspectIn.setEnabled(false);
        input_InspectOut.setEnabled(false);
        input_ReleaseOrder.setEnabled(false);
        input_GateOut.setEnabled(false);

        button_SetEntryTime.setEnabled(false);
        buttonDone.setEnabled(false);
        buttonSave.setEnabled(false);
        buttonDiscard.setEnabled(false);
        button_SetInspectIn.setEnabled(false);
        button_SetInspectOut.setEnabled(false);
        button_SetReleaseOrder.setEnabled(false);
        button_SetGateOut.setEnabled(false);
    }
}
