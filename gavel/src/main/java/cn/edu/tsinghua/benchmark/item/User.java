package cn.edu.tsinghua.benchmark.item;

import java.util.ArrayList;
import java.util.List;

public class User extends Item {

    // id
    private int userId;

    // 电子邮件
    private String email;

    // 密码
    private String password;

    // 姓
    private String lastName;

    // 名
    private String firstName;

    // 性别
    private String gender;

    // 生日
    private String birthday;

    // 国家
    private String country;

    // 城市
    private String city;

    // 邮政编码
    private String zipCode;

    public User(int userId, String email, String password, String lastName, String firstName, String gender, String birthday, String country, String city, String zipCode) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.lastName = lastName;
        this.firstName = firstName;
        this.gender = gender;
        this.birthday = birthday;
        this.country = country;
        this.city = city;
        this.zipCode = zipCode;
    }

    @Override
    public List<String> collectAttributes() {
        List<String> attributes = new ArrayList<>();
        attributes.add(String.valueOf(userId));
        attributes.add(email);
        attributes.add(password);
        attributes.add(lastName);
        attributes.add(firstName);
        attributes.add(gender);
        attributes.add(birthday);
        attributes.add(country);
        attributes.add(city);
        attributes.add(zipCode);
        return attributes;
    }
}
