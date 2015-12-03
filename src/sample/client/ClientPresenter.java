package sample.client;

import javafx.application.Platform;

public class ClientPresenter {
    private static ClientPresenter clientPresenter = new ClientPresenter();
    public static  ClientPresenter getInstance() {
        return clientPresenter;
    }
    private ClientPresenter() {
    }

    public void handleResult(String s) {
        Platform.runLater(()->clientController.setResult(s));
    }

    ClientController clientController;


    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }
}
