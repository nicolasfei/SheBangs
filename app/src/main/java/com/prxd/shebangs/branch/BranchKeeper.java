package com.prxd.shebangs.branch;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * 店铺信息
 */
public class BranchKeeper {
    public String loginName;            //登陆用户名
    public String loginPassword;        //登陆密码
    public String userId;               //店铺ID
    public String userKey;              //登陆成功后返回key

    public BranchInformation information;   //店铺信息
    public List<Guide> guides;              //店铺导购
    public Guide onDutyGuide;               //值班导购
    public PayCase payCase;                 //支付方式
    public List<GoodsClass> goodsClasses;   //商品类别

    private static BranchKeeper keeper = new BranchKeeper();

    private BranchKeeper() {
    }

    public static BranchKeeper getInstance() {
        return keeper;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public void setInformation(String data) {
        try {
            this.information = new BranchInformation(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addGuide(String guideData) {
        if (this.guides == null) {
            this.guides = new ArrayList<>();
        }
        Guide guide = null;
        try {
            guide = new Guide(guideData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.guides.add(guide);
    }

    public void setGoodsClasses(List<GoodsClass> goodsClasses) {
        this.goodsClasses = goodsClasses;
    }

    public void setPayCase(PayCase payCase) {
        this.payCase = payCase;
    }

    public void setOnDutyGuide(String guideName) {
        if (guides != null) {
            for (Guide g : guides) {
                if (g.guideName.equals(guideName)) {
                    this.onDutyGuide = g;
                    break;
                }
            }
        }
    }

    public void setInformation(BranchInformation branchInformation) {
        this.information = branchInformation;
    }

    public void setGuides(List<Guide> guides) {
        this.guides = guides;
    }

    /**
     * 获取导购名字列表
     *
     * @return 导购名字列表
     */
    public String[] getGuides() {
        String[] guideNames = null;
        if (guides != null) {
            guideNames = new String[guides.size()];
            for (int i = 0; i < guides.size(); i++) {
                guideNames[i] = guides.get(i).guideName;
            }
        }
        return guideNames;
    }

    /**
     * 获取商品类别名字
     *
     * @return String[]
     */
    public String[] getGoodsClassesName() {
        String[] goodsClassNames = null;
        if (goodsClasses != null) {
            goodsClassNames = new String[goodsClasses.size()];
            for (int i = 0; i < goodsClasses.size(); i++) {
                goodsClassNames[i] = goodsClasses.get(i).getName();
            }
        }
        return goodsClassNames;
    }

    /**
     * 获得供应商ID集合
     *
     * @return 供应商ID集合
     */
    public String[] getSupplierIDs() {
        return new String[0];
    }

    /**
     * 获取条码状态
     *
     * @return 条码状态
     */
    public String[] getCodeStatus() {
        return new String[0];
    }

    /**
     * 获取是否盘点选择值
     *
     * @return 是否盘点选择值
     */
    public String[] getIsInventorySpin() {
        return new String[]{"已盘点", "未盘点"};
    }
}
