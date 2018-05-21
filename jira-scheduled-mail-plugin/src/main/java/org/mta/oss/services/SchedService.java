package org.mta.oss.services;
import com.atlassian.configurable.ObjectConfiguration;
import com.atlassian.configurable.ObjectConfigurationException;
import com.atlassian.jira.service.AbstractService;

public class SchedService extends AbstractService
{
	public ObjectConfiguration getObjectConfiguration() throws ObjectConfigurationException
	{
		// TODO
		return getObjectConfiguration("SchedulerService", "org/mta/oss/services/schedservicecfg.xml", null);
	}

	
	public void run()
	{
		// TODO
		System.out.println("Running my service");
	}
}
