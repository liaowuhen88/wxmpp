package com.baodanyun.websocket.bean.userInterface.user;


//其他未注释的 可以不做显示
public class PersonalInfo {
    private Long id;

    private Long useraccountid;

    private String pname;//个人姓名

    private String mobile;//手机号

    private String email;//邮箱

    private Byte sex;//性别

    private Long birthday;//生日

    private String workaddress;//工作地点

    private String homeaddress;//家庭住址

    private String socialsecurityarea;

    private Byte healthdegree;//健康状况

    private String career;

    private String qq;

    private Byte identitytype;//个人信息标志位 个人信息 只能取这个值为1的

    private String weixin;

    private Long ct;

    private Long ut;

    private String attr;

    private String nationality;

    private String msalary;

    private String careertype;

    private String careercode;

    private String socialtype;

    private String vip;

    private String empcode;

    private Double pamount;

    private Long pcontractid;

    private Byte marry;

    private Byte idcardtype;

    private String idcard;

    private String relationship;

    private String mobileArea;

    private String prov;

    private String city;

    private String dist;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUseraccountid() {
        return useraccountid;
    }

    public void setUseraccountid(Long useraccountid) {
        this.useraccountid = useraccountid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname == null ? null : pname.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Byte getSex() {
        return sex;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }

    public Long getBirthday() {
        return birthday;
    }

    public void setBirthday(Long birthday) {
        this.birthday = birthday;
    }

    public String getWorkaddress() {
        return workaddress;
    }

    public void setWorkaddress(String workaddress) {
        this.workaddress = workaddress == null ? null : workaddress.trim();
    }

    public String getHomeaddress() {
        return homeaddress;
    }

    public void setHomeaddress(String homeaddress) {
        this.homeaddress = homeaddress == null ? null : homeaddress.trim();
    }

    public String getSocialsecurityarea() {
        return socialsecurityarea;
    }

    public void setSocialsecurityarea(String socialsecurityarea) {
        this.socialsecurityarea = socialsecurityarea == null ? null : socialsecurityarea.trim();
    }

    public Byte getHealthdegree() {
        return healthdegree;
    }

    public void setHealthdegree(Byte healthdegree) {
        this.healthdegree = healthdegree;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career == null ? null : career.trim();
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq == null ? null : qq.trim();
    }

    public Byte getIdentitytype() {
        return identitytype;
    }

    public void setIdentitytype(Byte identitytype) {
        this.identitytype = identitytype;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin == null ? null : weixin.trim();
    }

    public Long getUt() {
        return ut;
    }

    public void setUt(Long ut) {
        this.ut = ut;
    }

    public Long getCt() {
        return ct;
    }

    public void setCt(Long ct) {
        this.ct = ct;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr == null ? null : attr.trim();
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality == null ? null : nationality.trim();
    }

    public String getMsalary() {
        return msalary;
    }

    public void setMsalary(String msalary) {
        this.msalary = msalary == null ? null : msalary.trim();
    }

    public String getCareertype() {
        return careertype;
    }

    public void setCareertype(String careertype) {
        this.careertype = careertype == null ? null : careertype.trim();
    }

    public String getCareercode() {
        return careercode;
    }

    public void setCareercode(String careercode) {
        this.careercode = careercode == null ? null : careercode.trim();
    }

    public String getSocialtype() {
        return socialtype;
    }

    public void setSocialtype(String socialtype) {
        this.socialtype = socialtype == null ? null : socialtype.trim();
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip == null ? null : vip.trim();
    }

    public String getEmpcode() {
        return empcode;
    }

    public void setEmpcode(String empcode) {
        this.empcode = empcode == null ? null : empcode.trim();
    }

    public Double getPamount() {
        return pamount;
    }

    public void setPamount(Double pamount) {
        this.pamount = pamount;
    }

    public Long getPcontractid() {
        return pcontractid;
    }

    public void setPcontractid(Long pcontractid) {
        this.pcontractid = pcontractid;
    }

    public String getMobileArea() {
        return mobileArea;
    }

    public void setMobileArea(String mobileArea) {
        this.mobileArea = mobileArea;
    }
    public Byte getIdcardtype() {
        return idcardtype;
    }

    public void setIdcardtype(Byte idcardtype) {
        this.idcardtype = idcardtype;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard == null ? null : idcard.trim();
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship == null ? null : relationship.trim();
    }
}