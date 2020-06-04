package com.prxd.shebangs.ui.login.data;

import com.prxd.shebangs.ui.login.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {
        // TODO: handle loggedInUser authentication
        try {
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            "userid", "userkey");
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("login error", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
