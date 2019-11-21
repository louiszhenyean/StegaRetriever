package StegaRetriever;
import java.io.IOException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class C1_Main {

    public static void main(String[] args) throws UnsupportedAudioFileException, IOException 
    {
      String Path="test/";
        
        //C2_Compression comp = new C2_Compression();
        //comp.Compress(Path);

        //C3_Audio_Steganography adSte = new C3_Audio_Steganography();
        //adSte.AudioSte(Path);
        
        // THE BREAK POINT IS HERE
        
        C5_Retrive_StegoData Rtrv = new C5_Retrive_StegoData();
        Rtrv.RtrvStego(Path);

        C6_Error_Correction errCrt = new C6_Error_Correction();
        errCrt.ErrCorrect(Path);
        
        C7_Decompression decom = new C7_Decompression();
        decom.Decompress(Path);
    }
}