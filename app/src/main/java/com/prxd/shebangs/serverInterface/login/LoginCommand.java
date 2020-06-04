package com.prxd.shebangs.serverInterface.login;

import android.util.Log;

import com.prxd.shebangs.serverInterface.Command;
import com.prxd.shebangs.serverInterface.CommandTypeEnum;
import com.prxd.shebangs.serverInterface.CommandVo;

public class LoginCommand extends Command {
    private static final String TAG = "LoginCommand";
    @Override
    public String execute(CommandVo vo) {
        return super.firstNode.handleMessage(vo);
    }

    @Override
    protected void buildDutyChain() {
        Log.d(TAG, "buildDutyChain: ");
        LoginInterface sale = new BranchSaleLogin();
        LoginInterface admin = new BranchAdminLogin();
        sale.setNextHandler(admin);
        super.firstNode = sale;
    }

    @Override
    public CommandTypeEnum getCommandType() {
        return CommandTypeEnum.COMMAND_BRANCH_LOGIN;
    }
}
