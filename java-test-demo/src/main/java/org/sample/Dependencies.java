package org.sample;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by sgrift on 28-6-2016.
 */
@XmlRootElement
public class Dependencies {

    private List<Dependency> dependencyList;

    public List<Dependency> getDependencyList() {
        return dependencyList;
    }

    @XmlElement(name = "dependency")
    public void setDependencyList(List<Dependency> dependencyList) {
        this.dependencyList = dependencyList;
    }
}
