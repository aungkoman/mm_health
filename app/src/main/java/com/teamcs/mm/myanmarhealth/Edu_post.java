package com.teamcs.mm.myanmarhealth;

public class Edu_post
{
    private String msg;

    private edu_detail[] data;

    private String status;

    public String getMsg ()
    {
        return msg;
    }

    public void setMsg (String msg)
    {
        this.msg = msg;
    }

    public edu_detail[] getData ()
    {
        return data;
    }

    public void setData (edu_detail[] data)
    {
        this.data = data;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [msg = "+msg+", data = "+data+", status = "+status+"]";
    }
}
