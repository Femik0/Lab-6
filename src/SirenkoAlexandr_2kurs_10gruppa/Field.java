package SirenkoAlexandr_2kurs_10gruppa;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Field extends JPanel {
    // Динамический список скачущих мячей
    private ArrayList<BouncingBall> balls = new ArrayList<BouncingBall>(10);
    // Класс таймер отвечает за регулярную генерацию события типа ActionEvent
    // При создании его экземпляра используется анонимный класс,
    // реализующий интерфейс ActionListener
    private Timer repaintTimer = new Timer(10, new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
            Field.this.repaint();
        }
    });
    // Конструктор класса BouncingBall
    public Field(){
        // Установить цвет заднего фона белым
        setBackground(Color.BLACK);
        // Запустить таймер перерисовки области
        repaintTimer.start();
    }
    // Метод добавления нового мяча в список
    public void addBall(){
        // Заключается в добавлении в список нового экземпляра BouncingBall
        // Всю инициализацию BouncingBall выполняет сам в конструкторе
        balls.add(new BouncingBall(this));
    }
    // Унаследованный от JPanel метод перерисовки компонента
    public void paintComponent(Graphics gr) {
        // Вызвать версию метода, унаследованную от предка
        super.paintComponent(gr);
        Graphics2D canvas = (Graphics2D) gr;
        // Последовательно запросить прорисовку от всех мячей, хранимых в списке
        for (BouncingBall ball: balls) {
            ball.paint(canvas);
        }
    }
    // Синхронизированный, т.е. только 1 поток может одновременно быть внутри
    public synchronized void pause() {
        for (BouncingBall ball: balls){
            ball.setPaused();
        }
    }
    public synchronized void resume() {
        for (BouncingBall ball: balls){
            ball.resumePaused();
            notify();
        }
    }
    public synchronized void pauseGreen() {
        for (BouncingBall ball: balls) {
            if(ball.getColor().getGreen() >= 2*(ball.getColor().getBlue() + ball.getColor().getRed())) {
                ball.setPaused();
            }
        }
    }
    // Синхронизированный метод проверки, может ли мяч двигаться
    // (не включен ли режим паузы?)
    public synchronized void canMove(BouncingBall ball)
            throws InterruptedException{
        if (ball.isPaused()) {
            // Если режим паузы включен, то поток, зашедший внутрь
            // данного метода, засыпает
            wait();
        }
    }

}