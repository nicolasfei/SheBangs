package com.prxd.shebangs.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.prxd.shebangs.R;
import com.prxd.shebangs.branch.BranchInformation;
import com.prxd.shebangs.branch.BranchKeeper;
import com.prxd.shebangs.branch.GoodsClass;
import com.prxd.shebangs.branch.Guide;
import com.prxd.shebangs.branch.PayCase;
import com.prxd.shebangs.ui.login.data.LoginRepository;
import com.prxd.shebangs.serverInterface.CommandResponse;
import com.prxd.shebangs.serverInterface.CommandTypeEnum;
import com.prxd.shebangs.serverInterface.CommandVo;
import com.prxd.shebangs.serverInterface.HttpHandler;
import com.prxd.shebangs.serverInterface.Invoker;
import com.prxd.shebangs.serverInterface.cash.CashInterface;
import com.prxd.shebangs.serverInterface.login.LoginInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginViewModel extends ViewModel {

    private static final String TAG = "LoginViewModel";

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private MutableLiveData<LoginResult> branchInformationResult = new MutableLiveData<>();
    private MutableLiveData<LoginResult> guideListResult = new MutableLiveData<>();
    private MutableLiveData<LoginResult> goodsListResult = new MutableLiveData<>();
    private MutableLiveData<LoginResult> payCaseResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    LiveData<LoginResult> getGuideListResult() {
        return guideListResult;
    }

    LiveData<LoginResult> getBranchInformationResult() { return branchInformationResult; }

    LiveData<LoginResult> getGoodsClassListResult() { return goodsListResult; }

    LiveData<LoginResult> getPayCaseResult() { return payCaseResult; }

    /**
     * 分店登陆
     *
     * @param username 用户名
     * @param password 密码
     */
    public void login(String username, String password) {
        CommandVo vo = new CommandVo();
        vo.typeEnum = CommandTypeEnum.COMMAND_BRANCH_LOGIN;
        vo.url = LoginInterface.BranchSaleLogin;
        vo.contentType = HttpHandler.ContentType_APP;
        vo.requestMode = HttpHandler.RequestMode_GET;
        Map<String, String> parameters = new HashMap<>();
        parameters.put("loginName", username);
        parameters.put("loginPwd", password);
        vo.parameters = parameters;
        Invoker.getInstance().setOnEchoResultCallback(this.callback);
        Invoker.getInstance().exec(vo);
    }

    /**
     * 获取店铺信息
     *
     * @param userKey userKey
     */
    public void queryBranchInformation(String userKey) {
        CommandVo vo = new CommandVo();
        vo.typeEnum = CommandTypeEnum.COMMAND_BRANCH_CASH;
        vo.url = CashInterface.GetBranch;
        vo.contentType = HttpHandler.ContentType_APP;
        vo.requestMode = HttpHandler.RequestMode_GET;
        Map<String, String> parameters = new HashMap<>();
        parameters.put("userkey", userKey);
        vo.parameters = parameters;
        Invoker.getInstance().setOnEchoResultCallback(this.callback);
        Invoker.getInstance().exec(vo);
    }

    /**
     * 获取分店导购集合
     *
     * @param userKey userKey
     */
    public void queryBranchGuideList(String userKey) {
        CommandVo vo = new CommandVo();
        vo.typeEnum = CommandTypeEnum.COMMAND_BRANCH_CASH;
        vo.url = CashInterface.GetGuideList;
        vo.contentType = HttpHandler.ContentType_APP;
        vo.requestMode = HttpHandler.RequestMode_GET;
        Map<String, String> parameters = new HashMap<>();
        parameters.put("userkey", userKey);
        vo.parameters = parameters;
        Invoker.getInstance().setOnEchoResultCallback(this.callback);
        Invoker.getInstance().exec(vo);
    }

    /**
     * 获取商品类别
     * @param type 查询大类，“”--查询所有类型
     */
    public void queryGoodsClass(String type) {
        CommandVo vo = new CommandVo();
        vo.typeEnum = CommandTypeEnum.COMMAND_BRANCH_CASH;
        vo.url = CashInterface.GetGoodsClass;
        vo.contentType = HttpHandler.ContentType_APP;
        vo.requestMode = HttpHandler.RequestMode_GET;
        Map<String, String> parameters = new HashMap<>();
        parameters.put("name", type);
        vo.parameters = parameters;
        Invoker.getInstance().setOnEchoResultCallback(this.callback);
        Invoker.getInstance().exec(vo);
    }

    /**
     * 获取支付方式
     */
    public void queryPayCase(String userKey) {
        CommandVo vo = new CommandVo();
        vo.typeEnum = CommandTypeEnum.COMMAND_BRANCH_CASH;
        vo.url = CashInterface.GetPaymentList;
        vo.contentType = HttpHandler.ContentType_APP;
        vo.requestMode = HttpHandler.RequestMode_GET;
        Map<String, String> parameters = new HashMap<>();
        parameters.put("userkey", userKey);
        vo.parameters = parameters;
        Invoker.getInstance().setOnEchoResultCallback(this.callback);
        Invoker.getInstance().exec(vo);
    }

    Invoker.OnExecResultCallback callback = new Invoker.OnExecResultCallback() {

        @Override
        public void execResult(CommandResponse result) {
            switch (result.url) {
                case LoginInterface.BranchSaleLogin:        //分店登陆
                    if (!result.success) {
                        loginResult.setValue(new LoginResult(R.string.login_failed));
                    } else {
                        try {
                            JSONObject jsonObject = new JSONObject(result.data);
                            BranchKeeper.getInstance().setUserId(jsonObject.getString("userid"));
                            BranchKeeper.getInstance().setUserKey(jsonObject.getString("userkey"));
                            loginResult.setValue(new LoginResult(new LoggedInUserView("")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loginResult.setValue(new LoginResult(R.string.login_failed));
                        }
                    }
                    break;
                case CashInterface.GetBranch:               //获取店铺信息
                    if (!result.success) {
                        branchInformationResult.setValue(new LoginResult(R.string.get_branch_failed));
                    } else {
                        try {
                            BranchInformation information = new BranchInformation(result.data);
                            BranchKeeper.getInstance().setInformation(information);
                            branchInformationResult.setValue(new LoginResult(new LoggedInUserView("")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            branchInformationResult.setValue(new LoginResult(R.string.get_branch_failed));
                        }
                    }
                    break;
                case CashInterface.GetGuideList:            //获取导购信息集合
                    if (!result.success) {
                        guideListResult.setValue(new LoginResult(R.string.get_guide_failed));
                    } else {
                        List<Guide> guides = new ArrayList<>();
                        try {
                            JSONArray array = new JSONArray(result.data);
                            for (int i = 0; i < array.length(); i++) {
                                Guide guide = new Guide(array.getString(i));
                                guides.add(guide);
                            }
                            BranchKeeper.getInstance().setGuides(guides);
                            guideListResult.setValue(new LoginResult(new LoggedInUserView("")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            guideListResult.setValue(new LoginResult(R.string.get_guide_failed));
                        }
                    }
                    break;
                case CashInterface.GetPaymentList:      //获取支付方式集合
                    if (!result.success) {
                        payCaseResult.setValue(new LoginResult(R.string.get_pay_case_failed));
                    } else {
                        try {
                            PayCase payCase = new PayCase(result.data);
                            BranchKeeper.getInstance().setPayCase(payCase);
                            payCaseResult.setValue(new LoginResult(new LoggedInUserView("")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            payCaseResult.setValue(new LoginResult(R.string.get_pay_case_failed));
                        }
                    }
                    break;
                case CashInterface.GetGoodsClass:       //获取商品类别
                    if (!result.success) {
                        goodsListResult.setValue(new LoginResult(R.string.get_goods_class_failed));
                    } else {
                        List<GoodsClass> goodsClasses = new ArrayList<>();
                        try {
                            JSONArray array = new JSONArray(result.data);
                            for (int i = 0; i < array.length(); i++) {
                                GoodsClass goods = new GoodsClass(array.getString(i));
                                goodsClasses.add(goods);
                            }
                            BranchKeeper.getInstance().setGoodsClasses(goodsClasses);
                            goodsListResult.setValue(new LoginResult(new LoggedInUserView("")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            goodsListResult.setValue(new LoginResult(R.string.get_goods_class_failed));
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}
