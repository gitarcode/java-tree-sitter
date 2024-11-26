package ch.usi.si.seart.treesitter.version;

/**
 * Utility used for obtaining the current version of the {@code ${project.artifactId}} codebase.
 * Unlike attempting to access the "Implementation-Version" manifest attribute from the JAR file,
 * this utility will work even if the {@link ClassLoader} does not expose the manifest metadata, or
 * if the code is not packaged in a JAR file.
 *
 * @author Ozren Dabić
 * @since 1.11.0
 */
public final class Version {

  private static final String VALUE = "${project.version}";

  /**
   * Get the current version of {@code ${project.artifactId}}.
   *
   * @return the semantic version string
   */
  public static String getVersion() {
    return VALUE;
  }

  private Version() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }
}
