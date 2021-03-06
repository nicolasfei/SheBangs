package com.prxd.shebangs.serverInterface;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.prxd.shebangs.app.SheBangsApp;
import com.prxd.shebangs.serverInterface.background.BackgroundCommand;
import com.prxd.shebangs.serverInterface.cash.CashCommand;
import com.prxd.shebangs.serverInterface.login.LoginCommand;

import java.util.ArrayList;
import java.util.List;

public class Invoker {
    private static final String TAG = "Invoker";
    private OnExecResultCallback callback;
    private static Invoker invoker = new Invoker();
    private List<Command> commandList;

    private Invoker() {
        //创建所需命令
        commandList = new ArrayList<>();
        commandList.add(new LoginCommand());
        commandList.add(new CashCommand());
        commandList.add(new BackgroundCommand());
    }

    public static Invoker getInstance() {
        return invoker;
    }

    public void exec(CommandVo vo) {
        //线程异步执行
        TaskThread thread = new TaskThread(vo);
        thread.start();
    }

    //设置处理结果回调接口
    public void setOnEchoResultCallback(OnExecResultCallback callback) {
        this.callback = callback;
    }

    //处理结果返回接口
    public interface OnExecResultCallback {
        void execResult(CommandResponse result);
    }

    /**
     * handler
     */
    private Handler handler = new Handler(SheBangsApp.getInstance().getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    if (callback != null) {
                        Bundle b = msg.getData();
//                        Log.d(TAG, "handleMessage: " + b.getString("result") + "---" + b.getString("url"));
                        callback.execResult(new CommandResponse(b.getString("result"), b.getString("url")));
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 线程执行
     */
    private class TaskThread extends Thread {
        private CommandVo vo;

        private TaskThread(CommandVo vo) {
            this.vo = vo;
        }

        @Override
        public void run() {
            super.run();
            for (Command c : commandList) {
                if (c.getCommandType() == vo.typeEnum) {
                    String result = c.execute(vo);
                    Log.d(TAG, "run: Invoker result is "+result);
                    Message msg = handler.obtainMessage();
                    msg.what = 1;
                    Bundle b = new Bundle();
                    b.putString("result", result);
                    b.putString("url", vo.url);
                    msg.setData(b);
                    handler.sendMessage(msg);
                    break;
                }
            }
        }
    }
}
