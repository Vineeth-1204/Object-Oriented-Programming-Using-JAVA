import java.io.*;
import java.net.*;
import org.json.JSONObject;

public class HTTPClient {

    private static final String BASE_URL = "http://127.0.0.1:8000";

    // -------------------------
    // GET REQUEST
    // -------------------------
    public static JSONObject get(String route) throws Exception {
        URL url = new URL(BASE_URL + route);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        InputStream is = (responseCode >= 200 && responseCode < 300)
                ? con.getInputStream()
                : con.getErrorStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();

        return new JSONObject(sb.toString());
    }

    // -------------------------
    // POST JSON (FOR /edit_user, /create_user)
    // -------------------------
    public static JSONObject postJSON(String route, JSONObject payload) throws IOException {
        URL url = new URL(BASE_URL + route);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        // write json body
        try (OutputStream os = conn.getOutputStream()) {
            os.write(payload.toString().getBytes());
            os.flush();
        }

        int responseCode = conn.getResponseCode();
        InputStream is = (responseCode >= 200 && responseCode < 300)
                ? conn.getInputStream()
                : conn.getErrorStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder resp = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            resp.append(line);
        }
        br.close();

        return new JSONObject(resp.toString());
    }

    // -------------------------
    // OPTIONAL: DELETE REQUEST (for future /delete_user)
    // -------------------------
    public static JSONObject delete(String route) throws IOException {
        URL url = new URL(BASE_URL + route);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");

        int responseCode = conn.getResponseCode();
        InputStream is = (responseCode >= 200 && responseCode < 300)
                ? conn.getInputStream()
                : conn.getErrorStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder resp = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            resp.append(line);
        }
        br.close();

        return new JSONObject(resp.toString());
    }
}
