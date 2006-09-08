require gst-plugins.inc
DEPENDS += "gst-plugins-base libid3tag libmad mpeg2dec liba52 lame"
PR = "r2"

SRC_URI += " file://lame-autoconf.patch;patch=1"
EXTRA_OECONF += "--with-plugins=a52dec,lame,id3tag,mad,mpeg2dec,mpegstream,mpegaudioparse,asfdemux,realmedia"

