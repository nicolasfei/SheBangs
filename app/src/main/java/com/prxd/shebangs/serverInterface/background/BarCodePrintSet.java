package com.prxd.shebangs.serverInterface.background;

import com.prxd.shebangs.serverInterface.CommandVo;
import com.prxd.shebangs.serverInterface.HttpHandler;

public class BarCodePrintSet extends BackgroundInterface {
    @Override
    public String getUrlParam() {
        return super.SetBarCodePrint;
    }

    @Override
    public String echo(CommandVo vo) {
        return HttpHandler.handlerHttpRequest(vo.url, vo.parameters, vo.requestMode, vo.contentType);
    }
}
