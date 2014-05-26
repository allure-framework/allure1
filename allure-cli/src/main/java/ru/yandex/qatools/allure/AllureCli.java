package ru.yandex.qatools.allure;

import io.airlift.command.*;
import org.apache.commons.io.IOUtils;
import ru.yandex.qatools.allure.config.AllureConfig;
import ru.yandex.qatools.allure.data.AllureReportGenerator;

import javax.inject.Inject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 23.05.14
 */
@Command(name = "allure", description = "Allure report generation utility")
public class AllureCli {

    private static final String REPORT_FACE_DIRECTORY = "allure-report-face";

    private static final String DEFAULT_OUTPUT_PATH = "output";

    @Inject
    public HelpOption helpOption;

    @Option(type = OptionType.COMMAND,
            name = {"-o", "--outputPath"},
            description = "Directory to output report files to (default is \"" + DEFAULT_OUTPUT_PATH + "\")")
    public String outputPath = DEFAULT_OUTPUT_PATH;

    @Arguments(title = "input directories",
            description = "A list of input directories to be processed")
    public List<String> inputPaths = new ArrayList<>();

    @Option(name = {"--version"})
    public boolean version;

    @SuppressWarnings("unused")
    @Option(name = {"-v", "--verbose"})
    public boolean verbose;

    public static void main(String[] args) {
        AllureCli allure;
        try {
            allure = SingleCommand.singleCommand(AllureCli.class).parse(args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            System.exit(1);
            return;
        }

        if (allure.helpOption.showHelpIfRequested()) {
            return;
        }

        allure.run();
    }

    public void run() {
        if (version) {
            version();
            return;
        }

        generate();
    }

    /**
     * Generate allure report to outputPath using data from inputPaths
     */
    public void generate() {
        try {
            File outputDirectory = new File(outputPath);
            if (!outputDirectory.exists() && !outputDirectory.mkdirs()) {
                throw new RuntimeException("Can't create output directory " + outputPath);
            }

            List<File> inputDirectories = new ArrayList<>();
            for (String inputPath : inputPaths) {
                File inputDirectory = new File(inputPath);
                if (inputDirectory.exists() && inputDirectory.canRead()) {
                    inputDirectories.add(inputDirectory);
                }
            }

            AllureReportGenerator generator = new AllureReportGenerator(
                    inputDirectories.toArray(new File[inputDirectories.size()])
            );

            generator.generate(outputDirectory);

            unpackReport(currentJar(), REPORT_FACE_DIRECTORY, outputDirectory);
            System.out.println(String.format(
                    "Successfully generated report to %s.",
                    outputDirectory.getAbsolutePath()
            ));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Unpack files from jar by path
     *
     * @param jar             jar to unpack
     * @param path            path in jar to be unpacked
     * @param outputDirectory unpack to this directory
     * @throws IOException
     */
    private static void unpackReport(JarFile jar, String path, File outputDirectory) throws IOException {
        Enumeration entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry file = (JarEntry) entries.nextElement();
            if (file.getName().startsWith(path)) {
                System.out.println("copy " + file.getName());
                String newFileName = file.getName().replace(path, "");
                if (newFileName.length() > 0) {
                    String newFilePath = outputDirectory + newFileName;
                    System.out.println("copy to " + newFilePath);

                    File f = new File(newFilePath);

                    if (f.exists()) {
                        continue;
                    }

                    if (file.isDirectory() && !f.mkdirs()) {
                        throw new RuntimeException(String.format("Can't create directory <%s>", f.getPath()));
                    }

                    if (file.isDirectory()) {
                        continue;
                    }

                    copy(jar, file, f);
                }
            }
        }
    }

    /**
     * Copy JarEntry to file
     *
     * @param jar  from this archive will be copied
     * @param from JarEntry to copy
     * @param to   in which will be copied
     * @throws IOException
     */
    private static void copy(JarFile jar, JarEntry from, File to) throws IOException {
        try (InputStream is = jar.getInputStream(from); FileOutputStream os = new FileOutputStream(to)) {
            IOUtils.copy(is, os);
        }
    }

    /**
     * Return JarFile object for the jar file CLI resides in 
     * @return current Jar file
     * @throws IOException
     */
    public static JarFile currentJar() throws IOException {
        String path = AllureCli.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        return new JarFile(path);
    }

    /**
     * Show CLI version
     */
    public void version() {
        System.out.println(AllureConfig.newInstance().getVersion());
    }
}
