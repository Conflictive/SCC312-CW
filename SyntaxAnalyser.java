import java.io.*;

public class SyntaxAnalyser extends AbstractSyntaxAnalyser {
    
    public SyntaxAnalyser(String filename) throws IOException {
        lex = new LexicalAnalyser(filename);
    }
    
    @Override
    public void _statementPart_() throws IOException, CompilationException {
        try {
            // Implement parsing for <StatementPart> here
            myGenerate.commenceNonterminal("StatementPart");
            
            acceptTerminal(Token.beginSymbol);
            _statementList_();
            acceptTerminal(Token.endSymbol);
            
            myGenerate.finishNonterminal("StatementPart");
        } catch (CompilationException e) {
            throw new CompilationException("Error in StatementPart", e);
        }
    }
    
    // Add methods for each non-terminal in the grammar
    private void _statementList_() throws IOException, CompilationException {
        try {
            myGenerate.commenceNonterminal("StatementList");
            
            _statement_();
            
            // If we have a semicolon followed by another statement
            while (nextToken.symbol == Token.semicolonSymbol) {
                acceptTerminal(Token.semicolonSymbol);
                _statement_();
            }
            
            myGenerate.finishNonterminal("StatementList");
        } catch (CompilationException e) {
            throw new CompilationException("Error in StatementList", e);
        }
    }

    private void _statement_() throws IOException, CompilationException {
        try {
            myGenerate.commenceNonterminal("Statement");
            
            // Determine what type of statement to parse based on the current token
            switch (nextToken.symbol) {
                case Token.identifier:
                    // If the next token is an identifier, it must be an assignment statement
                    // (since this is the only statement type that starts with an identifier)
                    _assignmentStatement_();
                    break;
                    
                case Token.ifSymbol:
                    // If the next token is "if", it must be an if statement
                    _ifStatement_();
                    break;
                    
                case Token.whileSymbol:
                    // If the next token is "while", it must be a while statement
                    _whileStatement_();
                    break;
                    
                case Token.callSymbol:
                    // If the next token is "call", it must be a procedure statement
                    _procedureStatement_();
                    break;
                    
                case Token.doSymbol:
                    // If the next token is "do", it must be an until statement
                    _untilStatement_();
                    break;
                    
                case Token.forSymbol:
                    // If the next token is "for", it must be a for statement
                    _forStatement_();
                    break;
                    
                default:
                    // If none of the above, we've encountered an error
                    myGenerate.reportError(nextToken, 
                        "Expected the start of a statement (identifier, if, while, call, do, for) but found " + 
                        Token.getName(nextToken.symbol));
                    break;
            }
            
            myGenerate.finishNonterminal("Statement");
        } catch (CompilationException e) {
            throw new CompilationException("Error in Statement", e);
        }
    }

    private void _assignmentStatement_() throws IOException, CompilationException {
        try {
            myGenerate.commenceNonterminal("AssignmentStatement");
            
            // First, expect an identifier
            acceptTerminal(Token.identifier);
            
            // Then expect the := symbol
            acceptTerminal(Token.becomesSymbol);
            
            // Then parse either an expression or a string constant
            if (nextToken.symbol == Token.stringConstant) {
                acceptTerminal(Token.stringConstant);
            } else {
                _expression_();
            }
            
            myGenerate.finishNonterminal("AssignmentStatement");
        } catch (CompilationException e) {
            throw new CompilationException("Error in AssignmentStatement", e);
        }
    }

    private void _ifStatement_() throws IOException, CompilationException {
        try {
            myGenerate.commenceNonterminal("IfStatement");
    
            // Expect 'if' symbol
            acceptTerminal(Token.ifSymbol);
            
            // Parse the condition
            _condition_();
            
            // Expect 'then' symbol
            acceptTerminal(Token.thenSymbol);
            
            // Parse the 'then' statement list
            _statementList_();
            
            // Check if there's an 'else' part
            if (nextToken.symbol == Token.elseSymbol) {
                acceptTerminal(Token.elseSymbol);
                
                // Parse the 'else' statement list
                _statementList_();
            }
            
            // Expect 'end' symbol
            acceptTerminal(Token.endSymbol);
            
            // Expect 'if' symbol
            acceptTerminal(Token.ifSymbol);
            
            myGenerate.finishNonterminal("IfStatement");
        } catch (CompilationException e) {
            throw new CompilationException("Error in IfStatement", e);
        }
    }

