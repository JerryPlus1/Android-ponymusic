
package me.wcy.music.net.datasource;

import static me.wcy.music.utils.ModelExKt.SCHEME_NETEASE;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.AssetDataSource;
import androidx.media3.datasource.ContentDataSource;
import androidx.media3.datasource.DataSchemeDataSource;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSpec;
import androidx.media3.datasource.DefaultDataSource;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.datasource.FileDataSource;
import androidx.media3.datasource.HttpDataSource;
import androidx.media3.datasource.RawResourceDataSource;
import androidx.media3.datasource.TransferListener;
import androidx.media3.datasource.UdpDataSource;

import com.google.errorprone.annotations.CanIgnoreReturnValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import me.wcy.music.net.HttpClient;
import okhttp3.Call;


@UnstableApi
public final class MusicDataSource implements DataSource {

    
    public static final class Factory implements DataSource.Factory {

        private final Context context;
        private final DataSource.Factory baseDataSourceFactory;
        @Nullable
        private TransferListener transferListener;

        
        public Factory(Context context) {
            this(context, new DefaultHttpDataSource.Factory());
        }

        
        public Factory(Context context, DataSource.Factory baseDataSourceFactory) {
            this.context = context.getApplicationContext();
            this.baseDataSourceFactory = baseDataSourceFactory;
        }

        
        @CanIgnoreReturnValue
        @UnstableApi
        public Factory setTransferListener(@Nullable TransferListener transferListener) {
            this.transferListener = transferListener;
            return this;
        }

        @UnstableApi
        @Override
        public MusicDataSource createDataSource() {
            MusicDataSource dataSource =
                    new MusicDataSource(context, baseDataSourceFactory.createDataSource());
            if (transferListener != null) {
                dataSource.addTransferListener(transferListener);
            }
            return dataSource;
        }
    }

    private static final String TAG = "DefaultDataSource";

    private static final String SCHEME_ASSET = "asset";
    private static final String SCHEME_CONTENT = "content";
    private static final String SCHEME_RTMP = "rtmp";
    private static final String SCHEME_UDP = "udp";
    private static final String SCHEME_DATA = DataSchemeDataSource.SCHEME_DATA;

    @SuppressWarnings("deprecation") // Detecting deprecated scheme.
    private static final String SCHEME_RAW = RawResourceDataSource.RAW_RESOURCE_SCHEME;

    private static final String SCHEME_ANDROID_RESOURCE = ContentResolver.SCHEME_ANDROID_RESOURCE;

    private final Context context;
    private final List<TransferListener> transferListeners;
    private final DataSource baseDataSource;

    // Lazily initialized.
    @Nullable
    private DataSource fileDataSource;
    @Nullable
    private DataSource assetDataSource;
    @Nullable
    private DataSource contentDataSource;
    @Nullable
    private DataSource rtmpDataSource;
    @Nullable
    private DataSource udpDataSource;
    @Nullable
    private DataSource dataSchemeDataSource;
    @Nullable
    private DataSource rawResourceDataSource;
    @Nullable
    private DataSource neteaseDataSource;

    @Nullable
    private DataSource dataSource;

    
    @UnstableApi
    public MusicDataSource(Context context, boolean allowCrossProtocolRedirects) {
        this(
                context,
                 null,
                DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                allowCrossProtocolRedirects);
    }

    
    @UnstableApi
    public MusicDataSource(
            Context context, @Nullable String userAgent, boolean allowCrossProtocolRedirects) {
        this(
                context,
                userAgent,
                DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                allowCrossProtocolRedirects);
    }

    
    @UnstableApi
    public MusicDataSource(
            Context context,
            @Nullable String userAgent,
            int connectTimeoutMillis,
            int readTimeoutMillis,
            boolean allowCrossProtocolRedirects) {
        this(
                context,
                new DefaultHttpDataSource.Factory()
                        .setUserAgent(userAgent)
                        .setConnectTimeoutMs(connectTimeoutMillis)
                        .setReadTimeoutMs(readTimeoutMillis)
                        .setAllowCrossProtocolRedirects(allowCrossProtocolRedirects)
                        .createDataSource());
    }

    
    @UnstableApi
    public MusicDataSource(Context context, DataSource baseDataSource) {
        this.context = context.getApplicationContext();
        this.baseDataSource = Assertions.checkNotNull(baseDataSource);
        transferListeners = new ArrayList<>();
    }

    @UnstableApi
    @Override
    public void addTransferListener(TransferListener transferListener) {
        Assertions.checkNotNull(transferListener);
        baseDataSource.addTransferListener(transferListener);
        transferListeners.add(transferListener);
        maybeAddListenerToDataSource(fileDataSource, transferListener);
        maybeAddListenerToDataSource(assetDataSource, transferListener);
        maybeAddListenerToDataSource(contentDataSource, transferListener);
        maybeAddListenerToDataSource(rtmpDataSource, transferListener);
        maybeAddListenerToDataSource(udpDataSource, transferListener);
        maybeAddListenerToDataSource(dataSchemeDataSource, transferListener);
        maybeAddListenerToDataSource(rawResourceDataSource, transferListener);
    }

