package com.teamcs.mm.myanmarhealth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class edu_detail {
    @SerializedName("db_id")
    @Expose
    private String db_id;

    @SerializedName("post_id")
    @Expose
    private String post_id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("content")
    @Expose
    private String content;

    public String getDb_id ()
    {
        return db_id;
    }

    public void setDb_id (String db_id)
    {
        this.db_id = db_id;
    }

    public String getPost_id ()
    {
        return post_id;
    }

    public void setPost_id (String post_id)
    {
        this.post_id = post_id;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getContent ()
    {
        return content;
    }

    public void setContent (String content)
    {
        this.content = content;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [db_id = "+db_id+", post_id = "+post_id+", title = "+title+", content = "+content+"]";
    }

}
