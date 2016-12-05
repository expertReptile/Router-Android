package edu.csumb.cst438.router;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by pico on 12/4/16.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class ConnectorEndpointsTest {

    @Before
    public void setUp() {
        PowerMockito.mockStatic(Log.class);
    }

    @Test
    public void getFriendRequests() throws Exception {

        ExecutorService executorService = mock(Executors.newSingleThreadExecutor().getClass());
        when(executorService.submit(any(Callable.class))).thenReturn(null);
    }

    @Test
    public void getFriends() throws Exception {

    }

    @Test
    public void getRoutesShared() throws Exception {

    }

    @Test
    public void shareRoute() throws Exception {

    }

    @Test
    public void processRequest() throws Exception {

    }

    @Test
    public void searchFriends() throws Exception {

    }

    @Test
    public void removeFriend() throws Exception {

    }

    @Test
    public void addFriend() throws Exception {

    }

    @Test
    public void downloadRoute() throws Exception {

    }

    @Test
    public void createUser() throws Exception {

    }

    @Test
    public void updateUser() throws Exception {

    }

    @Test
    public void uploadRoute() throws Exception {

    }

    @Test
    public void getNearMe() throws Exception {

    }

    @Test
    public void checkLogin() throws Exception {

    }

}