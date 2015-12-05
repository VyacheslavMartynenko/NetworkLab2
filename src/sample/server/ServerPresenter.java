package sample.server;

import javafx.application.Platform;

public class ServerPresenter {
    private static ServerPresenter serverPresenter = new ServerPresenter();
    private ServerController serverController;

    private ServerPresenter() {}

    public static  ServerPresenter getInstance() {
        return serverPresenter;
    }

    public void handleResult(String s) {
        Platform.runLater(()-> serverController.setResult(s));
    }

    public void setServerController(ServerController serverController) {
        this.serverController = serverController;
    }
}
