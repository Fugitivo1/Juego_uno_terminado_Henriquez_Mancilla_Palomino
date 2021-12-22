import java.util.Scanner;

class Juego{
	private Naipe mazo;
	private int cantidadJugadores;
	private Jugador [] jugadores;
	private int cantidadCartasMano= Config.cantidadCartasMano;
	private int cantidadCartasTotal=Config.cantidadCartasNaipe;
	private int cantidadCartasRestantes;
	private int cartaNumeroPozo;

	Juego(int cantidadJugadores){
		this.mazo=new Naipe();
		if(cantidadJugadores>4){
			System.out.println("El máximo es 4 jugadores!");
			this.cantidadJugadores=4;
		}else if(cantidadJugadores<=1){
			System.out.println("El mínimo es 1 jugador contra el computador!");
			this.cantidadJugadores=2;
		}else{
			this.cantidadJugadores=cantidadJugadores;
		}
		this.jugadores = new Jugador[this.cantidadJugadores];
		// este seria el contador que si llega a 0 se compara quien tiene menos cartas para ganar
		this.cantidadCartasRestantes=108;

		asignarJugadores();
		repartir();		
	}
	//************************************************************************//
	// Métodos privados asociados a la creación de un Juego
	// Método para asignar turnos a jugadores, se llama al crear un Juego
	private void asignarJugadores(){
		Scanner teclado = new Scanner(System.in);
		boolean [] turnos= new boolean[4];
		boolean flag;
		String nombre;
		String pais;
		int turno;

		for(int i=1;i<=this.cantidadJugadores;i++){
			flag=false;
			turno=1;
			if(i==1){
				System.out.println("Ingrese nombre");
				nombre=teclado.nextLine();
				System.out.println("Ingrese pais");
				pais=teclado.nextLine();
			}else{
				nombre="Jugador"+(i-1);
				pais="Algun lugar del mundo";
			}
			while(!flag){
				turno = (int)(Math.random()*10);
				if(((turno<this.cantidadJugadores)&&(turno>=0))&&(!turnos[turno])){
					flag=true;
					turnos[turno]=true;
					turno=turno+1;
				}
			}
			jugadores[i-1]=new Jugador(nombre,pais,turno);
		}
	}
	// Método que genera las manos iniciales para cada jugador, 
	// se llama al crear un Juego
	private void repartir(){
		boolean flag=false;
		int cartaNumero=-1;

		for(int j=0;j<this.cantidadJugadores;j++){
			for(int i=1;i<=this.cantidadCartasMano;i++){
				flag=false;
				while(!flag){
					cartaNumero = (int)(Math.random()*1000);
					cartaNumero = cartaNumero%this.cantidadCartasTotal;
					if(((cartaNumero<Config.cantidadCartasNaipe)&&(cartaNumero>=0))&&(mazo.getJugadorAsignado(cartaNumero)==-1)){
						flag=true;
						jugadores[j].getMano().agregarCarta(mazo.getCarta(cartaNumero),cartaNumero);
						mazo.setJugadorAsignado(cartaNumero,j);
					}
				}
			}	
		}		
	}
	//************************************************************************//
	// Método que es llamado para jugar un Juego creado
	public void jugar(){
		Scanner teclado = new Scanner(System.in);
		int carta;
		boolean ganador=false;
		this.cartaNumeroPozo=generarCarta();
		// Acá se debe modificar para terminar el juego
		// Por ahora está en un ciclo infinito
		// se muestra la carta del pozo y se juega por el usuarios
		// y luego juega el computador, está solo con dos jugadores
		while(mazo.getCarta(this.cartaNumeroPozo).getColor()=="Especial")
		{
			this.cartaNumeroPozo=generarCarta();
		}
		while(!ganador){
			if((cantidadCartasRestantes<=0) || (jugadores[0].getMano().largo()==0) || (jugadores[1].getMano().largo()==0))
			{
				ganador=true;
			}
			else
			{	
				// si epicganador es igual a verdadero el jugador humano gana, en el caso contrario gana la maquina
				System.out.print("\033[H\033[2J");
				System.out.flush();
				System.out.println("La carta del pozo es:");
				mostrarPozo(mazo.getCarta(this.cartaNumeroPozo));
				System.out.println("Presione enter para continuar");
				teclado.nextLine();

				//Jugada del jugador usuario
				if(cantidadCartasRestantes>0){
				jugada(0);
				System.out.println("La carta del pozo es:");
				mostrarPozo(mazo.getCarta(this.cartaNumeroPozo));
				System.out.println("Presione enter para continuar");			
				teclado.nextLine();
				}
				else
				{
					ganador=true;
				}
				if(cantidadCartasRestantes>0){
				//Jugada del jugador Computador
				jugada(1);}
				else
				{
					ganador=true;
				}
			}

		}
		if(cantidadCartasRestantes<=0)
		{
			if(jugadores[0].getMano().largo()==jugadores[1].getMano().largo())
			{
				System.out.println("La cantidad de cartas de ambas manos es igual, EMPATE");
			}
			else if(jugadores[0].getMano().largo()<jugadores[1].getMano().largo())
			{
				System.out.println("El jugador "+jugadores[0].getNickname()+" ha ganado");
			}
			else if(jugadores[0].getMano().largo()>jugadores[1].getMano().largo())
			{
				System.out.println("Has perdido :(");
			}
		}
		else if((jugadores[0].getMano().largo()==0) || (jugadores[1].getMano().largo()==0))
		{
			if(jugadores[0].getMano().largo()==0)
			{
				System.out.println("El jugador "+jugadores[0].getNickname()+" ha ganado");
			}
			else if(jugadores[1].getMano().largo()==0)
			{
				System.out.println("Has perdido :(");
			}
		}
	}

