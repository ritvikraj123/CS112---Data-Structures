package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {
	
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	
	/**
	 * Builds the DOM tree from input HTML file, through scanner passed
	 * in to the constructor and stored in the sc field of this object. 
	 * 
	 * The root of the tree that is built is referenced by the root field of this object.
	 */
	
	private boolean tagCheck(String tag, String type){
		if(type.equals("o")){
			if(tag.startsWith("<") && tag.endsWith(">") && !tag.contains("/")){
				return true;
			}
			return false;
		}
		
		if(type.equals("c")){
			if(tag.startsWith("</") && tag.endsWith(">")){
				return true;
			}
			return false;
		}
		return true; //should not reach this line
	}
	
	
	public void build() {
		/** COMPLETE THIS METHOD **/		
		TagNode pointer = null;
		Stack<TagNode> allTags = new Stack<TagNode>();
		String current;
		
		if (sc == null)
			return;
		
		while(sc.hasNextLine()){
			
			current = sc.nextLine();
			
			//if(isTag(current, "o")){
			if(tagCheck(current, "o")){
				current = current.replace("<" , "");
				current = current.replace(">" , "");
				
				if(root == null){
					root = new TagNode(current, null, null);
					allTags.push(root);
					pointer = root;
					continue;
				}
				
				if(pointer.firstChild == null){
					pointer.firstChild = new TagNode(current, null, null);
					allTags.push(pointer.firstChild);
					pointer = allTags.peek();
					continue;
				}
				
				pointer = pointer.firstChild;
				
				while(pointer.sibling!=null){
					pointer = pointer.sibling;
				}
				
				pointer.sibling = new TagNode(current, null, null);
				allTags.push(pointer.sibling);
				pointer = allTags.peek();
				continue;
			}
			
			if(tagCheck(current, "c")){
				pointer = allTags.pop();
				if(allTags.isEmpty())
					continue;
				pointer = allTags.peek();
				continue;
			}
			
			if(!tagCheck(current, "o") && !tagCheck(current, "c")){ //Not a tag
				
				if(pointer.firstChild == null){
					pointer.firstChild = new TagNode(current, null, null);
					continue;
				}
				
				else{
					pointer = pointer.firstChild;
					while(pointer.sibling!=null){
						pointer = pointer.sibling;
					}
					pointer.sibling = new TagNode(current, null, null);
					pointer = allTags.peek();
					continue;
				}
			}

		}
	}

	
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	
	private void replaceTagHelper(TagNode root, ArrayList<TagNode> outputList, String originalTag) {
		
		if (root == null) {
			return;
		}
		
		replaceTagHelper(root.firstChild, outputList, originalTag);
		
		if(root.tag.equals(originalTag)){
			outputList.add(root);
		}
		
		replaceTagHelper(root.sibling, outputList, originalTag);
	} 
	
	
	public void replaceTag(String oldTag, String newTag) {
		/** COMPLETE THIS METHOD **/
		
		if (root == null || oldTag == null || newTag == null)
			return;
		
		ArrayList<TagNode> tagList = new ArrayList<TagNode>();
		
		replaceTagHelper(root, tagList, oldTag);
		
		for(int z =0; z < tagList.size(); z++){
			tagList.get(z).tag = newTag;
		}
	
	}
	
	
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {
		/** COMPLETE THIS METHOD **/
		ArrayList<TagNode> rowlist = new ArrayList<TagNode>();
		
		boldRowHelper(root, rowlist, "tr");
		
		if(rowlist.isEmpty() || rowlist.size() < row){
			return;
		}
		
		TagNode pointer = rowlist.get(0);	//set ptr to first row
		
		for(int z=1; z < row; z++){
			pointer = pointer.sibling;
		}
		
		pointer = pointer.firstChild;	//ptr gets set to first col
		TagNode tmp;
		
		while(pointer != null){				//ptr traverses through cols
			tmp = new TagNode("b", pointer.firstChild, null);
			tmp.firstChild = pointer.firstChild;
			pointer.firstChild = tmp;
			pointer = pointer.sibling;
		}
	}
	
	
	private void boldRowHelper(TagNode root, ArrayList<TagNode> outputList, String originalTag) {
		
		if (root == null) {
			return;
		}
		
		boldRowHelper(root.firstChild, outputList, originalTag);
		
		if(root.tag.equals(originalTag)){
			outputList.add(root);
		}
		
		boldRowHelper(root.sibling, outputList, originalTag);
	}
	
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	
	//removeTag helper method for p, em, b
	private void removeTagRootHelper(TagNode root, String current) {
		
		if (root == null) 
			return;
		
		removeTagRootHelper(root.firstChild, current);
		
		if(root.sibling != null && root.sibling.tag.equals(current)){
			
			TagNode pointer = root.firstChild;
			
			while(pointer.sibling != null){
				pointer = pointer.sibling;		//sets ptr to last sibling
			}
			
			pointer.sibling = root.sibling.sibling;
			root.sibling = root.sibling.firstChild;	
		}
		
		if(root.firstChild != null && root.firstChild.tag.equals(current)){
			root.firstChild = root.firstChild.firstChild;
		}
		
		removeTagRootHelper(root.sibling, current);
	}
	
	//removeTag helper method for ol, ul
	private void removeTagRootTableHelper(TagNode root, String current){
		
		if(root == null)
			return;
		
		removeTagRootTableHelper(root.firstChild, current);
		
		if(root.sibling != null && root.sibling.tag.equals(current)){
			TagNode pointer = root.sibling.firstChild;
			
			while(pointer.sibling != null){
				pointer.tag = "p";
				pointer = pointer.sibling;
			}
			
			pointer.tag = "p";
			pointer.sibling = root.sibling.sibling;
			root.sibling = root.sibling.firstChild;
		}
		
		if(root.firstChild != null && root.firstChild.tag.equals(current)){
			TagNode pointer = root.firstChild.firstChild;
			
			while(pointer.sibling != null){
				pointer.tag = "p";
				pointer = pointer.sibling;
			}
			
			pointer.tag = "p";
			pointer.sibling = root.firstChild.sibling;
			root.firstChild = root.firstChild.firstChild;
		}
		
		removeTagRootTableHelper(root.sibling, current);
	}
	
	public void removeTag(String tag) {
		/** COMPLETE THIS METHOD **/
		if(tag.equals("p") || tag.equals("em") || tag.equals("b"))
			removeTagRootHelper(root, tag);
		if(tag.equals("ol") || tag.equals("ul"))
			removeTagRootTableHelper(root, tag);
	}
	
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	
	private void addTagHelper(TagNode root, String wordList, String addTags){
		
		if(root == null)
			return;
		
		addTagHelper(root.firstChild, wordList, addTags);
		
		if(root.firstChild != null && root.firstChild.tag.contains(wordList)){
			
			String[] words = root.firstChild.tag.split(wordList);
			
			if(words.length == 0){
				TagNode tags = new TagNode(addTags, root.firstChild, root.sibling);
				root.firstChild = tags;
			}
			
			else if(words.length == 2){
				TagNode rightside = new TagNode(words[1], null, root.firstChild.sibling);
				TagNode leftside = new TagNode(words[0], null , null);
				TagNode alreadyTagged = new TagNode(wordList, null, null);
				TagNode tags = new TagNode(addTags, alreadyTagged, rightside);
				root.firstChild = leftside;
				leftside.sibling = tags;
				tags.sibling = rightside;
				if(words[0].equals(""))
					root.firstChild = tags;
			}
			
			else{
				
				if(words[0].charAt(0)==' '){
					TagNode alreadyTagged = new TagNode(wordList, null, null);
					TagNode tags = new TagNode(addTags, alreadyTagged, null);
					TagNode right = new TagNode(words[0], null, root.firstChild.sibling);
					tags.sibling = right;
					root.firstChild = tags;
					
				} //target is the last word
				else{
					TagNode alreadyTagged = new TagNode(wordList, null, null);
					TagNode tags = new TagNode(addTags, alreadyTagged, root.firstChild.sibling);
					TagNode left = new TagNode(words[0], null, tags);
					root.firstChild = left;
				}
			}
			
		}
		
		if(root.sibling != null && root.sibling.tag.contains(wordList)){
			
			String[] words = root.sibling.tag.split(wordList);
			
			if(words.length == 0){
				TagNode tags = new TagNode(addTags, root.sibling, root.sibling.sibling);
				root.sibling = tags;
			}
			
			else if(words.length > 0){
				TagNode rightside = new TagNode(words[1], null, root.sibling.sibling);
				TagNode leftside = new TagNode(words[0], null, null);
				TagNode alreadyTagged = new TagNode(wordList, null, null);
				TagNode tags = new TagNode(addTags, alreadyTagged, rightside);
				root.sibling = leftside;
				leftside.sibling = tags;
				tags.sibling = rightside;
			}
			
			else{
				TagNode alreadyTagged = new TagNode(wordList, null, null);
				TagNode tags = new TagNode(addTags, alreadyTagged, null);
				if(words[0].charAt(0) == ' '){//target is first word
					TagNode right = new TagNode(words[0], null, root.sibling.sibling);
					tags.sibling = right;
					root.sibling = tags;
				}//target is last word
				else{
					TagNode left = new TagNode(words[0], null, tags);
					root.sibling = left;
				}
			}
		}
		
		addTagHelper(root.sibling, wordList, addTags);
	}
	
	
	public void addTag(String word, String tag) {
		/** COMPLETE THIS METHOD **/
		if(root == null)
			return; 
		
		addTagHelper(root, word, tag);

	}
	
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
	/**
	 * Prints the DOM tree. 
	 *
	 */
	public void print() {
		print(root, 1);
	}
	
	private void print(TagNode root, int level) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			for (int i=0; i < level-1; i++) {
				System.out.print("      ");
			};
			if (root != this.root) {
				System.out.print("|---- ");
			} else {
				System.out.print("      ");
			}
			System.out.println(ptr.tag);
			if (ptr.firstChild != null) {
				print(ptr.firstChild, level+1);
			}
		}
	}
}