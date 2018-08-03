package com.example.yangwensing.myapplication.classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yangwensing.myapplication.R;
import com.example.yangwensing.myapplication.login.LoginFragment;
import com.example.yangwensing.myapplication.main.Common;
import com.example.yangwensing.myapplication.main.MyTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by nameless on 2018/4/26.
 */

public class ClassJoin extends Fragment {

    private String user = "";
    private final static String TAG = "ResultFragment";
    private MyTask myTask;
    private RecyclerView recycler;
    private EditText etSearch;
    private String ClassID = "";
    private int Teacherid = 0;
    private Button btSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.classjoin, container, false);
        getActivity().setTitle("加入班級");
        recycler = view.findViewById(R.id.recyclerr);
        recycler.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        setHasOptionsMenu(true); //這樣onCreateOptionsMenu()才有效、才能加optionsMenu進activity的options
        etSearch = view.findViewById(R.id.etSearch);
        btSearch = view.findViewById(R.id.btSearch);
        Bundle bundle = getArguments();

        user = bundle.getString("name");
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find();

            }
        });

        return view;
    }

    private boolean find() {

        boolean isUserValid = false;
        if (Common.networkConnected(getActivity())) {
            List<Classa> users = new ArrayList<>();
            String id = etSearch.getText().toString();
            String url = Common.URLForHen + "/LoginHelp";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findByClass");
            jsonObject.addProperty("id", id);
            String jsonOut = jsonObject.toString();
            myTask = new MyTask(url, jsonOut);
            try {
                String jsonIN = myTask.execute().get();
                Classa user = new Gson().fromJson(jsonIN, Classa.class);
                users.add(user);
                if (user == null) {
                    Common.showToast(getActivity(), "Not Found ClassCode");

                } else {
                    recycler.setAdapter(new ClassaAdapter(getActivity(), users));
                    Common.showToast(getActivity(), "success");
                }


            } catch (Exception e) {
                Log.e(TAG, "error message" + toString());
            }
        }
        return isUserValid;


//        getFragmentManager().popBackStack("cm",0);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.options_menu_classmanager, menu);
        menu.getItem(0).setVisible(false);
        menu.getItem(1).setVisible(false);
        menu.getItem(2).setVisible(false);
        menu.getItem(3).setVisible(false);
        menu.getItem(4).setVisible(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.cmcreate:
                Fragment classCreate = new ClassCreate();

                Bundle bundle = new Bundle();
                bundle.putString("name", user);
                classCreate.setArguments(bundle);

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

//                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.replace(R.id.content, classCreate);
                fragmentTransaction.commit();

                break;
            case R.id.cmjoin:
                Fragment classJoin = new ClassJoin();

                Bundle bundlejo = new Bundle();
                bundlejo.putString("name", user);
                classJoin.setArguments(bundlejo);

                FragmentManager fragmentManagerjo = getFragmentManager();
                FragmentTransaction fragmentTransactionjo = fragmentManagerjo.beginTransaction();

//                fragmentTransactionjo.addToBackStack(null);

                fragmentTransactionjo.replace(R.id.content, classJoin);
                fragmentTransactionjo.commit();

                break;
            case R.id.cmdelete:
                Fragment classdelete = new ClassDelete();

                Bundle bundlede = new Bundle();
                bundlede.putString("name", user);
                classdelete.setArguments(bundlede);

                FragmentManager fragmentManagerde = getFragmentManager();
                FragmentTransaction fragmentTransactionde = fragmentManagerde.beginTransaction();

//                fragmentTransactionde.addToBackStack(null);

                fragmentTransactionde.replace(R.id.content, classdelete);
                fragmentTransactionde.commit();

                break;
//            case R.id.cmlogout:
//                //重置偏好設定檔儲存的登入設定
//                SharedPreferences preferences = getActivity().getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
//                preferences.edit()
//                        .putInt("studentId", 0)
//                        .putInt("teacherId", 0)
//                        .putInt("subjectId", 0)
//                        .putInt("classId", 0)
//                        .putString("className", "")
//                        .apply();
//
//                //清除所有backStack
//                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//
//                //回到登入頁面
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, new LoginFragment()).commit();
//
//                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    class ClassaAdapter extends RecyclerView.Adapter<ClassaAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<Classa> aclass;


        public ClassaAdapter(FragmentActivity activity, List<Classa> aclass) {
            layoutInflater = LayoutInflater.from(activity);
            this.aclass = aclass;
        }


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.joinclass_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            final Classa classs = aclass.get(position);
            String text = getString(R.string.ClassNam) + classs.getName();


            holder.tvAll.setText(text);
            holder.ivAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ClassID = String.valueOf(classs.getId());
                    SharedPreferences preferences = getActivity().getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
                    Teacherid = preferences.getInt("teacherid", 0);

                    if (Common.networkConnected(getActivity())) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("action", "Join");
                        jsonObject.addProperty("ClassId", ClassID);
                        jsonObject.addProperty("TeacherId", Teacherid);
                        int count = 0;
                        myTask = new MyTask(Common.URLForHen + "/LoginHelp", jsonObject.toString());
                        try {
                            String result = myTask.execute().get();
                            count = Integer.valueOf(result);
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                        if (count == 0 ) {
                            Common.showToast(getActivity(), "Join fail");
                        } else {
                            Common.showToast(getActivity(), "Join success");
                        }
                    }
                }
            });
        }


        @Override
        public int getItemCount() {
            return aclass.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivAdd;
            TextView tvAll;

            public MyViewHolder(View itemView) {
                super(itemView);
                ivAdd = itemView.findViewById(R.id.ivAdd);
                tvAll = itemView.findViewById(R.id.ClassAll);


            }
        }


    }
}