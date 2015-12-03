package sample.server;

import javafx.application.Platform;

public class ServerPresenter {
    private static ServerPresenter serverPresenter = new ServerPresenter();
    public static  ServerPresenter getInstance() {
        return serverPresenter;
    }
    private ServerPresenter() {
    }

    public void handleResult(String s) {
        Platform.runLater(()-> serverController.setResult(s));
    }

    ServerController serverController;

    public void setServerController(ServerController serverController) {
        this.serverController = serverController;
    }
}
