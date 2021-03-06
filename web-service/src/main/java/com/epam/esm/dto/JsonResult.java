package com.epam.esm.dto;

import com.epam.esm.model.Model;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * A class containing the result of operation.
 * Serve for wrap result for transmission to the client.
 *
 * @param <T> - type of model, containing in the result
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class JsonResult<T extends Model> {

    @JsonInclude(JsonInclude.Include.ALWAYS)
    private boolean success;

    private String errorCode;

    private String message;

    private List<T> result;

    private PageMetadata pageMetadata;

    private JsonResult() {
    }

    public PageMetadata getMetadata() {
        return pageMetadata;
    }

    public void setMetadata(PageMetadata pageMetadata) {
        this.pageMetadata = pageMetadata;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class Builder<T extends Model> {
        private final JsonResult<T> jsonResult;

        public Builder() {
            jsonResult = new JsonResult<>();
        }

        public Builder<T> withSuccess(boolean success) {
            jsonResult.success = success;
            return this;
        }

        public Builder<T> withErrorCode(String status) {
            jsonResult.errorCode = status;
            return this;
        }

        public Builder<T> withMessage(String message) {
            jsonResult.message = message;
            return this;
        }

        public Builder<T> withResult(List<T> result) {
            jsonResult.result = result;
            return this;
        }

        public Builder<T> withMetadata(PageMetadata pageMetadata){
            jsonResult.pageMetadata = pageMetadata;
            return this;
        }

        public JsonResult<T> build() {
            return jsonResult;
        }
    }
}
