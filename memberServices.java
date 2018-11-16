//Class declaration for memberServices
//sub-architecture 1
//allows the provider to add services provided to member for billing


public class memberServices{
  
  //data fields
  protected int provider; //9-digit provider# 
  protected int member;  // 9-digit provider#
  protected String comments;
  protected int size = 20;
  protected int [] memDirectory = new int[size];

  public String getString(){
    String returnThis = new String();
    returnThis = inputScanner.nextLine();
    return returnThis;
  }

  public int getInt(){
    int return_this;
    return_this = inputScanner.nextInt();
    return return_this;
  }

  protected boolean isValid(int number){
    //check if member number is appropriate length
    if(number < 100000000 || number > 999999999){
      return false;
    }

    for(int i = 0; i < size; ++i){
      if(number == memDirectory[i])
        return true;
    }
    
    return false;
    }

}
