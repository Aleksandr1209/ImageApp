package src.main;

import src.main.controller.ImageController;
import src.main.model.ImageModel;
import src.main.view.ImageView;

public class App {
    public static void main(String[] args) {
        ImageModel model = new ImageModel();
        ImageView view = new ImageView();
        new ImageController(model, view);
    }
}