package com.prxd.shebangs.serverInterface.cash;

import com.prxd.shebangs.serverInterface.CommandVo;
import com.prxd.shebangs.serverInterface.HttpHandler;

/**
 * 分店信息查询
 */
public class BranchQuery extends CashInterface {
    @Override
    public String getUrlParam() {
        return super.GetBranch;
    }

    @Override
    public String echo(CommandVo vo) {
        return HttpHandler.handlerHttpRequest(vo.url, vo.parameters, vo.requestMode, vo.contentType);
    }
}
