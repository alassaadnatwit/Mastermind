package masterMind;

public class GameInput {

    private static boolean win = false;

    public static void main(String[] args) {
        //GameCode game = new GameCode(false);
        //game.getPattern();
        //runGame(game);
    }

    public static void runGame(GameCode game){
        int rowcounter;
        for(rowcounter = 0; rowcounter <game.getRows(); rowcounter++){
            //game.guess();
            //game.pins();
            //game.getPins();
            game.clear();
            if(false){
                rowcounter = game.getRows();
                win = true;
                System.out.println("YOU WIN");
            }
            game.setPins();
        }
        if(rowcounter == game.getRows() && !win){
            System.out.println("you lose.");
        }
    }
}
