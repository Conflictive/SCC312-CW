public class Generate extends AbstractGenerate {
    /**
     * Reports an error with the given token and explanatory message.
     *
     * @param token the token where the error occurred
     * @param explanatoryMessage the message explaining the error
     * @throws CompilationException if a compilation error occurs
     */
    @Override
    public void reportError(Token token, String explanatoryMessage) throws CompilationException {
        System.out.println("312ERROR at line " + token.lineNumber + ": " + explanatoryMessage);
        System.out.println(String.format("312TOKEN %s%s on line %d", 
                           Token.getName(token.symbol), 
                           (token.symbol == Token.identifier || token.symbol == Token.numberConstant || 
                            token.symbol == Token.stringConstant ? " '" + token.text + "'" : ""), 
                           token.lineNumber));
        throw new CompilationException("Error at token " + Token.getName(token.symbol) + 
                                      (token.symbol == Token.identifier || token.symbol == Token.numberConstant || 
                                       token.symbol == Token.stringConstant ? " '" + token.text + "'" : "") + 
                                      " on line " + token.lineNumber + ": " + explanatoryMessage);
    }
}