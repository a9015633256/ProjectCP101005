package TeacherMainActivityView.teacher_main_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.yangwensing.myapplication.R;

import TeacherMainActivityView.CommonPart.BottomNavigationBarHelper;
import TeacherMainActivityView.CommonPart.SectionsPageAdapter;
import TeacherMainActivityView.teacher_main_activity.Tab1Student.tap1fragment;
import TeacherMainActivityView.teacher_main_activity.Tab2Teacher.tap2fragment;

public class MainActivity extends AppCompatActivity {
    //主要管理頁面

    private static final String TAG = "MainActivity";

    private SectionsPageAdapter msectionsPageAdapter;

    private ViewPager mviewPager;
    public static ViewPager viewPager;
    private FloatingActionButton btAdd;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_activity_main);
        setTitle(R.string.app_name);
        Log.d(TAG, "onCreat:Starting.");
        msectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        mviewPager = findViewById(R.id.container);
        SetupViewPager(mviewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mviewPager);

        BottomNavigationView bottomNavigationView = findViewById(R.id.btNavigation_Bar);
        BottomNavigationBarHelper.disableShiftMode(bottomNavigationView);

        //切換NavigationBar的原始碼
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_Home:
                        break;

                    case R.id.ic_Management:
                        Intent intent1 = new Intent(MainActivity.this, Navigation1_ClassManagement.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_Homework:
                        Intent intent2 = new Intent(MainActivity.this, Navigation3_Homework.class);
                        startActivity(intent2);
                        break;

                    case R.id.ic_ExamSubject:
                        Intent intent3 = new Intent(MainActivity.this, Navigation2_ExamSubject.class);
                        startActivity(intent3);
                        break;

                    case R.id.ic_Chat:
                        Intent intent4 = new Intent(MainActivity.this, Navigation4_ParentConnection.class);
                        startActivity(intent4);
                        break;
                }

                return false;
            }
        });
        //切換NavigationBar的原始碼

    }

    private void SetupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new tap1fragment(), "StudentList");
        adapter.addFragment(new tap2fragment(), "TeacherList");

        viewPager.setAdapter(adapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
//        llMain = findViewById(R.id.llMain);
//        MenuInflater inflater = getMenuInflater();
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.teacher_options_manu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Teacher_ChangeFile:
                Intent intent5 = new Intent(MainActivity.this, TeacherChangeFilePage.class);
                startActivity(intent5);
                break;
            case R.id.Teacher_Setting:
                Intent intent2 = new Intent(MainActivity.this, TeacherSettingPage.class);
                startActivity(intent2);
                break;
            case R.id.Teacher_Loggin_out:
                Toast.makeText(getBaseContext(), "Log Out!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Teacher_Exit:
                finish();//關閉list
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void showFloatingActionButton() {
        btAdd = findViewById(R.id.btAdd);
        btAdd.show();
    }

    public void hideFloatingActionButton() {
        btAdd = findViewById(R.id.btAdd);
        btAdd.hide();
    }

}
