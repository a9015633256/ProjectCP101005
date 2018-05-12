package TeacherMainActivityView.teacher_main_activity.Tab2Teacher;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.yangwensing.myapplication.R;

import java.security.PrivateKey;

public class Tab2TeacherProfile extends Fragment {
    private EditText tvTeahcerProfileId, tvTeahcerProfileName, tvTeahcerProfileGender, tvTeahcerProfileDayOfBirth, tvTeahcerProfilePhoneNumber;
    private ImageView ivTeahcerPicProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teacher_tab2_profile, container, false);

        findViews(view);
        getTeacherProfile();

        return view;
    }

    private void findViews(View view) {
        tvTeahcerProfileId = view.findViewById(R.id.tvTeahcerProfileId);
        tvTeahcerProfileName = view.findViewById(R.id.tvTeahcerProfileName);
        tvTeahcerProfileGender = view.findViewById(R.id.tvTeahcerProfileGender);
        tvTeahcerProfileDayOfBirth = view.findViewById(R.id.tvTeahcerProfileDayOfBirth);
        tvTeahcerProfilePhoneNumber = view.findViewById(R.id.tvTeahcerProfilePhoneNumber);

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



    }


}
