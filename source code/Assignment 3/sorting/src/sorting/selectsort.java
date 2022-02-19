package sorting;

public class selectsort {
	
	public selectsort() {
		int[] a = {6,3,7,4,1,5,2,0,8,9};
		for(int i = 0; i < a.length; i++) {
			int temp = a[i];
			int min = i;
			for(int j = i+1; j < a.length;j++) {
				if(a[j] < temp) {
					temp = a[j];
					min = j;
					
				}
			}
			if(min!=i) {
				a[min] = a[i];
				a[i] = temp;
			}
		}
		for(int i : a) {
			System.out.print(i);	
		}
		
		
	}
	public static void main(String[] args) {
		new selectsort();
	}
}
