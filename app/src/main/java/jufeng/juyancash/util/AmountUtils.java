package jufeng.juyancash.util;

import java.math.BigDecimal;

public class AmountUtils {
    /**金额为分的格式 */
    public static final String CURRENCY_FEN_REGEX = "\\-?[0-9]+";

    /**
     * 将分为单位的转换为元并返回金额格式的字符串 （除100）
     *
     * @param amount
     * @return
     * @throws Exception
     */
    public static String changeF2Y(Long amount) throws Exception{
        if(!amount.toString().matches(CURRENCY_FEN_REGEX)) {
            throw new Exception("金额格式有误");
        }

        int flag = 0;
        String amString = amount.toString();
        if(amString.charAt(0)=='-'){
            flag = 1;
            amString = amString.substring(1);
        }
        StringBuffer result = new StringBuffer();
        if(amString.length()==1){
            result.append("0.0").append(amString);
        }else if(amString.length() == 2){
            result.append("0.").append(amString);
        }else{
            String intString = amString.substring(0,amString.length()-2);
            for(int i=1; i<=intString.length();i++){
                if( (i-1)%3 == 0 && i !=1){
                    result.append("");
                }
                result.append(intString.substring(intString.length()-i,intString.length()-i+1));
            }
            result.reverse().append(".").append(amString.substring(amString.length()-2));
        }
        if(flag == 1){
            return "-"+result.toString();
        }else{
            return result.toString();
        }
    }

    /**
     * 将分为单位的转换为元并返回金额格式的字符串 （除100）
     *
     * @param amount
     * @return
     * @throws Exception
     */
    public static String changeF2Y(Integer amount) throws RuntimeException{
        if(!amount.toString().matches(CURRENCY_FEN_REGEX)) {
            throw new RuntimeException("金额格式有误");
        }

        int flag = 0;
        String amString = amount.toString();
        if(amString.charAt(0)=='-'){
            flag = 1;
            amString = amString.substring(1);
        }
        StringBuffer result = new StringBuffer();
        if(amString.length()==1){
            result.append("0.0").append(amString);
        }else if(amString.length() == 2){
            result.append("0.").append(amString);
        }else{
            String intString = amString.substring(0,amString.length()-2);
            for(int i=1; i<=intString.length();i++){
                if( (i-1)%3 == 0 && i !=1){
                    result.append("");
                }
                result.append(intString.substring(intString.length()-i,intString.length()-i+1));
            }
            result.reverse().append(".").append(amString.substring(amString.length()-2));
        }
        if(flag == 1){
            return "-"+result.toString();
        }else{
            return result.toString();
        }
    }

    /**
     * 将分为单位的转换为元 （除100）
     *
     * @param amount
     * @return
     * @throws Exception
     */
    public static BigDecimal changeF2Y(String amount) throws Exception{
        if(!amount.matches(CURRENCY_FEN_REGEX)) {
            throw new Exception("金额格式有误");
        }
        return BigDecimal.valueOf(Long.valueOf(amount)).divide(new BigDecimal(100));
    }

    /**
     * 将元为单位的转换为分 （乘100）
     *
     * @param amount
     * @return
     */
    public static String changeY2F(Long amount){
        return BigDecimal.valueOf(amount).multiply(new BigDecimal(100)).toString();
    }

    /**
     * 将元为单位的转换为分 （乘100）
     *
     * @param amount
     * @return
     */
    public static Integer changeY2F(Double amount){
        return BigDecimal.valueOf(amount).multiply(new BigDecimal(100)).intValue();
    }

    /**
     * 将元为单位的转换为分 （乘100）
     *
     * @param amount
     * @return
     */
    public static Integer changeY2F(Integer amount){
        return BigDecimal.valueOf(amount).multiply(new BigDecimal(100)).intValue();
    }


    /**
     * 将元为单位的转换为分 替换小数点，支持以逗号区分的金额
     *
     * @param amount
     * @return
     */
    public static String changeY2F(String amount){
        String currency =  amount.replaceAll("\\$|\\￥|\\,", "");  //处理包含, ￥ 或者$的金额
        int index = currency.indexOf(".");
        int length = currency.length();
        Long amLong = 0l;
        if(index == -1){
            amLong = Long.valueOf(currency+"00");
        }else if(length - index >= 3){
            amLong = Long.valueOf((currency.substring(0, index+3)).replace(".", ""));
        }else if(length - index == 2){
            amLong = Long.valueOf((currency.substring(0, index+2)).replace(".", "")+0);
        }else{
            amLong = Long.valueOf((currency.substring(0, index+1)).replace(".", "")+"00");
        }
        return amLong.toString();
    }

