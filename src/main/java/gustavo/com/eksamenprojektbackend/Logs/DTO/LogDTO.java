package gustavo.com.eksamenprojektbackend.Logs.DTO;

import java.time.LocalDateTime;

public class LogDTO {
    private Integer userID;
    private Integer productID;
    private String action;
    private LocalDateTime timeStamp;

    public LogDTO() {
    }

    public LogDTO(Integer userID, Integer productID, String action, LocalDateTime timeStamp) {
        this.userID = userID;
        this.productID = productID;
        this.action = action;
        this.timeStamp = timeStamp;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}
