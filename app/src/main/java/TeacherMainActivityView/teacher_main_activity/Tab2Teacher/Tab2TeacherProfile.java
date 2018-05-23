package TeacherMainActivityView.teacher_main_activity.Tab2Teacher;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.yangwensing.myapplication.R;

import java.security.PrivateKey;
import java.util.concurrent.ExecutionException;

import TeacherMainActivityView.CommonPart.Common;

public class Tab2TeacherProfile extends Fragment {
    private EditText tvTeahcerProfileId, tvTeahcerProfileName, tvTeahcerProfileGender, tvTeahcerProfileDayOfBirth, tvTeahcerProfilePhoneNumber;
    private ImageView ivTeahcerPicProfile;
    private TeacherGetImageTask teacherGetImageTask;
    private BottomNavigationView bottomNavigationView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teacher_tab2_profile, container, false);

        findViews(view);
        getTeacherProfile();
        setHasOptionsMenu(true); //這樣onCreateOptionsMenu()才有效、才能加optionsMenu進activity的options


        return view;
    }

    private void findViews(View view) {
        tvTeahcerProfileId = view.findViewById(R.id.tvTeahcerProfileId);
        tvTeahcerProfileName = view.findViewById(R.id.tvTeahcerProfileName);
        tvTeahcerProfileGender = view.findViewById(R.id.tvTeahcerProfileGender);
        tvTeahcerProfileDayOfBirth = view.findViewById(R.id.tvTeahcerProfileDayOfBirth);
        tvTeahcerProfilePhoneNumber = view.findViewById(R.id.tvTeahcerProfilePhoneNumber);
        ivTeahcerPicProfile = view.findViewById(R.id.ivTeahcerPicProfile);
        bottomNavigationView = getActivity().findViewById(R.id.btNavigation_Bar);

    }

    public void getTeacherProfile() {
        SharedPreferences preferences = getActivity().getSharedPreferences(com.example.yangwensing.myapplication.main.Common.PREF_FILE, Context.MODE_PRIVATE);
        int id = preferences.getInt("id", 0);
        String ta = preferences.getString("teacher_Account", "");
        String te = preferences.getString("teacher_Email", "");
        String tp = preferences.getString("teacher_Phone", "");
        String tt = preferences.getString("teacher_TakeOfficeDate", "");
        int tg = preferences.getInt("teacher_Gender", 0);

        tvTeahcerProfileId.setText(ta);
        tvTeahcerProfilePhoneNumber.setText(tp);
        tvTeahcerProfileDayOfBirth.setText(tt);
        tvTeahcerProfileName.setText(te);
        if (tg == 1) {
            tvTeahcerProfileGender.setText("男");
        } else {
            tvTeahcerProfileGender.setText("女");
        }

        int imageSize = getResources().getDisplayMetrics().widthPixels /3;
        teacherGetImageTask = new TeacherGetImageTask(Common.URL + "/TeachersListServerlet", id, imageSize);
        String url = Common.URL+"/TeachersListServerlet";
        teacherGetImageTask = new TeacherGetImageTask(url, id, imageSize, ivTeahcerPicProfile);
        teacherGetImageTask.execute();

    }

    @Override
    public void onStart() {
        super.onStart();
        bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        super.onStop();
        bottomNavigationView.setVisibility(View.VISIBLE);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.removeGroup(0);
    }
}
