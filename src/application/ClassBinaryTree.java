/*
 * Abstract Data Structure: simple ordered binary tree using dynamically linked object instances.
 * A class with appropriate constructors and method to set data elements */

package application;

import java.util.ArrayList;

public class ClassBinaryTree {
	private Node root;

	public ClassBinaryTree() {
		root = null;
		java.util.ArrayList<Class> classes = Database.getClasses(); // Accessing data management layer
		while (!classes.isEmpty()) {
			this.add(classes.remove(0));
		}
	}

	public void add(Class data) {
		Node newNode = new Node(data);
		if (root == null)
			root = newNode;
		else
			root.add(newNode);
	}

	/*
	 * Getting data elements in a specified order
	 */
	public ArrayList<Class> inOrder(Node currentNode) {
		ArrayList<Class> list = new ArrayList<Class>();
		if (currentNode != null) {
			list.addAll(inOrder(currentNode.left));
			list.add(currentNode.data);
			list.addAll(inOrder(currentNode.right));
		}
		return list;
	}

	public Node getRootNode() {
		return root;
	}
}

class Node {
	
	Class data;
	Node left, right;

	public Node(Class data) {
		this.data = data;
		left = null;
		right = null;
	}

	public void add(Node newNode) {
		if (newNode.data.getNumberOfStudents() >= data.getNumberOfStudents()) {
			// error condition checks
			if (this.left == null)
				this.left = newNode;
			else
				this.left.add(newNode);
		} else {
			if (this.right == null)
				this.right = newNode;
			else
				this.right.add(newNode);
		}
	}
}