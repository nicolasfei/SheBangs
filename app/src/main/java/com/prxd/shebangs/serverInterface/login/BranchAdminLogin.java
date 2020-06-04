package com.prxd.shebangs.serverInterface.login;

import com.prxd.shebangs.serverInterface.CommandVo;
import com.prxd.shebangs.serverInterface.HttpHandler;

public class BranchAdminLogin extends LoginInterface {
    @Override
    public String getUrlParam() {
        return super.BranchAdminLogin;
    }

    @Override
    public String echo(CommandVo vo) {
        return HttpHandler.handlerHttpRequest(vo.url, vo.parameters, vo.requestMode, vo.contentType);
    }
}
