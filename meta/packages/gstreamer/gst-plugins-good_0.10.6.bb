require gst-plugins.inc
DEPENDS += "gst-plugins-base gconf cairo jpeg libpng gtk+ zlib libid3tag flac \
	    speex"

EXTRA_OECONF += " --with-plugins=ximagesrc,cairo,flac,gconfelements,gdkpixbuf,jpeg,png,speex,id3demux,avi,matroska,videofilter,autodetect,debug,alpha,auparse,flx,cutter,law,level,multipart,qtdemux,rtp,rtsp,udp,videobox,videocrop,videomixer,wavenc,wavparse,audiofx --disable-aalib --disable-esd --disable-shout2 --disable-libcaca --without-check"
