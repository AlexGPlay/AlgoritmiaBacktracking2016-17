import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;


public class Piramide {

	private double[][] pira;
	private ArrayList<Colores> colores = new ArrayList<Colores>();
	private ArrayList<Colores> rellenar = new ArrayList<Colores>();

	public Piramide(File f){
		cargaPiramide(f);
		Collections.reverse(rellenar);
	}

	public void resuelve(){
		rellenaPosibles();
		rellenaColores();
	}

	private int cargaPiramide(File f){
		BufferedReader br;
		FileInputStream fr;

		try {
			fr = new FileInputStream(f);
			br = new BufferedReader(new InputStreamReader(fr));
			int l;
			int j = 0;

			String str = br.readLine();
			l = Integer.parseInt(str);
			pira = new double[l][l];
			rellenaHuecos();
			String[] all;
			str = br.readLine();

			while(str!=null){
				all = str.split(" ");

				for(int i=0;i<all.length;i++){
					if(all[i].equals("*")){
						pira[j][i] = 0;
						rellenar.add(new Colores(j,i,'W'));
					}

					else if(all[i].equals("B")){
						pira[j][i] = -1;
						Colores temp = new Colores(j,i,'B');
						colores.add(temp);
						rellenar.add(temp);
					}

					else if(all[i].equals("R")){
						pira[j][i] = -2;
						Colores temp = new Colores(j,i,'R');
						colores.add(temp);
						rellenar.add(temp);
					}

					else if(all[i].equals("Y")){
						pira[j][i] = -3;
						Colores temp = new Colores(j,i,'Y');
						colores.add(temp);
						rellenar.add(temp);
					}

					else{
						pira[j][i] = Double.parseDouble(all[i]);
					}

				}

				str = br.readLine();
				j++;
			}

			System.out.println("");
			br.close();
			fr.close();

		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}

		return 0;
	}

	private void rellenaPosibles(){

		for(int i=0;i>pira.length;i--){
			for(int j=0;j<=i;j++){
				if(i<pira.length-1 && pira[i+1][j]>0 && pira[i+1][j+1]>0){
					ArrayList<Integer> temp = new ArrayList<Integer>();

					int suma = (int)(pira[i+1][j]+pira[i+1][j+1]);
					int resta = Math.abs((int)(pira[i+1][j]-pira[i+1][j+1]));

					if(suma<10)
						temp.add(suma);

					if(resta>0)
						temp.add(resta);

					if(temp.size()==1)
						pira[i][j] = temp.get(0);

				}
			}
		}

	}


	private void rellenaHuecos(){

		for(int i=0;i<pira.length;i++){
			for(int j=0;j<pira[i].length;j++){
				pira[i][j] = -10;
			}
		}

	}

	private ArrayList<Colores> getColor(int color){

		ArrayList<Colores> temp = new ArrayList<Colores>();

		for(Colores col : colores){
			if(col.getTrad() == color)
				temp.add(col);
		}

		return temp;
	}

	private void rellenaColores(){
		double[][] copia = clonar(pira);
		pira = resuelve(copia, 0);
	}

	private int checkColores(int i, int j, double[][] check){

		Colores temp = null;

		for(Colores c : colores){
			if(c.equals(new Colores(i,j,'x'))){
				temp = c;
				break;
			}
		}

		if(temp!=null)
			return temp.getTrad();

		return 0;
	}

	private double[][] resuelve(double[][] pira, int x){

		double[][] resC;
		double[][] pira2;
		int[] temp = buscaSiguiente(x, pira);

		if(temp!=null && temp[0]==-1)
			return pira;

		if(temp!=null){
			int resuCol = checkColores(temp[0],temp[1],pira);

			if(temp[0]<pira.length-1){
				int suma = (int)(pira[temp[0]+1][temp[1]]+pira[temp[0]+1][temp[1]+1]);
				int resta = Math.abs((int)(pira[temp[0]+1][temp[1]]-pira[temp[0]+1][temp[1]+1]));
				ArrayList<Integer> results = new ArrayList<Integer>();

				if(suma<10)
					results.add(suma);

				if(resta>0)
					results.add(resta);

				for(int w : results){
					pira2 = clonar(pira);
					double[][] res = coloca(temp, w, pira2, resuCol);
					if(res!=null){
						if(checkCompleta(res))
							return res;

						resC = resuelve(res,x+1);
						if(checkCompleta(resC))
							return resC;
					}
				}
			}

			else{
				for(int i=1;i<10;i++){
					pira2 = clonar(pira);
					double[][] res = coloca(temp, i, pira2, resuCol);
					if(res!=null){
						if(checkCompleta(res))
							return res;

						resC = resuelve(res,x+1);
						if(checkCompleta(resC))
							return resC;
					}
				}
			}
		}



		return null;
	}


