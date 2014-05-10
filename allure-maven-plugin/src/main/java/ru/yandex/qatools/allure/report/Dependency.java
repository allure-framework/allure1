package ru.yandex.qatools.allure.report;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 12.11.13
 */
@SuppressWarnings("unused")
public class Dependency {

    private String groupId;

    private String artifactId;

    private String version;

    private String packaging;

    public Dependency() {
    }

    public Dependency(String groupId, String artifactId, String version, String packaging) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.packaging = packaging;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }
}
