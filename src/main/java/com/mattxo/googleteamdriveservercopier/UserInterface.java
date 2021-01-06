/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mattxo.googleteamdriveservercopier;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author matt
 */
public class UserInterface extends javax.swing.JFrame {

    private ShellHandler shell;
    
    ArrayList<String> fromPath = new ArrayList<String>();
    ArrayList<String> toPath = new ArrayList<String>();
    
    
    /**
     * Creates new form UserInterface
     */
    public UserInterface() {
        initComponents();
        
        shell = new ShellHandler();
        
        populateComboBoxesWithRemotes();
        addComboBoxItemListeners();
        addTableMouseListeners();
        
        
    }
    
    RemoteFile createBackFile() {
        RemoteFile goBack = new RemoteFile();
        goBack.isDirectory = true;
        goBack.isFile = false;
        goBack.name = "../";
        return goBack;
    }
    
    void addTableMouseListeners() {
        tblTo.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    JTable target = (JTable)me.getSource();
                    int row = target.getSelectedRow();
                    int column = target.getSelectedColumn();

                    // adjust path
                    if (row == 0 && toPath.size() > 0) {
                        System.out.print("Going back ");
                        // go back
                        toPath.remove(toPath.size()-1);
                    } else {
                        System.out.print("Opening dir ");
                        toPath.add(target.getValueAt(row, column).toString());
                    }
                    
                    ArrayList<RemoteFile> dirs = shell.listDirectories(cboRemoteTo.getSelectedItem().toString() , getPath(toPath));
                    if (toPath.size() > 0) {
                        dirs.add(0, createBackFile());
                    }
                    DefaultTableModel model = generateTableModel(dirs);
                    tblTo.setModel(model);
               } else {
                    enableCopyButton();
                }
            }
        });
        
        tblFrom.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {     
                    JTable target = (JTable)me.getSource();
                    int row = target.getSelectedRow();
                    int column = target.getSelectedColumn();
                    
                    String value = target.getValueAt(row, column).toString();
                    if (value.endsWith("/")) {
                        // dir
                        // adjust path
                        if (row == 0 && fromPath.size() > 0) {
                            System.out.print("Going back ");
                            // go back
                            fromPath.remove(fromPath.size()-1);
                        } else {
                            System.out.print("Opening dir ");
                            fromPath.add(value.substring(0, value.length()-1));
                        }
                         ArrayList<RemoteFile> files = shell.listFiles(cboRemoteFrom.getSelectedItem().toString() , getPath(fromPath));
                        if (fromPath.size() > 0) {
                            files.add(0, createBackFile());
                        }
                        DefaultTableModel model = generateTableModel(files);
                        tblFrom.setModel(model);
                        
                    }
                    
                } else {
                    // single click
                    
                    
                    enableCopyButton();
                }
            }
        });
    }
    
    void enableCopyButton() {
        String fromFileName = tblFrom.getValueAt(tblFrom.getSelectedRow(), tblFrom.getSelectedColumn()).toString();
    }
    
    String getPath(ArrayList<String> path) {
        String p = "/";
        
        for (String s: path) {
            p += s+"/";
        }
        
        return p;
    }
    
    void addComboBoxItemListeners() {
        cboRemoteFrom.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent ie) {
                if (ie.getStateChange() == ItemEvent.SELECTED) {
                    ArrayList<RemoteFile> files = shell.listFiles(cboRemoteFrom.getSelectedItem().toString() , getPath(fromPath));
                    if (fromPath.size() > 0) { 
                        files.add(0, createBackFile());
                    }
                    DefaultTableModel model = generateTableModel(files);
                    tblFrom.setModel(model);
                }
            }
        });
        
        cboRemoteTo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent ie) {
                if (ie.getStateChange() == ItemEvent.SELECTED) {
                    ArrayList<RemoteFile> dirs = shell.listDirectories(cboRemoteTo.getSelectedItem().toString() , getPath(toPath));
                    if (toPath.size() > 0) {
                        dirs.add(0, createBackFile());
                    }
                    DefaultTableModel model = generateTableModel(dirs);
                    tblTo.setModel(model);
                    
                }
            }
        });
    }
    
    void populateComboBoxesWithRemotes() {
        ArrayList<String> remotes = shell.getRemotes();
        
        cboRemoteFrom.removeAllItems();;
        cboRemoteTo.removeAllItems();
        
        cboRemoteFrom.addItem(" ");
        cboRemoteTo.addItem(" ");
        
        for (String s : remotes) {
            cboRemoteFrom.addItem(s);
            cboRemoteTo.addItem(s);
        }
        
        
    }
    
    private DefaultTableModel generateTableModel(ArrayList<RemoteFile> files) {
        Object[][] cellData = new Object[files.size()][2];
        
        int pos = 0;
        
        for (int i = 0; i < files.size(); i++) {
            System.out.println(files.get(i).name);
            if (files.get(i).isDirectory) {
               cellData[pos][0] = files.get(i).name;
               cellData[pos][1] = "";
               pos++;
            }
        }
        
        for (int i = 0; i < files.size(); i++) {
            if (files.get(i).isFile) {
               cellData[pos][0] = files.get(i).name;
               cellData[pos][1] = "";
               pos++;
            }
        }
        
        DefaultTableModel model = new javax.swing.table.DefaultTableModel(
            cellData,
            new String [] {
                "File"
            }
        )  {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        };
        
        return model;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cboRemoteFrom = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        cboRemoteTo = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblFrom = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblTo = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("From");

        cboRemoteTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRemoteToActionPerformed(evt);
            }
        });

        jLabel2.setText("To");

        tblFrom.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "File", "Size"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblFrom);

        tblTo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Folder"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblTo);

        jButton1.setText("Copy");
        jButton1.setEnabled(false);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText("Debug output");
        jScrollPane3.setViewportView(jTextArea1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(309, 309, 309)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(301, 301, 301))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jScrollPane3)
                                .addGap(11, 11, 11)
                                .addComponent(jButton1))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cboRemoteFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboRemoteTo, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboRemoteFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboRemoteTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cboRemoteToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboRemoteToActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboRemoteToActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UserInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UserInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UserInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UserInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UserInterface().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cboRemoteFrom;
    private javax.swing.JComboBox<String> cboRemoteTo;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTable tblFrom;
    private javax.swing.JTable tblTo;
    // End of variables declaration//GEN-END:variables
}
