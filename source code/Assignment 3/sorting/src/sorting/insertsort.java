package sorting;

public class insertsort {
	
	public insertsort() {
		int[] a = {6,3,7,4,1,5,2,0,8,9};
		for(int i = 1; i < a.length; i++) {
			int temp = a[i];
			for(int j = i-1; j>=0; j--) {
				if(a[j] < temp) {
					a[j+1] = a[j];
					a[j] = temp;
				}
			}
		}
		
		for(int i : a) {
			System.out.print(i);
		}
		
	}
	public static void main(String[] args) {
		new insertsort();
	}
}
