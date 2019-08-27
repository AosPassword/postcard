package org.wuneng.web.postcard.beans;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class User {
    private int id;
    private String stu_id;
    private String password;
    private String name;
    private boolean is_man;
    private boolean in_school;
    private int graduation_year;
    private String major_name;
    private ArrayList<String> directions;
    private long qq_account;
    private String wechat_account;
    private String email;
    private String city;
    private String slogan;
    private boolean is_deleted;
    private Set<Integer> friends;
    private long phone_number;
    private byte[] slat;
    private String profile_photo;
    private String company;
    private String job;


    public User(int id, String stu_id, String password, String name, boolean is_man, boolean in_school, int graduation_year, String major_name, ArrayList<String> directions, long qq_account, String wechat_account, String email, String city, String slogan, boolean is_deleted, Set<Integer> friends, long phone_number, byte[] slat, String profile_photo, String company, String job) {
        this.id = id;
        this.stu_id = stu_id;
        this.password = password;
        this.name = name;
        this.is_man = is_man;
        this.in_school = in_school;
        this.graduation_year = graduation_year;
        this.major_name = major_name;
        this.directions = directions;
        this.qq_account = qq_account;
        this.wechat_account = wechat_account;
        this.email = email;
        this.city = city;
        this.slogan = slogan;
        this.is_deleted = is_deleted;
        this.friends = friends;
        this.phone_number = phone_number;
        this.slat = slat;
        this.profile_photo = profile_photo;
        this.company = company;
        this.job = job;
    }

    public boolean isIn_school() {
        return in_school;
    }

    public void setIn_school(boolean in_school) {
        this.in_school = in_school;
    }

    public boolean isIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    public Set<Integer> getFriends() {
        return friends;
    }

    public void setFriends(Set<Integer> friends) {
        this.friends = friends;
    }

    public User() {

    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public long getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(long phone_number) {
        this.phone_number = phone_number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStu_id() {
        return stu_id;
    }

    public void setStu_id(String stu_id) {
        this.stu_id = stu_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIs_man() {
        return is_man;
    }

    public void setIs_man(boolean is_man) {
        this.is_man = is_man;
    }

    public int getGraduation_year() {
        return graduation_year;
    }

    public void setGraduation_year(int graduation_year) {
        this.graduation_year = graduation_year;
    }

    public String getMajor_name() {
        return major_name;
    }

    public void setMajor_name(String major_name) {
        this.major_name = major_name;
    }

    public long getQq_account() {
        return qq_account;
    }

    public void setQq_account(long qq_account) {
        this.qq_account = qq_account;
    }


    public String getWechat_account() {
        return wechat_account;
    }

    public void setWechat_account(String wechat_account) {
        this.wechat_account = wechat_account;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }


    public ArrayList<String> getDirections() {
        return directions;
    }

    public void setDirections(ArrayList<String> directions) {
        this.directions = directions;
    }

    public byte[] getSlat() {
        return slat;
    }

    public void setSlat(byte[] slat) {
        this.slat = slat;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", stu_id='" + stu_id + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", is_man=" + is_man +
                ", in_school=" + in_school +
                ", graduation_year=" + graduation_year +
                ", major_name='" + major_name + '\'' +
                ", directions=" + directions +
                ", qq_account=" + qq_account +
                ", wechat_account='" + wechat_account + '\'' +
                ", email='" + email + '\'' +
                ", city='" + city + '\'' +
                ", slogan='" + slogan + '\'' +
                ", is_deleted=" + is_deleted +
                ", friends=" + friends +
                ", phone_number=" + phone_number +
                ", slat='" + new String(slat) + '\'' +
                ", profile_photo='" + profile_photo + '\'' +
                ", company='" + company + '\'' +
                ", job='" + job + '\'' +
                '}';
    }
}
