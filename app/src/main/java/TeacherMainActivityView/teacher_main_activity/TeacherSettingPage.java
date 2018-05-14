package TeacherMainActivityView.teacher_main_activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yangwensing.myapplication.R;
import com.example.yangwensing.myapplication.main.Common;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import TeacherMainActivityView.CommonPart.MyTask;
import TeacherMainActivityView.teacher_main_activity.Tab2Teacher.TeacherGetImageTask;
import TeacherMainActivityView.teacher_main_activity.Tab2Teacher.Teachers;

public class TeacherSettingPage extends AppCompatActivity {
    private static final String TAG = "TeacherSettingPage";
    private EditText etTeacherInfoName;
    private EditText etTeacherInfoDayOfBirth;
    private EditText etTeacherInfoGender;
    private EditText etTeacherInfoPhoneNumber;
    private EditText etTeacherInfoId;
    private ImageView ivTeacherPic;
    private MyTask TeacherGetFileTask, TeacherUpdateTask;
    private TeacherGetImageTask teacherGetImageTask;
    private Button btTackPhotoClick, btPickPhotoClick;
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    private boolean isPhotoChanged = false;
    private File file;
    private Uri contentUri, croppedImageUri;
    private int imageSize;
    private byte[] image;
    private int ida;
    public static final int REQUEST_TAKE_PHOTO = 0;
    public static final int REQUEST_PICK_PHOTO = 1;
    public static final int REQUEST_CROP_PHOTO = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_change_file);
        setTitle(R.id.Teacher_Setting);
        findViews();
    }

    private void findViews() {
        etTeacherInfoId = findViewById(R.id.etTeacherInfoId);
        etTeacherInfoName = findViewById(R.id.etTeacherInfoName);
        etTeacherInfoGender = findViewById(R.id.etTeacherInfoGender);
        etTeacherInfoPhoneNumber = findViewById(R.id.etTeacherInfoPhoneNumber);
        etTeacherInfoDayOfBirth = findViewById(R.id.etTeacherInfoDayOfBirth);
        ivTeacherPic = findViewById(R.id.ivTeacherPic);
        btTackPhotoClick = findViewById(R.id.btTeacherInfoPickPhoto);
        btPickPhotoClick = findViewById(R.id.btTeacherInfoPickPhoto);

        etTeacherInfoGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseGender();
            }
        });

        etTeacherInfoDayOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDate();
            }
        });

    }

    //create dialog
    private void chooseGender() {
        CharSequence[] values = {" Male ", " Female "};
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.ChooseGender);
        final int choose = 0;
        alert.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        etTeacherInfoGender.setText(R.string.text_rbMale);
                        break;
                    case 1:
                        etTeacherInfoGender.setText(R.string.text_rbFemale);
                        break;
                }
            }
        });
        alert.show();
    }

    //選生日  暫時無法作動
    private void chooseDate() {
        etTeacherInfoDayOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialogFragment datePickerDialog = new DatePickerDialogFragment();
                datePickerDialog.show(getSupportFragmentManager(), "DatePickerFragment");

            }
        });
    }

    public static class DatePickerDialogFragment extends DialogFragment {
        EditText etTeacherInfoDayOfBirth = null;

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            if (getFragmentManager() != null) {
                etTeacherInfoDayOfBirth = getActivity().findViewById(R.id.etTeacherInfoDayOfBirth);
            }

            android.app.DatePickerDialog.OnDateSetListener onDateSetListener = new android.app.DatePickerDialog.OnDateSetListener() {//這兩行無法作動
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String newDayOfBirth = year + "-" +
                            (month + 1) +
                            "-" +
                            dayOfMonth;

                    Date date = null;
                    try {
                        date = simpleDateFormat.parse(newDayOfBirth);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    etTeacherInfoDayOfBirth.setText(simpleDateFormat.format(date));
                }
            };

            String[] date = etTeacherInfoDayOfBirth.getText().toString().split("-");
            return new android.app.DatePickerDialog
                    (getActivity(), onDateSetListener, Integer.valueOf(date[0]),
                            Integer.valueOf(date[1]) - 1, Integer.valueOf(date[2]));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        fillProfile();
    }

    //取值部
    private void fillProfile() {
        SharedPreferences preferences = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
        final String userId = preferences.getString("name", "");
        if (userId.isEmpty()) {
            Common.showToast(this, R.string.msg_NoProfileFound);
            finish();
        }
        if (Common.networkConnected(this)) {
            String url = Common.URL + "/TeachersListServerlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findById");
            jsonObject.addProperty("Teacher_Account", userId);
            String jsonOut = jsonObject.toString();
            TeacherGetFileTask = new MyTask(url, jsonOut);
            List<Teachers> teachers = null;
            try {
                String result = TeacherGetFileTask.execute().get();
                Type type = new TypeToken<List<Teachers>>() {
                }.getType();
                teachers = new Gson().fromJson(result, type);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.toString());
            }
            if (teachers == null) {
                Common.showToast(TeacherSettingPage.this, R.string.msg_NoProfileFound);
                finish();
            } else {
                int ida = teachers.get(0).getId();
                preferences = getSharedPreferences
                        (com.example.yangwensing.myapplication.main.Common.PREF_FILE, Context.MODE_PRIVATE);
                preferences.edit().putInt("ida", ida).apply();
                String account = teachers.get(0).getTeacher_Account();
                etTeacherInfoId.setText(account);
                String mail = teachers.get(0).getTeacher_Email();
                etTeacherInfoName.setText(mail);//不知道為什麼不起作用
                int gender = teachers.get(0).getTeacher_Gender();
                if (gender == 1) {
                    etTeacherInfoGender.setText(R.string.text_rbMale);
                } else {
                    etTeacherInfoGender.setText(R.string.text_rbFemale);
                }
                String phone = teachers.get(0).getTeacher_Phone();
                etTeacherInfoPhoneNumber.setText(phone);
                String date = teachers.get(0).getTeacher_TakeOfficeDate();
                etTeacherInfoDayOfBirth.setText(date);

                ida = preferences.getInt("ida", 0);
                int imageSize = getResources().getDisplayMetrics().widthPixels / 3;
                teacherGetImageTask = new TeacherGetImageTask(TeacherMainActivityView.CommonPart.Common.URL + "/TeachersListServerlet", ida, imageSize);
                teacherGetImageTask = new TeacherGetImageTask(url, ida, imageSize, ivTeacherPic);
                teacherGetImageTask.execute();


            }
        } else {
            Common.showToast(this, R.string.msg_NoNetwork);
        }

    }

    //上傳部
    public void btTeacherProfileUpdate(final View view) {
        ida = getTeacherId();
        String at = etTeacherInfoId.getText().toString().trim();
        String ml = etTeacherInfoName.getText().toString().trim();
        String pe = etTeacherInfoPhoneNumber.getText().toString().trim();
        String gr = etTeacherInfoGender.getText().toString().trim();
        int grn = 0;
        if (gr == "Male"){
            grn = 1;
        }else {
            grn = 2;
        }
        String de = etTeacherInfoDayOfBirth.getText().toString().trim();

        String massage = "";
        boolean isInputValid = true;
        if (at.isEmpty()) {
            massage += getString(R.string.text_UserId) + " "
                    + getString(R.string.msg_InputEmpty) + "\n";
            isInputValid = false;
        }
        if (ml.isEmpty()) {
            massage += getString(R.string.text_UserName) + " "
                    + getString(R.string.msg_InputEmpty) + "\n";
            isInputValid = false;
        }
        if (gr.isEmpty()) {
            massage += getString(R.string.text_UseGender) + " "
                    + getString(R.string.msg_InputEmpty) + "\n";
            isInputValid = false;
        }
        if (pe.isEmpty()) {
            massage += getString(R.string.text_UserPhone) + " "
                    + getString(R.string.msg_InputEmpty) + "\n";
            isInputValid = false;
        }
        if (de.isEmpty()) {
            massage += getString(R.string.text_UserBirth) + " "
                    + getString(R.string.msg_InputEmpty) + "\n";
            isInputValid = false;
        }
        Common.showToast(this, massage);
        Teachers teachers = new Teachers(ida,at,"",ml,grn,pe,de);
        if (isInputValid) {
            if (Common.networkConnected(TeacherSettingPage.this)) {
                String url = Common.URL + "/TeachersListServerlet";
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "update");
                jsonObject.addProperty("teacherList", new Gson().toJson(teachers));
                if (isPhotoChanged) {
                    String imageBase64 = Base64.encodeToString(image, Base64.DEFAULT);
                    jsonObject.addProperty("studentPhoto", imageBase64);
                }
                String jsonOut = jsonObject.toString();
                TeacherUpdateTask = new MyTask(url, jsonOut);
                int count = 0;
                try {
                    String result = TeacherUpdateTask.execute().get();
                    count = Integer.valueOf(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (count == 0) {
                    Common.showToast(this, R.string.msg_UpdateFail);
                } else {
                    Common.showToast(this, R.string.msg_UpdateSuccess);
                }
            } else {
                Common.showToast(this, R.string.msg_NoNetwork);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Common.PERMISSION_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    btTackPhotoClick.setEnabled(true);
                    btPickPhotoClick.setEnabled(true);
                } else {
                    btTackPhotoClick.setEnabled(false);
                    btPickPhotoClick.setEnabled(false);
                }
                break;

            default:
                break;

        }

    }

    public boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,//packageManager管理手機裡面的APP資訊
                PackageManager.MATCH_DEFAULT_ONLY);//使用預設可以開啟的功能
        return list.size() > 0;//只要大於零就回傳ture
    }

    //照相
    public void btTackPhotoClick(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = getExternalFilesDir(Environment.DIRECTORY_PICTURES);//想要存的地方，PICTURES私有目錄
        file = new File(file, "picture.jpg");//私有名稱
        contentUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);//授權取得package，
        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);//URI路徑
        if (isIntentAvailable(this, intent)) {
            startActivityForResult(intent, REQUEST_TAKE_PHOTO);
        } else {
            Toast.makeText(this, R.string.msg_NoCameraAppsFound,
                    Toast.LENGTH_SHORT).show();
        }
    }

    //挑照片
    public void btPickPhotoClick(View view) {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
        Common.askPermissions(this, permissions, Common.PERMISSION_READ_EXTERNAL_STORAGE);

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//媒體庫
        startActivityForResult(intent, REQUEST_PICK_PHOTO);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (TeacherUpdateTask != null) {
            TeacherUpdateTask.cancel(true);
        }
        if (TeacherGetFileTask != null) {
            TeacherGetFileTask.cancel(true);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO:
                    crop(contentUri);

                    break;
                case REQUEST_PICK_PHOTO:
                    Uri uri = data.getData();
                    crop(uri);

                    break;

                case REQUEST_CROP_PHOTO:

                    isPhotoChanged = true;
                    Log.d(TAG, "REQUEST_CROP_PHOTO: " + croppedImageUri.toString());

                    try {
                        Bitmap croppedBitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(croppedImageUri));

                        //顯示圖片用
                        Bitmap downSizedBitmap = Common.downSize(croppedBitmap, imageSize);
                        ivTeacherPic.setImageBitmap(downSizedBitmap);

                        //上傳圖片用
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        image = byteArrayOutputStream.toByteArray();


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }

        }

    }

    private void crop(Uri uri) {
        File file = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        file = new File(file, "picture_crop.jpg");
        croppedImageUri = Uri.fromFile(file);


        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cropIntent.setDataAndType(uri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 0);
            cropIntent.putExtra("aspectY", 0);
            cropIntent.putExtra("outputX", 0);
            cropIntent.putExtra("outputY", 0);
            cropIntent.putExtra("scale", true);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, croppedImageUri);
            cropIntent.putExtra("return-data", true);

            startActivityForResult(cropIntent, REQUEST_CROP_PHOTO);
        } catch (ActivityNotFoundException e) {
            Common.showToast(this, "This device doesn't support the crop action!");

        }

    }
    private int getTeacherId() {
        SharedPreferences preferences = getSharedPreferences(com.example.yangwensing.myapplication.main.Common.PREF_FILE, Context.MODE_PRIVATE);
        int ida = preferences.getInt("ida", 0);
        return ida;
    }

}