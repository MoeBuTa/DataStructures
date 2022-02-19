package sorting;
import java.util.*;


public class bubblesort {
	int[] a = {6,3,7,4,1,5,2,0,8,9};
	
	


	
	public bubblesort() {
		//9-0
		for(int i = 0; i<a.length-1; i++) {
			for(int j = 0; j<a.length-1;j++) {
				if(a[j]<a[j+1]) {
					int temp = a[j];
					a[j] = a[j+1];
					a[j+1] = temp;
				}
			}
		}

		for(int n: a) {
			System.out.print(n);	
		}
		//0-9
		for(int i = 0; i<a.length-1; i++) {
			for(int j = 0; j<a.length-1;j++) {
				if(a[j]>a[j+1]) {
					int temp = a[j];
					a[j] = a[j+1];
					a[j+1] = temp;
				}
			}
		}
		System.out.println();
		for(int q: a) {
			System.out.print(q);	
		}
		

		
	}
	public static void main(String[] args) {
		new bubblesort();
	}
}
