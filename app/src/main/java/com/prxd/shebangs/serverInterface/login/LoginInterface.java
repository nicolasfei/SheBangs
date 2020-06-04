package com.prxd.shebangs.serverInterface.login;

import com.prxd.shebangs.serverInterface.AbstractInterface;

public abstract class LoginInterface extends AbstractInterface {
    //分店收银登陆接口
    public final static String BranchSaleLogin = AbstractInterface.COMMAND_URL+"api/Login/BranchSaleLogin";
    //分店后台登陆接口
    public final static String BranchAdminLogin = AbstractInterface.COMMAND_URL+"api/Login/BranchAdminLogin";
}
