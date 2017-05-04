package jufeng.juyancash.printer;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.Charset;

import jufeng.juyancash.DBHelper;
import jufeng.juyancash.printer.sdk.Command;
import zj.com.customize.sdk.Other;

/**
 * Created by Administrator102 on 2016/9/5.
 */
public class NetPrinter {
    public Socket socket;
    public int POS_OPEN_NETPORT = 9100;// 0X238c
    public boolean IFOpen = false;
    public int Net_SendTimeout = 5000;
    public int Net_ReceiveTimeout = 500;
    public String command = ""; //打印命令字符串
    public byte[] outbytes; //传输的命令集
    private String result;

    PrinterCMD pcmd = new PrinterCMD();

    /// <summary>
    /// 网络打印机 打开网络打印机
    /// </summary>
    /// <param name="ipaddress"></param>
    /// <returns></returns>
    public boolean Open(String ipaddress, int netport)
    {
        result = null;
        if (socket == null)
        {
            try
            {
                SocketAddress ipe = new InetSocketAddress(ipaddress,netport);
                socket = new Socket();  //Socket(ipaddress, netport,true);
                socket.connect(ipe);
                socket.setSoTimeout(Net_ReceiveTimeout);

                IFOpen = true;
            }
            catch(Exception e)
            {
                IFOpen = false;
            }
        }
        else
        {
            try
            {
                socket.close();
                SocketAddress ipe = new InetSocketAddress(ipaddress,netport);
                socket = new Socket();  //Socket(ipaddress, netport,true);
                socket.connect(ipe);
                socket.setSoTimeout(Net_ReceiveTimeout);
                IFOpen = true;
            }
            catch(Exception e)
            {
//                e.printStackTrace();
                IFOpen = false;
            }
        }
        return IFOpen;
    }

