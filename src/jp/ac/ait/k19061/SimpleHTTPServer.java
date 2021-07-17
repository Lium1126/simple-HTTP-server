/**
 * シンプルHTTPサーバーのメインクラス
 * @version 1.0
 */

package jp.ac.ait.k19061;

import java.net.ServerSocket;
import java.net.Socket;

public class SimpleHTTPServer {

    /**
     * @param args コマンドライン引数
     * @throws Exception IOException
     */
    public static void main(String[] args)  throws Exception {
        // ポート番号8088番でサーバソケットを生成(サーバ起動)
        try (ServerSocket ss = new ServerSocket(8088)) {

            // サーバ起動の旨を伝えるメッセージを出力
            System.out.println("Simple HTTP server is Running!");

            // クライアントからのリクエストを待つ
            while (true) {
                Socket s = ss.accept();

                // マルチスレッドでProcessorクラスを実行
                System.out.printf("クライアントからの接続: %s:%d\n", s.getInetAddress(), s.getPort());
                Processor app = new Processor(s);
                app.start();
            }
        }
    }
}
