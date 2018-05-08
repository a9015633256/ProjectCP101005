package TeacherMainActivityView.teacher_main_activity.Tab1Student;

public class Students {
    private int id;
    private String Student_Name;
    private String Student_Gender;
    private String Student_Birthday;
    private String Student_ID;
    private String Student_Phone;
    private String Student_AdmissionDate;
    private String Student_Address;
    private String Student_Class;

    public Students(int id, String student_Name, String student_Gender, String student_Birthday, String student_ID, String student_Phone, String student_AdmissionDate, String student_Address, String student_Class) {
        this.id = id;
        Student_Name = student_Name;
        Student_Gender = student_Gender;
        Student_Birthday = student_Birthday;
        Student_ID = student_ID;
        Student_Phone = student_Phone;
        Student_AdmissionDate = student_AdmissionDate;
        Student_Address = student_Address;
        Student_Class = student_Class;
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

    public String getStudent_Gender() {
        return Student_Gender;
    }

    public void setStudent_Gender(String student_Gender) {
        Student_Gender = student_Gender;
    }

    public String getStudent_Birthday() {
        return Student_Birthday;
    }

    public void setStudent_Birthday(String student_Birthday) {
        Student_Birthday = student_Birthday;
    }

    public String getStudent_ID() {
        return Student_ID;
    }

    public void setStudent_ID(String student_ID) {
        Student_ID = student_ID;
    }

    public String getStudent_Phone() {
        return Student_Phone;
    }

    public void setStudent_Phone(String student_Phone) {
        Student_Phone = student_Phone;
    }

    public String getStudent_AdmissionDate() {
        return Student_AdmissionDate;
    }

    public void setStudent_AdmissionDate(String student_AdmissionDate) {
        Student_AdmissionDate = student_AdmissionDate;
    }

    public String getStudent_Address() {
        return Student_Address;
    }

    public void setStudent_Address(String student_Address) {
        Student_Address = student_Address;
    }

    public String getStudent_Class() {
        return Student_Class;
    }

    public void setStudent_Class(String student_Class) {
        Student_Class = student_Class;
    }
}


