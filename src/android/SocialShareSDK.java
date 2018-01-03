package cordova.plugin.SocialShareSDK;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * This class echoes a string called from JavaScript.
 */
public class SocialShareSDK extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("coolMethod")) {
            String message = args.getString(0);
            this.coolMethod(message, callbackContext);
            return true;
        }
        return false;
    }

    private void coolMethod(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            try{
                String url= URLDecoder.decode(URLDecoder.decode(message,"UTF-8"),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            UrlQuerySanitizer Urlsanitizer=new UrlQuerySanitizer(url);
            String TheShareUrl= Urlsanitizer.getValue("ShareUrl");
            if(!TextUtils.isEmpty(TheShareUrl)){
                ShareUrl=TheShareUrl;
            }
            String TheShareImg=Urlsanitizer.getValue("ShareImg");
            if(!TextUtils.isEmpty(TheShareImg)){
                ShareImg=TheShareImg;
            }
            String TheShareTitle=Urlsanitizer.getValue("ShareTitle");
            if(!TextUtils.isEmpty(TheShareTitle)){
                //û�а취ͨ������Query�����ȷ�����ģ�ֻ��ֱ�ӽ�ȡ�ַ����ˡ�
                int BeginIndex=url.indexOf("ShareTitle");
                int EndIndex=url.indexOf("&",BeginIndex+1)==-1?url.length()-1:url.indexOf("&",BeginIndex+1);
                ShareTitle=url.substring(BeginIndex+11,url.indexOf("",EndIndex) );
            }    

            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //�ر�sso��Ȩ
        oks.disableSSOWhenAuthorize();

        // title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ�ռ��ʹ��
        oks.setTitle(ShareTitle);
        // titleUrl�Ǳ�����������ӣ�QQ��QQ�ռ��ʹ��
        oks.setTitleUrl(ShareUrl);
        // text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�
        oks.setText(ShareTitle);
        // imagePath��ͼƬ�ı���·����Linked-In�����ƽ̨��֧�ִ˲���
        //oks.setImagePath(ShareImg);//ȷ��SDcard������ڴ���ͼƬ
        oks.setImageUrl(ShareImg);
        // url����΢�ţ��������Ѻ�����Ȧ����ʹ��
        oks.setUrl(ShareUrl);
        // comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ��
        //oks.setComment("���ǲ��������ı�");
        // site�Ƿ�������ݵ���վ���ƣ�����QQ�ռ�ʹ��
        oks.setSite(ShareTitle);
        // siteUrl�Ƿ�������ݵ���վ��ַ������QQ�ռ�ʹ��
        oks.setSiteUrl(ShareUrl);

        //�Զ���΢�Żص�
        //oks.setShareContentCustomizeCallback(new WeChartShareCallBack());

        // ��������GUI
        oks.show(context);
    }

}
