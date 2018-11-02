package breakblock;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Main extends JFrame implements KeyListener {

    static final int W = 800, H = 600;
    int ballX, ballY, barX, barY, barL, ballVx, ballVy;
    boolean rightF, leftF, gameover, clear, start, superBreak;

    public static void main(String[] args) {
        new Main();
    }

    Main() {

        Container content = getContentPane();
        content.setLayout(new FlowLayout());
        JComponent jcom = new JComponentEx();
        jcom.setPreferredSize(new Dimension(W, H));
        content.add(jcom);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, W, H);
        setResizable(false);
        setVisible(true);
        this.addKeyListener(this);
        set();

    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        int i = e.getKeyCode();
        switch (i) {
            case KeyEvent.VK_LEFT:
                leftF = true;
                break;
            case KeyEvent.VK_RIGHT:
                rightF = true;
                break;
            case KeyEvent.VK_SPACE:
                if (start) {
                    ballVx = ballVy = -5;
                }
                start = false;
                break;
            case KeyEvent.VK_ENTER:
                if (gameover || clear) {
                    set();
                }
                break;
        }
    }

    void set() {
        new Stage1();
        barY = H - 100;
        barX = W / 2;
        barL = 150;
        start = true;
        superBreak = false;
        clear = gameover = rightF = leftF = false;
        ballX = W / 2;
        ballY = barY - 25;
        ballVx = ballVy = 0;
    }

    public void keyReleased(KeyEvent e) {
        int i = e.getKeyCode();
        switch (i) {
            case KeyEvent.VK_LEFT:
                leftF = false;
                break;
            case KeyEvent.VK_RIGHT:
                rightF = false;
                break;
        }
    }

    public class JComponentEx extends JComponent implements Runnable {

        JComponentEx() {
            (new Thread(this)).start();
        }

        public void run() {
            try {
                while (true) {

                    if (start) {
                        ballX = barX;
                    }
                    //キー操作
                    if (rightF) {
                        barX += 6;
                    } else if (leftF) {
                        barX -= 6;
                    }
                    //壁
                    if (ballX < 0 || ballX + 20 > W) {
                        ballVx = -ballVx;
                    }
                    if (ballY < 0) {
                        ballVy = -ballVy;
                    }
                    //バーの当たり判定
                    if (ballY + 20 >= barY && ballY < barY + 10 && ballX + 20 > barX && ballX < barX + barL) {
                        for (int k = 0; k < 7; k++) {
                            if (barX + barL * k / 7 <= ballX + 10 && ballX + 10 <= barX + barL * (k + 1) / 7) {
                                switch (k) {
                                    case 0:
                                    case 6:
                                        if (ballVx > 0) {
                                            ballVx = 7;
                                        } else {
                                            ballVx = -7;
                                        }
                                    case 1:
                                    case 5:
                                        if (ballVx > 0) {
                                            ballVx = 5;
                                        } else {
                                            ballVx = -5;
                                        }
                                        break;
                                    case 2:
                                    case 4:
                                        if (ballVx > 0) {
                                            ballVx = 3;
                                        } else {
                                            ballVx = -3;
                                        }
                                        break;
                                    case 3:
                                        if (ballVx > 0) {
                                            ballVx = 1;
                                        } else {
                                            ballVx = -1;
                                        }
                                }
                            }
                        }
                        ballVy = -ballVy;
                        if (ballVy > 0) {
                            ballVy = -ballVy;
                        }
                    }
                    //ゲームオーバー
                    if (ballY + 20 > H) {
                        gameover = true;
                    }
                    if (gameover) {
                        ballY = ballX = 0;
                    }
                    //バーに対する壁
                    if (barX < 0) {
                        barX = 0;
                    }
                    if (barX + barL > W) {
                        barX = W - barL;
                    }
                    ballX += ballVx;
                    ballY += ballVy;
                    //ブロックあたり判定
                    clear = true;
                    for (int i = 0; i < Stage1.tate; i++) {
                        for (int j = 0; j < Stage1.yoko; j++) {
                            if (!Stage1.b[i][j].breakF) {
                                if (Stage1.b[i][j].x <= ballX + 10 && ballX + 10 <= Stage1.b[i][j].x + Stage1.b[i][j].lx
                                        && Stage1.b[i][j].y <= ballY + 10 && ballY + 10 <= Stage1.b[i][j].y + Stage1.b[i][j].ly) {
                                    Stage1.b[i][j].breakF = true;
                                    if (!superBreak) {
                                        if ((Stage1.b[i][j].x <= ballX + 10 && ballX + 10 < Stage1.b[i][j].x + 4)
                                                || (Stage1.b[i][j].x + Stage1.b[i][j].lx - 4 <= ballX + 10 && ballX + 10 < Stage1.b[i][j].x + Stage1.b[i][j].lx)) {
                                            ballVx = -ballVx;
                                        } else {
                                            ballVy = -ballVy;
                                        }
                                    }
                                }
                                clear = false;
                            }
                        }
                    }
                    repaint();
                    Thread.sleep(10);
                }
            } catch (InterruptedException ex) {
            }
        }

        public void paint(Graphics g) {
            if (start) {
                g.setColor(Color.CYAN);
                g.setFont(new Font(null, Font.BOLD, 30));
                g.drawString("SPACEキーでSTART", W / 2 - 150, H / 2);
            }
            if (clear) {
                g.setFont(new Font(null, Font.BOLD, 60));
                g.drawString("クリア", W / 2 - 120, H / 2 - 20);
            } else if (gameover) {
                g.setFont(new Font(null, Font.BOLD, 60));
                g.drawString("GAME OVER", W / 2 - 160, H / 2 - 20);
            } else {
                g.setColor(Color.blue);
                g.fillRect(barX, barY, barL, 10);
                g.setColor(Color.red);
                g.fillOval(ballX, ballY, 20, 20);

                for (int i = 0; i < Stage1.tate; i++) {
                    for (int j = 0; j < Stage1.yoko; j++) {
                        if (!Stage1.b[i][j].breakF) {
                            g.setColor(Color.black);
                            g.fillRect(Stage1.b[i][j].x, Stage1.b[i][j].y, Stage1.b[i][j].lx, Stage1.b[i][j].ly);
                        }
                    }
                }
            }
        }
    }
}
