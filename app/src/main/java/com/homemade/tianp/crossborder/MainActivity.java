package com.homemade.tianp.crossborder;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;

import java.io.File;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static NavigationView nav;
    public static int[] navDrawerMenuItems = new int[]{
            R.id.Menu_Home,
            R.id.Menu_NewFormEntry,
            R.id.Menu_New1A,
            R.id.Menu_New1B,
            R.id.Menu_New1C,
            R.id.Menu_Manage1A,
            R.id.Menu_Complete1A,
            R.id.Menu_Incomplete1A,
            R.id.Menu_Manage1B,
            R.id.Menu_Complete1B,
            R.id.Menu_Incomplete1B,
            R.id.Menu_Manage1C,
            R.id.Menu_Complete1C,
            R.id.Menu_Incomplete1C,
            R.id.Menu_ViewAll,
            R.id.Menu_View1A,
            R.id.Menu_View1B,
            R.id.Menu_View1C,
            R.id.Menu_Share,
            R.id.Menu_Sync
    };
    public static final int REQUEST_ACCESS_WRITE_EXTERNAL = 0;
    public static final int REQUEST_ACCESS_READ_EXTERNAL = 0;
    public static boolean backPressed = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        nav = navigationView;
        navigationView.setNavigationItemSelectedListener(this);
        MenuItem menuItem = nav.getMenu().findItem(R.id.Menu_Home);
        menuItem.isChecked();
        onNavigationItemSelected(menuItem);

        /*
        * Check read and write permissions
        */
        checkPermissions(0);
        checkPermissions(1);
        /*
        * Set up directory
        */
        IOHandler.directorySetup(this);

    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            MenuItem menuItem;
            menuItem = nav.getMenu().findItem(R.id.Menu_Home);

            if(!menuItem.isChecked()){
                backPressed = true;
                onNavigationItemSelected(menuItem);
            }else {
                //ExitDialog
                basicAlert("Exit", "Are you sure you want to close the application?","quit");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        int id = item.getItemId();

        switch (id) {
            //Home screen
            case R.id.Menu_Home:
                if(!nav.getMenu().findItem(id).isChecked()){
                    checkSelectedMenuItem(id);
                    fragment = new Menu_Home();
                }
                closeDrawer();

                break;

            //New Form Entry
            case R.id.Menu_NewFormEntry:
                toggleItemVisibility(R.id.Menu_NewFormEntry);
                break;
            case R.id.Menu_New1A:
                //Check if want to store current form entry or discard dialog
                if(!nav.getMenu().findItem(id).isChecked()){
                    checkSelectedMenuItem(id);
                    fragment = new Menu_Form1A();
                }
                closeDrawer();
                break;
            case R.id.Menu_New1B:
                //Check if want to store current form entry or discard dialog
                if(!nav.getMenu().findItem(id).isChecked()){
                    checkSelectedMenuItem(id);
                    fragment = new Menu_Form1B();
                }
                closeDrawer();
                break;
            case R.id.Menu_New1C:
                //Check if want to store current form entry or discard dialog

                if(!nav.getMenu().findItem(id).isChecked()){
                    checkSelectedMenuItem(id);
                    fragment = new Menu_Form1C();
                }

                closeDrawer();
                break;

            //Manage Entries
            case R.id.Menu_Manage1A:
                toggleItemVisibility(R.id.Menu_Manage1A);
                break;
            case R.id.Menu_Complete1A:
                if(!nav.getMenu().findItem(id).isChecked()){
                    checkSelectedMenuItem(id);
                    Menu_CompleteEntries.setPath(IOHandler.mainDirectory_Form1A_CompleteEntries);
                    IOHandler.setCurrentDirectory(IOHandler.mainDirectory_Form1A_CompleteEntries);
                    fragment = new Menu_CompleteEntries();
                }
                closeDrawer();
                break;
            case R.id.Menu_Incomplete1A:

                if(!nav.getMenu().findItem(id).isChecked()){
                    checkSelectedMenuItem(id);
                    Menu_IncompleteEntries.setPath(IOHandler.mainDirectory_Form1A_IncompleteEntries);
                    IOHandler.setCurrentDirectory(IOHandler.mainDirectory_Form1A_IncompleteEntries);
                    fragment = new Menu_IncompleteEntries();
                }
                closeDrawer();
                break;
            case R.id.Menu_Manage1B:
                toggleItemVisibility(R.id.Menu_Manage1B);
                break;
            case R.id.Menu_Complete1B:
                if(!nav.getMenu().findItem(id).isChecked()){
                    checkSelectedMenuItem(id);
                    Menu_CompleteEntries.setPath(IOHandler.mainDirectory_Form1B_CompleteEntries);
                    IOHandler.setCurrentDirectory(IOHandler.mainDirectory_Form1B_CompleteEntries);
                    fragment = new Menu_CompleteEntries();
                }
                closeDrawer();
                break;
            case R.id.Menu_Incomplete1B:
                if(!nav.getMenu().findItem(id).isChecked()){
                    checkSelectedMenuItem(id);
                    Menu_IncompleteEntries.setPath(IOHandler.mainDirectory_Form1B_IncompleteEntries);
                    IOHandler.setCurrentDirectory(IOHandler.mainDirectory_Form1B_IncompleteEntries);
                    fragment = new Menu_IncompleteEntries();
                }

                closeDrawer();
                break;
            case R.id.Menu_Manage1C:
                toggleItemVisibility(R.id.Menu_Manage1C);
                break;
            case R.id.Menu_Complete1C:
                if(!nav.getMenu().findItem(id).isChecked()){
                    checkSelectedMenuItem(id);
                    Menu_CompleteEntries.setPath(IOHandler.mainDirectory_Form1C_CompleteEntries);
                    IOHandler.setCurrentDirectory(IOHandler.mainDirectory_Form1C_CompleteEntries);
                    fragment = new Menu_CompleteEntries();
                }

                closeDrawer();
                break;
            case R.id.Menu_Incomplete1C:
                if(!nav.getMenu().findItem(id).isChecked()){
                    checkSelectedMenuItem(id);
                    Menu_IncompleteEntries.setPath(IOHandler.mainDirectory_Form1C_IncompleteEntries);
                    IOHandler.setCurrentDirectory(IOHandler.mainDirectory_Form1C_IncompleteEntries);
                    fragment = new Menu_IncompleteEntries();
                }
                closeDrawer();
                break;

            //View Forms
            case R.id.Menu_ViewAll:
                toggleItemVisibility(R.id.Menu_ViewAll);
                break;
            case R.id.Menu_View1A:
                if(!nav.getMenu().findItem(id).isChecked()){
                    checkSelectedMenuItem(id);
                    Menu_IncompleteForms.setPath(IOHandler.mainDirectory_Form1A_IncompleteForms);
                    IOHandler.setCurrentDirectory(IOHandler.mainDirectory_Form1A_IncompleteForms);
                    fragment = new Menu_IncompleteForms();
                }
                closeDrawer();
                break;
            case R.id.Menu_View1B:
                if(!nav.getMenu().findItem(id).isChecked()){
                    checkSelectedMenuItem(id);
                    Menu_IncompleteForms.setPath(IOHandler.mainDirectory_Form1B_IncompleteForms);
                    IOHandler.setCurrentDirectory(IOHandler.mainDirectory_Form1B_IncompleteForms);
                    fragment = new Menu_IncompleteForms();
                }
                closeDrawer();
                break;
            case R.id.Menu_View1C:
                if(!nav.getMenu().findItem(id).isChecked()){
                    checkSelectedMenuItem(id);
                    Menu_IncompleteForms.setPath(IOHandler.mainDirectory_Form1C_IncompleteForms);
                    IOHandler.setCurrentDirectory(IOHandler.mainDirectory_Form1C_IncompleteForms);
                    fragment = new Menu_IncompleteForms();
                }
                closeDrawer();
                break;

            //Communication
            case R.id.Menu_Share:
                if(!nav.getMenu().findItem(id).isChecked()){
                    checkSelectedMenuItem(id);
                    fragment = new Menu_CompleteForms();
                }
                closeDrawer();
                break;
            case R.id.Menu_Sync:
                if(!nav.getMenu().findItem(id).isChecked()){
                    checkSelectedMenuItem(id);
                }

                closeDrawer();
                break;
        }
        if (fragment!=null){
            IOHandler.setCurrentMenuItemId(id);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (backPressed){
                ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
                backPressed =false;
            }else {
                ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            }
            ft.replace(R.id.content_main,fragment);
            ft.commit();
        }
        return true;
    }

    public void shareForm(View view){
        LinearLayout vwParentRow = (LinearLayout)view.getParent();
        TextView child = (TextView)vwParentRow.getChildAt(0);
        //File file = new File(IOHandler.getCurrentDirectory() + "/" + child.getText());

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("*/*");
        File file1 = new File(IOHandler.mainDirectory_Form1A_CompleteForms + "/" + child.getText());
        File file2 = new File(IOHandler.mainDirectory_Form1B_CompleteForms + "/" + child.getText());
        File file3 = new File(IOHandler.mainDirectory_Form1C_CompleteForms + "/" + child.getText());
        if (file1.exists()){
            sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file1));
        }else if (file2.exists()){
            sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file2));
        }else if (file3.exists()){
            sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file3));
        }
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private void closeDrawer(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void setMenuItemIcon(MenuItem menuItem, int id){
        if (menuItem.isVisible()){
            menuItem = nav.getMenu().findItem(id);
            menuItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_keyboard_arrow_up));
        }else{
            menuItem = nav.getMenu().findItem(id);
            menuItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_keyboard_arrow_down));
        }
    }

    private void setVisibility_NewEntries(boolean isVisible){
        MenuItem menuItem;
        menuItem = nav.getMenu().findItem(R.id.Menu_New1A);
        menuItem.setVisible(isVisible);
        menuItem = nav.getMenu().findItem(R.id.Menu_New1B);
        menuItem.setVisible(isVisible);
        menuItem = nav.getMenu().findItem(R.id.Menu_New1C);
        menuItem.setVisible(isVisible);

        //Change Icon
        setMenuItemIcon(menuItem,R.id.Menu_NewFormEntry);
    }

    private void setVisibility_EntriesA(boolean isVisible){
        MenuItem menuItem;
        menuItem = nav.getMenu().findItem(R.id.Menu_Complete1A);
        menuItem.setVisible(isVisible);
        menuItem = nav.getMenu().findItem(R.id.Menu_Incomplete1A);
        menuItem.setVisible(isVisible);

        //Change Icon
        setMenuItemIcon(menuItem,R.id.Menu_Manage1A);
    }

    private void setVisibility_EntriesB(boolean isVisible){
        MenuItem menuItem;
        menuItem = nav.getMenu().findItem(R.id.Menu_Complete1B);
        menuItem.setVisible(isVisible);
        menuItem = nav.getMenu().findItem(R.id.Menu_Incomplete1B);
        menuItem.setVisible(isVisible);

        //Change Icon
        setMenuItemIcon(menuItem,R.id.Menu_Manage1B);
    }

    private void setVisibility_EntriesC(boolean isVisible){
        MenuItem menuItem;
        menuItem = nav.getMenu().findItem(R.id.Menu_Complete1C);
        menuItem.setVisible(isVisible);
        menuItem = nav.getMenu().findItem(R.id.Menu_Incomplete1C);
        menuItem.setVisible(isVisible);

        //Change Icon
        setMenuItemIcon(menuItem,R.id.Menu_Manage1C);
    }

    private void setVisibility_ViewForms(boolean isVisible){
        MenuItem menuItem;
        menuItem = nav.getMenu().findItem(R.id.Menu_View1A);
        menuItem.setVisible(isVisible);
        menuItem = nav.getMenu().findItem(R.id.Menu_View1B);
        menuItem.setVisible(isVisible);
        menuItem = nav.getMenu().findItem(R.id.Menu_View1C);
        menuItem.setVisible(isVisible);

        //Change Icon
        setMenuItemIcon(menuItem,R.id.Menu_ViewAll);
    }

    private void setItemsVisibilityFalse(int id){
        switch (id){
            case R.id.Menu_NewFormEntry:
                setVisibility_EntriesA(false);
                setVisibility_EntriesB(false);
                setVisibility_EntriesC(false);
                setVisibility_ViewForms(false);
                break;
            case R.id.Menu_Manage1A:
                setVisibility_NewEntries(false);
                setVisibility_EntriesB(false);
                setVisibility_EntriesC(false);
                setVisibility_ViewForms(false);
                break;
            case R.id.Menu_Manage1B:
                setVisibility_NewEntries(false);
                setVisibility_EntriesA(false);
                setVisibility_EntriesC(false);
                setVisibility_ViewForms(false);
                break;
            case R.id.Menu_Manage1C:
                setVisibility_NewEntries(false);
                setVisibility_EntriesA(false);
                setVisibility_EntriesB(false);
                setVisibility_ViewForms(false);
                break;
            case R.id.Menu_ViewAll:
                setVisibility_NewEntries(false);
                setVisibility_EntriesA(false);
                setVisibility_EntriesB(false);
                setVisibility_EntriesC(false);
                break;
        }

    }

    private void toggleItemVisibility(int id){
        MenuItem menuItem;
        setItemsVisibilityFalse(id);
        switch(id){
            case R.id.Menu_NewFormEntry:
                menuItem = nav.getMenu().findItem(R.id.Menu_New1A);
                menuItem.setVisible(!menuItem.isVisible());
                menuItem = nav.getMenu().findItem(R.id.Menu_New1B);
                menuItem.setVisible(!menuItem.isVisible());
                menuItem = nav.getMenu().findItem(R.id.Menu_New1C);
                menuItem.setVisible(!menuItem.isVisible());

                //Change Icon
                setMenuItemIcon(menuItem,R.id.Menu_NewFormEntry);

                break;
            case R.id.Menu_Manage1A:
                menuItem = nav.getMenu().findItem(R.id.Menu_Complete1A);
                menuItem.setVisible(!menuItem.isVisible());
                menuItem = nav.getMenu().findItem(R.id.Menu_Incomplete1A);
                menuItem.setVisible(!menuItem.isVisible());

                //Change Icon
                setMenuItemIcon(menuItem,R.id.Menu_Manage1A);

                break;
            case R.id.Menu_Manage1B:
                menuItem = nav.getMenu().findItem(R.id.Menu_Complete1B);
                menuItem.setVisible(!menuItem.isVisible());
                menuItem = nav.getMenu().findItem(R.id.Menu_Incomplete1B);
                menuItem.setVisible(!menuItem.isVisible());

                //Change Icon
                setMenuItemIcon(menuItem,R.id.Menu_Manage1B);

                break;
            case R.id.Menu_Manage1C:
                menuItem = nav.getMenu().findItem(R.id.Menu_Complete1C);
                menuItem.setVisible(!menuItem.isVisible());
                menuItem = nav.getMenu().findItem(R.id.Menu_Incomplete1C);
                menuItem.setVisible(!menuItem.isVisible());

                //Change Icon
                setMenuItemIcon(menuItem,R.id.Menu_Manage1C);

                break;
            case R.id.Menu_ViewAll:
                menuItem = nav.getMenu().findItem(R.id.Menu_View1A);
                menuItem.setVisible(!menuItem.isVisible());
                menuItem = nav.getMenu().findItem(R.id.Menu_View1B);
                menuItem.setVisible(!menuItem.isVisible());
                menuItem = nav.getMenu().findItem(R.id.Menu_View1C);
                menuItem.setVisible(!menuItem.isVisible());

                //Change Icon
                setMenuItemIcon(menuItem,R.id.Menu_ViewAll);

                break;
        }
    }

    /*
    Determines which EditText to associate with time select
    **/
    public void timeSetDialog(View view){
        String dialogType = "Time";
        EditText editText = null;
        switch (view.getId()) {
            case R.id.Button_EntryTime:
                editText = (EditText)findViewById(R.id.Input_EntryTime);
                break;
            case R.id.Button_InspectIn:
                editText = (EditText)findViewById(R.id.Input_InspectIn);
                break;
            case R.id.Button_InspectOut:
                editText = (EditText)findViewById(R.id.Input_InspectOut);
                break;
            case R.id.Button_ReleaseOrder:
                editText = (EditText)findViewById(R.id.Input_ReleaseOrder);
                break;
            case R.id.Button_GateOut:
                editText = (EditText)findViewById(R.id.Input_GateOut);
                break;
            case R.id.Button_EntryDate:
                editText = (EditText)findViewById(R.id.Input_Date);
                dialogType = "Date";
                break;
            case R.id.Button_StartTime:
                editText = (EditText)findViewById(R.id.Input_StartTime);
                break;
            case R.id.Button_FinishTime:
                editText = (EditText)findViewById(R.id.Input_FinishTime);
                break;
        }
        setupDialog(this,editText, dialogType);
    }

    /*
    Creates a dialog on which the user can select their entry time
    **/
    private void setupDialog(final Context context, final EditText editText, String dialogType){

        switch (dialogType) {
            case "Time":
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);


                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        String selectedHour_string = "";
                        String selectedMinute_string = "";

                        if (selectedHour < 10) {
                            selectedHour_string = "0" + Integer.toString(selectedHour);
                        } else {
                            selectedHour_string = Integer.toString(selectedHour);
                        }

                        if (selectedMinute < 10) {
                            selectedMinute_string = "0" + Integer.toString(selectedMinute);

                        } else {
                            selectedMinute_string = Integer.toString(selectedMinute);
                        }
                        editText.setText("");
                        editText.setText(selectedHour_string + ":" + selectedMinute_string + ":00");
                    }
                }, hour, minute, true);//Yes 24 hour time

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                break;
            case "Date":
                Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editText.setText("");
                        editText.setText(dayOfMonth + " " + getMonth(month) + " " + year);
                    }
                }, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }
    }

    /*
    Check read and write permissions
    **/
    private void checkPermissions(int i){
        /*
        * Check READ_EXTERNAL_STORAGE permission
        * */
        if (i == 0) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_ACCESS_READ_EXTERNAL);
            }
        }
        /*
        * Check WRITE_EXTERNAL_STORAGE permission
        * */
        else if(i == 1)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_ACCESS_WRITE_EXTERNAL);
        }
    }


    public void delFile(View view){
        //get the row the clicked button is in
        LinearLayout vwParentRow = (LinearLayout)view.getParent();
        TextView child = (TextView)vwParentRow.getChildAt(0);
        File file = new File(IOHandler.getCurrentDirectory() + "/" + child.getText());
        basicAlert("Delete", "Are you sure you want to delete this entry?","delete", this, file);
        vwParentRow.refreshDrawableState();
    }

    public void editFile(View view){
        //get the row the clicked button is in
        LinearLayout vwParentRow = (LinearLayout)view.getParent();
        TextView child = (TextView)vwParentRow.getChildAt(0);
        File file = new File(IOHandler.getCurrentDirectory() + "/" + child.getText());

        if (IOHandler.getCurrentDirectory() == IOHandler.mainDirectory_Form1A_IncompleteEntries){
            Menu_Form1A.incompleteFile = file;
            Menu_Form1A.incompleteEntry = true;
            navigateTo(R.id.Menu_New1A);
        }else if (IOHandler.getCurrentDirectory() == IOHandler.mainDirectory_Form1B_IncompleteEntries){
            Menu_Form1B.incompleteFile = file;
            Menu_Form1B.incompleteEntry = true;
            navigateTo(R.id.Menu_New1B);

        }else if (IOHandler.getCurrentDirectory() == IOHandler.mainDirectory_Form1C_IncompleteEntries){
            Menu_Form1C.incompleteFile = file;
            Menu_Form1C.incompleteEntry = true;
            navigateTo(R.id.Menu_New1C);

        }
    }

    public void viewFile(View view){
        //get the row the clicked button is in
        LinearLayout vwParentRow = (LinearLayout)view.getParent();
        TextView child = (TextView)vwParentRow.getChildAt(0);
        File file = new File(IOHandler.getCurrentDirectory() + "/" + child.getText());

        if (IOHandler.getCurrentDirectory() == IOHandler.mainDirectory_Form1A_CompleteEntries){
            Menu_Form1A.completeFile = file;
            Menu_Form1A.completeEntry = true;
            navigateTo(R.id.Menu_New1A);
        }else if (IOHandler.getCurrentDirectory() == IOHandler.mainDirectory_Form1B_CompleteEntries){
            Menu_Form1B.completeFile = file;
            Menu_Form1B.completeEntry = true;
            navigateTo(R.id.Menu_New1B);

        }else if (IOHandler.getCurrentDirectory() == IOHandler.mainDirectory_Form1C_CompleteEntries){
            Menu_Form1C.completeFile = file;
            Menu_Form1C.completeEntry = true;
            navigateTo(R.id.Menu_New1C);

        }
    }

    public  void completeForm(View view){

        String currentDirectory = IOHandler.getCurrentDirectory();
        if (currentDirectory == IOHandler.mainDirectory_Form1A_IncompleteForms){
            Menu_FormFinalize.setSelectedForm("Form1A");
            checkSelectedMenuItem(R.id.Menu_View1A);
        }else if (currentDirectory == IOHandler.mainDirectory_Form1B_IncompleteForms){
            Menu_FormFinalize.setSelectedForm("Form1B");
            checkSelectedMenuItem(R.id.Menu_View1B);
        }else if (currentDirectory == IOHandler.mainDirectory_Form1C_IncompleteForms){
            Menu_FormFinalize.setSelectedForm("Form1C");
            checkSelectedMenuItem(R.id.Menu_View1C);
        }
        newFragment(new Menu_FormFinalize());
    }

    public void viewForm(View view){
        File file = null;
        String currentDirectory = IOHandler.getCurrentDirectory();
        
        if (currentDirectory == IOHandler.mainDirectory_Form1A_IncompleteForms){
            file = new File(IOHandler.mainDirectory_Form1A_IncompleteForms +"/IncompleteForm1A.csv");
        }else if (currentDirectory == IOHandler.mainDirectory_Form1B_IncompleteForms){
            file = new File(IOHandler.mainDirectory_Form1B_IncompleteForms +"/IncompleteForm1B.csv");
        }else if (currentDirectory == IOHandler.mainDirectory_Form1C_IncompleteForms){
            file = new File(IOHandler.mainDirectory_Form1C_IncompleteForms +"/IncompleteForm1C.csv");
        }
        
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(file),"application/vnd.ms-excel");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Open File");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "ActivityNotFound", Toast.LENGTH_SHORT).show();
        }
    }

    public void basicAlert(final String Heading, String Message, String Command){
        switch (Command) {
            case "quit":
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle(Heading)
                        .setMessage(Message)

                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Menu_Form1A.incompleteEntry = false;
                                Menu_Form1B.incompleteEntry = false;
                                Menu_Form1C.incompleteEntry = false;
                                MainActivity.super.onBackPressed();
                                //finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                break;
            case "delete":
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle(Heading)
                        .setMessage(Message)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();

                break;


            default:
                Toast.makeText(this, "Error 404: Something big went wrong...", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    public void basicAlert(final String Heading, String Message, String Command, final Context context, final File file){
        switch (Command) {
            case "quit":
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle(Heading)
                        .setMessage(Message)

                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                break;
            case "delete":
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle(Heading)
                        .setMessage(Message)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                IOHandler.deleteFile(context,file);
                                refreshCurrentFragment(IOHandler.getCurrentMenuItemId());
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();

                break;


            default:
                Toast.makeText(this, "Error 404: Something big went wrong...", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    private void refreshCurrentFragment(int id){
        // Reload current fragment
        MenuItem menuItem = MainActivity.nav.getMenu().findItem(id);
        menuItem.isChecked();
        onNavigationItemSelected(menuItem);
    }

    private void checkSelectedMenuItem(int id){
        unCheckAllMenuItems();
        nav.getMenu().findItem(id).setChecked(true);
    }
    
    private void unCheckAllMenuItems(){
        for (int i = 0; i < navDrawerMenuItems.length; i++){
            nav.getMenu().findItem(navDrawerMenuItems[i]).setChecked(false);
        }
    }

    private void newFragment(Fragment fragment){
        if (fragment!=null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main,fragment);
            ft.commit();
        }
    }

    private void navigateTo(int id){
        MenuItem menuItem = MainActivity.nav.getMenu().findItem(id);
        onNavigationItemSelected(menuItem);
    }

    public void completeForm_Home(View view) {
        LinearLayout vwParentRow = (LinearLayout) view.findViewById(R.id.selectLayout);
        TextView child = (TextView) vwParentRow.getChildAt(1);
        if (child.getText().equals("IncompleteForm1A.csv")) {
            Menu_FormFinalize.setSelectedForm("Form1A");
            onNavigationItemSelected(nav.getMenu().findItem(R.id.Menu_View1A));
        } else if (child.getText().equals("IncompleteForm1B.csv")) {
            Menu_FormFinalize.setSelectedForm("Form1B");
            onNavigationItemSelected(nav.getMenu().findItem(R.id.Menu_View1B));
        } else if (child.getText().equals("IncompleteForm1C.csv")) {
            Menu_FormFinalize.setSelectedForm("Form1C");
            onNavigationItemSelected(nav.getMenu().findItem(R.id.Menu_View1C));
        }
        newFragment(new Menu_FormFinalize());
    }

    public static void fragmentcheckSelectedMenuItem(int id){
        fragmentunCheckAllMenuItems();
        nav.getMenu().findItem(id).setChecked(true);
    }

    public static void fragmentunCheckAllMenuItems(){
        for (int i = 0; i < navDrawerMenuItems.length; i++){
            nav.getMenu().findItem(navDrawerMenuItems[i]).setChecked(false);
        }
    }

    public static String getMonth(int monthInt){
        String monthString = "";
        switch (monthInt){
            case 0:
                monthString = "Jan";
                break;
            case 1:
                monthString = "Feb";
                break;
            case 2:
                monthString = "Mar";
                break;
            case 3:
                monthString = "Apr";
                break;
            case 4:
                monthString = "May";
                break;
            case 5:
                monthString = "Jun";
                break;
            case 6:
                monthString = "Jul";
                break;
            case 7:
                monthString = "Aug";
                break;
            case 8:
                monthString = "Sept";
                break;
            case 9:
                monthString = "Oct";
                break;
            case 10:
                monthString = "Nov";
                break;
            case 11:
                monthString = "Dec";
                break;
        }
        return monthString;
    }

    public void displayHelpPage(MenuItem menuItem) {

        fragmentunCheckAllMenuItems();
        Fragment fragment = new Menu_Help();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        ft.replace(R.id.content_main,fragment);
        ft.commit();
    }
}
