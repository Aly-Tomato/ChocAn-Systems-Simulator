public class isValidTest{
  
  public static void main(String[]args){

    MemberServices obj = new MemberServices();
    String FileDirectory = new String();

  

    //invalid Test
    System.out.println("************");
    System.out.println("**TEST ONE**");
    System.out.println("************");
    System.out.println("This test will give valid and invalid values to the isValid function");
    System.out.println("found in MemberServices.java\n");
    System.out.println("**Passing in less than 9 digit memberID (111).");
    System.out.println("**This test should FAIL\n");

    FileDirectory = "./directories/member_directory";
    boolean rc = obj.isValid(111,FileDirectory); 

    if(rc){
      System.out.println("isValid returned True.\nThis unit test FAILS\n");
    }else{
      System.out.println("isValid returned False as expected.\nThis unit test is a SUCCESS\n");
    }

    //Valid Test
    System.out.println("\n\n************");
    System.out.println("**TEST TWO**");
    System.out.println("************");
    System.out.println("This test will give valid and invalid values to the isValid function");
    System.out.println("found in MemberServices.java\n");
    System.out.println("**Passing in valid 9 digit memberID (993392546).");
    System.out.println("**This test should FAIL\n");

    rc = obj.isValid(993392546,FileDirectory); 

    if(rc){
      System.out.println("isValid returned True as expected.\nThis unit test is a SUCCESS\n");
    }else{
      System.out.println("isValid returned False.\nThis unit test FAILS\n");
    }

    //isSuspended Test
    System.out.println("\n\n**************");
    System.out.println("**TEST THREE**");
    System.out.println("**************");
    System.out.println("This test will check and return the status of a member");
    System.out.println("isSuspended is found in MemberServices.java\n");
    System.out.println("**Passing in valid 9 digit memberID that is suspended.");
    System.out.println("**This test should FAIL\n");

    rc = obj.isSuspended(993392546,FileDirectory); 

    if(rc){
      System.out.println("isValid returned True as expected.\nThis unit test is a SUCCESS\n");
    }else{
      System.out.println("isValid returned False.\nThis unit test FAILS\n");
    }

  }
}
