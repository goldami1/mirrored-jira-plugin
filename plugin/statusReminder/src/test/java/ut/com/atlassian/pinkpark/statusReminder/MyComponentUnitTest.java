package ut.com.atlassian.pinkpark.statusReminder;

import org.junit.Test;
import com.atlassian.pinkpark.statusReminder.api.MyPluginComponent;
import com.atlassian.pinkpark.statusReminder.impl.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}