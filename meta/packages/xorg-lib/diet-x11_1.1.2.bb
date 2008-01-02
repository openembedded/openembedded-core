require libx11_${PV}.bb

SRC_URI += "file://X18NCMSstubs.diff;patch=1 \
            file://fix-disable-xlocale.diff;patch=1 \
            file://fix-utf8-wrong-define.patch;patch=1"

EXTRA_OECONF += "--disable-udc --disable-xcms --disable-xlocale --disable-xkb --with-keysymdef=${STAGING_INCDIR}/X11/keysymdef.h"
CFLAGS += "-D_GNU_SOURCE"
