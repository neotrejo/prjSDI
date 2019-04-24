/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Diana
 */
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.io.File;

import core.utils.FileTreeModel;
public class FileTreeDemo {
  public static void main(String[] args) {
    // Figure out where in the filesystem to start displaying
    File root;
    if (args.length > 0) root = new File(args[0]);
    else root = new File("C:\\Users\\Public\\Documents\\Logica");

    // Create a TreeModel object to represent our tree of files
    FileTreeModel model = new FileTreeModel(root);

    // Create a JTree and tell it to display our model
    JTree tree = new JTree();
    tree.setModel(model);

    // The JTree can get big, so allow it to scroll.
    JScrollPane scrollpane = new JScrollPane(tree);
    
    // Display it all in a window and make the window appear
    JFrame frame = new JFrame("FileTreeDemo");
    frame.getContentPane().add(scrollpane, "Center");
    frame.setSize(400,600);
    frame.setVisible(true);
  }
}

/**
 * The methods in this class allow the JTree component to traverse
 * the file system tree, and display the files and directories.
 **/


