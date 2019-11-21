/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StegaRetriever;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class C7_Decompression {

    
    public static void Decompress(String Path) {
        //input file path
        String Path1=Path+"Errchecked.txt";
        //Output file path
        String Path2=Path+"decompressed.txt";   
        ReadFile RFle=new ReadFile();
        String Str=RFle.OpenFile(Path1);
        SeparationStr sep=new SeparationStr();
        String CharStr=sep.ChrStrSep(Str);
        String CodeStr=sep.CodeStrSep(Str,CharStr);
        RetrieveChr rcv=new RetrieveChr();
        ArrayList CharTable=rcv.CharRtrv(CharStr);
        // converting ArrayList into character array
        char [] CharTabFinal=new char[CharTable.size()];
        for(int i=0;i<CharTable.size();i++)
            CharTabFinal[i]=(char)CharTable.get(i);
        TextCreation txtCrt=new TextCreation();
        String Text=txtCrt.FinalTxt(CodeStr,CharTabFinal);
        Final_FileWrite FlWrt=new Final_FileWrite();
        FlWrt.WriteOutput(Text,Path2);
        System.out.println("decompressed file created");
        }
    }
   
class ReadFile{
public String OpenFile(String Path){
String textLine;
String C1="";
try{
//reading the input file
FileReader fr= new FileReader(Path);
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
   store2 s1 = new store2(ch,s);
   b.add(s1);
}
       int length=0;
Object ib[] = b.toArray();
for(int i=0; i< ib.length; i++) 
{
    store2 a1 =  (store2) ib[i];
    length=length+a1.cpy()+1; 
}
char[] c1=new char[length];
int k=0;
for(int i=0;i< b.size();i++){
    store2 a1=(store2) ib[i]; 
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
class store2 {
    char ch[];
    String st;
    public store2(char[]c ,String s )
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
class SeparationStr{
public String ChrStrSep(String Str){
     String sub1; 
 int num;
 int count=0;
 int final_count=0;
 String Final_String="";
 Str=Str.trim();
 //character string separation based on seperator
while (Str.length()>=5){
      sub1=Str.substring(0,5);
      Str=Str.substring(5);
     sub1=sub1.trim();
     num=Integer.parseInt(sub1,2);
     if(num==31)
         count=count+1;
     else count=0;
     if(count>=14){
         count=0;
        final_count=final_count+1;
     }
     if(final_count==1)
         break;
     Final_String=Final_String+sub1;
     Final_String=Final_String.trim();
}  
//elemination of extra bit in character string
int length=(Final_String.length())/51;
int length_final=(length-1)*51;
Final_String=Final_String.substring(0,length_final);
return Final_String;
}
public String CodeStrSep(String Str, String CharStr){
    String CodeStr=Str.substring(CharStr.length());
    String FinalCodeStr=CodeStr.substring(101);
return FinalCodeStr;
}
}
class RetrieveChr{
 public ArrayList CharRtrv(String ChrStr){
     String Str1;
     String Enc="";
     String Pat="";
     String Exor="";
     String Enc1="";
     String Pat1="";
     // variable declaration for final character generation 
     char EncChr1;
     char PatChr1;
     char EncChr2;
     char PatChr2;
     ArrayList CharTable=new ArrayList();
    while(ChrStr.length()>=51){
     Str1=ChrStr.substring(0,51);
     ChrStr=ChrStr.substring(51);
     //encrypted, pattern, and exor string separation
     Enc=Str1.substring(0,18);
     Pat=Str1.substring(18,33);
     Exor=Str1.substring(33,51);
     //creation of 2nd encrypted and pattern string from exor string
     Pat="000"+Pat;
     Enc1=xorOperation(Exor,Pat);
     Pat1=xorOperation(Exor,Enc);
     //creation of charctrer
     EncChr1=DecryptSDES(Enc);
     EncChr2=DecryptSDES(Enc1);
     PatChr1=DePattern(Pat);
     PatChr2=DePattern(Pat1);
     //creation of character table
     if(EncChr1==EncChr2)
        CharTable.add(EncChr1);
     else if(PatChr1==PatChr2)
        CharTable.add(PatChr1);
     else if(EncChr1==PatChr1)
         CharTable.add(PatChr1);
     else if(EncChr1==PatChr2)
         CharTable.add(PatChr2);
     else if(EncChr2==PatChr1)
         CharTable.add(PatChr1);
     else if(EncChr2==PatChr2)
         CharTable.add(PatChr2);
     }
  return CharTable;
 }
 public String xorOperation(String Str1, String Str2){
 char [] chr1=Str1.toCharArray();
 char [] chr2=Str2.toCharArray();
 String exorStr="";
 for(int i=0;i<18;i++){
  if((chr1[i]=='0') &&(chr2[i]=='0'))
      exorStr=exorStr+'0';
  if((chr1[i]=='1') &&(chr2[i]=='1'))
      exorStr=exorStr+'0';
  if((chr1[i]=='1') &&(chr2[i]=='0'))
      exorStr=exorStr+'1';
  if((chr1[i]=='0') &&(chr2[i]=='1'))
      exorStr=exorStr+'1';
 }
 exorStr=exorStr.trim();
 return exorStr;
 }
 public char DecryptSDES(String Enc){
     String Key=Enc.substring(0,10);
     String CipherStr=Enc.substring(10,Enc.length());
     //P10 permutation with KeyChr
     String KeyP10=P10_permutation(Key);
     //separating string into 5 bits
     String First=KeyP10.substring(0,5);
     String Second=KeyP10.substring(5,10);
    //performing left shift operation with each 5 bits
     String First_leftShift=leftshift(First);
     String Second_leftShift=leftshift(Second);
     //concatenation of two leftshifted string
     String Initial_key1=First_leftShift+Second_leftShift;
     // P8 operation to gentate key1
     String Key1=OperationP8(Initial_key1);
     // 1st round left shift to Initial_key1
     String First_LeftShift1=leftshift(First_leftShift);
     String Second_LeftShift1=leftshift(Second_leftShift);
     // 2nd round left shift to Initial_key1
     String First_LeftShift2=leftshift(First_LeftShift1);
     String Secind_LeftShift2=leftshift(Second_LeftShift1);
     // concatenation to create initial key 2
     String Initial_key2=First_LeftShift2+Secind_LeftShift2;
     //Perform p8 operation to generate key 2 from initial key 2
     String Key2=OperationP8(Initial_key2);
     //Initial permuatation with cipher text
     String InitCiphr=Initial_permutation(CipherStr);
     //deviding cipher text into two groups after initial Permutation
     String ExpFirst=InitCiphr.substring(0,4);
     String ExpLast=InitCiphr.substring(4,8);
     //performing Exponential permutation with Right part i.e. InitCiphr2nd
     String ExpoStr1=Exponential_prmu(ExpLast);
     //xor operation between ExpoStr1 and Key2 to create position string
     String PosStr1=XorOpt(ExpoStr1, Key2);
     //performing sbox operation with position string
     String SboxStr1=Sbox_operation(PosStr1);
     //performing p4 permutation with SboxStr1 
     String InitialPlainText2=PermutationP4(SboxStr1);
     //xor operation to crate 2nd part of plain text
      String Plaintext2=XorOpt(InitialPlainText2, ExpFirst);
     //2nd round exponential permutation 
     String ExpoStr2=Exponential_prmu(Plaintext2);
     // Cretion postion string for 1st part of plain text
     String PosStr2=XorOpt(ExpoStr2, Key1);
     //2nd round s-box operation for 1st part plain text creation
     String SboxStr2=Sbox_operation(PosStr2);
     //performing p4 operationfor creation initial plain text1
     String InitialPlainText1=PermutationP4(SboxStr2);
     //creation of 1st part of plain text
     String Plaintext1=XorOpt(InitialPlainText1,ExpLast);
     //creation of plain text
     String PlainText=Plaintext1+Plaintext2;
     //creation of final plaintext string
     String FinalPlainTxtStr=Inverse_Permutation(PlainText);
     FinalPlainTxtStr=FinalPlainTxtStr.trim();
     //convert plain text into Ascii value
     int PlainTxtAscii=Integer.parseInt(FinalPlainTxtStr,2);
     //Conversion of asciivalue into character
     char AsciiChr=(char)PlainTxtAscii;
  return AsciiChr;
 }
 public String P10_permutation(String Chr){
 // char array conversion of key string  
 char str[]=new char[10];
 str=Chr.toCharArray();
 char Str1[]=new char[10];
 Str1[0]=str[2];
 Str1[1]=str[1];
 Str1[2]=str[4];
 Str1[3]=str[6];
 Str1[4]=str[3];
 Str1[5]=str[9];
 Str1[6]=str[0];
 Str1[7]=str[8];
 Str1[8]=str[7];
 Str1[9]=str[5];
 String FinalStr = String.valueOf(Str1);
return FinalStr;
}
 //left shift operation
public String leftshift(String chr){
 char Str[]=new char[5];
 Str=chr.toCharArray();
 char Str1[]=new char[5];  
 Str1[0]=Str[1];
 Str1[1]=Str[2];
 Str1[2]=Str[3];
 Str1[3]=Str[4];
 Str1[4]=Str[0];
 String LeftShiftStr=String.valueOf(Str1);
 return LeftShiftStr;
}
//p8 operation
public String OperationP8(String initKey1){
 char  str[]=new char[10];
 str=initKey1.toCharArray();
 char str1[]=new char [8];
 str1[0]=str[5];
 str1[1]=str[2];
 str1[2]=str[6];
 str1[3]=str[3];
 str1[4]=str[7];
 str1[5]=str[4];
 str1[6]=str[9];
 str1[7]=str[8];
 String StrP8=String.valueOf(str1);
 return StrP8;
}
public String Initial_permutation(String OriStr){
 char  str[]=new char[8];
 str=OriStr.toCharArray();
 char str1[]=new char [8];
 str1[0]=str[1];
 str1[1]=str[5];
 str1[2]=str[2];
 str1[3]=str[0];
 str1[4]=str[3];
 str1[5]=str[7];
 str1[6]=str[4];
 str1[7]=str[6];
 String Initial_Permutation=String.valueOf(str1);
return Initial_Permutation;
}
//exponential permutation operation
public String Exponential_prmu(String ExpStr){
 char  str[]=new char[4];
 str=ExpStr.toCharArray();
 char str1[]=new char [8];
 str1[0]=str[3];
 str1[1]=str[0];
 str1[2]=str[1];
 str1[3]=str[2];
 str1[4]=str[1];
 str1[5]=str[2];
 str1[6]=str[3];
 str1[7]=str[0];
 String ExpoPermuStr=String.valueOf(str1);
return ExpoPermuStr;
}
// xor operation to generate position stirng
public String XorOpt(String ExpoStr, String Key1){
 char str1[]=new char[ExpoStr.length()];
 char str2[]=new char[Key1.length()];
 char final_str[]=new char[Key1.length()];
 str1=ExpoStr.toCharArray();
 str2=Key1.toCharArray();
 for(int i=0;i<(Key1.length());i++){
     if(str1[i]=='0' && str2[i]=='0')
        final_str[i]='0'; 
     else if(str1[i]=='1' && str2[i]=='1')
        final_str[i]='0'; 
     else
         final_str[i]='1'; 
 } 
 String FinalXorStr=String.valueOf(final_str);
    return FinalXorStr;
}
// s1 and s2 box string formulation
public String Sbox_operation( String PosStr){
//s1 box creation
String [][]S0box={
    {"01","00","11","10"},
    {"11","10","01","00"},
    {"00","10","01","11"},
    {"11","01","11","10"}
};  
String [][] S1box={
    {"00","01","10","11"},
    {"10","00","01","11"},
    {"11","00","01","00"},
    {"10","01","00","11"}
};
// converting character aray to postion string PosStr
char str[]= new char[8];
str=PosStr.toCharArray();
// creation of position variable
String pos1=String.valueOf(str[0])+String.valueOf(str[3]);
String pos2=String.valueOf(str[1])+String.valueOf(str[2]);
String pos3=String.valueOf(str[4])+String.valueOf(str[7]);
String pos4=String.valueOf(str[5])+String.valueOf(str[6]);
//conversion of integer to every position value
int pos11=Integer.parseInt(pos1,2);
int pos12=Integer.parseInt(pos2,2);
int pos13=Integer.parseInt(pos3,2);
int pos14=Integer.parseInt(pos4,2);
//Sbox string creation from s0 and s1 box array
String SboxStr=S0box[pos11][pos12]+S1box[pos13][pos14];
return SboxStr;
}
public String PermutationP4(String SboxStr){
 char  str[]=new char[4];
 str=SboxStr.toCharArray();
 char str1[]=new char [4];
 str1[0]=str[1];
 str1[1]=str[3];
 str1[2]=str[2];
 str1[3]=str[0];
 String P4str=String.valueOf(str1);
return P4str;
}
public String Inverse_Permutation(String PlainTxt){
char str[]=new char[8];
str=PlainTxt.toCharArray();
char str1[]=new char[8];
str1[0]=str[3];
str1[1]=str[0];
str1[2]=str[2];
str1[3]=str[4];
str1[4]=str[6];
str1[5]=str[1];
str1[6]=str[7];
str1[7]=str[5];
String InverseStr=String.valueOf(str1);
return InverseStr;
}
public char DePattern(String Pat){
char OriginalChr;
String PatFinal=Pat.substring(3,Pat.length());
// separatin PatFinal into sub strings
String SubPat1=PatFinal.substring(0,5);
String SubPat2=PatFinal.substring(5,10);
String SubPat3=PatFinal.substring(10,15);
//converting substring into integer
int num1=Integer.parseInt(SubPat1,2);
int num2=Integer.parseInt(SubPat2,2);
int num3=Integer.parseInt(SubPat3,2);
//converting num into actual number from pattern table
int ActNum1=num1-1;
int ActNum2=num2-11;
int ActNum3=num3-21;
//forming actual Ascii value of character
int AsciiChr=(ActNum1*100)+(ActNum2*10)+ActNum3;
OriginalChr=(char)AsciiChr;
return OriginalChr;
}
}
class TextCreation{
public String FinalTxt(String Code,char [] CharTab){
 String TxtSub="";
 String TxtSub1;
 String FinalTxt="";
 int num=0;
 int num1;
 int index;
 while(Code.length()>=5){
      TxtSub1="";
      num1=0; 
      index=0;
      TxtSub=Code.substring(0,5);
      Code=Code.substring(5);
      num=Integer.parseInt(TxtSub,2);
      if((CharTab.length)<=28 && num>=28)
          num=CharTab.length-1;
      if((CharTab.length>28)&& (CharTab.length<=56)&& (num>28))
          num=28;
      if((CharTab.length>56)&& (CharTab.length<=84)&& (num>29))
          num=29;
      if((CharTab.length>84)&& (CharTab.length<=112)&& (num>30))
          num=30;
      if(num<28)
        FinalTxt=FinalTxt+CharTab[num];
      if(num==28 && Code.length()>=5){
        TxtSub1=Code.substring(0,5);
        Code=Code.substring(5);
        num1=Integer.parseInt(TxtSub1,2);
        if(num1>=(CharTab.length-28))
            num1=(CharTab.length-29);
        index=num+num1;
        FinalTxt=FinalTxt+CharTab[index];
      }
      if(num==29 && Code.length()>=5){
        TxtSub1=Code.substring(0,5);
        Code=Code.substring(5);
        num1=Integer.parseInt(TxtSub1,2);
        if(num1>=(CharTab.length-56))
            num1=(CharTab.length-57);
        index=(56+num1);
        FinalTxt=FinalTxt+CharTab[index];
      }
      if(num==30 && Code.length()>=5){
        TxtSub1=Code.substring(0,5);
        Code=Code.substring(5);
        num1=Integer.parseInt(TxtSub1,2);
        if(num1>=(CharTab.length-84))
           num1=(CharTab.length-85);
        index=(84+num1);
        FinalTxt=FinalTxt+CharTab[index];
      }
      if(num==31 && Code.length()>=5){
        TxtSub1=Code.substring(0,5);
        Code=Code.substring(5);
        num1=Integer.parseInt(TxtSub1,2);
        if(num1>=(CharTab.length-112))
          num1=(CharTab.length-113);
        index=(112+num1);
        FinalTxt=FinalTxt+CharTab[index];
        }
    }
 
return FinalTxt;
  }
}
class Final_FileWrite{
public void WriteOutput(String Str, String Path1){
   try{
  // Create file
  FileWriter fstream = new FileWriter(Path1);
  BufferedWriter out = new BufferedWriter(fstream);
  out.write(Str);
  //Close the output stream
  out.close();
  }catch (Exception e){//Catch exception if any
  System.err.println("Error: " + e.getMessage());
     }
  }
}