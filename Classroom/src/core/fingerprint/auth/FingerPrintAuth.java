/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.fingerprint.auth;
/**
 *
 * @author martin-trejo
 */

import core.gui.admon.CreateAccount;
import java.awt.image.BufferedImage;
import java.io.*;    
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

    public class FingerPrintAuth implements Runnable{
        private String pyEnrollFinger;
        private String pySearchFinger;   
        private String pyReadFinger; 
        private String pyDeleteFinger;
        private String pyDownloadFinger;        
        private String capturedValue;        
        public Process myProcess;
        public BufferedReader in;      
        public BufferedReader err;      
        private String basePath;
        
        private volatile boolean running = true;
        
        private readFingerPrintEvent readFPEvent;
                
        
        public FingerPrintAuth(readFingerPrintEvent event){
            // Save the event object for later use.
            readFPEvent = event; 
        }                  
        public void setPyEnrollFingerPath(String pyEnrollFinger){
            this.pyEnrollFinger= pyEnrollFinger;
        }        
        public void setPySearchFingerPath(String pySearchFinger){
            this.pySearchFinger = pySearchFinger;
        }                
        public void setPyReadFingerPath(String pyReadFinger){
            this.pyReadFinger = pyReadFinger;
        }
        public void setPyDeleteFingerPath(String pyDeleteFinger){
            this.pyDeleteFinger= pyDeleteFinger;
        }        
        public void setPyDownloadFingerPath(String pyDownloadFinger){
            this.pyDownloadFinger= pyDownloadFinger;
        }

        
        @Override 
        public void run(){ 
            String ret="";
            while ((running)&&(myProcess.isAlive() || myProcess.exitValue()==0)) {
                try {
                    //Logger.getLogger(CreateAccount.class.getName()).log(Level.INFO, "Sleeping...", this);
                    Thread.sleep((long)100);
                                        
                    //Logger.getLogger(CreateAccount.class.getName()).log(Level.INFO, "Processing...", this);                    
                    try{                        
                        System.out.println("Reading from the Fingerprint sensor output...");               

                        String line = in.readLine();       
                        if(line != null){
                            ret = ret + "\n" + line;                            
                            if(line.contains("<output>")){
                                 System.out.println("Finished reading the Fingerprint sensor"); 
                                 this.capturedValue = line.substring(8, line.length());
                                 
                                 // Signal the even by invoking the interface's method.
                                 this.readFPEvent.readFingerprintSensorEvent();                                      
                            }
                        }                        
                        //this.capturedValue = ret;
                        //System.out.println("The returned value is: " + ret);
                                           
                        //System.out.println(capturedValue);
                        try{
                            String errorLine = err.readLine();   
                            if(errorLine!=null){
                                System.out.println("ERROR FROM FINGERPRINT SENSOR: " + errorLine);
                                
                            }
                        }catch(Exception e){}
                                          
                    }catch(IOException e){
                        System.out.println(e); 
                        System.out.println("Error reading the output from the Fingerpint Procedure");
                                                           
                    }    
                    
                    
                } catch (InterruptedException e) {
                    Logger.getLogger(CreateAccount.class.getName()).log(Level.SEVERE, null, e);
                    running = false;
                }
            }
            
        }
        
        public String getCapturedValue(){
            return this.capturedValue;
        }
        
        public void execPyEnrollFinger(){
            try{                
                ProcessBuilder pb = new ProcessBuilder("python3",this.pyEnrollFinger);
                myProcess = pb.start();                           
                in = new BufferedReader(new InputStreamReader(myProcess.getInputStream()));                
                err = new BufferedReader(new InputStreamReader(myProcess.getErrorStream()));                                                 
                running = true;
            }catch(IOException e){
                System.out.println(e); 
                System.out.println("Error executing Python Enroll Fingerpint procedure");
            }                            
        }            
        
        public BufferedReader execPySearchFinger(String fingerPrintChar1, String fingerPrintLogIn  ){
            List<String> command = new ArrayList();
            
            try{                
                ProcessBuilder pb = new ProcessBuilder("python3",this.pySearchFinger + " \"" + fingerPrintChar1 + "\"" +  " \"" + fingerPrintLogIn + "\"");
                System.out.println("Executing cmd: " + "python3 " + this.pySearchFinger + " \"" + fingerPrintChar1 + "\"" +  " \"" + fingerPrintLogIn + "\"");
                myProcess = pb.start();
                in = new BufferedReader(new InputStreamReader(myProcess.getInputStream()));                                 
                err = new BufferedReader(new InputStreamReader(myProcess.getErrorStream()));                                                 
                running = true;
            }catch(IOException e){
                System.out.println(e); 
                System.out.println("Error executing Python Search Fingerpint procedure");
            }
            return in;
        }      
        public BufferedReader runPySearchFinger(String fingerPrintChar1, String fingerPrintLogIn  ){       
            
            try{                
                ProcessBuilder pb = new ProcessBuilder("python3",this.pySearchFinger, fingerPrintChar1 ,  fingerPrintLogIn );
                System.out.println("Executing cmd: " + "python3 " + this.pySearchFinger + " \"" + fingerPrintChar1 + "\"" +  " \"" + fingerPrintLogIn + "\"");
                myProcess = pb.start();
                in = new BufferedReader(new InputStreamReader(myProcess.getInputStream()));                                 
                err = new BufferedReader(new InputStreamReader(myProcess.getErrorStream()));                        
                running = true;
            }catch(IOException e){
                System.out.println(e); 
                System.out.println("Error executing Python Search Fingerpint procedure");
            }
            return in;
        } 
        
        public void execPyReadFinger(){
            try{                
                ProcessBuilder pb = new ProcessBuilder("python3",this.pyReadFinger );
                myProcess = pb.start();
                in = new BufferedReader(new InputStreamReader(myProcess.getInputStream()));                
                err = new BufferedReader(new InputStreamReader(myProcess.getErrorStream()));                        
                if(myProcess.isAlive() || !(myProcess.exitValue()==0))
                    running = true;
                else
                    System.err.println("ERROR TO LAUCH PYTHON PROCES @ execPyReadFinger");
            }catch(IOException e){
                System.out.println(e); 
                System.out.println("Error executing Python Read Fingerpint procedure");
            }
         }
                
        public void execPyDeleteFinger(){
            try{
                
                ProcessBuilder pb = new ProcessBuilder("python3",this.pyDeleteFinger );
                myProcess = pb.start();
                in = new BufferedReader(new InputStreamReader(myProcess.getInputStream()));                
                err = new BufferedReader(new InputStreamReader(myProcess.getErrorStream()));                        
                running = true;
            }catch(IOException e){
                System.out.println(e); 
                System.out.println("Error executing Python Delete Fingerprint procedure");
            }                            
        }      
        public void execPyDownloadFinger(){
            try{                
                ProcessBuilder pb = new ProcessBuilder("python3",this.pyDownloadFinger );
                myProcess = pb.start();
                in = new BufferedReader(new InputStreamReader(myProcess.getInputStream()));                
                err = new BufferedReader(new InputStreamReader(myProcess.getErrorStream()));                        
                running = true;
            }catch(IOException e){
                System.out.println(e); 
                System.out.println("Error executing Python Download Fingerprint procedure");
            }                            
        }      
            
        public void terminate() {
//            if(running && this.myProcess.isAlive()){
//                PrintWriter writer;
//                try {
//                    File file = new File(basePath + "/src/pythonCode/terminateSensorReading.true");
//                    if (!file.exists())
//                        file.createNewFile();
//                    writer = new PrintWriter(file.getPath(), "UTF-8");
//                    writer.println("FinishSensorReadout");                
//                    writer.close();
//                } catch (FileNotFoundException | UnsupportedEncodingException ex) {
//                    Logger.getLogger(FingerPrintAuth.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (IOException ex) {
//                    Logger.getLogger(FingerPrintAuth.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
            if(running && this.myProcess.isAlive()){
                myProcess.destroyForcibly();
            }
            running = false;
        }
        
        public String convertImageFiletoBase64(String fingerprintFile){
            
            File file = new File(fingerprintFile);
            
            FileInputStream fis;
            String fingerprintFileData ="";

            try {
                fis = new FileInputStream(file);
                byte[] data = new byte[(int) file.length()];
                try {
                    fis.read(data);
                    fis.close();
                } catch (IOException ex) {
                    Logger.getLogger(CreateAccount.class.getName()).log(Level.SEVERE, null, ex);
                }            

                fingerprintFileData = Base64.getEncoder().encodeToString(data);                    

            } catch (FileNotFoundException ex) {
                Logger.getLogger(CreateAccount.class.getName()).log(Level.SEVERE, null, ex);
            }
            return fingerprintFileData;
        }
    

        public BufferedImage convertToImage(String base64String)  {
            byte[] decodedString = Base64.getDecoder().decode(base64String);

            
            BufferedImage bufferedImage = null;
           
            System.out.println("LENGTH: " + decodedString.length);
            InputStream inputStream = new ByteArrayInputStream(decodedString);
            
            try {
                bufferedImage = ImageIO.read(inputStream);
            } catch (IOException ex) {
                Logger.getLogger(FingerPrintAuth.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return bufferedImage;        
        }

    }     
            
      
        
     

