package bruzsa.laszlo.dartsapp;

import android.content.Context;

import dagger.hilt.android.testing.HiltTestApplication;

public final class MyTestRunner extends androidx.test.runner.AndroidJUnitRunner {
    @Override
    public android.app.Application newApplication(ClassLoader cl, String appName, Context context)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        return super.newApplication(cl, HiltTestApplication.class.getName(), context);
    }
}