package com.example.commuterpassmeter.commuterpass;

/**
 * 対象の定期券情報が見つからない場合の例外です。
 */
public class CommuterPassNotFoundException extends RuntimeException {
    public CommuterPassNotFoundException(String message) {
        super(message);
    }
}
