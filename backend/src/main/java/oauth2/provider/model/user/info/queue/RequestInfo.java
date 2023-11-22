package oauth2.provider.model.user.info.queue;

public class RequestInfo {

    private String address;

    private long expire;

    private int times;

    public RequestInfo(String address, int times) {
        this.address = address;
        this.expire = System.currentTimeMillis();
        this.times = times;
    }

    public String getAddress() {
        return address;
    }

    public long getExpire() {
        return expire;
    }

    public int getTimes() {
        return times;
    }

    public void addTimes() {
        times++;
    }
}