	private double[][] coloca(int[] pos, int col, double[][] pira, int color){

		if(pos == null)
			return null;

		if(pos[0] == -1)
			return pira;

		if(checkCasilla(pos[0],pos[1],pira,col)){
			boolean t = colocaColores(color,pira,col);
			if(t){
				if(!CheckSuperiores(pos[0], pos[1], pira))
					return null;
				
				return pira;
			}


			return null;
		}

		else
			return null;

	}

	private boolean CheckSuperiores(int i, int j, double[][] pira){
		ArrayList<Integer> resultadosIz = new ArrayList<Integer>();
		ArrayList<Integer> resultadosDe = new ArrayList<Integer>();
		int[] posIz = {i-1,j-1};
		int[] posDe = {i-1,j};
		double[][] temp2 = clonar(pira);

		if(i==0)
			return true;

		//Izquierda
		if(j-1>=0 && pira[i-1][j-1]<=0 && pira[i][j-1]>0){
			int suma = (int)(pira[i][j]+pira[i][j-1]);
			int resta = Math.abs((int)(pira[i][j]-pira[i][j-1]));

			if(suma<10)
				resultadosIz.add(suma);

			if(resta>0)
				resultadosIz.add(resta);

			if(resultadosIz.size()==0)
				return false;

			if(resultadosIz.size()==1){
				double[][] temp = coloca(posIz,(int)(resultadosIz.get(0)),temp2,(int)this.pira[i-1][j-1]);
				if(temp==null)
					return false;
			}

		}


		//Derecha
		if(j<i && pira[i-1][j]<=0 && pira[i][j+1]>0){
			int suma = (int)(pira[i][j]+pira[i][j+1]);
			int resta = Math.abs((int)(pira[i][j]-pira[i][j+1]));

			if(suma<10)
				resultadosDe.add(suma);

			if(resta>0)
				resultadosDe.add(resta);

			if(resultadosDe.size()==0)
				return false;

			if(resultadosDe.size()==1){
				double[][] temp = coloca(posDe,(int)(resultadosDe.get(0)),temp2,(int)this.pira[i-1][j]);
				if(temp==null)
					return false;
			}	
			
		}


		if(resultadosIz.size()<=1 && resultadosDe.size()<=1)
			return true;

		else{

			if(resultadosIz.size()==0 && resultadosDe.size()==2){
				for(int a : resultadosDe)
					if(coloca(posDe,a,temp2,(int)this.pira[i-1][j])!=null)
						return true;
			}

			else if(resultadosDe.size()==0 && resultadosIz.size()==2){
				for(int a : resultadosIz)
					if(coloca(posIz,a,temp2,(int)this.pira[i-1][j-1])!=null)
						return true;
			}

			else{
				for(int a : resultadosDe)
					if(coloca(posDe,a,temp2,(int)this.pira[i-1][j])!=null)
						for(int x : resultadosIz)
							if(coloca(posIz,x,temp2,(int)this.pira[i-1][j-1])!=null)
								return true;
			}

			return false;
		}
	}

	private boolean colocaColores(int color, double[][] pira, int num){
		if(color<0){
			ArrayList<Colores> col = this.getColor(color);
			col.remove(1);
			Colores c = col.get(0);
			if(checkCasilla(c.i,c.j,pira,num)){
				pira[c.i][c.j] = num;
				return true;
			}

			return false;
		}

		return true;
	}

