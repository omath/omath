/* Generated By:JJTree: Do not edit this line. ASTOptional.java */

package org.omath.parser;


 class ASTOptional extends FullFormNode {
  
	private boolean reachedSingleChild = false;
	public ASTOptional(int id) {
    super(id);
  }

  public ASTOptional(SyntaxParserImpl p, int id) {
    super(p, id);
  }
  
  @Override
  public void jjtClose() {
  	  //we need to call getPattern() as long as the number of children is >1
	  setHead("TEMP");
	  super.jjtClose();
  	  if(children.size()>1){
  		  while(!reachedSingleChild){
  			  getPattern();
  		  }
  	  }
  }
  
 //this method looks at the list of children and removes from the end 
  //a number of them, creating a new one. The number that is removed depends
  //on their type.
private void getPattern(){

	//GENERAL FRAMEWORK
	
	//is there a node?
	//no: assert false.
	
	//are there two nodes?
	//no: copy the only child onto yourself. (remember to change the parent too)
	//return.
	
	//yes: look at the penultimate node.
	
	//is it a named pattern?
	//yes: create an optional node from it and the last. push it. return.
	
	//no: is there an antepenultimate node?
	  //yes: create a pattern from the antepenultimate and the penultimate. 
	   //put resulting node as child of "this" and also push the ultimate node. return.
	  //no: 
		//is ultimate node a named pattern?
		//yes: throw exption (cannot name a named pattern).
		//no: create a named pattern using penultimate and ultimate. push it. return.
	
	
	//Here's the code:
	
	Node ultimate;
	Node penultimate;
	Node antepenultimate;

	//is there a node?
	//no: assert false.
	if(jjtGetNumChildren()==0){
		assert(false);
		return;
	}
	
	//are there two nodes?
	if(jjtGetNumChildren()==1){
	//no: copy the only child onto yourself. (remember to change the parent too)
	//return.
		setHead("BLAH");
		replaceByOnlyChild();
		reachedSingleChild=true;
		return;
	}
	
	//yes: look at the penultimate node.
	penultimate = jjtGetChild(jjtGetNumChildren()-2);

	//is it a named pattern?
	if(((FullFormNode)penultimate).getHead().toString().equals("Pattern")){
		//yes: create an optional node from it and the last. push it. return.
		ultimate = popChild(); 
		penultimate = popChild();

		FullFormNode node = CREATEFULLFORM("Optional", penultimate, ultimate);
		jjtAddChild(node,jjtGetNumChildren());
		return;
	}
	
	//no: is there an antepenultimate node?
	if(jjtGetNumChildren()>2){
		//yes: create a pattern from the antepenultimate and the penultimate. 
		//TODO: assert that antepenultimate is a symbol.
		ultimate = popChild();
		penultimate = popChild(); 
		antepenultimate = popChild();
		FullFormNode node = CREATEFULLFORM("Pattern", antepenultimate, penultimate);

		//put resulting node as child of "this" and also push the ultimate node. return.
		
		jjtAddChild(node,jjtGetNumChildren());
		jjtAddChild(ultimate,jjtGetNumChildren());
		return;

	}
	
	ultimate = popChild();
	//no: 
	//is ultimate node a named pattern?
	if(((FullFormNode)ultimate).getHead().toString().equals("Pattern")){
		//yes: throw exption (cannot name a named pattern).
		throw(new Error("cannot name a named pattern!"));
	}
	//no: create a named pattern using penultimate and ultimate. push it. return.
	penultimate = popChild();
	FullFormNode node = CREATEFULLFORM("Pattern", penultimate, ultimate);

	jjtAddChild(node,jjtGetNumChildren());
	return;
}

}
