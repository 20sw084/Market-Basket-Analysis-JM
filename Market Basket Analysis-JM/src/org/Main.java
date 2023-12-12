package org;

import javax.swing.SwingUtilities;

class Main {
  public static void main(String argv[]) {
	  SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
              new GUI().setVisible(true);
          }
      });
  }
}