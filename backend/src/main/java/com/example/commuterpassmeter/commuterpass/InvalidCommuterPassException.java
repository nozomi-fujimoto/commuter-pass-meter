package com.example.commuterpassmeter.commuterpass;

/**
 * 定期券情報の入力値または状態が不正な場合の例外です。
 */
public class InvalidCommuterPassException extends RuntimeException {
    public InvalidCommuterPassException(String message) {
        super(message);
    }
}
