package cn.com.gcg.telbox;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by Jack on 2018/6/6.
 */
public class InitializeBox {

    public static void initialize() throws IOException {

        File path = new File(ResourceUtils.getURL("classpath:").getPath());
        //项目路径
        String projectPath = "";
        if(path != null){
            projectPath = path.getAbsolutePath();
        }


        System.load( projectPath + "\\static\\dll\\qnviccub.dll");//Jack InterfaceFun是dll文件这个在初始化时应该引入

        String Str= "";
        int iRet = BriSDKLib.QNV_OpenDevice(BriSDKLib.ODT_LBRIDGE, 0, "0");//

        int iNum = BriSDKLib.QNV_DevInfo(-1, BriSDKLib.QNV_DEVINFO_GETCHANNELS);// 取得通道
        int iDev = BriSDKLib.QNV_DevInfo(-1, BriSDKLib.QNV_DEVINFO_GETCHIPS);// 取得设备数目
        if (iRet > 0)
        {
            System.out.println("设备打开成功，通道数：" + iNum  + "设备数"
                    + iDev);
            for(int i=0;i<iNum ;i++)
            {

                //本地城市区号 010,设置城市区号后如果呼叫的号码开头跟本地城市区号相同时，系统自动过滤区号
                //如：如果设置了区号010
                //startdial("01082891111"),系统自动转换成startdial("82891111");
                //startdial("9,,;01082891111"),系统自动转换成startdial("9,,82891111");//';'->为号码内部处理区号的标记
                //QNV_SetParam(i,QNV_PARAM_CITYCODE,10);//设置区号为北京:010,设置时第一个0不能写，如果第一个为0，编译器会识别成其它进制 如：vc的010->8(八进制的10=8)

                Str="通道ID="+i+" 设备ID="+ BriSDKLib.QNV_DevInfo(i,BriSDKLib.QNV_DEVINFO_GETDEVID)+
                        " 序列号="+ BriSDKLib.QNV_DevInfo(i,BriSDKLib.QNV_DEVINFO_GETSERIAL)+
                        " 设备类型="+GetDevType(BriSDKLib.QNV_DevInfo(i,BriSDKLib.QNV_DEVINFO_GETTYPE))+
                        " 芯片类型="+BriSDKLib.QNV_DevInfo(i,BriSDKLib.QNV_DEVINFO_GETCHIPTYPE)+
                        " 模块=" +GetModule(i);
                System.out.println(Str);
                BriSDKLib.QNV_SetParam(i,BriSDKLib.QNV_PARAM_MAXCHKFLASHELAPSE,0);//不检测话机拍插簧功能
                BriSDKLib.QNV_SetParam(i,BriSDKLib.QNV_PARAM_HANGUPELAPSE,200);//设置话机挂机反应速度 Nms
//				BriSDKLib.QNV_SetParam(i,BriSDKLib.QNV_PARAM_OFFHOOKELAPSE,500);//设置话机摘机反应速度 Nms//太快<1000ms  bridge系列某些设备来电反应前可能会引起摘机误测

//				BriSDKLib.QNV_SetParam(i,QNV_PARAM_AM_LINEIN,6,0);//设置线路录音音量
                //---------------------------------------------dtmf来电号码接收灵敏度
                //BriSDKLib.QNV_SetParam(i,BriSDKLib.QNV_PARAM_DTMFCALLIDVOL,50);//
                //BriSDKLib.QNV_SetParam(i,BriSDKLib.QNV_PARAM_DTMFCALLIDLEVEL,4);//
                //BriSDKLib.QNV_SetParam(i,BriSDKLib.QNV_PARAM_DTMFCALLIDNUM,8);//
                //---------------------------------------------

                //BriSDKLib.QNV_SetParam(i,BriSDKLib.QNV_PARAM_DTMFNUM,7);//设置DTMF检测持续时间7*8ms=56ms
                //BriSDKLib.QNV_SetParam(i,BriSDKLib.QNV_PARAM_DTMFLEVEL,4);//
                //BriSDKLib.QNV_SetParam(i,BriSDKLib.QNV_PARAM_DTMFVOL,70);//设置DTMF允许的最低(幅度/能量)

                //CHECKDIALTONE_FAILED=3 检测拨号音超时就提示错误，不强制拨号
                //默认检测超时就强制拨号
                //BriSDKLib.QNV_SetParam(i,QNV_PARAM_DIALTONERESULT,CHECKDIALTONE_FAILED);

//				BriSDKLib.QNV_SetParam(i,BriSDKLib.QNV_PARAM_ECHOTHRESHOLD,20);//回音抵消N倍以后自动删除
//				BriSDKLib.QNV_SetParam(i,BriSDKLib.QNV_PARAM_ECHODECVALUE,1);//回音抵消N倍以后再除以N倍

                //如果有反级，可以设置到6个以上
                if(BriSDKLib.QNV_GetDevCtrl(i,BriSDKLib.QNV_CTRL_POLARITY) > 0)
                    BriSDKLib.QNV_SetParam(i,BriSDKLib.QNV_PARAM_BUSY,8);
                else
                    BriSDKLib.QNV_SetParam(i,BriSDKLib.QNV_PARAM_BUSY,2);

                BriSDKLib.QNV_SetDevCtrl(i,BriSDKLib.QNV_CTRL_READFRAMENUM,8);//降低CPU利用率,增加延迟N*12ms
                BriSDKLib.QNV_SetParam(i,BriSDKLib.QNV_PARAM_FLASHELAPSE,300);
                //BriSDKLib.QNV_SetDevCtrl(i,BriSDKLib.QNV_CTRL_DOLINETOSPK ,1);//打开耳机开关
                //	BriSDKLib.QNV_SetDevCtrl(i,BriSDKLib.QNV_CTRL_DOMICTOLINE ,1);//打开麦克开关
                //BriSDKLib.QNV_SetParam(i,QNV_PARAM_RINGTHRESHOLD,30);
            }
            /*byte buffer[] = new byte[1024];
            String ls;
            int size = System.in.read(buffer, 0, 255);
            ls = new String(buffer, 0, size);
            ls = ls.trim().toLowerCase();

            String[] strline = ls.split("(\\s* \\s*)|(\\s*and\\s*)");
            if (strline.length != 3) {
                System.out
                        .println("命令格式错误，这里是3个参数!如dial 0 9,13017471120 其中9是出局号");
            } else {
                boolean bFlag = true;
                int iCh = Integer.parseInt(strline[1]);
                if ((BriSDKLib.QNV_DevInfo(iCh,
                        BriSDKLib.QNV_DEVINFO_GETMODULE) & BriSDKLib.DEVMODULE_CALLID) == 0) {
                    System.out.println("该通道不能接入外线,不能拨号");
                    bFlag = false;
                }

                if ((BriSDKLib.QNV_DevInfo(iCh,
                        BriSDKLib.QNV_DEVINFO_GETMODULE) & BriSDKLib.DEVMODULE_HOOK) == 0) {
                    System.out.println("该通道不支持软摘机,不能拨号,请使用支持软拨号的语音盒");
                    bFlag = false;
                }
                if (bFlag) {
                    // BriSDKLib.QNV_SetDevCtrl(iCh,BriSDKLib.QNV_CTRL_PLAYTOLINE,1);//自动打开播放语音到LINE,驱动会自动根据摘挂机状态打开关闭控制
                    //通道启用 拨打电话
                    BriSDKLib.QNV_SetDevCtrl(iCh,
                            BriSDKLib.QNV_CTRL_LINEOUT, 1);//
                    BriSDKLib.QNV_General(iCh,
                            BriSDKLib.QNV_GENERAL_STARTDIAL, 0,
                            strline[2]);

                }
            }*/


//			EventCallback evtCall = new EventCallback();
//			BriSDKLib.QNV_Event(0, BriSDKLib.QNV_EVENT_REGCBFUNC, 0, evtCall,
//					evtCall.getCallbackAddress(), null, 0);
            //处理设备事件线程
            ProcessEvtThread evtHandle= new ProcessEvtThread(iNum);
            evtHandle.start();

        }
        else {
            System.out.println("设备打开失败!");
            printHelp();
        }
    }

