/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StegaRetriever;


import java.util.*;
import java.io.*;
import java.io.File;
import javax.sound.sampled.*;
public class C3_Audio_Steganography {
  public static void AudioSte(String Path) throws UnsupportedAudioFileException{
  //Input Audio Path
  String Path1=Path+"inputaudio.wav";
  //input compressed file path
  String Path2=Path+"compressed_db.txt";
  //Output file path
  String Path3=Path+"stega.wav";
    AudioReader AudioStr=new AudioReader();
    ArrayList AudioString=AudioStr.AudioRead(Path1, Path2);
    StegoAudioFile stegoAudio=new StegoAudioFile();
    stegoAudio.Audio_creation(AudioString, Path1, Path3);
    System.out.println("stego file has been created ");
     }
  } 

class AudioReader{  
public ArrayList AudioRead( String Path, String Path2) throws UnsupportedAudioFileException{   
 ArrayList AudioStr=new ArrayList(); 
 TextOperation text=new TextOperation();
 String InputStr=text.OpeningStr(Path2);
 if((InputStr.length()%2)==1)
     InputStr=InputStr+"1";
 //opening of audio file
 try{ 
 File file=new File(Path);
 AudioInputStream ais=AudioSystem.getAudioInputStream(file);
 byte[] data=new byte[ais.available()];
 ais.read(data);
 int[] dataVal=new int[(int)file.length()];
 int[] Symbol=new int[(int) file.length()];
 stegonography Ste=new stegonography();
 // copying input audio sample to byte array
  int PositionVal=0;
 // Separating symbol and data of audio file
for (int i=0; i<(data.length);i++){
      if(((int)data[i])<0){
         dataVal[i]=(-1)*((int)data[i]);
         Symbol[i]=(-1);
      }
      else{
       dataVal[i]=((int)data[i]);
       Symbol[i]=1;
      }
    }
int pos=0;
for(int i=0;i<data.length;i++){
    //finding of prime position
    if(i>=2)
    pos=PrimeNumber(i);
     // selection the position value of sample for incorporation
      if(pos==0 && i>=2){
        PositionVal=PositionVal+1; 
        if(PositionVal==10)
            PositionVal=1;
        if(InputStr.length()>0){
          String SubStr=InputStr.substring(0,2);
          InputStr=InputStr.substring(2);
          int num= Ste.Stego(SubStr,dataVal[i+PositionVal]);
          dataVal[i+PositionVal]=num;
          }
       }
    }
for(int i=0;i<data.length;i++)
AudioStr.add(dataVal[i]*(Symbol[i]));
 }
  catch(IOException ioe ){
System.err.println(ioe);
System.exit(1); 
} 
 return AudioStr;
 }
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
}
class stegonography{
public int Stego(String Sub, int val){
 // Converting Audio file into binary string
   // System.out.println(val);
String str=Integer.toBinaryString(val);
while(str.length()<8)
    str="0"+str;
//System.out.println(str);
String S1=(Sub.substring(0,1));
String S2=Sub.substring(1,2);
char s11=S1.charAt(0);
char s12=S2.charAt(0);
char [] chr=str.toCharArray();
//when 4th bit is changing from 0 to 1
if(chr[4]=='0' && s11=='1' ){
  chr[4]=s11;
  if(chr[5]=='1' && chr[3]=='1' ){
     for(int i=5; i<=7;i++)
         chr[i]='0';
  }   
   if(chr[5]=='1' && chr[3]=='0' ){
     for(int i=5; i<=7;i++)
         chr[i]='0';
  } 
    if(chr[5]=='0' && chr[3]=='1' ){
         chr[3]='0';
     for(int i=5; i<=7;i++)
         chr[i]='1';
  }  
     if(chr[5]=='0' && chr[3]=='0' ){
     for(int i=5; i<=7;i++)
         chr[i]='0';
    
  }    
}
// if the 4th bit changes from 1 to 0
if(chr[4]=='1' && s11=='0' ){
  chr[4]=s11;
  if(chr[5]=='0' && chr[3]=='0' ){
     for(int i=5; i<=7;i++)
         chr[i]='1';
  }   
   if(chr[5]=='0' && chr[3]=='1' ){
     for(int i=5; i<=7;i++)
         chr[i]='1';
  } 
    if(chr[5]=='1' && chr[3]=='0' ){
         chr[3]='1';
     for(int i=5; i<=7;i++)
         chr[i]='0';
  }  
     if(chr[5]=='1' && chr[3]=='1' ){
     for(int i=5; i<=7;i++)
         chr[i]='0';
     
  }    
}   
//if the 7th bit changes from 1 t0 0 
if(chr[6]=='1' && s12=='0' ){
    chr[6]=s12;
    chr[7]='1';
}
//if the 7th bit changes from 0 t0 1 
if(chr[6]=='0' && s12=='1' ){
    chr[6]=s12;
    chr[7]='0';
} 
String NumStr=String.valueOf(chr);
int num=Integer.parseInt(NumStr,2);
return num;
  }
}
class TextOperation{
public String OpeningStr(String Path2){
    String textLine;
    String C1="";
 try{
//reading the input file
FileReader fr= new FileReader(Path2);
BufferedReader reader= new BufferedReader(fr); 
ArrayList a = new ArrayList(); 
ArrayList b= new ArrayList();
for(;(textLine=reader.readLine())!= null;a.add(textLine));
Object ia[] = a.toArray();
//converting the file into character array
for(int i=0; i<ia.length ; i++)
{   
   String s = (String)ia[i];
   char ch[] = s.toCharArray();
   store1 s1 = new store1(ch,s);
   b.add(s1);
}
       int length=0;
Object ib[] = b.toArray();
for(int i=0; i< ib.length; i++) 
{
    store1 a1 =  (store1) ib[i];
    length=length+a1.cpy()+1; 
}
char[] c1=new char[length];
int k=0;
for(int i=0;i< b.size();i++){
    store1 a1=(store1) ib[i]; 
    char[] temp=new char[a1.cpy()];
    temp=a1.fun1();
    for(int j=0;j<a1.cpy();j++){  
         if(k==(length-1))
             break;
         else{
           c1[k++]=temp[j];
         }
    }
    if(k>=length)
       break;   
   }
for(int i=0;i<length;i++)  { 
String str1=String.valueOf(c1[i]);   
// eleminating of white space from the binary data
str1=str1.trim();
C1=C1+str1;
}
}
catch(IOException ioe ){
System.err.println(ioe);
System.exit(1); 
    }
       return C1;
  }
}
class store1 {
    char ch[];
    String st;
    public store1(char[]c ,String s )
    {
        ch=c;
        st=s;  
    }   
    public int cpy(){
    int length=ch.length;
    return (length);
    }
    public char[] fun1(){
        return ch;
    }
}
class StegoAudioFile{
 public void Audio_creation(ArrayList AudioSamp , String Path, String Path1){
 byte[] stegoSamp=new byte[AudioSamp.size()];
 for(int i=0;i<AudioSamp.size();i++){
     int a=(int)(AudioSamp.get(i));
     stegoSamp[i]=(byte)a;
    }
 //AudioReader audio=new AudioReader();
 try
    {
  File file=new File(Path);
  AudioInputStream stream = AudioSystem.getAudioInputStream(file);
  AudioFormat frmt = new AudioFormat((int)(stream.getFormat().getSampleRate()), stream.getFormat().getSampleSizeInBits(), stream.getFormat().getChannels(),
                true, stream.getFormat().isBigEndian());
 // AudioFormat  frmt= stream.getFormat();
  AudioInputStream ais = new AudioInputStream(
  new ByteArrayInputStream(stegoSamp),frmt,stegoSamp.length);
  Scanner scanner = new Scanner(System.in);
  AudioSystem.write(ais,AudioFileFormat.Type.WAVE,new File(Path1));
  }
    
 catch (Exception e){//Catch exception if any error
  System.err.println("Error: " + e.getMessage());
     }
 }
}