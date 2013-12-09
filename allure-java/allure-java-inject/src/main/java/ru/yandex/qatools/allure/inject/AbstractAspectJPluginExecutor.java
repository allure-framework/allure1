package ru.yandex.qatools.allure.inject;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.xml.Xpp3Dom;

import java.util.ArrayList;
import java.util.List;

import static org.twdata.maven.mojoexecutor.MojoExecutor.*;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 25.11.13
 */
public abstract class AbstractAspectJPluginExecutor extends AbstractPlugin {

    @Parameter(defaultValue = "1.7")
    protected String javaVersion;

    @Parameter(defaultValue = "1.7.4")
    protected String aspectJVersion;

    @Parameter(defaultValue = "${allure.version}")
    protected String adaptorVersion;

    public void executeAspectJPlugin() throws MojoExecutionException, MojoFailureException {
        mavenProject.getDependencyArtifacts().addAll(getRequestedDependencyArtifacts());
        mavenProject.getArtifacts().add(getAspectsArtifact());

        executeAspectJPluginWithGoal("compile");
        executeAspectJPluginWithGoal("test-compile");
    }


    protected Artifact getAspectsArtifact() {
        return getArtifact("ru.yandex.qatools.allure", "allure-java-aspects", adaptorVersion, "jar");
    }

    protected List<Artifact> getRequestedDependencyArtifacts() {
        ArrayList<Artifact> artifacts = new ArrayList<>();
        artifacts.add(getArtifact("org.aspectj", "aspectjrt", aspectJVersion, "jar"));
        artifacts.add(getArtifact("org.modeshape", "modeshape-common", "3.2.0.Final", "jar"));
        return artifacts;
    }



    protected Plugin getAspectJPlugin() {
        return plugin(
                "org.codehaus.mojo",
                "aspectj-maven-plugin",
                "1.4",
                dependencies(
                        dependency("org.aspectj", "aspectjtools", aspectJVersion)
                )
        );
    }

    protected Xpp3Dom getAspectJPluginConfiguration() {
        return configuration(
                element("source", javaVersion),
                element("target", javaVersion),
                element("complianceLevel", javaVersion),
                element("aspectLibraries",
                        element("aspectLibrary",
                                element("groupId", "ru.yandex.qatools.allure"),
                                element("artifactId", "allure-java-aspects")
                        )
                )
        );
    }

    protected void executeAspectJPluginWithGoal(String goal) throws MojoExecutionException {
        executeMojo(
                getAspectJPlugin(),
                goal(goal),
                getAspectJPluginConfiguration(),
                executionEnvironment(mavenProject, mavenSession, pluginManager)

        );
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        executeAspectJPlugin();
    }
}
