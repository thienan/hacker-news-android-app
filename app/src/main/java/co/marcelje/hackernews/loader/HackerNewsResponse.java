package co.marcelje.hackernews.loader;

public class HackerNewsResponse<T> {

    private T response;
    private String errorMessage;

    public static <T> HackerNewsResponse ok(T response) {
        return new HackerNewsResponse(response);
    }

    public static HackerNewsResponse error(String errorMessage) {
        return new HackerNewsResponse(errorMessage);
    }

    private HackerNewsResponse(T response) {
        this.response = response;
    }

    private HackerNewsResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isSuccessful() {
        return response != null;
    }

    public T getData() {
        return response;
    }

    @SuppressWarnings("unused")
    public String getErrorMessage() {
        return errorMessage;
    }
}
