package TeacherMainActivityView.teacher_main_activity.Tab2Teacher;

public class ClassSubjectTeacher {
    private int id;
    private String Teacher_Account;
    private String Teacher_Email;
    private String Teacher_Phone;
    private int Teacher_Gender;
    private String Teacher_TakeOfficeDate;
    private String Class_Name;


    public ClassSubjectTeacher(int id, String teacher_Account, String teacher_Email, String teacher_Phone,
                               int teacher_Gender, String teacher_TakeOfficeDate, String class_Name) {
        super();
        this.id = id;
        Teacher_Account = teacher_Account;
        Teacher_Email = teacher_Email;
        Teacher_Phone = teacher_Phone;
        Teacher_Gender = teacher_Gender;
        Teacher_TakeOfficeDate = teacher_TakeOfficeDate;
        Class_Name = class_Name;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeacher_Account() {
        return Teacher_Account;
    }

    public void setTeacher_Account(String teacher_Account) {
        Teacher_Account = teacher_Account;
    }

    public String getTeacher_Email() {
        return Teacher_Email;
    }

    public void setTeacher_Email(String teacher_Email) {
        Teacher_Email = teacher_Email;
    }

    public String getTeacher_Phone() {
        return Teacher_Phone;
    }

    public void setTeacher_Phone(String teacher_Phone) {
        Teacher_Phone = teacher_Phone;
    }

    public int getTeacher_Gender() {
        return Teacher_Gender;
    }

    public void setTeacher_Gender(int teacher_Gender) {
        Teacher_Gender = teacher_Gender;
    }

    public String getTeacher_TakeOfficeDate() {
        return Teacher_TakeOfficeDate;
    }

    public void setTeacher_TakeOfficeDate(String teacher_TakeOfficeDate) {
        Teacher_TakeOfficeDate = teacher_TakeOfficeDate;
    }

    public String getClass_Name() {
        return Class_Name;
    }

    public void setClass_Name(String class_Name) {
        Class_Name = class_Name;
    }

}
