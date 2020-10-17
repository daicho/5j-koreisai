import java.io.*;
import java.net.*;
import java.time.*;
import java.time.format.*;
import http.requests.*;

private static int rank(int quality) {
  return quality > 14 ? 14 : quality;
}

private static String rankName(int quality) {
  switch (rank(quality)) {
    case 0:  return "草・並";
    case 1:  return "草・上";
    case 2:  return "草・特上";
    case 3:  return "梅・並";
    case 4:  return "梅・上";
    case 5:  return "梅・特上";
    case 6:  return "竹・並";
    case 7:  return "竹・上";
    case 8:  return "竹・特上";
    case 9:  return "松・並";
    case 10: return "松・上";
    case 11: return "松・特上";
    case 12: return "桜・並";
    case 13: return "桜・上";
    case 14: return "桜・特上";
    default: return "虚無";
  }
}

private LinkedList<Record> databaseLoad() {
  final String data = databaseQuery("SELECT name, score FROM ranking ORDER BY score DESC LIMIT 10;");
  if (data == null) {
    return null;
  }
  
  final LinkedList<Record> result = new LinkedList<Record>();
  if (data.isEmpty()) {
    return result;
  }
  
  final String[] records = data.split("\n");
  for (String record : records) {
    final String[] cols = record.split(",");
    result.addLast(new Record(cols[0], Integer.parseInt(cols[1])));
  }
  
  return result;
}

private void databaseAdd(String name, int score) {
  LocalDateTime ldt = LocalDateTime.now();
  String datetime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(ldt);
  databaseQuery("INSERT INTO ranking VALUES" + "('" + name + "', " + score + ", '" + datetime + "');");
}

private String databaseQuery(String sql) {
  if (!databaseCanConnect()) {
    return null;
  }
  
  try {
    PostRequest post = new PostRequest(DB_URL);
    post.addData("query", URLEncoder.encode(sql, "UTF-8"));
    post.send();
    return post.getContent();
  } catch (Exception e) {
    return null;
  }
}

private boolean databaseCanConnect() {
  try {
    new URL(DB_URL).openConnection().getInputStream();
  } catch (Exception e) {
    return false;
  }
  
  return true;
}

private static final int CM_PER_UNIT = 10;
private static final String DB_NAME = "unagi";
private static final String DB_URL = "https://nitnc5j.sakura.ne.jp/" + DB_NAME + "/mysql/query.php";