    /// <summary>
    /// 网络打印机 关闭
    /// </summary>
    /// <param name="ipaddress"></param>
    /// <returns></returns>
    public void Close()
    {
        try
        {
            IFOpen = false;
            if(socket != null)
                socket.close();
            socket = null;

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public boolean getIfOpen(){
        return IFOpen;
    }

    /// <summary>
    /// 网络打印机 初始化打印机
    /// </summary>
    public void Set()
    {
        try
        {
            command = pcmd.CMD_SetPos();
            OutputStream stream = socket.getOutputStream();
            outbytes =  command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /// <summary>
    /// 网络打印机 打印的文本
    /// </summary>
    /// <param name="pszString"></param>
    /// <param name="nFontAlign">0:居左 1:居中 2:居右</param>
    /// <param name="nfontsize">字体大小0:正常大小 1:两倍高 2:两倍宽 3:两倍大小 4:三倍高 5:三倍宽 6:三倍大小 7:四倍高 8:四倍宽 9:四倍大小 10:五倍高 11:五倍宽 12:五倍大小</param>
    /// <param name="ifzhenda">0:非针打  1:针打</param>
    public void PrintText(String pszString, int nFontAlign, int nFontSize,int ifzhenda)
    {
        try
        {
            OutputStream stream = socket.getOutputStream();
            command = pcmd.CMD_TextAlign(nFontAlign);
            outbytes =  command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);

            if (ifzhenda == 1)
            {
                command = pcmd.CMD_FontSize_BTP_M280(nFontSize);
                outbytes =  command.getBytes(Charset.forName("ASCII"));
                stream.write(outbytes);

                command = pcmd.CMD_FontSize_BTP_M2801(nFontSize);
                outbytes =  command.getBytes(Charset.forName("ASCII"));
                stream.write(outbytes);
            }
            else
            {
                command = pcmd.CMD_FontSize(nFontSize);
                outbytes =  command.getBytes(Charset.forName("ASCII"));
                stream.write(outbytes);
            }

            command = pszString;// +CMD_Enter();
            outbytes =  command.getBytes(Charset.forName("GB2312")); //Charset.defaultCharset()); //forName("UTF-8")
            stream.write(outbytes);

        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }

    }

    /// <summary>
    /// 网络打印机 回车
    /// </summary>
    public void PrintEnter()
    {
        try
        {
            command = pcmd.CMD_Enter();
            OutputStream stream = socket.getOutputStream();
            outbytes =  command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /// <summary>
    /// 网络打印机 切割
    /// </summary>
    /// <param name="pagenum">切割时，走纸行数</param>
    public void CutPage(int pagenum)
    {
        try
        {
            OutputStream stream = socket.getOutputStream();

            command = pcmd.CMD_PageGO(pagenum) + pcmd.CMD_Enter();
            outbytes =  command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);

            command = pcmd.CMD_CutPage() + pcmd.CMD_Enter();
            outbytes =  command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);

            try {
                getPosStatus(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //打开蜂鸣
    public void OpenBuzzing()
    {
        try
        {
            OutputStream stream = socket.getOutputStream();
            outbytes = pcmd.ESC_B_m_n;
            stream.write(outbytes);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //打印二维码
    public void PrintQRCode()
    {
        try
        {
            OutputStream stream = socket.getOutputStream();
            outbytes = new byte[]{0x1b, 0x61, 0x00 };
            stream.write(outbytes);
            outbytes = pcmd.getBarCommand("ceshi",1,3,8);
            stream.write(outbytes);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //打印图片
    public void PrintImg(Bitmap bitmap){
        try {
            OutputStream stream = socket.getOutputStream();
            stream.write(0x1B);
            stream.write(97);
            stream.write(1);
            outbytes = new byte[] {0x1b, 0x40 };
            int nMode = 0;
            int nPaperWidth = 200;
            if(bitmap != null)
            {
                byte[] data = POS_PrintBMP(bitmap, nPaperWidth, nMode);
                stream.write(outbytes);
                stream.write(data);
                stream.write(new byte[]{0x0a});
            }
        }catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static byte[] POS_PrintBMP(Bitmap mBitmap, int nWidth, int nMode) {
        // 先转黑白，再调用函数缩放位图
        int width = ((nWidth + 7) / 8) * 8;
        int height = mBitmap.getHeight() * width / mBitmap.getWidth();
        height = ((height + 7) / 8) * 8;

        Bitmap rszBitmap = mBitmap;
        if (mBitmap.getWidth() != width){
            rszBitmap = Other.resizeImage(mBitmap, width, height);
        }

        Bitmap grayBitmap = Other.toGrayscale(rszBitmap);

        byte[] dithered = Other.thresholdToBWPic(grayBitmap);

        byte[] data = Other.eachLinePixToCmd(dithered, width, nMode);

        return data;
    }

    /// <summary>
    /// 网络打印机换行
    /// </summary>
    /// <param name="lines"></param>
    public void PrintNewLines(int lines)
    {
        try
        {
            OutputStream stream = socket.getOutputStream();

            command = pcmd.CMD_FontSize(0);
            outbytes =  command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);
            for (int i = 0; i < lines; i++)
            {
                command = "" + pcmd.CMD_Enter();
                outbytes =  command.getBytes(Charset.forName("ASCII"));
                stream.write(outbytes);
            }
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /// <summary>
    /// 打开钱箱
    /// </summary>
    public void OpenQianXiang()
    {
        try
        {
            OutputStream stream = socket.getOutputStream();
            command = "" + pcmd.CMC_QianXiang();
            outbytes =  command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 设置下划线
     */
    public void SetHuaxian(int line)
    {
        try
        {
            OutputStream stream = socket.getOutputStream();
            outbytes = pcmd.POS_Set_UnderLine(line);
            stream.write(outbytes);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /// <summary>
    /// 打印条码
    /// </summary>
    /// <param name="numstr"></param>
    /// <param name="height"></param>
    /// <param name="width"></param>
    /// <param name="numweizi">1:上方  2:下方  0:不打印数字</param>
    /// <param name="fontalign">0:居左 1:居中 2:居右</param>
    /// <param name="fontsize">字体大小0:正常大小 1:两倍高 2:两倍宽 3:两倍大小 4:三倍高 5:三倍宽 6:三倍大小 7:四倍高 8:四倍宽 9:四倍大小 10:五倍高 11:五倍宽 12:五倍大小</param>
    public void PrintTiaoMa(String numstr, int height, int width, int numweizi, int fontalign, int fontsize)
    {
        try
        {
            OutputStream stream = socket.getOutputStream();
            command = pcmd.CMD_TiaoMaHeight(height);
            outbytes =  command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);

            command = pcmd.CMD_TiaoMaWidth(width);
            outbytes =  command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);

            command = pcmd.CMD_TiaoMaWeiZi(numweizi);
            outbytes =  command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);

            command = pcmd.CMD_TextAlign(fontalign);
            outbytes =  command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);

            command = pcmd.CMD_FontSize(fontsize);
            outbytes =  command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);

            command = pcmd.CMD_TiaoMaPrint(numstr);
            outbytes =  command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 获取打印机状态
     * @return
     */
    public void getPosStatus(int num) throws  Exception{
        if(socket != null) {
            if(!IFOpen){
                throw new Exception();
            }
            OutputStream stream = socket.getOutputStream();
            command = pcmd.CMD_ReturnStatus(num);
            outbytes = command.getBytes(Charset.forName("ASCII"));
            stream.write(outbytes);
            InputStream inStream = socket.getInputStream();
            byte[] bytes = new byte[4];
            inStream.read(bytes);
        }
    }

    /**
     * 获取打印机状态
     * @return
     */
    public void getPosStatus(Context context,String orderId,String printIp, int num) throws  Exception{
        OutputStream stream = socket.getOutputStream();
        command = pcmd.CMD_ReturnStatus(num);
        outbytes =  command.getBytes(Charset.forName("ASCII"));
        stream.write(outbytes);
        InputStream inStream = socket.getInputStream();
        byte[] bytes = new byte[4];
        inStream.read(bytes);
        String result = new String(bytes);
        DBHelper.getInstance(context).insertPrintResult(orderId,printIp,"状态"+num+"："+result);
    }

    public String getResult(){
        return result;
    }
}

class PrinterCMD
{
    /**
     * 二维码打印函数
     * @param str                     打印二维码数据
     * @param nVersion				      二维码类型
     * @param nErrorCorrectionLevel   纠错级别
     * @param nMagnification          放大倍数
     * @return
     */
    public byte[] getBarCommand(String str, int nVersion, int nErrorCorrectionLevel,
                                       int nMagnification){

        if(nVersion<0 | nVersion >19 | nErrorCorrectionLevel<0 | nErrorCorrectionLevel > 3
                | nMagnification < 1 | nMagnification > 8){
            return null;
        }

        byte[] bCodeData = null;
        try
        {
            bCodeData = str.getBytes("GBK");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            return null;
        }

        byte[] command = new byte[bCodeData.length + 7];

        command[0] = 27;
        command[1] = 90;
        command[2] = ((byte)nVersion);
        command[3] = ((byte)nErrorCorrectionLevel);
        command[4] = ((byte)nMagnification);
        command[5] = (byte)(bCodeData.length & 0xff);
        command[6] = (byte)((bCodeData.length & 0xff00) >> 8);
        System.arraycopy(bCodeData, 0, command, 7, bCodeData.length);

        return command;
    }

    //蜂鸣指令
    public byte[] ESC_B_m_n = new byte[] { 0x1b, 0x42, 0x03, 0x03 };
    /// <summary>
    /// 初始化打印机
    /// </summary>
    /// <returns></returns>
    public String CMD_SetPos()
    {
        return new StringBuffer().append((char)27).append((char)64).toString();
    }

    /// <summary>
    /// 换行（回车）
    /// </summary>
    /// <returns></returns>
    public String CMD_Enter()
    {
        return new StringBuffer().append((char)10).toString();
    }

    /// <summary>
    /// 对齐模式
    /// </summary>
    /// <param name="align">0:左对齐 1:中对齐 2:右对齐</param>
    /// <returns></returns>
    public String CMD_TextAlign(int align)
    {
        return new StringBuffer().append((char)27).append((char)97).append((char)align).toString();
    }

    /// <summary>
    /// 字体的大小
    /// </summary>
    /// <param name="nfontsize">0:正常大小 1:两倍高 2:两倍宽 3:两倍大小 4:三倍高 5:三倍宽 6:三倍大小 7:四倍高 8:四倍宽 9:四倍大小 10:五倍高 11:五倍宽 12:五倍大小</param>
    /// <returns></returns>
    public String CMD_FontSize(int nfontsize)
    {
        String _cmdstr = "";

        //设置字体大小
        switch (nfontsize)
        {
            case -1:
                _cmdstr = new StringBuffer().append((char)29).append((char)33).append((char)0).toString();//29 33
                break;

            case 0:
                _cmdstr = new StringBuffer().append((char)29).append((char)33).append((char)0).toString();//29 33
                break;

            case 1:
                _cmdstr = new StringBuffer().append((char)29).append((char)33).append((char)1).toString();
                break;

            case 2:
                _cmdstr = new StringBuffer().append((char)29).append((char)33).append((char)16).toString();
                break;

            case 3:
                _cmdstr = new StringBuffer().append((char)29).append((char)33).append((char)17).toString();
                break;

            case 4:
                _cmdstr = new StringBuffer().append((char)29).append((char)33).append((char)2).toString();
                break;

            case 5:
                _cmdstr = new StringBuffer().append((char)29).append((char)33).append((char)32).toString();
                break;

            case 6:
                _cmdstr = new StringBuffer().append((char)29).append((char)33).append((char)34).toString();
                break;

            case 7:
                _cmdstr = new StringBuffer().append((char)29).append((char)33).append((char)3).toString();
                break;

            case 8:
                _cmdstr = new StringBuffer().append((char)29).append((char)33).append((char)48).toString();
                break;

            case 9:
                _cmdstr = new StringBuffer().append((char)29).append((char)33).append((char)51).toString();
                break;

            case 10:
                _cmdstr = new StringBuffer().append((char)29).append((char)33).append((char)4).toString();
                break;

            case 11:
                _cmdstr = new StringBuffer().append((char)29).append((char)33).append((char)64).toString();
                break;

            case 12:
                _cmdstr = new StringBuffer().append((char)29).append((char)33).append((char)68).toString();
                break;

        }
        return _cmdstr;
    }

    /// <summary>
    /// BTP-M280(针打) 倍宽倍高
    /// </summary>
    /// <param name="size">0:取消倍宽倍高模式  1:倍高模式 2:倍宽模式 3:两倍大小</param>
    /// <returns></returns>
    public String CMD_FontSize_BTP_M280(int size)
    {
        String _cmdstr = "";
        //只有0和1两种模式
        int fontsize = size;

        switch (fontsize)
        {
            case 1:
                _cmdstr = new StringBuffer().append((char)28).append((char)33).append((char)8).toString();
                break;
            case 2:
                _cmdstr = new StringBuffer().append((char)28).append((char)33).append((char)4).toString();
                break;
            case 3:
                _cmdstr = new StringBuffer().append((char)28).append((char)87).append((char)1).toString();
                break;
            default:
                _cmdstr = new StringBuffer().append((char)28).append((char)87).append((char)0).toString();
                break;
        }

        return _cmdstr;
    }

    /// <summary>
    /// BTP-M280(针打) 倍宽倍高
    /// </summary>
    /// <param name="size">0:取消倍宽倍高模式  1:倍高模式 2:倍宽模式 3:两倍大小</param>
    /// <returns></returns>
    public String CMD_FontSize_BTP_M2801(int size)
    {
        String _cmdstr = "";
        //只有0和1两种模式
        int fontsize = size;

        switch (fontsize)
        {
            case 1:
                _cmdstr = new StringBuffer().append((char)27).append((char)33).append((char)17).toString();
                break;
            case 2:
                _cmdstr = new StringBuffer().append((char)27).append((char)33).append((char)33).toString();
                break;
            case 3:
                _cmdstr = new StringBuffer().append((char)27).append((char)33).append((char)49).toString();
                break;
            default:
                _cmdstr = new StringBuffer().append((char)27).append((char)33).append((char)1).toString();
                break;
        }

        return _cmdstr;
    }

    /// <summary>
    /// 走纸
    /// </summary>
    /// <param name="line">走纸的行数</param>
    /// <returns></returns>
    public String CMD_PageGO(int line)
    {
        return new StringBuffer().append((char)27).append((char)100).append((char)line).toString();
    }

    /// <summary>
    /// 切割
    /// </summary>
    /// <returns></returns>
    public String CMD_CutPage()
    {
        return new StringBuffer().append((char)27).append((char)109).toString();
    }

    /// <summary>
    /// 返回状态(返回8位的二进制)
    /// </summary>
    /// <param name="num">1:打印机状态 2:脱机状态 3:错误状态 4:传送纸状态</param>
    /// 返回打印机状态如下：
    /// 第一位：固定为0
    /// 第二位：固定为1
    /// 第三位：0:一个或两个钱箱打开  1:两个钱箱都关闭
    /// 第四位：0:联机  1:脱机
    /// 第五位：固定为1
    /// 第六位：未定义
    /// 第七位：未定义
    /// 第八位：固定为0
    ///
    /// 返回脱机状态如下：
    /// 第一位：固定为0
    /// 第二位：固定为1
    /// 第三位：0:上盖关  1:上盖开
    /// 第四位：0:未按走纸键  1:按下走纸键
    /// 第五位：固定为1
    /// 第六位：0:打印机不缺纸  1: 打印机缺纸
    /// 第七位：0:没有出错情况  1:有错误情况
    /// 第八位：固定为0
    ///
    /// 返回错误状态如下：
    /// 第一位：固定为0
    /// 第二位：固定为1
    /// 第三位：未定义
    /// 第四位：0:切刀无错误  1:切刀有错误
    /// 第五位：固定为1
    /// 第六位：0:无不可恢复错误  1: 有不可恢复错误
    /// 第七位：0:打印头温度和电压正常  1:打印头温度或电压超出范围
    /// 第八位：固定为0
    ///
    /// 返回传送纸状态如下：
    /// 第一位：固定为0
    /// 第二位：固定为1
    /// 第三位：0:有纸  1:纸将尽
    /// 第四位：0:有纸  1:纸将尽
    /// 第五位：固定为1
    /// 第六位：0:有纸  1:纸尽
    /// 第七位：0:有纸  1:纸尽
    /// 第八位：固定为0
    /// <returns></returns>
    public String CMD_ReturnStatus(int num)
    {
        return new StringBuffer().append((char)16).append((char)4).append((char)num).toString();
    }

    /// <summary>
    /// 条码高宽
    /// </summary>
    /// <param name="num"></param>
    /// <returns></returns>
    public String CMD_TiaoMaHeight(int num)
    {
        //return ((char)29).append"h" + ((char)num).toString();
        return new StringBuffer().append((char)29).append((char)104).append((char)num).toString();
    }

    /// <summary>
    /// 条码宽度
    /// </summary>
    /// <param name="num"></param>
    /// <returns></returns>
    public String CMD_TiaoMaWidth(int num)
    {
        //return ((char)29).append"w" + ((char)num).toString();
        return new StringBuffer().append((char)29).append((char)119).append((char)num).toString();
    }

    /// <summary>
    /// 条码数字打印的位置
    /// </summary>
    /// <param name="num">1:上方  2:下方  0:不打印数字</param>
    /// <returns></returns>
    public String CMD_TiaoMaWeiZi(int num)
    {
        return new StringBuffer().append((char)29).append("H").append((char)num).toString();
    }

    /// <summary>
    /// 开始打印(条码类型为CODE39)
    /// </summary>
    /// <param name="numstr"></param>
    /// <returns></returns>
    public String CMD_TiaoMaPrint(String numstr)
    {
        //return ((char)29).append"k" + ((char)4).appendnumstr + ((char)0).toString();
        return new StringBuffer().append((char)29).append((char)107).append((char)4).append(numstr).append ((char)0).toString();
    }

    public String CMD_PrinterStatus(String numstr)
    {
        //return ((char)29).append"k" + ((char)4).appendnumstr + ((char)0).toString();
        return new StringBuffer().append((char)16).append((char)4).append(numstr).toString();
    }

    /// <summary>
    /// 打开钱箱
    /// </summary>
    /// <returns></returns>
    public String CMC_QianXiang()
    {
        return new StringBuffer().append((char)27).append((char)112).append((char)0).append((char)64).append((char)80).toString();
    }

    /**
     * 蜂鸣指令
     * @param m  蜂鸣次数
     * @param t  每次蜂鸣的时间
     * @return
     */
    public byte[] POS_Set_Beep(int m, int t){

        if((m<1 || m>9) | (t<1 || t>9))
            return null;

        ESC_B_m_n[2] = (byte)m;
        ESC_B_m_n[3] = (byte)t;

        byte[] data = Other.byteArraysToBytes(new byte[][]{
                ESC_B_m_n
        });
        return data;
    }

    /**
     * 设置下划线
     * @param line
     * @return
     */
    public byte[] POS_Set_UnderLine(int line){

        if((line<0 || line>2))
            return null;

        Command.ESC_Minus[2] = (byte)line;
        Command.FS_Minus[2] = (byte)line;

        byte[] data = Other.byteArraysToBytes(new byte[][]{
                Command.ESC_Minus, Command.FS_Minus
        });
        return data;
    }

}
