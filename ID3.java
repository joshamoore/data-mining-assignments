// ID3 Algorithm implementation

import java.util.*;
import java.io.*;  
import java.lang.Math;

public class ID3 {
	public static void main(String[] args) throws FileNotFoundException
	{
		// Data input should return a 2D array
		String fileLoc;
		String currLine;
		String target;
		int rows = 0;
		int cols = 0;
		int currRow = 0;
		int count = 0;
		String[] curr;
		int line = 0;
		int ruleCount = 0;

		Scanner keyboard = new Scanner(System.in);
		ArrayList<String> temp = new ArrayList<String>();
		
		// Text file saved on desktop in a folder named a4, enter just the name of the file
		System.out.println("Enter the name of your text file: ");
		fileLoc = keyboard.nextLine();
		
		System.out.println("Enter a target attribute: ");
		target = keyboard.nextLine();
		
		//Scanner inFile = new Scanner(new FileReader("/Users/josh/desktop/a4/"+fileLoc));
		Scanner inFile = new Scanner(new FileReader(fileLoc));

	    while (inFile.hasNext()) 
	    { 
	   		// Splits data by line
	    	currLine = inFile.nextLine();
	    	
	    	if (line>0)
	   			rows++;
	   		line++;
	   		for (String retval: currLine.split("\\s+"))
	   		{
	   			// Find where the target column is
	   			// The rest of the columns are predictors
	   			temp.add(retval);
	   			// These are the value rows
	            if (line==1)
	            {
	                cols++;
	            }
	        }
		}		
	    // Here we create our 2D array of size [rows][cols]
	    String[][] db =  new String[rows][cols];
	    for (int i = 0; i < rows; i++)
	    {
	    	for (int j = 0; j < cols; j++)
	    	{
	    		// row*col+ pos
	    		db[i][j] = temp.get(i*cols+j);
	    		//System.out.print(db[i][j]+ " ");
	    	}
	    	//System.out.println();
	    }
		// Get Attributes > Build the tree > Print the tree
		getAttributes(db, target, rows, cols);
	}
	
