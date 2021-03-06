package com.qiyei.framework.database.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * @author Created by qiyei2015 on 2019/12/19.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description:
 */
@Entity
public class User {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "first_name")
    private String firstName;

    @ColumnInfo(name = "last_name")
    private String lastName;

    @ColumnInfo(name = "sex")
    private String sex;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;
        return uid == user.uid &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(sex, user.sex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, firstName, lastName, sex);
    }

    /**
     * @return {@link #uid}
     */
    public int getUid() {
        return uid;
    }

    /**
     * @param uid the {@link #uid} to set
     */
    public void setUid(int uid) {
        this.uid = uid;
    }

    /**
     * @return {@link #firstName}
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the {@link #firstName} to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return {@link #lastName}
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the {@link #lastName} to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return {@link #sex}
     */
    public String getSex() {
        return sex;
    }

    /**
     * @param sex the {@link #sex} to set
     */
    public void setSex(String sex) {
        this.sex = sex;
    }
}
