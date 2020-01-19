package com.gmail.supersonicleader.service.model;

public class PrintUserDTO {

    private int id;
    private String username;
    private String password;
    private boolean isActive;
    private String userGroupName;
    private int age;
    private String address;
    private String telephone;

    private PrintUserDTO(Builder builder) {
        id = builder.id;
        username = builder.username;
        password = builder.password;
        isActive = builder.isActive;
        userGroupName = builder.userGroupName;
        age = builder.age;
        address = builder.address;
        telephone = builder.telephone;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getUserGroupName() {
        return userGroupName;
    }

    public int getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public String getTelephone() {
        return telephone;
    }

    @Override
    public String toString() {
        return "PrintUserDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", isActive=" + isActive +
                ", userGroupName='" + userGroupName + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                ", telephone='" + telephone + '\'' +
                '}';
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {

        private int id;
        private String username;
        private String password;
        private boolean isActive;
        private String userGroupName;
        private int age;
        private String address;
        private String telephone;

        private Builder() {}

        public Builder id(int val) {
            id = val;
            return this;
        }

        public Builder username(String val) {
            username = val;
            return this;
        }

        public Builder password(String val) {
            password = val;
            return this;
        }

        public Builder isActive(boolean val) {
            isActive = val;
            return this;
        }

        public Builder userGroupName(String val) {
            userGroupName = val;
            return this;
        }

        public Builder age(int val) {
            age = val;
            return this;
        }

        public Builder address(String val) {
            address = val;
            return this;
        }

        public Builder telephone(String val) {
            telephone = val;
            return this;
        }

        public PrintUserDTO build() {
            return new PrintUserDTO(this);
        }

    }

}
