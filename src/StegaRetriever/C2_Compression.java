/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StegaRetriever;


import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.io.*;
public class C2_Compression {

   
    public static void Compress(String Path) {
     //Input file path
     String Path1=Path+"input_db.txt";
     //output file path
     String Path2=Path+"compressed_db.txt";
       //operation for reading files and copying characters
        FileReading File=new FileReading();
        ArrayList File_Char=new ArrayList();
        File_Char= File.FileTest(Path1);
        String Compressed_String="";
        String Compressed_String_Final="";
        String CharStr="";
       //operation for elemiate the redundancy
      Redundancy_elemination red=new Redundancy_elemination();
      ArrayList nonRedundant_Char=new ArrayList();
      nonRedundant_Char=red.Redundant_eleminate(File_Char);
      // Sorting the elements
      sort srt=new sort();
      ArrayList SortElement=new ArrayList();
      SortElement=srt.Sorting(nonRedundant_Char,File_Char);
      //Encription using SDES
       SDES Enc=new SDES();
       ArrayList Encryption_SDES=new ArrayList();
       Encryption_SDES=Enc.Encryption(SortElement);
       //Pattern String generation
       pattern Pat=new pattern();
       ArrayList PatternStr=new ArrayList();
       PatternStr=Pat.Pattern_generation(SortElement);
       //concatenation of pattern string and encrypted string to generate security string
       ArrayList security_str=new ArrayList();
       for(int i=0; i<SortElement.size();i++){
           String SecStr=((String)Encryption_SDES.get(i))+((String)PatternStr.get(i));
           security_str.add(SecStr);
          } 
       //Addition of error checking information
       ErrorHandelling err=new ErrorHandelling();
       ArrayList ErrorCheck=new ArrayList();
       ErrorCheck=err.ErrorCheckInfo(security_str, Encryption_SDES,PatternStr );
       //compressed string generation
       compression compStr=new compression();
       ArrayList CompressedStr=new ArrayList();
       CompressedStr=compStr.comppressedStr_Generation(File_Char,SortElement);
       // Concatenation of compressed string into single string
       for(int i=0;i<CompressedStr.size();i++)
           Compressed_String=Compressed_String+CompressedStr.get(i);
       //concatenation of error checking info
       for(int i=0; i< ErrorCheck.size();i++){
           CharStr=CharStr+ ErrorCheck.get(i);
       }
       //creation of separator
       String Sep="";
       for(int i=0;i<101;i++)
          Sep=Sep+"1";
   //creation of compressed string by concatenating error checking,separator and compressed strings 
       Compressed_String_Final=CharStr+Sep+Compressed_String+Sep;
       FileWriting FileWr=new FileWriting();
       FileWr.FWriting(Compressed_String_Final, Path2);
       System.out.println("Compressed File has been created ");
    }
   }

class FileReading{
    String textLine;  
    ArrayList C1=new ArrayList();
    public ArrayList FileTest(String Path){
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
   store s1 = new store(ch,s);
   b.add(s1);
}
       int length=0;
Object ib[] = b.toArray();
for(int i=0; i< ib.length; i++) 
{
    store a1 =  (store) ib[i];
    length=length+a1.cpy()+1; 
}
char[] c1=new char[length];
int k=0;
for(int i=0;i< b.size();i++){
    store a1=(store) ib[i]; 
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
// Addition of new line character
    else
       c1[k++]='\n';   
}
for(int i=0;i<length;i++)      
C1.add(c1[i]);
}
catch(IOException ioe ){
System.err.println(ioe);
System.exit(1); 
}
       return C1;
}
    }
class store {
    char ch[];
    String st;
    public store(char[]c ,String s )
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
// elemination of redundancy
class Redundancy_elemination{
public ArrayList Redundant_eleminate(ArrayList Allchars){
    ArrayList NonRed=new ArrayList();
    int count;
    for(int i=0;i<Allchars.size();i++){
        count=0;
        for(int j=0;j<NonRed.size();j++){
            if(NonRed.get(j)==Allchars.get(i))
                count=count+1;
        }
        if(count==0)
            NonRed.add(Allchars.get(i));
    }
 return NonRed;
}
}
// class for sort the elements
class sort{
 public ArrayList Sorting(ArrayList NonRedundantStr, ArrayList Allchars ){
    // Calculation of frequencies of each nonredundant character
     ArrayList  FreqList=new ArrayList();
     ArrayList Sorted_ele=new ArrayList();
     int count;
     for(int j=0; j<NonRedundantStr.size();j++){
         count=0;
         for(int i=0; i<Allchars.size();i++){
           if(NonRedundantStr.get(j)==Allchars.get(i))
               count=count+1;
        }
         FreqList.add(count);
     }
     Sorted_ele=sort_final(NonRedundantStr,FreqList);
 return Sorted_ele;
 }
 
