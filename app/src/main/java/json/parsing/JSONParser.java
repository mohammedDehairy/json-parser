package json.parsing;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import json.Exceptions.JSONMappingException;
import json.Exceptions.JSONParsingException;
import json.mapping.JSONMapper;
import json.tokenization.Token;
import json.tokenization.TokenType;
import json.tokenization.Tokenizer;
import json.types.JSONArray;
import json.types.JSONBoolean;
import json.types.JSONDictionary;
import json.types.JSONFloat;
import json.types.JSONInteger;
import json.types.JSONObject;
import json.types.JSONString;

public final class JSONParser {

    private Tokenizer tokenizer = new Tokenizer();
    private JSONMapper jsonMapper = new JSONMapper();

    public JSONObject<?> parse(InputStream inputStream) throws IOException, JSONParsingException {
        List<Token> tokens = tokenizer.tokenize(inputStream);

        return parse(tokens);
    }

    public <T> T parse(Class<T> clazz, InputStream inputStream) throws IOException, JSONMappingException, JSONParsingException {
        List<Token> tokens = tokenizer.tokenize(inputStream);
        JSONObject<?> jsonObject = parse(tokens);

        return jsonMapper.map(clazz, jsonObject);
    }

    private JSONObject<?> parse(List<Token> tokens) throws JSONParsingException {
        if (tokens.isEmpty()) {
            return new JSONDictionary();
        }
        State state = new State();
        Token firsToken = tokens.get(state.getIndex());

        switch (firsToken.getType()) {
            case OPEN_CURLY_BRAKET:
                return parseDictionary(tokens, state);
            case OPEN_SQUARE_BRAEKT:
                return parseArray(tokens, state);
            default:
                throw new JSONParsingException("Invalid json should start with either [ or {");
        }
    }

    private JSONDictionary parseDictionary(List<Token> tokens, State state) throws JSONParsingException {
        JSONDictionary result = new JSONDictionary();
        state.incrementIndex();
        for (;state.getIndex() < tokens.size();) {

            Token key = tokens.get(state.getIndex());

            if (key.getType() != TokenType.STRING) {
                throw new JSONParsingException("Unexpected token " + key);
            }

            state.incrementIndex();

            Token colon = tokens.get(state.getIndex());

            if (colon.getType() != TokenType.COLON) {
                throw new JSONParsingException("Unexpected token " + colon);
            }

            state.incrementIndex();

            Token value = tokens.get(state.getIndex());

            switch (value.getType()) {
                case INTEGER:
                    result.put(key.getValue(), new JSONInteger(Integer.parseInt(value.getValue())));
                    break;
                case FLOATING_POINT:
                    result.put(key.getValue(), new JSONFloat(Float.parseFloat(value.getValue())));
                    break;
                case STRING:
                    result.put(key.getValue(), new JSONString(value.getValue()));
                    break;
                case BOOLEAN_FALSE:
                    result.put(key.getValue(), new JSONBoolean(false));
                    break;
                case BOOLEAN_TRUE:
                    result.put(key.getValue(), new JSONBoolean(true));
                    break;
                case OPEN_CURLY_BRAKET:
                    result.put(key.getValue(), parseDictionary(tokens, state));
                    break;
                case OPEN_SQUARE_BRAEKT:
                    result.put(key.getValue(), parseArray(tokens, state));
                    break;
            
                default:
                    throw new JSONParsingException("Unexpected token " + value);
            }
            
            state.incrementIndex();

            Token next = tokens.get(state.getIndex());

            switch (next.getType()) {
                case COMMA:
                    state.incrementIndex();
                    break;
                case CLOSED_CURLY_BRAKET:
                    return result;
                default:
                    throw new JSONParsingException("Unexpected token " + next);
            }
        }
        throw new JSONParsingException("Missing closing curly braket");
    }

    private JSONArray parseArray(List<Token> tokens, State state) throws JSONParsingException {
        JSONArray result = new JSONArray();
        state.incrementIndex();

        for (;state.getIndex() < tokens.size();) {
            Token value = tokens.get(state.getIndex());

            switch (value.getType()) {
                case INTEGER:
                    result.add(new JSONInteger(Integer.parseInt(value.getValue())));
                    break;
                case FLOATING_POINT:
                    result.add(new JSONFloat(Float.parseFloat(value.getValue())));
                    break;
                case STRING:
                    result.add(new JSONString(value.getValue()));
                    break;
                case OPEN_CURLY_BRAKET:
                    result.add(parseDictionary(tokens, state));
                    break;
                case OPEN_SQUARE_BRAEKT:
                    result.add(parseArray(tokens, state));
                    break;
            
                default:
                    throw new JSONParsingException("Unexpected token " + value);
            }

            state.incrementIndex();

            Token next = tokens.get(state.getIndex());

            switch (next.getType()) {
                case COMMA:
                    state.incrementIndex();
                    break;
                case CLOSED_SQUARE_BRAKET:
                    return result;
                default:
                    throw new JSONParsingException("Unexpected token " + next);
            }
        }

        throw new JSONParsingException("Missing closing square braket");
    }

    static class State {
        private int index = 0;

        public int getIndex() {
            return index;
        }

        public void incrementIndex() {
            this.index++;
        }
    }
}
