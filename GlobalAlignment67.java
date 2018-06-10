import java.util.ArrayList;

public class GlobalAlignment67 {

	public static final int MAX_LENGTH	= 100;

	public static final int MATCH_SCORE	= 2;
	public static final int MISMATCH_SCORE	= -1;
	public static final int GAP_PENALTY	= -2;

	public static final int STOP		= 0;
	public static final int UP		= 1;
	public static final int LEFT		= 2;
	public static final int DIAG		= 3;
	public static final int UPDIAG	= 4;
	public static final int LEFTDIAG	= 5;
	public static final int UPLEFT 	= 6; 
	public static final int ALL		= 7;
	public static int m;
	public static int n;
	public static int numPath;
	
	public static int trace[][] = new int[MAX_LENGTH+1][MAX_LENGTH+1]; /* trace matrix */	
	public static ArrayList<String> al = new ArrayList<String>();
	

    public static void main(String[] args) {

	int i, j, similarityCount;
	int alignmentLength, score, tmp, tmpDiag, tmpLeft, tmpUp;
	
	double idPercentage = 0;
	int hammingDistance = 0;
	
	
	similarityCount = 0;
	String X = "ATTA";
	String Y = "ATTTTA";

	int F[][] = new int[MAX_LENGTH+1][MAX_LENGTH+1];     /* score matrix */
	
	char[] alignX = new char[MAX_LENGTH*2];	/* aligned X sequence */
	char[] alignY = new char[MAX_LENGTH*2];	/* aligned Y sequence */

	m = X.length();
	n = Y.length();


	//
	// Initialise matrices
	//

	F[0][0] = 0;
	trace[0][0] = STOP;
	for ( i=1 ; i<=m ; i++ ) {
		F[i][0] = F[i-1][0] + GAP_PENALTY;
		trace[i][0] = STOP;
	}
	for ( j=1 ; j<=n ; j++ ) {
		F[0][j] = F[0][j-1] + GAP_PENALTY;
		trace[0][j] = STOP;
	}


	//
	// Fill matrices
	//

	for ( i=1 ; i<=m ; i++ ) {

		for ( j=1 ; j<=n ; j++ ) {
			
			if ( X.charAt(i-1)==Y.charAt(j-1) ) {
				tmpDiag = F[i-1][j-1] + MATCH_SCORE;
			} else {
				tmpDiag = F[i-1][j-1] + MISMATCH_SCORE;
			}
			tmpUp = F[i-1][j] + GAP_PENALTY;
			tmpLeft = F[i][j-1] + GAP_PENALTY;
			
			if(tmpLeft>tmpUp && tmpLeft>tmpDiag) {
				F[i][j] = tmpLeft;
				trace[i][j] = LEFT;
			}
			if(tmpDiag>tmpLeft && tmpDiag>tmpUp) {
				F[i][j] = tmpDiag;
				trace[i][j] = DIAG;
			}
			if(tmpUp>tmpLeft && tmpUp>tmpDiag) {
				F[i][j] = tmpUp;
				trace[i][j] = UP;
			}
			if(tmpLeft == tmpDiag && tmpLeft>tmpUp) {
				F[i][j] = tmpLeft;
				trace[i][j] = LEFTDIAG;
			}
			if(tmpLeft == tmpUp && tmpLeft>tmpDiag) {
				F[i][j] = tmpLeft;
				trace[i][j] = UPLEFT;
			}
			if(tmpDiag == tmpUp && tmpDiag>tmpLeft) {
				F[i][j] = tmpDiag;
				trace[i][j] = UPDIAG;
			}
			if(tmpUp == tmpLeft && tmpLeft == tmpDiag && tmpUp == tmpDiag) {
				F[i][j] = tmpUp;
				trace[i][j] = ALL;
			} 
			
		}
	}


	//
	// Print score matrix
	//

	System.out.println("Score matrix:");
	System.out.print("      ");
	for ( j=0 ; j<n ; ++j ) {
		System.out.print("    " + Y.charAt(j));
	}
	System.out.println();
	for ( i=0 ; i<=m ; i++ ) {
		if ( i==0 ) {
			System.out.print(" ");
		} else {
			System.out.print(X.charAt(i-1));
		}
		for ( j=0 ; j<=n ; j++ ) {
			System.out.format("%5d", F[i][j]);
		}
		System.out.println();
	}
	System.out.println();
	

	//
	// Trace back from the lower-right corner of the matrix
	//
	
	int pathCount = pathCounter(m,n);
	
    System.out.println("number of paths: "+pathCount);
   
  
    
    for(int a=0; a<pathCount; a++){
    al.add("");
    }
    numPath=0;
    pathFinder(m,n);
    System.out.println("Different paths:\n"+al+"\n");
    System.out.println("Alignments:\n");
    
    ////alignments here
    
    

	
	for(int c=0; c<pathCount; c++) {
		i = m;
		j = n;
		int d=0;
		alignmentLength = 0;
		String alPath = al.get(c);
		int alPathLength = al.get(c).length();
		while(Character.getNumericValue(alPath.charAt(d))!=STOP) {
			
			
				switch ( Character.getNumericValue(alPath.charAt(d))) {

					case DIAG:
						
						alignX[alignmentLength] = X.charAt(i-1);
						alignY[alignmentLength] = Y.charAt(j-1);
						d++;
						i--;
						j--;
						alignmentLength++;
						break;

					case LEFT:
						
						alignX[alignmentLength] = '-';
						alignY[alignmentLength] = Y.charAt(j-1);
						d++;
						j--;
						alignmentLength++;
						break;

					case UP:
						
						System.out.println("u");
						alignX[alignmentLength] = X.charAt(i-1);
						alignY[alignmentLength] = '-';
						d++;
						i--;
						alignmentLength++;
				}
		}
			//
			// Unaligned beginning
			//

			while ( i>0 ) {
				alignX[alignmentLength] = X.charAt(i-1);
				alignY[alignmentLength] = '-';
				i--;
				alignmentLength++;
			}

			while ( j>0 ) {
				alignX[alignmentLength] = '-';
				alignY[alignmentLength] = Y.charAt(j-1);
				j--;
				alignmentLength++;
			}


			//
			// Print alignment
			//

			for ( i=alignmentLength-1 ; i>=0 ; i-- ) {
				System.out.print(alignX[i]);
			}
			System.out.println();
			
			for ( i=alignmentLength-1 ; i>=0 ; i-- ) {
				if(alignX[i] == alignY[i]) {
					System.out.print("|");
					similarityCount++; 
				}else {
					System.out.print(" ");
				}
			}
			System.out.println();
			
			
			for ( i=alignmentLength-1 ; i>=0 ; i-- ) {
				System.out.print(alignY[i]);
			}
			System.out.println();
			System.out.println();
			
			
		
	}
	

    }  
    
  
    public static int pathCounter(int i, int j) {          
   
   
    		switch ( trace[i][j] ) {
    			case DIAG:
    				return pathCounter(i-1,j-1);
    			case LEFT:
    				return pathCounter(i,j-1);
    			case UP:
    				return pathCounter(i-1,j);
    			case LEFTDIAG:
    				return pathCounter(i,j-1) + pathCounter(i-1,j-1);
    			case UPLEFT:
    				return pathCounter(i-1,j) + pathCounter(i,j-1);
    			case UPDIAG:
    				return pathCounter(i-1,j-1) + pathCounter(i-1,j);
    			case ALL:
    				return pathCounter(i-1,j-1) + pathCounter(i-1,j) + pathCounter(i,j-1);
    			default:
    				return 1;
    		
    	}

    } 
	
