package ut.com.atlassian.jiraPlugin;

import org.junit.Test;
import com.atlassian.jiraPlugin.api.MyPluginComponent;
import com.atlassian.jiraPlugin.impl.MyPluginComponentImpl;

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