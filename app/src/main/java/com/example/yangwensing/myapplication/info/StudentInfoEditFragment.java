package com.example.yangwensing.myapplication.info;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.yangwensing.myapplication.R;
import com.example.yangwensing.myapplication.main.Common;
import com.example.yangwensing.myapplication.main.GetImageTask;
import com.example.yangwensing.myapplication.main.MyTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class StudentInfoEditFragment extends Fragment {
    public static final String TAG = "StudentInfoEditFragment";
    public static final int REQUEST_TAKE_PHOTO = 0;
    public static final int REQUEST_PICK_PHOTO = 1;
    public static final int REQUEST_CROP_PHOTO = 2;

    private Student student;
    private EditText etId, etName, etDayOfBirth, etPhoneNumber, etGender, etClassName, etAddress;
    private Button btUpdate, btTakePhoto, btPickPhoto;
    private BottomNavigationView bottomNavigationView;

    private ImageView ivStudentPic;
    private File file;
    private Uri contentUri, croppedImageUri;
    private byte[] image;
    private boolean isPhotoChanged = false;
    private int imageSize;


    //生日輸入、輸出用
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_student_info_edit, container, false); //回傳父元件(linearLayout) 最尾要記得加false否則預設為true
        getActivity().setTitle(R.string.text_editInfo);


        findViews(view);


        //取得上一頁資訊並顯示在畫面上
        getStudentInfo();

        //拍照按鈕
        btTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                Common.askPermissions(getActivity(), permissions, Common.PERMISSION_READ_EXTERNAL_STORAGE);


                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                file = new File(file, "picture.jpg");

                contentUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".provider", file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                if (isIntentAvailable(getActivity(), intent)) {
                    startActivityForResult(intent, REQUEST_TAKE_PHOTO);


                } else {
                    Common.showToast(getActivity(), R.string.msg_NoCameraAppsFound);
                }


            }
        });

        //選照片按鈕
        btPickPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                Common.askPermissions(getActivity(), permissions, Common.PERMISSION_READ_EXTERNAL_STORAGE);


                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_PICK_PHOTO);


            }
        });

        //性別更改
        etGender.setInputType(InputType.TYPE_NULL);
        etGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (etGender.getText().toString().equals("男")) {
                    etGender.setText("女");
                } else {
                    etGender.setText("男");
                }


            }
        });

        //生日更改
        etDayOfBirth.setInputType(InputType.TYPE_NULL);
        etDayOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //顯示datePicker
                DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment();
                datePickerDialogFragment.show(getFragmentManager(), "DatePickerFragment");

            }
        });

        //確認資料送出按紐
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isFormatCorrect()) {
                    return;
                }

                updateInfo();


            }
        });


        return view; //要改成回傳view
    }


    @Override
    public void onStart() {
        super.onStart();

        //隱藏底部導覽列
        bottomNavigationView.setVisibility(View.GONE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Common.PERMISSION_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    btTakePhoto.setEnabled(true);
                    btPickPhoto.setEnabled(true);
                } else {
                    btTakePhoto.setEnabled(false);
                    btPickPhoto.setEnabled(false);
                }
                break;
            default:
                break;


        }
    }


    private boolean isIntentAvailable(Context context, Intent intent) {
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;


    }


    private void updateInfo() {

        if (Common.networkConnected(getActivity())) {

            //學生資料更新
            if (etGender.getText().toString().equals("男")) {
                student.setGender(1);
            } else {
                student.setGender(2);
            }
            try {
                Date date = simpleDateFormat.parse(etDayOfBirth.getText().toString()); //用simpleDateFormat取出date(util.date)
                student.setDayOfBirth(new java.sql.Date(date.getTime())); //轉成sql.date並放入student裡
            } catch (ParseException e) {
                e.printStackTrace();
            }
            student.setPhoneNumber(etPhoneNumber.getText().toString().trim());
            student.setAddress(etAddress.getText().toString());

            //傳資料給db
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            String studentStr = gson.toJson(student);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "updateStudentInfo");
            jsonObject.addProperty("student", studentStr);
            if (isPhotoChanged) {
                String imageBase64 = Base64.encodeToString(image, Base64.DEFAULT);
                jsonObject.addProperty("studentPhoto", imageBase64);
            }


            try {
                String jsonIn = new MyTask(Common.URLForMingTa + "/StudentInfoServlet", jsonObject.toString()).execute().get();
                int count = Integer.valueOf(jsonIn);

                if (count == 1) {

                    Common.showToast(getActivity(), R.string.text_editStudentInfoSucceeded);
                    if (getFragmentManager() != null) {
                        getFragmentManager().popBackStack();
                    }

                } else {
                    Common.showToast(getActivity(), R.string.text_editStudentInfoFailed);

                }


            } catch (Exception e) {
                e.printStackTrace();
                Common.showToast(getActivity(), R.string.text_no_server);

            }
        } else {
            Common.showToast(getActivity(), R.string.text_no_network);

        }


    }

    private boolean isFormatCorrect() {

        if (!etPhoneNumber.getText().toString().trim().matches("\\d*")) {
            etPhoneNumber.setError("Please Check Format!");
            return false;
        }

        return true;

    }


    private void findViews(View view) {
        etId = view.findViewById(R.id.etStudentInfoId);
        etName = view.findViewById(R.id.etStudentInfoName);
        etClassName = view.findViewById(R.id.etStudentInfoClassName);
        etGender = view.findViewById(R.id.etStudentInfoGender);
        etDayOfBirth = view.findViewById(R.id.etStudentInfoDayOfBirth);
        etPhoneNumber = view.findViewById(R.id.etStudentInfoPhoneNumber);
        etAddress = view.findViewById(R.id.etStudentInfoAddress);
        ivStudentPic = view.findViewById(R.id.ivUserPic);
        btUpdate = view.findViewById(R.id.btUpdateStudentInfo);
        btTakePhoto = view.findViewById(R.id.btStudentInfoTakePhoto);
        btPickPhoto = view.findViewById(R.id.btStudentInfoPickPhoto);
        bottomNavigationView = getActivity().findViewById(R.id.bnForStudent);


    }

    private void getStudentInfo() {


        Bundle bundle = getArguments();
        if (bundle != null) {
            student = (Student) bundle.getSerializable("student");
        }

        //文字部分
        if (student != null) {
            etId.setText(student.getStudentNumber());
            etName.setText(student.getName());
            etClassName.setText(student.getClassName());
            if (student.getGender() == 1) {
                etGender.setText("男");

            } else {
                etGender.setText("女");

            }
            etDayOfBirth.setText(simpleDateFormat.format(student.getDayOfBirth()));
            etPhoneNumber.setText(String.valueOf(student.getPhoneNumber()));
            etAddress.setText((student.getAddress()));


        }


        //取得照片部分
        if (Common.networkConnected(getActivity())) {
            imageSize = getResources().getDisplayMetrics().widthPixels / 3;
            GetImageTask getStudentPicTask = new GetImageTask(Common.URLForMingTa + "/StudentInfoServlet", student.getId(), imageSize);
            try {
                Bitmap bitmap = getStudentPicTask.execute().get();
                ivStudentPic.setImageBitmap(bitmap);


            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            Common.showToast(getActivity(), R.string.text_no_network);

        }


    }


    @Override
    public void onStop() {
        //重新顯示底部導覽列
        bottomNavigationView.setVisibility(View.VISIBLE);

        super.onStop();
    }


    //datePicker選生日
    public static class DatePickerDialogFragment extends DialogFragment {
        EditText etDayOfBirthForPicker = null;

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            if (getFragmentManager() != null) {
                etDayOfBirthForPicker = getFragmentManager().findFragmentByTag("StudentInfoEditFragment").getView().findViewById(R.id.etStudentInfoDayOfBirth);
            }


            //監聽器，選好日期就顯示在畫面上
            DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
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


                    etDayOfBirthForPicker.setText(simpleDateFormat.format(date));


                }

            };

            //產生picker時會以畫面上一開始的時間為預設時間
            String[] date = etDayOfBirthForPicker.getText().toString().split("-");
            return new DatePickerDialog(getActivity(), onDateSetListener, Integer.valueOf(date[0]), Integer.valueOf(date[1]) - 1, Integer.valueOf(date[2]));


        }


    }


    private void crop(Uri uri) {
        File file = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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
            Common.showToast(getActivity(), "This device doesn't support the crop action!");

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
                        Bitmap croppedBitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(croppedImageUri));

                        //顯示圖片用
                        Bitmap downSizedBitmap = Common.downSize(croppedBitmap, imageSize);
                        ivStudentPic.setImageBitmap(downSizedBitmap);

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


}

