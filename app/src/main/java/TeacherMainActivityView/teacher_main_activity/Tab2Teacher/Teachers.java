package TeacherMainActivityView.teacher_main_activity.Tab2Teacher;

public class Teachers {

        private int id;
        private String Teacher_Account;
        private String Teacher_Password;
        private String Teacher_Email;
        private int Teacher_Gender;
        private String Teacher_Phone;
        private String Teacher_TakeOfficeDate;

        public Teachers(int id, String teacher_Account, String teacher_Password, String teacher_Email,
                           int teacher_Gender, String teacher_Phone, String teacher_TakeOfficeDate) {
            super();
            this.id = id;
            Teacher_Account = teacher_Account;
            Teacher_Password = teacher_Password;
            Teacher_Email = teacher_Email;
            Teacher_Gender = teacher_Gender;
            Teacher_Phone = teacher_Phone;
            Teacher_TakeOfficeDate = teacher_TakeOfficeDate;
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

        public String getTeacher_Password() {
            return Teacher_Password;
        }

        public void setTeacher_Password(String teacher_Password) {
            Teacher_Password = teacher_Password;
        }

        public String getTeacher_Email() {
            return Teacher_Email;
        }

        public void setTeacher_Email(String teacher_Email) {
            Teacher_Email = teacher_Email;
        }

        public int getTeacher_Gender() {
            return Teacher_Gender;
        }

        public void setTeacher_Gender(int teacher_Gender) {
            Teacher_Gender = teacher_Gender;
        }

        public String getTeacher_Phone() {
            return Teacher_Phone;
        }

        public void setTeacher_Phone(String teacher_Phone) {
            Teacher_Phone = teacher_Phone;
        }

        public String getTeacher_TakeOfficeDate() {
            return Teacher_TakeOfficeDate;
        }

        public void setTeacher_TakeOfficeDate(String teacher_TakeOfficeDate) {
            Teacher_TakeOfficeDate = teacher_TakeOfficeDate;
        }

    }


