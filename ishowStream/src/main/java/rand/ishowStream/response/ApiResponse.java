package rand.ishowStream.response;


import lombok.Data;

@Data
public class ApiResponse<T> {
    private T data;
    private Long totalCount;
    private String message;
    public String status;
    public String code;
    public int page;
    public int pageSize;


    public static <T> ApiResponse<T> success(T data, String code,
                                             Long totalCount, int page, int pageSize) {
        ApiResponse<T> response = new ApiResponse<>();
        response.status = ApiStatus.SUCCESS.toString();
        response.data = data;
        response.code = code;
        response.message = "Operation successful";
        response.totalCount = totalCount;
        response.page = page;
        response.pageSize = pageSize;
        return response;
    }

    public static <T> ApiResponse<T> success(T data, String code) {

        return success(data, code, 1L, 1, 1);
    }


    public static <T> ApiResponse<T> failure(String code, Object[] args) {
        ApiResponse<T> response = new ApiResponse<>();
        response.status = ApiStatus.FAILURE.toString();
        response.code = code;
        response.message ="Internal server error";
        response.page = 1;
        response.pageSize = 10;
        response.totalCount = 0L;
        return response;
    }
}
