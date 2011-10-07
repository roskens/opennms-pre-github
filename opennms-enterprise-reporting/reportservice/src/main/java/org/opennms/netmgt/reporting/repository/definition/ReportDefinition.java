package org.opennms.netmgt.reporting.repository.definition;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * General information about a jasper report
 * 
 * @author thargor
 */
@XmlRootElement(name = "reportDefinition")
public class ReportDefinition {

    private Integer m_id;
    private RepositoryTyp m_repositoryTyp;
    private String m_name;
    private String m_description;
    private String m_templateName;

    @XmlTransient
    private List<String> m_engineVersions = new ArrayList<String>();

    public Integer getId() {
        return m_id;
    }

    public void setId(Integer id) {
        m_id = id;
    }

    public RepositoryTyp getRepositoryTyp() {
        return m_repositoryTyp;
    }

    public void setRepositoryTyp(RepositoryTyp repositoryTyp) {
        m_repositoryTyp = repositoryTyp;
    }

    public String getName() {
        return m_name;
    }

    public void setName(String name) {
        m_name = name;
    }

    public String getDescription() {
        return m_description;
    }

    public void setDescription(String description) {
        m_description = description;
    }

    public String getTemplateName() {
        return m_templateName;
    }

    public void setTemplateName(String templateName) {
        m_templateName = templateName;
    }

    public List<String> getEngineVersions() {
        return m_engineVersions;
    }

    public void setEngineVersions(List<String> versions) {
        m_engineVersions = versions;
    }

}
