package edu.csumb.cst438.router;

import android.media.MediaRouter;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.assertEquals;

/**
 * Created by kami on 12/4/16.
 */


@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class UserTest {
    public String username = "meeseks";
    public String bio = "I'm Mr. meeseks look at me!";
    public String email = "jerrysgolfswing@email.com";
    public String id = "6";
    public String privacy = "PRIVACY";

    @Test
    public void createUser() throws Exception {
        PowerMockito.mockStatic(Log.class);
        User test = new User(this.username, this.bio, this.email, this.id, this.privacy);
        assertEquals(this.username, test.username);
        assertEquals(this.bio, test.bio);
        assertEquals(this.email, test.email);
        assertEquals(this.id, test.id);
        assertEquals(this.privacy, test.privacy);
    }


}
