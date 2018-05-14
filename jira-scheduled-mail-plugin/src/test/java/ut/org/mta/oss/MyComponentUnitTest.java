package ut.org.mta.oss;

import org.junit.Test;
import org.mta.oss.api.MyPluginComponent;
import org.mta.oss.impl.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest {
    @Test
    public void testMyName() {
        MyPluginComponent component = new MyPluginComponentImpl(null, null);
        assertEquals("names do not match!", "myComponent", component.getName());
    }
}