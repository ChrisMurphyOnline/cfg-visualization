public class MyClass {
     public int something(int a, int b) {
         int x = a + b;    // line 1
         if (x > 0) {      // line 2


         
             x = x * -1;   // line 3
             System.out.println(“hello”); // line 4
         }
         return x; // line 5
     }
}
