package com.pyramid.utils;

import com.pyramid.infra.exceptions.GitUtilsException;
import java.io.File;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

public class GitUtils {

  private static final String ORIGIN = "origin/";

  private GitUtils() {
  }

  public static void sparseCheckout(File localPath, String repoUrl, String repoBranch,
      String repoDirectory) {
    try {
      Git repository = Git.cloneRepository()
          .setURI(repoUrl)
          .setDirectory(localPath)
          .setNoCheckout(true)
          .call();
      repository.checkout().setName(repoBranch)
          .setStartPoint(ORIGIN + repoBranch)
          .addPath(repoDirectory)
          .call();
      repository.getRepository().close();
    } catch (GitAPIException e) {
      e.printStackTrace();
      throw new GitUtilsException(
          "::: ERROR ::: sparseCheckout(File localPath, String repoUrl, String repoBranch, "
              + "String repoDirectory) ::: Could not perform sparse checkout.");
    }
  }
}
