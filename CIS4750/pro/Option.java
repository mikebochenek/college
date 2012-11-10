/**	
 * This class holds the options 
 */
public class Option 
{
   public int speed=0;
   public boolean gridlines = true;
   public int firepower=10;
   public boolean display = true;

   public int blueUnits = 0;
   public int blueRandom = 0;
   public int redUnits = 0;
   public int redRandom = 0;
   public int blueAI = 0;
   public int redAI = 0;
   public int blueFun = 0;
   public int redFun = 0; 
   public int blueVisibility = 0; 
   public int redVisibility = 0; 
   public int blueCom = 0; 
   public int redCom = 0; 
   private String file = "";


   /**
    * Empty constructor never explicitly called.
    */
   public Option ()
   {
   }


   /** 
    * Will read the options from filename and save the to this object.
    */
   public Option (String filename)
   {
      file = filename;
   }


   /**
    * This will actually perform the reading.
    */
   public void readOptions ()
   {
      if (file == "")
      {
         OptionInterface optionGetter = new OptionInterface(this);
      }
      else
      {
         OptionInterface optionGetter = new OptionInterface(this, file);
      }
   }
}
