package com.user.app.commons.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
	private String cause;

	private String message;
	private boolean success;

}
