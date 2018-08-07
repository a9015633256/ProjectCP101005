package TeacherMainActivityView.teacher_main_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.yangwensing.myapplication.ExamSubject.ExamFragment;
import com.example.yangwensing.myapplication.R;
import com.example.yangwensing.myapplication.chat.ChatList;
import com.example.yangwensing.myapplication.homework.TeacherHomeworkFragment;
import com.example.yangwensing.myapplication.main.BottomNavigationViewHelper;

import TeacherMainActivityView.CommonPart.BottomNavigationBarHelper;
import TeacherMainActivityView.CommonPart.SectionsPageAdapter;
import TeacherMainActivityView.teacher_main_activity.Tab1Student.tap1fragment;
import TeacherMainActivityView.teacher_main_activity.Tab2Teacher.tap2fragment;

public class MainActivity extends AppCompatActivity {
    //主要管理頁面

    private static final String TAG = "MainActivity";
    private TabLayout tlForTeacherHomeworkCheck;

//    private SectionsPageAdapter msectionsPageAdapter;
//
//    private ViewPager mviewPager;
//    public static ViewPager viewPager;
//    private  FloatingActionButton btAdd;
//    private TabLayout tabLayout;
//    private AppBarLayout appBarLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_activity_main);
//        setTitle(R.string.app_name);

//        msectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
//
//        mviewPager = findViewById(R.id.container);
//        SetupViewPager(mviewPager);
//
//        tabLayout = findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(mviewPager);
//
//        //
//        flForFragment = findViewById(R.id.flForFragment);
//        appBarLayout = findViewById(R.id.appbar);


        BottomNavigationView bottomNavigationView = findViewById(R.id.btNavigation_Bar);
        BottomNavigationBarHelper.disableShiftMode(bottomNavigationView);


        //切換NavigationBar的原始碼
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_Home:

                        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new Navigation1_ClassManagement()).commit();


                        break;


                    case R.id.ic_Homework:
//                        Intent intent2 = new Intent(MainActivity.this, Navigation3_Homework.class);
//                        startActivity(intent2);

                        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new TeacherHomeworkFragment()).commit();

//                        appBarLayout.setVisibility(View.GONE);
//                        tabLayout.setVisibility(View.GONE);
//                        mviewPager.setVisibility(View.GONE);
                        break;

                    case R.id.ic_ExamSubject:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new ExamFragment()).commit();

                        break;

                    case R.id.ic_Chat:
                        Fragment chatList = new ChatList();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.replace(R.id.main_content, chatList);
                        fragmentTransaction.commit();
                        break;
                }

                return true;
            }
        });

        //一開activity就選第一個選項
        bottomNavigationView.setSelectedItemId(R.id.ic_Home);

        //(作業頁面用到)tabLayout給老師作業勾選
        tlForTeacherHomeworkCheck = findViewById(R.id.tlForTeacherHomework);
        tlForTeacherHomeworkCheck.addTab(tlForTeacherHomeworkCheck.newTab().setText(R.string.text_tabHomeworkContent));
        tlForTeacherHomeworkCheck.addTab(tlForTeacherHomeworkCheck.newTab().setText(R.string.text_tabHomeworkCheck));




    }

//    private void SetupViewPager(ViewPager viewPager) {
//        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
//        adapter.addFragment(new tap1fragment(), "StudentList");
//        adapter.addFragment(new tap2fragment(), "TeacherList");
//
//        viewPager.setAdapter(adapter);
//    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.teacher_options_manu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.Teacher_Setting:
                Intent intent2 = new Intent(MainActivity.this, TeacherSettingPage.class);
                startActivity(intent2);
                break;
            case R.id.Teacher_Loggin_out:
//                Toast.makeText(getBaseContext(), "Log Out!", Toast.LENGTH_SHORT).show();
                com.example.yangwensing.myapplication.MainActivity.alarmType = 1;

                new com.example.yangwensing.myapplication.MainActivity.AlertDialogFragment().show(getSupportFragmentManager(), "exit"); //呼叫警示視窗fragment

                break;
            case R.id.Teacher_Exit:
                finish();//關閉list
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
//


}
