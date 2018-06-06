import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServiceSocket {
    public static void main(String[] args) {
        startService();
    }
    private static  void startService(){
        try {
            ServerSocket serverSocket   = new ServerSocket(8080);
            while (true){
                Socket socket = serverSocket.accept();
                OutputStream os=socket.getOutputStream();
                os.write("您好，您收到了服务器的新年祝福！\n".getBytes("utf-8"));
//                startReader(socket);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 从参数的Socket里获取最新的消息
     */
    private static void startReader(final Socket socket) {

        new Thread(){
            @Override
            public void run() {
                System.out.println("*111*");
                DataInputStream reader;
                try {
                    // 获取读取流
                    reader = new DataInputStream(socket.getInputStream());
                    while (true) {
                        System.out.println("*等待客户端输入*");
                        // 读取数据
                        String msg = reader.readLine();
                        System.out.println("获取到客户端的信息：" + msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
