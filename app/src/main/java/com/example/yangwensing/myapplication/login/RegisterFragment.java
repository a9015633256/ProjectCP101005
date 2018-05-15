package com.example.yangwensing.myapplication.login;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.yangwensing.myapplication.MainActivity;
import com.example.yangwensing.myapplication.R;
import com.example.yangwensing.myapplication.main.Account;
import com.example.yangwensing.myapplication.main.Common;
import com.example.yangwensing.myapplication.main.MyTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by nameless on 2018/4/16.
 */

public class RegisterFragment extends Fragment {
    private final static String TAG = "RegisterFragment";
    int ggender = 0;
    private MyTask registerTask,getaccountTask;
    List<Account> accounts ;
    int same = 0;
    //帳號比對格式
    String accoutwho ="\\w{1,}@{1,1}\\w{1,}\\.\\w{1,}\\.{0,}\\w{1,}";




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_register, container, false);

        getActivity().setTitle(R.string.textRegister);


        showallaccount();



        final EditText rgAccount = view.findViewById(R.id.rgAccount);
        final EditText rgPassword = view.findViewById(R.id.rgPassword);
        final EditText rgPasswordconfirm = view.findViewById(R.id.rgPasswordConfirm);
        Button btBack = view.findViewById(R.id.btBack);
        final TextView acstate = view.findViewById(R.id.state);
        Button btCreat = view.findViewById(R.id.btCreat);

        final EditText rgbirthday = view.findViewById(R.id.birthday);
        final EditText rgphone = view.findViewById(R.id.PhoneNumber);
        final EditText rgName = view.findViewById(R.id.rgname);
        Button male = view.findViewById(R.id.rbMale);
        Button female = view.findViewById(R.id.rbFemale);
        RadioGroup rggender = view.findViewById(R.id.rgGender);



        rggender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rbMale) {
                    ggender = 1;
                } else if (i == R.id.rbFemale) {
                    ggender = 2;
                }
            }
        });


        //點擊彈出選擇日期dialog，API24需要
        rgbirthday.setInputType(InputType.TYPE_NULL); //不跳出系统輸入鍵盤

        rgbirthday.setOnFocusChangeListener(new View.OnFocusChangeListener() {

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
                                rgbirthday.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
                    }

                }
            }
        });

        rgbirthday.setOnClickListener(new View.OnClickListener() {

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
                            rgbirthday.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
                }

            }
        });
        //點擊彈出選擇日期dialog


        //科目選單設定
        final Spinner spinner1 = view.findViewById(R.id.spSubject1);
        ArrayAdapter<CharSequence> subjectList = ArrayAdapter.createFromResource(getActivity(),
                R.array.subject,
                android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(subjectList);

        //科目選單設定



        // 每輸入一個字就觸發一次
        rgAccount.addTextChangedListener( new TextWatcher(){
            @SuppressLint("ResourceAsColor")
            @Override
            public void afterTextChanged(Editable arg0) {
                // 顯示文字內容
//                Log.i( "test", arg0.toString() );
                String name = rgAccount.getText().toString();

                same =0;

                if (name.matches(accoutwho)){
                    for (int c=0; c < accounts.size();c++){
                        if (name.equals(accounts.get(c).getAccount())){
                            same = 1;
                            break;
                        }
                    }

                }else{

                    same =1;
                }


                if (same ==1){
                    acstate.setText("帳號重複或格式錯誤");
                    acstate.setTextColor(getResources().getColor(R.color.stateWrong));
                }else {
                    acstate.setText("OK");
                    acstate.setTextColor(getResources().getColor(R.color.stateCorrect));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });
        // 每輸入一個字就觸發一次








        btCreat.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                String account = rgAccount.getText().toString();

                String password = rgPassword.getText().toString().trim();
                String passwordconfirm = rgPasswordconfirm.getText().toString().trim();
                String name = rgName.getText().toString();
                String birthday = rgbirthday.getText().toString();
                String phone = rgphone.getText().toString();
                String subject = spinner1.getSelectedItem().toString();
                int gender = ggender;


                for (int c=0; c < accounts.size();c++){
                    if (account.equals(accounts.get(c).getAccount())){
                        same = 1;
                        break;
                    }

                }




                if (password.equals(passwordconfirm) && same == 0) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "insert");
                    jsonObject.addProperty("account", account);
                    jsonObject.addProperty("password", password);

                    jsonObject.addProperty("name", name);
                    jsonObject.addProperty("phone", phone);
                    jsonObject.addProperty("gender", gender);
                    jsonObject.addProperty("birthday", birthday);
                    jsonObject.addProperty("subject", subject);


                    accounts.clear();


                    try {
                        registerTask = new MyTask(Common.URL + "/LoginHelp", jsonObject.toString());
                        int count = Integer.valueOf(registerTask.execute().get());
//                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();






                        if (count == 0 ) {
                            Toast.makeText(getActivity(), "Regitration failed ", Toast.LENGTH_SHORT).show();
                            return;
                        }else
                        {
                            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();

                            //回到登入頁面用開啟新activity的方式，就不需叫清理backStack了
                            Intent i = new Intent(getActivity(), MainActivity.class);
                            // set the new task and clear flags
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
//                            Fragment loginFragment = new LoginFragment();
//
//                            FragmentManager fragmentManager = getFragmentManager();
//                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////                            fragmentTransaction.addToBackStack(null);
//
//                            fragmentTransaction.replace(R.id.content, loginFragment);
//                            fragmentTransaction.commit();
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "error message" + e.toString());
                    }


                } else {
                    Toast.makeText(getActivity(), "帳號重複或是密碼不一", Toast.LENGTH_SHORT).show();
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

    private void showallaccount() {
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "/LoginHelp";

//            List<Account> accounts = null;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAllaccounts");
            String jsonOut = jsonObject.toString();
            getaccountTask = new MyTask(url, jsonOut);
            try {
                String jsonIn = getaccountTask.execute().get();
                Log.d(TAG, jsonIn);
                Type listType = new TypeToken<List<Account>>() {
                }.getType();
                accounts = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (accounts == null || accounts.isEmpty()) {
                Toast.makeText(getActivity(), "empty", Toast.LENGTH_SHORT).show();

            } else {
//                rvClass.setAdapter(new ClassManager.ClassesRecyclerViewAdapter(getActivity(), classes));
            }
        } else {
            Toast.makeText(getActivity(), "No Net", Toast.LENGTH_SHORT).show();
        }
    }
}