   /* public static double changeF2YI(double d) throws Exception{
    	return Integer.parseInt(changeY2F(d));
    }*/


    public static void main(String[] args) {
        try {
            System.out.println("结果："+changeF2Y("120"));
        } catch(Exception e){
            System.out.println("----------->>>"+e.getMessage());
//          return e.getErrorCode();
        }
        System.out.println(AmountUtils.changeY2F("1.322"));
        System.out.println(Long.parseLong(AmountUtils.changeY2F("1000000000000000")));
    }

    //默认除法运算精度

    private static final int DEFAULT_DIV_SCALE = 10;



    /**

     * 提供精确的加法运算。

     * @param v1

     * @param v2

     * @return 两个参数的和

     */

    public static double add(double v1, double v2)

    {

        BigDecimal b1 = new BigDecimal(Double.toString(v1));

        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        double result = b1.add(b2).doubleValue();
        return divide(result,1,2,BigDecimal.ROUND_DOWN);

    }

    /**

     * 提供精确的加法运算

     * @param v1

     * @param v2

     * @return 两个参数数学加和，以字符串格式返回

     */

    public static String add(String v1, String v2)

    {

        BigDecimal b1 = new BigDecimal(v1);

        BigDecimal b2 = new BigDecimal(v2);

        String result = b1.add(b2).toString();

        return divide(result,1,2,BigDecimal.ROUND_DOWN);

    }

    /**
     * 将两个字符串相加返回double类型
     * @param v1
     * @param v2
     * @return
     */
    public static double add1(String v1, String v2)

    {

        BigDecimal b1 = new BigDecimal(v1);

        BigDecimal b2 = new BigDecimal(v2);

        double result = b1.add(b2).doubleValue();

        return divide(result,1,2,BigDecimal.ROUND_DOWN);

    }



    /**

     * 提供精确的减法运算。

     * @param v1

     * @param v2

     * @return 两个参数的差

     */

    public static double subtract(double v1, double v2)
    {

        BigDecimal b1 = new BigDecimal(Double.toString(v1));

        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        double result = b1.subtract(b2).doubleValue();
        return divide(result,1,2,BigDecimal.ROUND_DOWN);

    }



    /**

     * 提供精确的减法运算

     * @param v1

     * @param v2

     * @return 两个参数数学差，以字符串格式返回

     */

    public static String subtract(String v1, String v2)

    {

        BigDecimal b1 = new BigDecimal(v1);

        BigDecimal b2 = new BigDecimal(v2);

        return b1.subtract(b2).toString();

    }





    /**

     * 提供精确的乘法运算。

     * @param v1

     * @param v2

     * @return 两个参数的积

     */

    public static double multiply(double v1, double v2)
    {

        BigDecimal b1 = new BigDecimal(Double.toString(v1));

        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        double result = b1.multiply(b2).doubleValue();
        return divide(result,1,2,BigDecimal.ROUND_DOWN);

    }

    /**

     * 提供精确的乘法运算。

     * @param v1

     * @param v2

     * @return 两个参数的积

     */

    public static double multiply(double v1, double v2,double v3)
    {

        BigDecimal b1 = new BigDecimal(Double.toString(v1));

        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        BigDecimal b3 = new BigDecimal(Double.toString(v3));

        BigDecimal m1 = b1.multiply(b2);
        double result = b3.multiply(m1).doubleValue();
        return divide(result,1,2,BigDecimal.ROUND_DOWN);

    }

    /**

     * 提供精确的乘法运算

     * @param v1

     * @param v2

     * @return 两个参数的数学积，以字符串格式返回

     */

    public static String multiply(String v1, String v2)
    {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        double result = b1.multiply(b2).doubleValue();
        return divide(""+result,1,2,BigDecimal.ROUND_DOWN);

    }

    public static double multiply1(String v1, String v2)

    {

        BigDecimal b1 = new BigDecimal(v1);

        BigDecimal b2 = new BigDecimal(v2);

        double result = b1.multiply(b2).doubleValue();
        return divide(result,1,2,BigDecimal.ROUND_DOWN);
    }



    /**

     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到

     * 小数点以后10位，以后的数字四舍五入,舍入模式采用ROUND_HALF_EVEN

     * @param v1

     * @param v2

     * @return 两个参数的商

     */

    public static double divide(double v1, double v2)

    {

        return divide(v1, v2, DEFAULT_DIV_SCALE);

    }



