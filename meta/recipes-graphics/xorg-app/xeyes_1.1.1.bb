require xorg-app-common.inc
PE = "1"

LIC_FILES_CHKSUM = "file://COPYING;md5=3ea51b365051ac32d1813a7dbaa4bfc6"

SRC_URI[md5sum] = "a3035dcecdbdb89e864177c080924981"
SRC_URI[sha256sum] = "975e98680cd59e1f9439016386609546ed08c284d0f05a95276f96aca6e8a521"

DEPENDS += " virtual/libx11 libxau libxt libxext libxmu"
