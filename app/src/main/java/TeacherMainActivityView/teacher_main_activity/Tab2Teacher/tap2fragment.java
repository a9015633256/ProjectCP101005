package TeacherMainActivityView.teacher_main_activity.Tab2Teacher;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yangwensing.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import TeacherMainActivityView.teacher_main_activity.TeacherContact;
import TeacherMainActivityView.teacher_main_activity.TeacherRecyclerViewAdapter;


//Tab導師資料
public class tap2fragment extends Fragment {
    private static final String TAG = "tap2fragment";

    private RecyclerView recyclerView;
    private List<TeacherContact> teacherContact;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teacher_tap2_contact_fragment, container, false);
        recyclerView = view.findViewById(R.id.contact_recyclerview2);
        TeacherRecyclerViewAdapter teacherRecyclerViewAdapter = new TeacherRecyclerViewAdapter(getContext(), teacherContact);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(teacherRecyclerViewAdapter);

        FloatingActionButton btAdd = view.findViewById(R.id.btAdd2);//浮動按鈕
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new Tab2_AddNewTeachers();
                switchFragment(fragment);

            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        teacherContact = new ArrayList<>();
        teacherContact.add(new TeacherContact("很小的城市", "7777777777", R.drawable.cityscape));
        teacherContact.add(new TeacherContact("很小的森林", "8888888888", R.drawable.forest));
        teacherContact.add(new TeacherContact("很小的樹", "9999999999", R.drawable.trees));
        teacherContact.add(new TeacherContact("很小的瀑布", "0000000000", R.drawable.waterfall));
        teacherContact.add(new TeacherContact("很小的雲", "1234567890", R.drawable.weather_icon));
        teacherContact.add(new TeacherContact("很小的風車", "0987654321", R.drawable.windmills));
    }

    private void switchFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.body2, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
