package org.mta.oss.impl;

import com.atlassian.jira.issue.IssueManager;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.ApplicationProperties;
import org.mta.oss.api.MyPluginComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;

@ExportAsService({MyPluginComponent.class})
@Named("myPluginComponent")
public class MyPluginComponentImpl implements MyPluginComponent {
    private static final Logger log = LoggerFactory.getLogger(MyPluginComponentImpl.class);

    @ComponentImport
    private final ApplicationProperties applicationProperties;
    @ComponentImport
    private final IssueManager issueManager;

    @Inject
    public MyPluginComponentImpl(final ApplicationProperties applicationProperties, final IssueManager issueManager) {
        this.applicationProperties = applicationProperties;
        this.issueManager = issueManager;

        // issueManager is currently null in unit testing
        if (issueManager != null) {
            log.info("Issue count: {}", issueManager.getIssueCount());
        }
    }

    public String getName() {
        if (null != applicationProperties) {
            return "myComponent:" + applicationProperties.getDisplayName();
        }

        return "myComponent";
    }
}