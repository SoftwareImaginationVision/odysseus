package ro.simavi.odysseus.platform.util;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import java.io.File;
import java.io.IOException;

public class GitRepository
{
    public static void getFile(String fileName) {
        try {
            //Provide the correct Repository link and the directory to store the repository on the computer.
            String repositoryLink = "https://git.siveco.ro/iacobc/ICS2.git";
            String directory = "C:\\GitRepos\\Iacobc";

            CloneCommand cloneCommand = Git.cloneRepository();
            cloneCommand.setURI( "https://git.siveco.ro/iacobc/ICS2.git" );
            cloneCommand.setCredentialsProvider( new UsernamePasswordCredentialsProvider( "iacobc", "161281Gvm->" ) );
            cloneCommand.setDirectory( new File(directory) );
            cloneCommand.call();

            //Git.cloneRepository().setURI(repositoryLink).setDirectory(new File(directory)).call();

            System.out.println("Successfully downloaded the repository!");
        }
        catch( GitAPIException | JGitInternalException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static String readContentOfFileAtCommit(String commitStr, String fileName)
            throws IOException
    {

        String content = null;
        ObjectId lastCommitId = null;
        Git git = Git.open( new File( fileName ) );
        Repository currentRepo = git.getRepository();
        if(commitStr == null)
        {
             lastCommitId = currentRepo.resolve( Constants.HEAD );
        }
        else{
             lastCommitId = currentRepo.resolve( commitStr );
        }
        try ( RevWalk revWalk = new RevWalk(currentRepo)) {
            RevCommit commit = revWalk.parseCommit(lastCommitId);
            RevTree tree = commit.getTree();

            try ( TreeWalk treeWalk = new TreeWalk(currentRepo)) {
                treeWalk.addTree(tree);
                treeWalk.setRecursive(true);
                treeWalk.setFilter( PathFilter.create(fileName));
                if (!treeWalk.next()) {
                    throw new IllegalStateException("Did not find expected file:" + fileName);
                }

                ObjectId objectId = treeWalk.getObjectId(0);
                ObjectLoader loader = currentRepo.open(objectId);
                content = new String(loader.getBytes());
            }

            revWalk.dispose();
        }

        return content;
    }
}
