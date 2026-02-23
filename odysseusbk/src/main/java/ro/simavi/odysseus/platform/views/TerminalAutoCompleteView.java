package ro.simavi.odysseus.platform.views;

import org.primefaces.model.terminal.TerminalAutoCompleteModel;
import org.primefaces.model.terminal.TerminalCommand;
import org.springframework.stereotype.Component;

import javax.faces.view.ViewScoped;
import java.io.Serializable;

@Component
@ViewScoped
public class TerminalAutoCompleteView implements Serializable {

    private TerminalAutoCompleteModel autoCompleteModel;

    public TerminalAutoCompleteView() {
        this.autoCompleteModel = buildAutoCompleteModel();
    }

    private TerminalAutoCompleteModel buildAutoCompleteModel() {
        TerminalAutoCompleteModel model = new TerminalAutoCompleteModel();

        TerminalCommand git = model.addCommand("git");

        git.addArgument("checkout");
        git.addArgument("commit");
        git.addArgument("status");
        git.addArgument("pull");
        git.addArgument("push").addArgument("origin").addArgument("master");

        TerminalCommand svn = model.addCommand("svn");

        svn.addArgument("commit");
        svn.addArgument("checkout");
        svn.addArgument("status");
        svn.addArgument("update");

        return model;
    }

    public TerminalAutoCompleteModel getAutoCompleteModel() {
        return autoCompleteModel;
    }

    public String handleCommand(String command, String[] params) {
        StringBuilder response = new StringBuilder("The command you entered was: '").append(command);

        for (String param : params) {
            response.append(" ").append(param);
        }

        return response.append("'").toString();
    }

}