package ru.yandex.qatools.allure.inject;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.util.List;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 25.11.13
 */
public abstract class AbstractPlugin extends AbstractMojo {

    protected static final String FAQ = "https://github.com/allure-framework/allure-core/blob/master/docs/FAQ.md";

    protected static final String ALLURE_TEAM = "mailto:charlie@yandex-team.ru";

    @Parameter(defaultValue = "${project}", required = true)
    protected MavenProject mavenProject;

    @Parameter(defaultValue = "${session}", required = true)
    protected MavenSession mavenSession;

    @Parameter(defaultValue = "${localRepository}", readonly = true, required = true)
    protected ArtifactRepository localRepository;

    @Parameter(defaultValue = "${project.remoteArtifactRepositories}", readonly = true, required = true)
    protected List<ArtifactRepository> remoteRepositories;

    @Component
    protected BuildPluginManager pluginManager;

    @Component
    protected ArtifactFactory factory;

    @Component
    protected ArtifactResolver resolver;

    protected Artifact getArtifact(String groupId, String artifactId, String version, String packaging) {
        Artifact artifact = factory.createBuildArtifact(
                groupId,
                artifactId,
                version,
                packaging
        );

        try {
            resolver.resolve(artifact, remoteRepositories, localRepository);
        } catch (ArtifactResolutionException | ArtifactNotFoundException e) {
            getLog().error(getErrorMessage(e));
        }

        return artifact;
    }

    protected String getErrorMessage(Throwable throwable) {
        return String.format(
                "%s. Please, see FAQ %s or contact with allure team %s.",
                throwable.getMessage(),
                FAQ,
                ALLURE_TEAM
        );
    }
}
