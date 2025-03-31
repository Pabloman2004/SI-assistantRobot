package domotic;

public class Calendar {
    private int hora;

    public Calendar() {
        this.hora = 0;

        // Hilo que actualiza la hora automáticamente
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(3000); // Espera X milisegundos
                    increaseHour(); // Incrementa la hora
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private synchronized void increaseHour() {
        if (this.hora < 23) {
            this.hora++;
        } else {
            this.hora = 0;
        }
        System.out.println("Hora actual: " + this.hora); // Muestra la hora actualizada
    }

    public synchronized int getHora() {
        return this.hora;
    }
    
}
