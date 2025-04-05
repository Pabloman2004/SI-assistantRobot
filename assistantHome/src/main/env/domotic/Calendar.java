package domotic;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
public class Calendar {
    private int hora;
    private LocalDateTime prov;
    private LocalDate dia;

    public Calendar() {  
        this.prov = LocalDateTime.now(); // Obtiene la fecha y hora actual      
        this.dia = this.prov.toLocalDate(); // Obtiene la fecha 
        this.hora= this.prov.getHour();
        System.out.println("Día actual: " + this.dia);
        System.out.println("Hora actual: " + this.hora); 
        // Hilo que actualiza la hora automáticamente
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(15000); // Espera X milisegundos
                    increaseHour(); // Incrementa la hora
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private synchronized void increaseHour() {
        if (this.hora < 23) {
            this.hora ++; // Actualiza la hora
        } else {
            this.hora = 0;
            this.dia = this.dia.plusDays(1); // Incrementa el día this.dia.plusDays(1);
        }
        if(this.hora == 1) {
            System.out.println("Día actual: " + this.dia); // Muestra el día actualizado
        }
        System.out.println("Hora actual: " + this.hora); // Muestra la hora actualizada
    }

    public synchronized int getHora() {
        return this.hora;
    }

    public synchronized int getDia() {
        return this.dia.getDayOfMonth(); // Devuelve el día del mes
    }
    
}
