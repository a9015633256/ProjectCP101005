package TeacherMainActivityView.teacher_main_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.yangwensing.myapplication.R;
import com.example.yangwensing.myapplication.chat.ChatList;

import TeacherMainActivityView.CommonPart.BottomNavigationBarHelper;

//家長聯絡頁面
public class Navigation4_ParentConnection extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_navigation4_parent_connection);
        setTitle(R.string.ParentConnection);
        TextView title = findViewById(R.id.tvTitle4);
        title.setText("this is view4");
        BottomNavigationView bottomNavigationView = findViewById(R.id.btNavigation_Bar);
        BottomNavigationBarHelper.disableShiftMode(bottomNavigationView);

        Fragment chatList = new ChatList();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.main_content,chatList);
        fragmentTransaction.commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_Home:
                        Intent intent0 = new Intent(Navigation4_ParentConnection.this, MainActivity.class);
                        startActivity(intent0);
                        break;

                    case R.id.ic_Management:
                        Intent intent1 = new Intent(Navigation4_ParentConnection.this, Navigation1_ClassManagement.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_Homework:
                        Intent intent3 = new Intent(Navigation4_ParentConnection.this, Navigation3_Homework.class);
                        startActivity(intent3);
                        break;

                    case R.id.ic_ExamSubject:
                        Intent intent4 = new Intent(Navigation4_ParentConnection.this, Navigation2_ExamSubject.class);
                        startActivity(intent4);
                        break;

                    case R.id.ic_Chat:
                        break;
                }

                return false;
            }
        });
    }
}