    private void _whileStatement_() throws IOException, CompilationException {
        try {
            myGenerate.commenceNonterminal("WhileStatement");
            
            // Expect 'while' symbol
            acceptTerminal(Token.whileSymbol);
            
            // Parse the condition
            _condition_();
            
            // Expect 'loop' symbol
            acceptTerminal(Token.loopSymbol);
            
            // Parse the statement list
            _statementList_();
            
            // Expect 'end' symbol
            acceptTerminal(Token.endSymbol);
            
            // Expect 'loop' symbol
            acceptTerminal(Token.loopSymbol);
            
            myGenerate.finishNonterminal("WhileStatement");
        } catch (CompilationException e) {
            throw new CompilationException("Error in WhileStatement", e);
        }
    }

    private void _procedureStatement_() throws IOException, CompilationException {
        try {
            myGenerate.commenceNonterminal("ProcedureStatement");
            
            // Expect 'call' symbol
            acceptTerminal(Token.callSymbol);
            
            // Expect an identifier (procedure name)
            acceptTerminal(Token.identifier);
            
            // Expect '(' symbol
            acceptTerminal(Token.leftParenthesis);
            
            // Parse the argument list (required according to the grammar)
            _argumentList_();
            
            // Expect ')' symbol
            acceptTerminal(Token.rightParenthesis);
            
            myGenerate.finishNonterminal("ProcedureStatement");
        } catch (CompilationException e) {
            throw new CompilationException("Error in ProcedureStatement", e);
        }
    }

    private void _untilStatement_() throws IOException, CompilationException {
        try {
            myGenerate.commenceNonterminal("UntilStatement");
            
            // Expect 'do' symbol
            acceptTerminal(Token.doSymbol);
            
            // Parse the statement list
            _statementList_();
            
            // Expect 'until' symbol
            acceptTerminal(Token.untilSymbol);
            
            // Parse the condition
            _condition_();
            
            myGenerate.finishNonterminal("UntilStatement");
        } catch (CompilationException e) {
            throw new CompilationException("Error in UntilStatement", e);
        }
    }


    private void _forStatement_() throws IOException, CompilationException {
        try {
            myGenerate.commenceNonterminal("ForStatement");
            
            // Expect the 'for' symbol
            acceptTerminal(Token.forSymbol);

            // Expect '(' symbol
            acceptTerminal(Token.leftParenthesis);

            // Parse the initialization part of the for statement
            _assignmentStatement_();
            
            // Expect the ';' symbol
            acceptTerminal(Token.semicolonSymbol);

            // Parse the condition part of the for statement
            _condition_();

            // Expect the ';' symbol
            acceptTerminal(Token.semicolonSymbol);

            // Parse the increment part of the for statement
            _assignmentStatement_();

            // Expect the ')' symbol
            acceptTerminal(Token.rightParenthesis);

            // Expect the 'do' symbol
            acceptTerminal(Token.doSymbol);
            
            // Parse the statement part of the for statement
            _statementList_();

            // Expect the 'end' symbol
            acceptTerminal(Token.endSymbol);

            // Expect the 'loop' symbol
            acceptTerminal(Token.loopSymbol);
            
            myGenerate.finishNonterminal("ForStatement");
        } catch (CompilationException e) {
            throw new CompilationException("Error in ForStatement", e);
        }
    }

    private void _argumentList_() throws IOException, CompilationException {
        try {
            myGenerate.commenceNonterminal("ArgumentList");
            
            // First, expect an identifier (this is always the first part)
            acceptTerminal(Token.identifier);
            
            // Then, look for zero or more occurrences of ", identifier"
            while (nextToken.symbol == Token.commaSymbol) {
                // Accept the comma
                acceptTerminal(Token.commaSymbol);
                
                // Expect another identifier
                acceptTerminal(Token.identifier);
                
                // The loop will continue if there's another comma
            }
            
            myGenerate.finishNonterminal("ArgumentList");
        } catch (CompilationException e) {
            throw new CompilationException("Error in ArgumentList", e);
        }
    }

    private void _condition_() throws IOException, CompilationException {
        try {
            myGenerate.commenceNonterminal("Condition");
            
            // Parse the first identifier
            acceptTerminal(Token.identifier);
            
            // Parse the conditional operator
            _conditionalOperator_();
            
            // Parse the second operand
            switch (nextToken.symbol) {
                case Token.identifier:
                    acceptTerminal(Token.identifier);
                    break;
                case Token.numberConstant:
                    acceptTerminal(Token.numberConstant);
                    break;
                case Token.stringConstant:
                    acceptTerminal(Token.stringConstant);
                    break;
                default:
                    myGenerate.reportError(nextToken, 
                        "Expected an identifier, number constant, or string constant but found " + 
                        Token.getName(nextToken.symbol));
                    break;
            }
            
            myGenerate.finishNonterminal("Condition");
        } catch (CompilationException e) {
            throw new CompilationException("Error in Condition", e);
        }
    }
    
