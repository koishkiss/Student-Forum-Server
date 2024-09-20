package student.forum.model.ENUM;

import lombok.AllArgsConstructor;
import student.forum.model.CONSTANT.VALUE;

@AllArgsConstructor
public enum FileType {
    IMAGE(new String[]{
            ".png",
            ".PNG",
            ".jpg",
            ".JPG",
            ".jpeg",
            ".JPEG",
            ".gif",
            ".GIF",
            ".bmp",
            ".BMP"
    },
        5242880L, //图片大小最大限制(5MB)
        VALUE.img_local,
        VALUE.img_web
    ),
    VIDEO(new String[]{
            ".avi",
            ".AVI",
            ".mp4",
            ".MP4",
            ".mkv",
            ".MKV",
            ".wmv",
            ".WMV"
    },
        104857600L, //视频大小最大限制(100MB)
        VALUE.video_local,
        VALUE.video_web
    ),
    AUDIO(new String[]{
            ".mp3",
            ".MP3",
            ".wav",
            ".WAV",
            ".m4a",
            ".M4A",
            ".flac",
            ".FLAC",
            ".Ogg",
            ".ogg"
    },
        10485760L, //音频大小最大限制(10MB)
        VALUE.audio_local,
        VALUE.audio_web
    );

    public final String[] suffix;
    public final long max_size;
    public final String local_path;
    public final String web_path;

}
