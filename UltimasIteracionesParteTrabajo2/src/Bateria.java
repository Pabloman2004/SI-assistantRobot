// Java Program to create a
// simple progress bar
import java.awt.Color;

import javax.swing.*;
public class Bateria extends JFrame {
    private JProgressBar bar;
	public void setBar(int valor)
	{
		bar.setValue(valor);
	}
	public Bateria(String agente){
        bar = new JProgressBar(0, 200);
        setTitle("Batería " + agente);
        setSize(900, 75);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        bar.setValue(200);
        bar.setStringPainted(true);
        add(bar);
        setVisible(true);
        bar.setForeground(Color.GREEN);
	}

    public void reduce(){
        bar.setValue(bar.getValue() - 1);
        if(bar.getValue()/2 > 80){
            bar.setForeground(Color.GREEN);
        }else if(bar.getValue()/2 > 60){
            bar.setForeground(new Color(115, 149, 80));
        }else if(bar.getValue()/2 > 40){
            bar.setForeground(new Color(132, 149, 80));
        }else if(bar.getValue()/2 > 20){
            bar.setForeground(new Color(149, 98, 80));
        }else{
            bar.setForeground(new Color(149, 80, 80));
        }
    }

    public void increase(){
        bar.setValue(bar.getValue() + 10 >= 200 ? 200 : bar.getValue() + 10);
        if(bar.getValue()/2 > 80){
            bar.setForeground(Color.GREEN);
        }else if(bar.getValue()/2 > 60){
            bar.setForeground(new Color(115, 149, 80));
        }else if(bar.getValue()/2 > 40){
            bar.setForeground(new Color(132, 149, 80));
        }else if(bar.getValue()/2 > 20){
            bar.setForeground(new Color(149, 98, 80));
        }else{
            bar.setForeground(new Color(149, 80, 80));
        }
    }
}
