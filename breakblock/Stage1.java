package breakblock;

public class Stage1 {
    static public int tate, yoko;
    static block b[][];
    Stage1() {
        tate = 4;
        yoko = 7;
        b = new block[tate][yoko];
        for (int i = 0; i < tate; i++) {
            for (int j = 0; j < yoko; j++) {
                b[i][j] = new block();
                b[i][j].setlxy(100, 50);
                b[i][j].setxy(j * b[i][j].lx + 2 * j + 45, i * b[i][j].ly + 2 * i + 50);
            }
        }
    }
    class block {
        public int x, y, lx, ly;
        public boolean breakF;
        block() {
            breakF = false;
        }
        void setlxy(int lx, int ly) {
            this.lx = lx;
            this.ly = ly;
        }
        void setxy(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
