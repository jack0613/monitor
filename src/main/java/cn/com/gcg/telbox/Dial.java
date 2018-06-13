package cn.com.gcg.telbox;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Jack on 2018/6/10.
 */
public class Dial {


    /**
     * @param: phoneNumber
     * @description: 拨打电话
     * @author: Jack
     * @data: 2018/6/10 18:51
     * @return：null
     */
    public static void dial(String phoneNumber,String playFile){

        File path = null;
        try {
            path = new File(ResourceUtils.getURL("classpath:").getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //项目路径
        String projectPath = "";
        if(path != null){
            projectPath = path.getAbsolutePath();
        }

        //添加通道和出局号
        phoneNumber = "dial 0 0," + phoneNumber;

        String[] strline = phoneNumber.split("(\\s* \\s*)|(\\s*and\\s*)");
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

                //Jack 设置播放语音 默认暂停 播放一次
                //BriSDKLib.DIAL_STATUS = BriSDKLib.QNV_PlayFile(0, BriSDKLib.QNV_PLAY_FILE_START, 0, BriSDKLib.PLAYFILE_MASK_PAUSE, projectPath + "\\static\\wav\\ipwarn.wav");

                BriSDKLib.DIAL_STATUS = BriSDKLib.QNV_PlayFile(0, BriSDKLib.QNV_PLAY_FILE_START, 0, BriSDKLib.PLAYFILE_MASK_PAUSE, projectPath + playFile);

                //通道启用 拨打电话
                BriSDKLib.QNV_SetDevCtrl(iCh,
                        BriSDKLib.QNV_CTRL_LINEOUT, 1);//
                BriSDKLib.QNV_General(iCh,
                        BriSDKLib.QNV_GENERAL_STARTDIAL, 0,
                        strline[2]);

            }
        }

    }
}
