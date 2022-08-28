package de.isuewo;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        String gitPath = "<path to git repository>";
        String name = "OverimaginedShaders_";
        String version = "r1.3_dev2-1.1_Beta9";
        String oldBranch = "main";
        String newBranch = "testing";

        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository = builder.setGitDir(new File(gitPath + ".git")).readEnvironment().findGitDir().build();

        Git git = new Git(repository);
        try {
            List<DiffEntry> diff = git.diff().setOldTree(prepareTreeParser(repository, oldBranch)).setNewTree(prepareTreeParser(repository, newBranch)).setOutputStream(new FileOutputStream(name + version + ".patch")).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    private static AbstractTreeIterator prepareTreeParser(Repository repository, String s) {
        try {
            ObjectId objectId = repository.resolve(s + "^{tree}");
            return new CanonicalTreeParser(null, repository.newObjectReader(), objectId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
