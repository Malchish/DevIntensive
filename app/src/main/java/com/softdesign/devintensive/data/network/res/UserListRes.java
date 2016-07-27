package com.softdesign.devintensive.data.network.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.softdesign.devintensive.data.storage.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alena on 15.07.16.
 */
public class UserListRes {

        @SerializedName("success")
        @Expose
        private boolean success;
        @SerializedName("data")
        @Expose
        private List<UserData> data = new ArrayList<>();

        public List<UserData> getData() {
            return data;
        }

        public class UserData{

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("second_name")
        @Expose
        private String secondName;
        @SerializedName("__v")
        @Expose
        private int v;
        @SerializedName("repositories")
        @Expose
        private UserModelRes.Repositories repositories;
        @SerializedName("profileValues")
        @Expose
        private UserModelRes.ProfileValues profileValues;
        @SerializedName("publicInfo")
        @Expose
        private UserModelRes.PublicInfo publicInfo;
        @SerializedName("specialization")
        @Expose
        private String specialization;
        @SerializedName("updated")
        @Expose
        private String updated;



            public String getFullName() {
                return firstName + " " + secondName;
            }
            public UserModelRes.ProfileValues getProfileValues() {
                return profileValues;
            }

           /* public List<Repository> getRepositories() {
                return (List<Repository>) repositories;
            }*/

            public UserModelRes.PublicInfo getPublicInfo() {
                return publicInfo;
            }

            public String getId() {
                return id;
            }


            public UserModelRes.Repositories getRepositories() {
                return repositories;
            }
        }

}




