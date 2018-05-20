package org.mta.oss.impl;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.search.SearchException;
import com.atlassian.jira.issue.search.SearchRequest;
import com.atlassian.jira.bc.JiraServiceContextImpl;
import com.atlassian.jira.bc.filter.SearchRequestService;
import com.atlassian.jira.bc.issue.search.SearchService;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.query.Query;
import com.atlassian.sal.api.ApplicationProperties;

import org.mta.oss.api.MyPluginComponent;
import org.ofbiz.core.entity.GenericEntityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

@ExportAsService({MyPluginComponent.class})
@Named("myPluginComponent")
@Scanned
public class MyPluginComponentImpl implements MyPluginComponent {
    private static final Logger log = LoggerFactory.getLogger(MyPluginComponentImpl.class);

    private final ApplicationProperties m_ApplicationProperties;
    private final IssueManager m_IssueManager;
    private final SearchService m_SearchService;
    private List<Project> m_ProjectsList;
    private List<ApplicationUser> m_NotifiedAdmins;
    private List<Issue> m_FilteredIssuesToBeMailed;

    @Inject
    public MyPluginComponentImpl(	@ComponentImport final ApplicationProperties i_ApplicationProperties,
    								@ComponentImport final IssueManager i_IssueManager,
    								@ComponentImport final SearchService i_SearchService)
    {
        m_ApplicationProperties = i_ApplicationProperties;
        m_IssueManager = i_IssueManager;
        m_SearchService = i_SearchService;
        
    	Collection<Long> issuesIds = null;
        List<Issue> issuesList = null;
        m_NotifiedAdmins = new LinkedList<>();
        m_ProjectsList = ComponentAccessor.getProjectManager().getProjects();
        
        
        for(Project proj : m_ProjectsList)
        {
        	m_NotifiedAdmins.add(proj.getProjectLead());
        }
        
		try
		{
			issuesIds = m_IssueManager.getIssueIdsForProject(m_ProjectsList.get(0).getId());
		} catch (GenericEntityException e)
		{
			e.printStackTrace();
		}
		
		if(issuesIds!=null)
		{
			issuesList = m_IssueManager.getIssueObjects(issuesIds);
		}
		
		
		//<begin> gets filter with id=10003 and associated to ApplicationUser m_NotifiedAdmins.get(0). This filter was created through Jira's GUI
		SearchRequestService srs = ComponentAccessor.getComponent(SearchRequestService.class);
		SearchRequest sr = srs.getFilter(new JiraServiceContextImpl(m_NotifiedAdmins.get(0)), Long.valueOf(10003));
		Query srq = sr.getQuery();
		String generatedJqlString = m_SearchService.getGeneratedJqlString(srq);
		final SearchService.ParseResult parseResult = m_SearchService.parseQuery(m_NotifiedAdmins.get(0), generatedJqlString);
		
		if(parseResult.isValid())
		{
			try
			{
				com.atlassian.jira.issue.search.SearchResults results = m_SearchService.search(m_NotifiedAdmins.get(0), parseResult.getQuery(), com.atlassian.jira.web.bean.PagerFilter.getUnlimitedFilter());
				m_FilteredIssuesToBeMailed = results.getIssues();
				
				for(Issue i : m_FilteredIssuesToBeMailed)
				{
					String issueDescReflect = i.getDescription();
					issueDescReflect+=" ";
				}
				
			}catch(SearchException ex)
			{
				ex.printStackTrace();
			}
		}
		else
		{
			String errorMsg = "";
			for(String str : parseResult.getErrors().getErrorMessages())
			{
				errorMsg += " " + str;
			}
			(new Exception(errorMsg)).printStackTrace();
		}
		//<end>
		
		
		
    }    
    

	public String getName()
	{
        if (null != m_ApplicationProperties) {
            return "myComponent:" + m_ApplicationProperties.getDisplayName();
        }

        return "myComponent";
    }
}