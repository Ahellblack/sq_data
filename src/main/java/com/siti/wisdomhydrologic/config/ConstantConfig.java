package com.siti.wisdomhydrologic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by DC on 2019/6/12.
 *
 * @data ${DATA}-14:58
 */
@Configuration
public class ConstantConfig extends WebMvcConfigurerAdapter {
    //水位71 72  73 75 81 83 84 85 86 89
    public static final int WS=83;
    //潮位
    public static final int TS=81;
    //雨量
    public static final int RS=84;
    //风速
    public static final int WSS=85;
    //风向
    public static final int WDS=86;
    //气温
    public static final int WAT=75;
    //气压
    public static final int WAP=73;
    //流速X
    public static final int WFV=71;
    //流速Y
    public static final int WFVY=72;
    //电压
    public static final int ES=89;

    public static final String BASEPACKAGE="com";

    public static final String SWAGGER_TITLE="swagger测试接口";

    public static final String DESCRIPTION="智慧水情运维系统";

    public static final String SWAGGER_VERSION="1.0";

    public static final String SWAGGER_URL="http://localhost:8099/wisdomhydrologic/swagger-ui.html";

    public static final String ACCOUNT_SID = "8a216da855826478015599e3f66e1411";

    public static final String AUTH_TOKEN = "71a6619327734d81957e60f2eeaa2626";

    public static final String APPID = "8a216da86c8a1a54016c8dc74f1c0182";

    public static final String SERVERIP = "app.cloopen.com";

    public static final String SERVERPORT = "8883";

    public static final String TEMPLATEID = "462488";

    public static final boolean SMSFLAG=false;
    /*

    */
/**
     * PC版本号
     *//*

    @Value("${version.pc}")
    private String pcversion;
    */
/**
     * 小版本号
     *//*

    @Value("${version.build}")
    private String build;


    public String getPcversion() {
        return pcversion;
    }

    public void setPcversion(String pcversion) {
        this.pcversion = pcversion;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }
*/
}
