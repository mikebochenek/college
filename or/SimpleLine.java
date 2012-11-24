/*
 * Independent Study Project
 * MATH*4240 Advanced Topics in Modelling (Winter 2001)
 * Michael Bochenek [ID: 0041056] mboche01@uoguelph.ca
 */


/**
 * Contains information for a line (namely two vertices)
 * @author Michael Bochenek (mboche01@uoguelph.ca)
 */
public class SimpleLine 
{
   private Vertex v1;
   private Vertex v2;

   /**
    * Constructor
    */
   public SimpleLine (Vertex v1In, Vertex v2In)
   {
      v1 = v1In;
      v2 = v2In;
   }


   /**
    * Constructor
    */
   public SimpleLine ()
   {

   }


   public Vertex getV1 ()
   {
      return v1;
   }


   public Vertex getV2 ()
   {
      return v2;
   }

}



