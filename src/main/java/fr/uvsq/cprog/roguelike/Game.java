package fr.uvsq.cprog.roguelike;


import java.util.Scanner;

/**
 * Classe principale du jeu qui gère l'exécution de la partie.
 */
public class Game {

  private transient Player player;
  private World world;
  private int level;

  private boolean isFinished;


  /**
   * Constructeur de la classe Game qui crée un nouveau monde en fonction du niveau choisi.
   *
   * @param level le niveau choisi pour la partie
   */
  public Game(int level) {
    this.level = level;

    this.world = new WorldBuilder(level)
        .addSols()
        .addWalls()
        .addRandomWalls()
        .addMonsters()
        .addWeapons()
        .addPlayer()
        .addOutput()
        .build();
    this.player = world.getPlayer();
    this.isFinished = false;
  }

  /**
   * Récupère le joueur du jeu.
   *
   * @return le joueur du jeu
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * Modifie le joueur du jeu.
   *
   * @param player le nouveau joueur du jeu
   */

  public void setPlayer(Player player) {
    this.player = player;
  }

  /**
   * Récupère le monde du jeu.
   *
   * @return le monde du jeu
   */

  public World getWorld() {
    return world;
  }

  /**
   * Modifie le monde du jeu.
   *
   * @param world le nouveau monde du jeu
   */
  public void setWorld(World world) {
    this.world = world;
  }

  /**
   * Récupère le niveau du jeu.
   *
   * @return le niveau du jeu
   */

  public int getLevel() {
    return level;
  }

  /**
   * Modifie le niveau du jeu.
   *
   * @param level le nouveau niveau du jeu
   */

  public void setLevel(int level) {
    this.level = level;
  }

  /**
   * Indique si le jeu est fini ou non.
   *
   * @return vrai si le jeu est fini, faux sinon
   */
  public boolean isFinished() {
    return isFinished;
  }

  /**
   * Modifie l'état de fin du jeu.
   *
   * @param finished vrai si le jeu est fini, faux sinon
   */
  public void setFinished(boolean finished) {
    isFinished = finished;
  }

  /**
   * Méthode principale qui exécute la partie jusqu'à ce que le joueur meurt ou termine le niveau.
   */
  public void runGame() {

    Scanner scanner = new Scanner(System.in);

    while ((player.isAlive()) && !(this.isFinished())) {
      String input = scanner.nextLine();
      Game.clearConsole();
      Command command = null;
      int dx;
      int dy;
      switch (input) {
        case "z":
          dx = -1;
          dy = 0;
          command = new MoveCommand(player, dx, dy, world);
          break;

        case "s":
          dx = 1;
          dy = 0;
          command = new MoveCommand(player, dx, dy, world);
          break;

        case "d":
          dx = 0;
          dy = 1;
          command = new MoveCommand(player, dx, dy, world);
          break;

        case "q":
          dx = 0;
          dy = -1;
          command = new MoveCommand(player, dx, dy, world);
          break;

        case "e":
          command = new PickUpWeaponCommand(player, world);
          break;

        case "a":
          command = new AttackCommand(player, world);
          break;
        case "exit":
          System.out.println("vous etes au point de quitter la parite, vous voulez sauvegareder la partie ? O/N");
          input = scanner.nextLine();
          switch (input) {
            case "O":
              command = new SaveCommand(this);
              break;
            case "N":
              System.exit(0);
          }
          break;

        default:
          System.out.println("Commande erronée");
          displayGame();
          break;
      }

      if (command != null) {
        command.execute();
        moveMonsters();
        checkLevelCompleted();
        displayGame();

      }
      if (!player.isAlive()) {
        System.out.println("You have died. Game over.");
        System.out.println("Voulez-vous rejouer ? O/N");
        String input1 = scanner.nextLine();
        if (input1.equals("O")) {
          Game newGame = new Game(this.level);
          newGame.runGame();
        } else {
          break;
        }
      }
    }
  }

  /**
   * Déplace les monstres et fait attaquer les monstres situés à côté du joueur.
   */
  public void moveMonsters() {
    for (Monster monster : world.getMonsters()) {
      monster.move(world);
      if (((Math.abs(player.getX() - monster.getX()) == 1) && (player.getY() - monster.getY() == 0))
          || (((Math.abs(player.getY() - monster.getY()) == 1) && (player.getX() - monster.getX() == 0)))) {
        monster.attack(player);
      }
    }
  }


}