	private double[][] clonar(double[][] objetivo){
		double[][] temp = new double[objetivo.length][objetivo[0].length];

		for(int i=0;i<temp.length;i++){
			for(int j=0;j<temp[i].length;j++){
				temp[i][j] = objetivo[i][j];
			}
		}

		return temp;
	}

	private boolean checkCasilla(int i, int j, double[][] pira, int col){
		pira[i][j] = col;

		boolean x = checkBajo(i,j,pira);

		if(!x)
			return false;

		boolean y = checkAlto(pira,i,j);

		if(!y)
			return false;

		return true;
	}

	private boolean checkCompleta(double[][] pira){
		if(pira==null)
			return false;

		for(int i=0;i<pira.length;i++){
			for(int j=0;j<i+1;j++){
				if(pira[i][j]<=0)
					return false;
			}
		}

		return true;
	}

	private boolean checkBajo(int i, int j, double[][] pira){

		if(i<pira.length-1 && pira[i+1][j]>0 && pira[i+1][j+1]>0){
			int suma = (int)(pira[i+1][j]+pira[i+1][j+1]);
			int resta = Math.abs((int)(pira[i+1][j]-pira[i+1][j+1]));

			if(pira[i][j]==suma && suma<10)
				return true;

			if(pira[i][j]==resta && resta>0)
				return true;

			return false;
		}

		else
			return true;

	}

	private boolean checkAlto(double[][] pira, int i, int j){

		if(i==0)
			return true;

		else{
			//Izquierda
			if(j-1>=0 && pira[i-1][j-1]>0 && pira[i][j-1]>0){
				int suma = (int)(pira[i][j]+pira[i][j-1]);
				int resta = Math.abs((int)(pira[i][j]-pira[i][j-1]));

				if(!((pira[i-1][j-1]==suma && suma<10) || (pira[i-1][j-1]==resta && resta>0)))
					return false;
			}

			//Derecha
			if(j<i && pira[i-1][j]>0 && pira[i][j+1]>0){
				int suma = (int)(pira[i][j]+pira[i][j+1]);
				int resta = Math.abs((int)(pira[i][j]-pira[i][j+1]));

				if(!((pira[i-1][j]==suma && suma<10) || (pira[i-1][j]==resta && resta>0)))
					return false;
			}

		}


		return true;
	}

	private int[] buscaSiguiente(int x, double[][] pira){
		if(x == rellenar.size())
			return null;

		int[] temp = rellenar.get(x).getCoordenadas();
		if(pira[temp[0]][temp[1]]<=0)
			return temp;

		else
			return buscaSiguiente(x+1, pira);

	}

	@Override
	public String toString(){

		if(pira==null)
			return "No se ha encontrado solución";

		String s = "";

		for(int i=0;i<pira.length;i++){
			for(int z=0;z<pira.length-i;z++)
				s += " ";

			for(int j=0;j<pira[i].length;j++){
				if(pira[i][j]==0)
					s += "* ";

				else if(pira[i][j]==-1)
					s += "B ";

				else if(pira[i][j]==-2)
					s +="R ";

				else if(pira[i][j]==-3)
					s+="Y ";

				else if(pira[i][j]==-10)
					s += " ";

				else
					s += String.valueOf((int)pira[i][j]) + " ";
			}
			s+="\n";
		}

		return s;
	}
	
	private class Colores {

		int i;
		int j;
		char color;
		
		public Colores(int i, int j, char color){
			this.i = i;
			this.j = j;
			this.color = color;
		}
		
		public int[] getCoordenadas(){
			int[] x = {i,j};
			return x;
		}
		
		public int getTrad() {
			if(color=='R')
				return -2;
			
			else if(color=='B')
				return -1;
			
			else
				return -3;
		}
		
		@Override
		public boolean equals(Object other){
			if(other == null)
				return false;
			
			if(other == this)
				return true;
			
			if (!(other instanceof Colores))
				return false;
			
		    Colores prueba = (Colores)other;
			
		    if(prueba.i == this.i && prueba.j == this.j)
		    	return true;
		    
		    return false;
		}
		
	}

}
