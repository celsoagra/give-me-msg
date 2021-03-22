package io.gmm.dto;

import javax.validation.constraints.NotBlank;

import io.gmm.entity.Message;

public class MessageDTO {

	@NotBlank(message="key may not be blank")
	public String key;
	
	@NotBlank(message="value may not be blank")
    public String value;

    public MessageDTO(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public MessageDTO() {
    }
	
	public Message entity() {
		return new Message(key, value);
	}
	
	public static MessageDTO dto(Message messageEntity) {
		return new MessageDTO(messageEntity.key, messageEntity.value);
	}
	
}
