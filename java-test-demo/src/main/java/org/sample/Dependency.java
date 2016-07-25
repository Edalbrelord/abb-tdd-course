package org.sample;

import javax.xml.bind.annotation.XmlElement;

/**
 * Created by sgrift on 28-6-2016.
 */

public class Dependency {

    private String groupId;
    private String artifactId;
    private String scope;
    private String systemPath;
    private String version;

    public String getGroupId() {
        return groupId;
    }

    @XmlElement
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    @XmlElement
    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getScope() {
        return scope;
    }

    @XmlElement
    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getSystemPath() {
        return systemPath;
    }

    @XmlElement
    public void setSystemPath(String url) {
        this.systemPath = url;
    }

    public String getVersion() {
        return version;
    }

    @XmlElement
    public void setVersion(String version) {
        this.version = version;
    }
}
