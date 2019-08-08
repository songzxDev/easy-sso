package cn.chenzw.easy.sso.server.entify;


import cn.chenzw.easy.sso.server.constants.SSOConstants;
import cn.chenzw.easy.sso.server.exception.SSOException;
import cn.chenzw.easy.sso.server.utils.SSOUtils;
import cn.chenzw.toolkit.codec.AESUtils;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenzw
 */
public class SSODefinition {

    public SSODefinition(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;

        this.init();
    }


    private void init() {
        try {
            this.source = ServletRequestUtils.getStringParameter(request, SSOConstants.SOURCE_IDENTIFIER);
            if (StringUtils.isBlank(source)) {
                throw new SSOException(SSOConstants.SOURCE_IDENTIFIER + "����Ϊ��!");
            }

            this.key = ServletRequestUtils.getStringParameter(request, SSOConstants.KEY_IDENTIFIER);
            if (StringUtils.isBlank(key)) {
                throw new SSOException(SSOConstants.KEY_IDENTIFIER + "����Ϊ��!");
            }

            this.userName = ServletRequestUtils.getStringParameter(request, SSOConstants.USERNAME_IDENTIFIER);
            if (StringUtils.isBlank(userName)) {
                throw new SSOException(SSOConstants.USERNAME_IDENTIFIER + "����Ϊ��!");
            }

            this.redirectUrl = ServletRequestUtils.getStringParameter(request, SSOConstants.REDIRECT_URL_IDENTIFIER);
            // �������Դϵͳ��Կ
            this.sourcePrivateKey = SSOUtils.getSourcePrivateKey(source);

            // ��ȡ�����û���
            try {
                this.plainUserName = new String(AESUtils.decryptHexString(userName, sourcePrivateKey),
                        StandardCharsets.UTF_8);
            } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException | DecoderException e) {
                throw new SSOException("�����û���ʧ��!");
            }

            this.extraParams = new HashMap(request.getParameterMap());

            // ȥ���̶��ı�ʶ��
            this.extraParams.remove(SSOConstants.KEY_IDENTIFIER);
            this.extraParams.remove(SSOConstants.USERNAME_IDENTIFIER);
            this.extraParams.remove(SSOConstants.REDIRECT_URL_IDENTIFIER);
            this.extraParams.remove(SSOConstants.SOURCE_IDENTIFIER);
        } catch (ServletRequestBindingException e) {
            throw new SSOException("������ʼ��ʧ��!");
        }

    }

    private HttpServletRequest request;

    private HttpServletResponse response;

    private String source;

    private String userName;

    private String key;

    /**
     * ����
     */
    private String plainUserName;

    /**
     * ϵͳ��Կ
     *
     * @Description ͨ��source��Ӧ����Կ����MD5���ܺ���
     */
    private String sourcePrivateKey;

    private String redirectUrl;

    private Map extraParams;

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public Map getExtraParams() {
        return extraParams;
    }

    public void setExtraParams(Map extraParams) {
        this.extraParams = extraParams;
    }

    public String getPlainUserName() {
        return plainUserName;
    }

    public void setPlainUserName(String plainUserName) {
        this.plainUserName = plainUserName;
    }

    public String getSourcePrivateKey() {
        return sourcePrivateKey;
    }

    public void setSourcePrivateKey(String sourcePrivateKey) {
        this.sourcePrivateKey = sourcePrivateKey;
    }

    @Override
    public String toString() {
        return "SSODefinition{" + "request=" + request + ", response=" + response + ", source='" + source + '\''
                + ", userName='" + userName + '\'' + ", key='" + key + '\'' + ", plainUserName='" + plainUserName + '\''
                + ", sourcePrivateKey='" + sourcePrivateKey + '\'' + ", redirectUrl='" + redirectUrl + '\''
                + ", extraParams=" + extraParams + '}';
    }
}