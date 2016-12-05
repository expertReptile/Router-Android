package edu.csumb.cst438.router;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by pico on 12/4/16.
 */
public class ConnectorEndpointsTest {
    @Test
    public void getFriendRequests() throws Exception {

        ExecutorService executorService = mock(Executors.newSingleThreadExecutor().getClass());
        when(executorService.submit(any(Callable.class))).thenReturn()
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