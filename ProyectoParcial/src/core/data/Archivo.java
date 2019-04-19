package core.data;

public class Archivo {
    private String id;
    private String name;
    private String path;
    private String current;
    private String total;
    private String host;
    
    public Archivo(){
        
    }

    public Archivo(String id, String name, String path, String current, String total) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.current = current;
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getcurrent() {
        return current;
    }

    public void setcurrent(String current) {
        this.current = current;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String gettotal() {
        return total;
    }

    public void settotal(String total) {
        this.total = total;
    }

    public String getpath() {
        return path;
    }

    public void setpath(String path) {
        this.path = path;
    }   

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }
    
}
