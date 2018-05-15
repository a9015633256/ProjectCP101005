package com.example.yangwensing.myapplication.ExamSubject;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.yangwensing.myapplication.R;
import com.example.yangwensing.myapplication.main.Common;
import com.example.yangwensing.myapplication.main.MyTask;
import com.google.gson.JsonObject;

public class UpDateSubject extends Fragment {

    private final static String TAG = "itemSignin";
    private MyTask myTask;
    private EditText etTeacher, etSubject, etDate, etTitle, etContent;
    private Button btSure,btUpdate;
    private String ClassID = "";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.subject_create, container, false);
        getActivity().setTitle(R.string.updatesubject);
        etDate = view.findViewById(R.id.etDate);
        etTitle = view.findViewById(R.id.etTitle);
        etContent = view.findViewById(R.id.etContent);
        btSure = view.findViewById(R.id.btSure);
        etDate.setInputType(InputType.TYPE_NULL);
        btUpdate = view.findViewById(R.id.btUpdate);
        Bundle q = getArguments();
        String title = q.getString("title");
        String date = q.getString("date");
        String content = q.getString("content");
        ClassID = q.getString("ClassID");
        etTitle.setText(title);
        etDate.setText(date);
        etContent.setText(content);
        btUpdate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Bundle b = getArguments();
               String id = b.getString("id");
               String title = etTitle.getText().toString();
               String date = etDate.getText().toString();
               String content = etContent.getText().toString();
               if (Common.networkConnected(getActivity())){
                   JsonObject jsonObject = new JsonObject();
                   jsonObject.addProperty("action","update");
                   jsonObject.addProperty("ID",id);
                   jsonObject.addProperty("Title",title);
                   jsonObject.addProperty("Date",date);
                   jsonObject.addProperty("Content",content);
                   int count = 0;
                   myTask = new MyTask(Common.URLForHen+"/LoginHelp",jsonObject.toString());
                   try {
                       String result = myTask.execute().get();
                       count = Integer.valueOf(result);
                   }  catch (Exception e) {
                       Log.e(TAG,e.toString());
                   }if (count == 0 ){
                       Common.showToast(getActivity(),"update fail");
                   }else {
                       Common.showToast(getActivity(),"update success");

                   }
               }
           }
       });










        etDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    Calendar c = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        c = Calendar.getInstance();
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // TODO Auto-generated method stub
                                etDate.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
                    }

                }
            }
        });
        etDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar c = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    c = Calendar.getInstance();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            // TODO Auto-generated method stub
                            etDate.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
                }

            }
        });
        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = true;
                String Date = etDate.getText().toString();
                String Title = etTitle.getText().toString();
                String Context = etContent.getText().toString();


                if (Date.trim().isEmpty()) {
                    etDate.setError("IsValid Date");
                    isValid = false;
                }
                if (Title.trim().isEmpty()) {
                    etTitle.setError("IsValid Title");
                    isValid = false;
                }
                if (Context.trim().isEmpty()) {
                    etContent.setError("IsValid Context");
                    isValid = false;
                }
                if (isValid) {
                    Bundle b = new Bundle();
                    b.putString("ClassID",ClassID);
                    Fragment fragment = new ExamFragment();
                    fragment.setArguments(b);
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_content, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
//
            }

        });

        return view;
    }



}