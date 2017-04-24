package com.homemade.tianp.crossborder;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;
import org.apache.commons.io.FileUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianp on 18 Apr 2017.
 */

public class IOHandler {

    public static String mainDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() + "/CrossBorder";

    public static String mainDirectory_Form1A = mainDirectory + "/Form1A";
    public static String mainDirectory_Form1B = mainDirectory + "/Form1B";
    public static String mainDirectory_Form1C = mainDirectory + "/Form1C";

    public static String mainDirectory_Form1A_CompleteEntries = mainDirectory_Form1A + "/CompleteEntries";
    public static String mainDirectory_Form1A_CompleteForms = mainDirectory_Form1A + "/CompleteForms";
    public static String mainDirectory_Form1A_IncompleteEntries = mainDirectory_Form1A + "/IncompleteEntries";
    public static String mainDirectory_Form1A_IncompleteForms = mainDirectory_Form1A + "/IncompleteForms";

    public static String mainDirectory_Form1B_CompleteEntries = mainDirectory_Form1B + "/CompleteEntries";
    public static String mainDirectory_Form1B_CompleteForms = mainDirectory_Form1B + "/CompleteForms";
    public static String mainDirectory_Form1B_IncompleteEntries = mainDirectory_Form1B + "/IncompleteEntries";
    public static String mainDirectory_Form1B_IncompleteForms = mainDirectory_Form1B + "/IncompleteForms";

    public static String mainDirectory_Form1C_CompleteEntries = mainDirectory_Form1C + "/CompleteEntries";
    public static String mainDirectory_Form1C_CompleteForms = mainDirectory_Form1C + "/CompleteForms";
    public static String mainDirectory_Form1C_IncompleteEntries = mainDirectory_Form1C + "/IncompleteEntries";
    public static String mainDirectory_Form1C_IncompleteForms = mainDirectory_Form1C + "/IncompleteForms";

    public static String currentDirectory = "";
    public static int CurrentMenuItemId;