    private void _conditionalOperator_() throws IOException, CompilationException {
        try {
            myGenerate.commenceNonterminal("ConditionalOperator");
            
            // Parse the comparison operator
            switch (nextToken.symbol) {
                case Token.equalSymbol:        // =
                case Token.notEqualSymbol:     // !=
                case Token.lessThanSymbol:     // 
                case Token.lessEqualSymbol:    // <=
                case Token.greaterThanSymbol:  // >
                case Token.greaterEqualSymbol: // >=
                    acceptTerminal(nextToken.symbol);
                    break;
                default:
                    myGenerate.reportError(nextToken, 
                        "Expected a comparison operator (=, !=, <, <=, >, >=) but found " + 
                        Token.getName(nextToken.symbol));
                    break;
            }
            
            myGenerate.finishNonterminal("ConditionalOperator");
        } catch (CompilationException e) {
            throw new CompilationException("Error in ConditionalOperator", e);
        }
    }

    private void _expression_() throws IOException, CompilationException {
        try {
            myGenerate.commenceNonterminal("Expression");
            
            // First, parse a Term (this is always the first part of an Expression)
            _term_();
            
            // Then, look for zero or more occurrences of (+|-) <Term>
            while (nextToken.symbol == Token.plusSymbol || 
                   nextToken.symbol == Token.minusSymbol) {
                
                // Accept the operator (+ or -)
                acceptTerminal(nextToken.symbol);
                
                // Parse another Term
                _term_();
                
                // The loop will continue if there's another + or - token
            }
            
            myGenerate.finishNonterminal("Expression");
        } catch (CompilationException e) {
            throw new CompilationException("Error in Expression", e);
        }
    }

    private void _term_() throws IOException, CompilationException {
        try {
            myGenerate.commenceNonterminal("Term");
            
            // First, parse a Factor (this is always the first part of a Term)
            _factor_();
            
            // Then, look for zero or more occurrences of (*|/|%) <Factor>
            while (nextToken.symbol == Token.timesSymbol || 
                   nextToken.symbol == Token.divideSymbol || 
                   nextToken.symbol == Token.modSymbol) {
                
                // Accept the operator (*, /, or %)
                acceptTerminal(nextToken.symbol);
                
                // Parse another Factor
                _factor_();
                
                // The loop will continue if there's another *, /, or % token
            }
            
            myGenerate.finishNonterminal("Term");
        } catch (CompilationException e) {
            throw new CompilationException("Error in Term", e);
        }
    }
    
    private void _term_() throws IOException, CompilationException {
        try {
            myGenerate.commenceNonterminal("Term");
            
            // First, parse a Factor (this is always the first part of a Term)
            _factor_();
            
            // Then, look for zero or more occurrences of (*|/|%) <Factor>
            while (nextToken.symbol == Token.timesSymbol || 
                   nextToken.symbol == Token.divideSymbol || 
                   nextToken.symbol == Token.modSymbol) {
                
                // Accept the operator (*, /, or %)
                acceptTerminal(nextToken.symbol);
                
                // Parse another Factor
                _factor_();
                
                // The loop will continue if there's another *, /, or % token
            }
            
            myGenerate.finishNonterminal("Term");
        } catch (CompilationException e) {
            throw new CompilationException("Error in Term", e);
        }
    }
    
    private void _factor_ () throws IOException, CompilationException {
        try {
            myGenerate.commenceNonterminal("Factor");
            
            // Determine what type of factor to parse based on the current token
            switch (nextToken.symbol) {
                case Token.identifier:
                    // If the next token is an identifier, it must be a variable
                    acceptTerminal(Token.identifier);
                    break;
                    
                case Token.numberConstant:
                    // If the next token is a number constant, it must be a number
                    acceptTerminal(Token.numberConstant);
                    break;
     
                case Token.leftParenthesis:
                    // If the next token is a left parenthesis, it must be an expression
                    acceptTerminal(Token.leftParenthesis);
                    _expression_();
                    acceptTerminal(Token.rightParenthesis);
                    break;
                    
                default:
                    // If none of the above, we've encountered an error
                    myGenerate.reportError(nextToken, 
                        "Expected an identifier, number constant, string constant, or expression but found " + 
                        Token.getName(nextToken.symbol));
                    break;
            }
            
            myGenerate.finishNonterminal("Factor");
        } catch (CompilationException e) {
            throw new CompilationException("Error in Factor", e);
        }
    }
    
    @Override
    public void acceptTerminal(int symbol) throws IOException, CompilationException {
        if (nextToken.symbol == symbol) {
            myGenerate.insertTerminal(nextToken);
            nextToken = lex.getNextToken();
        } else {
            myGenerate.reportError(nextToken, "Expected " + Token.getName(symbol) + 
                                  " but found " + Token.getName(nextToken.symbol));
        }
    }
}