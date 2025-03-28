import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.util.Dictionary;
import java.util.Hashtable;

public class Calendar extends JFrame {
    private JSlider slider;
    private Timer timer;

    public Calendar(HouseEnv env) {
        setTitle("Hora Slider");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 100);

        slider = new JSlider(0, 24, 0); // Rango de horas
        slider.setMinimum(0);
        slider.setMaximum(23);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setValue(0);
        Dictionary<Integer, JLabel> lbs = new Hashtable<Integer, JLabel>();
        for (int i = 0; i < 24; i++) {
            lbs.put(i, new JLabel("" + i + ":00"));
        }
        slider.setLabelTable(lbs);

        timer = new Timer(30000, e -> {
            int hora = slider.getValue();
            if (hora < 23) {
                slider.setValue(hora + 1);
            } else {
                slider.setValue(0);
            }
        });

        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent event) {
                env.updatePercepts();
            }
        });
        timer.start();
        add(slider);
        slider.setPreferredSize(new Dimension(800, 50));
        setVisible(true);
        setAlwaysOnTop(true);
    }

    public String getHora() {
        return Integer.toString(slider.getValue());
    }
}