	// Método que selecciona una carta de forma aleatoria de un mazo
	// retorna el número de la carta entre los valores 0 y 107
	private int generarCarta(){
		int cartaNumero=-1;
		boolean flag=false;

		while(!flag){
			cartaNumero = (int)(Math.random()*1000);
			cartaNumero = cartaNumero%108;
			if(((cartaNumero<Config.cantidadCartasNaipe)&&(cartaNumero>=0))&&(mazo.getJugadorAsignado(cartaNumero)==-1)){
				flag=true;
			}
		}
		return cartaNumero;
	}

	// Método que realiza una jugada para un jugador, ya sea usuario o computador
	// recibe el número del jugador, 0 para el usuario y 1,2 o 3 para computador 
	private void jugada(int jugador){
		if((jugadores[0].getMano().largo()==0) || (jugadores[1].getMano().largo()==0) )
		{
			
		}
		else{
			
		int cartaNumero=-1;
		Carta carta;
		boolean flag=false;
		Scanner teclado = new Scanner(System.in);
		//valida que el jugador tenga al menos una carta válida para jugar
		if(validarMano(jugadores[jugador].getMano())){
			// se printea el nombre del jugador 0, osea nosotros
			if(jugador==0){
				System.out.println("Es el turno del jugador "+ jugadores[0].getNickname());
			}
			// se printea el nombre del jugador 1, osea la maquina
			else{
			System.out.println("Es el turno del jugador "+jugador);
			}
			if(jugador==0){
				while(!flag){ //repite hasta que seleccione una carta válida
					cartaNumero=seleccionarCarta();
					if(validarJugada(cartaNumero)){
						flag=true;
					}else{
						System.out.println("No puede jugar esa carta");
					}
				}
			}else{
				cartaNumero=generarJugada(jugador);
			}
			// aca se deben meter los cambios de cartas especiales
			cambioColor(cartaNumero,jugador,cartaNumeroPozo);
			this.cartaNumeroPozo=cartaNumero;//
			this.mazo.setUso(cartaNumero);//
			this.jugadores[jugador].getMano().borrarCarta(this.jugadores[jugador].getMano().buscarCarta(cartaNumero));
			cantidadCartasRestantes--;
			// Cartas especiales
			BloqueoOsentido(cartaNumero, jugador);
			masDos(cartaNumero, jugador);
			masCuatro(cartaNumero, jugador);
			// Final cartas especiales
			System.out.println("Presione enter para continuar");
			teclado.nextLine();			
		}else{
			System.out.println("El jugador "+jugadores[jugador].getNickname()+" no tiene carta para jugar, roba una carta");
			cartaNumero=generarCarta();
			robarCarta(jugador,cartaNumero);
			cantidadCartasRestantes--;
			if(jugador==0)
				mostrarMano(jugador);
		}
		carta=this.mazo.getCarta(this.cartaNumeroPozo);	
		//mostrarPozo(carta);		
		}
	}
	// Método que escoge una carta de la mano de un jugador de forma aleatoria
	// retorna el número de la carta
	public int generarJugada(int jugador){
		boolean flag=false;
		int posicion;
		int cartaNumero;
		int largo=jugadores[jugador].getMano().largo();

		while(!flag){
			posicion = (int)(Math.random()*1000);
			posicion = posicion%largo;
			cartaNumero = jugadores[jugador].getMano().getNumeroCarta(posicion);
			if(validarJugada(cartaNumero)){
				flag=true;
				return cartaNumero;
			}
		}
		return -1;
	}

