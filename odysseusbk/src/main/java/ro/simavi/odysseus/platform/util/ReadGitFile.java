package ro.simavi.odysseus.platform.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.Base64;

@Getter
@Setter
@Component
public class ReadGitFile
{
    java.net.URL url = null;
    @Value("${mescobrad.git.plugin.url}")
    String urlString="https://git.siveco.ro/iacobc/ICS2/blob/master/Readme.md";

    @Value("${mescobrad.git.plugin.username}")
    String username ="iacobc";

    @Value("${mescobrad.git.plugin.pwd}")
    String password= "161281Gvm->";

    @Value("${mescobrad.git.plugin.file}")
    String file="gitFile.jar";

    public String getFileFromGit(){
        try {
            url = new java.net.URL(urlString);
            java.net.URLConnection uc;
            uc = url.openConnection();

            uc.setRequestProperty("X-Requested-With", "Curl");
            java.util.ArrayList<String> list = new java.util.ArrayList<String>();
            String userpass = username + ":" + password;
            String basicAuth = "Basic " + new String( Base64.getEncoder().encode(userpass.getBytes()));//needs Base64 encoder, apache.commons.codec
            uc.setRequestProperty("Authorization", basicAuth);

            BufferedReader reader = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null)
                file = file + line + "\n";
            //System.out.println(file);

        } catch (IOException e) {
            System.out.println("Wrong username and password");
        }
        return file;
    }

}
