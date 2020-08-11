package controller;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

public class CustomListView extends Application {
    private static class CustomThing {
        private String name;
        private int price;
        public String getName() {
            return name;
        }
        public int getPrice() {
            return price;
        }
        public CustomThing(String name, int price) {
            super();
            this.name = name;
            this.price = price;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ObservableList<CustomThing> data = FXCollections.observableArrayList();
        data.addAll(new CustomThing("Cheese", 123), new CustomThing("Horse", 456), new CustomThing("Jam", 789));
        for (int i = 0; i < data.size(); i++) {
            System.out.println(data.get(i));
        }


        final ListView<CustomThing> listView = new ListView<CustomThing>(data);

        listView.setCellFactory(new Callback<ListView<CustomThing>, ListCell<CustomThing>>() {
            public CustomListCell call(ListView<CustomThing> listView) {
                System.out.println("in call");
                return new CustomListCell();
            }
        });
        System.out.println("outside call");
        StackPane root = new StackPane();
        System.out.println("stackpane");
        root.getChildren().add(listView);
        System.out.println("added listview");
        primaryStage.setScene(new Scene(root, 200, 250));
        System.out.println("scene set");
        primaryStage.show();
        System.out.println("showed");
    }

    private class CustomListCell extends ListCell<CustomThing> {

        private HBox content;
        private Text name;
        private Text price;

        public CustomListCell() {
            super();
            System.out.println("in constructor");
            name = new Text();
            price = new Text();
            VBox vBox = new VBox(name, price);
            content = new HBox(new Label("[Graphic]"), vBox);
            content.setSpacing(10);
        }

        @Override
        protected void updateItem(CustomThing item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null && !empty) { // <== test for null item and empty parameter
                System.out.println(item.getName());
                name.setText(item.getName());
                price.setText(String.format("%d $", item.getPrice()));
                setGraphic(content);
            } else {
                setGraphic(null);
            }
        }
    }

}