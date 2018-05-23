package TeacherMainActivityView.teacher_main_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yangwensing.myapplication.R;
import com.example.yangwensing.myapplication.homework.TeacherHomeworkFragment;

import java.util.List;

import TeacherMainActivityView.CommonPart.BottomNavigationBarHelper;
import TeacherMainActivityView.CommonPart.SectionsPageAdapter;
import TeacherMainActivityView.teacher_main_activity.Tab1Student.tap1fragment;
import TeacherMainActivityView.teacher_main_activity.Tab2Teacher.tap2fragment;

//班級管理頁面
public class Navigation1_ClassManagement extends Fragment {
    private static final String TAG = "ClassManagementFragment";


//    private SectionsPageAdapter msectionsPageAdapter;
//    private AppBarLayout appBarLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teacher_navigation1_class_management, container, false); //回傳父元件(linearLayout) 最尾要記得加false否則預設為true
        getActivity().setTitle(R.string.ClassManagement);

//        msectionsPageAdapter = new SectionsPageAdapter(getFragmentManager());
//        appBarLayout = view.findViewById(R.id.appbar);

        ViewPager mviewPager = view.findViewById(R.id.container);
        SetupViewPager(mviewPager);

        TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mviewPager,true);


        return view;
    }

    private void SetupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getChildFragmentManager());

        adapter.addFragment(new tap1fragment(), "StudentList");
        adapter.addFragment(new tap2fragment(), "TeacherList");

        viewPager.setAdapter(adapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    //    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.teacher_options_manu, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//
////            case R.id.Teacher_Setting:
////                Intent intent2 = new Intent(MainActivity.this, TeacherSettingPage.class);
////                startActivity(intent2);
////                break;
//            case R.id.Teacher_Loggin_out:
//                Toast.makeText(getBaseContext(), "Log Out!", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.Teacher_Exit:
//                finish();//關閉list
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//        return true;
//    }

//    @Override
//    public void onStop() {
//        super.onStop();
//        hideFloatingActionButton();
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        showFloatingActionButton();
//    }
//
//    public void showFloatingActionButton() {
//        btAdd.show();
//    }
//
//    public void hideFloatingActionButton() {
//        btAdd.hide();
//    }


}
