/*
 * Independent Study Project
 * MATH*4240 Advanced Topics in Modelling (Winter 2001)
 * Michael Bochenek [ID: 0041056] mboche01@uoguelph.ca
 */

import java.awt.Graphics;

/**
 * @author Michael Bochenek (mboche01@uoguelph.ca)
 */

public interface Screen
{
   public boolean mouseMoveChanged (int x, int y);

   public int checkAction (int x, int y);

   public void paint (Graphics g);
}


