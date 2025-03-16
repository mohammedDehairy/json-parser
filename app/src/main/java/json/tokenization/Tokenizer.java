package json.tokenization;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public final class Tokenizer {

    private static final List<Token> constantTokens = List.of(
        new Token(TokenType.COLON, ":"),
        new Token(TokenType.CLOSED_SQUARE_BRAKET, "]"),
        new Token(TokenType.OPEN_SQUARE_BRAEKT, "["),
        new Token(TokenType.OPEN_CURLY_BRAKET, "{"),
        new Token(TokenType.CLOSED_CURLY_BRAKET, "}"),
        new Token(TokenType.COMMA, ","),
        new Token(TokenType.BOOLEAN_FALSE, "false"),
        new Token(TokenType.BOOLEAN_TRUE, "true"));

    public final List<Token> tokenize(InputStream inputStream) throws IOException {
        List<Token> result = new ArrayList<>();
        try(InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);) {
            bufferedReader.mark(1);
            int nextInt;
            while((nextInt = bufferedReader.read()) != -1) {
                char nextChar = (char)nextInt;

                if (Character.isWhitespace(nextChar)) {
                    bufferedReader.mark(1);
                    continue;
                }
    
                if (Character.isDigit(nextChar)) {
                    result.add(parseNumber(bufferedReader, nextChar));
                } else if (nextChar == '"') {
                    result.add(parseString(bufferedReader));
                } else {
                    bufferedReader.reset();
                    result.add(parseConstantToken(bufferedReader));
                }
                bufferedReader.mark(1);
            }
            return result;
            
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private Token parseNumber(BufferedReader bufferedReader, char currentChar) throws IOException {
        int nextInt;
        StringBuilder value = new StringBuilder();
        value.append(currentChar);

        while((nextInt = bufferedReader.read()) != -1 && (Character.isDigit((char)nextInt) || (char)nextInt == '.')) {
            bufferedReader.mark(1);
            value.append((char)nextInt);
        }

        bufferedReader.reset();

        String finalValue = value.toString();

        if (finalValue.isEmpty()) {
            throw new IllegalArgumentException("Invalid Number"); 
        }

        String[] parts = finalValue.split("\\.");

        if (parts.length == 2) {
            return new Token(TokenType.FLOATING_POINT, finalValue);
        } else if (parts.length == 1) {
            return new Token(TokenType.INTEGER, finalValue);
        }
        
        throw new IllegalArgumentException("Invalid Number");
    }

    private Token parseString(BufferedReader bufferedReader) throws IOException {
        int nextInt;
        StringBuilder value = new StringBuilder();

        while((nextInt = bufferedReader.read()) != -1 && (char)nextInt != '"') {
            value.append((char)nextInt);
        }

        if ((char)nextInt == '"') {
            return new Token(TokenType.STRING, value.toString());
        }

        throw new IllegalArgumentException("Invalid String");
    }

    private Token parseConstantToken(BufferedReader bufferedReader) throws IOException {
        if (!bufferedReader.markSupported()) {
            throw new IllegalArgumentException("matchNextToken expects a mark supported BufferedReader");
        }

        bufferedReader.mark(4);

        for (Token token: constantTokens) {
            char[] charArray = token.getValue().toCharArray();
            boolean equal = true;
            for (int i = 0; i < charArray.length; i++) {
                char nextReadChar = (char)bufferedReader.read();
                char currentChar = charArray[i];

                if (nextReadChar != currentChar) {
                    equal = false;
                    break;
                }
            }

            if (equal) {
                return token;
            } else {
                bufferedReader.reset();
            }
        }
        throw new IllegalArgumentException("Invalid Token");
    }
}
