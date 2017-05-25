package com.baodanyun.wxmpp.test;

//import com.baodanyun.websocket.service.order.QDOrderHelper;

import com.baodanyun.websocket.util.DesUtil;
import com.baodanyun.websocket.util.PhoneUtils;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by liaowuhen on 2016/11/15.
 */
public class TestOrder {

    public static void main(String args[]) throws Exception{
        //客服全量订单列表测试
//        System.out.print(QDOrderHelper.findDataJsonByUid(35685l));
//[{"id":1675,"uid":35685,"oderCode":"E20160914160410090818024","commodityTitle":"宇信员工入职专享体检套餐","oderType":3,"commodityNumber":1,"orderPrice":85.0,"createTime":"Sep 14, 2016 4:04:10 PM","payTime":"Sep 14, 2016 4:10:18 PM","tid":"E20160914160410090818024","status":"WAIT_BUYER_CONFIRM_GOODS","discountfee":0.0,"openid":"oAH_qsqiTt875rIhImh-QgX_5gpM","numiid":"272330361","tjName":"汪明,","mobile":"18810419053,","idcard":"413026199003300329,","sex":"女,","wxNc":"Get","lishuNo":"35685","ct":"Sep 14, 2016 4:16:41 PM"}]
    }

    @Test
    public  void desc() throws NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidKeyException {
       // String content ="0wKaCW/5dqqOsOailGeX30gMo6T31G2h64R2HtIvP42xb7TViL4NkvljhYX/ypwNpSFYjc1ftjvWhOOWaAvEUngIcxKoqI27+vDe+ONIBi7+LZpKavgxcC7kx0eXGqrm647ZZnwmWKzAwJY/PisLfmhR0Ut4NrUyytdh9YyJKlADPcArz3/sQTkEjkZPCEHVMabZ933/I2AVeTAwREqBdRCx0UWLv5NlFxzipHt1/fJ7iK84WBRC87u1e3q+kXeobdTOAxXw2omT5RlecZOF0vklyB9+1tM4BitvlblDO4wGk//IY3Zh/CoPwJclE+DRfd7OF8WvVGT4VhPwtmugC3SvVEPHhi3whc4hLZx7gJmIpz9xtDhWTwBOPWLuqCt3J+p0FDcM7KKX9tThqwiE+L6tS6cvTXe0MW8jF96ftM8KHWDrLtPLJ2A0b0ph0NTW79jGOgZQo9SUK7zMpcM7H7n19LEjNPxuBpM4DMVMdauyn9vJfNIPKPkcD6DJdgh+";
        // 个人信息补充 昵称 头像等
        ///String content ="0wKaCW/5dqqOsOailGeX30gMo6T31G2h64R2HtIvP42xb7TViL4NkvljhYX/ypwNpSFYjc1ftjvWhOOWaAvEUngIcxKoqI27+vDe+ONIBi7+LZpKavgxcC7kx0eXGqrm647ZZnwmWKzAwJY/PisLfmhR0Ut4NrUyytdh9YyJKlADPcArz3/sQTkEjkZPCEHVMabZ933/I2AVeTAwREqBdRCx0UWLv5NlFxzipHt1/fJ7iK84WBRC87u1e3q+kXeobdTOAxXw2omT5RlecZOF0vklyB9+1tM4BitvlblDO4wGk//IY3Zh/CoPwJclE+DRh/dhu1QQgoZ37xzUEcJaaXSvVEPHhi3whc4hLZx7gJn5/k9x1xRjoWoikT9DEJskJ+p0FDcM7KLiG4B4TowQa0gscGJ8iGiYMW8jF96ftM8KHWDrLtPLJ2A0b0ph0NTW79jGOgZQo9Rhf1iFUZG82Ln19LEjNPxuBpM4DMVMdauyn9vJfNIPKPkcD6DJdgh+";

        // 个人信息
        String content = "hF33slqh9dYkrfPMnCfgfbNuXY/vF9C+J6QoLufTB47+Jfl4PKVSViN8+wI3Thirl6NCG8u7VggyGCBePusESZv4rCehfBkLmdQQQ9JQe5LDiyFjYSvc7ZYUtbM44J5zf/HKChMgioGr/k5elZDMIaK9bfeVwHSFo0bxOX2tEqCJX1lug9rGGiO6b12Kmh4+eK2ng7lM2LJ466vYNnEvgOpq14mf650zYQUCeLPjG4HuwsFMa9UhzoIpF4SzpUnB8LJ3TeR8VcZ6jvADFzFdnO/YxjoGUKPUgxaUEhVdUNvEm7xsOJVg63g7CRfzKuFj";


        String mm = DesUtil.decryptECB("_(&%#!_L",content);


        System.out.print(mm);
    }

    @Test
    public  void jsonToString(){

       /* Msg sendMsg = new Msg("llllll<a href=www.baidu.com &u=5555\">我i要留言</a>");

        String content = JSONObject.toJSONString(sendMsg);*/

        String url ="http://localhost:8085/api/customerlogin";
        int end = url.indexOf("/api/");
        String base = url.substring(0,end);

        System.out.print(base);
    }

    @Test
    public void isPhone() {


        System.out.print(PhoneUtils.isMobile("18649041578"));
    }

}
