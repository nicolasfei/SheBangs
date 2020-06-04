package com.prxd.shebangs.serverInterface.background;

import com.prxd.shebangs.serverInterface.CommandVo;
import com.prxd.shebangs.serverInterface.HttpHandler;

public class PageListSaleGet extends BackgroundInterface {
    @Override
    public String getUrlParam() {
        return super.GetPageListSale;
    }

    @Override
    public String echo(CommandVo vo) {
        return HttpHandler.handlerHttpRequest(vo.url, vo.parameters, vo.requestMode, vo.contentType);
    }
}
