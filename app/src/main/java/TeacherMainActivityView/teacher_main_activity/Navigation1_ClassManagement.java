package TeacherMainActivityView.teacher_main_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yangwensing.myapplication.R;

import TeacherMainActivityView.CommonPart.BottomNavigationBarHelper;

//班級管理頁面
public class Navigation1_ClassManagement extends AppCompatActivity {

    LinearLayout llMain;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_navigation1_class_management);
        setTitle(R.string.ClassManagement);
        TextView title = findViewById(R.id.tvTitle1);
        title.setText("this is view1");

        BottomNavigationView bottomNavigationView = findViewById(R.id.btNavigation_Bar);
        BottomNavigationBarHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_Home:
                        Intent intent0 = new Intent(Navigation1_ClassManagement.this, MainActivity.class);
                        startActivity(intent0);
                        break;


                    case R.id.ic_Homework:
                        Intent intent2 = new Intent(Navigation1_ClassManagement.this, Navigation3_Homework.class);
                        startActivity(intent2);
                        break;

                    case R.id.ic_ExamSubject:
                        Intent intent3 = new Intent(Navigation1_ClassManagement.this, Navigation2_ExamSubject.class);
                        startActivity(intent3);
                        break;

                    case R.id.ic_Chat:
                        Intent intent4 = new Intent(Navigation1_ClassManagement.this, Navigation4_ParentConnection.class);
                        startActivity(intent4);

                        break;
                }

                return false;
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        llMain = findViewById(R.id.llMain);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.teacher_options_manu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Teacher_Setting:
                Intent intent2 = new Intent(Navigation1_ClassManagement.this, TeacherSettingPage.class);
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
}
