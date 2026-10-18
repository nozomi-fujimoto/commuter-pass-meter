package com.example.commuterpassmeter.commuterpass;

import java.util.List;

/**
 * APIエラー時に返す共通レスポンスです。
 */
public record ApiErrorResponse(String message, List<String> errors) {
}
