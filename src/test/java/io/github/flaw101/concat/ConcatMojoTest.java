/**
 * MIT License
 * <p>
 * Copyright (c) 2018 Darren Forsythe
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.flaw101.concat;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.codehaus.plexus.util.ExceptionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.charset.Charset;

public class ConcatMojoTest extends AbstractMojoTestCase {
    private static String NEWLINE = System.getProperty("line.separator");

    /**
     * {@inheritDoc}
     */
    @Override
    @Before
    protected void setUp() throws Exception {
        super.setUp();
        FileUtils.deleteQuietly(new File("target/concatfile.output"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @After
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testConcat() throws Exception {
        execute("src/test/resources/test-pom.xml", "concat", "target/concatfile.output",
                "file1file2file3");
    }

    @Test
    public void testConcat_Directory() throws Exception {
        execute("src/test/resources/test-pom-directory.xml", "concat",
                "target/concatfile.output", "file1file2file2bfile3start");
    }

    @Test
    public void testConcat_Directory_StartingFile() throws Exception {
        execute("src/test/resources/test-pom-directory-with-starting-file.xml", "concat",
                "target/concatfile.output", "startfile1file2file2bfile3");
    }

    @Test
    public void testConcatShouldNotDeleteTarget() throws Exception {
        FileUtils.copyFile(new File("src/test/resources/testfiles/file_1.input"),
                new File("target/concatfile.output"));
        execute("src/test/resources/test-pom.xml", "concat", "target/concatfile.output",
                "file1file1file2file3");
    }

    @Test
    public void testConcatDeleteTargetFile() throws Exception {
        FileUtils.copyFile(new File("src/test/resources/testfiles/file_1.input"),
                new File("target/concatfile.output"));
        execute("src/test/resources/test-delete-target-file-pom.xml", "concat",
                "target/concatfile.output", "file1file2file3");
    }

    @Test
    public void testConcatNewline() throws Exception {
        final String expectedResult = "file1" + NEWLINE + "file2" + NEWLINE + "file3"
                + NEWLINE;
        execute("src/test/resources/test-newline-pom.xml", "concat",
                "target/concatfile.output", expectedResult);

    }

    @Test
    public void testMissingOutputParameter() throws Exception {
        try {
            execute("src/test/resources/test-missing-output-pom.xml", "concat", "bla",
                    "nothing");
        } catch (final Exception e) {
            assertEquals(MojoExecutionException.class, e.getClass());
        }
    }

    @Test
    public void testMissingConcatParameter() throws Exception {
        try {
            execute("src/test/resources/test-missing-concat-pom.xml", "concat", "bla",
                    "nothing");
        } catch (final Exception e) {
            ExceptionUtils.printRootCauseStackTrace(e);
            assertEquals(MojoExecutionException.class, e.getClass());
        }
    }

    @Test
    public void testDirectoryCompetingArgs() throws Exception {
        try {
            execute("src/test/resources/test-pom-directory-competing-args.xml", "concat",
                    "bla", "nothing");
        } catch (final Exception e) {
            ExceptionUtils.printRootCauseStackTrace(e);
            assertEquals(MojoExecutionException.class, e.getClass());
        }
    }

    @Test
    public void testListCompetingArgs() throws Exception {
        try {
            execute("src/test/resources/test-pom-competing-arguments.xml", "concat",
                    "bla", "nothing");
        } catch (final Exception e) {
            ExceptionUtils.printRootCauseStackTrace(e);
            assertEquals(MojoExecutionException.class, e.getClass());
        }
    }

    @Test
    public void testDirectoryMissingDirectory() throws Exception {
        try {
            execute("src/test/resources/test-pom-directory-missing-directory.xml",
                    "concat", "bla", "nothing");
        } catch (final Exception e) {
            ExceptionUtils.printRootCauseStackTrace(e);
            assertEquals(MojoExecutionException.class, e.getClass());
        }
    }

    @Test
    public void testNotADirectory() throws Exception {
        try {
            execute("src/test/resources/test-pom-directory-not-a-directory.xml", "concat",
                    "bla", "nothing");
        } catch (final Exception e) {
            ExceptionUtils.printRootCauseStackTrace(e);
            assertEquals(MojoExecutionException.class, e.getClass());
        }
    }


    @Test
    public void testSemVer() throws Exception {

        execute("src/test/resources/test-pom-semver.xml",
                "concat", "target/concatfile.output", "anotherfirsttwothreefinal");

    }

    private void execute(final String pomPath, final String mojo, final String resultFile,
                         final String expectedResult) throws Exception {
        final File pom = getTestFile(pomPath);
        assertNotNull(pom);
        assertTrue(pom.exists());

        final ConcatMojo concatMojo = (ConcatMojo) lookupMojo(mojo, pom);
        assertNotNull(concatMojo);
        concatMojo.execute();

        final File outputFile = getTestFile(resultFile);
        assertNotNull(outputFile);
        assertTrue(outputFile.exists());
        final String output = FileUtils.readFileToString(outputFile,
                Charset.forName("UTF-8"));
        assertEquals(expectedResult, output);
    }

}
