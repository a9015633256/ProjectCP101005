package TeacherMainActivityView.teacher_main_activity;


//定義導師資料頁面

public class TeacherContact {
    private String tvName;
    private String tvPhone;
    private int imageview;

    public TeacherContact(String tvName, String tvPhone, int imageview) {
        this.tvName = tvName;
        this.tvPhone = tvPhone;
        this.imageview = imageview;
    }

    public String getTvName() {
        return tvName;
    }

    public String getTvPhone() {
        return tvPhone;
    }

    public int getImageview() {
        return imageview;
    }

    public void setTvName(String tvName) {
        this.tvName = tvName;
    }

    public void setTvPhone(String tvPhone) {
        this.tvPhone = tvPhone;
    }

    public void setImageview(int imageview) {
        this.imageview = imageview;
    }
}


