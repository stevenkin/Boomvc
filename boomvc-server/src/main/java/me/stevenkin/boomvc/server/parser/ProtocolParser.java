package me.stevenkin.boomvc.server.parser;

import me.stevenkin.boomvc.server.exception.ProtocolParserException;

public interface ProtocolParser {

    void parser() throws ProtocolParserException;

    boolean parsed();

}
