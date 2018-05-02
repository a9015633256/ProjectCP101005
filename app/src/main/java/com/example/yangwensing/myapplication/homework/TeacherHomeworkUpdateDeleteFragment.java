package com.example.yangwensing.myapplication.homework;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class TeacherHomeworkUpdateDeleteFragment extends Fragment {
    //    private BottomNavigationView bottomNavigationView;
    private EditText etTitle, etContent;
    private Button btUpdate;

    //接上一頁資料用
    private static Homework homework;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_homework_update_delete, container, false); //回傳父元件(linearLayout) 最尾要記得加false否則預設為true
        getActivity().setTitle(R.string.title_homeworkUpdateDelete);

        findViews(view);

//        bottomNavigationView = getActivity().findViewById(R.id.navigation);
//        bottomNavigationView.setVisibility(View.GONE);

        setHasOptionsMenu(true);

        //取得上一頁資料
        Bundle bundle = getArguments();
        if (bundle != null) {
            homework = (Homework) bundle.getSerializable("homework");
        }

        if (homework != null) {
            etTitle.setText(homework.getTitle());
            etContent.setText(homework.getContent());
        } else {
            Common.showToast(getActivity(), R.string.text_data_error);
        }


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

                    int count = -1;

                    try {
                        String jsonIn = new MyTask(Common.URLForMingTa + "/HomeworkServlet", jsonObject.toString()).execute().get();
                        count = Integer.valueOf(jsonIn);

                        if (count == -1) {

                            Common.showToast(getActivity(), "Update homework failed!!");
                        } else {

                            Common.showToast(getActivity(), "Update succeeded!!");
                            getFragmentManager().beginTransaction().replace(R.id.content, new TeacherHomeworkFragment()).commit();


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

    private void findViews(View view) {
        etTitle = view.findViewById(R.id.etAddHomeworkTitle);
        etContent = view.findViewById(R.id.etAddHomeworkContent);
        btUpdate = view.findViewById(R.id.btUpdateHomework);


    }

    @Override
    public void onStop() {
//        bottomNavigationView.setVisibility(View.VISIBLE);

        super.onStop();
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
                new AlertDialogFragment().show(getFragmentManager(), "delete");


                break;
            default:
                break;


        }


        return super.onOptionsItemSelected(item);
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

                        int count = -1;
                        try {
                            String jsonIn = new MyTask(Common.URLForMingTa + "/HomeworkServlet", jsonObject.toString()).execute().get();
                            count = Integer.valueOf(jsonIn);

                            if (count == -1) {
                                Common.showToast(getActivity(), "Delete homework failed!!");
                            } else {

                                Common.showToast(getActivity(), "Delete succeeded!!");
                                getFragmentManager().beginTransaction().replace(R.id.content, new TeacherHomeworkFragment()).commit();


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

