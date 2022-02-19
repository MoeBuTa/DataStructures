package sorting;

public class mergesort {
	
	
	
	 public static void mergeSort(int[] a,int left,int right){

	        if(left<right){
	            int mid = (left+right)/2;
	            mergeSort(a,left,mid);//��߹鲢����ʹ��������������
	            mergeSort(a,mid+1,right);//�ұ߹鲢����ʹ��������������
	            merge(a,left,mid,right);//�ϲ�����������
	        }
	    }
	    private static void merge(int[] a, int left, int mid, int right) {
	        int[] temp = new int[right - left + 1];//ps��Ҳ���Դӿ�ʼ������һ����ԭ�����С��ͬ�����飬��Ϊ�ظ�new�����Ƶ�������ڴ�
	        int i = left;
	        int j = mid+1;
	        int k = 0;
	        while(i<=mid&&j<=right){
	            if (a[i] < a[j]) {
	                temp[k++] = a[i++];
	            } else {
	                temp[k++] = a[j++];
	            }
	        }
	        while(i<=mid){//�����ʣ��Ԫ������temp��
	            temp[k++] = a[i++];
	        }
	        while(j<=right){//��������ʣ��Ԫ������temp��
	            temp[k++] = a[j++];
	        }
	        //��temp�е�Ԫ��ȫ��������ԭ������
	        for (int k2 = 0; k2 < temp.length; k2++) {
	            a[k2 + left] = temp[k2];
	        }
	    }
	    public static void main(String args[]){
	    	int[] test = {6,3,7,4,1,5,2,0,8,9};
	        mergeSort(test,0,test.length-1);
	        for(int i=0; i<test.length;i++){
	            System.out.print(test[i] + " ");
	        }
	    }

}