	// Método para mostrar la carta en el tope del pozo
	private void mostrarPozo(Carta carta){
		System.out.println("************************************");
		mostrarCarta(carta,0);
		System.out.println("************************************");
	}

	// Método para mostrar una mano de un jugador
	private void mostrarMano(int jugador){
		int largo2=jugadores[1].getMano().largo();
		System.out.println("Tu mano es:");
		System.out.println("************************************");
		for(int i=0;i<jugadores[jugador].getMano().largo();i++){
			mostrarCarta(jugadores[jugador].getMano().getCarta(i),i+1);
		}
		System.out.println("************************************");
		System.out.println("Al jugador 1 le quedan: "+largo2+" cartas");
		System.out.println("************************************");
		System.out.println("Cartas restantes: "+cantidadCartasRestantes);
		System.out.println("************************************");
	}

	// Método para mostrar una carta en pantalla
	public void mostrarCarta(Carta carta, int numero){
		String texto;
		int largo;
		texto=numero+". "+carta.getValor()+"-"+carta.getColor();
		largo=texto.length();

			for(int j=0;j<largo+3;j++){
				if(j<4)
					System.out.print(" ");
				else
					System.out.print("-");
			}
			System.out.println("\n"+numero+". | "+carta.getValor()+"-"+carta.getColor()+" |");
			for(int j=0;j<largo+3;j++){
				if(j<4)
					System.out.print(" ");
				else
					System.out.print("-");
			}
			System.out.println("\n");
	}

