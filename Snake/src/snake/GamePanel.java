
package snake;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;


public class GamePanel extends JPanel implements ActionListener{
    // Bien tao luoi va toa do : static final - bien co dinh ton tai cho tat ca doi tuong cua lop
    static final int SCREEN_WIDTH = 700;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE*UNIT_SIZE);
    // Xu ly toc do Delay cang lon ran chay cang cham
    public static int DELAY = 100;
    // Bien ve ran
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 1; // Chieu dai ban dau cua ran
    int appleEaten = 0; // Dem so tao an duoc
    // Bien tao ra qua tao
    int appleX;
    int appleY;
    // Bien check huong di chuyen
    char direction = 'R';
    // Thiet lap ban dau la chua chay
    boolean running = false;
    // Goi thoi gian va bien ngau nhien
    Timer timer;
    Random random;
    
    // Panel chinh - Constructor
    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        
        startGame(); // Bat dau game
        
    }
    
    public void startGame(){
        newApple(); // Tao qua tao moi
        running = true; // Bat dau game la chay 
        bodyParts = 1;
        appleEaten = 0;
        direction = 'R';
        for (int i = 0;i <bodyParts; i++) {
            x[i] = 75 - i * 25;
            y[i] = 25;
        } 
        // Dem thoi gian
        timer = new Timer(DELAY, this);
        timer.start();
    }
    // Ve cac vat lieu
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    // Ham ve cac bien can thiet
    public void draw(Graphics g) {
        // draw grid, apple, and snake
      if(running){
//           g.setColor(Color.WHITE);
        // Ve luoi
        for(int i=0; i < SCREEN_WIDTH / UNIT_SIZE; i++) {
            g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
        }
        
        for(int i=0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
            g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
        }
        // Ve tao
        g.setColor(Color.red);
        g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
        if(appleEaten % 3 == 2){
            g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
        }
        
        // Ve ran
        for(int i = 0; i < bodyParts; i++){
            // Dau ran
            if(i == 0){
                g.setColor(Color.GREEN);
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            } 
            // Than ran
            else {
                g.setColor(new Color(45, 180, 0));
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
        }
        
        // Ve diem
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: "+appleEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+appleEaten))/2, g.getFont().getSize());
      } 
        else{
            gameOver(g);
        }
    }
    // Ham tao qua tao moi
    public void newApple(){
        // tao se xuat hien tai mot vi tri ngau nhien trong khung game
        appleX = random.nextInt((int) SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        appleY = random.nextInt((int) SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
        
    }
    // Ham di chuyen
    public void move() {
        // Dich chuyen than ran
        for(int i=bodyParts; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        // Di chuyen theo nut dieu huong cac mui ten co the them cac phim thay the :)) W A S D
        switch(direction){
            case 'U':
                y[0] -= UNIT_SIZE;
                break;
            case 'D':
                y[0] += UNIT_SIZE;
                break;
            case 'L':
                x[0] -= UNIT_SIZE;
                break;
            case 'R':
                x[0] += UNIT_SIZE;
                break;
        }  
    }
    // Ham check neu an duoc tao thi se tao them qua tao moi xuat hien ngau nhien tren khung game
    public void checkApple() {
        if((x[0] == appleX) && (y[0] == appleY)){
            bodyParts++;
            appleEaten++;
            DELAY -= 5;
            this.timer.setDelay(DELAY);
            if(appleEaten % 3 == 0){
                bodyParts += 1;
                appleEaten += 3;
            }
            newApple();
        }
    }
    // Ham kiem tra neu ran cham vao than hoac tuong 
    public void checkCollisions() {
        // check if head collides with the body
        for(int i = bodyParts; i > 0; i--){
            if((x[0] == x[i]) && (y[0] == y[i])){
                running = false;
                break;
            }
        }
        // ran di xuyen tuong
        if(x[0] < 0) x[0] = SCREEN_WIDTH-UNIT_SIZE;
        if(x[0] > SCREEN_WIDTH) x[0] = 0;
        if(y[0] < 0) y[0] = SCREEN_HEIGHT-UNIT_SIZE;
        if(y[0] > SCREEN_HEIGHT) y[0] = 0; 
        
        if(!running) timer.stop();
    }
    // Ran chet hien diem + Game Over !!!
    JButton btnreset; // Tao nut reset
    JButton btnclose; // Tao nut close
    public void gameOver(Graphics g) {
        //Score
        g.setColor(Color.red);
        g.setFont( new Font("Ink Free",Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: "+appleEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+appleEaten))/2, g.getFont().getSize());
        //Game Over text
        g.setColor(Color.red);
        g.setFont( new Font("Ink Free",Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
        // Tao nut reset
        btnreset = new JButton("Reset");
        btnreset.setSize(200, 50);
        btnreset.setBackground(Color.BLACK);
        btnreset.setForeground(Color.GREEN);
        btnreset.setFont(new Font("Ink Free",Font.BOLD,25) );
        btnreset.setFocusable(false);
        btnreset.setLocation(100, 400);
        btnreset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnresetClick(e);
            }
        });
        // Tao nut close
        btnclose = new JButton("Close");
        btnclose.setSize(200, 50);
        btnclose.setBackground(Color.BLACK);
        btnclose.setForeground(Color.GREEN);
        btnclose.setFont(new Font("Ink Free", Font.BOLD, 25));
        btnclose.setLocation(400, 400);
        btnclose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnClose(e);
            }

            
        });
        add(btnreset);
        add(btnclose);
        
        this.btnreset.setVisible(true);
        this.btnclose.setVisible(true);
    }
    // reset game
    private void  btnresetClick(java.awt.event.ActionEvent a){
        btnreset.setVisible(false);
        DELAY = 100;
        startGame();
        repaint();
    }
    // Close Game
    private void btnClose(ActionEvent e) {
        btnclose.setVisible(false);
        System.exit(0);
        //repaint();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move(); // Bat dau di chuyen
            checkApple(); 
            checkCollisions();
        }
        repaint(); // Ve lai

    }
    
    public class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()){
                // Dieu huong con ran
                case KeyEvent.VK_LEFT: case KeyEvent.VK_A:
                    if(direction != 'R') direction = 'L';
                    break;
                
                case KeyEvent.VK_RIGHT: case KeyEvent.VK_D:
                    if(direction != 'L') direction = 'R';
                    break;
                    
                case KeyEvent.VK_UP: case KeyEvent.VK_W:
                    if(direction != 'D') direction = 'U';
                    break;
                    
                case KeyEvent.VK_DOWN: case KeyEvent.VK_S:
                    if(direction != 'U') direction = 'D';
                    break;  
                // Tao qua tao moi
                case KeyEvent.VK_N:
                    newApple();
                    break;
                // Tang body ran va hack diem
                case KeyEvent.VK_I:
                    bodyParts++;
                    appleEaten++;
                    break;
                // Dung va chay tiep 
                case KeyEvent.VK_P:
                    timer.stop();
                    break;
                case KeyEvent.VK_O:
                    timer.start();
                    break;
            }
        }
        
    }
}
