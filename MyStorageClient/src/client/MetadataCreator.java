/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cuong Nguyen Ngoc
 */
public class MetadataCreator implements Runnable{

    private String dirPath;
    
    public MetadataCreator(String dirPath){
        this.dirPath = dirPath;
    }
    
    @Override
    public void run() {
        try {
            setMetaData();
        } catch (IOException ex) {
            Logger.getLogger(MetadataCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void setMetaData() throws IOException{
        
        File metadata = new File(dirPath+"\\metadata");
        // if file doesnt exists, then create it
        if (!metadata.exists()) {
                metadata.createNewFile();
        }
//        Path file = Paths.get(metadata.getAbsolutePath());
//        Files.setAttribute(file, "dos:hidden", true); // To set file to be hidden
        
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(metadata), "utf-8"))) {
            writeMetaDataToFiles(new File(dirPath), bw);
        }
        
    }
    
    public void setMetaData(Map<String,Long> metadata) throws IOException{
        File meta = new File(dirPath+"\\metadata");
        // if file doesnt exists, then create it
        if (!meta.exists()) {
            meta.createNewFile();
        }
        
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(meta), "utf-8"))) {
            for(Map.Entry<String, Long> entry : metadata.entrySet()){
                bw.write(entry.getKey()+":"+entry.getValue());
                bw.write("\r\n");
            }
        }
    }
    
    private void writeMetaDataToFiles(File dir, BufferedWriter bw) throws IOException{
        File[] files = dir.listFiles();
        for(File fuzzyFile : files){
            if(fuzzyFile.isFile() && !fuzzyFile.getName().equals("metadata")){
                bw.write(fuzzyFile.getName() + ":" + String.valueOf(fuzzyFile.lastModified()));
                bw.write("\r\n");
            }else if(fuzzyFile.isDirectory()){
                writeMetaDataToFiles(fuzzyFile, bw);
            }
        }
    }
}
