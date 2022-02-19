package sorting;

public class quicksort {
	
	public quicksort() {
		int[] a = {6,3,7,4,1,5,2,0,8,9};
		quickSort(a,0,a.length-1);
		for(int i : a) {
			System.out.print(i);	
		}
		
	}
	public static void quickSort(int[] a, int start, int end) {
	    if(start >= end)
	        return;
	    int i = start;
	    int j = end;
	    int base = a[start];
	    while(i < j) {
	        while(a[j] >= base && i < j)
	            j--;
	        while(a[i] <= base && i < j)
	            i++;
	        if(i < j) {
	            int temp = a[i];
	            a[i] = a[j];
	            a[j] = temp;
	        }
	        System.out.print(i);
	        System.out.print(j);
	        System.out.println();
	    }
	    a[start] = a[i];
	    a[i] = base;
	    quickSort(a, start, i - 1);
	    quickSort(a, i + 1, end);
	}
	
	public static void main(String[] args) {
		new quicksort();
	}
}
