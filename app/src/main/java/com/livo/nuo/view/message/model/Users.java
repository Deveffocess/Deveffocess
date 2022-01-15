package com.livo.nuo.view.message.model;

import android.content.res.TypedArray;

import java.util.ArrayList;
import java.util.List;

import com.livo.nuo.utility.MyApplication;
import com.livo.nuo.R;

public class Users {

    private static List<User> users;

    private Users() {
    }

    static {
        users = new ArrayList<>();

        // addData();

        addRichData();
    }

    private static void addRichData() {

        String[] firstNames = MyApplication.get().getResources().getStringArray(R.array.first_names);
        String[] lastNames = MyApplication.get().getResources().getStringArray(R.array.last_names);
        TypedArray images = MyApplication.get().getResources().obtainTypedArray(R.array.images);
        String[] designations = MyApplication.get().getResources().getStringArray(R.array.designations);

        for (int i = 0; i < firstNames.length; i++) {
            users.add(User.newBuilder()
                    .firstName("Rohit")
                    .lastName("B")
                    .profilePictureUrl(images.getResourceId(i, 0))
                    .designation("")
                    .uuid("123456")
                    .build());
        }

        images.recycle();



    }


    public static List<User> all() {
        return users;
    }

    public static User getUserById(String id) {
        for (User user : users) {
            if (user.uuid.equals(id))
                return user;
        }
        return User.newBuilder().build();
    }

    public static class User {

        private String firstName, lastName, uuid;
        private String displayName, designation;
        private Integer profilePictureUrl;

        private User(Builder builder) {
            firstName = builder.firstName;
            lastName = builder.lastName;
            uuid = builder.uuid;
            designation = builder.designation;
            profilePictureUrl = builder.profilePictureUrl;
            displayName = firstName + " " + lastName;
        }

        static Builder newBuilder() {
            return new Builder();
        }

        static final class Builder {

            private String firstName;
            private String lastName;
            private String uuid = "null";
            private Integer profilePictureUrl;
            private String designation;

            private Builder() {
            }

            Builder firstName(String firstName) {
                this.firstName = firstName;
                return this;
            }

            Builder lastName(String lastName) {
                this.lastName = lastName;
                return this;
            }

            Builder uuid(String uuid) {
                this.uuid = uuid;
                return this;
            }

            Builder profilePictureUrl(Integer profilePictureUrl) {
                this.profilePictureUrl = profilePictureUrl;
                return this;
            }

            Builder designation(String designation) {
                this.designation = designation;
                return this;
            }

            User build() {
                return new User(this);
            }
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getUuid() {
            return uuid;
        }

        public Integer getProfilePictureUrl() {
            return profilePictureUrl;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getDesignation() {
            return designation;
        }
    }

}
