require gst-plugins.inc
DEPENDS += "gst-plugins-base gconf cairo jpeg libpng gtk+ zlib libid3tag flac \
	    speex"
PR = "r2"

EXTRA_OECONF += " --with-plugins=ximagesrc,cairo,flac,gconfelements,gdkpixbuf,jpeg,png,speex,taglib,avi,matroska,videofilter,autodetect,debug --disable-aalib --disable-esd --disable-shout2"

