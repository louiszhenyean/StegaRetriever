/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StegaRetriever;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.io.*;
public class C6_Error_Correction {
    public static void ErrCorrect(String Path){
     String Txt; 
     //Input file
     String Path1=Path+"Retrieved.txt";
     //output File
     String Path2=Path+"Errchecked.txt";
    
     ReadTxt read=new ReadTxt();
     errorChecking errChk=new errorChecking();
     Txt=read.ReadClass(Path1);
     String CorrectedStr=errChk.errorDetectCoorrect(Txt);
     FileWrite Fwr=new FileWrite();
     Fwr.WriteOutput(CorrectedStr,Path2);
     System.out.println("Error Checked file has been created");
     }
    }

class ReadTxt{
    
public String ReadClass (String Path) {
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
   storeErr s1 = new storeErr(ch,s);
   b.add(s1);
}
       int length=0;
Object ib[] = b.toArray();
for(int i=0; i< ib.length; i++) 
{
    storeErr a1 =  (storeErr) ib[i];
    length=length+a1.cpy()+1; 
}
char[] c1=new char[length];
int k=0;
for(int i=0;i< b.size();i++){
    storeErr a1=(storeErr) ib[i]; 
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
C1=C1+str1;
C1=C1.trim();
}
}

catch(IOException ioe ){
System.err.println(ioe);
System.exit(1); 
 }
