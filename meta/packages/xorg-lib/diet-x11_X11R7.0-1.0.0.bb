SECTION = "x11/base"
require libx11_${PV}.bb

EXTRA_OECONF += "--disable-udc --disable-xcms --disable-xlocale --disable-xkb"
CFLAGS += "-D_GNU_SOURCE"

SRC_URI += "file://X18NCMSstubs.diff;patch=1 \
	    file://fix-disable-xlocale.diff;patch=1 \
            file://fix-utf8-wrong-define.patch;patch=1"

#do_stage_append () {
#	rm -f ${STAGING_INCDIR}/X11/XKBlib.h
#}
