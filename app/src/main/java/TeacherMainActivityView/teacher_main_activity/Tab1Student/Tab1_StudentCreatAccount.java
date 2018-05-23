package TeacherMainActivityView.teacher_main_activity.Tab1Student;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yangwensing.myapplication.R;
import com.google.gson.JsonObject;

import TeacherMainActivityView.CommonPart.Common;
import TeacherMainActivityView.CommonPart.MyTask;
import TeacherMainActivityView.teacher_main_activity.MainActivity;



public class Tab1_StudentCreatAccount extends Fragment {
    private final static String TAG = "Tab1StudentCreatAccount";
    private MyTask myTask;
    private int classId;
    private FloatingActionButton btAdd;

    private BottomNavigationView bottomNavigationView;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teacher_tab1_student_creat_account, container, false);
        final EditText etStudentAccountID = view.findViewById(R.id.etStudentAccountID);
        final EditText etStudentAP = view.findViewById(R.id.etStudentAP);
        final EditText etStudentName = view.findViewById(R.id.etStudentName);
        bottomNavigationView = getActivity().findViewById(R.id.btNavigation_Bar);

        getActivity().setTitle(R.string.title_addStudentToClass);

        classId = getClassId();
//        btAdd = view
//        ((MainActivity) getActivity()).hideFloatingActionButton();
        Button btAddStudentAccount = view.findViewById(R.id.btAddStudentAccount);
        btAddStudentAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = true;
                String StudentAccountID = etStudentAccountID.getText().toString();
                if (StudentAccountID.trim().isEmpty()) {
                    etStudentAccountID.setError("Invalid User ID");
                    isValid = false;
                }
                String StudentAP = etStudentAP.getText().toString();
                if (StudentAP.trim().isEmpty()) {
                    etStudentAP.setError("Invalid User Password");
                    isValid = false;
                }
                String StudentName = etStudentName.getText().toString();
                if (isValid) {
                    if (Common.networkConnected(getActivity())) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("action", "insert");
                        jsonObject.addProperty("Student_ID", StudentAccountID);
                        jsonObject.addProperty("Student_Password", StudentAP);
                        jsonObject.addProperty("Student_Name",StudentName);
                        jsonObject.addProperty("Class_Name",classId);

                        try {
                            myTask = new MyTask(Common.URL + "/StudentAccountServlet", jsonObject.toString());
                            int count = Integer.valueOf(myTask.execute().get());
                            count = 1;
                            if (count == 1) {
                                Toast.makeText(getActivity(), "SignUp Success", Toast.LENGTH_LONG).show();
                                if (getFragmentManager() != null) {
                                    getFragmentManager().popBackStack();
                                }
                            }else {
                                Toast.makeText(getActivity(), "SignUp Fail", Toast.LENGTH_LONG).show();

                            }
                        } catch (Exception e) {
                            Log.e(TAG,"error message"+toString());
                        }
                    }else {
                        Toast.makeText(getActivity(),"NetWork connection fail",Toast.LENGTH_SHORT).show();
                    }
                }



            }
        });

        setHasOptionsMenu(true);


        return view;
    }

//    @Override
//    public void onStop() {
//        super.onStop();
//        ((MainActivity) getActivity()).showFloatingActionButton();
//    }


    private int getClassId() {
        SharedPreferences preferences = getActivity().getSharedPreferences(com.example.yangwensing.myapplication.main.Common.PREF_FILE, Context.MODE_PRIVATE);
        int id = preferences.getInt("classId", 0);
        return id;
    }

    public void onStart() {
        super.onStart();
        //隱藏底部導覽列
        bottomNavigationView.setVisibility(View.GONE);
    }
    public void onStop() {
        super.onStop();
        //重新顯示底部導覽列
        bottomNavigationView.setVisibility(View.VISIBLE);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.removeGroup(0);
    }
}
