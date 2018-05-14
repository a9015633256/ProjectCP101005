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

import TeacherMainActivityView.CommonPart.BottomNavigationBarHelper;

//考試科目頁面
public class Navigation2_ExamSubject extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_navigation3_exam_subject);
        setTitle(R.string.ExamSubject);
        TextView title = findViewById(R.id.tvTitle3);
        title.setText("this is view3");
        BottomNavigationView bottomNavigationView = findViewById(R.id.btNavigation_Bar);
        BottomNavigationBarHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_Home:
                        Intent intent0 = new Intent(Navigation2_ExamSubject.this, MainActivity.class);
                        startActivity(intent0);
                        break;


                    case R.id.ic_Homework:
                        Intent intent3 = new Intent(Navigation2_ExamSubject.this, Navigation3_Homework.class);
                        startActivity(intent3);
                        break;

                    case R.id.ic_ExamSubject:
                        break;

                    case R.id.ic_Chat:
                        Intent intent4 = new Intent(Navigation2_ExamSubject.this, Navigation4_ParentConnection.class);
                        startActivity(intent4);
                        break;
                }

                return false;
            }
        });
    }
}
