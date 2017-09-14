import java.io.File;
import java.util.Calendar;


public class Main {

	public final static String fichero = "caso17.txt";

	public static void main(String[] args) throws Exception {
		int caso = 0;
		// caso = 0 resuelve los casos del 1 al 17
		// caso = 1 resuelve el caso dado
		// Cualquier otro caso genera el problema pedido a generador
		
		if(caso == 0){
			for(int i=1;i<18;i++){
				String f = "caso"+i+".txt";
				File fs = new File("src/files/" + f);
				Piramide p = new Piramide(fs);
				long x = System.nanoTime();
				p.resuelve();
				long y = System.nanoTime();
				System.out.printf("Tiempo usado para resolver el caso %d: %f segundos\n", i, ((double)(y-x))*(Math.pow(10, -9)));
			}
		}
		
		else if(caso == 1){
			System.out.println("Inicio de ejecución: " + Calendar.getInstance().getTime());
			System.out.println("Resolviendo " + fichero);
			File f = new File("src/files/" + fichero);
			Piramide p = new Piramide(f);
			long x = System.nanoTime();
			p.resuelve();
			long y = System.nanoTime();
			System.out.println(p);
			System.out.printf("Tiempo usado para resolver: %f segundos\n", ((double)(y-x))*(Math.pow(10, -9)));
		}

		else{
			@SuppressWarnings("unused")
			Generador g = new Generador(20,53,3,"caso18.txt");
		}
	}

}
