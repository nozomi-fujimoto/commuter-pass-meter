package com.example.commuterpassmeter.commuterpass;

import java.util.List;

public record ApiErrorResponse(String message, List<String> errors) {
}