	// Método para validar si una jugada es válida, se recibe la carta y
	// se revisa si tiene el mismo color, valor o si es carta especial
	// retorna verdadero si se cumple una o más condiciones
	public boolean validarJugada(int cartaNumeroJugada){
		boolean flag=false;
		if(!flag){

			if(mazo.getCarta(cartaNumeroJugada).getValor().compareTo(mazo.getCarta(this.cartaNumeroPozo).getValor())==0){
				//System.out.println("Jugada por valor");
				flag=true;
			}
			if(mazo.getCarta(cartaNumeroJugada).getColor().compareTo(mazo.getCarta(this.cartaNumeroPozo).getColor())==0){
				//System.out.println("Jugada por color");
				flag=true;
			}
			if(mazo.getCarta(cartaNumeroJugada).getColor().compareTo("Especial")==0){
				//System.out.println("Jugada por carta especial");
				flag=true;
			}
		}
		return flag;
	}
	// Método que valida si el jugador posee al menos una carta en su
	// mano para poder jugar, retorna verdadero si cumple
	private boolean validarMano(Mano mano){
		int largo = mano.largo();
		for(int i=0;i<largo;i++){
			if(validarJugada(mano.getNumeroCarta(i))){
				return true;
			}
		}
		return false;
	}
	// Método permite a un jugador no computador seleccionar la carta que desea
	// jugar, se retorna el número de la carta seleccionada de su mano
	private int seleccionarCarta(){
		Scanner teclado = new Scanner(System.in);
		String input;
		int carta;
		//jugador 0 es el jugador no computador
		mostrarMano(0);
		System.out.println("Seleccione la carta a jugar");
		System.out.println("Ingrese el número de la carta");
		input=teclado.nextLine();
		carta = Integer.parseInt(input);
		return jugadores[0].getMano().getNumeroCarta(carta-1);
	}
	// Método que asigna una carta a un jugador y la agrega a su mano
	private void robarCarta(int jugador,int cartaNumero){
		this.jugadores[jugador].getMano().agregarCarta(this.mazo.getCarta(cartaNumero),cartaNumero);
		this.mazo.setJugadorAsignado(cartaNumero,jugador);
	}
	// Método para carta bloqueo y carta cambio de sentido (se ve raro pero funciona)
	private void BloqueoOsentido(int cartaNumero, int jugador){
		if((mazo.getCarta(cartaNumero).getValor()=="Bloqueo") || (mazo.getCarta(cartaNumero).getValor()=="Sentido")){
			if(jugador==0)
			{	
				mostrarPozo(mazo.getCarta(this.cartaNumeroPozo));
				nuevaJugada(0);
			}
			else
			{	mostrarPozo(mazo.getCarta(this.cartaNumeroPozo));
				nuevaJugada(1);
			}
		}
	}
	// Método para carta +2 de cualquier color
	private void masDos(int cartaNumero,int jugador)
	{
		if(mazo.getCarta(cartaNumero).getValor()=="+2")
		{	int cartaNumero2=generarCarta();
			int cartaNumero3=generarCarta();
			if(jugador==0)
			{	
				robarCarta(1, cartaNumero2);
				robarCarta(1, cartaNumero3);	
				cantidadCartasRestantes=cantidadCartasRestantes-2;
			}
			else
			{
				robarCarta(0, cartaNumero2);
				robarCarta(0, cartaNumero3);	
				cantidadCartasRestantes=cantidadCartasRestantes-2;
			}
		}
	}
	// Método para carta cambio de color
	private void cambioColor(int cartaNumero,int jugador, int cartaNumeroPozo)
	{

		if(mazo.getCarta(cartaNumero).getValor()=="Color")
		{

			actualizarPozo(cartaNumero, jugador);
		}
	}
	// Método que recibe una carta y le cambia el color para mostrarla en el pozo
	private void actualizarPozo(int cartaNumero,int jugador)
	{	
		if (jugador==0)
		{	
			Scanner teclado = new Scanner(System.in);
			String input;
			System.out.println("Ingrese el número del color que quiera: [1] Rojo, [2] Amarillo, [3] Azul, [4] Verde ");
			input=teclado.nextLine();
			int input2=Integer.parseInt(input);
			if(input2==1)
			{
				System.out.println("**************************");
				System.out.println("Se eligio el color Rojo");
				System.out.println("**************************");
				mazo.getCarta(cartaNumero).setColor("Rojo");
				mazo.getCarta(cartaNumero).setValor("Rojo");
				
			}
			else if(input2==2)
			{
				System.out.println("**************************");
				System.out.println("Se eligio el color Amarillo");
				System.out.println("**************************");
				mazo.getCarta(cartaNumero).setColor("Amarillo");
				mazo.getCarta(cartaNumero).setValor("Amarillo");
			}
			else if(input2==3)
			{
				System.out.println("**************************");
				System.out.println("Se eligio el color Azul");
				System.out.println("**************************");
				mazo.getCarta(cartaNumero).setColor("Azul");
				mazo.getCarta(cartaNumero).setValor("Azul");
			}
			else if(input2==4)
			{
				System.out.println("**************************");
				System.out.println("Se eligio el color Verde");
				System.out.println("**************************");
				mazo.getCarta(cartaNumero).setColor("Verde");
				mazo.getCarta(cartaNumero).setValor("Verde");
			}
		}
		else
		{
			System.out.println("**************************");
			System.out.println("Se eligio el color Rojo");
			System.out.println("**************************");
			mazo.getCarta(cartaNumero).setColor("Rojo");
			mazo.getCarta(cartaNumero).setValor("Rojo");
		}
	}
	// Metodo si es el jugador Humano

