package TeacherMainActivityView.teacher_main_activity.Tab2Teacher;

public class ClassSubjectTeacher {
    private int id;
    private String Teacher_Account;
    private String Teacher_Email;
    private String Teacher_Phone;
    private String Class_Name;

    public ClassSubjectTeacher(int id, String teacher_Account, String teacher_Email, String teacher_Phone,
                               String class_Name) {
        super();
        this.id = id;
        Teacher_Account = teacher_Account;
        Teacher_Email = teacher_Email;
        Teacher_Phone = teacher_Phone;
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

    public String getClass_Name() {
        return Class_Name;
    }

    public void setClass_Name(String class_Name) {
        Class_Name = class_Name;
    }
}
