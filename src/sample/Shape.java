package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;

import java.awt.*;
import java.util.stream.Stream;

public class Shape {

    private double w1;
    private double w2;
    private double h;
    private double r;
    private double zoom;
    private double offsetX = 0;
    private double offsetY = 0;
    private ObservableList<Сoordinate> coordinate;
    public ObservableList<Сoordinate> getCoordinate() {
        return coordinate;
    }


    public void setOffsetX(double offsetX) {
        this.offsetX = offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(double offsetY) {
        this.offsetY = offsetY;
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    public double getW1() {
        return w1;
    }

    public void setW1(double w1) {
        this.w1 = w1;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }


    public double getW2() {
        return w2;
    }

    public void setW2(double w2) {
        this.w2 = w2;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }
    public void setСoordinates(){
        coordinate = FXCollections.observableArrayList();
        coordinate.add(new Сoordinate(-w1 / 2, h / 2));
        coordinate.add(new Сoordinate(w1 / 2, h / 2));
        coordinate.add(new Сoordinate(w1 / 2,  -h / 2));
        coordinate.add(new Сoordinate(-w1 / 2 - w2, -h / 2));
    }

    void Render(GraphicsContext cxt, Canvas canvas, Image image) {
        setСoordinates();
        //Получить dpi экрана
        double dpi = Toolkit.getDefaultToolkit().getScreenResolution();
        double zoom = dpi / 2.54 * getZoom() / 100;
        cxt.save(); //Сохранить состояние матрицы
        //Матрица преобразования
        Affine transform = cxt.getTransform();
        //Выровнять по центру
        transform.appendTranslation(canvas.getWidth() /2 + offsetX, canvas.getHeight() / 2 + offsetY);
        //Увеличили масштаб
        transform.appendScale(zoom, -zoom);
        //Сохранить изменения
        cxt.setTransform(transform);
        //Сузить линию
        cxt.setLineWidth(4. / zoom);
        cxt.setStroke(Color.RED);
        cxt.strokeArc(
                w1 / 2 - r,
                -h / 2 - r,
                r * 2,
                r * 2,
                -90,
                -90,
                ArcType.ROUND
        );

        Stream.of(
                Color.ORANGE,
                new ImagePattern(image, 0, 0, 4. / zoom, 1. / zoom, false)
        ).forEach(paint ->{
            cxt.setFill(paint);
            cxt.fillArc(
                    w1 / 2 - r,
                    -h / 2 - r,
                    r * 2,
                    r * 2,
                    -90,
                    -90,
                    ArcType.ROUND
            );
        });

        cxt.setFill(new ImagePattern(image, 0, 0, 4. / zoom, 1. / zoom, false));
        cxt.fillArc(
                w1 / 2 - r,
                -h / 2 - r,
                r * 2,
                r * 2,
                -90,
                -90,
                ArcType.ROUND
        );
        cxt.setStroke(Color.NAVY);
        cxt.strokePolygon(
                new double[]{-w1 / 2, w1 / 2, w1 / 2, -w1 / 2 - w2},
                new double[]{h / 2, h / 2, -h / 2, -h / 2},
                4);

        cxt.strokeOval(
                -w1 /2 - r / 2,
                h/2 - r / 2,
                r,
                r
        );

        //Текст
        for (var coor: coordinate) {
            RenderСoordinates(cxt, transform, coor, zoom, String.format("(%.1f; %.1f)", coor.getX(), coor.getY()));
        }
        cxt.restore(); //Вызвать обратно
    }

    private void RenderСoordinates(GraphicsContext cxt, Affine transform, Сoordinate coor, double zoom, String text){
        cxt.save();
        transform = cxt.getTransform();
        double x = coor.getX();
        double y = coor.getY();
        transform.appendTranslation(x, y); //Сдвинуть к координатам рисовку
        transform.appendScale(1. / zoom, 1. / -zoom); //Заскуйлить в нужный формат
        cxt.setTransform(transform);
        cxt.setFill(Color.RED);
        cxt.setFont(Font.font("Times New Roman", FontWeight.BOLD, 16));
        cxt.setTextAlign(TextAlignment.CENTER);
        if(y > 0){
            cxt.fillText(text, 0, -15);
        }
        else {
            cxt.fillText(text, 0, 20);
        }
        cxt.fillOval(-5, -5, 10, 10);
        cxt.restore();
    }
}