    private static void printHelp() {
        System.out.println("help 是帮助命令");
        System.out.println("quit 退出命令");
        System.out
                .println("dial 拨号命令，格式为 dial n(通道号) num(电话号码)如dial 0 013807837878");
        System.out
                .println("setspkmic 打开耳麦开关命令，格式为 setspkmic n(通道号) 1或0(1为打开0关闭)");

        System.out.println("hang 挂机命令，格式为 hang n(通道号))如hang 0");
        System.out.println("loc  定位电话号码命令，格式为loc num(电话号码))如loc 01082895766");

    }



    private static String GetDevType(int lDevType)
    {
        String str="";
        str ="" + lDevType;
        switch(lDevType)
        {
            case BriSDKLib.DEVTYPE_T1:str+="(cc301 T1)";break;
            case BriSDKLib.DEVTYPE_T2:str+="(cc301 T2)";break;
            case BriSDKLib.DEVTYPE_T3:str+="(cc301 T3)";break;
            case BriSDKLib.DEVTYPE_T4:str+="(cc301 T4)";break;
            case BriSDKLib.DEVTYPE_T5:str+="(cc301 T5)";break;
            case BriSDKLib.DEVTYPE_T6:str+="(cc301 T6)";break;
            case BriSDKLib.DEVTYPE_IR1:str+="(cc301 IR1)";break;
            case BriSDKLib.DEVTYPE_ID1:str+="(cc301 ID1)";break;
            case BriSDKLib.DEVTYPE_IP1:str+="(cc301 IP1)";break;
            case BriSDKLib.DEVTYPE_IA1:str+="(cc301 IA1)";break;
            case BriSDKLib.DEVTYPE_IA2:str+="(cc301 IA2)";break;
            case BriSDKLib.DEVTYPE_IA3:str+="(cc301 IA3)";break;
            case BriSDKLib.DEVTYPE_IA4:str+="(cc301 IA4)";break;
            case BriSDKLib.DEVTYPE_IB1:str+="(cc301 IB1)";break;
            case BriSDKLib.DEVTYPE_IB2:str+="(cc301 IB2)";break;
            case BriSDKLib.DEVTYPE_IB3:str+="(cc301 IB3)";break;
            case BriSDKLib.DEVTYPE_IB4:str+="(cc301 IB4)";break;
            case BriSDKLib.DEVTYPE_IP1_F:str+="(cc301 IP1_F)";break;
            case BriSDKLib.DEVTYPE_IA4_F:str+="(cc301 IA4_F)";break;
            case BriSDKLib.DEVTYPE_IC2_R:str+="(cc301 IC2_R)";break;
            case BriSDKLib.DEVTYPE_IC2_LP:str+="(cc301 IC2_LP)";break;
            case BriSDKLib.DEVTYPE_IC2_LPQ:str+="(cc301 IC2_LPQ)";break;
            case BriSDKLib.DEVTYPE_IC2_LPF:str+="(cc301 IC2_LPF)";break;
            case BriSDKLib.DEVTYPE_IC4_R:str+="(cc301 IC4_R)";break;
            case BriSDKLib.DEVTYPE_IC4_LP:str+="(cc301 IC4_LP)";break;
            case BriSDKLib.DEVTYPE_IC4_LPQ:str+="(cc301 IC4_LPQ)";break;
            case BriSDKLib.DEVTYPE_IC4_LPF:str+="(cc301 IC4_LPF)";break;
            case BriSDKLib.DEVTYPE_IC7_R:str+="(cc301 IC7_R)";break;
            case BriSDKLib.DEVTYPE_IC7_LP:str+="(cc301 IC7_LP)";break;
            case BriSDKLib.DEVTYPE_IC7_LPQ:str+="(cc301 IC7_LPQ)";break;
            case BriSDKLib.DEVTYPE_IC7_LPF:str+="(cc301 IC7_LPF)";break;
            case BriSDKLib.DEVTYPE_A1:str+="(玻瑞器 A1)";break;
            case BriSDKLib.DEVTYPE_A2:str+="(玻瑞器 A2)";break;
            case BriSDKLib.DEVTYPE_A3:str+="(玻瑞器 A3)";break;
            case BriSDKLib.DEVTYPE_A4:str+="(玻瑞器 A4)";break;
            case BriSDKLib.DEVTYPE_B1:str+="(玻瑞器 B1)";break;
            case BriSDKLib.DEVTYPE_B2:str+="(玻瑞器 B2)";break;
            case BriSDKLib.DEVTYPE_B3:str+="(玻瑞器 B3)";break;
            case BriSDKLib.DEVTYPE_B4:str+="(玻瑞器 B4)";break;
            case BriSDKLib.DEVTYPE_C4_L:str+="(玻瑞器 C4-L)";break;
            case BriSDKLib.DEVTYPE_C4_P:str+="(玻瑞器 C4-P)";break;
            case BriSDKLib.DEVTYPE_C4_LP:str+="(玻瑞器 C4-LP)";break;
            case BriSDKLib.DEVTYPE_C4_LPQ:str+="(玻瑞器 C4-LPQ)";break;
            case BriSDKLib.DEVTYPE_C7_L:str+="(玻瑞器 C7-L)";break;
            case BriSDKLib.DEVTYPE_C7_P:str+="(玻瑞器 C7-P)";break;
            case BriSDKLib.DEVTYPE_C7_LP:str+="(玻瑞器 C7-LP)";break;
            case BriSDKLib.DEVTYPE_C7_LPQ:str+="(玻瑞器 C7-LPQ)";break;
            case BriSDKLib.DEVTYPE_R1:str+="(玻瑞器 R1)";break;
            default:
            {
            }break;
        }
        return str;
    }

