import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

public class Generador {

	private int altura;
	private int huecos;
	private int colores;
	private String fichero;
	private int[][] pira;
	private int total;

	ArrayList<RepColor> iguales;

	public Generador(int altura, int huecos, int colores, String fichero) throws Exception {
		super();
		this.altura = altura;
		this.huecos = huecos;
		this.colores = colores;
		this.fichero = fichero;
		total = 0;

		generaPira();
		if(huecos+colores*2>=total)
			throw new Exception("Hay demasiados huecos");

		do {
			iguales = new ArrayList<RepColor>();
			for(int i=1;i<10;i++)
				iguales.add(new RepColor());
			boolean cb;
			
			do {
				generaBase();
				cb = continuaBase();
			}while(!Montada() && !cb);

			almacenaColores();
			limpiaColores();
		}while(iguales.size()<colores);

		colocaColores();
		generaHuecos();
		guardaGeneracion();
	}

	private int guardaGeneracion(){
		String t = this.altura + "\n";
		t += this.toString();

		BufferedWriter bf;
		FileWriter fl;
		PrintWriter pw;

		try {
			fl = new FileWriter(new File(fichero), false);
			bf = new BufferedWriter(fl);
			pw = new PrintWriter(bf);

			pw.print(t);

			pw.close();
			bf.close();
			fl.close();
		} 

		catch (Exception e) {
			return 1;
		}

		return 0;
	}

	private void almacenaColores() {

		for(int i=0;i<pira.length;i++) {
			for(int j=0;j<i+1;j++) {
				if(iguales.get(pira[i][j]-1).posiciones.size()==0) {
					int[] temp = {i,j};
					iguales.get(pira[i][j]-1).add(temp);
					buscaIgual(pira[i][j],i,j);
				}
			}
		}

	}

	private void buscaIgual(int busqueda, int bI, int bJ) {

		for(int i=0;i<pira.length;i++) {
			for(int j=0;j<i+1;j++) {
				if(pira[i][j]==busqueda && i!=bI && j!=bJ) {
					int[] temp = {i,j};
					iguales.get(pira[i][j]-1).add(temp);
				}
			}
		}

	}

	private void generaPira() throws Exception{

		pira = new int[altura][altura];

		for(int i=0;i<pira.length;i++){
			for(int j=0;j<pira.length;j++){
				if(j<i+1){
					pira[i][j] = 0;
					total++;
				}
				else
					pira[i][j] = -10;
			}
		}

		if(total<huecos)
			throw new Exception("No puede haber más huecos que tamaño");
	}

	private void generaBase() {

		int i = pira.length-1;
		Random r = new Random();

		for(int j=0;j<pira[i].length;j++) 
			pira[i][j] = r.nextInt(9)+1;

	}

	private void generaHuecos(){
		Random r = new Random();
		int cordI;
		int cordJ;
		for(int i=0;i<=huecos;i++){
			do{
				cordI = r.nextInt(altura);
				cordJ = r.nextInt(cordI+1);
			}while(pira[cordI][cordJ]<=0);

			pira[cordI][cordJ] = 0;
		}

	}

	private boolean continuaBase() {


		for(int i=pira.length-2;i>=0;i--) {
			for(int j=0;j<i+1;j++) {
				int suma = (int)(pira[i+1][j]+pira[i+1][j+1]);
				int resta = Math.abs((int)(pira[i+1][j]-pira[i+1][j+1]));
				
				if(Math.random()>0.5 && suma<10)
					pira[i][j] = suma;

				else if(resta>0)
					pira[i][j] = resta;
				
				else
					return false;
			}
		}
		
		return true;
	}

	private void colocaColores() {

		if(colores==0)
			return;

		Random r = new Random();
		int t = r.nextInt(iguales.size());
		RepColor temp = iguales.get(t);

		for(int i=0;i<2;i++) {
			int c = r.nextInt(temp.posiciones.size());
			int[] pos = temp.posiciones.get(c);
			temp.posiciones.remove(c);
			pira[pos[0]][pos[1]]=-1;
		}
		iguales.remove(t);

		if(colores==1)
			return;

		t = r.nextInt(iguales.size());
		temp = iguales.get(t);

		for(int i=0;i<2;i++) {
			int c = r.nextInt(temp.posiciones.size());
			int[] pos = temp.posiciones.get(c);
			temp.posiciones.remove(c);
			pira[pos[0]][pos[1]]=-2;
		}
		iguales.remove(t);

		if(colores==2)
			return;

		t = r.nextInt(iguales.size());
		temp = iguales.get(t);

		for(int i=0;i<2;i++) {
			int c = r.nextInt(temp.posiciones.size());
			int[] pos = temp.posiciones.get(c);
			temp.posiciones.remove(c);
			pira[pos[0]][pos[1]]=-3;
		}
		iguales.remove(t);

	}

	@Override
	public String toString(){
		String s = "";

		for(int i=0;i<pira.length;i++){
			for(int j=0;j<pira[i].length;j++){
				if(pira[i][j]==0)
					s += "*";

				else if(pira[i][j]==-1)
					s += "B";

				else if(pira[i][j]==-2)
					s +="R";

				else if(pira[i][j]==-3)
					s+="Y";

				else if(pira[i][j]==-10)
					s += "";

				else
					s += String.valueOf((int)pira[i][j]);
				
				if(j<i)
					s+=" ";
			}
			if(i<pira.length-1)
				s+="\n";
		}

		return s;
	}

	private boolean Montada() {
		for(int i=pira.length-1;i>=0;i--) {
			for(int j=0;j<i+1;j++) {
				if(pira[i][j]<=0)
					return false;
			}
		}

		return true;
	}

	private void limpiaColores() {
		int i = 0;
		int t = iguales.size();
		
		while(i<t){
			if(iguales.get(i).posiciones.size()<2){
				iguales.remove(i);
				t--;
				i--;
			}
			i++;
		}
	}

	private class RepColor{
		ArrayList<int[]> posiciones;

		public RepColor() {
			posiciones = new ArrayList<int[]>();
		}

		public void add(int[] pos) {
			posiciones.add(pos);
		}

	}

}
