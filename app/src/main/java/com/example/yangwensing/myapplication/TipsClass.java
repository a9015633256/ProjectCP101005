package com.example.yangwensing.myapplication;

public class TipsClass {

    /*
    這是一個記事本，各個Class,Activity,layout,style乃至manifest可以的話把版面的功用新增在這裡

    Activity區

    **classes





    **contact

    StudentContactFragment:學生用 老師列表檢視
    StudentContactDetailFragment:學生用 老師詳細檢視
    Teacher:列表用的老師列表，之後會刪除改用mainPackage裡的


    **exam

    StudentExamFragment:學生成績檢視

    **homework

    AssignDate:rv用的作業類別，按照日期分類
    Homework:作業類別
    HomeworkCheck:老師編輯學生作業未完成用的類別
    HomeworkIsDone:學生看作業檢視用的類別
    StudentHomeworkDetailFragment:學生作業內容
    StudentHomeworkFragment:學生作業列表
    TeacherHomeworkAddFragment:老師新增作業
    TeacherHomeworkCheckFragment:老師編輯學生作業未完成
    TeacherHomeworkFragment:老師看自己負責的作業列表
    TeacherHomeworkUpdateDeleteFragment:老師編輯作業內容


    **info

    Student:學生類別
    StudentInfoEditFragment:學生個人資料編輯
    StudentInfoFragment:學生個人資料

    **login


    **main

    Account:
    BottomNavigationViewHelper:底部導覽列輔助(item超過三個時才不會有浮動動畫)
    Common:放檢查網路連線的方法、連接webApp的URL位址、圖片縮小方法、showToast方法、取pref的整數值方法、取得permission方法
    MainActivity:放底下導覽列控制、放右上角menu控制
    GetImageTask:開子執行緒連接web取得圖片
    MyTask:開子執行緒連接web

    **TeacherMainActivityView

    *CommonPart
    BottomNavigationBarHelper       控制底下的NavigationBar讓他不要浮動
    Common                          基本連線資料
    MyTask                          基本抓取資料介面
    SectionPageAdapter              控制Tab Bar

    *Tab1Student
    StudentGetImageTask             抓取圖片的Task
    Students                        學生的基本資料
    Tab1_StudentCreatAccount        新增學生帳號的介面
    tab1Fragment                    觀看學生列表的介面


    *Tab2Teacher
    TeacherGetImageTask             抓取圖片的Task
    Teachers                        老師的基本資料
    Tab2_AddNewTeachers             新增老師的介面
    tap2fragment                    觀看老師列表的介面

    MainActivity                    老師的主畫面
    Navigation1_ClassManagement     班級管理（之後會刪除
    Navigation2_ExamSubject         考試管理
    Navigation3_Homework            功課管理
    Navigation4_ParentConnection    連絡家長
    TeacherChangeFilePage           更改資料介面
    TeacherContact                  （假資料，之後會刪除
    TeacherRecyclerViewAdapter      （假資料，之後會刪除
    TeacherSettingPage              （假資料，之後會刪除




    -----------

    layout區

    activity_main:有放底部導覽列

    fragment_student_info:學生個人資料
    fragment_student_info_edit:學生個人資料編輯
    fragment_student_homework:學生作業檢視
    fragment_student_homework_detail:學生作業檢視的細項的格式
    fragment_student_exam:學生成績檢視
    fragment_student_contact:學生用 老師列表檢視
    fragment_student_contact_detail:學生用 老師詳細資料檢視

    cardview_homework:rv用
    cardview_exam:rv用
    cardview_contact:rv用

    teacher_activity_main               老師的主畫面
    teacher_change_file                 更改資料畫面
    teacher_navigation1                 班級管理
    teacher_navigation2                 學生功課
    teacher_navigation3                 考試管理
    teacher_navigation4                 家長聯絡
    teacher_profile                     老師資料
    teacher_setting                     設定頁面
    teacher_setting_menu                設定頁面
    teacher_tab1_student_creat_account  新增學生帳號
    teacher_tab1_students_contact       學生的recyclerView
    teacher_tab2_add_new_teacher        新增老師帳號
    teacher_tab2_teachers_contact       老師的recyclerView
    teacher_tab1_contact_fragment       Tab1畫面
    teacher_tab2_contact_fragment       Tab2畫面









    -----------

    manifest區
    以下六個頁面是extends AppCompatActivity
    <activity android:name="TeacherMainActivityView.teacher_main_activity.Navigation1_ClassManagement"></activity>
    <activity android:name="TeacherMainActivityView.teacher_main_activity.Navigation3_Homework"></activity>
    <activity android:name="TeacherMainActivityView.teacher_main_activity.Navigation2_ExamSubject"></activity>
    <activity android:name="TeacherMainActivityView.teacher_main_activity.Navigation4_ParentConnection"></activity>
    <activity android:name="TeacherMainActivityView.teacher_main_activity.TeacherSettingPage"></activity>
    <activity android:name="TeacherMainActivityView.teacher_main_activity.TeacherChangeFilePage"></activity>













     */

}
