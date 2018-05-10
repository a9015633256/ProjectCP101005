package TeacherMainActivityView.teacher_main_activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yangwensing.myapplication.R;

import java.util.List;

//導師資料的RecyclerView建構群
public class TeacherRecyclerViewAdapter extends RecyclerView.Adapter<TeacherRecyclerViewAdapter.MyViewHolder> {

        Context context;
        List<TeacherContact> contacts;

public TeacherRecyclerViewAdapter(Context context, List<TeacherContact> contacts) {
        this.context = context;
        this.contacts = contacts;
        }

@NonNull
@Override
public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(context).inflate(R.layout.teacher_tab2_teachers_contact,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
        }

@Override
public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvName.setText(contacts.get(position).getTvName());
        holder.tvPhone.setText(contacts.get(position).getTvPhone());
        holder.imageView.setImageResource(contacts.get(position).getImageview());



        }

@Override
public int getItemCount() {
        return contacts.size();
        }

public static class MyViewHolder extends RecyclerView.ViewHolder{

    private TextView tvName;
    private TextView tvPhone;
    private ImageView imageView;

    public MyViewHolder(View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.teacherName);
        tvPhone = itemView.findViewById(R.id.teacherPhone);
        imageView = itemView.findViewById(R.id.teacherImageview);


    }
}


}
