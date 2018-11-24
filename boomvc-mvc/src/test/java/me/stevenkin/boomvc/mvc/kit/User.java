package me.stevenkin.boomvc.mvc.kit;

public class User {
    private String name;
    private String password;
    private Integer code;

    public User(String name, String password, Integer code) {
        this.name = name;
        this.password = password;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", code=" + code +
                '}';
    }
}
