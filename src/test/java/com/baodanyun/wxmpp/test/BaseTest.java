
package com.baodanyun.wxmpp.test;

import com.baodanyun.websocket.springConfig.DispatcherServletInitializer;
import com.baodanyun.websocket.springConfig.SpringConfig;
import com.baodanyun.websocket.springConfig.WebXmlConfig;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Created by on 2016/7/19.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        DispatcherServletInitializer.class, SpringConfig.class
})
public class BaseTest {
}

