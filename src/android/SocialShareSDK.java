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
                //没有办法通过解析Query获得正确的中文，只有直接截取字符串了。
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
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle(ShareTitle);
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl(ShareUrl);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(ShareTitle);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath(ShareImg);//确保SDcard下面存在此张图片
        oks.setImageUrl(ShareImg);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(ShareUrl);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        //oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(ShareTitle);
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(ShareUrl);

        //自定义微信回调
        //oks.setShareContentCustomizeCallback(new WeChartShareCallBack());

        // 启动分享GUI
        oks.show(context);
    }

}
