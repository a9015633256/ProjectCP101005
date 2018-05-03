package com.example.yangwensing.myapplication.login;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.yangwensing.myapplication.R;
import com.example.yangwensing.myapplication.main.Common;
import com.example.yangwensing.myapplication.main.MyTask;
import com.google.gson.JsonObject;

import static com.example.yangwensing.myapplication.Common.networkConnected;
import static com.example.yangwensing.myapplication.main.Common.networkConnected;

/**
 * Created by nameless on 2018/4/16.
 */

public class RegisterFragment extends Fragment {
    private final static String TAG = "RegisterFragment";
    int ggender = 0;
    private MyTask registerTask;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_register,container,false);

        getActivity().setTitle(R.string.textRegister);

        final EditText rgName = view.findViewById(R.id.rgName);
        final EditText rgPassword = view.findViewById(R.id.rgPassword);
        final EditText rgPasswordconfirm = view.findViewById(R.id.rgPasswordConfirm);
        Button btBack = view.findViewById(R.id.btBack);
        Button btCreat = view.findViewById(R.id.btCreat);
        final EditText birthday = view.findViewById(R.id.birthday);
        final EditText rgphone = view.findViewById(R.id.PhoneNumber);
        final EditText rgemail = view.findViewById(R.id.email);
        Button male = view.findViewById(R.id.rbMale);
        Button female = view.findViewById(R.id.rbFemale);
        RadioGroup rggender = view.findViewById(R.id.rgGender);


        rggender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rbMale){
                    ggender = 1;
                }else if (i == R.id.rbFemale){
                    ggender = 2;
                }
            }
        });


        //點擊彈出選擇日期dialog，API24需要
        birthday.setInputType(InputType.TYPE_NULL); //不跳出系统輸入鍵盤

        birthday.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if(hasFocus){
                    Calendar c = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        c = Calendar.getInstance();
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // TODO Auto-generated method stub
                                birthday.setText(year+"/"+(monthOfYear+1)+"/"+dayOfMonth);
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
                    }

                }
            }
        });

        birthday.setOnClickListener(new View.OnClickListener() {

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
                            birthday.setText(year+"/"+(monthOfYear+1)+"/"+dayOfMonth);
                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
                }

            }
        });
        //點擊彈出選擇日期dialog



        //科目選單設定
        Spinner spinner1 = view.findViewById(R.id.spSubject1);
        ArrayAdapter<CharSequence> subjectList = ArrayAdapter.createFromResource(getActivity(),
                R.array.subject,
                android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(subjectList);
        Spinner spinner2 = view.findViewById(R.id.spSubject2);
        ArrayAdapter<CharSequence> anothersubjectList = ArrayAdapter.createFromResource(getActivity(),
                R.array.anothersubject,
                android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(anothersubjectList);
        Spinner spinner3 = view.findViewById(R.id.spSubject3);
        spinner3.setAdapter(anothersubjectList);
        //科目選單設定




        btCreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = rgName.getText().toString();
                String password = rgPassword.getText().toString().trim();
                String passwordconfirm = rgPasswordconfirm.getText().toString().trim();
                String email = rgemail.getText().toString();
                int phone = Integer.valueOf(rgphone.getText().toString());
                int gender = ggender;

                if (networkConnected(getActivity())) {

                    if (password.equals(passwordconfirm)) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("action", "insert");
                        jsonObject.addProperty("password", password);
                        jsonObject.addProperty("name", name);
                        jsonObject.addProperty("email", email);
                        jsonObject.addProperty("phone", phone);
                        jsonObject.addProperty("gender", gender);


                        try {
                            registerTask = new MyTask(Common.URL + "/LoginHelp", jsonObject.toString());
                            int count = Integer.valueOf(registerTask.execute().get());
                            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                            if (count == 0) {
                                Toast.makeText(getActivity(), "Regitration failed ", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.d(TAG, "error message" + e.toString());
                        }


                    }
                }
                else{
                    Toast.makeText(getActivity(),"connection to network failed", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });


        return view;
    }
}
