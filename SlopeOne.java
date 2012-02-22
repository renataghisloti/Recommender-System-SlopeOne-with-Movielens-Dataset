/* Renata Ghisloti Duarte de Souza */

import java.util.*;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileOutputStream;


/* Slope One version
 * ****Simple slope one*****
 * Here is presented only the calculation of the diffs matrix (the most expensive part)
 * the predction part is in another file
 */

public class SlopeOne {

    int maxItemsId = 0;
    float mteste[][];
    int mFreq[][];
    Map<Integer,Map<Integer,Float>> usersMatrix;

    public static void main(String args[]){

        long start = System.currentTimeMillis();
        SlopeOne so = new SlopeOne();

        /* Estimates time */
        long end = System.currentTimeMillis();
        System.out.println("\nExecution time was "+(end-start)+" ms.");
    }


    public SlopeOne(){

        readInput();
        buildDiffMatrix();


        /* Print the output */
        try{
            FileOutputStream output = new FileOutputStream("slope-intermidiary-output.txt");
            /* Print the maximum number of items */
            output.write(String.valueOf(maxItemsId).getBytes());
            output.write( String.valueOf("\n").getBytes()  );

            for(int i = 1; i <= maxItemsId; i++){
                for(int j = i; j <= maxItemsId; j++){

                    if(!Float.isNaN (mteste[i][j])){
                        /* Print the rates */
                        output.write( String.valueOf(i).getBytes()  );
                        output.write( String.valueOf("\t").getBytes()  );
                        output.write( String.valueOf(j).getBytes()  );
                        output.write( String.valueOf("\t").getBytes()  );
                        output.write( String.valueOf( mteste[i][j] ).getBytes());
                        output.write( String.valueOf("\n").getBytes()  );

                        /* Print the frequencies */
                        output.write( String.valueOf(i).getBytes()  );
                        output.write( String.valueOf("\t").getBytes()  );
                        output.write( String.valueOf(j).getBytes()  );
                        output.write( String.valueOf("\t").getBytes()  );
                        output.write( String.valueOf( mFreq[i][j] ).getBytes());
                        output.write( String.valueOf("\n").getBytes()  );
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Function readInput()
     * Read the input and saves it in the usersMatrix
     *
     */
    public void readInput(){
        File file = new File("ratings_set1.dat");

        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        int i;
        int j;

        usersMatrix = new HashMap<Integer,Map<Integer,Float>>();
        String line;

        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);

            line = dis.readLine();

            /*
             * First get all ratings from one user, 
             * calculate the diffs for this user and save them 
             * in the mDiffMatrix
             */

            while (dis.available() != 0) {

                StringTokenizer t = new StringTokenizer(line, ","); 
                int user =  Integer.parseInt(t.nextToken());
                int tempUser = user;

                usersMatrix.put(user, new HashMap<Integer,Float>());

                // Read all lines for one user
                while(user == tempUser){

                    /* Get item */
                    i = Integer.parseInt(t.nextToken());

                    /* Get the quantity of items by finding the maximun value
                    * of itemId */
                    maxItemsId = maxItemsId < i ? i : maxItemsId;

                    /* Save rating */
                    usersMatrix.get(user).put(i, Float.parseFloat(t.nextToken()) );

                    if(dis.available()!=0){
                        line = dis.readLine();
                        t = new StringTokenizer(line, ",");
                        tempUser = Integer.parseInt(t.nextToken());
                    }
                   else
                        tempUser = -1;

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



    /*
     * Function buildDiffMatrix()
     * Calculates the DiffMatrix for all items
     *
     */
    public  void buildDiffMatrix() {

        mteste = new float[maxItemsId+1][maxItemsId+1];
        mFreq = new int[maxItemsId+1][maxItemsId+1];

        for(int i = 1; i <= maxItemsId; i++)
            for(int j = 1; j <= maxItemsId; j++){
                mteste[i][j] = 0;
                mFreq[i][j] = 0;
            }

        /* Iterate through all users, and then, through all items do calculate the diffs */
        for(int cUser : usersMatrix.keySet()){
            for(int i: usersMatrix.get(cUser).keySet()){
                for(int j : usersMatrix.get(cUser).keySet() ){
                    mteste[i][j] = mteste[i][j]  + 
                                   ( usersMatrix.get(cUser).get(i).floatValue() - (usersMatrix.get(cUser).get(j).floatValue()));
                    mFreq[i][j] = mFreq[i][j] + 1;
                }
            }
        }

        /*  Calculate the averages (diff/freqs) */
        for(int i = 1; i<= maxItemsId; i++){
            for(int j = i; j <= maxItemsId; j++){
                if(mFreq[i][j] > 0){
                    mteste[i][j] = mteste[i][j] / mFreq[i][j];
                }
            }
        }
    }

}