    @UnstableApi
    @Override
    public long open(DataSpec dataSpec) throws IOException {
        Assertions.checkState(dataSource == null);
        // Choose the correct source for the scheme.
        String scheme = dataSpec.uri.getScheme();
        if (Util.isLocalFileUri(dataSpec.uri)) {
            String uriPath = dataSpec.uri.getPath();
            if (uriPath != null && uriPath.startsWith("/android_asset/")) {
                dataSource = getAssetDataSource();
            } else {
                dataSource = getFileDataSource();
            }
        } else if (SCHEME_ASSET.equals(scheme)) {
            dataSource = getAssetDataSource();
        } else if (SCHEME_CONTENT.equals(scheme)) {
            dataSource = getContentDataSource();
        } else if (SCHEME_RTMP.equals(scheme)) {
            dataSource = getRtmpDataSource();
        } else if (SCHEME_UDP.equals(scheme)) {
            dataSource = getUdpDataSource();
        } else if (SCHEME_DATA.equals(scheme)) {
            dataSource = getDataSchemeDataSource();
        } else if (SCHEME_RAW.equals(scheme) || SCHEME_ANDROID_RESOURCE.equals(scheme)) {
            dataSource = getRawResourceDataSource();
        } else if (SCHEME_NETEASE.equals(scheme)) {
            dataSource = getNeteaseDataSource();
        } else {
            dataSource = baseDataSource;
        }
        // Open the source and return.
        return dataSource.open(dataSpec);
    }

    @UnstableApi
    @Override
    public int read(byte[] buffer, int offset, int length) throws IOException {
        return Assertions.checkNotNull(dataSource).read(buffer, offset, length);
    }

    @UnstableApi
    @Override
    @Nullable
    public Uri getUri() {
        return dataSource == null ? null : dataSource.getUri();
    }

    @UnstableApi
    @Override
    public Map<String, List<String>> getResponseHeaders() {
        return dataSource == null ? Collections.emptyMap() : dataSource.getResponseHeaders();
    }

    @UnstableApi
    @Override
    public void close() throws IOException {
        if (dataSource != null) {
            try {
                dataSource.close();
            } finally {
                dataSource = null;
            }
        }
    }

    private DataSource getUdpDataSource() {
        if (udpDataSource == null) {
            udpDataSource = new UdpDataSource();
            addListenersToDataSource(udpDataSource);
        }
        return udpDataSource;
    }

    private DataSource getFileDataSource() {
        if (fileDataSource == null) {
            fileDataSource = new FileDataSource();
            addListenersToDataSource(fileDataSource);
        }
        return fileDataSource;
    }

    private DataSource getAssetDataSource() {
        if (assetDataSource == null) {
            assetDataSource = new AssetDataSource(context);
            addListenersToDataSource(assetDataSource);
        }
        return assetDataSource;
    }

    private DataSource getContentDataSource() {
        if (contentDataSource == null) {
            contentDataSource = new ContentDataSource(context);
            addListenersToDataSource(contentDataSource);
        }
        return contentDataSource;
    }

    private DataSource getRtmpDataSource() {
        if (rtmpDataSource == null) {
            try {
                Class<?> clazz = Class.forName("androidx.media3.datasource.rtmp.RtmpDataSource");
                rtmpDataSource = (DataSource) clazz.getConstructor().newInstance();
                addListenersToDataSource(rtmpDataSource);
            } catch (ClassNotFoundException e) {
                // Expected if the app was built without the RTMP extension.
                Log.w(TAG, "Attempting to play RTMP stream without depending on the RTMP extension");
            } catch (Exception e) {
                // The RTMP extension is present, but instantiation failed.
                throw new RuntimeException("Error instantiating RTMP extension", e);
            }
            if (rtmpDataSource == null) {
                rtmpDataSource = baseDataSource;
            }
        }
        return rtmpDataSource;
    }

    private DataSource getDataSchemeDataSource() {
        if (dataSchemeDataSource == null) {
            dataSchemeDataSource = new DataSchemeDataSource();
            addListenersToDataSource(dataSchemeDataSource);
        }
        return dataSchemeDataSource;
    }

    private DataSource getRawResourceDataSource() {
        if (rawResourceDataSource == null) {
            rawResourceDataSource = new RawResourceDataSource(context);
            addListenersToDataSource(rawResourceDataSource);
        }
        return rawResourceDataSource;
    }

    private DataSource getNeteaseDataSource() {
        if (neteaseDataSource == null) {
            neteaseDataSource = new OnlineMusicDataSource((Call.Factory) HttpClient.INSTANCE.getOkHttpClient());
            addListenersToDataSource(neteaseDataSource);
        }
        return neteaseDataSource;
    }

    private void addListenersToDataSource(DataSource dataSource) {
        for (int i = 0; i < transferListeners.size(); i++) {
            dataSource.addTransferListener(transferListeners.get(i));
        }
    }

    private void maybeAddListenerToDataSource(
            @Nullable DataSource dataSource, TransferListener listener) {
        if (dataSource != null) {
            dataSource.addTransferListener(listener);
        }
    }
}
