package com.example.yangwensing.myapplication.info;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.yangwensing.myapplication.R;
import com.example.yangwensing.myapplication.main.Common;
import com.example.yangwensing.myapplication.main.GetImageTask;
import com.example.yangwensing.myapplication.main.MyTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class StudentInfoFragment extends Fragment {
    private int studentId;
    private Student student;
    //EditText把輸入功能關閉、充當textView用
    private EditText tvId, tvName, tvDayOfBirth, tvPhoneNumber, tvGender, tvClassName, tvAddress;
    private ImageView ivStudentPic;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_info, container, false); //回傳父元件(linearLayout) 最尾要記得加false否則預設為true
        getActivity().setTitle(R.string.title_info);

        findViews(view);

        Bundle bundle = getArguments();
        if (bundle != null && bundle.getInt("studentIdForTeacher") != 0) {
            studentId = bundle.getInt("studentIdForTeacher");


        } else {
            setHasOptionsMenu(true); //這樣onCreateOptionsMenu()才有效、才能加optionsMenu進activity的options


        }


        studentId = Common.getDataFromPref(
                getActivity(), "studentId", 0);

        //取得db資訊並顯示在畫面上
        getStudentInfo();

//
//
//        GetImageTask getStudentPicTask = new GetImageTask(Common.URLForMingTa + "/StudentInfoServlet", studentId, imageSize, ivStudentPic);
//        getStudentPicTask.execute();
//


        return view; //要改成回傳view
    }

    private void findViews(View view) {
        tvId = view.findViewById(R.id.tvStudentInfoId);
        tvName = view.findViewById(R.id.tvStudentInfoName);
        tvClassName = view.findViewById(R.id.tvStudentInfoClassName);
        tvGender = view.findViewById(R.id.tvStudentInfoGender);
        tvDayOfBirth = view.findViewById(R.id.tvStudentInfoDayOfBirth);
        tvPhoneNumber = view.findViewById(R.id.tvStudentInfoPhoneNumber);
        tvAddress = view.findViewById(R.id.tvStudentInfoAddress);
        ivStudentPic = view.findViewById(R.id.ivUserPic);


    }


    private void getStudentInfo() {


        if (Common.networkConnected(getActivity())) {

            //取得文字部分
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findStudentById");
            jsonObject.addProperty("studentId", studentId);


            try {
                String jsonIn = new MyTask(Common.URLForMingTa + "/StudentInfoServlet", jsonObject.toString()).execute().get();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                student = gson.fromJson(jsonIn, Student.class);

                tvId.setText(student.getStudentNumber());
                tvName.setText(student.getName());
                tvClassName.setText(student.getClassName());

                if (student.getGender() == 1) {
                    tvGender.setText("男");

                } else {
                    tvGender.setText("女");

                }


                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                tvDayOfBirth.setText(simpleDateFormat.format(student.getDayOfBirth()));

                tvPhoneNumber.setText(String.valueOf(student.getPhoneNumber()));
                tvAddress.setText((student.getAddress()));


            } catch (Exception e) {
                e.printStackTrace();
                Common.showToast(getActivity(), R.string.text_no_server);

            }

            //取得照片部分
            int imageSize = getResources().getDisplayMetrics().widthPixels / 3;
            GetImageTask getStudentPicTask = new GetImageTask(Common.URLForMingTa + "/StudentInfoServlet", studentId, imageSize);
            try {
                Bitmap bitmap = getStudentPicTask.execute().get();
                ivStudentPic.setImageBitmap(bitmap);


            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


        } else {
            Common.showToast(getActivity(), R.string.text_no_network);

        }
    }

    //製造右上角編輯圖示(鉛筆)
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_options_edit_info, menu);
        menu.getItem(0).setVisible(false);


    }

    //製造右上角編輯圖示(鉛筆)（功能)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_editInfo:

                if (student == null) {
                    return super.onOptionsItemSelected(item);
                }


                Bundle bundle = new Bundle();
                bundle.putSerializable("student", student);

                StudentInfoEditFragment studentInfoEditFragment = new StudentInfoEditFragment();
                studentInfoEditFragment.setArguments(bundle);

                getFragmentManager().beginTransaction().replace(R.id.content, studentInfoEditFragment, "StudentInfoEditFragment").addToBackStack("123").commit();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        getStudentInfo();

        super.onResume();

    }
}

