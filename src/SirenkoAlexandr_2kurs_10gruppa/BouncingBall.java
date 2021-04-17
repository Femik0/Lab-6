package SirenkoAlexandr_2kurs_10gruppa;

import java.awt.*;
import java.awt.geom.Ellipse2D;


public class BouncingBall implements Runnable {
    private static final int MAX_RADIUS = 40;
    private static final int MIN_RADIUS = 3;
    private static final int MAX_SPEED = 15;

    private Field field;
    private int radius;
    private Color color;
    private boolean paused;

    private double x, y;

    private int speed;
    private double speedX;
    private double speedY;

    public void setPaused(){
        paused = true;
    }

    public void resumePaused(){
        paused = false;
    }

    public boolean isPaused() {
        return paused;
    }
    public Color getColor() {
        return color;
    }

    public BouncingBall(Field field){
        this.field = field;
        radius = new Double(Math.random()* //возвращает число на отрезке [0;1]
                (MAX_RADIUS - MIN_RADIUS)).intValue() + MIN_RADIUS;
        speed = new Double(Math.round(5*MAX_SPEED / radius)).intValue();
        if(speed>MAX_SPEED){
            speed = MAX_SPEED;
        }
        //направление скорости
        double angle = Math.random()*2* Math.PI;
        //составляющие скорости
        speedX = Math.cos(angle);
        speedY = Math.sin(angle);
        color = new Color((float)Math.random(),
                (float)Math.random(),(float)Math.random());
        Thread T = new Thread(this);
        T.start();
    }

    public void run(){
        try {
            while(true) {
                field.canMove(this);
                if (x + speedX <= radius) {
                    // Достигли левой стенки, отскакиваем право
                    speedX = -speedX;
                    x = radius;
                } else
                if (x + speedX >= field.getWidth() - radius) {
                    // Достигли правой стенки, отскок влево
                    speedX = -speedX;
                    x=new Double(field.getWidth()-radius).intValue();
                } else
                if (y + speedY <= radius) {
                    // Достигли верхней стенки
                    speedY = -speedY;
                    y = radius;
                } else
                if (y + speedY >= field.getHeight() - radius) {
                    // Достигли нижней стенки
                    speedY = -speedY;
                    y=new Double(field.getHeight()-radius).intValue();
                } else {
                    // Просто смещаемся
                    x += speedX;
                    y += speedY;
                }
                // Засыпаем на X миллисекунд, где X определяется
                // исходя из скорости
                // Скорость = 1 (медленно), засыпаем на 15 мс.
                // Скорость = 15 (быстро), засыпаем на 1 мс.
                Thread.sleep(16-speed);
            }
        } catch (InterruptedException ex) {
            // Если нас прервали, то ничего не делаем
            // и просто выходим (завершаемся)
        }

    }

    public void paint(Graphics2D canvas){
        canvas.setColor(color);
        canvas.setPaint(color);
        Ellipse2D.Double ball = new Ellipse2D.Double(x-radius, y-radius,
                2*radius, 2*radius);
        canvas.draw(ball);
        canvas.fill(ball);
    }
}