	// Método para carta +4
	private void masCuatro(int cartaNumero, int jugador)
	{
		if(mazo.getCarta(cartaNumero).getValor()=="+4")
		{	int cartaNumero2=generarCarta();
			int cartaNumero3=generarCarta();
			int cartaNumero4=generarCarta();
			int cartaNumero5=generarCarta();
			if(jugador==0)
			{
				robarCarta(1, cartaNumero2);
				robarCarta(1, cartaNumero3);
				robarCarta(1, cartaNumero4);	
				robarCarta(1, cartaNumero5);
				cantidadCartasRestantes=cantidadCartasRestantes-4;
				mostrarPozo(mazo.getCarta(this.cartaNumeroPozo));
				//Cambio de color
				actualizarPozo(cartaNumero, jugador);
				nuevaJugada(0);
			}
			else
			{
				robarCarta(0, cartaNumero2);
				robarCarta(0, cartaNumero3);
				robarCarta(0, cartaNumero4);	
				robarCarta(0, cartaNumero5);
				cantidadCartasRestantes=cantidadCartasRestantes-4;
				mostrarPozo(mazo.getCarta(this.cartaNumeroPozo));
				//cambio de color
				actualizarPozo(cartaNumero, jugador);
				nuevaJugada(1);
			}
		}
	}
	//metodo nueva jugada
	private void nuevaJugada(int jugador)
	{
		if(jugadores[jugador].getMano().largo()==0)
		{
			
		}
		else{

		
		int cartaNumero=-1;
		Carta carta;
		boolean flag=false;
		Scanner teclado = new Scanner(System.in);
		//valida que el jugador tenga al menos una carta válida para jugar
		
		if(validarMano(jugadores[jugador].getMano())){
			// se printea el nombre del jugador 0, osea nosotros
			if(jugador==0){
				System.out.println("Es el turno del jugador "+ jugadores[0].getNickname());
			}
			// se printea el nombre del jugador 1, osea la maquina
			else{
			System.out.println("Es el turno del jugador "+jugador);
			}
			if(jugador==0){
				while(!flag){ //repite hasta que seleccione una carta válida
					cartaNumero=seleccionarCarta();
					if(validarJugada(cartaNumero)){
						flag=true;
					}else{
						System.out.println("No puede jugar esa carta");
					}
				}
			}else{
				cartaNumero=generarJugada(jugador);
			}
			// aca se deben meter los cambios de cartas especiales
			cambioColor(cartaNumero,jugador,cartaNumeroPozo);
			this.cartaNumeroPozo=cartaNumero;//
			this.mazo.setUso(cartaNumero);//
			this.jugadores[jugador].getMano().borrarCarta(this.jugadores[jugador].getMano().buscarCarta(cartaNumero));
			cantidadCartasRestantes--;
			// Cartas especiales
			BloqueoOsentido(cartaNumero, jugador);
			masDos(cartaNumero, jugador);
			masCuatro(cartaNumero, jugador);
			cantidadCartasRestantes--;
			// Final cartas especiales
					
		}else{
			System.out.println("El jugador "+jugadores[jugador].getNickname()+" no tiene carta para jugar, roba una carta");
			cartaNumero=generarCarta();
			robarCarta(jugador,cartaNumero);
			cantidadCartasRestantes--;
			if(jugador==0)
				mostrarMano(jugador);
		}
		carta=this.mazo.getCarta(this.cartaNumeroPozo);	
		//mostrarPozo(carta);	
		}
	}
}