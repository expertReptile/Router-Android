package edu.csumb.cst438.router;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by pico on 12/4/16.
 */
public class ConnectorSha1Test {
    @Test
    public void sha1() throws Exception {
        String password = "password";
        String result = Connector.sha1(password);


        assertEquals(result, "70617373776f7264");
    }

}