    /**

     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指

     * 定精度，以后的数字四舍五入。舍入模式采用ROUND_HALF_EVEN

     * @param v1

     * @param v2

     * @param scale 表示需要精确到小数点以后几位。

     * @return 两个参数的商

     */

    public static double divide(double v1,double v2, int scale)

    {

        return divide(v1, v2, scale, BigDecimal.ROUND_HALF_EVEN);

    }



    /**

     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指

     * 定精度，以后的数字四舍五入。舍入模式采用用户指定舍入模式

     * @param v1

     * @param v2

     * @param scale 表示需要精确到小数点以后几位

     * @param round_mode 表示用户指定的舍入模式

     * @return 两个参数的商

     */

    public static double divide(double v1,double v2,int scale, int round_mode){

        if(scale < 0)

        {

            throw new IllegalArgumentException("The scale must be a positive integer or zero");

        }

        BigDecimal b1 = new BigDecimal(Double.toString(v1));

        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return b1.divide(b2, scale, round_mode).doubleValue();

    }

    public static String divide(String v1,double v2,int scale, int round_mode){

        if(scale < 0)

        {

            throw new IllegalArgumentException("The scale must be a positive integer or zero");

        }

        BigDecimal b1 = new BigDecimal(v1);

        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return b1.divide(b2, scale, round_mode).toString();
    }



    /**

     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到

     * 小数点以后10位，以后的数字四舍五入,舍入模式采用ROUND_HALF_EVEN

     * @param v1

     * @param v2

     * @return 两个参数的商，以字符串格式返回

     */

    public static String divide(String v1, String v2)

    {

        return divide(v1, v2, DEFAULT_DIV_SCALE);

    }



    /**

     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指

     * 定精度，以后的数字四舍五入。舍入模式采用ROUND_HALF_EVEN

     * @param v1

     * @param v2

     * @param scale 表示需要精确到小数点以后几位

     * @return 两个参数的商，以字符串格式返回

     */

    public static String divide(String v1, String v2, int scale)

    {

        return divide(v1, v2, DEFAULT_DIV_SCALE, BigDecimal.ROUND_HALF_EVEN);

    }



    /**

     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指

     * 定精度，以后的数字四舍五入。舍入模式采用用户指定舍入模式

     * @param v1

     * @param v2

     * @param scale 表示需要精确到小数点以后几位

     * @param round_mode 表示用户指定的舍入模式

     * @return 两个参数的商，以字符串格式返回

     */

    public static String divide(String v1, String v2, int scale, int round_mode)

    {

        if(scale < 0)

        {

            throw new IllegalArgumentException("The scale must be a positive integer or zero");

        }

        BigDecimal b1 = new BigDecimal(v1);

        BigDecimal b2 = new BigDecimal(v2);

        return b1.divide(b2, scale, round_mode).toString();

    }



    /**

     * 提供精确的小数位四舍五入处理,舍入模式采用ROUND_HALF_EVEN

     * @param v 需要四舍五入的数字

     * @param scale 小数点后保留几位

     * @return 四舍五入后的结果

     */

    public static double round(double v,int scale)

    {

        return round(v, scale, BigDecimal.ROUND_HALF_EVEN);

    }

    /**

     * 提供精确的小数位四舍五入处理

     * @param v 需要四舍五入的数字

     * @param scale 小数点后保留几位

     * @param round_mode 指定的舍入模式

     * @return 四舍五入后的结果

     */

    public static double round(double v, int scale, int round_mode)

    {

        if(scale<0)

        {

            throw new IllegalArgumentException("The scale must be a positive integer or zero");

        }

        BigDecimal b = new BigDecimal(Double.toString(v));

        return b.setScale(scale, round_mode).doubleValue();

    }



    /**

     * 提供精确的小数位四舍五入处理,舍入模式采用ROUND_HALF_EVEN

     * @param v 需要四舍五入的数字

     * @param scale 小数点后保留几位

     * @return 四舍五入后的结果，以字符串格式返回

     */

    public static String round(String v, int scale)

    {

        return round(v, scale, BigDecimal.ROUND_HALF_EVEN);

    }

    /**

     * 提供精确的小数位四舍五入处理

     * @param v 需要四舍五入的数字

     * @param scale 小数点后保留几位

     * @param round_mode 指定的舍入模式

     * @return 四舍五入后的结果，以字符串格式返回

     */

    public static String round(String v, int scale, int round_mode)

    {

        if(scale<0)

        {

            throw new IllegalArgumentException("The scale must be a positive integer or zero");

        }

        BigDecimal b = new BigDecimal(v);

        return b.setScale(scale, round_mode).toString();

    }
}
