package com.prxd.shebangs.serverInterface.login;

import com.prxd.shebangs.serverInterface.CommandVo;
import com.prxd.shebangs.serverInterface.HttpHandler;

public class BranchSaleLogin extends LoginInterface {
    @Override
    public String getUrlParam() {
        return super.BranchSaleLogin;
    }

    @Override
    public String echo(CommandVo vo) {
        return HttpHandler.handlerHttpRequest(vo.url, vo.parameters, vo.requestMode, vo.contentType);
    }
}
