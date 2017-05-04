package jufeng.juyancash.bean;

/**
 * Created by Administrator102 on 2016/12/20.
 */

public class WXPayBean {
    private String oid;//订单ID
    private int tl;//订单总金额
    private int pt;//订单支付类型：1：微信支付；2：会员卡支付
    private int yh;//订单优惠金额
    private int pay;//订单支付金额

    private String vno;//会员卡卡号 如果有会员卡就有该字段
    private int vb;//会员卡余额
    private int vt;//会员类型

    private String cuid;//优惠券和用户关联ID
    private String cid;//优惠券ID
    private int fc;//优惠券条件
    private int fv;//面值
    private int cpt;//优惠券类型
    private int vp;//是否可以和会员卡同时使用
    private int vf;//是否适用于不可打折的商品和套餐

    private String pno;//微信支付流水号

    public String getPno() {
        return pno;
    }

    public void setPno(String pno) {
        this.pno = pno;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public int getTl() {
        return tl;
    }

    public void setTl(int tl) {
        this.tl = tl;
    }

    public int getPt() {
        return pt;
    }

    public void setPt(int pt) {
        this.pt = pt;
    }

    public int getYh() {
        return yh;
    }

    public void setYh(int yh) {
        this.yh = yh;
    }

    public int getPay() {
        return pay;
    }

    public void setPay(int pay) {
        this.pay = pay;
    }

    public String getVno() {
        return vno;
    }

    public void setVno(String vno) {
        this.vno = vno;
    }

    public String getCuid() {
        return cuid;
    }

    public void setCuid(String cuid) {
        this.cuid = cuid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public int getFc() {
        return fc;
    }

    public void setFc(int fc) {
        this.fc = fc;
    }

    public int getFv() {
        return fv;
    }

    public void setFv(int fv) {
        this.fv = fv;
    }

    public int getCpt() {
        return cpt;
    }

    public void setCpt(int cpt) {
        this.cpt = cpt;
    }

    public int getVp() {
        return vp;
    }

    public void setVp(int vp) {
        this.vp = vp;
    }

    public int getVf() {
        return vf;
    }

    public void setVf(int vf) {
        this.vf = vf;
    }

    public int getVb() {
        return vb;
    }

    public void setVb(int vb) {
        this.vb = vb;
    }

    public int getVt() {
        return vt;
    }

    public void setVt(int vt) {
        this.vt = vt;
    }
}
