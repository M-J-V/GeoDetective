package com.example.geodetective;

import static org.junit.Assert.fail;

import android.content.Intent;

import com.example.geodetective.connectivity.ConnectivityChecker;

import org.junit.Test;

@SuppressWarnings("ALL")
public class ConnectivityCheckerTest {

    @Test
    public void checkHasInternetConnectionInvalidInput() {
        try {
            ConnectivityChecker.hasInternetConnection(null);
            fail("Exception not thrown");
        } catch (Exception ignored) {}
    }

    @Test
    public void checkHasGPSConnectionInvalidInput() {
        try {
            ConnectivityChecker.hasGPSConnection(null);
            fail("Exception not thrown");
        } catch (Exception ignored) {}
    }

    @Test
    public void checkOpenNoInternetDialogInvalidInput() {
        try {
            ConnectivityChecker.openNoInternetDialog(null);
            fail("Exception not thrown");
        } catch (Exception ignored) {}
    }

    @Test
    public void checkOpenNoGPSDialogInvalidInput() {
        try {
            ConnectivityChecker.openNoGPSDialog(null);
            fail("Exception not thrown");
        } catch (Exception ignored) {}
    }

    @Test
    public void checkOnReceiveInvalidInput() {
        try {
            ConnectivityChecker connectivityChecker = new ConnectivityChecker();
            connectivityChecker.onReceive(null, null);
            fail("Exception not thrown");
        } catch (Exception ignored) {}

        try {
            ConnectivityChecker connectivityChecker = new ConnectivityChecker();
            connectivityChecker.onReceive(null, new Intent());
            fail("Exception not thrown");
        } catch (Exception ignored) {}
    }

}