return C1;
  }
}
class storeErr {
    char ch[];
    String st;
    public storeErr(char[]c ,String s )
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
class errorChecking{
public  String errorDetectCoorrect(String RcvStr){
RcvStr=RcvStr.trim();
String ErrorCheckingStr=Separation(RcvStr); 
String CodeStr=RcvStr.substring(ErrorCheckingStr.length());
String Str;
String errFree;
String CorrectedErr="";
while(ErrorCheckingStr.length()>0){
    Str=ErrorCheckingStr.substring(0,101);
    ErrorCheckingStr=ErrorCheckingStr.substring(101);
    errFree=removeErr(Str);
    CorrectedErr=CorrectedErr+errFree;
 }
String FinalStr=CorrectedErr+CodeStr;
return FinalStr;
  }

public String Separation(String Str){
 
 String StrErrChk=Str; 
 StrErrChk=StrErrChk.trim();
 String sub1;
 int num;
 int count=0;
 int length=0;
 String Final_Str="";
 while(StrErrChk.length()>0){
     num=0;
     sub1=StrErrChk.substring(0,5);
     StrErrChk=StrErrChk.substring(5);
     num=Integer.parseInt(sub1,2);
     if(num==31)
        count=count+1;
     else count=0;
     length=length+5;
     Final_Str=Final_Str+sub1;
     if(count>=14)
         break;
   }
 Final_Str=Final_Str.trim();
 int length1=(length/101);
 int length2=(length1*101);
 String ErrStr=Final_Str.substring(0, length2);
 return ErrStr;
  }
public String removeErr(String Str){
    Str=Str.trim();
 //declaration of error checking variables
    char [] chr=Str.toCharArray();
    String EncryptedStr= RetriveEnc(Str);
    String PatternStr=RetrievePat(Str);
    //making the pattern string into 18 bits
    String PatternStr1="000"+PatternStr;
    String ExorStr= RetriveXor(Str);
    EncryptedStr=EncryptedStr.trim();
    PatternStr1=PatternStr1.trim();
    ExorStr=ExorStr.trim();
    //char array conversion of string
    char [] EncChr=EncryptedStr.toCharArray();
    char [] PatChr=PatternStr1.toCharArray();
    char [] ExorChr=ExorStr.toCharArray();
    //variable declaration for 1st round error correction and detection
    char [] Enc1st=new char[18];
    char [] Pat1st=new char[18];
    char [] Exor1st=new char[18];
    int count1=0;
    int count2=0;
    int count3=0;
    Enc1st[count1]=xorOperation(chr[1],chr[2]);
    count1=count1+1;
    Enc1st[count1]=xorOperation(chr[3],chr[4]);
    count1=count1+1;
    for(int i=3;i<chr.length;){
    Enc1st[count1]=xorOperation(chr[i+2],chr[i+3]);
    count1=count1+1;
    i=i+2;
    if(count1>17)
        break;
    }
    for(int i=0;i<3;i++){
    Pat1st[count2]='0';
    count2=count2+1;
    }
    for(int i=35;i<chr.length;){
    Pat1st[count2]=xorOperation(chr[i+2],chr[i+3]);
    count2=count2+1;
    i=i+2;
    if(count2>17)
        break;
    }
    for(int i=65;i<chr.length;){
    Exor1st[count3]=xorOperation(chr[i+2],chr[i+3]);
    count3=count3+1;
    i=i+2;
    if(count3>16)
        break;
    }
    Exor1st[17]=xorOperation(chr[97],chr[100]);
    //2nd round error correction and detection
    char [] Enc2nd=new char[18];
    char [] Pat2nd=new char [18];
    char [] Exor2nd=new char [18];
    int num1=0;
    int num2=0;
    int num3=0;
    Enc2nd[num1]=xorOperation(chr[1],chr[2]);
    num1=num1+1;
    Enc2nd[num1]=xorOperation(chr[0],chr[2]);
    num1=num1+1;
    for(int i=3;i<chr.length;){
     Enc2nd[num1]=xorOperation(chr[i-2],chr[i+1]);
     num1=num1+1;
     i=i+2;
     if(num1>17)
         break;
    }
    for(int i=0;i<3;i++){
    Pat2nd[num2]='0';
    num2=num2+1;
    }
    for(int i=35;i<chr.length;){
    Pat2nd[num2]=xorOperation(chr[i-2],chr[i+1]);
    num2=num2+1;
    i=i+2;
    if(num2>17)
        break;
    }
    for(int i=65;i<chr.length;){
    Exor2nd[num3]=xorOperation(chr[i-2],chr[i+1]);
    num3=num3+1;
    i=i+2;
    if(num3>16)
        break;
    }
    Exor2nd[17]=xorOperation(chr[100],chr[97]);
    //error free encrypted, pattern, exor char array creation
    char [] EncFinal=new char[18];
    char [] PatFinal=new char[18];
    char [] ExorFinal=new char[18];
    char ExorVar1;
    char ExorVar2;
    char ExorVar3;
    for(int i=0;i<18;i++){
        ExorVar1=xorOperation(EncChr[i],PatChr[i]);
        ExorVar2=xorOperation(Enc1st[i],Pat1st[i]);
        ExorVar3=xorOperation(Enc2nd[i],Pat2nd[i]);
        if(ExorVar1==ExorChr[i]){
          EncFinal[i]=EncChr[i];
          PatFinal[i]=PatChr[i];
          ExorFinal[i]=ExorChr[i];
        }
        else if(ExorVar2==Exor1st[i]){
          EncFinal[i]=Enc1st[i];
          PatFinal[i]=Pat1st[i];
          ExorFinal[i]=Exor1st[i];
        }
        else if(ExorVar3==Exor2nd[i]){
          EncFinal[i]=Enc2nd[i];
          PatFinal[i]=Pat2nd[i];
          ExorFinal[i]=Exor2nd[i];
        }
    }
    // final string creation
    String EncFinlStr="";
    String PatFinalStr="";
    String ExorFinalStr="";
    for(int i=0;i<18;i++){
       EncFinlStr=EncFinlStr+EncFinal[i];
       ExorFinalStr=ExorFinalStr+ExorFinal[i];
    }
    for(int i=3;i<18;i++)
       PatFinalStr=PatFinalStr+PatFinal[i];
    //final eoorcorrection string creation
    String CorrectdStr="";
    CorrectdStr=CorrectdStr+EncFinlStr+PatFinalStr+ExorFinalStr;
    CorrectdStr=CorrectdStr.trim();
    return CorrectdStr ;
}
public String RetriveEnc(String Enc){
    char []Chr=Enc.toCharArray();
    char[] EncChr=new char[18];
    int count=2;
    String EncStr="";
    EncChr[0]=Chr[0];
    EncChr[1]=Chr[1];
    EncStr=EncStr+EncChr[0]+EncChr[1];
    for(int i=3;i<Chr.length;){
       EncChr[count]=Chr[i];
       EncStr=EncStr+EncChr[count];
       i=i+2;
       count=count+1;
       if(count>17)
           break;
    }
    EncStr=EncStr.trim();
return EncStr;
}
public String RetrievePat(String Pat){
    char [] Chr=Pat.toCharArray();
    char [] PatChr=new char[15];
    int count=0;
    String PatStr="";
    for(int i=35;i<Chr.length;){
        PatChr[count]=Chr[i];
        PatStr=PatStr+PatChr[count];
        i=i+2;
        count=count+1;
        if(count>14)
            break;
    }
    PatStr=PatStr.trim();
     return PatStr;
}
public String RetriveXor(String Exor){
    char [] Chr=Exor.toCharArray();
    char [] ExorChr=new char[18];
    int count=0;
    String ExorStr="";
    for(int i=65;i<Chr.length;){
        ExorChr[count]=Chr[i];
        ExorStr=ExorStr+ExorChr[count];
        i=i+2;
        count=count+1;
        if(count>17)
           break;
    }
    ExorStr=ExorStr.trim();
    return ExorStr;
}
public char xorOperation(char x, char y){
    char z='0';
if(x=='1' && y=='1')
    z='0';
if(x=='0' && y=='0')
    z='0';
if(x=='1' && y=='0')
    z='1';
if(x=='0' && y=='1')
    z='1';
return z;
}
}
class FileWrite{
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