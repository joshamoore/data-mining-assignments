import java.util.ArrayList;

public class ID3_Node {
	public double entropy;
	public ArrayList<String> data;
	public int splitAttrib;
	
	public int splitVal;
	
	public ID3_Node children[];
	public ID3_Node parent;
	
	public ArrayList<String> data()
	{
		data = new ArrayList<String>();
		
		return data;
	}
	public double getEntropy() {
		return entropy;
	}

	public void setEntropy(double entropy) {
		this.entropy = entropy;
	}

	public ArrayList<String> getData() {
		return data;
	}

	public void setData(ArrayList<String> data) {
		this.data = data;
	}

	public int getSplitAttrib() {
		return splitAttrib;
	}

	public void setSplitAttrib(int splitAttrib) {
		this.splitAttrib = splitAttrib;
	}

	public int getSplitVal() {
		return splitVal;
	}

	public void setSplitVal(int splitVal) {
		this.splitVal = splitVal;
	}

	public ID3_Node[] getChildren() {
		return children;
	}

	public void setChildren(ID3_Node[] children) {
		this.children = children;
	}

	public ID3_Node getParent() {
		return parent;
	}

	public void setParent(ID3_Node parent) {
		this.parent = parent;
	}
	
}
