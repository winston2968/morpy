package test;

import java.util.Scanner;

import commandline.Morpion;
import generalClasses.Client;
import generalClasses.Server;

public class AlgoCLTest {
	
	public char[][] grille = new char[3][3]; // plateau de jeu
	public boolean connexionMode ; // true si serveur ou false si client
	public char symbol ; // Symbole par lequel le joueur remplit le tableau
	Client client ;
	Server server ;

	
	public AlgoCLTest() {
		
		// Création du plateau de jeu
		for (int i = 0; i < this.grille.length; i = i + 1) {
			for (int j = 0; j < this.grille[i].length; j = j + 1) {
				this.grille[i][j] = '.';
			}
		}
	}
	

	
	/*
	 * ================================================================================================================================================================
	 * 															Méthodes pour la gestion du jeu
	 * ================================================================================================================================================================
	 */
	
	// ========================== Initialisation du jeu ==========================
	
	public void startGame() {
		System.out.println("Bienvenue \n");
		
		Scanner scan = new Scanner(System.in);
		System.out.print("""
				Morpy/:$ --- Choisissez votre mode de connexion (un serveur et un client par partie):
								1 -> Client
								2 -> Serveur
				Morpy/:$
				""");
		int nombre = scan.nextInt();
		if (nombre == 1) {
			this.connexionMode = false ;
			this.symbol = 'O';
		} else {
			this.connexionMode = true ;
			this.symbol = 'X';
		}
		
		if (this.connexionMode == true) {
			this.server = new Server();
			System.out.println("""
					Morpy/:$ --- Vous avez choisit une connexion serveur :
							 --- Communiquez l'adresse ci-dessous à votre partenaire pour qu'il se connecte
						     --- Adresse : """ + this.server.ipAdress);
			this.server.connectToClient();
			
		} else {
			this.client = new Client() ;
			System.out.print("""
					Morpy/:$ --- Vous avez choisit une connexion client.
						     --- Entrez l'adresse comuniquée par votre adversaire ---
					Morpy/:$ """);
			scan.next(); // Pour récupérer le "\n" restant de l'entrée int précédente
			String entree = scan.nextLine();
			this.client.serverAdress = entree ;
			this.client.connectToServer();
		}
		
	}
	
	// ========================== Envoi et récupération des données ==========================
	
	public void envoyer() {
		if (this.connexionMode == true) {
			this.server.sendToClient(this.grille.toString());
		} else {
			this.client.sendToServer(this.grille.toString());
		}
	}
	
	/*
	public int[][] recevoir() {
		if (this.connexionMode == true) {
			
		}
	}
	*/
	
	/*
	 * ================================================================================================================================================================
	 * 															Gestion d'un tour
	 * ================================================================================================================================================================
	 */
	
	public void tour(char c) {
		
		// Vérification si le coup joué par l'adversaire n'est pas gagnant
		if (this.partieFinie() == true) {
			System.out.println("Morpy/:$ --- Votre adversaire a gagné !");
		}
		
		// Séparation grahique de l'ancien coup
		System.out.println("""
				=========================================================================
				Morpy/:$ --- A votre tour de jouer !
				""");
		System.out.println("\n" + this.affiche() + "\n");
		
		// Récupération des coordonnées
		int[] coord = this.getCoordonnees();

		while (this.case_vide(coord[0] - 1, coord[1] - 1) == false) {
			System.out.println("Morpy/:$ --- /!\\ La case saisie n'est pas libre ! /!\\");
			coord = this.getCoordonnees();
		}
		
		// Entrée de la nouvelle valeur dans le tableau
		this.grille[coord[0] - 1][coord[1] - 1] = c;
		
		// Vérification que le coup entré n'est pas gagnant
		if (this.partieFinie() == true) {
			System.out.println("Morpy/:$ --- Vous avez gagné !");
			System.exit(0);
		} 
	}
	
	/*
	 * ================================================================================================================================================================
	 * 															Méthodes pour la gestion du plateau
	 * ================================================================================================================================================================
	 */
	
	
	
	public String affiche() { // Renvoie le plateau
		String retour = "" ;
		for (int i = 0; i < this.grille.length; i = i + 1) {
			for (int j = 0; j < this.grille[i].length; j = j + 1) {
				retour += this.grille[i][j] + " ";
			}
			retour += "\n";
		}
		return retour ;
	}
	
	public char[][] getPlateau() {
		return this.getPlateau();
	}
	
	
	public int[] getCoordonnees() { // Demande à l'utilisateur de saisir le coup à jouer 
		Scanner scan = new Scanner(System.in);
		// scan.next();
		int[] retour = new int[2] ;
		retour[0] = -1 ;
		retour[1] = -1 ;
		while ((retour[0] >= 1 && retour[0] <= 3) == false || (retour[1] >= 1 && retour[1] <= 3) == false) {
			System.out.println("Morpy/:$ --- Entrez une ligne :");
			System.out.print("Morpy/:$ ");
			retour[0] = scan.nextInt();
			System.out.println("Morpy/:$ --- Entrez une colonne :");
			System.out.print("Morpy/:$ ");
			retour[1] = scan.nextInt();	
		}
		return retour ;
	}


	public boolean case_vide(int i, int j) { // Vérifie si une case du plateau est vide 
		if (this.grille[i][j]=='.' ) {
			return true;
		}
		return false;
	}
	
	public boolean ligne_vide(int i) { // Vérifie si une ligne est vide (utile plus tard)
		if (this.case_vide(i, 0) && this.case_vide(i, 1) && this.case_vide(i, 2)) {
			return true;
		}
		return false;
	}
	
	public boolean partieFinie() { // Test si une partie est finie
		if (this.tableau_plein() == true && this.gagne() == false) {
			System.out.println("Morpy/:$ --- PARTIE FINIE --- ");
			System.out.println("Morpy/:$ --- Aucun des deux joueurs n'a gagné !");
			System.exit(0);
		} else if (this.gagne() == true) {
			System.out.println("Morpy/:$ --- Partie Finie --- ");
			return true ;
		} 
		return false ;
	}
	
	public boolean tableau_plein() { // Test si le plateau est plein
		if (!this.ligne_vide(0) && !this.ligne_vide(1) && !this.ligne_vide(2)) {
			return true;
		}
		return false;
	}
	
	public boolean gagne() { // Test si une partie est gagnée (sous cas d'une partie finie)
		for (int i = 0; i < this.grille.length; i = i + 1) {
			if ((this.grille[i][0] == this.grille[i][1] && this.grille[i][1] == this.grille[i][2])
					&& this.grille[i][0] != '.') {
				return true;
			} else if ((this.grille[0][i] == this.grille[1][i] && this.grille[1][i] == this.grille[2][i])
					&& this.grille[0][i] != '.') {
				return true;
			} else if ((this.grille[0][0] == this.grille[1][1] && this.grille[1][1] == this.grille[2][2])
					&& this.grille[0][0] != '.') {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		AlgoCLTest test = new AlgoCLTest();
		test.affiche();
		while (test.partieFinie() != true) {
			test.tour('X');
			test.affiche();
			test.tour('O');
			test.affiche();
		}
	}

}
