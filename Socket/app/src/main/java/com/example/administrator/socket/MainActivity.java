package com.example.administrator.socket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    /**
     * 主 变量
     */

    // 主线程Handler
    // 用于将从服务器获取的消息显示出来
    private Handler mMainHandler;

    // Socket变量
    private Socket socket;

    // 线程池
    // 为了方便展示,此处直接采用线程池进行线程管理,而没有一个个开线程
    private ExecutorService mThreadPool;

    /**
     * 接收服务器消息 变量
     */
    // 输入流对象
    InputStream is;

    // 输入流读取器对象
    InputStreamReader isr ;
    BufferedReader br ;

    // 接收服务器发送过来的消息
    String response;


    /**
     * 发送消息到服务器 变量
     */
    // 输出流对象
    OutputStream outputStream;

    /**
     * 按钮 变量
     */

    // 连接 断开连接 发送数据到服务器 的按钮变量
    private Button btnConnect, btnDisconnect, btnSend;

    // 显示接收服务器消息 按钮
    private TextView Receive,receive_message;

    // 输入需要发送的消息 输入框
    private EditText mEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 初始化所有按钮
        btnConnect = (Button) findViewById(R.id.connect);
        btnDisconnect = (Button) findViewById(R.id.disconnect);
        btnSend = (Button) findViewById(R.id.send);
        mEdit = (EditText) findViewById(R.id.edit);
        receive_message = (TextView) findViewById(R.id.receive_message);
        Receive = (Button) findViewById(R.id.Receive);

        mThreadPool = Executors.newCachedThreadPool();
        mMainHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        receive_message.setText(response);
                        break;
                }
            }
        };



        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        Looper.loop();
                        try {

                            // 创建Socket对象 & 指定服务端的IP 及 端口号
                            socket = new Socket("192.168.16.111", 8080);

                            // 判断客户端和服务器是否连接成功
                            System.out.println(socket.isConnected());
                            try {
                                is = socket.getInputStream();
                                isr = new InputStreamReader(is);
                                br = new BufferedReader(isr);
                                response = br.readLine();
                                if (!TextUtils.isEmpty(response)) {
                                    Message message = Message.obtain();
                                    message.what = 0;
                                    mMainHandler.sendMessage(message);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });


            }
        });
        Receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (true) {
                                is = socket.getInputStream();
                                isr = new InputStreamReader(is);
                                br = new BufferedReader(isr);
                                response = br.readLine();
                                if (!TextUtils.isEmpty(response)) {
                                    Message message = Message.obtain();
                                    message.what = 0;
                                    mMainHandler.sendMessage(message);
                                }

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });


            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            outputStream = socket.getOutputStream();
                            outputStream.write((mEdit.getText().toString()+"\n").getBytes("utf-8"));
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
        btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // 断开 客户端发送到服务器 的连接，即关闭输出流对象OutputStream
                    if (outputStream!=null)
                    outputStream.close();

                    // 断开 服务器发送到客户端 的连接，即关闭输入流读取器对象BufferedReader
                    if(br!=null)
                    br.close();

                    // 最终关闭整个Socket连接
                    if(socket!=null)
                    socket.close();

                    // 判断客户端和服务器是否已经断开连接
                    System.out.println(socket.isConnected());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
