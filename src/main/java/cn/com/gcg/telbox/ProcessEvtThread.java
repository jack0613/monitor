/**
 *
 */
package cn.com.gcg.telbox;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Random;


/**
 * @author fds
 *
 */
public class ProcessEvtThread extends Thread {

	private int m_iMaxChannels = 0;
	private int m_iCurrentChannel = -1;// -1表示所有通道
	private int [] m_iRecFileHandle = null;//录音文件句柄

	/**
	 *
	 */
	public ProcessEvtThread(int iMaxChannels) {
		// TODO Auto-generated constructor stub
		m_iMaxChannels = iMaxChannels;
		if(m_iRecFileHandle == null)
		{
			m_iRecFileHandle = new int[m_iMaxChannels];
			for(int i = 0; i < m_iMaxChannels; i++)
			{
				m_iRecFileHandle[i] = -1;
			}
		}
	}

	public void AssignChannel(int iCh) {
		m_iCurrentChannel = iCh;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				if (m_iCurrentChannel != -1) {
					RecEvt(m_iCurrentChannel);
				} else {
					for (int i = 0; i < m_iMaxChannels; i++) {
						RecEvt(i);
					}
				}
				Thread.sleep(200);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private void RecEvt(int iCh) {
		// 使用非删除模式,和自动删除模式2个选择一种方式就可以,只有一个地方需要获取事件时，建议使用自动删除模式
		int lEventType = BriSDKLib.QNV_Event(iCh, BriSDKLib.QNV_EVENT_TYPE, 0,
				null, null, 0);
		int lEventHandle = -1;
		int lParam = 0;
		int lResult = -1;
		if (lEventType > 0) {
			lEventHandle = BriSDKLib.QNV_Event(iCh, BriSDKLib.QNV_EVENT_HANDLE,
					0, null, null, 0);
			lParam = BriSDKLib.QNV_Event(iCh, BriSDKLib.QNV_EVENT_PARAM, 0,
					null, null, 0);
			lResult = BriSDKLib.QNV_Event(iCh, BriSDKLib.QNV_EVENT_RESULT, 0,
					null, null, 0);
			StringBuffer szDataBuffer = new StringBuffer(1024);
			BriSDKLib.QNV_Event(iCh, BriSDKLib.QNV_EVENT_DATA, 0, null,
					szDataBuffer, 1024);
			BriSDKLib.QNV_Event(iCh, BriSDKLib.QNV_EVENT_REMOVE, 0, null, null,
					0);// 删除
			ProcessEvent(iCh, lEventType, lEventHandle, lResult, lParam,
					szDataBuffer.toString());
		}
	}

	public int ProcessEvent(int iCh, int lType, int lHandle, int lResult,
							int lParam, String szData) {
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
		// String szDataEx = "";
		try {

			// szDataEx = _pointer1.getAsString();
			String str = "", strValue = "";
			strValue = "Handle=" + lHandle + "Result=" + lResult + "Data="
					+ szData;
			switch (lType) {
				case BriSDKLib.BriEvent_PhoneHook: {// 电话接通后根据对方阻抗大小，声音会变大变小,200就太大，中间幅度200就太大,一般电话机100可以
				/*
				 * QNV_SetParam(pEvent->uChannelID,QNV_PARAM_DTMFVOL,50);
				 * QNV_SetParam(pEvent->uChannelID,QNV_PARAM_DTMFLEVEL,4);
				 * QNV_SetParam(pEvent->uChannelID,QNV_PARAM_DTMFNUM,9);
				 * QNV_SetParam(pEvent->uChannelID,QNV_PARAM_DTMFLOWINHIGH,20);
				 * QNV_SetParam(pEvent->uChannelID,QNV_PARAM_DTMFHIGHINLOW,20);
				 */
					// str.Format("通道%d: 电话机摘机,演示修改检测DTMF灵敏度,DTMFVOL=50/DTMFLEVEL=5/DTMFNUM=10,如果检测不到电话机拨号就修改该值更小",m_nChannelID+1);


					if (BriSDKLib.QNV_General(iCh, BriSDKLib.QNV_GENERAL_ISDIALING,
							0, null) <= 0) {
						// QNV_SetDevCtrl(m_nChannelID,QNV_CTRL_DOHOOK,0);//没有正在拨号可以考虑自动软挂机,避免3方通话状态，话机里有背景音出现
					}
					String recFile = "";
					if(this.m_iRecFileHandle[iCh] == -1)
					{
						Date curdate = new Date();
						Random r = new Random();
						r.setSeed(curdate.getTime());
						recFile = "d:\\"+curdate.getTime()+"-"+ iCh  +".wav";
						this.m_iRecFileHandle[iCh] = BriSDKLib.QNV_RecordFile(iCh, BriSDKLib.QNV_RECORD_FILE_START, BriSDKLib.BRI_WAV_FORMAT_PCM8K8B,
								BriSDKLib.RECORD_MASK_ECHO,recFile);
						int lVolume=100;//设置音量,默认为100,200表示放大一倍,0表示静音,建议该设备不要跟自动增益控制一起使用
						if(this.m_iRecFileHandle[iCh] > 0)
							BriSDKLib.QNV_RecordFile(iCh,BriSDKLib.QNV_RECORD_FILE_SETVOLUME,this.m_iRecFileHandle[iCh],lVolume,null);
					}
					else
					{
						BriSDKLib.QNV_RecordFile(iCh, BriSDKLib.QNV_RECORD_FILE_STOP, this.m_iRecFileHandle[iCh],0,null);
						this.m_iRecFileHandle[iCh] = -1;
					}
					if(recFile.length() > 0)
					{
						if(this.m_iRecFileHandle[iCh] > 0)
							str = "通道" + iCh + ": 电话机摘机录音  " + recFile +"文件句柄是" + this.m_iRecFileHandle[iCh];
						else
							str = "通道" + iCh + ": 电话机摘机录音 失败 "+ this.m_iRecFileHandle[iCh] + "可能权限不够或空间不足，请检查文件路径" + recFile;
					}
					else
						str = "通道" + iCh + ": 电话机摘机并停止以前的录音  " + recFile;
				}
				break;
				case BriSDKLib.BriEvent_PhoneHang: {
					// QNV_SetParam(pEvent->uChannelID,QNV_PARAM_DTMFVOL,5);
					// QNV_SetParam(pEvent->uChannelID,QNV_PARAM_DTMFLEVEL,3);
					// QNV_SetParam(pEvent->uChannelID,QNV_PARAM_DTMFNUM,6);
					// str.Format("通道%d: 电话机挂机,演示修改检测DTMF灵敏度",m_nChannelID+1);
					str = "通道" + iCh + ": 电话机挂机  ";
					if(this.m_iRecFileHandle[iCh] != -1)
					{
						BriSDKLib.QNV_RecordFile(iCh, BriSDKLib.QNV_RECORD_FILE_STOP, this.m_iRecFileHandle[iCh],0,null);
						str +="并停止录音";
						this.m_iRecFileHandle[iCh] = -1;
					}
				}
				break;
				case BriSDKLib.BriEvent_CallIn:
					str = "通道" + iCh + ": 来电响铃 " + strValue;
					break;
				case BriSDKLib.BriEvent_GetCallID: {
					long lSerial = BriSDKLib.QNV_DevInfo(iCh,
							BriSDKLib.QNV_DEVINFO_GETSERIAL);
					str = "通道" + iCh + ": 接收到来电号码 " + szData;
				}
				break;
				case BriSDKLib.BriEvent_StopCallIn:
					str = "通道" + iCh + ": 停止呼入,产生一个未接电话 " + szData;
					break;
				case BriSDKLib.BriEvent_DialEnd: {
					if (BriSDKLib.QNV_GetDevCtrl(iCh, BriSDKLib.QNV_CTRL_PHONE) > 0) {
						// QNV_SetDevCtrl(m_nChannelID,QNV_CTRL_DOHOOK,0);//电话机已经拿着可以考虑自动软挂机,避免3方通话状态，话机里有背景音出现
					}
					str = "通道:" + iCh + " 拨号结束 " + strValue;
				}
				break;
				case BriSDKLib.BriEvent_PlayFileEnd:
					str = "通道" + iCh + ": 播放文件结束 " + strValue;
					break;
				case BriSDKLib.BriEvent_PlayMultiFileEnd:
					str = "通道" + iCh + ": 多文件连播结束 %s" + strValue;
					break;
				case BriSDKLib.BriEvent_RepeatPlayFile:
					str = "通道" + iCh + ": 循环播放文件 %s" + strValue;
					break;
				case BriSDKLib.BriEvent_PlayStringEnd:
					str = "通道" + iCh + ": 播放字符结束 %s" + strValue;
					break;
				case BriSDKLib.BriEvent_SendCallIDEnd:
					str = "通道" + iCh + ": 给话机震铃时发送号码结束 " + strValue;
					break;
				case BriSDKLib.BriEvent_Silence:
					str = "通道" + iCh + ": 通话中一定时间的静音 " + strValue;
					break;
				case BriSDKLib.BriEvent_GetDTMFChar:
					str = "通道" + iCh + ": 接收到按键  " + strValue;
					break;
				case BriSDKLib.BriEvent_RemoteHook: {
					if (BriSDKLib.HOOK_POLARITY == lResult) {
						str = "通道" + iCh + ": 远程摘机(反级检测) " + strValue;
					} else {
						str = "通道 " + iCh + ":信号音检测远程摘机信号,仅做参考 " + strValue;
						//Jack 设置播放语音 重复播放 进程无法停止
						//int i32PlayFileHandle = BriSDKLib.QNV_PlayFile(0, BriSDKLib.QNV_PLAY_FILE_START, 0, BriSDKLib.PLAYFILE_MASK_REPEAT, projectPath + "\\static\\wav\\warn.wav");

						//Jack 设置播放语音 默认暂停 播放一次
						//int i32PlayFileHandle = BriSDKLib.QNV_PlayFile(0, BriSDKLib.QNV_PLAY_FILE_START, 0, BriSDKLib.PLAYFILE_MASK_PAUSE, projectPath + "\\static\\wav\\ipwarn.wav");

						//Jack 设置恢复播放语音
						int status = BriSDKLib.QNV_PlayFile(0,BriSDKLib.QNV_PLAY_FILE_RESUME,BriSDKLib.DIAL_STATUS,0,"");

						if (status < 0) {
							System.out.println("Jack-----:拨号后语音播放失败");
						} else {
							System.out.println("Jack-----:拨号后语音播放成功");
						}
						//播音后初始化电话拨号状态
						BriSDKLib.DIAL_STATUS = 0;

						//设置接通状态
						BriSDKLib.CONNECT_STATUS = 999;

					}

				}
				break;
				case BriSDKLib.BriEvent_RemoteHang: {
					if (BriSDKLib.HOOK_POLARITY == lResult) {
						str = "通道" + iCh + ": 远程挂机(反级检测) " + strValue;
					} else {
						str = "通道" + iCh + ": 信号音检测远程挂机(忙音检测),仅做参考 " + strValue;
					}
					BriSDKLib.QNV_General(iCh,
							BriSDKLib.QNV_GENERAL_STOPDIAL, 0, null);
					BriSDKLib.QNV_SetDevCtrl(iCh,
							BriSDKLib.QNV_CTRL_DOHOOK, 0);
					if(this.m_iRecFileHandle[iCh] != -1)
					{
						BriSDKLib.QNV_RecordFile(iCh, BriSDKLib.QNV_RECORD_FILE_STOP, this.m_iRecFileHandle[iCh],0,null);
						str +=" 并停止录音";
						this.m_iRecFileHandle[iCh] = -1;
					}
				}
				break;
				case BriSDKLib.BriEvent_Busy:
					str = "通道" + iCh + ": 接收到忙音,线路可能已经断开 " + strValue;
					if(this.m_iRecFileHandle[iCh] != -1)
					{
						BriSDKLib.QNV_RecordFile(iCh, BriSDKLib.QNV_RECORD_FILE_STOP, this.m_iRecFileHandle[iCh],0,null);
						str +=" 停止录音";
						this.m_iRecFileHandle[iCh] = -1;
					}
					break;
				case BriSDKLib.BriEvent_DialTone:
					str = "通道" + iCh + ": 检测到拨号音 " + strValue;
					break;
				case BriSDKLib.BriEvent_DialToneEx:
					str = "通道" + iCh + ": 接通状态下检测到拨号音信号,如果是刚来电,可能是刚才的来电已经未接了,仅做参考 "
							+ strValue;
					break;
				case BriSDKLib.BriEvent_PhoneDial:
					str = "通道" + iCh + ": 电话机拨号 " + strValue;
					break;
				case BriSDKLib.BriEvent_RingBack:
					str = "通道" + iCh + ": 拨号后接收到回铃音 " + strValue;
					break;
				case BriSDKLib.BriEvent_MicIn:
					str = "通道" + iCh + ": 麦克风插入 " + strValue;
					break;
				case BriSDKLib.BriEvent_MicOut:
					str = "通道" + iCh + ": 麦克风拔出 " + strValue;
					break;
				case BriSDKLib.BriEvent_FlashEnd:
					str = "通道" + iCh + ": 拍插簧完成 " + strValue;
					break;
				case BriSDKLib.BriEvent_RemoteSendFax:
					str = "通道" + iCh + ": 对方准备发送传真  " + strValue;
					break;
				case BriSDKLib.BriEvent_FaxRecvFinished:
					str = "通道" + iCh + ": 接收传真完成  " + strValue;
					break;
				case BriSDKLib.BriEvent_FaxRecvFailed:
					str = "通道" + iCh + ": 接收传真失败  " + strValue;
					break;
				case BriSDKLib.BriEvent_FaxSendFinished:
					str = "通道" + iCh + ": 发送传真完成  " + strValue;
					break;
				case BriSDKLib.BriEvent_FaxSendFailed:
					str = "通道" + iCh + ": 发送传真失败  " + strValue;
					break;
				case BriSDKLib.BriEvent_RefuseEnd:
					str = "通道" + iCh + ": 拒接来电完成  " + strValue;
					break;
				case BriSDKLib.BriEvent_RecvedFSK: {
					if (lResult == BriSDKLib.CALLIDMODE_FSK)
						str = "通道" + iCh + ": 接收到来电号码信息FSK数据  " + strValue;
					else
						str = "通道" + iCh + ": 接收到来电号码信息DTMF数据  " + strValue;
				}
				break;
				case BriSDKLib.BriEvent_PSTNFree: {
					str = "通道" + iCh + ":  PSTN线路已空闲  " + strValue;
					// WriteCallLog(m_nChannelID);
				}
				break;
				case BriSDKLib.BriEvent_CheckLine: {
					if ((lResult & BriSDKLib.CHECKLINE_MASK_DIALOUT) > 0) {
						str = "通道" + iCh
								+ ":[ok]***线路拨号音正常,能正常软拨号***-----------------";
					} else {
						str = "通道" + iCh + "[err]线路拨号音不正常,可能不能正常软拨号，检查LINE口线路";
					}
					if ((lResult & BriSDKLib.CHECKLINE_MASK_REV) > 0) {
						str = "通道"
								+ iCh
								+ "[ok]***线路LINE口/PHONE口未接反***----------------------";
					} else {
						str = "通道" + iCh + "[err]线路LINE口/PHONE口可能接反了";
					}
				}
				break;
				case BriSDKLib.BriEvent_DevErr: {
					str = "通道" + iCh + ": 设备发生错误  " + strValue;
					if (lResult == 3)// || (atol(pEvent->szData)&0xFF) ==
					// 6)//检测到移除获取多个失败
					{
					/*
					 * QNV_CloseDevice(ODT_CHANNEL,m_nChannelID); long
					 * lChNum=QNV_DevInfo(0,QNV_DEVINFO_GETCHANNELS);
					 * str.Format("当前通道数量:%d,考虑重新初始化所有设备",lChNum<0?0:lChNum);
					 */

					}
					BriSDKLib.QNV_RecordFile(iCh, BriSDKLib.QNV_RECORD_FILE_STOPALL, 0,0,null);
					for(int i = 0; i < m_iMaxChannels; i++)
					{
						m_iRecFileHandle[i] = -1;
					}
					str +="并停止所有录音";
				}
				break;
				case BriSDKLib.BriEvent_PlugOut: {
					str = "通道" + iCh + ": 设备被拔掉  ";
				}
				break;
				case BriSDKLib.BriEvent_EnableHook: {
					str = "通道" + iCh + ": HOOK被控制 lResult= " + lResult;
				}
				break;
				case BriSDKLib.BriEvent_EnablePlay: {
					str = "通道" + iCh + ": 喇叭被控制 lResult= " + lResult;
				}
				break;
				case BriSDKLib.BriEvent_EnablePlayMux: {
					str = "通道" + iCh + ": 喇叭mux修改 lResult= " + lResult;
				}
				break;
				case BriSDKLib.BriEvent_DoStartDial: {
					if (lResult == BriSDKLib.CHECKDIALTONE_FAILED) {
						str = "通道" + iCh + ": 自动拨号失败，未检测到拨号音,请检查线路";
					} else {
						str = "通道" + iCh + ": 开始拨号 data=" + szData;
					}
				}
				break;
				case BriSDKLib.BriEvent_DevCtrl: {
					if (lResult == BriSDKLib.QNV_CTRL_PLAYTOLINE) {
						if (Integer.parseInt(szData) > 0) {
							str = "播放到线路状态  打开";
						} else {
							str = "播放到线路状态 关闭";
						}
					}
				}
				break;
				default: {
					strValue = "通道:" + iCh + "其它忽略事件 eventid= " + lType + "Result="
							+ lResult + "Data=" + szData;
				}
				break;
			}

			if (lType == BriSDKLib.BriEvent_RemoteSendFax && lResult == 1) {
				// BFU_FaxTooltip(m_nChannelID,"",TTIP_AUTORECV);
				/*
				 * if(MessageBox("对方准备发送传真，是否接收?","传真提示",MB_YESNO|MB_ICONWARNING)
				 * == IDYES) { BFU_StartRecvFax(m_nChannelID,"",0); }
				 */
			}

			System.out.println(str);

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return 1;
	}

}
