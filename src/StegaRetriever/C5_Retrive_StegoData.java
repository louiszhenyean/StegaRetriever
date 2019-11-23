/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StegaRetriever;

import java.io.*;
import java.io.File;
import javax.sound.sampled.*;
public class C5_Retrive_StegoData {
     public static void RtrvStego(String Path ) throws UnsupportedAudioFileException{
         //Input Stego Path
         String Path1=Path+"stega.wav";
         String Path2=Path+"Retrieved.txt";
         stegoFileReader stego=new stegoFileReader();
         String retrieveStr=stego.StegoAudioRead(Path1);
         Writefile wrt=new Writefile();
         wrt.filewriting(retrieveStr, Path2);
         System.out.println("StegaData has been retrieved");
         }
        }

class stegoFileReader{
 public  String StegoAudioRead(String AudioPath) throws UnsupportedAudioFileException{
     String FinalStr="";
     try{
        File file=new File(AudioPath);   
        int pos=0;
        int PositionVal=0;
        AudioInputStream ais=AudioSystem.getAudioInputStream(file);  
        byte[] StegoData=new byte[ais.available()];  
        ais.read(StegoData);
        for(int i=0;i<StegoData.length;i++){
          if(i>=2)  
          pos=PrimeNumber(i);
           if(pos==0 && i>=2){
              PositionVal=PositionVal+1;
           if(PositionVal==10)
              PositionVal=1;
           if ((i+PositionVal)>StegoData.length)
               PositionVal=0;
           FinalStr=FinalStr+FindStr(StegoData[i+PositionVal]);
           }
           }   
        }
     catch(IOException ioe ){
         System.err.println(ioe);
         System.exit(1); 
        }
     return FinalStr;
      }
 //counting of prime position
 public int PrimeNumber(int number){
  int j = 2;
  int result = 0;
  
  while (j <= number / 2)
  {
      if (number % j == 0)
      {
         result = 1;
      }
      j++;
  }
  return result;
  }
 //Finding the stego string
 public String FindStr(byte Stegoval){
  String str="";
  String originalStr="";
  int num=(int)Stegoval;
  if (num<0)
      num=num*(-1);
  str=Integer.toBinaryString(num);
  while(str.length()<8)
      str="0"+str;
  char [] Chr=str.toCharArray();
  originalStr=originalStr+Chr[4]+Chr[6];
  originalStr=originalStr.trim();
  return originalStr;
 }
}
class Writefile{
public void filewriting(String retrieveStr, String Path1){
String sub1; 
int count=0;
int final_count=0;
String Final_String="";
int num;
int length=0;
retrieveStr=retrieveStr.trim();
String retrieveStr1=retrieveStr;
while(retrieveStr1.length()>=0){
     sub1=retrieveStr1.substring(0,5);
     retrieveStr1=retrieveStr1.substring(5);
     sub1=sub1.trim();
     num=Integer.parseInt(sub1,2);
     if(num==31)
         count=count+1;
     else count=0;
     if(count==14){
         count=0;
        final_count=final_count+1;
     }
     Final_String=Final_String+sub1;
     length=length+5;
     if(final_count>1)
         break;
    }
length=length-70;
Final_String=Final_String.substring(0, length);
   try{
  // Create file 
  FileWriter fstream = new FileWriter(Path1);
  BufferedWriter out = new BufferedWriter(fstream);
  out.write(Final_String);
  //Close the output stream
  out.close();
     }
   catch (Exception e){//Catch exception if any
  System.err.println("Error: " + e.getMessage());
     }
    }  
  }
         