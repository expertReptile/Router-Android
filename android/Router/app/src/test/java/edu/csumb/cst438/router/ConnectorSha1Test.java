package edu.csumb.cst438.router;

import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by pico on 12/4/16.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class ConnectorSha1Test {

    @Test
    public void sha1() throws Exception {
        PowerMockito.mockStatic(Log.class);
        String password = "password";
        String result = Connector.sha1(password);

        assertEquals(result, "70617373776f7264");
    }

}