    private static String GetModule(int chID)
    {
        String strModule="",str="";
        int lModule=BriSDKLib.QNV_DevInfo(chID,BriSDKLib.QNV_DEVINFO_GETMODULE);
        str = "/" + lModule;
        strModule+=str;
        if( isTrue(lModule& BriSDKLib.DEVMODULE_DOPLAY)) strModule+="有喇叭/";
        if(isTrue(lModule&BriSDKLib.DEVMODULE_CALLID)) strModule+="有来电显示/";
        if(isTrue(lModule&BriSDKLib.DEVMODULE_PHONE)) strModule+="话机拨号/";
        if(isTrue(lModule&BriSDKLib.DEVMODULE_SWITCH)) strModule+="断开电话机,接收话机按键/";
        if(isTrue(lModule&BriSDKLib.DEVMODULE_PLAY2TEL)) strModule+="播放语音到电话机/";
        if(isTrue(lModule&BriSDKLib.DEVMODULE_HOOK)) strModule+="软摘机/";
        if(isTrue(lModule&BriSDKLib.DEVMODULE_MICSPK)) strModule+="有耳机/MIC/";
        if(isTrue(lModule&BriSDKLib.DEVMODULE_RING)) strModule+="模拟话机震铃/";
        if(isTrue(lModule&BriSDKLib.DEVMODULE_STORAGE)) strModule+="FLASH数据存储/";
        if(isTrue(lModule&BriSDKLib.DEVMODULE_FAX)) strModule+="收发传真/";
        if(isTrue(lModule&BriSDKLib.DEVMODULE_POLARITY)) strModule+="反级检测/";
        return strModule;
    }

    public static boolean isTrue(int nValue)
    {
        boolean bRet = false;
        bRet = (nValue !=0)?true:false;
        return bRet;
    }
}
