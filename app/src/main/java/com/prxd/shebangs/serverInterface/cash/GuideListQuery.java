package com.prxd.shebangs.serverInterface.cash;

import com.prxd.shebangs.serverInterface.CommandVo;
import com.prxd.shebangs.serverInterface.HttpHandler;

/**
 * 获取导购集合信息
 */
public class GuideListQuery extends CashInterface {
    @Override
    public String getUrlParam() {
        return super.GetGuideList;
    }

    @Override
    public String echo(CommandVo vo) {
        return HttpHandler.handlerHttpRequest(vo.url, vo.parameters, vo.requestMode, vo.contentType);
    }
}
