package tutorial691online.visitors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.LineComment;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.CompilationUnit;

import tutorial691online.handlers.SampleHandler;

public class CommentVisitor extends ASTVisitor  {
    List<String> lineComments = new ArrayList<String>();
    List<String> blockComments = new ArrayList<String>();
    CompilationUnit compilationUnit;
    private String[] source;
    
    public CommentVisitor(CompilationUnit compilationUnit, String[] source) {
    	super();
        this.compilationUnit = compilationUnit;
        this.source = source;
    }
    
    @Override
    public boolean visit(LineComment node) {
        int startLineNumber = compilationUnit.getLineNumber(node.getStartPosition()) - 1;
        String lineComment = source[startLineNumber].trim();
        
        lineComments.add(lineComment);
        
        //SampleHandler.printMessage("2222222:" + lineComments.toString());
        return super.visit(node);
    }

    @Override
    public boolean visit(BlockComment node) {
        int startLineNumber = compilationUnit.getLineNumber(node.getStartPosition()) - 1;
        int endLineNumber = compilationUnit.getLineNumber(node.getStartPosition() + node.getLength()) - 1;
        StringBuffer blockComment = new StringBuffer();

        for (int lineCount = startLineNumber ; lineCount<= endLineNumber; lineCount++) {

            String blockCommentLine = source[lineCount].trim();
            blockComment.append(blockCommentLine);
            if (lineCount != endLineNumber) {
                blockComment.append("\n");
            }
        }
        blockComments.add(blockComment.toString());
        
        //SampleHandler.printMessage("333333:" + blockComments.toString());
        return super.visit(node);
    }

    public List<String> getLineComments() {
            return lineComments;
    }

    public List<String> getBlockComments() {
            return blockComments;
    }
    
}
