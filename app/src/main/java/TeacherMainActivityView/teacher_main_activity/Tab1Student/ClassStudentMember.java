package TeacherMainActivityView.teacher_main_activity.Tab1Student;

public class ClassStudentMember {

    private int id;
    private String Student_Name;
    private String Student_Phone;
    private String Class_Name;

    public ClassStudentMember(int id, String student_Name, String student_Phone, String class_Name) {
        super();
        this.id = id;
        Student_Name = student_Name;
        Student_Phone = student_Phone;
        Class_Name = class_Name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudent_Name() {
        return Student_Name;
    }

    public void setStudent_Name(String student_Name) {
        Student_Name = student_Name;
    }

    public String getStudent_Phone() {
        return Student_Phone;
    }

    public void setStudent_Phone(String student_Phone) {
        Student_Phone = student_Phone;
    }

    public String getClass_Name() {
        return Class_Name;
    }

    public void setClass_Name(String class_Name) {
        Class_Name = class_Name;
    }


}
