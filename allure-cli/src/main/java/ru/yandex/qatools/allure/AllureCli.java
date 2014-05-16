package ru.yandex.qatools.allure;

import io.airlift.command.*;
import io.airlift.command.Cli.CliBuilder;
import ru.yandex.qatools.allure.data.AllureReportGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.Manifest;

public class AllureCli {

    private static final String DEFAULT_OUTPUT_PATH = "output";
    private static final String REPORT_FACE_DIRECTORY = "allure-report-face";

    public static void main(String[] args) {

        final CliBuilder<Runnable> builder = Cli.<Runnable>builder("allure")
                .withDescription("Allure command line client")
                .withDefaultCommand(Help.class)
                .withCommands(Help.class, GenerateCommand.class, ShowVersionCommand.class);

        final Cli<Runnable> parser = builder.build();

        parser.parse(args).run();

    }

    @Command(name = "generate", description = "Generate HTML report from XML files")
    public static class GenerateCommand implements Runnable {

        @Option(type = OptionType.COMMAND, name = {"-o", "--outputPath"}, description = "Directory to output report files to (default is \"" + DEFAULT_OUTPUT_PATH + "\")")
        public String outputPath = DEFAULT_OUTPUT_PATH;

        @Arguments(title = "inputPaths", required = true, description = "A list of input directories to be processed")
        public List<String> inputPaths;

        public void run() {

            try {
                final File outputDirectory = new File(outputPath);
                if (!outputDirectory.exists()) {
                    outputDirectory.mkdir();
                }
                final List<File> inputDirectories = new ArrayList<>();
                for (final String inputDirectory : inputPaths) {
                    inputDirectories.add(new File(inputDirectory));
                }
                final AllureReportGenerator generator = new AllureReportGenerator(inputDirectories.toArray(new File[inputDirectories.size()]));
                generator.generate(outputDirectory);
                final String reportFaceWildcard = REPORT_FACE_DIRECTORY + File.separator + ".*";
                copyStaticReportData(getCurrentJarFilePath(), outputDirectory, reportFaceWildcard);
                System.out.println("Successfully generated report to " + outputDirectory.getAbsolutePath() + ".");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        private static void copyStaticReportData(final File currentJarFile, final File outputDirectory, final String wildcard) throws IOException {
            final JarFile jar = new java.util.jar.JarFile(currentJarFile);
            final Enumeration entries = jar.entries();
            while (entries.hasMoreElements()) {
                final JarEntry file = (java.util.jar.JarEntry) entries.nextElement();
                if (file.getName().matches(wildcard)) {
                    final String newFileName = file.getName().replace(REPORT_FACE_DIRECTORY + File.separator, "");
                    if (newFileName.length() > 0) {
                        final String newFilePath = outputDirectory + File.separator + newFileName;
                        final File f = new File(newFilePath);
                        if (file.isDirectory() && !f.exists()) {
                            f.mkdir();
                            continue;
                        }
                        final InputStream inputStream = jar.getInputStream(file);
                        final FileOutputStream fileOutputStream = new FileOutputStream(f);
                        while (inputStream.available() > 0) {
                            fileOutputStream.write(inputStream.read());
                        }
                        fileOutputStream.close();
                        inputStream.close();
                    }
                }
            }
        }

        private static File getCurrentJarFilePath() {
            return new File(AllureCli.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        }
    }

    @Command(name = "version", description = "Show client version")
    public static class ShowVersionCommand implements Runnable {

        @Override
        public void run() {
            try {
                final URL url = ((URLClassLoader) getClass().getClassLoader()).findResource("META-INF/MANIFEST.MF");
                final Manifest manifest = new Manifest(url.openStream());
                final String specificationVersion = manifest.getMainAttributes().getValue("Specification-Version");
                if (specificationVersion != null) {
                    System.out.println(specificationVersion);
                } else {
                    System.out.println("Failed to load version from MANIFEST.MF. This is probably a bug.");
                }
            } catch (IOException e) {
                System.out.println("Failed to load version because of exception.");
                throw new RuntimeException(e);
            }
        }
    }


}
