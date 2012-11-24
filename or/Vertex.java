/*
 * Independent Study Project
 * MATH*4240 Advanced Topics in Modelling (Winter 2001)
 * Michael Bochenek [ID: 0041056] mboche01@uoguelph.ca
 */


/**
 * This class contains data for a 2-D point 
 * @author Michael Bochenek (mboche01@uoguelph.ca)
 */
public class Vertex
{
   /** x coordinate */
   private double x;

   /** y coordinate */
   private double y;


   /**
    * Constructor that sets the coordinates
    * @param xIn x coordinate
    * @param yIn y coordinate
    */
   public Vertex (double xIn, double yIn)
   {
      x = xIn;
      y = yIn;
   } 


   /**
    * Constructor that sets both of the coordinates to 0
    */
   public Vertex ()
   {
      x = 0.0;
      y = 0.0;
   }


   /**
    * Get the x coordinate
    * @return value of the x coordinate
    */
   public double getX()
   {
      return x;
   }


   /**
    * Get the y coordinate
    * @return value of the y coordinate
    */
   public double getY()
   {
      return y;
   }

}



