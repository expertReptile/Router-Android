package edu.csumb.cst438.router;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;



/**
 * Created by pico on 12/4/16.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, Future.class, Executors.class, UserServices.class})
public class ConnectorEndpointsTest {

    @Before
    public void setUp() {
        PowerMockito.mockStatic(Log.class);
    }

    @Test
    public void getFriendRequests() throws Exception {

        User user1 = new User("username1", "bio1", "email1", "1", "privacy1");
        User user2 = new User("username2", "bio2", "email2", "2", "privacy2");
        ArrayList<User> users = new ArrayList<User>();
        users.add(user1);
        users.add(user2);

        ExecutorService executorService = PowerMockito.mock(ExecutorService.class);
        Future future = PowerMockito.mock(Future.class);
        when(future.get()).thenReturn(users);

        when(executorService.submit(any(Callable.class))).thenReturn(future);
        Whitebox.setInternalState(Connector.class, "executorService", executorService);

        Connector connector = new Connector();

        assertEquals(connector.getFriendRequests(), users);

    }

    @Test
    public void getFriends() throws Exception {
        User user1 = new User("username1", "bio1", "email1", "1", "privacy1");
        User user2 = new User("username2", "bio2", "email2", "2", "privacy2");
        ArrayList<User> friends = new ArrayList<User>();
        friends.add(user1);
        friends.add(user2);

        ExecutorService executorService = PowerMockito.mock(ExecutorService.class);
        Future future = PowerMockito.mock(Future.class);
        when(future.get()).thenReturn(friends);

        when(executorService.submit(any(Callable.class))).thenReturn(future);
        Whitebox.setInternalState(Connector.class, "executorService", executorService);

        Connector connector = new Connector();

        assertEquals(connector.getFriends(), friends);
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