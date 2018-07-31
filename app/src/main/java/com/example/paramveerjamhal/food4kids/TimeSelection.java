package com.example.paramveerjamhal.food4kids;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TimeSelection extends AppCompatActivity{
    TextView rel_contact;
    Spinner sItems;
    ListView listView;
    ArrayList<Integer> dataModels;
    ArrayAdapter<Integer> customAdaptor;
    String spinnerText;

    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Select Time");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_selection);
        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("morning");
        spinnerArray.add("afternoon");
        spinnerArray.add("evening");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sItems = (Spinner) findViewById(R.id.spinner);
        sItems.setAdapter(adapter);

        listView = (ListView) findViewById(R.id.list);

        dataModels = new ArrayList<>();
        int j = 8;
        for (int i = 0; i < 13; i++) {


            dataModels.add(j);
            j = j + 1;

        }

        customAdaptor = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, dataModels){
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the current item from ListView
                View view = super.getView(position,convertView,parent);
               /* if(position %2 == 1)
                {*/
                    // Set a background color for ListView regular row/item

                      /* view.setBackgroundColor(Color.parseColor("#FFB6B546"));*/






               /* }
                else
                {
                    // Set the background color for alternate row/item
                    view.setBackgroundColor(Color.parseColor("#FFCCCB4C"));
                }*/
                return view;
            }
        };

        spinnerText="";
        listView.setAdapter(customAdaptor);


        sItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerText = sItems.getSelectedItem().toString();


                    AlertDialog alertDialog = new AlertDialog.Builder(TimeSelection.this).create();
                    alertDialog.setTitle("Information");
                    alertDialog.setMessage("Your Request has been Sent");


                    alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // here you can add functions
                        }
                    });
                    alertDialog.show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });



    }
}
