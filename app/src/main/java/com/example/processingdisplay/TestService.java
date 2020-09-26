package com.example.processingdisplay;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.processingdisplay.ShapeClass.Circle;
import com.example.processingdisplay.ShapeClass.Line;
import com.example.processingdisplay.ShapeClass.Rectangle;
import com.example.processingdisplay.ShapeClass.Triangle;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentLinkedQueue;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;


public class TestService extends Service {
    private ConcurrentLinkedQueue<String> mStringConcurrentLinkedQueue = new ConcurrentLinkedQueue<>();
    private Socket socket;
    private TestBinder testBinder = new TestBinder();
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public class TestBinder extends Binder{
        private UserModel usermodel;
        private boolean isOpen = false;
        private GreetingClient greetingClient = new GreetingClient();
        Thread mThread;
        public void setisOpen(boolean i){
            this.isOpen = i;
        }
        public void receiveData(UserModel userm){
            this.usermodel = userm;
            transferUartData();
            }
        public void closeWifiThread() {
            if (socket != null) {
                try {
                    socket.close ();
                } catch (IOException e) {
                    e.printStackTrace ();
                }
            }
            greetingClient.setIsSend(false);
        }

        public void sendDataToWifi(){
            if(mThread==null){
                mThread=  new Thread(greetingClient);
                mThread.start();
            }

        }
        public boolean sendData(String writeString) {
            byte[] to_write = toByteArray(writeString);// 将输入框内的值转换为byte数组
            return (MyApp.driver.WriteData(to_write, to_write.length) < 0);//函数结果小于0表示执行失败，否则返回实际写入的字节数
        }

        private String toHexString (byte[] arg, int length,SharedPreferences sharedPreferences) {
            int count = sharedPreferences.getInt(getString(R.string.sdatacount),0);
            StringBuilder result = new StringBuilder();
            if (arg != null) {
                if(arg[0] ==(byte)0xff && arg[length-1] ==(byte)0xfe){
                    int tempcou = 0;
                    for(int i = 0;i<count;i++){
                        //此处的bit表示字节
                        int bit = sharedPreferences.getInt(getString(R.string.sbit)+i,0);
                        String type = sharedPreferences.getString(getString(R.string.stype)+i,"");
                        try{
                            switch (type){
                                case "int":
                                    int tempi = 0;
                                    int[] ch = new int[4];
                                    for (int j = 0; j <bit; j++) {
                                        if(arg[j + tempcou+1]<0){
                                            ch[j+4-bit] = arg[j + tempcou+1]+256;
                                        }
                                        else{
                                            ch[j+4-bit] = arg[j + tempcou+1];
                                        }
                                    }
                                    for(int j =0;j<4;j++){
                                        tempi+=ch[j]<<(8*(4-j-1));
                                    }
                                    result.append(tempi);
                                    result.append(" ");
                                    break;
                                case "float":
                                    int tempf = 0;
                                    int[] chf = new int[4];
                                    for (int j = 0; j <bit; j++) {
                                        if(arg[j + tempcou+1]<0){
                                            chf[j+4-bit] = arg[j + tempcou+1]+256;
                                        }
                                        else{
                                            chf[j+4-bit] = arg[j + tempcou+1];
                                        }
                                    }
                                    for(int j =0;j<4;j++){
                                        tempf+=chf[j]<<(8*(4-j-1));
                                    }
                                    float f = Float.intBitsToFloat(tempf);
                                    result.append(f);
                                    result.append(" ");
                                    break;
                                case "string":
                                    char[] ch1 = new char[bit];
                                    for(int j = 0;j<bit;j++)
                                    {
                                        ch1[j] = (char)arg[j+tempcou+1];
                                        System.out.println(ch1[j]);
                                    }
                                    String temp =  new String(ch1);
                                    result.append(temp);
                                    result.append(" ");
                                    break;
                                default:
                                    Log.d(TAG, "toHexString: 配置文件类型字段与实际数据不符");
                            }tempcou+=bit;
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    return result.toString();
                }else{
                    Log.d(TAG, "toHexString: your data is formatting error");
                }
            }
            return "null";
        }
        /**
         * 将String转化为byte数组
         * @param arg
         *            需要转换的String对象
         * @return 转换后的byte[]数组
         */
        private byte[] toByteArray(String arg) {
            if (arg != null) {
                /* 1.先去除String中的' '，然后将String转换为char数组 */
                char[] NewArray = new char[1000];
                char[] array = arg.toCharArray();
                int length = 0;
                for (int i = 0; i < array.length; i++) {
                    if (array[i] != ' ') {
                        NewArray[length] = array[i];
                        length++;
                    }
                }
                /* 将char数组中的值转成一个实际的十进制数组 */
                int EvenLength = (length % 2 == 0) ? length : length + 1;
                if (EvenLength != 0) {
                    int[] data = new int[EvenLength];
                    data[EvenLength - 1] = 0;
                    for (int i = 0; i < length; i++) {
                        if (NewArray[i] >= '0' && NewArray[i] <= '9') {
                            data[i] = NewArray[i] - '0';
                        } else if (NewArray[i] >= 'a' && NewArray[i] <= 'f') {
                            data[i] = NewArray[i] - 'a' + 10;
                        } else if (NewArray[i] >= 'A' && NewArray[i] <= 'F') {
                            data[i] = NewArray[i] - 'A' + 10;
                        }
                    }
                    /* 将 每个char的值每两个组成一个16进制数据 */
                    byte[] byteArray = new byte[EvenLength / 2];
                    for (int i = 0; i < EvenLength / 2; i++) {
                        byteArray[i] = (byte) (data[i * 2] * 16 + data[i * 2 + 1]);
                    }
                    return byteArray;
                }
            }
            return new byte[] {};
        }
        private void transferUartData(){
            new Thread(){
                @Override
                public void run() {
                    super.run();

                    SharedPreferences sharedPreferences= getSharedPreferences("dataReceive", Context .MODE_PRIVATE);
                    int count=sharedPreferences.getInt(getString(R.string.sdatacount),0);
                    int sumCount = 0;
                    for(int i = 0;i<count;i++){
                        sumCount+=sharedPreferences.getInt(getString(R.string.sbit)+i,0);
                    }
                    byte[] recv_buff = new byte[sumCount+2];
                    while (isOpen){
                        int recv_len = MyApp.driver.ReadData(recv_buff, sumCount+2);

                        if(recv_len>0) {
                            Log.d(TAG, "run: recv_len = "+recv_len);
                            String read_string = toHexString(recv_buff, recv_len,sharedPreferences);
                            Log.d(TAG, "transferUartData: ==" + read_string);
                            usermodel.doDataAction(read_string);
                            mStringConcurrentLinkedQueue.add(read_string);

                            //Integer.valueOf("FFFF",16).toString();

                        }
                    }
                }
            }.start();
            }

        }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return testBinder;
    }
    public class GreetingClient implements Runnable {
        private String ip;
        private int port;
        private boolean isSend = false;
        public void setIsSend(boolean send){
            this.isSend = send;
        }
        private boolean isWIFIContent() {
            ConnectivityManager myManager = (ConnectivityManager) getSystemService ( Context.CONNECTIVITY_SERVICE );
            if (myManager != null) {
                Network myNetwork = myManager.getActiveNetwork();
                NetworkCapabilities networkCapabilities = myManager.getNetworkCapabilities(myNetwork);
                return (networkCapabilities != null);
            }

            return false;
        }
        @Override
        public void run()
        {
              SharedPreferences spSend = getSharedPreferences("dataSend", MODE_PRIVATE);
             String protocol = spSend.getString(getString(R.string.sagreement),"");
              ip = spSend.getString(getString(R.string.sip),"");
              port = Integer.parseInt(spSend.getString(getString(R.string.sport),""));

            switch (protocol){
                case "socket":
                    socketSend();
                    break;
                case "http":
                    httpSend(spSend);
                    break;
                default:
                    Toast.makeText(TestService.this,"请选择正确的发送协议",Toast.LENGTH_SHORT).show();
                        break;
            }
        }



        private void httpSend(SharedPreferences sp) {
            Data p2 = new Data();
            p2.setName("lucky");

            p2.save(new SaveListener<String>() {
                @Override
                public void done(String objectId, BmobException e) {
                    if(e==null){
                        Toast.makeText(TestService.this,
                                "添加数据成功，返回objectId为："+objectId,
                                Toast.LENGTH_SHORT).show();

                    }else{
                        Log.d(TAG, "done: "+e.getMessage());

                    }
                }
            });

            /*if(!mStringConcurrentLinkedQueue.isEmpty()){
                String a = mStringConcurrentLinkedQueue.poll();
                String[]datas = a.split(" ");
                String[]names = new String[datas.length];
                for(int i = 0;i<datas.length;i++){
                    names[i] = sp.getString(getString(R.string.sname)+i, "");
                }
                RequestBody requestBody;
                FormBody.Builder fb = new FormBody.Builder();
                for(int i = 0;i<datas.length;i++){
                    if(!names[i].equals("")){
                        fb.add(names[i],datas[i]);
                    }
                }
                requestBody = fb.build();
                Request request = new Request.Builder()
                        .url(ip)
                        .post(requestBody)
                        .build();
                OkHttpClient okHttpClient = new OkHttpClient();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.body() != null) {
                            Log.d(TAG, "onResponse: " + response.body().string());
                        }
                    }
                });
            }*/

        }

        public void socketSend(){
            if(isWIFIContent()){
                try {
                    socket = new Socket (ip,port);
                    socket.setSoTimeout ( 10000 ); //5秒超时
                    if (!socket.isClosed () && socket.isConnected ()) {
                        Log.d ( TAG, "SocketConnect: 连接成功" );
                        this.isSend = true;

                    }
                } catch (UnknownHostException e) {
                    String msg = "未找到服务器";
                    Log.e ( "UnknownHost", msg );
                    e.printStackTrace ();
                } catch (IOException e) {
                    String msg = "来自服务器的数据出错";
                    Log.e ( "IOException", e.toString());
                    e.printStackTrace ();
                }
            }else{
                String msg = "无网络连接";
                Log.e ( "IOException", msg );
            }
            while(socket.isConnected()&&isSend)
            {
                Log.d(TAG, "run: 进入发数据线程");
                if(!mStringConcurrentLinkedQueue.isEmpty()){
                    try{
                        Log.d(TAG, "connect to host:"+ip+"the port is:"+port);
                        Log.d(TAG,"远程主机地址："+socket.getRemoteSocketAddress());
                        OutputStream outToServer = socket.getOutputStream();
                        DataOutputStream out = new DataOutputStream(outToServer);
                        String a = mStringConcurrentLinkedQueue.poll();
                        Log.d(TAG, "run: send data is"+a);
                        out.writeUTF(a);
                        InputStream inFromServer = socket.getInputStream();
                        DataInputStream in = new DataInputStream(inFromServer);
                        Log.d(TAG,"服务器响应：" +in.readUTF());
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }

            }
            if(isSend&& !socket.isConnected ()){
                Toast.makeText(TestService.this,"socket isn't connect!",Toast.LENGTH_SHORT).show();
            }
        }

    }


}
