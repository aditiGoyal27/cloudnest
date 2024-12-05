package com.opensource.cloudnest.dto;

import com.opensource.cloudnest.dto.response.ResDTOMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResDTO<T> implements Serializable {
    private Boolean success;

    private ResDTOMessage message;

    private T payload;

    public ResDTO<Object> toResponse() {
        return new ResDTO<>(this.success, this.message, this.payload);
    }

    @SuppressWarnings("unchecked")
    public <R> ResDTO<R> castData() {
        return new ResDTO<>(this.success, this.message, (R) this.payload);
    }

    public boolean isSuccess() {
        return success;
    }
}