import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
public class flappybird extends JPanel implements ActionListener,KeyListener{
    int boardwidth=360;
    int boardHeight=640;

    //Images
    Image backgroundImg;
    Image birdImg;
    Image topPipImage;
    Image bottomPipeImg;

    //Bird
    int birdX=boardwidth/8;
    int birdY=boardHeight/2;
    int birdWidth=34;
    int birdHeight=24;

    class Bird{
        int x=birdX;
        int y=birdY;
        int width=birdWidth;
        int height=birdHeight;
        Image img;
    

    Bird(Image img){
        this.img=img;

    }
}

    //pipes
    int pipeX=boardwidth;
    int pipeY=0;
    int pipeWidth=64;
    int pipeHeight=512;

    class pipe{
        int x=pipeX;
        int y=pipeY;
        int width=pipeWidth;
        int height=pipeHeight;
        Image img;
        boolean passed=false;

        pipe(Image img){
            this.img=img;
        }

    }
    //game logic
    Bird bird;
    int velocityX=-4;
    int velocityY=0;
    int gravity=1;

    ArrayList<pipe>pipes;
    Random random=new Random();

    Timer gameLoop;
    Timer placepipTimer;

    double score=0;


    boolean gameOver=false;


    //connect this jpanel to app.java
    public flappybird() {
        setPreferredSize(new Dimension(boardwidth,boardHeight));
        //setBackground(Color.blue);
        setFocusable(true);//makes sure this is the taking value of keylisteners
        addKeyListener(this);//it will checks three keylistners which action is performed

        //load images
        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipImage = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg=new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        //bird
        bird=new Bird(birdImg);
        pipes=new ArrayList<pipe>();

        //place pipes timer
        placepipTimer=new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                placepipes();
            }
        });
        placepipTimer.start();

        //game timer
        gameLoop=new Timer(1000/60,this);//drawing 60times per second
        gameLoop.start();
    
    }
    public void placepipes(){
        //random:(0-1)*pipeheight/2 -> (0-256)
        //0-pipeheight/4 ->128
        //128-(0-256)
        //-->1/4 pipeHeight -->3/4 pipeheigth
        int randompipeY=(int)(pipeY-pipeHeight/4-Math.random()*(pipeHeight/2));
        int openingspace=boardHeight/4;


        pipe topPipe=new pipe(topPipImage);
        topPipe.y=randompipeY;
        pipes.add(topPipe);

        pipe bottompipe=new pipe(bottomPipeImg);
        bottompipe.y=topPipe.y + pipeHeight+openingspace;
        pipes.add(bottompipe);
    }
    //A Function of JPanel
    public void paintComponent(Graphics g){
        super.paintComponent(g);//super inherits the JPanel
        draw(g);
    }
    public void draw(Graphics g){
        //background
        g.drawImage(backgroundImg,0,0,boardwidth,boardHeight,null);//start from top left corner

        //bird
        g.drawImage(bird.img, bird.x, bird.y, bird.width,bird.height,null);

        //pipes
        for (int i = 0; i < pipes.size(); i++) {
            pipe Pipe=pipes.get(i);
            g.drawImage(Pipe.img,Pipe.x,Pipe.y,Pipe.width,Pipe.height,null);
        }

        //score
        g.setColor(Color.white);
        g.setFont(new Font("Arial",Font.PLAIN,32));
        if(gameOver){
            g.drawString("Game Over: " + String.valueOf((int)score),10,35);//position
        }
        else{
            g.drawString(String.valueOf((int)score),10,35);
        }
    }

    public void move(){
        //bird
        velocityY+=gravity;
        bird.y+=velocityY;//bird yth position will be get changing per second
        bird.y=Math.max(bird.y,0);//the bird shouldn't go outside the panel i.e.,0

        //pipes
        for(int i=0;i<pipes.size();i++){
            pipe Pipe=pipes.get(i);
            Pipe.x+=velocityX;

            if (!Pipe.passed && bird.x > Pipe.x + Pipe.width){
                Pipe.passed=true;
                score+=0.5;//for each pipe up and down
            }

            if(collision(bird, Pipe)){
                gameOver=true;

            }
        }

        //gameover logic
        if(bird.y>boardHeight){
            gameOver=true;
        }
    }

    public boolean collision(Bird a ,pipe b){
        return a.x<b.x+b.width &&
               a.x+a.width>b.x &&
               a.y<b.y+b.height &&
               a.y+a.height>b.y;
    }





    @Override
    public void actionPerformed(ActionEvent e) {
        move();//before repainting we specify where to draw the bird
       repaint();//this calls paintcomponent again
       if(gameOver){
        placepipTimer.stop();
        gameLoop.stop();
       }
    }

   
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_UP){
            velocityY=-9;
            if(gameOver){
                bird.y=birdY;
                velocityY=0;
                pipes.clear();
                score=0;
                gameOver=false;
                gameLoop.start();
                placepipTimer.start();
            }
        }
    }
     @Override
    public void keyTyped(KeyEvent e) {
        
    }
    @Override
    public void keyReleased(KeyEvent e) {
            }
   



    
}
