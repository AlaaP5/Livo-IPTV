export interface OriginalUploadMetadata {
    fileName: string;
    generatedPath: string;
    fullPath: string;
    size: number;     
    contentType: string;
    height: number;
    width: number;
    bitrate: number;
    ext: string;
}


export interface VideoVariant {
    resolution: string;
    width: number;
    height: number;
    bitrate: number;
    playlistPath: string;
    segmentPath: string;
}


export interface TranscodeMetaData {
    masterPlaylist: string;
    variants: VideoVariant[];
}