    public static void createSingleDirectory(Context context, String path){
        File dir = new File(path);
        try {
            if (!dir.exists()){
                dir.mkdirs();
            }
        }catch (Exception ex){
            Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public static void createMultipleDirectories(Context context, String[] paths){
        File[] directories = new File[paths.length];
        for (int i = 0; i < paths.length; i++){
            directories[i] = new File(paths[i]);
            try {
                if (!directories[i].exists()) {
                    directories[i].mkdirs();
                }
            }catch (Exception ex){
                Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void deleteAllFilesFromDirectory(Context context, String path){

        File file = new File(path);
        try {
            if (file.exists()){
                File[] files;
                files = file.listFiles();
                for (int i=0; i<files.length; i++) {
                    File myFile = new File(String.valueOf(files[i]));
                    myFile.delete();
                }
            }
        }catch (Exception ex){
            Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    public static void copyFileFromDir(Context context, String sourcePath, String sourceFilename, String destinationPath, String destinationFilename) throws FileNotFoundException {

        File sourceFile = new File(sourcePath + "/" + sourceFilename);
        File destinationFile = new File(destinationPath + "/" + destinationFilename);

        InputStream in = new FileInputStream(sourceFile);
        OutputStream out = new FileOutputStream(destinationFile);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        try {
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void renameFile(Context context, String path, String oldFilename, String newFilename){

        File oldFile = new File(path + "/" + oldFilename + ".csv");
        File newFile = new File(path + "/" + newFilename + ".csv");
        try {
            if (!oldFile.exists()){
                oldFile.renameTo(newFile);
            }
        }catch (Exception ex){
            Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    public static void createEmptyFile(Context context, String path, String fileName){
        File dir = new File(path);
        try {
            if (!dir.exists()){
                dir.mkdirs();
            }
        }catch (Exception ex){
            Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
        }

        File file = new File(dir, fileName);
        try {
            FileWriter writer = new FileWriter(file);
            writer.append("");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createFile(Context context, String path, String fileName, String text){
        File dir = new File(path);
        try {
            if (!dir.exists()){
                dir.mkdirs();
            }
        }catch (Exception ex){
            Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
        }

        File file = new File(dir, fileName);
        try {
            FileWriter writer = new FileWriter(file);
            writer.append(text);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void appendToFile(Context context, String path, String fileName, String text){
        File dir = new File(path);
        try {
            if (!dir.exists()){
                dir.mkdirs();
            }
        }catch (Exception ex){
            Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
        }


        File file = new File(dir, fileName);
        try {
            FileWriter writer = new FileWriter(file,true);
            writer.write("\n" + text);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkIfFileExist(Context context, String path, String fileName){
        File dir = new File(path, fileName);
        boolean exists = false;
        try {
            if (dir.exists()){
                exists = true;
            }else {
                exists = false;
            }
        }catch (Exception ex){
            Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
        }
        return exists;
    }

    public static List<String> readFromFile(Context context, String path, String fileName){
        //Get the text file
        File file = new File(path,fileName);

        //Read text from file
        StringBuilder text = new StringBuilder();
        List<String> fileContents = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
                fileContents.add(line);
            }
            br.close();
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();;
        }
        catch (IOException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
        return fileContents;
    }

    public static List<String> readFromFile(Context context, File file){

        //Read text from file
        StringBuilder text = new StringBuilder();
        List<String> fileContents = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
                fileContents.add(line);
            }
            br.close();
        }
        catch (IOException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
        return fileContents;
    }

    public static List<String> readDirectory(String path){
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        List<String> fileNames = new ArrayList<>();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                fileNames.add(file.getName());
            }
        }
        return fileNames;
    }

    public static void deleteFile(Context context, String path, String fileName){
        File file = new File(path + "/" + fileName);
        file.delete();
        if(file.exists()){
            try {
                file.getCanonicalFile().delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(file.exists()){
                context.deleteFile(file.getName());
            }
        }
    }

    public static void deleteFile(Context context, File file){
        file.delete();
        if(file.exists()){
            try {
                file.getCanonicalFile().delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(file.exists()){
                context.deleteFile(file.getName());
            }
        }
    }

    public static String[] interpretEntry(Context context, File file, String form){
        List<String> lines = readFromFile(context,file);
        String[] tempArray;
        String[] contents = lines.get(1).split(";");
        switch (form){
            case "Form1A":
                tempArray = new String[10];
                for (int i = 0; i < tempArray.length; i++){
                    tempArray[i] = "";
                }
                for (int i = 0; i < contents.length; i++){
                    tempArray[i] = contents[i];
                }
                contents = tempArray;
                break;
            case "Form1B":
                tempArray = new String[7];
                for (int i = 0; i < tempArray.length; i++){
                    tempArray[i] = "";
                }
                for (int i = 0; i < contents.length; i++){
                    tempArray[i] = contents[i];
                }
                contents = tempArray;

                break;
            case "Form1C":
                tempArray = new String[3];
                for (int i = 0; i < tempArray.length; i++){
                    tempArray[i] = "";
                }
                for (int i = 0; i < contents.length; i++){
                    tempArray[i] = contents[i];
                }
                contents = tempArray;

                break;
        }
        return contents;
    }

    public static String getCurrentDirectory(){
        return currentDirectory;
    }

    public static void setCurrentDirectory(String directory){
        currentDirectory = directory;
    }

    public static int getCurrentMenuItemId(){
        return CurrentMenuItemId;
    }

    public static void setCurrentMenuItemId(int id){
        CurrentMenuItemId = id;
    }

    public static void directorySetup(Context context){
        IOHandler.createSingleDirectory(context, IOHandler.mainDirectory);
        IOHandler.createMultipleDirectories(context, new String[]{
                IOHandler.mainDirectory_Form1A,
                IOHandler.mainDirectory_Form1B,
                IOHandler.mainDirectory_Form1C});

        IOHandler.createMultipleDirectories(context, new String[]{
                IOHandler.mainDirectory_Form1A_CompleteEntries,
                IOHandler.mainDirectory_Form1A_CompleteForms,
                IOHandler.mainDirectory_Form1A_IncompleteEntries,
                IOHandler.mainDirectory_Form1A_IncompleteForms});

        IOHandler.createMultipleDirectories(context, new String[]{
                IOHandler.mainDirectory_Form1B_CompleteEntries,
                IOHandler.mainDirectory_Form1B_CompleteForms,
                IOHandler.mainDirectory_Form1B_IncompleteEntries,
                IOHandler.mainDirectory_Form1B_IncompleteForms});

        IOHandler.createMultipleDirectories(context, new String[]{
                IOHandler.mainDirectory_Form1C_CompleteEntries,
                IOHandler.mainDirectory_Form1C_CompleteForms,
                IOHandler.mainDirectory_Form1C_IncompleteEntries,
                IOHandler.mainDirectory_Form1C_IncompleteForms});
    }
}
