package com.example.yangwensing.myapplication.homework;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.yangwensing.myapplication.R;
import com.example.yangwensing.myapplication.main.Common;
import com.example.yangwensing.myapplication.main.MyTask;
import com.google.gson.JsonObject;

public class TeacherHomeworkUpdateDeleteFragment extends Fragment {
    private static final String TAG ="TeacherHomeUFragment";

    //    private BottomNavigationView bottomNavigationView;
    private EditText etTitle, etContent;
    private Button btUpdate;
    private TabLayout tabLayout;
    private TabLayout.OnTabSelectedListener onTabSelectedListener;
    private MyTask updateHomeworkTask;
    private static MyTask deleteHomeworkTask;


    //接上一頁資料用
    private static Homework homework;
    private BottomNavigationView bottomNavigationView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_homework_update_delete, container, false); //回傳父元件(linearLayout) 最尾要記得加false否則預設為true
        getActivity().setTitle(R.string.title_homeworkUpdateDelete);

        findViews(view);

        setHasOptionsMenu(true);


        //取得上一頁資料
        final Bundle bundle = getArguments();
        if (bundle != null) {
            homework = (Homework) bundle.getSerializable("homework");
        }

        if (homework != null) {
            etTitle.setText(homework.getTitle());
            etContent.setText(homework.getContent());
        } else {
            Common.showToast(getActivity(), R.string.msg_data_error);
        }



        //tabLayout給老師作業勾選
        tabLayout = getActivity().findViewById(R.id.tlForTeacherHomework);
        tabLayout.setVisibility(View.VISIBLE);
        onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        break;
                    case 1:
                        TeacherHomeworkCheckFragment teacherHomeworkCheckFragment = new TeacherHomeworkCheckFragment();
                        teacherHomeworkCheckFragment.setArguments(bundle);

                        if (getFragmentManager() != null) {
                            getFragmentManager().beginTransaction().replace(R.id.main_content, teacherHomeworkCheckFragment, "TeacherHomeworkCheckFragment").commit();
                        }
                        break;
                    default:
                        break;


                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
        tabLayout.addOnTabSelectedListener(onTabSelectedListener);
        tabLayout.getTabAt(0).select();


        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.networkConnected(getActivity())) {


                    String title = etTitle.getText().toString().trim();
                    String content = etContent.getText().toString().trim();

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "updateHomework");
                    jsonObject.addProperty("homeworkId", homework.getId());
                    jsonObject.addProperty("homeworkTitle", title);
                    jsonObject.addProperty("homeworkContent", content);


                    try {
                        updateHomeworkTask = new MyTask(Common.URLForMingTa + "/HomeworkServlet", jsonObject.toString());
                        String jsonIn = updateHomeworkTask.execute().get();
                        int count = Integer.valueOf(jsonIn);

                        if (count == 0) {

                            Common.showToast(getActivity(), "Update homework failed!!");
                        } else {

                            Common.showToast(getActivity(), "Update succeeded!!");
                            getFragmentManager().popBackStack();


                        }


                    } catch (Exception e) {
                        Common.showToast(getActivity(), R.string.text_no_server);

                        e.printStackTrace();
                    }


                } else {
                    Common.showToast(getActivity(), R.string.text_no_network);


                }
            }

        });


        return view; //要改成回傳view
    }

    @Override
    public void onStart() {
        super.onStart();

        //隱藏底部導覽列
        bottomNavigationView.setVisibility(View.GONE);

    }

    @Override
    public void onStop() {

        Log.d(TAG, "onStop: 1234");
        //重新顯示底部導覽列
        bottomNavigationView.setVisibility(View.VISIBLE);
        if (updateHomeworkTask != null) {
            updateHomeworkTask.cancel(true);

        }
        if (deleteHomeworkTask != null) {
            deleteHomeworkTask.cancel(true);

        }

        super.onStop();
    }


    @Override
    public void onDestroyView() {
        tabLayout.setVisibility(View.GONE);
        super.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_options_delete_homework, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_deleteHomework:
                if (getFragmentManager() != null) {
                    new AlertDialogFragment().show(getFragmentManager(), "delete");
                }


                break;
            default:
                break;


        }


        return super.onOptionsItemSelected(item);
    }

    private void findViews(View view) {
        etTitle = view.findViewById(R.id.etAddHomeworkTitle);
        etContent = view.findViewById(R.id.etAddHomeworkContent);
        btUpdate = view.findViewById(R.id.btUpdateHomework);
        bottomNavigationView = getActivity().findViewById(R.id.btNavigation_Bar);


    }


    public static class AlertDialogFragment
            extends DialogFragment implements DialogInterface.OnClickListener {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.text_deleteHomework)
                    .setIcon(R.drawable.ic_alert)
                    .setMessage(R.string.msg_WantToDelete)
                    .setPositiveButton(R.string.text_delete, this)
                    .setNegativeButton(R.string.text_No, this)
                    .create();
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:

                    if (Common.networkConnected(getActivity())) {

                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("action", "deleteHomework");
                        jsonObject.addProperty("homeworkId", homework.getId());

                        int count;
                        try {
                            deleteHomeworkTask = new MyTask(Common.URLForMingTa + "/HomeworkServlet", jsonObject.toString());
                            String jsonIn = deleteHomeworkTask.execute().get();
                            count = Integer.valueOf(jsonIn);

                            if (count == 0) {
                                Common.showToast(getActivity(), "Delete homework failed!!");
                            } else {

                                Common.showToast(getActivity(), "Delete succeeded!!");
                                getFragmentManager().popBackStack();


                            }


                        } catch (Exception e) {
                            Common.showToast(getActivity(), R.string.text_no_server);

                            e.printStackTrace();
                        }


                    } else {
                        Common.showToast(getActivity(), R.string.text_no_network);


                    }
                    break;
                default:
                    dialog.cancel();
                    break;
            }
        }
    }

}