 public  ArrayList sort_final(ArrayList NonRedStr, ArrayList Freq){
     int temp;
     char temp1;
         for(int i = 0; i < NonRedStr.size(); i++)
        {
            for(int j = 1; j < (NonRedStr.size()-i); j++)
            {
                // Soting according the higher frequency to lower
                if(((int) Freq.get(j-1)) < ((int) Freq.get(j)))
                {
                    temp = ((int) Freq.get(j-1)); 
                    temp1=(char) NonRedStr.get(j-1);
                    Freq.set((j-1),Freq.get(j));
                    NonRedStr.set((j-1),NonRedStr.get(j));
                    Freq.set(j,temp);
                    NonRedStr.set(j,temp1);
                }
            }
        }
 return NonRedStr;
 }
}
// creation of SDES tring
class SDES{
public ArrayList Encryption(ArrayList SortChar){
    ArrayList EncryptedString=new ArrayList();
    String Final_cipherText;
    String Final_EncryptedStr;
    BitGenerator keyStr=new BitGenerator();
    for(int i=0;i<SortChar.size();i++){
        Final_cipherText="";
          //binary string conversion
        char str=(char)SortChar.get(i);
        String num=Integer.toBinaryString((int)str);
        while(num.length()<8)
                num="0"+num;
        //10 bit key generation
        String Key=keyStr.KeyBits();
        // p10 permutation
        String PermuP10=P10_permutation(Key);
        //separating string into 5 bits
        String First=PermuP10.substring(0,5);
        String Second=PermuP10.substring(5,10);
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
        String Second_LeftShift2=leftshift(Second_LeftShift1);
        // concatenation to create initial key 2
        String Initial_key2=First_LeftShift2+Second_LeftShift2;
        //Perform p8 operation to generate key 2 from initial key 2
        String Key2=OperationP8(Initial_key2);
        // create Initial Permutation with original plain test string
        String Initial_PerStr=Initial_permutation(num);
        //separation of last 4 bits from from Initial_PerStr to perform expansion/permutation\
        String ExpFirst=Initial_PerStr.substring(0,4);
        String ExpLast=Initial_PerStr.substring(4,8);
       // exponential permutation with ExpLast
        String ExpoStr1=Exponential_prmu(ExpLast);
       // XOR operation between ExpoStr1 and Key1 to generate Position string1
        String PosStr1=XorOpt(ExpoStr1, Key1);
        // S box operation with position string PosStr
        String SboxStr1=Sbox_operation(PosStr1);
        // p4 permutation with sbox string SboxStr1 to create initial cipher text 1
        String Initiacl_cipher1=PermutationP4(SboxStr1);
        //perform xor opration between Initiacl_cipher1 and iPStr1 to create cipher text 2
        String CipherText2=XorOpt(Initiacl_cipher1,ExpFirst);   
// second phase of cipher text generation
     //exponential premutation with cipher text2 to generate ExpoStr2
        String ExpoStr2=Exponential_prmu(CipherText2);
     //xor operation between Expostr2 and Key2 to generate position string2
        String PosStr2=XorOpt(ExpoStr2, Key2);
     // s box operation with position string 2 i.e. PosStr2
        String SboxStr2=Sbox_operation(PosStr2);
     //p4 operation with sbox string 2 i.e. SboxStr2 to generate initial cipher text 2
        String Initiacl_cipher2=PermutationP4(SboxStr2);
     //perform xor opration between Initiacl_cipher2 and iPStr2 to create cipher text1
        String CipherText1=XorOpt(Initiacl_cipher2,ExpLast);
     // creation of final cipher text
        Final_cipherText=CipherText1+CipherText2;
    //creation of final plaintext string
        String FinalCiphrTxtStr=Inverse_Permutation(Final_cipherText);
        FinalCiphrTxtStr=FinalCiphrTxtStr.trim();
     // encrypted string generation addition of Key and cipher text
        Final_EncryptedStr=Key+FinalCiphrTxtStr;
    //  Addition of encrypted string to the final encrypted string    
        EncryptedString.add(Final_EncryptedStr);
        }
    return EncryptedString;
    }
//P10 permutation function
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
//initial permutation operation
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
     if(str1[i]=='1' && str2[i]=='1')
        final_str[i]='0'; 
     if(str1[i]=='0' && str2[i]=='1')
         final_str[i]='1'; 
     if(str1[i]=='1' && str2[i]=='0')
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
}
//10 bit key generation 
class BitGenerator{ 
public String KeyBits() { 
String bits = ""; 
Random r = new Random(); 
for(int i=0; i<10; i++){ 
   int x = 0; 
   if(r.nextBoolean()) x=1; 
     bits += x; 
  } 
return bits;
 } 
}
//pattern string generation
class pattern{
    public ArrayList Pattern_generation(ArrayList Ptr){
        ArrayList PtrStr=new ArrayList();
        for(int i=0;i<Ptr.size();i++){
         char chr=(char)Ptr.get(i);
         int num=(int)chr;
         // separation of number
         int num3=num%10;
         num=num/10;
         int num2=num%10;
         int num1=num/10;
         // find the position number from the table
         int num_pos3=2*10+num3+1;
         int num_pos2=10+num2+1;
         int num_pos1=num1+1;
         //converting position numbers inti binary string
        String Pos_str1=Integer.toBinaryString(num_pos1);
        String Pos_str2=Integer.toBinaryString(num_pos2);
        String Pos_str3=Integer.toBinaryString(num_pos3);
        //Converting each postion string to 5 bits length
        while(Pos_str1.length()<5)
            Pos_str1="0"+Pos_str1;
        while(Pos_str2.length()<5)
            Pos_str2="0"+Pos_str2;
        while(Pos_str3.length()<5)
            Pos_str3="0"+Pos_str3;
        String Patern_string=Pos_str1+Pos_str2+Pos_str3;
        PtrStr.add(Patern_string);
        }
    return PtrStr;
    }
}
//compressed string generation
class compression{
public ArrayList comppressedStr_Generation(ArrayList AllChar, ArrayList SortedChar){
    ArrayList CompressedStr=new ArrayList();
    ArrayList CodeStr=new ArrayList();
    // when the size of sorted chater varies from 1 to 28
    if(SortedChar.size()<=28){
      for(int i=0;i<SortedChar.size();i++){
          String str=BinaryConversion(i);
          CodeStr.add(str);
         }
    }
    // when the size of sorted chater varies from 29 to 56
    if((SortedChar.size()>28)&&(SortedChar.size()<=56)){
      for(int i=0;i<=27;i++){
          String str=BinaryConversion(i);
          CodeStr.add(str);
         }
       for(int j=28;j<SortedChar.size();j++){
          String str=BinaryConversion(j-28);
          String str1="11100";
          str=str1+str;
          CodeStr.add(str);
         }
    }
    // when the size of sorted chater varies from 57 to 84
    if((SortedChar.size()>56)&&(SortedChar.size()<=84)){
      for(int i=0;i<=27;i++){
          String str=BinaryConversion(i);
          CodeStr.add(str);
         }
       for(int j=28;j<=55;j++){
          String str=BinaryConversion(j-28);
          String str1="11100";
          str=str1+str;
          CodeStr.add(str);
         }
       for(int k=56;k<SortedChar.size();k++){
          String str=BinaryConversion(k-56);
          String str1="11101";
          str=str1+str;
          CodeStr.add(str);
         }
    }
    // when the size of sorted chater varies from 85 to 112
    if((SortedChar.size()>84)&&(SortedChar.size()<=112)){
      for(int i=0;i<=27;i++){
          String str=BinaryConversion(i);
          CodeStr.add(str);
         }
       for(int j=28;j<=55;j++){
          String str=BinaryConversion(j-28);
          String str1="11100";
          str=str1+str;
          CodeStr.add(str);
         }
       for(int k=56;k<=83;k++){
          String str=BinaryConversion(k-56);
          String str1="11101";
          str=str1+str;
          CodeStr.add(str);
         }
       for(int k=84;k<SortedChar.size();k++){
          String str=BinaryConversion(k-84);
          String str1="11110";
          str=str1+str;
          CodeStr.add(str);
         }
    }
    // when the size of sorted chater varies from 113 to 128
    if(SortedChar.size()>112){
      for(int i=0;i<=27;i++){
          String str=BinaryConversion(i);
          CodeStr.add(str);
         }
       for(int j=28;j<=55;j++){
          String str=BinaryConversion(j-28);
          String str1="11100";
          str=str1+str;
          CodeStr.add(str);
         }
       for(int k=56;k<=83;k++){
          String str=BinaryConversion(k-56);
          String str1="11101";
          str=str1+str;
          CodeStr.add(str);
         }
       for(int k=84;k<=111;k++){
          String str=BinaryConversion(k-84);
          String str1="11110";
          str=str1+str;
          CodeStr.add(str);
         }
       for(int k=112;k<SortedChar.size();k++){
          String str=BinaryConversion(k-112);
          String str1="11111";
          str=str1+str;
          CodeStr.add(str);
         }
    }  
    // compressed string replacement
    for (int i=0;i<AllChar.size();i++){
        for(int j=0;j<SortedChar.size();j++){
           if(AllChar.get(i)==SortedChar.get(j))
              CompressedStr.add(CodeStr.get(j)); 
            } 
        }
    
return CompressedStr;
}
public String BinaryConversion(int num){
String CodeStr;
CodeStr=Integer.toBinaryString(num);
while(CodeStr.length()<5)
    CodeStr="0"+CodeStr;
return CodeStr;
}
}
class ErrorHandelling{
 public ArrayList ErrorCheckInfo(ArrayList SecurityInfo, ArrayList EncStr, ArrayList PatStr){
    ArrayList ErrChkInfo=new ArrayList();
    String ErrChkPhase1="";
    String ErrChkStr2="";
    String ErrChkphase2="";
    char [] chr;
    for(int i=0;i<EncStr.size();i++){
        //1st phase of error checking
         ErrChkPhase1=ErrorChecking1(((String)EncStr.get(i)),((String)PatStr.get(i)));
        //2nd phase of error checking 
         ErrChkStr2=(String)EncStr.get(i)+(String)PatStr.get(i)+ErrChkPhase1;
         ErrChkphase2=ErrorChecking2(ErrChkStr2);
         ErrChkInfo.add(ErrChkphase2);
    }
 return ErrChkInfo;
 }
 public String ErrorChecking1(String EncStr, String PatStr){
     char [] Errcheck1=new char[EncStr.length()];
     PatStr="000"+PatStr;
     String ErrCheckStr="";
     char [] chrEnc=EncStr.toCharArray();
     char [] chrPat=PatStr.toCharArray();
     for(int i=0;i<chrEnc.length;i++){
        if((chrEnc[i]=='0') && (chrPat[i]=='0'))
         Errcheck1[i]='0';
        if((chrEnc[i]=='1') && (chrPat[i]=='1'))
         Errcheck1[i]='0';
        if((chrEnc[i]=='0') && (chrPat[i]=='1'))
         Errcheck1[i]='1'; 
        if((chrEnc[i]=='1') && (chrPat[i]=='0'))
         Errcheck1[i]='1'; 
        ErrCheckStr=ErrCheckStr+Errcheck1[i];
     }
 return ErrCheckStr;
 }
 public String ErrorChecking2(String ErrChkStr1){
    char [] ErrChkChr2=ErrChkStr1.toCharArray();
    String ErrChkStr="";
    String chr;
    ErrChkStr=ErrChkStr+ErrChkChr2[0];
    for(int i=0;i<((ErrChkChr2.length)-1);i++){
      ErrChkStr=ErrChkStr+ErrChkChr2[i+1];
      chr="";
      if((ErrChkChr2[i]=='0') &&(ErrChkChr2[i+1]=='0'))
          chr=chr+'0';
      if((ErrChkChr2[i]=='1') &&(ErrChkChr2[i+1]=='1'))
          chr=chr+'0';
      if((ErrChkChr2[i]=='1') &&(ErrChkChr2[i+1]=='0'))
          chr=chr+'1';
      if((ErrChkChr2[i]=='0') &&(ErrChkChr2[i+1]=='1'))
          chr=chr+'1';
      ErrChkStr=ErrChkStr+chr;
    }
 return ErrChkStr;
}
}
class FileWriting{
public void FWriting(String CompStr, String Path4){
    try{
  // Create file
  FileWriter fstream = new FileWriter(Path4);
  BufferedWriter out = new BufferedWriter(fstream);
  out.write(CompStr);
  //Close the output stream
  out.close();
  }catch (Exception e){//Catch exception if any
  System.err.println("Error: " + e.getMessage());
     }
  }  
}