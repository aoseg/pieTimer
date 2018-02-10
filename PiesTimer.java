
package piestimer;

/**
 *
 * @author alexanderoseguera
 */


import java.awt.Graphics;
import java.awt.Color;
import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JTextField;



public class PiesTimer extends JPanel implements ActionListener
{ 
    private int piePosition;
    private double change;
    
    public PiesTimer() 
    {
        piePosition=360; 
    }

     public void paintComponent(Graphics g)
     {
        super.paintComponent(g);
        g.setColor(Color.RED);
        g.fillArc(100, 100, 500, 500, 90, piePosition);
    
        g.setColor(Color.BLACK);
        g.drawArc(100, 100, 500, 500, 0, 360);
     }
     
     public void setChange(int seconds)
     {
           change = 360/seconds; 
     }
     
     public void resetPosition()
     {
         piePosition=360; 
         repaint(); 
         change= 0; 
     }
     
     
     public boolean isEnd()
     {
        return piePosition <= 0; 
     }
     
     public void actionPerformed(ActionEvent e)
    {
        piePosition-=change; 
        repaint();
    }
     
    public int currentTimeSecs()
    {
        Calendar cal = Calendar.getInstance();
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);
        int seconds = cal.get(Calendar.SECOND);
        return (3600*hours)+(60*minutes)+seconds; 
        
    }
     
    
     public static void playSound(File sound) throws LineUnavailableException, IOException, UnsupportedAudioFileException, InterruptedException
     {
        try{ 
         Clip clip = AudioSystem.getClip(); 
         clip.open(AudioSystem.getAudioInputStream(sound));
         clip.start(); 
         
         Thread.sleep(clip.getMicrosecondLength());
        }
        
        catch(Exception e){
        }
     }
     
     
    
    public static void main(String[] args) throws LineUnavailableException, IOException, UnsupportedAudioFileException, InterruptedException 
    { 
        
        
        PiesTimer panel = new PiesTimer();
        JTextField hourInput = new JTextField("Hour",10);
        JTextField minuteInput = new JTextField("Minute",10);
        JButton startTimer = new JButton("Start Timer");
        JButton reset = new JButton("Reset");

        panel.add(hourInput);
        panel.add(minuteInput);
        panel.add(startTimer); 
        panel.add(reset);
        panel.setBackground(Color.WHITE);
       
        JFrame window = new JFrame("Timer");
        window.setBounds(300, 300, 700, 700);
        
        Container c = window.getContentPane();
        c.add(panel);
        
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        
        Timer clock = new Timer(1000,panel);
  
        
        
        startTimer.addActionListener(new ActionListener() 
        {
          public void actionPerformed(ActionEvent e)
          {
              int hour = Integer.parseInt(hourInput.getText());
              int minutes = Integer.parseInt(minuteInput.getText());
              int seconds = (3600*hour)+ (60*minutes);
              panel.setChange(seconds-panel.currentTimeSecs());
          }
        }); 
        
        reset.addActionListener(new ActionListener() 
        {
          public void actionPerformed(ActionEvent e)
          {
              panel.resetPosition();
              clock.restart(); 
          }
        });
        
        clock.start(); 
        File tone = new File("Tone.wav");
        
        while(clock.isRunning())
        {
            if(panel.isEnd())
            {
                clock.stop();
                while(reset.getModel().isPressed()==false)
                {
                    playSound(tone);
                }
            }
        }
        
        
    }
    
}
