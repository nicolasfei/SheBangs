package com.prxd.shebangs.serverInterface.cash;

import com.prxd.shebangs.serverInterface.CommandVo;
import com.prxd.shebangs.serverInterface.HttpHandler;

public class UserBasicTotalQuery extends CashInterface {
    @Override
    public String getUrlParam() {
        return super.GetUserBasicTotal;
    }

    @Override
    public String echo(CommandVo vo) {
        return HttpHandler.handlerHttpRequest(vo.url, vo.parameters, vo.requestMode, vo.contentType);
    }
}
