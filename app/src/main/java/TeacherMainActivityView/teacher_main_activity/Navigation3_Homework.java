package TeacherMainActivityView.teacher_main_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.yangwensing.myapplication.R;
import com.example.yangwensing.myapplication.homework.TeacherHomeworkFragment;

import TeacherMainActivityView.CommonPart.BottomNavigationBarHelper;

//學生作業頁面
public class Navigation3_Homework extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_navigation2_student_homework);
        setTitle(R.string.Homework);
//        TextView title = findViewById(R.id.tvTitle2);
//        title.setText("this is view2");
        BottomNavigationView bottomNavigationView = findViewById(R.id.btNavigation_Bar);
        BottomNavigationBarHelper.disableShiftMode(bottomNavigationView);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new TeacherHomeworkFragment(), "TeacherHomeworkFragment").commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_Home:
                        Intent intent0 = new Intent(Navigation3_Homework.this, MainActivity.class);
                        startActivity(intent0);
                        break;

                    case R.id.ic_Management:
                        Intent intent1 = new Intent(Navigation3_Homework.this, Navigation1_ClassManagement.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_Homework:
                        break;

                    case R.id.ic_ExamSubject:
                        Intent intent3 = new Intent(Navigation3_Homework.this, Navigation2_ExamSubject.class);
                        startActivity(intent3);
                        break;

                    case R.id.ic_Chat:
                        Intent intent4 = new Intent(Navigation3_Homework.this, Navigation4_ParentConnection.class);
                        startActivity(intent4);
                        break;
                }

                return false;
            }
        });
    }
}
