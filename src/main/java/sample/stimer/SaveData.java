package sample.stimer;

import java.io.*;
import java.util.ArrayList;


public class SaveData {
    //ImePredmeta@casVsekundah

    private File dir = new File("C:\\StudyBudy");
    private int numOfPredmetov;
    private File data = new File("C:\\StudyBudy\\data.txt");
    private ArrayList<String> arrayListPredmetov;
    private ArrayList<Integer> arrayListPredmetovCas;



    public SaveData() throws IOException {

        checkDir();
        checkFileData();
        checkFilePredmetov();
        this.numOfPredmetov = this.numOfPredmetovFun();
        this.arrayListPredmetov = arrayOfPredmetov();
        //this.arrayListPredmetovCas = arrayOfCas();
    }


    public int getNumOfPredmetov() {
        return numOfPredmetov;
    }

    public ArrayList<String> getArrayListPredmetov() {
        return arrayListPredmetov;
    }

    public void checkDir() throws IOException {
        if (!dir.exists()) {
            if (!dir.mkdir()) {
                throw new IOException("Failed to create directory: " + dir.getAbsolutePath());
            }
        }
    }

    public void checkFileData() throws IOException {
        if (!data.exists()) {
            if (!data.createNewFile()) {
                throw new IOException("Failed to create file: " + data.getAbsolutePath());
            }
        }
    }

    public void checkFilePredmetov() throws IOException {
        if (arrayListPredmetov != null) {
            for (String predmet : arrayListPredmetov) {
                File file = new File("C:\\StudyBudy\\" + predmet + ".txt");
                if (!file.exists()) {
                    if (!file.createNewFile()) {
                        throw new IOException("Failed to create file: " + file.getAbsolutePath());
                    }
                }
            }
        }
    }


    public void newPredmet(String predmet) {
        try {
            BufferedReader file = new BufferedReader(new FileReader(data));
            StringBuffer inputBuffer = new StringBuffer();
            String line;

            while ((line = file.readLine()) != null) {
                inputBuffer.append(line);
                inputBuffer.append('\n');
            }
            line = predmet + "@0";
            inputBuffer.append(line);
            inputBuffer.append('\n');
            file.close();

            // write the new string with the replaced line OVER the same file
            FileOutputStream fileOut = new FileOutputStream(data);
            fileOut.write(inputBuffer.toString().getBytes());
            fileOut.close();
            numOfPredmetov++;
            arrayListPredmetov.add(predmet);
            File f = new File("C:\\StudyBudy\\"+predmet+".txt");
            f.createNewFile();
            System.out.println(f.getAbsolutePath());

        } catch (Exception e) {
            System.out.println("Problem reading file. " + data.getAbsolutePath());
        }

    }

    public void updateTime(String predmet, int time) {
        try {
            BufferedReader file = new BufferedReader(new FileReader(data));
            StringBuffer inputBuffer = new StringBuffer();
            String line;

            while ((line = file.readLine()) != null) {
                if (line.contains(predmet)) {
                    String[] newLineArray = line.split("@");
                    newLineArray[1] = Integer.toString(Integer.parseInt(newLineArray[1]) + time);
                    line = newLineArray[0] + "@" + newLineArray[1];
                }
                inputBuffer.append(line);
                inputBuffer.append('\n');
            }
            file.close();

            // write the new string with the replaced line OVER the same file
            FileOutputStream fileOut = new FileOutputStream(data);
            fileOut.write(inputBuffer.toString().getBytes());
            fileOut.close();


        } catch (Exception e) {
            System.out.println("Problem reading file. " + data.getAbsolutePath());
        }
    }

    public void delete(String predmet) {
        try {
            BufferedReader file = new BufferedReader(new FileReader(data));
            StringBuffer inputBuffer = new StringBuffer();
            String line;

            while ((line = file.readLine()) != null) {
                if (!line.contains(predmet)) {
                    inputBuffer.append(line);
                    inputBuffer.append('\n');
                }
            }
            file.close();

            FileOutputStream fileOut = new FileOutputStream(data);
            fileOut.write(inputBuffer.toString().getBytes());
            fileOut.close();
            numOfPredmetov--;
            arrayListPredmetov.remove(predmet);
            File f = new File("C:\\StudyBudy\\"+predmet+".txt");
            f.delete();
            System.out.println(f.getAbsolutePath());

        } catch (Exception e) {
            System.out.println("Problem reading file. " + data.getAbsolutePath());
        }
    }

    public void deleteAll() {
        try {
            FileWriter myWriter = new FileWriter(data);
            myWriter.write("");
            myWriter.close();
            numOfPredmetov = 0;
            arrayListPredmetov = null;
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public int numOfPredmetovFun() {
        int c = 0;
        try {
            BufferedReader file = new BufferedReader(new FileReader(data));
            StringBuffer inputBuffer = new StringBuffer();
            String line;

            while ((line = file.readLine()) != null) {
                c = c + 1;
            }
            file.close();


        } catch (Exception e) {
            System.out.println("Problem reading file. " + data.getAbsolutePath());
            return -1;
        }
        if (c <= 0) {
            return -1;
        } else return c;
    }

    public ArrayList<String> arrayOfPredmetov() {
        ArrayList<String> arrayListPredmetov = new ArrayList<>();
        try {
            BufferedReader file = new BufferedReader(new FileReader(data));
            String line;

            while ((line = file.readLine()) != null) {
                String[] newLineArray = line.split("@");
                arrayListPredmetov.add(newLineArray[0]);

            }
            file.close();

        } catch (Exception e) {
            System.out.println("Problem reading file. " + data.getAbsolutePath());
            return null;
        }
        return arrayListPredmetov;
    }


    public int timeUcenja(String predmet) {
        try {
            BufferedReader file = new BufferedReader(new FileReader(data));
            String line;

            while ((line = file.readLine()) != null) {
                if (line.contains(predmet)) {
                    String[] newLineArray = line.split("@");

                    return Integer.parseInt(newLineArray[1]);
                }
            }
            file.close();
        } catch (Exception e) {
            System.out.println("Problem reading file. " + data.getAbsolutePath());

        }
        return 0;
    }

     public int [] getArrayOfCas (){
        int [] arrayCas = new int[arrayListPredmetov.size()];
        int c = 0;
        try {
            BufferedReader file = new BufferedReader(new FileReader(data));
            String line;

            while ((line = file.readLine()) != null) {
                String[] newLineArray = line.split("@");
                arrayCas[c] = Integer.parseInt(newLineArray[1]);
                c++;

            }
            file.close();

        } catch (Exception e) {
            System.out.println("Problem reading file. " + data.getAbsolutePath());
            return null;
        }
        return arrayCas;
    }

}