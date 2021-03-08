package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    public Slider sidZoom;
    public TableView<Сoordinate> tblOutput;
    public TableColumn<Сoordinate, Double> columnX;
    public TableColumn<Сoordinate, Double> columnY;
    @FXML
    Canvas mainCanvas;
    @FXML
    Slider sidW1;
    @FXML
    Slider sidW2;
    @FXML
    Slider sidH;
    @FXML
    Slider sidR;

    Shape shape;
    GraphicsContext cxt;
    private ImagePattern imagePattern;
    private Image image;

    private double offsetX = 0;
    private double offsetY = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        shape = new Shape();
        cxt = mainCanvas.getGraphicsContext2D();

        image = new Image("pattern1.png");
        //imagePattern = new ImagePattern(image, 0, 0, 4, 1, true);
        Upstate();
        Render();
        sidW1.valueProperty().addListener((ov, old_val, new_val) -> {
            Upstate();
            Render();
        });

        sidW2.valueProperty().addListener((ov, old_val, new_val) -> {
            Upstate();
            Render();
        });

        sidH.valueProperty().addListener((ov, old_val, new_val) -> {
            Upstate();
            Render();
        });
        sidR.valueProperty().addListener((ov, old_val, new_val) -> {
            Upstate();
            Render();
        });
        sidZoom.valueProperty().addListener((ov, old_val, new_val) -> {
            Upstate();
            Render();
        });
    }

    void Render(){
        cxt.setFill(Color.WHITE);
        cxt.fillRect(0,0,mainCanvas.getHeight(), mainCanvas.getHeight());
        shape.Render(cxt, mainCanvas, image);
    }

    void Upstate(){
        if(sidH.getValue() < sidR.getValue()) {
            sidR.setValue(sidH.getValue());
        }
        if(sidW1.getValue() + sidW2.getValue() < sidR.getValue()){
            sidR.setValue(sidW1.getValue() + sidW2.getValue());
        }
        shape.setW1(sidW1.getValue());
        shape.setH(sidH.getValue());
        shape.setR(sidR.getValue());
        shape.setW2(sidW2.getValue());
        shape.setZoom(sidZoom.getValue());
        shape.setOffsetX(offsetX);
        shape.setOffsetY(offsetY);
        shape.setСoordinates();
        columnX.setCellValueFactory(new PropertyValueFactory<Сoordinate, Double>("x"));
        columnY.setCellValueFactory(new PropertyValueFactory<Сoordinate, Double>("y"));
        tblOutput.setItems(shape.getCoordinate());
    }

    public void onMouseCanvas(MouseEvent mouseEvent) {
        offsetX += mouseEvent.getX() - pressedX;
        offsetY += mouseEvent.getY() - pressedY;
        pressedX = mouseEvent.getX();
        pressedY = mouseEvent.getY();
        Upstate();
        Render();
    }

    double pressedX;
    double pressedY;

    public void onMouseClick(MouseEvent mouseEvent) {
        pressedX = mouseEvent.getX();
        pressedY = mouseEvent.getY();
    }
}
