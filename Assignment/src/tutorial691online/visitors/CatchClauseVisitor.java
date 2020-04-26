package tutorial691online.visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;

import tutorial691online.handlers.SampleHandler;

public class CatchClauseVisitor extends ASTVisitor{
	private static HashSet<CatchClause> catchClauses = new HashSet<>();
	private int catchBlockCount = 0;
	private HashSet<CatchClause> throwStatements = new HashSet<>();
	private int logActionCount = 0;
	private int methodCallActionCount = 0;
	private String exceptionName;
	private ITypeBinding exceptionType;
	private String exceptionTypeName;
	private ArrayList<String> flowHandlingStatements = new ArrayList<String>();
	private int catchBlockLOC = 0;
	private ArrayList<String> catchBlockLOCStatements = new ArrayList<String>();
	private HashSet<CatchClause> emptyCatches = new HashSet<>();
	
	@Override
	public boolean visit(CatchClause node) {
		catchClauses.add(node);
		catchBlockCount++;
		
		MethodInvocationVisitor methodInvocationVisitor = new MethodInvocationVisitor("LogCatchSwitch");
		node.accept(methodInvocationVisitor);
		flowHandlingStatements.addAll(methodInvocationVisitor.getFlowHandlingActions());
		
		
		if(isfirstPatternException(node)) {
			throwStatements.add(node);
		}
		
		List<Statement> bodyStatements = node.getBody().statements();
		//SampleHandler.printMessage("nodeeeee:" + node);
		for (Statement st : bodyStatements) {
			catchBlockLOCStatements.add(st.toString());
			catchBlockLOC++;
		}
		
		if(isEmptyException(node)) {
			emptyCatches.add(node);
		}
		
		return super.visit(node);
	}
	
	private boolean isfirstPatternException(CatchClause node) {
		int throwCounter = 0;
		int logCounter = 0;
		
		List<Statement> blockStatements = node.getBody().statements();
		
		for(Statement statement : blockStatements) {
			if(statement.toString().contains("throw")) {
				throwCounter++;
			}
			if(statement.toString().contains("log")) {
				flowHandlingStatements.add(statement.toString() + ", Action:'Log'");
				logCounter++;
			}
			if(throwCounter>0 && logCounter>0) {
				return true;
			}
		}
		
		
		return false;
	}

	public static HashSet<CatchClause> getCatchBlocks(){
		return catchClauses;
	}
	
	public int getCatchBlockCount() {
		return catchBlockCount;
	}
	
	public HashSet<CatchClause> getThrowStatements() {
		return throwStatements;
	}
	
	public ArrayList<String> getActionStatements() {
		return flowHandlingStatements;
	}
	
	public ArrayList<String> getCatchBlockLOCStatements(){
		return catchBlockLOCStatements;
	}
	
	public int getTryBlockLOC() {
		return catchBlockLOC;
	}
	
	public HashSet<CatchClause> getEmptyCatches() {
		return emptyCatches;
	}
	
	private boolean isEmptyException(CatchClause node) {
			
			return node.getBody().statements().isEmpty();
	}
}