    public static void pathFinder(int i, int j) {
    	   
    	   
		switch ( trace[i][j] ) {
		
			case LEFT:
				
				al.set(numPath, al.get(numPath)+LEFT);
				pathFinder(i,j-1);
				break;

			case DIAG:
				
			 	al.set(numPath, al.get(numPath)+DIAG);
				pathFinder(i-1,j-1);
				break;

			case UP:
				
				al.set(numPath, al.get(numPath)+UP);        
				pathFinder(i-1,j);
				break;
				
			case LEFTDIAG:
				
				String dummy = al.get(numPath);
				al.set(numPath, al.get(numPath)+LEFT);  
				pathFinder(i,j-1);
				numPath+=1;
				al.set(numPath, dummy+DIAG);
				pathFinder(i-1,j-1);
			    break;
			     
			case UPLEFT:
				
				String dummy1 = al.get(numPath);
				al.set(numPath, al.get(numPath)+LEFT);		
				pathFinder(i,j-1);
				numPath+=1;
				al.set(numPath, dummy1+UP);
				pathFinder(i-1,j);
				break;
			
			case UPDIAG:
				
				String dummy2 = al.get(numPath);
				al.set(numPath, al.get(numPath)+DIAG); 
				pathFinder(i-1,j);
				numPath+=1;
				al.set(numPath, dummy2+UP);
				pathFinder(i-1,j-1);
				break;
				
			case ALL:
				
				String dummy3 = al.get(numPath);
				al.set(numPath, al.get(numPath)+LEFT); 	 
				pathFinder(i,j-1);
				numPath+=1;
				al.set(numPath, dummy3+DIAG);
				pathFinder(i-1,j-1);
				numPath+=1;
				al.set(numPath, dummy3+UP);
				pathFinder(i-1,j);
				break;
			case STOP:
				al.set(numPath, al.get(numPath)+STOP);
				break;
		
	}

} 
   
}
