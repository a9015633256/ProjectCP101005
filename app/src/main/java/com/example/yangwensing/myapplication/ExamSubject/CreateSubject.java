package com.example.yangwensing.myapplication.ExamSubject;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.example.yangwensing.myapplication.R;
import com.example.yangwensing.myapplication.main.Common;
import com.example.yangwensing.myapplication.main.MyTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class CreateSubject extends Fragment {
    private final static String TAG = "MainFragment";
    private MyTask myTask;
    private EditText etTeacher, etSubject, etDate, etTitle, etContent;
    private Button btSure,btUpdate;
    private String Classid = "";
    private String Subjectid = "";
    private String Teacherid = "";



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.subject_create, container, false);
        getActivity().setTitle(R.string.createsubject);
        etDate = view.findViewById(R.id.etDate);
        etTitle = view.findViewById(R.id.etTitle);
        etContent = view.findViewById(R.id.etContent);
        btSure = view.findViewById(R.id.btSure);
        etDate.setInputType(InputType.TYPE_NULL);
        btUpdate = view.findViewById(R.id.btUpdate);
        btUpdate.setVisibility(View.GONE);
        Bundle b = getArguments();
        Classid = b.getString("ClassID");

        SharedPreferences preferences = getActivity().getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
        Subjectid = preferences.getString("Subject","0");
        Teacherid = String.valueOf(preferences.getInt("teacherid",0));

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
                String Content = etContent.getText().toString();


                if (Date.trim().isEmpty()) {
                    etDate.setError("IsValid Date");
                    isValid = false;
                }
                if (Title.trim().isEmpty()) {
                    etTitle.setError("IsValid Title");
                    isValid = false;
                }
                if (Content.trim().isEmpty()) {
                    etContent.setError("IsValid Content");
                    isValid = false;
                }
                if (isValid) {

                    AddSubject exam = new AddSubject(Subjectid,Teacherid,Classid,Title,Content,Date);
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "Add");
                    jsonObject.addProperty("Subject",new Gson().toJson(exam));


                    myTask = new MyTask(Common.URLForHen + "/LoginHelp", jsonObject.toString());
                    try {
                        int count = Integer.valueOf(myTask.execute().get());
                        if (count == 0) {
                            Toast.makeText(getActivity(), "Add failed!",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Bundle bb = new Bundle();
                            bb.putString("ClassID",Classid);
                            Fragment fragment = new ExamFragment();
                            fragment.setArguments(bb);
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.main_content, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }

                    } catch (Exception e) {
                        Log.e(TAG, "error massage" + e.toString());
                    }

                } else {
                    Toast.makeText(getActivity(), "connection to network failed",
                            Toast.LENGTH_SHORT).show();
                }
            }

        });

        return view;
    }
}
