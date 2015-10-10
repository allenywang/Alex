package ch.idsia.benchmark.mario.engine;
/*************************************************************************
 *  Compilation:  javac -classpath .:jl1.0.jar MP3.java         (OS X)
 *                javac -classpath .;jl1.0.jar MP3.java         (Windows)
 *  Execution:    java -classpath .:jl1.0.jar MP3 filename.mp3  (OS X / Linux)
 *                java -classpath .;jl1.0.jar MP3 filename.mp3  (Windows)
 *
 *  Plays an MP3 file using the JLayer MP3 library.
 *
 *  Reference:  http://www.javazoom.net/javalayer/sources.html
 *
 *
 *  To execute, get the file jl1.0.jar from the website above or from
 *
 *      http://www.cs.princeton.edu/introcs/24inout/jl1.0.jar
 *
 *  and put it in your working directory with this file MP3.java.
 *
 *************************************************************************/

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import javazoom.jl.player.Player;


public class MP32 {
    private String filenameStart;
    private String filenameNext;
    private Player playerOne;
    private Player playerTwo;
    private Player playerThree;
    private Player playerFour;
    private Player playerFive;
    private Player playerSix;
    private Player playerSeven;
    private Player playerEight;

    // constructor that takes the name of an MP3 file
    public MP32() {
        this.filenameStart = "eecs.mp3";
        this.filenameNext = "im-in-eecs.mp3";
    }

    //public void close() { if (player != null) player.close(); }

    // play the MP3 file to the sound card
    public void play() {
        try {
            FileInputStream fis     = new FileInputStream(filenameStart);
            BufferedInputStream bis = new BufferedInputStream(fis);
            playerOne = new Player(bis);

            FileInputStream fisTwo     = new FileInputStream(filenameNext);
            BufferedInputStream bisTwo = new BufferedInputStream(fisTwo);
            playerTwo = new Player(bisTwo);

            FileInputStream fisThree     = new FileInputStream(filenameNext);
            BufferedInputStream bisThree = new BufferedInputStream(fisThree);
            playerThree = new Player(bisThree);

            FileInputStream fisFour     = new FileInputStream(filenameNext);
            BufferedInputStream bisFour = new BufferedInputStream(fisFour);
            playerFour = new Player(bisFour);

            FileInputStream fisFive     = new FileInputStream(filenameNext);
            BufferedInputStream bisFive = new BufferedInputStream(fisFive);
            playerFive = new Player(bisFive);

            FileInputStream fisSix     = new FileInputStream(filenameNext);
            BufferedInputStream bisSix = new BufferedInputStream(fisSix);
            playerSix = new Player(bisSix);

            FileInputStream fisSeven     = new FileInputStream(filenameNext);
            BufferedInputStream bisSeven = new BufferedInputStream(fisSeven);
            playerSeven = new Player(bisSeven);

            FileInputStream fisEight    = new FileInputStream(filenameNext);
            BufferedInputStream bisEight = new BufferedInputStream(fisEight);
            playerEight = new Player(bisEight);


            new Thread() {
                public void run() {
                    try {
                        playerOne.play();
                        playerTwo.play();
                        playerThree.play();
                        playerFour.play();
                        playerFive.play();
                        playerSix.play();
                        playerSeven.play();
                        playerEight.play();
//                        playerNine.play();
                    }
                    catch (Exception e) { System.out.println(e); }
                }
            }.start();
        }
        catch (Exception e) {
            System.out.println("Problem playing file ");
            System.out.println(e);
        }

        // run in new thread to play in background





    }


    // test client
    public static void main(String[] args) {
        String filename = args[0];
        MP32 mp3 = new MP32();
        mp3.play();

        // do whatever computation you like, while music plays
        int N = 4000;
        double sum = 0.0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                sum += Math.sin(i + j);
            }
        }
        System.out.println(sum);

        // when the computation is done, stop playing it
        //mp3.close();

        // play from the beginning
        mp3 = new MP32();
        mp3.play();

    }

}
