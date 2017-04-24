package com.homemade.tianp.crossborder;import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class CustomAdapter_CompleteForms extends ArrayAdapter<String> {

    public CustomAdapter_CompleteForms(Context context, String[] filenames) {
        super(context, R.layout.custom_row_complete_forms, filenames);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.custom_row_complete_forms, parent,false);
        String fileName = getItem(position);
        TextView fileNameTextView = (TextView) customView.findViewById(R.id.adapter_Filename);

        fileNameTextView.setText(fileName);
        return customView;
    }

}

