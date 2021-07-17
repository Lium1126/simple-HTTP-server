/**
 * HTTPヘッダの解析をしてレスポンスを返す
 * @version 1.0
 */

package jp.ac.ait.k19061;

import java.io.*;
import java.net.Socket;

public class Processor extends Thread {

    /*---フィールド---*/
    /**
     * 受信用ソケット
     */
    java.net.Socket socket;

    /**
     * レスポンスするhtml文書
     */
    private final String respHtml =
            "HTTP/1.0 200 OK\n" +
            "Content-Type: text/html\n" +
            "\n" +
            "<!DOCTYPE html>\n" +
            "<html lang=\"ja\">\n" +
            "<head>\n" +
            "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
            "  <meta charset=\"UTF-8\">\n" +
            "  <title>SimpleHTTPServer</title>\n" +
            "  <link rel=\"stylesheet\" href=\"style.css\">\n" +
            "</head>\n" +
            "<body>\n" +
            "  <main>\n" +
            "    <h1>このページはSimpleHTTPServerより生成されて返されています。</h1>\n" +
            "    <p><button class=\"fire\">Push!!</button></p>\n" +
            "    <p class=\"copyright\">K19061 - 鈴木誉写</p>\n" +
            "  </main>\n" +
            "  <script src=\"script.js\"></script>\n" +
            "</body>\n" +
            "</html>";

    /**
     * レスポンスするcssデータ
     */
    private final String respCss =
            "HTTP/1.0 200 OK\n" +
            "Content-Type: text/css\n" +
            "\n" +
            "* {\n" +
            "  margin: 0;\n" +
            "  padding: 0;\n" +
            "  box-sizing: border-box;\n" +
            "}\n" +
            "body {\n" +
            "  height: 100vh;\n" +
            "  display: flex;\n" +
            "  justify-content: center;\n" +
            "  align-items: center;\n" +
            "}\n" +
            "main {\n" +
            "  height: 450px;\n" +
            "  max-height: 90vh;\n" +
            "  width: 800px;\n" +
            "  max-width: 90vw;\n" +
            "  border-radius: 10px;\n" +
            "  box-shadow: rgba(0, 0, 0, 0.1) 0px 20px 60px -10px;\n" +
            "  display: flex;\n" +
            "  justify-content: center;\n" +
            "  align-items: center;\n" +
            "  flex-direction: column;\n" +
            "}\n" +
            "h1 {\n" +
            "  padding: 0 3em;\n" +
            "  margin-bottom: 2em;\n" +
            "  text-align: center;\n" +
            "}\n" +
            "button {\n" +
            "  font-size: 1.25em;\n" +
            "  padding: 0.5em 1em;\n" +
            "}\n" +
            ".copyright {\n" +
            "  margin-top: 20px;\n" +
            "  text-decoration: underline;\n" +
            "  font-style: italic;\n" +
            "}";

    /**
     * レスポンスするjsスクリプト
     */
    private final String respJs =
            "HTTP/1.0 200 OK\n" +
            "Content-Type: text/javascript\n" +
            "\n" +
            "var btn = document.querySelector('button.fire');\n" +
            "btn.addEventListener('click', function() {\n" +
            "  alert('Hello, SimpleHTTPServer!!');\n" +
            "});";

    /*---コンストラクタ---*/
    /**
     * ソケットを指定するコンストラクタ
     * @param s 生成するソケット
     */
    public Processor(Socket s) {
        socket = s;
    }

    /**
     * HTTPヘッダを解析して規定のデータを返す
     * スレッドのメインメソッド
     */
    @Override
    public void run() {
        // in:ソケットからの情報取得用reader
        // out:ソケットへのレスポンスを書き込むwriter
        try (
                // ユーザからの入力ストリームの生成
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // クライアントへのソケット通信出力を送信するストリームの生成
                PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true)) {

            // HTTPヘッダを取得
            String httpHeader = in.readLine();
            System.out.println(httpHeader);

            /*---以降httpHeaderに対して解析---*/
            // 空白でHTTPヘッダを分割
            String[] headerArgs = httpHeader.split(" ", 0);

            // headerArgs[1]:リクエストパス
            // リクエストパスの拡張子を解析し、レスポンスデータをソケット通信に出力する
            if (headerArgs[1].lastIndexOf(".") == -1) {   // 拡張子なし
                out.println(respHtml);
            } else {                                          // 拡張子あり
                String extension = headerArgs[1].substring(headerArgs[1].lastIndexOf("."));
                if (extension.equals(".css")) {
                    out.println(respCss);
                } else if (extension.equals(".js")) {
                    out.println(respJs);
                } else {
                    out.println(respHtml);
                }
            }

            // 一回の通信ごとにソケットを閉じる(ステートレス)
            socket.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
