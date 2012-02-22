/* Renata Ghisloti Duarte de Souza */

import java.util.*;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileOutputStream;


public class Predict{

    Map<Integer,Float> user = new HashMap<Integer, Float>();
    HashMap<Integer,Float> predictions = new HashMap<Integer,Float>();
    /* User to whom the prediction will be made */
    int targetUser = 2;
    float mDiff[][];
    float mFreq[][];
    /* Contains the maximun number of items */
    int maxItem;

    public static void main(String args []){
        long start = System.currentTimeMillis();
        Predict newPrediction = new Predict();
        /* Estimates time */
        long end = System.currentTimeMillis();
        System.out.println("\nExecution time was "+(end-start)+" ms.");
    }

    public Predict(){

        getUser(targetUser);
        readDiffs();
        float totalFreq[] =  new float [maxItem+1];

        /* Start prediction */
        for (int j=1; j <= maxItem; j++) {
            predictions.put(j,0.0f);
            totalFreq[j] = 0;
        }

        for (int j : user.keySet()) {
            for (int k = 1; k <= maxItem; k++) {
                if( j != k) {
                    /* Only for items the user has not seen */
                    if(!user.containsKey(k)){
                        float newVal = 0;
                        if(k < j) {
                            newVal = mFreq[j][k] * (mDiff[j][k] + user.get(j).floatValue());
                        }
                        else {
                            newVal = mFreq[j][k] * (-1 * mDiff[j][k] + user.get(j).floatValue());
                        }
                        totalFreq[k] = totalFreq[k] + mFreq[j][k];
                        predictions.put(k, predictions.get(k).floatValue() + newVal);
                    }
                } 
            }
        }

        /* Calculate the average */
        for (int j : predictions.keySet()) {
            predictions.put(j, predictions.get(j).floatValue()/(totalFreq[j] ));
        }

        /* Fill the predictions vector with the already known rating values */
        for (int j : user.keySet()) {
            predictions.put(j, user.get(j));
        }

        /* Print predictions */
        System.out.println("\n" + "#### Predictions #### ");
        for (int j : predictions.keySet()) {
            System.out.println( j + " " + predictions.get(j).floatValue());
        }
    }

    /*
     * Function readDiff()
     * Read the precalculated Diffs between items
     *
     */
    public void readDiffs(){

        File foutput = new File("slope-intermidiary-output.txt");
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;

        try {
            fis = new FileInputStream(foutput);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);

            String line = dis.readLine();
            StringTokenizer t = new StringTokenizer(line, "\t");
            maxItem = Integer.parseInt(t.nextToken());
            mDiff = new float[maxItem + 1][maxItem + 1];
            mFreq = new float[maxItem + 1][maxItem + 1];

            for(int i = 1; i <= maxItem; i++)
              for(int j = 1; j <= maxItem; j++){
                mDiff[i][j] = 0;
                mFreq[i][j] = 0;
              }

            System.out.println("\n" + "#### Diffs #### ");

            while(dis.available() != 0){

                line = dis.readLine();
                t = new StringTokenizer(line, "\t");
                int itemID1 = Integer.parseInt(t.nextToken());
                int itemID2 = Integer.parseInt(t.nextToken());

                mDiff[itemID1][itemID2] = Float.parseFloat(t.nextToken());

                line = dis.readLine();
                t = new StringTokenizer(line, "\t");
                itemID1 = Integer.parseInt(t.nextToken());
                itemID2 = Integer.parseInt(t.nextToken());

                mFreq[itemID1][itemID2] = Float.parseFloat(t.nextToken());
            }

            fis.close();
            bis.close();
            dis.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*
     * Function getUser()
     * Get already known user preferences
     *
     */
    public  void getUser( int userID ){

        File fuser = new File("ratings_set1.dat");
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;

        System.out.println("\n" + "#### Initial User x Item x Rating #### ");
        try {
            fis = new FileInputStream(fuser);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);


            while(dis.available() != 0){

                String line = dis.readLine();

                StringTokenizer t = new StringTokenizer(line, ",");
                int tempUser = Integer.parseInt(t.nextToken());

                while(tempUser == userID){
                    user.put(Integer.parseInt(t.nextToken()),Float.parseFloat(t.nextToken()  ));
                    line = dis.readLine();
                    t = new StringTokenizer(line, ",");
                    tempUser = Integer.parseInt(t.nextToken());
                }
            }

            fis.close();
            bis.close();
            dis.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
