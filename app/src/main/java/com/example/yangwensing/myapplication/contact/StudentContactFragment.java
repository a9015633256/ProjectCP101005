package com.example.yangwensing.myapplication.contact;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yangwensing.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class StudentContactFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<Teacher> teachers;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_contact, container, false); //回傳父元件(linearLayout) 最尾要記得加false否則預設為true

        getActivity().setTitle(R.string.title_contact);

        findViews(view);

        //rv
        teachers = getTheTeachers();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new ContactAdapter(teachers, getActivity()));

        return view; //要改成回傳view
    }



    //rv假資料
    private List<Teacher> getTheTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(new Teacher("黃老大"));
        teachers.add(new Teacher("周二哥"));
        teachers.add(new Teacher("陳三哥"));
        teachers.add(new Teacher("紀老四"));


        return teachers;
    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.rvContact);

    }


    //rv
    private class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
        private List<Teacher> teachers;
        private Context context;

        public ContactAdapter(List<Teacher> teachers, Context context) {
            this.teachers = teachers;
            this.context = context;
        }

        @NonNull
        @Override
        public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.cardview_contact, parent, false);


            return new ContactViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ContactViewHolder contactViewHolder, int position) {
            Teacher teacher = teachers.get(position);
            String text = "老師："+teacher.getName();
            contactViewHolder.textView.setText(text);
            contactViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    StudentContactDetailFragment studentContactDetailFragment = new StudentContactDetailFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content, studentContactDetailFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
        }

        @Override
        public int getItemCount() {
            return teachers.size();
        }

        class ContactViewHolder extends RecyclerView.ViewHolder {
            private TextView textView;


            ContactViewHolder(View itemView) {
                super(itemView);
                this.textView = itemView.findViewById(R.id.tvTeacherName);

            }
        }
    }
}
