package sudoku;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

/**
 * Créer un chronomètre.
 * 
 * @inv 
 * 		
 * 
 */
public class Chrono {
	private long start;
	private long pause;
	
	public Chrono() {
		long time = System.currentTimeMillis();
		start = time;
		pause = time;
	}
	
	// requetes
	public long getTimeStart() {
		return start;
	}
	
	public long getTimepause() {
		return pause;
	}
	
	public long getTime() {
		pause = System.currentTimeMillis();
		return pause - start;
	}
	
	public String getChrono() {
		long time = getTime();
		
		//hh:mm:ss
		return String.format("%02d:%02d:%02d", 
		    TimeUnit.MILLISECONDS.toHours(time),
		    TimeUnit.MILLISECONDS.toMinutes(time) - 
		    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time)),
		    TimeUnit.MILLISECONDS.toSeconds(time) - 
		    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
	}
	
	// commandes
	public void pause() {
		pause = System.currentTimeMillis();
	}
	
	public void start() {
		start = System.currentTimeMillis();
		pause = System.currentTimeMillis();
	}
}