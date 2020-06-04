package com.prxd.shebangs.ui.login;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.prxd.shebangs.MainActivity;
import com.prxd.shebangs.R;
import com.prxd.shebangs.branch.BranchKeeper;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private LoginViewModel loginViewModel;
    private ProgressDialog loginDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        /**
         * 监听登陆信息输入
         */
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        /**
         * 监听登陆结果
         */
        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    dismissLoginDialog();
                    showLoginFailed(R.string.login_failed);
                    return;
                }
                if (loginResult.getError() != null) {
                    dismissLoginDialog();
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    showLoginDialog(getString(R.string.getting_branch));
                    //获取分店信息
                    loginViewModel.queryBranchInformation(BranchKeeper.getInstance().userKey);
                }
            }
        });

        /**
         * 监听获取分店信息
         */
        loginViewModel.getBranchInformationResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(LoginResult branchInformation) {
                if (branchInformation == null) {
                    dismissLoginDialog();
                    showLoginFailed(R.string.query_failed);
                    return;
                }

                if (branchInformation.getError() != null) {
                    dismissLoginDialog();
                    showLoginFailed(R.string.get_branch_failed);
                }
                if (branchInformation.getSuccess() != null) {
                    showLoginDialog(getString(R.string.getting_guide));
                    //获取所有商品类别
                    loginViewModel.queryGoodsClass("");
                }
            }
        });

        /**
         * 监听获取商品类别信息
         */
        loginViewModel.getGoodsClassListResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(LoginResult goodsClassList) {
                if (goodsClassList == null) {
                    dismissLoginDialog();
                    showLoginFailed(R.string.query_failed);
                    return;
                }

                if (goodsClassList.getError() != null) {
                    dismissLoginDialog();
                    showLoginFailed(R.string.get_goods_class_failed);
                }
                if (goodsClassList.getSuccess() != null) {
                    showLoginDialog(getString(R.string.getting_pay_case));
                    //获取支付方式
                    loginViewModel.queryPayCase(BranchKeeper.getInstance().userKey);
                }
            }
        });

        /**
         * 查询支付方式
         */
        loginViewModel.getPayCaseResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(LoginResult payCase) {
                if (payCase == null) {
                    dismissLoginDialog();
                    showLoginFailed(R.string.query_failed);
                    return;
                }
                if (payCase.getError() != null) {
                    dismissLoginDialog();
                    showLoginFailed(payCase.getError());
                }
                if (payCase.getSuccess() != null) {
                    showLoginDialog(getString(R.string.getting_guide));
                    //获取导购信息
                    loginViewModel.queryBranchGuideList(BranchKeeper.getInstance().userKey);
                }
            }
        });

        /**
         * 监听获取导购信息
         */
        loginViewModel.getGuideListResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(LoginResult guides) {
                dismissLoginDialog();
                if (guides == null) {
                    showLoginFailed(R.string.query_failed);
                    return;
                }
                if (guides.getError() != null) {
                    showLoginFailed(R.string.get_guide_failed);
                }
                if (guides.getSuccess() != null) {
                    //选择当前在岗导购
                    choiceOnDutyGuide();
                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    showLoginDialog(getString(R.string.login_ing));
                    String userName = usernameEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    BranchKeeper.getInstance().setLoginName(userName);
                    BranchKeeper.getInstance().setLoginPassword(password);
                    loginViewModel.login(userName, password);
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loadingProgressBar.setVisibility(View.VISIBLE);
                showLoginDialog(getString(R.string.login_ing));
                String userName = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                BranchKeeper.getInstance().setLoginName(userName);
                BranchKeeper.getInstance().setLoginPassword(password);
                loginViewModel.login(userName, password);
            }
        });
    }

    /**
     * 选择在岗导购
     */
    private void choiceOnDutyGuide() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.login_guide_choice);
        builder.setCancelable(false);

        final String items[] = new String[BranchKeeper.getInstance().guides.size()];
        for (int i = 0; i < BranchKeeper.getInstance().guides.size(); i++) {
            items[i] = BranchKeeper.getInstance().guides.get(i).guideName;
        }
        // -1代表没有条目被选中
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 设置当前在岗导购
                BranchKeeper.getInstance().setOnDutyGuide(items[which]);
                updateUiWithUser(new LoggedInUserView(items[which]));       //登陆成功
                // [2]把对话框关闭
                dialog.dismiss();
            }
        });

        // 最后一步 一定要记得 和Toast 一样 show出来
        builder.create().show();
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void showLoginDialog(String loginMsg) {
        if (loginDialog == null) {
            loginDialog = new ProgressDialog(this);
            loginDialog.setCancelable(false);
            loginDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        loginDialog.setMessage(loginMsg);
        if (!loginDialog.isShowing()) {
            loginDialog.show();
        }
    }

    private void dismissLoginDialog() {
        if (loginDialog.isShowing()) {
            loginDialog.dismiss();
        }
    }
}
