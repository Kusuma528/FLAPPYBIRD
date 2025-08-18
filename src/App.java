import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
       int boardwidth=360;//in pixels
       int boardHeight=640;

       JFrame frame=new JFrame("FLAPPY BIRD");
       //frame.setVisible(true);
       frame.setSize(boardwidth,boardHeight);
       frame.setLocationRelativeTo(null);//this will opens window at center
       frame.setResizable(false);//user cannot resize 
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

       flappybird flappy=new flappybird();
       frame.add(flappy);
       frame.pack();//the size of title bar is not included 
       flappy.requestFocus();
       frame.setVisible(true);
    }
}
