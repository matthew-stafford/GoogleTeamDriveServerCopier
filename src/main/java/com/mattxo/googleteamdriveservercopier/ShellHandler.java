/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mattxo.googleteamdriveservercopier;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

/**
 *
 * @author matt
 */
public class ShellHandler {
    
    private String rclonePath = "rclone";
    
    
    public ShellHandler() {
        
    }
    
    private ArrayList<String> executeShell(String[] cmd) {
        
        System.out.println("Executing shell command: "+Arrays.toString(cmd));
        
        ArrayList<String> lines = new ArrayList<String>();
        try {
            Runtime run = Runtime.getRuntime();
            Process shell = run.exec(cmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(shell.getInputStream()));
            String line = "";
            while ((line = br.readLine()) != null) {
                System.out.println("Shell returned: " + line);
                lines.add(line);
            }
            
            int exitCode = shell.waitFor();
            if (exitCode != 0) {
                // get error
                
                BufferedReader ebr = new BufferedReader(new InputStreamReader(shell.getErrorStream()));
                line = "";
                while ((line = ebr.readLine()) != null) {
                    System.err.println("Shell error: " + line);
                }
                System.out.println("Shell exited early? error? trying again in 5 seconds..");
                Thread.sleep(5000);
                for (int i = 0 ; i < 3; i++) {
                    ArrayList<String> attempt = executeShell(cmd);
                    if (attempt.size() > 0) {
                        return attempt;
                    }
                    Thread.sleep(250);
                }
            }
        } catch (Exception ex) { 
            ex.printStackTrace();
        } finally {
            System.out.println("Shell command returned");
            return lines;
        }
    }
    
    
    public ArrayList<String> getRemotes() {
        return executeShell(new String[] { rclonePath, "listremotes" });
    }


    public ArrayList<RemoteFile> listFiles(String remote, String path) {
        if (remote == null || remote.length() == 0 || remote.trim().length() == 0) {
            return new ArrayList<RemoteFile>();
        }
        ArrayList<String> output = executeShell(new String[] { rclonePath, "lsf", remote+""+path });
        ArrayList<RemoteFile> files = new ArrayList<RemoteFile>();
        
        for (String s : output) {
            RemoteFile f = new RemoteFile();
            f.filePath = remote+path+s;
            f.name = s;
            if (s.endsWith("/")) {
                f.isDirectory = true;
                f.isFile = false;
            } else {
                f.isDirectory = false;
                f.isFile = true;
            }
            files.add(f);
        }
        
        return files;
    }
    
    public ArrayList<RemoteFile> listDirectories(String remote, String path) {
        if (remote == null || remote.length() == 0 || remote.trim().length() == 0) {
            return new ArrayList<RemoteFile>();
        }
        ArrayList<String> output = executeShell(new String[] { rclonePath, "lsd", remote+""+path });
        ArrayList<RemoteFile> files = new ArrayList<RemoteFile>();
        
        int startIndex = 0;
        
        for (String s : output) {      
            
            if (startIndex == 0) {
                int occurances = 0;
                for (int i = 0 ; i < s.length(); i++) {
                    if (s.charAt(i) == '-' && s.charAt(i+1) == '1') {
                        occurances ++;
                        if (occurances == 2) {
                            startIndex = i+3;
                            break;
                        }
                    }
                }
            }
            
            s = s.substring(startIndex, s.length());
            
            RemoteFile f = new RemoteFile();
            f.filePath = remote+path+s;
            f.name = s;
            f.isDirectory = true;
            f.isFile = false;
            files.add(f);
        }
        
        return files;
    }
    
}