	// This method finds the attributes necessary for building our tree
	public static void getAttributes(String[][] data, String target, int rows, int cols)
	{
		int yes = 0;
		int no = 0;
		int targetCol = 0;
		String curr = "";
		
		// Keep these values to find our decision node
		String decisionNode = null;
		double largest = 0.0;
		
		// This builds our outcomes with respect to our target
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < cols; j++)
			{
				//System.out.println("data["+i+"]["+j+"]: "+data[i][j]+", Target: "+target);
				curr = data[i][j];
				if (curr.equalsIgnoreCase(target))
				{
					targetCol = j;
					//System.out.println("Here");
				}
				// This gets our number of yes' and no's
				if (j==targetCol)
				{
					if (curr.equalsIgnoreCase("No"))
						no++;
					if (curr.equalsIgnoreCase("Yes"))
						yes++;
				}
			}
		}
		// Here we calculate our entropy for our target
		//System.out.println("Entropy of "+target+": "+entropy(yes,no));
		double entropyTarget = entropy(yes,no);
		double tempEF;
		double currGain;
		//We can now calculate the information gains of all predictors to determine which one will be the decision node
		for (int i = 0; i < cols; i++)
		{
			String attribute = data[0][i];
			// If we're not in the target column, calculate the entropy gain
			if (!attribute.equalsIgnoreCase(target))
			{
				// This calculates the gain of a given node
				tempEF = entropyFreq(entropyTarget, attribute, data, rows, cols, targetCol );
				currGain = gain(tempEF, entropyTarget);
				//System.out.println("Attribute: "+attribute+" Entropy: "+tempEF);
				if (currGain > largest)
				{
					largest = currGain;
					decisionNode = attribute;
					System.out.println("Decision Node: "+decisionNode+", Gain: "+largest);
				}
			}
		}
		buildTree(decisionNode,data,target,rows,cols,targetCol);
	}
	// This method calculates the entropy given the number of yes' and no's and returns the entropy
	public static double entropy(int yes, int no)
	{
		double entropy;
		int total = yes+no;
		double yesTot = (double)yes/(total);
		double noTot = (double)no/(total);
		// -(yes/total)log2(yes/total) - (no/total)log2(no/total)
		if (no!=0 && yes!=0)
			entropy = -yesTot*(Math.log(yesTot)/Math.log(2)) - noTot*(Math.log(noTot)/Math.log(2));
		else if (no == 0)
			entropy = -yesTot*(Math.log(yesTot)/Math.log(2));
		else
			entropy = -noTot*(Math.log(noTot)/Math.log(2));
		return entropy;
	}
	// This calculates our entropy between our target and a given predictor, which will be used to calculate the gain
	public static double entropyFreq(double target, String predictor, String data[][], int rows, int cols, int targetCol)
	{
		ArrayList<String> values = new ArrayList<String>();				
		int predCol = 0;
		double entropy = 0.0;
		String curr;
		int count = 0;

		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < cols; j++)
			{
				curr = data[i][j];
				
				// So we know what column we can add from
				if (i==0 && curr.equalsIgnoreCase(predictor))
					predCol = j;
				// If this value does not have a node
				// create the node and add it
				// We need to loop through our array list of values to see if our current value is new
				if ((i*cols+predCol)==(i*cols+j))
				{
					if (!values.contains(curr) && i!=0)
					{
						values.add(curr);
						count++;
					}
				}
			}
		}
		// We can initialize an array of size count*2 to contain the yes' and the no's for each value
		// This is quite confusing as we have 2 tables that directly correlate with each other
		// Odds are yes' and Evens are no's
		String[] valTable = new String[count];
		int[] freqTable = new int[count*2];
		String a = "";
		String b = "";
		for (int i = 0; i < values.size(); i++)
		{
			valTable[i] = values.get(i);
		}
		for (int i = 0; i < rows; i++) 
		{
			for (int j = 0; j< cols; j++)
			{		
				// a keeps track of the predictors value
				if ((i*cols+predCol)==(i*cols+j) && i!=0)
					a = data[i][j];
				// b keeps track of the yes or no value
				if ((i*cols+targetCol )==(i*cols+j) && i!=0)
					b = data[i][j];	
			}
			// organizing this array is tricky
			// first search through our array of names
			for (int k = 0; k < valTable.length; k++)
			{

				// if the name is found
				if (valTable[k].equalsIgnoreCase(a) && valTable[k]!=null)
				{
					// multiply our position by 2 if its a yes and multiply our position by 2 and add 1 if its a no
					if (b.equalsIgnoreCase("Yes"))
						freqTable[k*2]++;
					if (b.equalsIgnoreCase("No"))
						freqTable[(k*2)+1]++;
				}
			}
		}
		double tempEntropy = 0.0;
		
		double prob = 0.0;
		for (int i = 0; i < valTable.length; i++)
		{
			prob = (((double)(freqTable[i*2]))+((double)(freqTable[i*2+1])))/((double)(rows));
			//System.out.println("Probability: "+prob);
			int x = freqTable[i*2];
			int y = freqTable[i*2+1];
			//System.out.println("("+x+", "+y+")"+" entropy: "+entropy(x,y));
			tempEntropy += (prob*entropy(x,y));
		}
		// We can now calculate the entropy
		entropy = tempEntropy;
		tempEntropy = 0.0;
		return entropy;
	}
	// Calculate our gain
	public static double gain(double x, double y)
	{
		double gain = y - x;
		return gain;
	}
	public static void buildTree(String decision,String[][] data,String target,int rows, int cols, int targetCol)
	{
		ArrayList<String> values = new ArrayList<String>();				
		int predCol = 0;
		double entropy = 0.0;
		String curr;
		int count = 0;

		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < cols; j++)
			{
				curr = data[i][j];
				// So we know what column we can add from
				if (i==0 && curr.equalsIgnoreCase(decision))
					predCol = j;
				// If this value does not have a node
				// create the node and add it
				// We need to loop through our array list of values to see if our current value is new
				if ((i*cols+predCol)==(i*cols+j))
				{
					if (!values.contains(curr) && i!=0)
					{
						values.add(curr);
						count++;
					}
				}
			}
		}
		// We can initialize an array of size count*2 to contain the yes' and the no's for each value
		// This is quite confusing as we have 2 tables that directly correlate with each other
		// Odds are yes' and Evens are no's
		String[] valTable = new String[count];
		int[] freqTable = new int[count*2];
		String a = "";
		String b = "";
		for (int i = 0; i < values.size(); i++)
		{
			valTable[i] = values.get(i);
			//System.out.println(valTable[i]);
		}
		for (int i = 0; i < rows; i++) 
		{
			for (int j = 0; j< cols; j++)
			{		
				// a keeps track of the predictors value
				if ((i*cols+predCol)==(i*cols+j) && i!=0)
					a = data[i][j];
				// b keeps track of the yes or no value
				if ((i*cols+targetCol )==(i*cols+j) && i!=0)
					b = data[i][j];	
			}
			// organizing this array is tricky
			// first search through our array of names
			for (int k = 0; k < valTable.length; k++)
			{

				// if the name is found
				if (valTable[k].equalsIgnoreCase(a) && valTable[k]!=null)
				{
					// multiply our position by 2 if its a yes and multiply our position by 2 and add 1 if its a no
					if (b.equalsIgnoreCase("Yes"))
						freqTable[k*2]++;
					if (b.equalsIgnoreCase("No"))
						freqTable[(k*2)+1]++;
				}
			}
		}
		for (int i = 0; i < valTable.length; i++)
		{
			System.out.println("IF ("+decision+"="+valTable[i]+") THEN "+target+" = Yes");
		}
	}
}
