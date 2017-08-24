package com.baodanyun.robot.dto;

import java.io.Serializable;
import java.util.Date;

public class RobotImages implements Serializable {

    /*图片路径*/
    private String imgPath;

    /*图片上传时间*/
    private Date uploadTime;

    public RobotImages() {
    }

    public RobotImages(String imgPath, Date uploadTime) {
        this.imgPath = imgPath;
        this.uploadTime = uploadTime;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }
}
