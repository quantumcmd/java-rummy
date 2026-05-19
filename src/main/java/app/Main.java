package app;

import ui.MainMenu;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        MainMenu mainMenu = new MainMenu(scanner);
        Game game = new Game(mainMenu);
        game.start();
    }
}
