package sample.client;

import javafx.application.Platform;

public class ClientPresenter {
    private static ClientPresenter clientPresenter = new ClientPresenter();
    ClientController clientController;

    private ClientPresenter() {
    }

    public static  ClientPresenter getInstance() {
        return clientPresenter;
    }

    public void handleResult(String s) {
        Platform.runLater(()->clientController.setResult(s));
    }

    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }
}
